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
				<label class="sr-only">用户号</label>
				<input type="text" class="form-control input-sm" name="userCode" value="${param.userCode}" placeholder=用户号 />
			</div>
			<div class="form-group">
				<label class="sr-only">用户名</label>
				<input type="text" class="form-control input-sm" name="userName" value="${param.userName}" placeholder=用户名 />
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
			<th>用户号</th>
			<th>用户名</th>
			<th class="hidden">密码</th>
			<th>性别</th>
			<th>职位</th>
			<th class="hidden">用户照片(单击照片删除)</th>
			<th class="hidden">选择照片</th>
			<th>用户QQ</th>
			<th class="hidden">用户MSN</th>
			<th>用户手机</th>
			<th class="hidden">用户手机2</th>
			<th class="hidden">办公电话</th>
			<th class="hidden">家庭电话</th>
			<th class="hidden">家庭住址</th>
			<th>用户邮件</th>
			<th class="hidden">用户有效期</th>
			<th>所属机构</th>
			<th>所属角色</th>
			<th>用户状态</th>
			<th class="hidden">备注</th>
			<th>创建人</th>
			<th>创建日期</th>
		</tr>
		<c:forEach var="user" varStatus="stat" items="${page.content}">
		<%
			String json = JsonUtils.toJson(pageContext.getAttribute("user"));
			pageContext.setAttribute("json", json);
		%>
		<tr>
			<td><input type="checkbox" name="single" /><em class="hidden">${json}</em></td>
			<td>${stat.index + 1}</td>
			<td>${user.userCode}</td>
			<td>${user.userName}</td>
			<td class="hidden"></td>
			<td><tags:data name="gender" value="${user.userGender}"></tags:data></td>
			<td>${user.userPosition}</td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td>${user.userQq}</td>
			<td class="hidden"></td>
			<td>${user.userMobile}</td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td class="hidden"></td>
			<td>${user.userEmail}</td>
			<td class="hidden"></td>
			<td>${user.uumOrgName}</td>
			<td>${user.uumRoleName}</td>
			<td><tags:data name="status" value="${user.status}"></tags:data></td>
			<td class="hidden"></td>
			<td>${user.creator}</td>
			<td><fmt:formatDate value="${user.createDate}" pattern="yyyy-MM-dd" /></td>
		</tr>
		</c:forEach>
	</table>
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>