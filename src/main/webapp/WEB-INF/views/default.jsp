<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:if test="${not empty sessionScope.userCode}">
	<c:redirect url="/${sessionScope.userCode}/index"/>
</c:if>
<c:redirect url="/login.jsp"/>