<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>密码设置</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
</head>
<body>
	<%@ include file="/WEB-INF/layout/header.jsp" %>
	<div class="container-fluid mycuckoo-container">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar mycuckoo-sidebar">
				<%@ include file="/WEB-INF/layout/sidebar.jsp"%>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main mycuckoo-main">
				<div class="row">
					<div class="col-sm-12 col-md-12">
						<c:if test="${not empty param.error}">
							<div class="alert alert-danger">提示: ${param.error}</div>
						</c:if>
						<h3 class="page-header">设置新密码</h3>
						<form class="form-horizontal" name="editForm" action="updateUserInfo" method="post">
							<div class="form-group">
								<label class="col-sm-4 col-md-2 control-label">用户号:</label>
								<div class="col-sm-6 col-md-3">
									<p class="form-control-static">${sessionScope.userCode}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 col-md-2 control-label">用户名:</label>
								<div class="col-sm-6 col-md-3">
									<p class="form-control-static">${sessionScope.userName}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 col-md-2 control-label">密&nbsp;码:</label>
								<div class="col-sm-6 col-md-3">
									<input type="password" class="form-control" name="userPassword"  />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 col-md-2 control-label">新的密码:</label>
								<div class="col-sm-6 col-md-3">
									<input type="password" class="form-control" name="userNewPassword"  />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 col-md-2 control-label">确认密码:</label>
								<div class="col-sm-6 col-md-3">
									<input type="password" class="form-control" name="userConfirmPassword"  />
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-4 col-md-2 col-sm-offset-4 col-md-offset-2">
									<button type="submit" class="btn btn-primary">确认</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script type="text/javascript">
		jQuery(function($) {
			
		});
	</script>
</body>
</html>