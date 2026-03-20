package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
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
 * Realiza exclusão lógica (soft delete) de uma tarefa.
 * A tarefa NÃO é apagada do banco — apenas marcada com isDeleted=true.
 * Isso preserva o histórico e permite eventual recuperação.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/delete_task"
    },
    service = MVCActionCommand.class
)
public class DeleteTaskMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        if (!themeDisplay.isSignedIn()) {
            SessionErrors.add(actionRequest, "authentication-required");
            return;
        }

        long userId = themeDisplay.getUserId();
        long taskId = ParamUtil.getLong(actionRequest, "taskId");

        // Busca a tarefa para verificar ownership antes de deletar
        Task task = TaskLocalServiceUtil.getTask(taskId);

        // OWNERSHIP CHECK: só o dono pode deletar sua tarefa
        if (task.getUserId() != userId) {
            SessionErrors.add(actionRequest, "task-not-authorized");
            return;
        }

        // Soft delete: não remove do banco, apenas seta isDeleted=true
        TaskLocalServiceUtil.softDeleteTask(taskId);

        SessionMessages.add(actionRequest, "task-deleted");
    }
}
