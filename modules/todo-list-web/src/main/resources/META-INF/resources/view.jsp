<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
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

// Aba ativa via parâmetro de render (server-side tabs)
String activeTab = ParamUtil.getString(renderRequest, "activeTab", "pending");

List<Task> pendingTasks   = new java.util.ArrayList<>();
List<Task> completedTasks = new java.util.ArrayList<>();
for (Task t : tasks) {
    if (t.getIsCompleted()) completedTasks.add(t);
    else pendingTasks.add(t);
}

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
    <portlet:param name="filter" value="all" />
    <portlet:param name="activeTab" value="<%= activeTab %>" />
</portlet:renderURL>

<portlet:renderURL var="filterNoCatURL">
    <portlet:param name="filter" value="no-category" />
    <portlet:param name="activeTab" value="<%= activeTab %>" />
</portlet:renderURL>

<%-- URLs das abas mantendo o filtro atual --%>
<portlet:renderURL var="pendingTabURL">
    <portlet:param name="filter" value="<%= currentFilter %>" />
    <portlet:param name="activeTab" value="pending" />
</portlet:renderURL>

<portlet:renderURL var="doneTabURL">
    <portlet:param name="filter" value="<%= currentFilter %>" />
    <portlet:param name="activeTab" value="done" />
</portlet:renderURL>

<div class="container-fluid mt-3">

    <%-- Cabeçalho --%>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Minhas Tarefas</h2>
        <div>
            <a href="<%= categoriesURL %>" class="btn btn-outline-secondary btn-sm mr-2">
                &#9776; Categorias
            </a>
            <a href="<%= addTaskURL %>" class="btn btn-primary btn-sm">+ Nova Tarefa</a>
        </div>
    </div>

    <%-- Mensagens de feedback --%>
    <liferay-ui:success key="task-added"       message="task-added" />
    <liferay-ui:success key="task-updated"     message="task-updated" />
    <liferay-ui:success key="task-deleted"     message="task-deleted" />
    <liferay-ui:success key="task-toggled"     message="task-toggled" />
    <liferay-ui:success key="category-added"   message="category-added" />
    <liferay-ui:success key="category-deleted" message="category-deleted" />
    <liferay-ui:error   key="task-not-authorized" message="task-not-authorized" />

    <%-- Filtros por categoria --%>
    <div class="mb-2 d-flex flex-wrap" style="gap: 6px;">

        <a href="<%= filterAllURL %>"
           class="btn btn-sm <%= "all".equals(currentFilter) ? "btn-dark" : "btn-outline-dark" %>">
            Todas
            <span style="background:rgba(255,255,255,0.25);padding:1px 6px;border-radius:10px;font-size:11px;margin-left:3px;">
                <%= tasks.size() %>
            </span>
        </a>

        <a href="<%= filterNoCatURL %>"
           class="btn btn-sm <%= "no-category".equals(currentFilter) ? "btn-secondary" : "btn-outline-secondary" %>">
            Sem Categoria
        </a>

        <% for (Category cat : categories) {
            String catFilter = "categoryId:" + cat.getCategoryId();
            boolean isActive = catFilter.equals(currentFilter);
        %>
            <portlet:renderURL var="filterCatURL">
                <portlet:param name="filter" value="<%= catFilter %>" />
                <portlet:param name="activeTab" value="<%= activeTab %>" />
            </portlet:renderURL>
            <a href="<%= filterCatURL %>"
               class="btn btn-sm <%= isActive ? "btn-info" : "btn-outline-info" %>">
                <%= HtmlUtil.escape(cat.getName()) %>
            </a>
        <% } %>

    </div>

    <%-- Contadores por categoria --%>
    <% if (!categoryCounters.isEmpty()) { %>
        <div class="mb-3 d-flex flex-wrap" style="gap: 6px;">
            <% for (Map.Entry<String, Long> entry : categoryCounters.entrySet()) { %>
                <span class="badge badge-light border" style="font-size:12px;padding:4px 10px;">
                    <%= HtmlUtil.escape(entry.getKey()) %>: <strong><%= entry.getValue() %></strong>
                </span>
            <% } %>
        </div>
    <% } %>

    <%-- Conteúdo --%>
    <% if (tasks.isEmpty()) { %>
        <div class="alert alert-info mt-2">
            <% if ("all".equals(currentFilter)) { %>
                Nenhuma tarefa ainda. Clique em <strong>"+ Nova Tarefa"</strong> para começar!
            <% } else { %>
                Nenhuma tarefa encontrada para o filtro selecionado.
            <% } %>
        </div>
    <% } else { %>

        <%-- Abas server-side (sem depender de Bootstrap JS) --%>
        <ul class="nav nav-tabs">
            <li class="nav-item">
                <a class="nav-link <%= "pending".equals(activeTab) ? "active font-weight-bold" : "" %>"
                   href="<%= pendingTabURL %>">
                    Pendentes
                    <span class="badge <%= "pending".equals(activeTab) ? "badge-warning" : "badge-secondary" %> ml-1">
                        <%= pendingTasks.size() %>
                    </span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link <%= "done".equals(activeTab) ? "active font-weight-bold" : "" %>"
                   href="<%= doneTabURL %>">
                    Conclu&#237;das
                    <span class="badge <%= "done".equals(activeTab) ? "badge-success" : "badge-secondary" %> ml-1">
                        <%= completedTasks.size() %>
                    </span>
                </a>
            </li>
        </ul>

        <div class="border border-top-0 rounded-bottom p-3 bg-white">

            <% if ("pending".equals(activeTab)) { %>

                <%-- Aba Pendentes --%>
                <% if (pendingTasks.isEmpty()) { %>
                    <p class="text-muted mb-0">Nenhuma tarefa pendente neste filtro.</p>
                <% } else { %>
                    <table class="table table-hover mb-0">
                        <thead class="thead-light">
                            <tr>
                                <th>T&#237;tulo</th>
                                <th>Categoria</th>
                                <th>Prazo</th>
                                <th>A&#231;&#245;es</th>
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
                                        <span class="text-muted">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <% if (task.getDueDate() != null) { %>
                                        <fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" />
                                    <% } else { %>
                                        <span class="text-muted">&#8212;</span>
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

            <% } else { %>

                <%-- Aba Concluídas --%>
                <% if (completedTasks.isEmpty()) { %>
                    <p class="text-muted mb-0">Nenhuma tarefa conclu&#237;da neste filtro.</p>
                <% } else { %>
                    <table class="table table-hover mb-0">
                        <thead class="thead-light">
                            <tr>
                                <th>T&#237;tulo</th>
                                <th>Categoria</th>
                                <th>Prazo</th>
                                <th>A&#231;&#245;es</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Task task : completedTasks) { %>

                            <portlet:actionURL name="/todolist/toggle_task" var="doneToggleURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <portlet:actionURL name="/todolist/delete_task" var="doneDeleteURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <tr class="table-success">
                                <td><strong><%= HtmlUtil.escape(task.getTitle()) %></strong></td>
                                <td>
                                    <% String catName2 = catNames.get(task.getCategoryId());
                                       if (catName2 != null) { %>
                                        <span class="badge badge-info"><%= HtmlUtil.escape(catName2) %></span>
                                    <% } else { %>
                                        <span class="text-muted">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <% if (task.getDueDate() != null) { %>
                                        <fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" />
                                    <% } else { %>
                                        <span class="text-muted">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <form method="post" action="<%= doneToggleURL %>" style="display:inline">
                                        <button type="submit" class="btn btn-sm btn-outline-warning">Reabrir</button>
                                    </form>
                                    <form method="post" action="<%= doneDeleteURL %>" style="display:inline"
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
    <% } %>

</div>
