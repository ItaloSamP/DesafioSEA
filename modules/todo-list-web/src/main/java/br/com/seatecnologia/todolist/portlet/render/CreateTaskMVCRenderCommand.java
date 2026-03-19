package br.com.seatecnologia.todolist.portlet.render;

import br.com.seatecnologia.todolist.model.Category;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.CategoryLocalServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * Renderiza o formulário de criação de nova tarefa.
 * Carrega as categorias do usuário para popular o select.
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + TodoListMVCPortlet.PORTLET_NAME,
        "mvc.command.name=/todolist/create_task"
    },
    service = MVCRenderCommand.class
)
public class CreateTaskMVCRenderCommand implements MVCRenderCommand {

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) {

        ThemeDisplay themeDisplay =
            (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long userId  = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        try {
            List<Category> categories =
                CategoryLocalServiceUtil.getCategoriesByUserId(groupId, userId);
            renderRequest.setAttribute("categories", categories);
        } catch (Exception e) {
            _log.warn("Erro ao carregar categorias para nova tarefa", e);
        }

        return "/edit_task.jsp";
    }

    private static final Log _log =
        LogFactoryUtil.getLog(CreateTaskMVCRenderCommand.class);
}
