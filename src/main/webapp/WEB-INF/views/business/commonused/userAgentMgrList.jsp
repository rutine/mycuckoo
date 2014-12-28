<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<div>
	<div class="row">
		<div class="col-md-8">
			<!-- 内容列表 -->
			<table class="table table-striped table-hover table-condensed">
				<tr>
					<th>序号</th>
					<th>代理人</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>备注说明</th>
					<th>操作</th>
				</tr>
				<c:forEach var="agent" varStatus="stat" items="${page.content}">
				<tr>
					<td>${stat.index + 1}</td>
					<td>${agent.userName}</td>
					<td><fmt:formatDate value="${agent.beginDate}" pattern="yyyy-MM-dd" /></td>
					<td><fmt:formatDate value="${agent.endDate}" pattern="yyyy-MM-dd" /></td>
					<td>${agent.reason}</td>
					<td>
						<a class="btn btn-default btn-xm" data-id="${agent.agentId}" data-url=getAssignedUserAgentPrivilegeList>查看代理权限</a>
						<a class="btn btn-default btn-xm" data-id="${agent.agentId}" data-url="delete">取消代理</a>
					</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		<div class="col-md-3 col-md-offset-1">
			<ul class="nav">
				<li style="font-size:13px"><strong>权限树</strong></li>
				<li><ul id="tree_agent_user" class="ztree"></ul></li>
			</ul>
		</div>
	</div>
	<!-- 分页 -->
	<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
</div>