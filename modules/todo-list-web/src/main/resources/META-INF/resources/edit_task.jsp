<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<%
// O portlet carrega a tarefa no doView quando taskId está presente.
// Se "task" estiver no request → modo edição. Senão → modo criação.
Task task = (Task) renderRequest.getAttribute("task");

boolean isEdit       = (task != null);
long    taskId       = isEdit ? task.getTaskId() : 0;
String  titleValue   = isEdit ? HtmlUtil.escapeAttribute(task.getTitle()) : "";
String  descValue    = isEdit && task.getDescription() != null
                          ? HtmlUtil.escape(task.getDescription()) : "";
long    catIdValue   = isEdit ? task.getCategoryId() : 0;

// Formata a data para o input type="date" (espera yyyy-MM-dd)
String dueDateValue = "";
if (isEdit && task.getDueDate() != null) {
    dueDateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate());
}

// Categorias disponíveis para o select
@SuppressWarnings("unchecked")
List<Category> categories = (List<Category>) renderRequest.getAttribute("categories");
if (categories == null) categories = new java.util.ArrayList<>();

// Define para qual action command o formulário vai submeter
String actionCommand = isEdit ? "/todolist/edit_task" : "/todolist/add_task";
%>

<portlet:actionURL name="<%= actionCommand %>" var="submitURL" />
<portlet:renderURL var="backURL" />

<div class="container-fluid mt-3" style="max-width: 600px;">

    <h2><%= isEdit ? "Editar Tarefa" : "Nova Tarefa" %></h2>

    <liferay-ui:error key="task-title-required"   message="task-title-required" />
    <liferay-ui:error key="task-duedate-invalid"  message="task-duedate-invalid" />
    <liferay-ui:error key="task-not-authorized"   message="task-not-authorized" />

    <form action="<%= submitURL %>" method="post">

        <input type="hidden" name="<portlet:namespace />taskId" value="<%= taskId %>" />

        <div class="form-group">
            <label for="<portlet:namespace />title"><strong>Título *</strong></label>
            <input
                type="text"
                id="<portlet:namespace />title"
                name="<portlet:namespace />title"
                class="form-control"
                placeholder="Ex: Estudar Service Builder"
                value="<%= titleValue %>"
                required
            />
        </div>

        <div class="form-group">
            <label for="<portlet:namespace />description">Descrição</label>
            <textarea
                id="<portlet:namespace />description"
                name="<portlet:namespace />description"
                class="form-control"
                rows="3"
                placeholder="Detalhes opcionais sobre a tarefa..."
            ><%= descValue %></textarea>
        </div>

        <div class="form-group">
            <label for="<portlet:namespace />dueDate">Prazo</label>
            <input
                type="date"
                id="<portlet:namespace />dueDate"
                name="<portlet:namespace />dueDate"
                class="form-control"
                value="<%= dueDateValue %>"
            />
        </div>

        <div class="form-group">
            <label for="<portlet:namespace />categoryId">Categoria</label>
            <select
                id="<portlet:namespace />categoryId"
                name="<portlet:namespace />categoryId"
                class="form-control"
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

        <div class="mt-3">
            <button type="submit" class="btn btn-primary">
                <%= isEdit ? "Salvar Alterações" : "Criar Tarefa" %>
            </button>
            <a href="<%= backURL %>" class="btn btn-secondary ml-2">Cancelar</a>
        </div>

    </form>
</div>
