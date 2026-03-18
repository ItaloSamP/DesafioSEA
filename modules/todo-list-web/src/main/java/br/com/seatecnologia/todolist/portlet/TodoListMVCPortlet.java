package br.com.seatecnologia.todolist.portlet;

import br.com.seatecnologia.todolist.model.Category;
import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.service.CategoryLocalServiceUtil;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
    immediate = true,
    property = {
        "com.liferay.portlet.display-category=category.sample",
        "com.liferay.portlet.instanceable=true",
        "javax.portlet.display-name=Todo List",
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user"
    },
    service = Portlet.class
)
public class TodoListMVCPortlet extends MVCPortlet {

    public static final String PORTLET_NAME =
        "br_com_seatecnologia_todolist_portlet_TodoListMVCPortlet";

    private static final Log _log = LogFactoryUtil.getLog(TodoListMVCPortlet.class);

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        try {
            ThemeDisplay themeDisplay =
                (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

            long userId  = themeDisplay.getUserId();
            long groupId = themeDisplay.getScopeGroupId();

            renderRequest.setAttribute("userId", userId);
            renderRequest.setAttribute("userName", themeDisplay.getUser().getFullName());

            // Carrega categorias do usuário (usada na view.jsp e no edit_task.jsp)
            List<Category> categories =
                CategoryLocalServiceUtil.getCategoriesByUserId(groupId, userId);
            renderRequest.setAttribute("categories", categories);

            // Todas as tarefas ativas (sem filtro) — base para contadores
            List<Task> allTasks =
                TaskLocalServiceUtil.getActiveTasksByUserId(groupId, userId);

            // Aplica filtro de status/categoria conforme parâmetro da URL
            String filter = ParamUtil.getString(renderRequest, "filter", "all");
            renderRequest.setAttribute("filter", filter);

            List<Task> tasks;
            if ("pending".equals(filter)) {
                tasks = allTasks.stream()
                    .filter(t -> !t.getIsCompleted())
                    .collect(Collectors.toList());
            } else if ("done".equals(filter)) {
                tasks = allTasks.stream()
                    .filter(t -> t.getIsCompleted())
                    .collect(Collectors.toList());
            } else if ("no-category".equals(filter)) {
                tasks = allTasks.stream()
                    .filter(t -> t.getCategoryId() == 0)
                    .collect(Collectors.toList());
            } else if (filter.startsWith("categoryId:")) {
                long catId = Long.parseLong(filter.substring("categoryId:".length()));
                tasks = allTasks.stream()
                    .filter(t -> t.getCategoryId() == catId)
                    .collect(Collectors.toList());
            } else {
                tasks = allTasks;
            }

            renderRequest.setAttribute("tasks", tasks);

            // Contadores por categoria (sempre calculado sobre TODAS as tarefas ativas)
            Map<String, Long> categoryCounters = new LinkedHashMap<>();
            for (Category cat : categories) {
                long count = allTasks.stream()
                    .filter(t -> t.getCategoryId() == cat.getCategoryId())
                    .count();
                if (count > 0) {
                    categoryCounters.put(cat.getName(), count);
                }
            }
            long noCategoryCount = allTasks.stream()
                .filter(t -> t.getCategoryId() == 0)
                .count();
            categoryCounters.put("Sem categoria", noCategoryCount);

            renderRequest.setAttribute("categoryCounters", categoryCounters);

        } catch (Exception e) {
            _log.error("Erro ao carregar dados do portlet", e);
        }

        super.doView(renderRequest, renderResponse);
    }
}
