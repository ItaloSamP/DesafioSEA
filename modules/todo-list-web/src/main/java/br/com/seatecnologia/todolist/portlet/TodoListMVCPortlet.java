package br.com.seatecnologia.todolist.portlet;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.List;

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

            long userId = themeDisplay.getUserId();
            long groupId = themeDisplay.getScopeGroupId();

            // Passa dados do usuário para a view
            renderRequest.setAttribute("userId", userId);
            renderRequest.setAttribute("userName", themeDisplay.getUser().getFullName());

            // Carrega a lista de tarefas ativas do usuário e passa para a view
            List<Task> tasks = TaskLocalServiceUtil.getActiveTasksByUserId(groupId, userId);
            renderRequest.setAttribute("tasks", tasks);

        } catch (Exception e) {
            _log.error("Erro ao carregar dados do portlet", e);
        }

        super.doView(renderRequest, renderResponse);
    }
}
