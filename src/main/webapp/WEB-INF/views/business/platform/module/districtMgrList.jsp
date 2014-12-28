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
				<label class="sr-only">地区名称</label>
				<input type="text" class="form-control input-sm" name="districtName" value="${param.districtName}" placeholder=地区名称 />
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
			<th>地区名称</th>
			<th>地区代码</th>
			<th>地区邮编</th>
			<th>电话区号</th>
			<th>地区级别</th>
			<th>地区状态</th>
			<th class="hide">模块备注</th>
			<th>创建者</th>
			<th>创建日期</th>
			<th class="hide">上级地区</th>
		</tr>
		<c:forEach var="district" varStatus="stat" items="${page.content}">
		<%
			String json = JsonUtils.toJson(pageContext.getAttribute("district"));
			pageContext.setAttribute("json", json);
		%>
		<tr>
			<td><input type="checkbox" name="single"  /><em class="hide">${json}</em></td>
			<td>${stat.index + 1}</td>
			<td>${district.districtName}</td>
			<td>${district.districtCode}</td>
			<td>${district.districtPostal}</td>
			<td>${district.districtTelcode}</td>
			<td>${district.districtLevel}</td>
			<td><tags:data name="status" value="${district.status}"></tags:data></td>
			<td class="hide"></td>
			<td>${district.creator}</td>
			<td><fmt:formatDate value="${district.createDate}" pattern="yyyy-MM-dd" /></td>
			<td class="hide"></td>
		</tr>
		</c:forEach>
	</table>
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>