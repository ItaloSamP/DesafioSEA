<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>

<%
// O portlet carrega a tarefa no doView quando taskId está presente.
// Se "task" estiver no request → modo edição. Senão → modo criação.
Task task = (Task) renderRequest.getAttribute("task");

boolean isEdit     = (task != null);
long    taskId     = isEdit ? task.getTaskId() : 0;
String  titleValue = isEdit ? HtmlUtil.escapeAttribute(task.getTitle()) : "";
String  descValue  = isEdit && task.getDescription() != null
                        ? HtmlUtil.escape(task.getDescription()) : "";

// Formata a data para o input type="date" (espera yyyy-MM-dd)
String dueDateValue = "";
if (isEdit && task.getDueDate() != null) {
    dueDateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate());
}

// Define para qual action command o formulário vai submeter
String actionCommand = isEdit ? "/todolist/edit_task" : "/todolist/add_task";
%>

<portlet:actionURL name="<%= actionCommand %>" var="submitURL" />
<portlet:renderURL var="backURL" />

<div class="container-fluid mt-3" style="max-width: 600px;">

    <h2><%= isEdit ? "Editar Tarefa" : "Nova Tarefa" %></h2>

    <liferay-ui:error key="task-title-required"   message="O título é obrigatório." />
    <liferay-ui:error key="task-duedate-invalid"  message="Data de prazo inválida." />
    <liferay-ui:error key="task-not-authorized"   message="Você não tem permissão para modificar esta tarefa." />

    <form action="<%= submitURL %>" method="post">

        <%-- taskId é necessário apenas na edição, mas enviamos sempre (0 no modo criação) --%>
        <input type="hidden" name="taskId" value="<%= taskId %>" />

        <div class="form-group">
            <label for="title"><strong>Título *</strong></label>
            <input
                type="text"
                id="title"
                name="title"
                class="form-control"
                placeholder="Ex: Estudar Service Builder"
                value="<%= titleValue %>"
                required
            />
        </div>

        <div class="form-group">
            <label for="description">Descrição</label>
            <textarea
                id="description"
                name="description"
                class="form-control"
                rows="3"
                placeholder="Detalhes opcionais sobre a tarefa..."
            ><%= descValue %></textarea>
        </div>

        <div class="form-group">
            <label for="dueDate">Prazo</label>
            <input
                type="date"
                id="dueDate"
                name="dueDate"
                class="form-control"
                value="<%= dueDateValue %>"
            />
        </div>

        <div class="mt-3">
            <button type="submit" class="btn btn-primary">
                <%= isEdit ? "Salvar Alterações" : "Criar Tarefa" %>
            </button>
            <a href="<%= backURL %>" class="btn btn-secondary ml-2">Cancelar</a>
        </div>

    </form>
</div>
