<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="br.com.seatecnologia.todolist.model.Comment" %>
<%@ page import="br.com.seatecnologia.todolist.model.Subtask" %>
<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.document.library.kernel.service.DLAppLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.repository.model.FileEntry" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%-- Proteção: se não logado, mostra card de acesso restrito.
     NÃO usar response.sendRedirect() em JSP de portlet — a resposta já foi
     iniciada pelo portal e o redirect lança IllegalStateException. --%>
<%
if (!themeDisplay.isSignedIn()) {
%>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-sm text-center p-5">
                <div style="font-size:3rem;">&#128274;</div>
                <h3 class="mt-3 mb-2">Acesso restrito</h3>
                <p class="text-muted mb-4">Fa&#231;a login para acessar sua lista de tarefas.</p>
                <a href="<%= themeDisplay.getURLSignIn() %>" class="btn btn-primary btn-lg">Entrar</a>
            </div>
        </div>
    </div>
</div>
<%
    return;
}

Task task = (Task) renderRequest.getAttribute("task");
%>

<%-- backURL DEVE ser declarado ANTES do null-check de task.
     Portlet tags com var= são variáveis Java locais — se declaradas dentro de um
     bloco if, ficam fora de escopo fora dele, causando "cannot find symbol". --%>
<portlet:renderURL var="backURL" />

<%
if (task == null) {
%>
    <div class="container mt-4">
        <div class="alert alert-danger">Tarefa n&#227;o encontrada ou acesso negado.</div>
        <a href="<%= backURL %>" class="btn btn-secondary">&#8592; Voltar</a>
    </div>
<%
    return;
}

@SuppressWarnings("unchecked")
List<Subtask> subtasks = (List<Subtask>) renderRequest.getAttribute("subtasks");
if (subtasks == null) subtasks = new ArrayList<>();

@SuppressWarnings("unchecked")
List<Comment> comments = (List<Comment>) renderRequest.getAttribute("comments");
if (comments == null) comments = new ArrayList<>();

@SuppressWarnings("unchecked")
List<Category> categories = (List<Category>) renderRequest.getAttribute("categories");
if (categories == null) categories = new ArrayList<>();

Map<Long, String> catNames = new HashMap<>();
for (Category cat : categories) {
    catNames.put(cat.getCategoryId(), cat.getName());
}

String categoryName = catNames.get(task.getCategoryId());
long completedCount = 0;
for (Subtask s : subtasks) {
    if (s.getIsCompleted()) completedCount++;
}
long completedSubtasks = completedCount;
%>

<%-- URLs que dependem de task.getTaskId() — declaradas APÓS o null-check --%>
<portlet:renderURL var="editURL">
    <portlet:param name="mvcRenderCommandName" value="/todolist/edit_task" />
    <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
</portlet:renderURL>

<portlet:actionURL name="/todolist/toggle_task" var="toggleURL">
    <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
</portlet:actionURL>

<portlet:actionURL name="/todolist/add_subtask" var="addSubtaskURL">
    <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
</portlet:actionURL>
<portlet:actionURL name="/todolist/add_comment" var="addCommentURL">
    <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
</portlet:actionURL>
<portlet:actionURL name="/todolist/edit_comment" var="editCommentBaseURL" />

<div class="container mt-3">

    <%-- Mensagens de feedback --%>
    <liferay-ui:success key="subtask-added"     message="subtask-added" />
    <liferay-ui:success key="subtask-toggled"   message="subtask-toggled" />
    <liferay-ui:success key="subtask-deleted"   message="subtask-deleted" />
    <liferay-ui:success key="comment-added"     message="comment-added" />
    <liferay-ui:success key="comment-edited"    message="comment-edited" />
    <liferay-ui:success key="comment-deleted"   message="comment-deleted" />
    <liferay-ui:error   key="task-not-authorized"    message="task-not-authorized" />
    <liferay-ui:error   key="comment-not-authorized" message="comment-not-authorized" />
    <liferay-ui:error   key="subtask-title-required" message="subtask-title-required" />
    <liferay-ui:error   key="comment-text-required"  message="comment-text-required" />
    <liferay-ui:error   key="comment-text-too-long"  message="comment-text-too-long" />

    <a href="<%= backURL %>" class="btn btn-outline-secondary btn-sm mb-3">
        &#8592; Voltar para lista
    </a>

    <%-- Card principal da tarefa --%>
    <div class="card mb-4">
        <div class="card-header d-flex justify-content-between align-items-start">
            <div>
                <h4 class="mb-1">
                    <% if (task.getIsCompleted()) { %>
                        <span class="badge badge-success mr-2">Conclu&#237;da</span>
                    <% } else { %>
                        <span class="badge badge-warning mr-2">Pendente</span>
                    <% } %>
                    <%= HtmlUtil.escape(task.getTitle()) %>
                </h4>
                <% if (categoryName != null) { %>
                    <span class="badge badge-info"><%= HtmlUtil.escape(categoryName) %></span>
                <% } %>
            </div>
            <div class="d-flex" style="gap: 6px;">
                <form method="post" action="<%= toggleURL %>" style="display:inline">
                    <button type="submit" class="btn btn-sm <%= task.getIsCompleted() ? "btn-outline-warning" : "btn-outline-success" %>">
                        <%= task.getIsCompleted() ? "Reabrir" : "Concluir" %>
                    </button>
                </form>
                <a href="<%= editURL %>" class="btn btn-sm btn-outline-secondary">Editar</a>
            </div>
        </div>
        <div class="card-body">
            <% if (task.getDescription() != null && !task.getDescription().isEmpty()) { %>
                <p class="mb-2"><%= HtmlUtil.escape(task.getDescription()) %></p>
            <% } %>
            <% if (task.getDueDate() != null) { %>
                <small class="text-muted">
                    Prazo: <strong><fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" /></strong>
                </small>
            <% } %>
            <%-- Task image --%>
            <% if (task.getImageId() > 0) {
                try {
                    FileEntry imgEntry = DLAppLocalServiceUtil.getFileEntry(task.getImageId());
                    String imgURL = "/documents/" + imgEntry.getRepositoryId() + "/" +
                        imgEntry.getFolderId() + "/" +
                        URLEncoder.encode(imgEntry.getFileName(), "UTF-8").replace("+", "%20");
            %>
                <div class="mt-3">
                    <img src="<%= imgURL %>"
                         alt="Imagem da tarefa"
                         style="max-width:100%; max-height:320px; border-radius:10px; border:1.5px solid #e5e7eb; display:block;" />
                </div>
            <%
                } catch (Exception e) {
                    // image file entry not found — skip silently
                }
            } %>
        </div>
    </div>

    <%-- Subtarefas --%>
    <div class="card mb-4">
        <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0">&#9745; Subtarefas</h5>
            <small class="text-muted"><%= completedSubtasks %> / <%= subtasks.size() %> conclu&#237;das</small>
        </div>
        <div class="card-body">

            <% if (!subtasks.isEmpty()) { %>
                <ul class="list-group mb-3">
                    <% for (Subtask subtask : subtasks) { %>

                    <portlet:actionURL name="/todolist/toggle_subtask" var="toggleSubtaskURL">
                        <portlet:param name="subtaskId" value="<%= String.valueOf(subtask.getSubtaskId()) %>" />
                        <portlet:param name="taskId"    value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:actionURL>

                    <portlet:actionURL name="/todolist/delete_subtask" var="deleteSubtaskURL">
                        <portlet:param name="subtaskId" value="<%= String.valueOf(subtask.getSubtaskId()) %>" />
                        <portlet:param name="taskId"    value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:actionURL>

                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <form method="post" action="<%= toggleSubtaskURL %>" style="display:inline; flex:1;">
                            <button type="submit" class="btn btn-link p-0 text-left" style="text-decoration:none; width:100%;">
                                <% if (subtask.getIsCompleted()) { %>
                                    <span class="badge badge-success mr-2">&#10003;</span>
                                    <span style="text-decoration:line-through; color:#6c757d;"><%= HtmlUtil.escape(subtask.getTitle()) %></span>
                                <% } else { %>
                                    <span class="badge badge-secondary mr-2">&#9675;</span>
                                    <%= HtmlUtil.escape(subtask.getTitle()) %>
                                <% } %>
                            </button>
                        </form>
                        <form method="post" action="<%= deleteSubtaskURL %>" style="display:inline; margin-left:8px;"
                              onsubmit="return confirm('Remover esta subtarefa?')">
                            <button type="submit" class="btn btn-sm btn-outline-danger">&#215;</button>
                        </form>
                    </li>

                    <% } %>
                </ul>
            <% } else { %>
                <p class="text-muted mb-3">Nenhuma subtarefa ainda.</p>
            <% } %>

            <form method="post" action="<%= addSubtaskURL %>" class="d-flex" style="gap:6px;">
                <input type="text" name="<portlet:namespace />title" class="form-control form-control-sm"
                       placeholder="Nova subtarefa..." maxlength="255" required />
                <button type="submit" class="btn btn-sm btn-primary" style="white-space:nowrap;">+ Adicionar</button>
            </form>

        </div>
    </div>

    <%-- Comentários --%>
    <div class="card mb-4">
        <div class="card-header">
            <h5 class="mb-0">&#128172; Coment&#225;rios (<%= comments.size() %>)</h5>
        </div>
        <div class="card-body">

            <% if (comments.isEmpty()) { %>
                <p class="text-muted mb-3">Nenhum comentário ainda.</p>
            <% } %>

            <% for (Comment comment : comments) { %>

            <portlet:actionURL name="/todolist/delete_comment" var="deleteCommentURL">
                <portlet:param name="commentId" value="<%= String.valueOf(comment.getCommentId()) %>" />
                <portlet:param name="taskId"    value="<%= String.valueOf(task.getTaskId()) %>" />
            </portlet:actionURL>

            <portlet:actionURL name="/todolist/edit_comment" var="editCommentURL">
                <portlet:param name="commentId" value="<%= String.valueOf(comment.getCommentId()) %>" />
                <portlet:param name="taskId"    value="<%= String.valueOf(task.getTaskId()) %>" />
            </portlet:actionURL>

            <div class="mb-3 pb-3 border-bottom">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <strong><%= HtmlUtil.escape(comment.getUserName()) %></strong>
                        <small class="text-muted ml-2">
                            <fmt:formatDate value="<%= comment.getCreateDate() %>" pattern="dd/MM/yyyy HH:mm" />
                        </small>
                        <% if (comment.getModifiedDate() != null &&
                               comment.getModifiedDate().after(comment.getCreateDate())) { %>
                            <small class="text-muted ml-1">(editado)</small>
                        <% } %>
                    </div>
                    <% if (comment.getUserId() == themeDisplay.getUserId()) { %>
                        <div style="display:flex; gap:4px;">
                            <button type="button" class="btn btn-sm btn-outline-secondary"
                                    onclick="toggleEditComment(<%= comment.getCommentId() %>)">
                                &#9998; Editar
                            </button>
                            <form method="post" action="<%= deleteCommentURL %>" style="display:inline"
                                  onsubmit="return confirm('Remover este coment&#225;rio?')">
                                <button type="submit" class="btn btn-sm btn-outline-danger">&#215;</button>
                            </form>
                        </div>
                    <% } %>
                </div>

                <%-- Texto do comentário --%>
                <p class="mb-1 mt-1" id="comment-text-<%= comment.getCommentId() %>">
                    <%= HtmlUtil.escape(comment.getText()) %>
                </p>

                <%-- Formulário de edição (oculto por padrão) --%>
                <% if (comment.getUserId() == themeDisplay.getUserId()) { %>
                <form method="post" action="<%= editCommentURL %>"
                      id="edit-form-<%= comment.getCommentId() %>"
                      style="display:none; margin-top:6px;">
                    <input type="hidden" name="<portlet:namespace />commentId" value="<%= comment.getCommentId() %>" />
                    <input type="hidden" name="<portlet:namespace />taskId"    value="<%= task.getTaskId() %>" />
                    <textarea name="<portlet:namespace />text" class="form-control form-control-sm mb-2" rows="2"
                              maxlength="1000" required><%= HtmlUtil.escape(comment.getText()) %></textarea>
                    <div style="display:flex; gap:4px;">
                        <button type="submit" class="btn btn-sm btn-primary">Salvar</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary"
                                onclick="toggleEditComment(<%= comment.getCommentId() %>)">Cancelar</button>
                    </div>
                </form>
                <% } %>

            </div>

            <% } %>

            <%-- Novo comentário --%>
            <form method="post" action="<%= addCommentURL %>">
                <div class="form-group mb-2">
                    <textarea name="<portlet:namespace />text" class="form-control" rows="2"
                              placeholder="Escreva um coment&#225;rio..." maxlength="1000" required></textarea>
                </div>
                <button type="submit" class="btn btn-sm btn-primary">Comentar</button>
            </form>

        </div>
    </div>

</div>

<script>
function toggleEditComment(id) {
    var textEl = document.getElementById('comment-text-' + id);
    var formEl = document.getElementById('edit-form-' + id);
    if (formEl.style.display === 'none') {
        textEl.style.display = 'none';
        formEl.style.display = 'block';
    } else {
        textEl.style.display = 'block';
        formEl.style.display = 'none';
    }
}
</script>
