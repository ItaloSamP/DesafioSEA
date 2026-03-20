package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.model.Subtask;
import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.SubtaskLocalServiceUtil;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * Remove uma subtarefa.
 * Verifica ownership via tarefa pai (Subtask não tem userId).
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/delete_subtask"
    },
    service = MVCActionCommand.class
)
public class DeleteSubtaskMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        if (!themeDisplay.isSignedIn()) {
            SessionErrors.add(actionRequest, "authentication-required");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        long userId    = themeDisplay.getUserId();
        long subtaskId = ParamUtil.getLong(actionRequest, "subtaskId");
        long taskId    = ParamUtil.getLong(actionRequest, "taskId");

        // Redireciona sempre para o detalhe da tarefa pai
        actionResponse.setRenderParameter("mvcRenderCommandName", "/todolist/task_detail");
        actionResponse.setRenderParameter("taskId", String.valueOf(taskId));

        // Busca a subtarefa para obter o taskId real (não confiar só no param)
        Subtask subtask = SubtaskLocalServiceUtil.getSubtask(subtaskId);

        // Ownership check via tarefa pai
        Task task = TaskLocalServiceUtil.getTask(subtask.getTaskId());
        if (task.getUserId() != userId) {
            SessionErrors.add(actionRequest, "task-not-authorized");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        SubtaskLocalServiceUtil.deleteSubtask(subtaskId);

        SessionMessages.add(actionRequest, "subtask-deleted");
        hideDefaultSuccessMessage(actionRequest);
    }
}
