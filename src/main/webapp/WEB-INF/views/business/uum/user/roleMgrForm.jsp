<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>统一用户</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
	<link href="${ctx}/static/jquery-validation/1.10.0/css/jquery-validate.css" type="text/css" rel="stylesheet" />
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
						<h3 class="page-header">角色管理</h3>
						<!-- 表单操作按钮 -->
						<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
						<form name="editForm" action="${action}Form" method="post">
							<input type="hidden" name="opt" value="" />
							<input type="hidden" name="roleId" value="${role.roleId}"/>
							<table class="table table-bordered">
								<tr>
									<td width=14%><label>角色名称</label></td>
									<td><input type=text  name="roleName" value="${role.roleName}" class="required" maxlength="10"/></td>
									<td width=14%><label>角色级别</label></td>
									<td>
										<select name="roleLevel" class="required">
											<option value="1" <c:if test="${role.roleLevel eq 1}">selected="selected"</c:if>>1</option>
											<option value="2" <c:if test="${role.roleLevel eq 2}">selected="selected"</c:if>>2</option>
											<option value="3" <c:if test="${role.roleLevel eq 3}">selected="selected"</c:if>>3</option>
											<option value="4" <c:if test="${role.roleLevel eq 4}">selected="selected"</c:if>>4</option>
											<option value="5" <c:if test="${role.roleLevel eq 5}">selected="selected"</c:if>>5</option>
											<option value="6" <c:if test="${role.roleLevel eq 6}">selected="selected"</c:if>>6</option>
											<option value="7" <c:if test="${role.roleLevel eq 7}">selected="selected"</c:if>>7</option>
											<option value="8" <c:if test="${role.roleLevel eq 8}">selected="selected"</c:if>>8</option>
											<option value="9" <c:if test="${role.roleLevel eq 9}">selected="selected"</c:if>>9</option>
										</select>
									</td>
								</tr>
								<tr>
									<td width=14%><label>角色状态</label></td>
									<td>
										<select name="status" class="required">
											<option value="enable" <c:if test="${role.status eq 'enable'}">selected="selected"</c:if>>启用</option>
											<option value="disable" <c:if test="${role.status eq 'disable'}">selected="selected"</c:if>>停用</option>
										</select>
									</td>
									<td width=14%><label>备注</label></td>
									<td><input type="text" name="memo" value="${role.memo}" maxlength="50"/></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.additional_methods.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.messages_bs_zh.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.main');
			var $form = $main.find('form[name="editForm"]');
			// 操作按钮
			$main.on('click', '.btn-toolbar .btn-group a', function(event) {
				event.preventDefault();
				$(this).button('loading');
				switch($(this).attr('href')) {
				case "save":
					if($form.validate().form()) {
						$form.submit();
					}
					break;
				case "saveadd":
					if($form.validate().form()) {
						$form.find('input[name="opt"]').val('saveadd');
						$form.submit();
					}
					break;
				case "reback":
					window.location = 'index';
					break;
				}
				$(this).button('reset');
			});
		});
	</script>
</body>
</html>