<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="br.com.seatecnologia.todolist.model.Task" %>
<%@ page import="com.liferay.document.library.kernel.service.DLAppLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.repository.model.FileEntry" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
// themeDisplay já está disponível via <liferay-theme:defineObjects /> do init.jsp
if (!themeDisplay.isSignedIn()) {
%>

<style>
.sea-login-wrapper {
    min-height: 60vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 2rem;
}
.sea-login-card {
    background: #ffffff;
    border-radius: 20px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.12);
    padding: 3rem 2.5rem;
    text-align: center;
    max-width: 420px;
    width: 100%;
    position: relative;
    overflow: hidden;
}
.sea-login-card::before {
    content: '';
    position: absolute;
    top: 0; left: 0; right: 0;
    height: 5px;
    background: linear-gradient(90deg, #4f46e5, #7c3aed, #ec4899);
}
.sea-login-icon-wrap {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: linear-gradient(135deg, #ede9fe, #fce7f3);
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 1.5rem;
}
.sea-login-icon-wrap svg {
    width: 36px;
    height: 36px;
    color: #7c3aed;
}
.sea-login-title {
    font-size: 1.6rem;
    font-weight: 700;
    color: #1e1b4b;
    margin-bottom: 0.5rem;
}
.sea-login-subtitle {
    color: #6b7280;
    font-size: 0.95rem;
    margin-bottom: 2rem;
    line-height: 1.5;
}
.sea-btn-primary {
    display: block;
    width: 100%;
    padding: 0.85rem;
    background: linear-gradient(135deg, #4f46e5, #7c3aed);
    color: white !important;
    border: none;
    border-radius: 12px;
    font-size: 1rem;
    font-weight: 600;
    text-decoration: none;
    margin-bottom: 0.75rem;
    transition: all 0.2s ease;
    box-shadow: 0 4px 15px rgba(79,70,229,0.35);
}
.sea-btn-primary:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba(79,70,229,0.45);
    color: white !important;
    text-decoration: none;
}
.sea-btn-secondary {
    display: block;
    width: 100%;
    padding: 0.85rem;
    background: transparent;
    color: #4f46e5 !important;
    border: 2px solid #e0e7ff;
    border-radius: 12px;
    font-size: 1rem;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.2s ease;
}
.sea-btn-secondary:hover {
    background: #f5f3ff;
    border-color: #a5b4fc;
    text-decoration: none;
    color: #4f46e5 !important;
}
.sea-divider {
    color: #9ca3af;
    font-size: 0.8rem;
    margin: 0.75rem 0;
}
</style>

<div class="sea-login-wrapper">
    <div class="sea-login-card">
        <div class="sea-login-icon-wrap">
            <svg viewBox="0 0 24 24" fill="none" stroke="#7c3aed" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
            </svg>
        </div>
        <div class="sea-login-title">Acesso restrito</div>
        <p class="sea-login-subtitle">
            Fa&#231;a login para organizar suas tarefas e aumentar sua produtividade.
        </p>
        <a href="<%= themeDisplay.getURLSignIn() %>" class="sea-btn-primary">
            Entrar na conta
        </a>
        <div class="sea-divider">ou</div>
        <%
        String signInURL = themeDisplay.getURLSignIn();
        String createAccountHref = signInURL.replace(
            "mvcRenderCommandName=%2Flogin%2Flogin",
            "mvcRenderCommandName=%2Flogin%2Fcreate_account");
        %>
        <a href="<%= createAccountHref %>" class="sea-btn-secondary">
            Criar conta gratuita
        </a>
    </div>
</div>

<%
    return;
}
%>

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

// Data de hoje (sem hora) para comparar com dueDate
java.util.Date todayDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(
    new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));

List<Task> inProgressTasks = new java.util.ArrayList<>();
List<Task> overdueTasks    = new java.util.ArrayList<>();
List<Task> completedTasks  = new java.util.ArrayList<>();
for (Task t : tasks) {
    if (t.getIsCompleted()) {
        completedTasks.add(t);
    } else if (t.getDueDate() != null && t.getDueDate().before(todayDate)) {
        overdueTasks.add(t);
    } else {
        inProgressTasks.add(t);
    }
}

java.util.Map<Long, String> catNames = new java.util.HashMap<>();
for (Category cat : categories) {
    catNames.put(cat.getCategoryId(), cat.getName());
}

// Pré-computa URLs das imagens para evitar queries repetidas no loop
java.util.Map<Long, String> taskImageURLs = new java.util.HashMap<>();
for (Task t : tasks) {
    if (t.getImageId() > 0) {
        try {
            FileEntry fe = DLAppLocalServiceUtil.getFileEntry(t.getImageId());
            String imgURL = "/documents/" + fe.getRepositoryId() + "/" +
                fe.getFolderId() + "/" +
                URLEncoder.encode(fe.getFileName(), "UTF-8").replace("+", "%20");
            taskImageURLs.put(t.getTaskId(), imgURL);
        } catch (Exception e) {
            // imagem não encontrada — ignora
        }
    }
}
%>

<portlet:renderURL var="addTaskURL">
    <portlet:param name="mvcRenderCommandName" value="/todolist/create_task" />
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

<portlet:renderURL var="overdueTabURL">
    <portlet:param name="filter" value="<%= currentFilter %>" />
    <portlet:param name="activeTab" value="overdue" />
</portlet:renderURL>

<portlet:renderURL var="doneTabURL">
    <portlet:param name="filter" value="<%= currentFilter %>" />
    <portlet:param name="activeTab" value="done" />
</portlet:renderURL>

<style>
/* ===== TODO LIST STYLES ===== */
.sea-todo-wrapper {
    padding: 1.5rem 1rem;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

/* Header */
.sea-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 1.5rem;
    flex-wrap: wrap;
    gap: 0.75rem;
}
.sea-header-title {
    font-size: 1.75rem;
    font-weight: 800;
    color: #1e1b4b;
    margin: 0;
    letter-spacing: -0.5px;
}
.sea-header-title span {
    background: linear-gradient(135deg, #4f46e5, #7c3aed);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}
.sea-header-actions {
    display: flex;
    gap: 0.5rem;
    align-items: center;
}
.sea-btn-categories {
    display: inline-flex;
    align-items: center;
    gap: 0.35rem;
    padding: 0.5rem 1rem;
    border: 1.5px solid #e5e7eb;
    border-radius: 10px;
    background: white;
    color: #374151 !important;
    font-size: 0.85rem;
    font-weight: 500;
    text-decoration: none;
    transition: all 0.2s;
}
.sea-btn-categories:hover {
    border-color: #c4b5fd;
    background: #f5f3ff;
    color: #4f46e5 !important;
    text-decoration: none;
}
.sea-btn-new-task {
    display: inline-flex;
    align-items: center;
    gap: 0.35rem;
    padding: 0.5rem 1.1rem;
    background: linear-gradient(135deg, #4f46e5, #7c3aed);
    color: white !important;
    border-radius: 10px;
    font-size: 0.85rem;
    font-weight: 600;
    text-decoration: none;
    box-shadow: 0 3px 10px rgba(79,70,229,0.3);
    transition: all 0.2s;
}
.sea-btn-new-task:hover {
    transform: translateY(-1px);
    box-shadow: 0 5px 15px rgba(79,70,229,0.4);
    color: white !important;
    text-decoration: none;
}

/* Filter pills */
.sea-filters {
    display: flex;
    flex-wrap: wrap;
    gap: 0.4rem;
    margin-bottom: 0.75rem;
}
.sea-filter-pill {
    display: inline-flex;
    align-items: center;
    gap: 0.3rem;
    padding: 0.35rem 0.85rem;
    border-radius: 999px;
    font-size: 0.82rem;
    font-weight: 500;
    text-decoration: none;
    transition: all 0.2s;
    border: 1.5px solid transparent;
}
.sea-filter-pill:hover {
    text-decoration: none;
    transform: translateY(-1px);
}
.sea-filter-pill.active-all {
    background: #1e1b4b;
    color: white !important;
}
.sea-filter-pill.inactive-all {
    background: #f3f4f6;
    color: #374151 !important;
    border-color: #e5e7eb;
}
.sea-filter-pill.inactive-all:hover {
    background: #e5e7eb;
}
.sea-filter-pill.active-nocat {
    background: #6b7280;
    color: white !important;
}
.sea-filter-pill.inactive-nocat {
    background: #f9fafb;
    color: #6b7280 !important;
    border-color: #d1d5db;
}
.sea-filter-pill.active-cat {
    background: linear-gradient(135deg, #4f46e5, #7c3aed);
    color: white !important;
    box-shadow: 0 2px 8px rgba(79,70,229,0.3);
}
.sea-filter-pill.inactive-cat {
    background: #ede9fe;
    color: #4f46e5 !important;
    border-color: #c4b5fd;
}
.sea-filter-pill.inactive-cat:hover {
    background: #ddd6fe;
}
.sea-pill-count {
    background: rgba(255,255,255,0.3);
    padding: 0 5px;
    border-radius: 999px;
    font-size: 0.75rem;
    line-height: 1.4;
}
.sea-pill-count-dark {
    background: rgba(0,0,0,0.1);
    padding: 0 5px;
    border-radius: 999px;
    font-size: 0.75rem;
    line-height: 1.4;
}

/* Counters */
.sea-counters {
    display: flex;
    flex-wrap: wrap;
    gap: 0.4rem;
    margin-bottom: 1.25rem;
}
.sea-counter-chip {
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 0.25rem 0.7rem;
    font-size: 0.8rem;
    color: #6b7280;
}
.sea-counter-chip strong {
    color: #1e1b4b;
}

/* Tabs */
.sea-tabs {
    display: flex;
    border-bottom: 2px solid #e5e7eb;
    margin-bottom: 0;
    gap: 0;
}
.sea-tab-link {
    display: inline-flex;
    align-items: center;
    gap: 0.4rem;
    padding: 0.65rem 1.25rem;
    font-size: 0.9rem;
    font-weight: 600;
    text-decoration: none;
    color: #6b7280 !important;
    border-bottom: 2px solid transparent;
    margin-bottom: -2px;
    transition: all 0.2s;
}
.sea-tab-link:hover {
    color: #4f46e5 !important;
    text-decoration: none;
}
.sea-tab-link.sea-tab-active {
    color: #4f46e5 !important;
    border-bottom-color: #4f46e5;
}
.sea-tab-badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 20px;
    height: 20px;
    padding: 0 5px;
    border-radius: 999px;
    font-size: 0.72rem;
    font-weight: 700;
}
.sea-tab-badge-pending {
    background: #dbeafe;
    color: #1e40af;
}
.sea-tab-badge-overdue {
    background: #fee2e2;
    color: #991b1b;
}
.sea-tab-badge-done {
    background: #d1fae5;
    color: #065f46;
}
.sea-tab-badge-inactive {
    background: #f3f4f6;
    color: #6b7280;
}
.sea-tab-link.sea-tab-overdue {
    color: #dc2626 !important;
    border-bottom-color: #dc2626;
}

/* Tab content panel */
.sea-tab-panel {
    background: #ffffff;
    border: 1.5px solid #e5e7eb;
    border-top: none;
    border-radius: 0 0 16px 16px;
    overflow: hidden;
}

/* Task table */
.sea-task-table {
    width: 100%;
    border-collapse: collapse;
}
.sea-task-table thead tr {
    background: #f8f9ff;
    border-bottom: 1.5px solid #e5e7eb;
}
.sea-task-table th {
    padding: 0.75rem 1rem;
    font-size: 0.78rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    color: #6b7280;
    white-space: nowrap;
}
.sea-task-table td {
    padding: 0.85rem 1rem;
    vertical-align: middle;
    border-bottom: 1px solid #f3f4f6;
}
.sea-task-table tbody tr:last-child td {
    border-bottom: none;
}
.sea-task-table tbody tr:hover {
    background: #fafbff;
}
.sea-task-table tbody tr.sea-row-done {
    background: #f0fdf4;
}
.sea-task-table tbody tr.sea-row-done:hover {
    background: #dcfce7;
}
.sea-task-title {
    font-weight: 600;
    color: #111827;
    font-size: 0.92rem;
}
.sea-task-title-done {
    font-weight: 600;
    color: #6b7280;
    font-size: 0.92rem;
    text-decoration: line-through;
}
.sea-cat-badge {
    display: inline-block;
    padding: 0.2rem 0.6rem;
    border-radius: 999px;
    font-size: 0.75rem;
    font-weight: 600;
    background: linear-gradient(135deg, #ede9fe, #ddd6fe);
    color: #4f46e5;
}
.sea-cat-badge-done {
    display: inline-block;
    padding: 0.2rem 0.6rem;
    border-radius: 999px;
    font-size: 0.75rem;
    font-weight: 600;
    background: #d1fae5;
    color: #065f46;
}
.sea-date {
    font-size: 0.85rem;
    color: #374151;
    white-space: nowrap;
}
.sea-date-overdue {
    font-size: 0.85rem;
    color: #dc2626;
    font-weight: 600;
    white-space: nowrap;
}

/* Action buttons */
.sea-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 0.3rem;
    align-items: center;
}
.sea-action-btn {
    display: inline-flex;
    align-items: center;
    padding: 0.3rem 0.65rem;
    border-radius: 7px;
    font-size: 0.78rem;
    font-weight: 500;
    text-decoration: none;
    border: 1.5px solid transparent;
    cursor: pointer;
    transition: all 0.15s;
    line-height: 1.4;
    white-space: nowrap;
    background: none;
}
.sea-action-btn:hover { text-decoration: none; transform: translateY(-1px); }
.sea-btn-detail { background: #eff6ff; color: #1d4ed8 !important; border-color: #bfdbfe; }
.sea-btn-detail:hover { background: #dbeafe; }
.sea-btn-complete { background: #f0fdf4; color: #16a34a !important; border-color: #bbf7d0; }
.sea-btn-complete:hover { background: #dcfce7; }
.sea-btn-reopen { background: #fffbeb; color: #d97706 !important; border-color: #fde68a; }
.sea-btn-reopen:hover { background: #fef3c7; }
.sea-btn-edit { background: #f8fafc; color: #475569 !important; border-color: #e2e8f0; }
.sea-btn-edit:hover { background: #f1f5f9; }
.sea-btn-delete { background: #fff5f5; color: #dc2626 !important; border-color: #fecaca; }
.sea-btn-delete:hover { background: #fee2e2; }

/* Empty state */
.sea-empty {
    text-align: center;
    padding: 3rem 1.5rem;
    color: #9ca3af;
}
.sea-empty-icon {
    font-size: 2.5rem;
    margin-bottom: 0.75rem;
    opacity: 0.5;
}
.sea-empty-text {
    font-size: 0.95rem;
}

/* Task thumbnail (cover image) */
.sea-task-thumb {
    width: 44px;
    height: 44px;
    border-radius: 8px;
    object-fit: cover;
    border: 1.5px solid #e5e7eb;
    flex-shrink: 0;
    display: block;
}
.sea-task-thumb-empty {
    width: 44px;
    height: 44px;
    border-radius: 8px;
    background: #f3f4f6;
    border: 1.5px dashed #d1d5db;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #d1d5db;
    font-size: 1.1rem;
}
.sea-title-cell {
    display: flex;
    align-items: center;
    gap: 0.65rem;
}

/* Alert */
.sea-alert-empty {
    background: #eff6ff;
    border: 1.5px solid #bfdbfe;
    border-radius: 12px;
    padding: 1.25rem 1.5rem;
    color: #1d4ed8;
    font-size: 0.9rem;
}
</style>

<div class="sea-todo-wrapper">

    <%-- Cabeçalho --%>
    <div class="sea-header">
        <h2 class="sea-header-title"><span>TodoList</span></h2>
        <div class="sea-header-actions">
            <a href="<%= categoriesURL %>" class="sea-btn-categories">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg>
                Categorias
            </a>
            <a href="<%= addTaskURL %>" class="sea-btn-new-task">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                Nova Tarefa
            </a>
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
    <div class="sea-filters">

        <a href="<%= filterAllURL %>"
           class="sea-filter-pill <%= "all".equals(currentFilter) ? "active-all" : "inactive-all" %>">
            Todas
            <span class="<%= "all".equals(currentFilter) ? "sea-pill-count" : "sea-pill-count-dark" %>"><%= tasks.size() %></span>
        </a>

        <a href="<%= filterNoCatURL %>"
           class="sea-filter-pill <%= "no-category".equals(currentFilter) ? "active-nocat" : "inactive-nocat" %>">
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
               class="sea-filter-pill <%= isActive ? "active-cat" : "inactive-cat" %>">
                <%= HtmlUtil.escape(cat.getName()) %>
            </a>
        <% } %>

    </div>

    <%-- Contadores por categoria --%>
    <% if (!categoryCounters.isEmpty()) { %>
        <div class="sea-counters">
            <% for (Map.Entry<String, Long> entry : categoryCounters.entrySet()) { %>
                <span class="sea-counter-chip">
                    <%= HtmlUtil.escape(entry.getKey()) %>: <strong><%= entry.getValue() %></strong>
                </span>
            <% } %>
        </div>
    <% } %>

    <%-- Conteúdo --%>
    <% if (tasks.isEmpty()) { %>
        <div class="sea-alert-empty">
            <% if ("all".equals(currentFilter)) { %>
                Nenhuma tarefa ainda. Clique em <strong>"+ Nova Tarefa"</strong> para come&#231;ar!
            <% } else { %>
                Nenhuma tarefa encontrada para o filtro selecionado.
            <% } %>
        </div>
    <% } else { %>

        <%-- Abas server-side --%>
        <div class="sea-tabs">
            <a class="sea-tab-link <%= "pending".equals(activeTab) ? "sea-tab-active" : "" %>"
               href="<%= pendingTabURL %>">
                In Progress
                <span class="sea-tab-badge <%= "pending".equals(activeTab) ? "sea-tab-badge-pending" : "sea-tab-badge-inactive" %>">
                    <%= inProgressTasks.size() %>
                </span>
            </a>
            <a class="sea-tab-link <%= "overdue".equals(activeTab) ? "sea-tab-active sea-tab-overdue" : "" %>"
               href="<%= overdueTabURL %>">
                Pendente
                <span class="sea-tab-badge <%= "overdue".equals(activeTab) ? "sea-tab-badge-overdue" : (overdueTasks.isEmpty() ? "sea-tab-badge-inactive" : "sea-tab-badge-overdue") %>">
                    <%= overdueTasks.size() %>
                </span>
            </a>
            <a class="sea-tab-link <%= "done".equals(activeTab) ? "sea-tab-active" : "" %>"
               href="<%= doneTabURL %>">
                Conclu&#237;das
                <span class="sea-tab-badge <%= "done".equals(activeTab) ? "sea-tab-badge-done" : "sea-tab-badge-inactive" %>">
                    <%= completedTasks.size() %>
                </span>
            </a>
        </div>

        <div class="sea-tab-panel">

            <% if ("pending".equals(activeTab)) { %>

                <%-- Aba In Progress --%>
                <% if (inProgressTasks.isEmpty()) { %>
                    <div class="sea-empty">
                        <div class="sea-empty-icon">&#9654;</div>
                        <p class="sea-empty-text">Nenhuma tarefa em andamento neste filtro.</p>
                    </div>
                <% } else { %>
                    <table class="sea-task-table">
                        <thead>
                            <tr>
                                <th>T&#237;tulo</th>
                                <th>Categoria</th>
                                <th>Prazo</th>
                                <th>A&#231;&#245;es</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Task task : inProgressTasks) { %>

                            <portlet:renderURL var="editURL">
                                <portlet:param name="mvcRenderCommandName" value="/todolist/edit_task" />
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:renderURL>

                            <portlet:renderURL var="detailURL">
                                <portlet:param name="mvcRenderCommandName" value="/todolist/task_detail" />
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:renderURL>

                            <portlet:actionURL name="/todolist/toggle_task" var="toggleURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <portlet:actionURL name="/todolist/delete_task" var="deleteURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <tr>
                                <td>
                                    <div class="sea-title-cell">
                                        <% String thumbURL = taskImageURLs.get(task.getTaskId());
                                           if (thumbURL != null) { %>
                                            <img src="<%= thumbURL %>" class="sea-task-thumb" alt="" />
                                        <% } else { %>
                                            <div class="sea-task-thumb-empty">&#128247;</div>
                                        <% } %>
                                        <span class="sea-task-title"><%= HtmlUtil.escape(task.getTitle()) %></span>
                                    </div>
                                </td>
                                <td>
                                    <% String catName = catNames.get(task.getCategoryId());
                                       if (catName != null) { %>
                                        <span class="sea-cat-badge"><%= HtmlUtil.escape(catName) %></span>
                                    <% } else { %>
                                        <span class="text-muted" style="font-size:1.1rem;">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <% if (task.getDueDate() != null) { %>
                                        <span class="sea-date">
                                            <fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" />
                                        </span>
                                    <% } else { %>
                                        <span class="text-muted" style="font-size:1.1rem;">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <div class="sea-actions">
                                        <a href="<%= detailURL %>" class="sea-action-btn sea-btn-detail">Detalhes</a>
                                        <form method="post" action="<%= toggleURL %>" style="display:inline;margin:0;">
                                            <button type="submit" class="sea-action-btn sea-btn-complete">Concluir</button>
                                        </form>
                                        <a href="<%= editURL %>" class="sea-action-btn sea-btn-edit">Editar</a>
                                        <form method="post" action="<%= deleteURL %>" style="display:inline;margin:0;"
                                              onsubmit="return confirm('Remover a tarefa \'<%= HtmlUtil.escapeJS(task.getTitle()) %>\'?')">
                                            <button type="submit" class="sea-action-btn sea-btn-delete">Deletar</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>

                            <% } %>
                        </tbody>
                    </table>
                <% } %>

            <% } else if ("overdue".equals(activeTab)) { %>

                <%-- Aba Pendente (tarefas com prazo estourado) --%>
                <% if (overdueTasks.isEmpty()) { %>
                    <div class="sea-empty">
                        <div class="sea-empty-icon">&#127881;</div>
                        <p class="sea-empty-text">Nenhuma tarefa com prazo estourado. Arrasou!</p>
                    </div>
                <% } else { %>
                    <table class="sea-task-table">
                        <thead>
                            <tr>
                                <th>T&#237;tulo</th>
                                <th>Categoria</th>
                                <th>Prazo (estourado)</th>
                                <th>A&#231;&#245;es</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Task task : overdueTasks) { %>

                            <portlet:renderURL var="ovEditURL">
                                <portlet:param name="mvcRenderCommandName" value="/todolist/edit_task" />
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:renderURL>

                            <portlet:renderURL var="ovDetailURL">
                                <portlet:param name="mvcRenderCommandName" value="/todolist/task_detail" />
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:renderURL>

                            <portlet:actionURL name="/todolist/toggle_task" var="ovToggleURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <portlet:actionURL name="/todolist/delete_task" var="ovDeleteURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <tr style="background:#fff5f5;">
                                <td>
                                    <div class="sea-title-cell">
                                        <% String thumbURLOv = taskImageURLs.get(task.getTaskId());
                                           if (thumbURLOv != null) { %>
                                            <img src="<%= thumbURLOv %>" class="sea-task-thumb" alt=""
                                                 style="border-color:#fecaca;" />
                                        <% } else { %>
                                            <div class="sea-task-thumb-empty" style="border-color:#fecaca; color:#fca5a5;">&#9201;</div>
                                        <% } %>
                                        <span class="sea-task-title" style="color:#dc2626;"><%= HtmlUtil.escape(task.getTitle()) %></span>
                                    </div>
                                </td>
                                <td>
                                    <% String catNameOv = catNames.get(task.getCategoryId());
                                       if (catNameOv != null) { %>
                                        <span class="sea-cat-badge" style="background:#fee2e2; color:#dc2626;">
                                            <%= HtmlUtil.escape(catNameOv) %>
                                        </span>
                                    <% } else { %>
                                        <span class="text-muted" style="font-size:1.1rem;">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <span class="sea-date-overdue">
                                        <fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" />
                                        &#9888;
                                    </span>
                                </td>
                                <td>
                                    <div class="sea-actions">
                                        <a href="<%= ovDetailURL %>" class="sea-action-btn sea-btn-detail">Detalhes</a>
                                        <form method="post" action="<%= ovToggleURL %>" style="display:inline;margin:0;">
                                            <button type="submit" class="sea-action-btn sea-btn-complete">Concluir</button>
                                        </form>
                                        <a href="<%= ovEditURL %>" class="sea-action-btn sea-btn-edit" title="Atualize o prazo para voltar ao In Progress">Editar Prazo</a>
                                        <form method="post" action="<%= ovDeleteURL %>" style="display:inline;margin:0;"
                                              onsubmit="return confirm('Remover a tarefa \'<%= HtmlUtil.escapeJS(task.getTitle()) %>\'?')">
                                            <button type="submit" class="sea-action-btn sea-btn-delete">Deletar</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>

                            <% } %>
                        </tbody>
                    </table>
                <% } %>

            <% } else { %>

                <%-- Aba Concluídas --%>
                <% if (completedTasks.isEmpty()) { %>
                    <div class="sea-empty">
                        <div class="sea-empty-icon">&#128230;</div>
                        <p class="sea-empty-text">Nenhuma tarefa conclu&#237;da neste filtro.</p>
                    </div>
                <% } else { %>
                    <table class="sea-task-table">
                        <thead>
                            <tr>
                                <th>T&#237;tulo</th>
                                <th>Categoria</th>
                                <th>Prazo</th>
                                <th>A&#231;&#245;es</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Task task : completedTasks) { %>

                            <portlet:renderURL var="doneDetailURL">
                                <portlet:param name="mvcRenderCommandName" value="/todolist/task_detail" />
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:renderURL>

                            <portlet:actionURL name="/todolist/toggle_task" var="doneToggleURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <portlet:actionURL name="/todolist/delete_task" var="doneDeleteURL">
                                <portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
                            </portlet:actionURL>

                            <tr class="sea-row-done">
                                <td>
                                    <div class="sea-title-cell">
                                        <% String thumbURL2 = taskImageURLs.get(task.getTaskId());
                                           if (thumbURL2 != null) { %>
                                            <img src="<%= thumbURL2 %>" class="sea-task-thumb" alt=""
                                                 style="opacity:0.6;" />
                                        <% } else { %>
                                            <div class="sea-task-thumb-empty" style="opacity:0.5;">&#128247;</div>
                                        <% } %>
                                        <span class="sea-task-title-done"><%= HtmlUtil.escape(task.getTitle()) %></span>
                                    </div>
                                </td>
                                <td>
                                    <% String catName2 = catNames.get(task.getCategoryId());
                                       if (catName2 != null) { %>
                                        <span class="sea-cat-badge-done"><%= HtmlUtil.escape(catName2) %></span>
                                    <% } else { %>
                                        <span class="text-muted" style="font-size:1.1rem;">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <% if (task.getDueDate() != null) { %>
                                        <span class="sea-date">
                                            <fmt:formatDate value="<%= task.getDueDate() %>" pattern="dd/MM/yyyy" />
                                        </span>
                                    <% } else { %>
                                        <span class="text-muted" style="font-size:1.1rem;">&#8212;</span>
                                    <% } %>
                                </td>
                                <td>
                                    <div class="sea-actions">
                                        <a href="<%= doneDetailURL %>" class="sea-action-btn sea-btn-detail">Detalhes</a>
                                        <form method="post" action="<%= doneToggleURL %>" style="display:inline;margin:0;">
                                            <button type="submit" class="sea-action-btn sea-btn-reopen">Reabrir</button>
                                        </form>
                                        <form method="post" action="<%= doneDeleteURL %>" style="display:inline;margin:0;"
                                              onsubmit="return confirm('Remover a tarefa \'<%= HtmlUtil.escapeJS(task.getTitle()) %>\'?')">
                                            <button type="submit" class="sea-action-btn sea-btn-delete">Deletar</button>
                                        </form>
                                    </div>
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
