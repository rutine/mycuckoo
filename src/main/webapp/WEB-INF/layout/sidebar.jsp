<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<c:forEach var="firstMenu" varStatus="stat" items="${firstList}">
<div <c:if test="${!stat.first}">style="display:none;"</c:if> id="${firstMenu.moduleId}_menu">
	<ul class="nav nav-sidebar mycuckoo-sidenav">
		<li>
			<a class="usedfuncmaint-hidden">常用功能<span class="glyphicon glyphicon-chevron-up"></span></a>
			<ul class="nav">
			<c:forEach var="uumUserCommfun" items="${assignUumUserCommfunList}">
				<c:set var="url" value="${ctx}/menu/${uumUserCommfun.belongToSys}/${uumUserCommfun.modEnId}
					?&modPageType=${uumUserCommfun.modPageType}" />
				<li><a href="${url}" class="${uumUserCommfun.modImgCls}-hidden">${uumUserCommfun.modName}</a></li>
			</c:forEach>
			</ul>
		</li>
		<c:set var="secondList" value="${secondMap[fn:trim(firstMenu.moduleId)]}" />
		<c:forEach var="secondMenu" varStatus="stat2" items="${secondList}">
		<li>
			<a class="${secondMenu.modImgCls}-hidden">${secondMenu.modName}
			<c:choose>
				<c:when test="${stat2.first}"><span class="glyphicon glyphicon-chevron-down"></span></c:when>
				<c:otherwise><span class="glyphicon glyphicon-chevron-left"></span></c:otherwise>
			</c:choose>
			</a>
			<ul class="nav" <c:if test="${stat2.first}">style="display:block;"</c:if>>
				<c:set var="thirdList" value="${thirdMap[fn:trim(secondMenu.moduleId)]}" />
				<c:forEach var="thirdMenu" items="${thirdList}">
					<c:set var="url" value="${ctx}/menu/${thirdMenu.belongToSys}/${thirdMenu.modEnId}
						/index?moduleId=${thirdMenu.moduleId}&modPageType=${thirdMenu.modPageType}" />
					<li <c:if test="${sessionScope.modEnId eq thirdMenu.moduleId}">class="active"</c:if>>
						<a href="${url}" class="${thirdMenu.modImgCls}-hidden">${thirdMenu.modName}</a>
					</li>
				</c:forEach>
			</ul>
		</li>
		</c:forEach>
	</ul>
</div>
</c:forEach>