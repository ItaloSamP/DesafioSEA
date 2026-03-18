<%@ page pageEncoding="UTF-8" %>
<%@ include file="/init.jsp" %>

<%@ page import="br.com.seatecnologia.todolist.model.Category" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.util.List" %>

<%
@SuppressWarnings("unchecked")
List<Category> categories = (List<Category>) renderRequest.getAttribute("categories");
if (categories == null) categories = new java.util.ArrayList<>();
%>

<portlet:actionURL name="/todolist/add_category" var="addCategoryURL" />
<portlet:renderURL var="backURL" />

<div class="container-fluid mt-3" style="max-width: 600px;">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Minhas Categorias</h2>
        <a href="<%= backURL %>" class="btn btn-secondary btn-sm">← Voltar</a>
    </div>

    <%-- Mensagens de feedback --%>
    <liferay-ui:success key="category-added"   message="category-added" />
    <liferay-ui:success key="category-deleted" message="category-deleted" />
    <liferay-ui:error   key="category-name-required"    message="category-name-required" />
    <liferay-ui:error   key="category-name-too-long"    message="category-name-too-long" />
    <liferay-ui:error   key="category-not-authorized"   message="category-not-authorized" />

    <%-- Formulário para adicionar nova categoria --%>
    <div class="card mb-4">
        <div class="card-body">
            <h5 class="card-title">Nova Categoria</h5>
            <form action="<%= addCategoryURL %>" method="post" class="form-inline">
                <div class="form-group mr-2" style="flex: 1;">
                    <input
                        type="text"
                        name="<portlet:namespace />name"
                        class="form-control w-100"
                        placeholder="Nome da categoria (ex: Trabalho, Pessoal...)"
                        maxlength="75"
                        required
                    />
                </div>
                <button type="submit" class="btn btn-primary">Adicionar</button>
            </form>
        </div>
    </div>

    <%-- Lista de categorias existentes --%>
    <% if (categories.isEmpty()) { %>
        <div class="alert alert-info">
            Nenhuma categoria criada ainda. Use o formulário acima para criar a primeira!
        </div>
    <% } else { %>
        <ul class="list-group">
            <% for (Category cat : categories) { %>

            <portlet:actionURL name="/todolist/delete_category" var="deleteCatURL">
                <portlet:param name="categoryId" value="<%= String.valueOf(cat.getCategoryId()) %>" />
            </portlet:actionURL>

            <li class="list-group-item d-flex justify-content-between align-items-center">
                <span><%= HtmlUtil.escape(cat.getName()) %></span>
                <form method="post" action="<%= deleteCatURL %>" style="display:inline"
                      onsubmit="return confirm('Remover a categoria \'<%= HtmlUtil.escapeJS(cat.getName()) %>\'? As tarefas vinculadas ficarão sem categoria.')">
                    <button type="submit" class="btn btn-sm btn-outline-danger">Remover</button>
                </form>
            </li>

            <% } %>
        </ul>
    <% } %>

</div>
