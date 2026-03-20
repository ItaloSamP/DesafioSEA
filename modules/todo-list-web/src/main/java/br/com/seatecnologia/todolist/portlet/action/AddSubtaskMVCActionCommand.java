package br.com.seatecnologia.todolist.portlet.action;

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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * Adiciona uma subtarefa (item de checklist) a uma tarefa existente.
 * Verifica ownership da tarefa pai antes de criar.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/add_subtask"
    },
    service = MVCActionCommand.class
)
public class AddSubtaskMVCActionCommand extends BaseMVCActionCommand {

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

        long userId = themeDisplay.getUserId();
        long taskId = ParamUtil.getLong(actionRequest, "taskId");
        String title = ParamUtil.getString(actionRequest, "title").trim();

        // Redireciona sempre para o detalhe da tarefa pai
        actionResponse.setRenderParameter("mvcRenderCommandName", "/todolist/task_detail");
        actionResponse.setRenderParameter("taskId", String.valueOf(taskId));

        if (Validator.isNull(title)) {
            SessionErrors.add(actionRequest, "subtask-title-required");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        // Ownership check: subtarefa não tem userId, então verificamos a task pai
        Task task = TaskLocalServiceUtil.getTask(taskId);
        if (task.getUserId() != userId) {
            SessionErrors.add(actionRequest, "task-not-authorized");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        SubtaskLocalServiceUtil.addSubtask(taskId, title);

        SessionMessages.add(actionRequest, "subtask-added");
        hideDefaultSuccessMessage(actionRequest);
    }
}
