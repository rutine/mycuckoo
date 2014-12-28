<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.mycuckoo.common.utils.JsonUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<div>
	<!-- 查询表单 -->
	<div class="page-header">
		<form class="form-inline" name="searchForm">
			<div class="form-group">
				<label class="sr-only">模块名称</label>
				<input type="text" class="form-control input-sm" name="modName" value="${param.modName}" placeholder=模块名称 />
			</div>
			<div class="form-group">
				<label class="sr-only">模块ID</label>
				<input type="text" class="form-control input-sm" name="modEnId" value="${param.modEnId}" placeholder=模块ID />
			</div>
			<button type="button" class="btn btn-info btn-sm" name="search">查询
				<span class="glyphicon glyphicon-search"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm" name="clear">&nbsp;清空
				<span class="glyphicon glyphicon-repeat"></span>
			</button>
		</form>
	</div>
	<!-- 模块操作按钮 -->
	<tags:toolbar name="moduleOpt" value="${sessionScope.fourthMap[sessionScope.modEnId]}"></tags:toolbar>
	<!-- 内容列表 -->
	<table class="table table-striped table-hover table-condensed">
		<tr>
			<th><input type="checkbox" name="all"/></th>
			<th>序号</th>
			<th>模块名称</th>
			<th>模块ID</th>
			<th>模块图片样式</th>
			<th>模块级别</th>
			<th>模块顺序</th>
			<th>所属系统</th>
			<th>页面类型</th>
			<th>模块状态</th>
			<th class="hide">模块备注</th>
			<th>创建者</th>
			<th>创建日期</th>
			<th class="hide">上级模块</th>
		</tr>
		<c:forEach var="module" varStatus="stat" items="${page.content}">
			<%
				String json = JsonUtils.toJson(pageContext.getAttribute("module"));
				pageContext.setAttribute("json", json);
			%>
			<tr>
				<td><input type="checkbox" name="single"  /><em class="hidden">${json}</em></td>
				<td>${stat.index + 1}</td>
				<td>${module.modName}</td>
				<td>${module.modEnId}</td>
				<td>${module.modImgCls}</td>
				<td><tags:data name="modLevel" value="${module.modLevel}"></tags:data></td>
				<td>${module.modOrder}</td>
				<td><tags:data name="systemType" value="${module.belongToSys}"></tags:data></td>
				<td><tags:data name="modPageType" value="${module.modPageType}"></tags:data></td>
				<td><tags:data name="status" value="${module.status}"></tags:data></td>
				<td class="hide"></td>
				<td>${module.creator}</td>
				<td><fmt:formatDate value="${module.createDate}" pattern="yyyy-MM-dd" /></td>
				<td class="hide"></td>
			</tr>
		</c:forEach>
	</table>
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>