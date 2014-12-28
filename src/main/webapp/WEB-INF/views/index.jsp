<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统平台, 统一用户</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
	
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/js/docs.js" type="text/javascript"></script>
</head>
<body>
	<%@ include file="/WEB-INF/layout/header.jsp" %>
	<div class="container-fluid mycuckoo-container">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar mycuckoo-sidebar">
				<%@ include file="/WEB-INF/layout/sidebar.jsp"%>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main mycuckoo-main">

					<div id="welcome-page" class="active">
						<h1 class="page-header">MyCuckoo Main Page</h1>
						<div class="row placeholders">
							<div class="col-xs-6 col-sm-3 placeholder">
								<img src="" data-src="holder.js/200x200/auto/sky" class="img-responsive" alt="200x200">
								<h4>1</h4>
							</div>
							<div class="col-xs-6 col-sm-3 placeholder">
								<img src="" data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="200x200">
								<h4>2</h4>
							</div>
							<div class="col-xs-6 col-sm-3 placeholder">
								<img src="" data-src="holder.js/200x200/auto/sky" class="img-responsive" alt="200x200">
								<h4>3</h4>
							</div>
							<div class="col-xs-6 col-sm-3 placeholder">
								<img src="" data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="200x200">
								<h4>4</h4>
							</div>
						</div>
					</div>

			</div>
		</div>
	</div>
</body>
</html>
