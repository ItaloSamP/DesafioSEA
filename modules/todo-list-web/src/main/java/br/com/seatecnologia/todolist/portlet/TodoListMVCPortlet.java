package br.com.seatecnologia.todolist.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

@Component(
    immediate = true,
    property = {
        "com.liferay.portlet.display-category=category.sample",
        "com.liferay.portlet.instanceable=true",
        "javax.portlet.display-name=Todo List",
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "javax.portlet.security-role-ref=power-user,user"
    },
    service = Portlet.class
)
public class TodoListMVCPortlet extends MVCPortlet {
    public static final String PORTLET_NAME = "br_com_seatecnologia_todolist_portlet_TodoListMVCPortlet";
    private static final Log _log = LogFactoryUtil.getLog(TodoListMVCPortlet.class);

    /**
     * Sobrescreve doView para capturar dados do usuário logado
     */
    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {
        try {
            // Captura o ThemeDisplay do request
            ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

            if (themeDisplay != null) {
                // Obtém o ID do usuário logado
                long userId = themeDisplay.getUserId();
                String userName = themeDisplay.getUser().getFullName();
                String userEmail = themeDisplay.getUser().getEmailAddress();

                // Passa os dados para a view (JSP)
                renderRequest.setAttribute("userId", userId);
                renderRequest.setAttribute("userName", userName);
                renderRequest.setAttribute("userEmail", userEmail);

                _log.info("Usuário logado - ID: " + userId + ", Nome: " + userName);
            } else {
                _log.warn("ThemeDisplay não encontrado no request");
            }
        } catch (Exception e) {
            _log.error("Erro ao capturar dados do usuário", e);
        }

        // Chama o doView da classe pai para continuar o processamento normal
        super.doView(renderRequest, renderResponse);
    }
}
