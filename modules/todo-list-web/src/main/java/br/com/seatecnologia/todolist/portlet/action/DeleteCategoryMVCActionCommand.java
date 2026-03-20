package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.model.Category;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.CategoryLocalServiceUtil;

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
 * Remove uma categoria do usuário logado.
 * Verifica ownership antes de deletar.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/delete_category"
    },
    service = MVCActionCommand.class
)
public class DeleteCategoryMVCActionCommand extends BaseMVCActionCommand {

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

        long userId     = themeDisplay.getUserId();
        long categoryId = ParamUtil.getLong(actionRequest, "categoryId");

        Category category = CategoryLocalServiceUtil.getCategory(categoryId);

        // OWNERSHIP CHECK: só o dono pode remover sua categoria
        if (category.getUserId() != userId) {
            SessionErrors.add(actionRequest, "category-not-authorized");
            hideDefaultErrorMessage(actionRequest);
            actionResponse.setRenderParameter("mvcPath", "/categories.jsp");
            return;
        }

        CategoryLocalServiceUtil.deleteCategory(categoryId);

        SessionMessages.add(actionRequest, "category-deleted");
        hideDefaultSuccessMessage(actionRequest);

        actionResponse.setRenderParameter("mvcPath", "/categories.jsp");
    }
}
