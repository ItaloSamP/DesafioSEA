package br.com.seatecnologia.todolist.portlet.render;

import br.com.seatecnologia.todolist.model.Category;
import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.CategoryLocalServiceUtil;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/edit_task"
    },
    service = MVCRenderCommand.class
)
public class EditTaskMVCRenderCommand implements MVCRenderCommand {

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long taskId  = ParamUtil.getLong(renderRequest, "taskId");
        long userId  = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        if (taskId > 0) {
            try {
                Task task = TaskLocalServiceUtil.getTask(taskId);

                if (task.getUserId() == userId) {
                    renderRequest.setAttribute("task", task);
                } else {
                    _log.warn("Usuário " + userId +
                        " tentou editar tarefa " + taskId + " de outro usuário.");
                }
            } catch (Exception e) {
                _log.warn("Tarefa não encontrada: " + taskId, e);
            }
        }

        // Carrega categorias para popular o select no formulário
        try {
            List<Category> categories =
                CategoryLocalServiceUtil.getCategoriesByUserId(groupId, userId);
            renderRequest.setAttribute("categories", categories);
        } catch (Exception e) {
            _log.warn("Erro ao carregar categorias para edição de tarefa", e);
        }

        return "/edit_task.jsp";
    }

    private static final Log _log = LogFactoryUtil.getLog(EditTaskMVCRenderCommand.class);
}
