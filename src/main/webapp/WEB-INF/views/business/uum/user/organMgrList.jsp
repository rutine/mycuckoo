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
				<label class="sr-only">机构简称</label>
				<input type="text" class="form-control input-sm" name="orgName" value="${param.orgName}" placeholder=机构简称 />
			</div>
			<div class="form-group">
				<label class="sr-only">机构代码</label>
				<input type="text" class="form-control input-sm" name="orgCode" value="${param.orgCode}" placeholder=机构代码 />
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
			<th>机构简称</th>
			<th class="hidden">机构全称</th>
			<th>机构代码</th>
			<th>联系地址1</th>
			<th class="hidden">联系地址2</th>
			<th>联系电话1</th>
			<th class="hidden">联系电话2</th>
			<th>成立日期</th>
			<th class="hidden">机构类型</th>
			<th class="hidden">机构邮编</th>
			<th class="hidden">法人代表</th>
			<th class="hidden">税务号</th>
			<th class="hidden">注册登记号</th>
			<th class="hidden">所属地区</th>
			<th>机构状态</th>
			<th class="hidden">备注</th>
			<th>创建人</th>
			<th>创建日期</th>
			<th class="hidden">上级机构</th>
		</tr>
		<c:forEach var="organ" varStatus="stat" items="${page.content}">
		<%
			String json = JsonUtils.toJson(pageContext.getAttribute("organ"));
			pageContext.setAttribute("json", json);
		%>
		<tr>
			<td><input type="checkbox" name="single" /><em class="hidden">${json}</em></td>
			<td>${stat.index + 1}</td>
			<td>${organ.orgSimpleName}</td>
			<td class="hidden"></td>
			<td>${organ.orgCode}</td>
			<td>${organ.orgAddress1}</td>
			<td class="hidden"></td>
			<td>${organ.orgTel1}</td>
			<td class="hidden"></td>
			<td><fmt:formatDate value="${organ.orgBeginDate}" pattern="yyyy-MM-dd" /></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td><tags:data name="status" value="${organ.status}"></tags:data></td>
			<td class="hidden"></td>
			<td>${organ.creator}</td>
			<td><fmt:formatDate value="${organ.createDate}" pattern="yyyy-MM-dd" /></td>
			<td class="hidden"></td>
		</tr>
		</c:forEach>
	</table>
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>