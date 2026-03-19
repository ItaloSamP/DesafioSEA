package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.model.Comment;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.CommentLocalServiceUtil;

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
 * Edita o texto de um comentário existente.
 * Ownership check direto: Comment tem userId, basta comparar comment.getUserId() == userId.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/edit_comment"
    },
    service = MVCActionCommand.class
)
public class EditCommentMVCActionCommand extends BaseMVCActionCommand {

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
        long commentId = ParamUtil.getLong(actionRequest, "commentId");
        long taskId    = ParamUtil.getLong(actionRequest, "taskId");
        String text    = ParamUtil.getString(actionRequest, "text").trim();

        // Redireciona sempre para o detalhe da tarefa
        actionResponse.setRenderParameter("mvcRenderCommandName", "/todolist/task_detail");
        actionResponse.setRenderParameter("taskId", String.valueOf(taskId));

        if (Validator.isNull(text)) {
            SessionErrors.add(actionRequest, "comment-text-required");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        if (text.length() > 1000) {
            SessionErrors.add(actionRequest, "comment-text-too-long");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        // Ownership check direto pelo userId do comentário
        Comment comment = CommentLocalServiceUtil.getComment(commentId);
        if (comment.getUserId() != userId) {
            SessionErrors.add(actionRequest, "comment-not-authorized");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        CommentLocalServiceUtil.updateComment(commentId, text);

        SessionMessages.add(actionRequest, "comment-edited");
        hideDefaultSuccessMessage(actionRequest);
    }
}
