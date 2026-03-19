package br.com.seatecnologia.todolist.portlet.action;

import br.com.seatecnologia.todolist.model.Task;
import br.com.seatecnologia.todolist.portlet.TodoListMVCPortlet;
import br.com.seatecnologia.todolist.service.TaskLocalServiceUtil;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

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

        if (!themeDisplay.isSignedIn()) {
            SessionErrors.add(actionRequest, "authentication-required");
            hideDefaultErrorMessage(actionRequest);
            return;
        }

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();

        UploadPortletRequest uploadRequest =
            PortalUtil.getUploadPortletRequest(actionRequest);

        long taskId = ParamUtil.getLong(uploadRequest, "taskId");

        Task task = TaskLocalServiceUtil.getTask(taskId);

        if (task.getUserId() != userId) {
            SessionErrors.add(actionRequest, "task-not-authorized");
            return;
        }

        String title       = ParamUtil.getString(uploadRequest, "title").trim();
        String description = ParamUtil.getString(uploadRequest, "description");
        String dueDateStr  = ParamUtil.getString(uploadRequest, "dueDate");

        if (Validator.isNull(title)) {
            SessionErrors.add(actionRequest, "task-title-required");
            actionResponse.setRenderParameter("mvcPath", "/edit_task.jsp");
            actionResponse.setRenderParameter("taskId", String.valueOf(taskId));
            return;
        }

        Date dueDate = null;
        if (Validator.isNotNull(dueDateStr)) {
            try {
                dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateStr);
            } catch (Exception e) {
                SessionErrors.add(actionRequest, "task-duedate-invalid");
                actionResponse.setRenderParameter("mvcRenderCommandName", "/todolist/edit_task");
                actionResponse.setRenderParameter("taskId", String.valueOf(taskId));
                return;
            }

            try {
                Date today = new SimpleDateFormat("yyyy-MM-dd").parse(
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                if (dueDate.before(today)) {
                    SessionErrors.add(actionRequest, "task-duedate-past");
                    hideDefaultErrorMessage(actionRequest);
                    actionResponse.setRenderParameter("mvcRenderCommandName", "/todolist/edit_task");
                    actionResponse.setRenderParameter("taskId", String.valueOf(taskId));
                    return;
                }
            } catch (Exception e) {
                // formato fixo — não deve ocorrer
            }
        }

        long categoryId = ParamUtil.getLong(uploadRequest, "categoryId");

        // Handle image upload: if new file uploaded, replace old one
        long imageId = task.getImageId();
        try {
            String imageFileName = uploadRequest.getFileName("image");
            if (Validator.isNotNull(imageFileName)) {
                File imageFile = uploadRequest.getFile("image");
                String contentType = uploadRequest.getContentType("image");
                if (imageFile != null && imageFile.length() > 0) {
                    // Delete old image if exists
                    if (imageId > 0) {
                        try {
                            DLAppLocalServiceUtil.deleteFileEntry(imageId);
                        } catch (Exception ex) {
                            _log.warn("Could not delete old image: " + ex.getMessage());
                        }
                    }
                    ServiceContext serviceContext =
                        ServiceContextFactory.getInstance(actionRequest);
                    String uniqueTitle =
                        userId + "_" + System.currentTimeMillis() + "_" + imageFileName;
                    FileEntry fileEntry = DLAppLocalServiceUtil.addFileEntry(
                        userId, groupId,
                        DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
                        imageFileName, contentType,
                        uniqueTitle, "", "",
                        imageFile, serviceContext);
                    imageId = fileEntry.getFileEntryId();
                }
            }
        } catch (Exception e) {
            _log.warn("Failed to upload image for task: " + e.getMessage());
        }

        // Handle image removal
        boolean removeImage = ParamUtil.getBoolean(uploadRequest, "removeImage");
        if (removeImage && imageId > 0) {
            try {
                DLAppLocalServiceUtil.deleteFileEntry(imageId);
            } catch (Exception e) {
                _log.warn("Could not delete image: " + e.getMessage());
            }
            imageId = 0;
        }

        TaskLocalServiceUtil.updateTask(
            taskId, title, description, dueDate, imageId, categoryId);

        SessionMessages.add(actionRequest, "task-updated");
    }

    private static final Log _log =
        LogFactoryUtil.getLog(EditTaskMVCActionCommand.class);
}
