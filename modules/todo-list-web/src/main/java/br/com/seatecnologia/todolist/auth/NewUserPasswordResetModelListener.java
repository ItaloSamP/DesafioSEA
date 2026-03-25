package br.com.seatecnologia.todolist.auth;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = ModelListener.class)
public class NewUserPasswordResetModelListener extends BaseModelListener<User> {

    @Override
    public void onAfterCreate(User user) throws ModelListenerException {
        _clearPasswordReset(user);
    }

    private void _clearPasswordReset(User user) throws ModelListenerException {
        if ((user == null) || user.isDefaultUser() || !user.isPasswordReset()) {
            return;
        }

        try {
            user.setPasswordReset(false);
            _userLocalService.updateUser(user);
        }
        catch (Exception exception) {
            _log.error(
                "Nao foi possivel desabilitar passwordReset para o usuario " +
                    user.getUserId(),
                exception);

            throw new ModelListenerException(exception);
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
        NewUserPasswordResetModelListener.class);

    @Reference
    private UserLocalService _userLocalService;
}
