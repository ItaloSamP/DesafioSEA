package br.com.seatecnologia.todolist.portlet.action;

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
 * Responsável por receber o formulário de nova tarefa e salvar no banco.
 * O mvc.command.name tem que bater exatamente com o name da portlet:actionURL no JSP.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/add_task"
    },
    service = MVCActionCommand.class
)
public class AddTaskMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId  = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        // ParamUtil.getString é o jeito Liferay de ler parâmetros de form com segurança
        String title       = ParamUtil.getString(actionRequest, "title").trim();
        String description = ParamUtil.getString(actionRequest, "description");
        String dueDateStr  = ParamUtil.getString(actionRequest, "dueDate");

        // Validação: título é obrigatório
        if (Validator.isNull(title)) {
            SessionErrors.add(actionRequest, "task-title-required");
            // Volta pro formulário em vez de renderizar a view principal
            actionResponse.setRenderParameter("mvcPath", "/edit_task.jsp");
            return;
        }

        // Parseia a data (input type="date" envia no formato yyyy-MM-dd)
        Date dueDate = null;
        if (Validator.isNotNull(dueDateStr)) {
            try {
                dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateStr);
            } catch (Exception e) {
                SessionErrors.add(actionRequest, "task-duedate-invalid");
                actionResponse.setRenderParameter("mvcPath", "/edit_task.jsp");
                return;
            }
        }

        // imageId = 0 por enquanto (será usado em sprint futura com upload de imagem)
        TaskLocalServiceUtil.addTask(userId, groupId, title, description, dueDate, 0);

        SessionMessages.add(actionRequest, "task-added");
        // Sem setRenderParameter → vai renderizar a view padrão (view.jsp)
    }
}
