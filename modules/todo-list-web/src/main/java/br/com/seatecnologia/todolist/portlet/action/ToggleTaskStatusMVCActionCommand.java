package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * Alterna o status de uma tarefa entre Pendente e Concluída.
 * A lógica de inversão (! isCompleted) está no TaskLocalServiceImpl.toggleTaskStatus.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/toggle_task"
    },
    service = MVCActionCommand.class
)
public class ToggleTaskStatusMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId = themeDisplay.getUserId();
        long taskId = ParamUtil.getLong(actionRequest, "taskId");

        // Busca a tarefa para verificar ownership
        Task task = TaskLocalServiceUtil.getTask(taskId);

        // OWNERSHIP CHECK: só o dono pode alterar o status
        if (task.getUserId() != userId) {
            SessionErrors.add(actionRequest, "task-not-authorized");
            return;
        }

        // Inverte isCompleted: false → true ou true → false
        TaskLocalServiceUtil.toggleTaskStatus(taskId);

        // Sem SessionMessages — o toggle é uma ação rápida, o feedback visual
        // já vem pelo badge de status na lista de tarefas.
    }
}
