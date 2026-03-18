package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.CategoryLocalServiceUtil;

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
 * Cria uma nova categoria para o usuário logado.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/add_category"
    },
    service = MVCActionCommand.class
)
public class AddCategoryMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws Exception {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId  = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        String name = ParamUtil.getString(actionRequest, "name").trim();

        if (Validator.isNull(name)) {
            SessionErrors.add(actionRequest, "category-name-required");
            hideDefaultErrorMessage(actionRequest);
            actionResponse.setRenderParameter("mvcPath", "/categories.jsp");
            return;
        }

        if (name.length() > 75) {
            SessionErrors.add(actionRequest, "category-name-too-long");
            hideDefaultErrorMessage(actionRequest);
            actionResponse.setRenderParameter("mvcPath", "/categories.jsp");
            return;
        }

        CategoryLocalServiceUtil.addCategory(userId, groupId, name);

        SessionMessages.add(actionRequest, "category-added");
        hideDefaultSuccessMessage(actionRequest);

        actionResponse.setRenderParameter("mvcPath", "/categories.jsp");
    }
}
