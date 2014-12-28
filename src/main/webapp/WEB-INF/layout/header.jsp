<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<header class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${ctx}/${sessionScope.userCode}/index">MyCuckoo</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<c:forEach items="${firstList}" var="firstMenu" varStatus="stat">
				<li class="<c:if test="${stat.first}">active</c:if>"><a href="#${firstMenu.moduleId}_menu">${firstMenu.modName}</a></li>
				<li class="nav-divider"></li>
				</c:forEach>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li>
					<a href="#" class="dropdown-toggle config" data-toggle="dropdown">
						<span class="glyphicon glyphicon-cog"></span> 常用配置<span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="${ctx}/userCommFunMgr/index"><span class="glyphicon glyphicon-tasks"></span> 设置常用功能</a></li>
						<li><a href="${ctx}/userAgentMgr/index"><span class="glyphicon glyphicon-dashboard"></span> 设置代理</a></li>
						<li><a href="${ctx}/uum/userMgr/updateUserInfo"><span class="glyphicon glyphicon-log-in"></span> 设置密码</a></li>
					</ul>
				</li>
				<li class="nav-divider"></li>
				<li><a href="#" class="user" data-photo-url="${sessionScope.userPhotoUrl}"><span class="glyphicon glyphicon-user"></span> 个人中心</a></li>
				<li class="nav-divider"></li>
				<li><a href="#">&nbsp;</a></li>
			</ul>
		</div>
	</div>
</header>
<div class="mycuckoo-header"></div>