<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.mycuckoo.common.utils.JsonUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<div>
	<table class="table table-striped table-hover table-condensed">
		<tr>
			<th><input type="checkbox" name="all"/></th>
			<th>序号</th>
			<th>角色名称</th>
			<th>角色状态</th>
			<th class="hidden">备注</th>
			<th>创建人</th>
			<th>创建日期</th>
		</tr>
		<c:forEach var="role" varStatus="stat" items="${page.content}">
		<%
			String json = JsonUtils.toJson(pageContext.getAttribute("role"));
			pageContext.setAttribute("json", json);
		%>
		<tr>
			<td><input type="checkbox" name="single"  /><em class="hidden">${json}</em></td>
			<td>${stat.index + 1}</td>
			<td>${role.roleName}</td>
			<td><tags:data name="status" value="${role.status}"></tags:data></td>
			<td class="hidden"></td>
			<td>${role.creator}</td>
			<td><fmt:formatDate value="${role.createDate}" pattern="yyyy-MM-dd" /></td>
		</tr>
		</c:forEach>
	</table>	
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>