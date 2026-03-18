<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<%
// O portlet já carregou as tarefas no doView e as colocou no renderRequest
@SuppressWarnings("unchecked")
List<Task> tasks = (List<Task>) renderRequest.getAttribute("tasks");
if (tasks == null) tasks = new java.util.ArrayList<>();

long pendingCount   = tasks.stream().filter(t -> !t.getIsCompleted()).count();
long completedCount = tasks.stream().filter(t ->  t.getIsCompleted()).count();
%>

<portlet:renderURL var="addTaskURL">
    <portlet:param name="mvcPath" value="/edit_task.jsp" />
</portlet:renderURL>

<div class="container-fluid mt-3">

    <%-- Cabeçalho --%>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Minhas Tarefas</h2>
        <a href="<%= addTaskURL %>" class="btn btn-primary">+ Nova Tarefa</a>
    </div>

    <%-- Mensagens de feedback das ações --%>
    <liferay-ui:success key="task-added"   message="Tarefa criada com sucesso!" />
    <liferay-ui:success key="task-updated" message="Tarefa atualizada com sucesso!" />
    <liferay-ui:success key="task-deleted" message="Tarefa removida." />
    <liferay-ui:error   key="task-not-authorized" message="Você não tem permissão para modificar esta tarefa." />

    <%-- Resumo --%>
    <% if (!tasks.isEmpty()) { %>
    <div class="mb-3">
        <span class="badge badge-warning mr-2">Pendentes: <%= pendingCount %></span>
        <span class="badge badge-success">Concluídas: <%= completedCount %></span>
    </div>
    <% } %>

    <%-- Lista de tarefas --%>
    <% if (tasks.isEmpty()) { %>

        <div class="alert alert-info">
            Nenhuma tarefa encontrada. Clique em <strong>"+ Nova Tarefa"</strong> para começar!
        </div>

    <% } else { %>

        <table class="table table-hover">
            <thead class="thead-light">
                <tr>
                    <th>Título</th>
                    <th>Descrição</th>
                    <th>Prazo</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>

                <% for (Task task : tasks) { %>

                <%-- URLs geradas por iteração --%>
                <portlet:renderURL var="editURL">
                    <portlet:param name="mvcPath" value="/edit_task.jsp" />
                    <portlet:param name="taskId"  value="<%= String.valueOf(task.getTaskId()) %>" />
                </portlet:renderURL>

                <portlet:actionURL name="/todolist/toggle_task" var="toggleURL">
                    <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                </portlet:actionURL>

                <portlet:actionURL name="/todolist/delete_task" var="deleteURL">
                    <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                </portlet:actionURL>

                <tr class="<%= task.getIsCompleted() ? "table-success" : "" %>">

                    <td><strong><%= HtmlUtil.escape(task.getTitle()) %></strong></td>

                    <td>
                        <% String desc = task.getDescription();
                           if (desc != null && !desc.isEmpty()) { %>
                            <%= HtmlUtil.escape(desc) %>
                        <% } else { %>
                            <span class="text-muted">—</span>
                        <% } %>
                    </td>

                    <td>
                        <% if (task.getDueDate() != null) { %>
                            <fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" />
                        <% } else { %>
                            <span class="text-muted">—</span>
                        <% } %>
                    </td>

                    <td>
                        <span class="badge badge-<%= task.getIsCompleted() ? "success" : "warning" %>">
                            <%= task.getIsCompleted() ? "Concluída" : "Pendente" %>
                        </span>
                    </td>

                    <td>
                        <%-- Toggle: form POST para evitar GET em ação de escrita --%>
                        <form method="post" action="<%= toggleURL %>" style="display:inline">
                            <button type="submit"
                                class="btn btn-sm <%= task.getIsCompleted() ? "btn-outline-warning" : "btn-outline-success" %>">
                                <%= task.getIsCompleted() ? "Reabrir" : "Concluir" %>
                            </button>
                        </form>

                        <%-- Editar: render URL → vai para edit_task.jsp --%>
                        <a href="<%= editURL %>" class="btn btn-sm btn-outline-secondary">Editar</a>

                        <%-- Deletar: form POST com confirmação JavaScript --%>
                        <form method="post" action="<%= deleteURL %>" style="display:inline"
                              onsubmit="return confirm('Tem certeza que deseja remover a tarefa \\'<%= HtmlUtil.escapeJS(task.getTitle()) %>\\'?')">
                            <button type="submit" class="btn btn-sm btn-outline-danger">Deletar</button>
                        </form>
                    </td>

                </tr>

                <% } %>
            </tbody>
        </table>

    <% } %>

</div>
