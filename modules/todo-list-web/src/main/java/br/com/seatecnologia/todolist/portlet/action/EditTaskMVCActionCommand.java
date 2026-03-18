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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * Responsável por receber o formulário de edição de tarefa e salvar as alterações.
 * Verifica obrigatoriamente se o usuário logado é o dono da tarefa (ownership check).
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/edit_task"
    },
    service = MVCActionCommand.class
)
public class EditTaskMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId = themeDisplay.getUserId();
        long taskId = ParamUtil.getLong(actionRequest, "taskId");

        // Busca a tarefa no banco antes de qualquer operação
        Task task = TaskLocalServiceUtil.getTask(taskId);

        // OWNERSHIP CHECK: só o dono pode editar sua própria tarefa
        if (task.getUserId() != userId) {
            SessionErrors.add(actionRequest, "task-not-authorized");
            return;
        }

        String title       = ParamUtil.getString(actionRequest, "title").trim();
        String description = ParamUtil.getString(actionRequest, "description");
        String dueDateStr  = ParamUtil.getString(actionRequest, "dueDate");

        // Validação: título obrigatório
        if (Validator.isNull(title)) {
            SessionErrors.add(actionRequest, "task-title-required");
            actionResponse.setRenderParameter("mvcPath", "/edit_task.jsp");
            actionResponse.setRenderParameter("taskId", String.valueOf(taskId));
            return;
        }

        // Parseia a data
        Date dueDate = null;
        if (Validator.isNotNull(dueDateStr)) {
            try {
                dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateStr);
            } catch (Exception e) {
                SessionErrors.add(actionRequest, "task-duedate-invalid");
                actionResponse.setRenderParameter("mvcPath", "/edit_task.jsp");
                actionResponse.setRenderParameter("taskId", String.valueOf(taskId));
                return;
            }
        }

        TaskLocalServiceUtil.updateTask(taskId, title, description, dueDate, task.getImageId());

        SessionMessages.add(actionRequest, "task-updated");
    }
}
