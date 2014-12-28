<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<div>
	<h4>${param.modName}<input type="hidden" value="${param.moduleId}" /></h4>
	<div class="select-side">
		<span class="muted">未选操作</span>
		<select id="left_multi_select" multiple="multiple">
			<c:forEach var="operate" items="${unassignedModOpts}">
				<option value="${operate.operateId}">${operate.operateName}</option>
			</c:forEach>
		</select>
	</div>
	<div class="select-opt">
		<p class="to-right" title="添加">&lt;&lt;</p>
		<p class="to-left" title="移除">&gt;&gt;</p>
	</div>
	<div class="select-side">
		<span class="muted">已选操作</span>
		<select id="right_multi_select" multiple="multiple">
			<c:forEach var="operate" items="${assignedModOpts}">
				<option value="${operate.operateId}">${operate.operateName}</option>
			</c:forEach>
		</select>
	</div>
	<div class="clearfix"></div>
</div>