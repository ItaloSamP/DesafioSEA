package br.com.seatecnologia.todolist.listener;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
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
        if ((user == null) || user.isDefaultUser() || !user.isPasswordReset()) {
            return;
        }

        try {
            _userLocalService.updatePasswordReset(user.getUserId(), false);
        }
        catch (PortalException portalException) {
            throw new ModelListenerException(
                "Nao foi possivel desabilitar o reset de senha obrigatorio do usuario " +
                    user.getUserId(),
                portalException);
        }
        catch (Exception exception) {
            _log.error(
                "Falha inesperada ao ajustar passwordReset do usuario " +
                    user.getUserId(),
                exception);
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
        NewUserPasswordResetModelListener.class);

    @Reference
    private UserLocalService _userLocalService;
}
