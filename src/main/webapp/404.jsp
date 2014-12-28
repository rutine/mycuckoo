<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<%
	response.setStatus(200);
%>
<!DOCTYPE html>
<html>
<head>
	<title>404 - 页面不存在</title>
	<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/error.css" media="screen" />
</head>
<body>
	<div id="container">
		<img class="png" src="${ctx}/static/images/404.png" /> 
		<img class="png msg" src="${ctx}/static/images/404_msg.png" />
		<p>
			<a href="<c:url value="/index"/>">
				<img class="png" src="${ctx}/static/images/404_to_index.png" />
			</a>
		</p>
	</div>
	<div id="cloud" class="png"></div>
	<pre style="DISPLAY: none"></pre>
</body>
</html>