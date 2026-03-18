package br.com.seatecnologia.todolist.portlet.render;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

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

        long taskId = ParamUtil.getLong(renderRequest, "taskId");

        if (taskId > 0) {
            try {
                ThemeDisplay themeDisplay =
                    (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

                Task task = TaskLocalServiceUtil.getTask(taskId);

                if (task.getUserId() == themeDisplay.getUserId()) {
                    renderRequest.setAttribute("task", task);
                } else {
                    _log.warn("Usuário " + themeDisplay.getUserId() +
                        " tentou editar tarefa " + taskId + " de outro usuário.");
                }
            } catch (Exception e) {
                _log.warn("Tarefa não encontrada: " + taskId, e);
            }
        }

        return "/edit_task.jsp";
    }

    private static final Log _log = LogFactoryUtil.getLog(EditTaskMVCRenderCommand.class);
}
