<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
@SuppressWarnings("unchecked")
List<Task> tasks = (List<Task>) renderRequest.getAttribute("tasks");
if (tasks == null) tasks = new java.util.ArrayList<>();

@SuppressWarnings("unchecked")
List<Category> categories = (List<Category>) renderRequest.getAttribute("categories");
if (categories == null) categories = new java.util.ArrayList<>();

@SuppressWarnings("unchecked")
Map<String, Long> categoryCounters = (Map<String, Long>) renderRequest.getAttribute("categoryCounters");
if (categoryCounters == null) categoryCounters = new java.util.LinkedHashMap<>();

String currentFilter = (String) renderRequest.getAttribute("filter");
if (currentFilter == null) currentFilter = "all";

List<Task> pendingTasks   = new java.util.ArrayList<>();
List<Task> completedTasks = new java.util.ArrayList<>();
for (Task t : tasks) {
    if (t.getIsCompleted()) completedTasks.add(t);
    else pendingTasks.add(t);
}

// Mapa categoryId -> nome para exibir na tabela
java.util.Map<Long, String> catNames = new java.util.HashMap<>();
for (Category cat : categories) {
    catNames.put(cat.getCategoryId(), cat.getName());
}
%>

<portlet:renderURL var="addTaskURL">
    <portlet:param name="mvcPath" value="/edit_task.jsp" />
</portlet:renderURL>

<portlet:renderURL var="categoriesURL">
    <portlet:param name="mvcPath" value="/categories.jsp" />
</portlet:renderURL>

<portlet:renderURL var="filterAllURL">
    <portlet:param name="mvcPath" value="/view.jsp" />
    <portlet:param name="filter" value="all" />
</portlet:renderURL>

<portlet:renderURL var="filterPendingURL">
    <portlet:param name="mvcPath" value="/view.jsp" />
    <portlet:param name="filter" value="pending" />
</portlet:renderURL>

<portlet:renderURL var="filterDoneURL">
    <portlet:param name="mvcPath" value="/view.jsp" />
    <portlet:param name="filter" value="done" />
</portlet:renderURL>

<portlet:renderURL var="filterNoCatURL">
    <portlet:param name="mvcPath" value="/view.jsp" />
    <portlet:param name="filter" value="no-category" />
</portlet:renderURL>

<div class="container-fluid mt-3">

    <%-- Cabeçalho --%>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Minhas Tarefas</h2>
        <div>
            <a href="<%= categoriesURL %>" class="btn btn-outline-secondary btn-sm mr-2">
                &#9776; Categorias
            </a>
            <a href="<%= addTaskURL %>" class="btn btn-primary">+ Nova Tarefa</a>
        </div>
    </div>

    <%-- Mensagens de feedback --%>
    <liferay-ui:success key="task-added"      message="task-added" />
    <liferay-ui:success key="task-updated"    message="task-updated" />
    <liferay-ui:success key="task-deleted"    message="task-deleted" />
    <liferay-ui:success key="category-added"  message="category-added" />
    <liferay-ui:success key="category-deleted" message="category-deleted" />
    <liferay-ui:error   key="task-not-authorized" message="task-not-authorized" />

    <%-- Barra de filtros (4.6) --%>
    <div class="mb-3 d-flex flex-wrap" style="gap:6px;">

        <a href="<%= filterAllURL %>"
           class="btn btn-sm <%= "all".equals(currentFilter) ? "btn-dark" : "btn-outline-dark" %>">
            Todas
            <span style="background:rgba(255,255,255,0.3);padding:1px 6px;border-radius:10px;font-size:11px;margin-left:3px;">
                <%= tasks.size() %>
            </span>
        </a>

        <a href="<%= filterPendingURL %>"
           class="btn btn-sm <%= "pending".equals(currentFilter) ? "btn-warning" : "btn-outline-warning" %>">
            Pendentes
        </a>

        <a href="<%= filterDoneURL %>"
           class="btn btn-sm <%= "done".equals(currentFilter) ? "btn-success" : "btn-outline-success" %>">
            Concluídas
        </a>

        <a href="<%= filterNoCatURL %>"
           class="btn btn-sm <%= "no-category".equals(currentFilter) ? "btn-secondary" : "btn-outline-secondary" %>">
            Sem Categoria
        </a>

        <%-- Filtros por categoria --%>
        <% for (Category cat : categories) {
            String catFilter = "categoryId:" + cat.getCategoryId();
            boolean isActive = catFilter.equals(currentFilter);
        %>
            <portlet:renderURL var="filterCatURL">
                <portlet:param name="mvcPath" value="/view.jsp" />
                <portlet:param name="filter" value="<%= catFilter %>" />
            </portlet:renderURL>
            <a href="<%= filterCatURL %>"
               class="btn btn-sm <%= isActive ? "btn-info" : "btn-outline-info" %>">
                <%= HtmlUtil.escape(cat.getName()) %>
            </a>
        <% } %>
    </div>

    <%-- Contadores por categoria (4.7) --%>
    <% if (!categoryCounters.isEmpty()) { %>
        <div class="mb-3 d-flex flex-wrap" style="gap:6px;">
            <% for (Map.Entry<String, Long> entry : categoryCounters.entrySet()) { %>
                <span class="badge badge-light border" style="font-size:13px;padding:5px 10px;">
                    <%= HtmlUtil.escape(entry.getKey()) %>: <strong><%= entry.getValue() %></strong>
                </span>
            <% } %>
        </div>
    <% } %>

    <%-- Conteúdo filtrado --%>
    <% if (tasks.isEmpty()) { %>
        <div class="alert alert-info">
            <% if ("all".equals(currentFilter)) { %>
                Nenhuma tarefa ainda. Clique em <strong>"+ Nova Tarefa"</strong> para começar!
            <% } else { %>
                Nenhuma tarefa encontrada para o filtro selecionado.
            <% } %>
        </div>
    <% } else { %>

        <%-- Tabela de tarefas Pendentes --%>
        <% if (!pendingTasks.isEmpty()) { %>
            <h5 class="text-muted mb-2">
                Pendentes
                <span style="background:#ffc107;color:#212529;padding:1px 8px;border-radius:10px;font-size:12px;margin-left:4px;">
                    <%= pendingTasks.size() %>
                </span>
            </h5>
            <table class="table table-hover mb-4">
                <thead class="thead-light">
                    <tr>
                        <th>Título</th>
                        <th>Categoria</th>
                        <th>Prazo</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Task task : pendingTasks) { %>

                    <portlet:renderURL var="editURL">
                        <portlet:param name="mvcRenderCommandName" value="/todolist/edit_task" />
                        <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:renderURL>

                    <portlet:actionURL name="/todolist/toggle_task" var="toggleURL">
                        <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:actionURL>

                    <portlet:actionURL name="/todolist/delete_task" var="deleteURL">
                        <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:actionURL>

                    <tr>
                        <td><strong><%= HtmlUtil.escape(task.getTitle()) %></strong></td>

                        <td>
                            <% String catName = catNames.get(task.getCategoryId());
                               if (catName != null) { %>
                                <span class="badge badge-info"><%= HtmlUtil.escape(catName) %></span>
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
                            <form method="post" action="<%= toggleURL %>" style="display:inline">
                                <button type="submit" class="btn btn-sm btn-outline-success">Concluir</button>
                            </form>

                            <a href="<%= editURL %>" class="btn btn-sm btn-outline-secondary">Editar</a>

                            <form method="post" action="<%= deleteURL %>" style="display:inline"
                                  onsubmit="return confirm('Remover a tarefa \'<%= HtmlUtil.escapeJS(task.getTitle()) %>\'?')">
                                <button type="submit" class="btn btn-sm btn-outline-danger">Deletar</button>
                            </form>
                        </td>
                    </tr>

                    <% } %>
                </tbody>
            </table>
        <% } %>

        <%-- Tabela de tarefas Concluídas --%>
        <% if (!completedTasks.isEmpty()) { %>
            <h5 class="text-muted mb-2">
                Concluídas
                <span style="background:#28a745;color:#fff;padding:1px 8px;border-radius:10px;font-size:12px;margin-left:4px;">
                    <%= completedTasks.size() %>
                </span>
            </h5>
            <table class="table table-hover">
                <thead class="thead-light">
                    <tr>
                        <th>Título</th>
                        <th>Categoria</th>
                        <th>Prazo</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Task task : completedTasks) { %>

                    <portlet:actionURL name="/todolist/toggle_task" var="toggleURL">
                        <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:actionURL>

                    <portlet:actionURL name="/todolist/delete_task" var="deleteURL">
                        <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                    </portlet:actionURL>

                    <tr class="table-success">
                        <td><strong><%= HtmlUtil.escape(task.getTitle()) %></strong></td>

                        <td>
                            <% String catName2 = catNames.get(task.getCategoryId());
                               if (catName2 != null) { %>
                                <span class="badge badge-info"><%= HtmlUtil.escape(catName2) %></span>
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
                            <form method="post" action="<%= toggleURL %>" style="display:inline">
                                <button type="submit" class="btn btn-sm btn-outline-warning">Reabrir</button>
                            </form>

                            <form method="post" action="<%= deleteURL %>" style="display:inline"
                                  onsubmit="return confirm('Remover a tarefa \'<%= HtmlUtil.escapeJS(task.getTitle()) %>\'?')">
                                <button type="submit" class="btn btn-sm btn-outline-danger">Deletar</button>
                            </form>
                        </td>
                    </tr>

                    <% } %>
                </tbody>
            </table>
        <% } %>

    <% } %>

</div>
