<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.document.library.kernel.service.DLAppLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.repository.model.FileEntry" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.List" %>

<%
Task task = (Task) renderRequest.getAttribute("task");

boolean isEdit       = (task != null);
long    taskId       = isEdit ? task.getTaskId() : 0;
String  titleValue   = isEdit ? HtmlUtil.escapeAttribute(task.getTitle()) : "";
String  descValue    = isEdit && task.getDescription() != null
                          ? HtmlUtil.escape(task.getDescription()) : "";
long    catIdValue   = isEdit ? task.getCategoryId() : 0;

String dueDateValue = "";
if (isEdit && task.getDueDate() != null) {
    dueDateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate());
}
String todayValue = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

@SuppressWarnings("unchecked")
List<Category> categories = (List<Category>) renderRequest.getAttribute("categories");
if (categories == null) categories = new java.util.ArrayList<>();

String actionCommand = isEdit ? "/todolist/edit_task" : "/todolist/add_task";

// Resolve current image URL if editing and has image
String currentImageURL = null;
if (isEdit && task.getImageId() > 0) {
    try {
        FileEntry fe = DLAppLocalServiceUtil.getFileEntry(task.getImageId());
        currentImageURL = "/documents/" + fe.getRepositoryId() + "/" +
            fe.getFolderId() + "/" +
            URLEncoder.encode(fe.getFileName(), "UTF-8").replace("+", "%20");
    } catch (Exception e) {
        // image not found — ignore
    }
}
%>

<portlet:actionURL name="<%= actionCommand %>" var="submitURL" />
<portlet:renderURL var="backURL" />

<style>
.sea-form-wrapper {
    max-width: 600px;
    margin: 2rem auto;
    padding: 0 1rem;
}
.sea-form-card {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.08);
    overflow: hidden;
}
.sea-form-header {
    background: linear-gradient(135deg, #4f46e5, #7c3aed);
    padding: 1.5rem 2rem;
    color: white;
}
.sea-form-title {
    font-size: 1.3rem;
    font-weight: 700;
    margin: 0;
}
.sea-form-body {
    padding: 2rem;
}
.sea-field {
    margin-bottom: 1.25rem;
}
.sea-label {
    display: block;
    font-size: 0.82rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    color: #6b7280;
    margin-bottom: 0.4rem;
}
.sea-input, .sea-textarea, .sea-select {
    width: 100%;
    padding: 0.65rem 0.9rem;
    border: 1.5px solid #e5e7eb;
    border-radius: 10px;
    font-size: 0.92rem;
    color: #111827;
    background: #fafafa;
    transition: border-color 0.2s, box-shadow 0.2s;
    outline: none;
    box-sizing: border-box;
}
.sea-input:focus, .sea-textarea:focus, .sea-select:focus {
    border-color: #a5b4fc;
    box-shadow: 0 0 0 3px rgba(167,139,250,0.15);
    background: #fff;
}
.sea-textarea { resize: vertical; min-height: 90px; }

/* Image upload area */
.sea-upload-area {
    border: 2px dashed #d1d5db;
    border-radius: 12px;
    padding: 1.5rem;
    text-align: center;
    cursor: pointer;
    transition: all 0.2s;
    background: #f9fafb;
    position: relative;
}
.sea-upload-area:hover {
    border-color: #a5b4fc;
    background: #f5f3ff;
}
.sea-upload-area input[type=file] {
    position: absolute;
    inset: 0;
    opacity: 0;
    cursor: pointer;
    width: 100%;
    height: 100%;
}
.sea-upload-icon {
    font-size: 2rem;
    margin-bottom: 0.5rem;
    display: block;
    color: #9ca3af;
}
.sea-upload-text {
    font-size: 0.85rem;
    color: #6b7280;
    margin: 0;
}
.sea-upload-hint {
    font-size: 0.75rem;
    color: #9ca3af;
    margin-top: 0.2rem;
}
/* Current image preview */
.sea-img-preview {
    margin-top: 0.75rem;
    border-radius: 10px;
    overflow: hidden;
    position: relative;
    display: inline-block;
    max-width: 100%;
}
.sea-img-preview img {
    max-width: 100%;
    max-height: 200px;
    display: block;
    border-radius: 10px;
    border: 1.5px solid #e5e7eb;
}
.sea-img-label {
    font-size: 0.78rem;
    color: #6b7280;
    margin-bottom: 0.4rem;
}
.sea-remove-img {
    display: inline-flex;
    align-items: center;
    gap: 0.25rem;
    margin-top: 0.5rem;
    padding: 0.25rem 0.6rem;
    border: 1.5px solid #fecaca;
    border-radius: 7px;
    background: #fff5f5;
    color: #dc2626;
    font-size: 0.78rem;
    cursor: pointer;
}
/* Image name preview on select */
#sea-selected-name {
    margin-top: 0.5rem;
    font-size: 0.8rem;
    color: #4f46e5;
    font-weight: 500;
}

/* Form actions */
.sea-form-footer {
    display: flex;
    gap: 0.75rem;
    align-items: center;
    padding-top: 0.5rem;
}
.sea-submit-btn {
    padding: 0.65rem 1.5rem;
    background: linear-gradient(135deg, #4f46e5, #7c3aed);
    color: white;
    border: none;
    border-radius: 10px;
    font-size: 0.92rem;
    font-weight: 600;
    cursor: pointer;
    box-shadow: 0 3px 10px rgba(79,70,229,0.3);
    transition: all 0.2s;
}
.sea-submit-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 5px 15px rgba(79,70,229,0.4);
}
.sea-cancel-btn {
    padding: 0.65rem 1.2rem;
    background: transparent;
    color: #6b7280;
    border: 1.5px solid #e5e7eb;
    border-radius: 10px;
    font-size: 0.92rem;
    font-weight: 500;
    text-decoration: none;
    transition: all 0.2s;
}
.sea-cancel-btn:hover {
    background: #f9fafb;
    color: #374151;
    text-decoration: none;
}
</style>

<div class="sea-form-wrapper">
    <div class="sea-form-card">

        <div class="sea-form-header">
            <h2 class="sea-form-title">
                <%= isEdit ? "&#9998; Editar Tarefa" : "&#43; Nova Tarefa" %>
            </h2>
        </div>

        <div class="sea-form-body">

            <liferay-ui:error key="task-title-required"   message="task-title-required" />
            <liferay-ui:error key="task-duedate-invalid"  message="task-duedate-invalid" />
            <liferay-ui:error key="task-duedate-past"     message="task-duedate-past" />
            <liferay-ui:error key="task-not-authorized"   message="task-not-authorized" />

            <form action="<%= submitURL %>" method="post" enctype="multipart/form-data">

                <input type="hidden" name="<portlet:namespace />taskId" value="<%= taskId %>" />

                <div class="sea-field">
                    <label class="sea-label" for="<portlet:namespace />title">T&#237;tulo *</label>
                    <input
                        type="text"
                        id="<portlet:namespace />title"
                        name="<portlet:namespace />title"
                        class="sea-input"
                        placeholder="Ex: Estudar Service Builder"
                        value="<%= titleValue %>"
                        required
                    />
                </div>

                <div class="sea-field">
                    <label class="sea-label" for="<portlet:namespace />description">Descri&#231;&#227;o</label>
                    <textarea
                        id="<portlet:namespace />description"
                        name="<portlet:namespace />description"
                        class="sea-textarea"
                        placeholder="Detalhes opcionais sobre a tarefa..."
                    ><%= descValue %></textarea>
                </div>

                <div class="sea-field">
                    <label class="sea-label" for="<portlet:namespace />dueDate">Prazo</label>
                    <input
                        type="date"
                        id="<portlet:namespace />dueDate"
                        name="<portlet:namespace />dueDate"
                        class="sea-input"
                        value="<%= dueDateValue %>"
                        min="<%= todayValue %>"
                    />
                </div>

                <div class="sea-field">
                    <label class="sea-label" for="<portlet:namespace />categoryId">Categoria</label>
                    <select
                        id="<portlet:namespace />categoryId"
                        name="<portlet:namespace />categoryId"
                        class="sea-select"
                    >
                        <option value="0" <%= catIdValue == 0 ? "selected" : "" %>>Sem categoria</option>
                        <% for (Category cat : categories) { %>
                            <option value="<%= cat.getCategoryId() %>"
                                <%= catIdValue == cat.getCategoryId() ? "selected" : "" %>>
                                <%= HtmlUtil.escape(cat.getName()) %>
                            </option>
                        <% } %>
                    </select>
                </div>

                <%-- Image upload field --%>
                <div class="sea-field">
                    <label class="sea-label">Imagem</label>

                    <% if (currentImageURL != null) { %>
                        <p class="sea-img-label">Imagem atual:</p>
                        <div class="sea-img-preview">
                            <img src="<%= currentImageURL %>" alt="Imagem da tarefa" />
                        </div>
                        <label class="sea-remove-img">
                            <input type="checkbox" name="<portlet:namespace />removeImage" value="true"
                                   style="margin-right:4px;" />
                            Remover imagem
                        </label>
                        <p class="sea-img-label" style="margin-top:0.75rem;">Substituir por nova imagem:</p>
                    <% } %>

                    <div class="sea-upload-area" id="sea-upload-area">
                        <input type="file"
                               name="<portlet:namespace />image"
                               id="<portlet:namespace />image"
                               accept="image/png,image/jpeg,image/gif,image/webp"
                               onchange="seaOnFileChange(this)" />
                        <span class="sea-upload-icon">&#128247;</span>
                        <p class="sea-upload-text">Clique ou arraste uma imagem aqui</p>
                        <p class="sea-upload-hint">PNG, JPG, GIF, WEBP &#8226; M&#225;x 10 MB</p>
                    </div>
                    <div id="sea-selected-name"></div>
                </div>

                <div class="sea-form-footer">
                    <button type="submit" class="sea-submit-btn">
                        <%= isEdit ? "Salvar Altera&#231;&#245;es" : "Criar Tarefa" %>
                    </button>
                    <a href="<%= backURL %>" class="sea-cancel-btn">Cancelar</a>
                </div>

            </form>
        </div>
    </div>
</div>

<script>
function seaOnFileChange(input) {
    var nameEl = document.getElementById('sea-selected-name');
    if (input.files && input.files[0]) {
        nameEl.textContent = '✓ ' + input.files[0].name;
        document.getElementById('sea-upload-area').style.borderColor = '#a5b4fc';
        document.getElementById('sea-upload-area').style.background = '#f5f3ff';
    } else {
        nameEl.textContent = '';
    }
}
</script>
