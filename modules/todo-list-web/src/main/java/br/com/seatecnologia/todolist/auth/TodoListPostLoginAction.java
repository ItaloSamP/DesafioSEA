package br.com.seatecnologia.todolist.auth;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    immediate = true,
    property = "key=login.events.post",
    service = LifecycleAction.class
)
public class TodoListPostLoginAction extends Action {

    @Override
    public void run(
            HttpServletRequest request, HttpServletResponse response)
        throws ActionException {

        try {
            User user = PortalUtil.getUser(request);

            if ((user == null) || user.isDefaultUser() || !user.isPasswordReset()) {
                return;
            }

            user.setPasswordReset(false);
            _userLocalService.updateUser(user);
        }
        catch (Exception exception) {
            _log.error("Falha ao liberar o usuario apos login", exception);

            throw new ActionException(exception);
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
        TodoListPostLoginAction.class);

    @Reference
    private UserLocalService _userLocalService;
}
