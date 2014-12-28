<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<!-- 管理员选择 -->
<div>
	<c:if test="${'add' eq param.userAddDelFlag }">
	<div>
		<form class="form-inline" name="searchForm">
			<input type="hidden" name="userAddDelFlag" value="${param.userAddDelFlag}" /> 
			<div class="form-group">
				<label class="sr-only">用户号&nbsp;</label>
				<input type="text" class="form-control input-sm" name="userCode" value="${param.userCode}" placeholder=用户号 />
			</div>
			<div class="form-group">
				<label class="sr-only">用户名&nbsp;</label>
				<input type="text" class="form-control input-sm" name="userName" value="${param.userName}" placeholder=用户名 />
			</div>
			<button type="button" class="btn btn-info btn-sm" name="search">&nbsp;查询
				<span class="glyphicon glyphicon-search"></span>
			</button>
			<button type="button" class="btn btn-default btn-sm" name="clear">&nbsp;清空
				<span class="glyphicon glyphicon-repeat"></span>
			</button>
		</form>
	</div>
	</c:if>
	<table class="table table-striped table-hover table-condensed">
		<tr>
			<th><input type="checkbox" name="all" /></th>
			<th>序号</th>
			<th>用户号</th>
			<th>用户名</th>
			<th>性别</th>
			<th>职位</th>
			<th>所属机构</th>
			<th>所属角色</th>
		</tr>
		<c:forEach var="user" varStatus="stat" items="${page.content}">
			<tr>
				<td>
					<input type="checkbox" name="single" />
					<input type="hidden" name="userCode" value="${user.userCode}" />
				</td>
				<td>${stat.index + 1}</td>
				<td>${user.userCode}</td>
				<td>${user.userName}</td>
				<td><tags:data name="gender" value="${user.userGender}"></tags:data></td>
				<td>${user.userPosition}</td>
				<td>${user.uumOrgName}</td>
				<td>${user.uumRoleName}</td>
			</tr>
		</c:forEach>
	</table>
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>
