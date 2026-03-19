package br.com.seatecnologia.todolist.portlet.render;

import br.com.seatecnologia.todolist.model.Comment;
import br.com.seatecnologia.todolist.model.Subtask;
import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.CommentLocalServiceUtil;
import br.com.seatecnologia.todolist.service.SubtaskLocalServiceUtil;
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
        "mvc.command.name=/todolist/task_detail"
    },
    service = MVCRenderCommand.class
)
public class TaskDetailMVCRenderCommand implements MVCRenderCommand {

    private static final Log _log =
        LogFactoryUtil.getLog(TaskDetailMVCRenderCommand.class);

    @Override
    public String render(
            RenderRequest renderRequest, RenderResponse renderResponse) {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        if (!themeDisplay.isSignedIn()) {
            return "/task_detail.jsp";
        }

        long userId = themeDisplay.getUserId();
        long taskId = ParamUtil.getLong(renderRequest, "taskId");

        try {
            Task task = TaskLocalServiceUtil.getTask(taskId);

            if (task.getUserId() == userId) {
                List<Subtask> subtasks =
                    SubtaskLocalServiceUtil.getSubtasksByTaskId(taskId);
                List<Comment> comments =
                    CommentLocalServiceUtil.getCommentsByTaskId(taskId);

                renderRequest.setAttribute("task", task);
                renderRequest.setAttribute("subtasks", subtasks);
                renderRequest.setAttribute("comments", comments);
            } else {
                _log.warn("Acesso negado: userId=" + userId +
                    " tentou acessar taskId=" + taskId);
            }
        } catch (Exception e) {
            _log.warn("Tarefa não encontrada para taskId=" + taskId, e);
        }

        return "/task_detail.jsp";
    }
}
