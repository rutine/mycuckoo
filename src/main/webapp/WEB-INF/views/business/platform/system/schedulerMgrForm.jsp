<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统平台</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
	<link href="${ctx}/static/bootstrap-datetimepicker/master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
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
						<h3 class="page-header">系统调度管理</h3>
						<!-- 表单操作按钮 -->
						<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
						<form class="form-inline" name="editForm" action="${action}Form" method="post">
							<input type="hidden" name="opt" value="" />
							<input type="hidden" name="jobId" value="${scheduler.jobId}"/>
							<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<p><strong class="text-info">基本信息</strong></p>
								<table class="table">
									<tr>
										<td width=14%><label>任务名称</label></td>
										<td><input type=text  name="jobName" value="${scheduler.jobName}" class="required alphanumeric" maxlength="30"/></td>
										<td width=14%><label>任务类描述</label></td>
										<td><input type=text name="jobClassDescript" value="${scheduler.jobClassDescript}" class="required" maxlength="100" /></td>
									</tr>
									<tr>
										<td width=14%><label>触发器类型</label></td>
										<td>
											<div class="form-group">
												<label class="radio"><input type="radio" name=triggerType value="Simple" 
													<c:if test="${empty scheduler.triggerType or scheduler.triggerType eq 'Simple'}">checked</c:if> />Simple</label>
												<label class="radio"><input type="radio" name=triggerType value="Cron" 
														<c:if test="${scheduler.triggerType eq 'Cron'}">checked</c:if> />Cron</label>
												</div>
										</td>
										<td width=14%><label>状态</label></td>
										<td>
											<select name="status" class="required">
												<option value="enable" <c:if test="${scheduler.status eq 'enable'}">selected="selected"</c:if>>启用</option>
												<option value="disable" <c:if test="${scheduler.status eq 'disable'}">selected="selected"</c:if>>停用</option>
											</select>
										</td>
									</tr>
									<tr>
										<td width=14%><label>编码备注</label></td>
										<td colspan="3"><input type="text" name="memo" value="${scheduler.memo}" maxlength="50"/></td>
									</tr>
								</table>
							</div>
							<div class="jumbotron <c:if test="${scheduler.triggerType eq 'Cron'}">hidden</c:if>" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<p><strong class="text-info">任务设置</strong></p>
								<table class="table">
									<tr>
										<td width=14%><label>开始时间</label></td>
										<td>
											<div id="start_time" class="input-group date">
												<input type=text class="form-control required" name="startTime" 
													value="<fmt:formatDate value="${scheduler.startTime}" pattern="yyyy-MM-dd" />" readOnly />
												<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
											</div>
										</td>
										<td width=14%><label>结束时间</label></td>
										<td>
											<div id="end_time" class="input-group date">
												<input type=text class="form-control required" name="endTime" 
													value="<fmt:formatDate value="${scheduler.endTime}" pattern="yyyy-MM-dd" />" readOnly />
												<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
											</div>
										</td>
									</tr>
									<tr>
										<td width=14%><label>重复次数</label></td>
										<td><input type=text name="repeatTime" value="${scheduler.repeatTime}" class="required digits" maxlength="2" <c:if test="${scheduler.triggerType eq 'Cron' }">disabled="disabled"</c:if>/></td>
										<td width=14%><label>时间间隔</label></td>
										<td><input type=text name="splitTime" value="splitTime" class="required digits" maxlength="10" <c:if test="${scheduler.triggerType eq 'Cron' }">disabled="disabled"</c:if>/></td>
									</tr>
								</table>
							</div>
							<div class="jumbotron <c:if test="${empty scheduler.triggerType or scheduler.triggerType eq 'Simple'}">hidden</c:if>" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<p><strong  class="text-info">任务设置</strong></p>
								<table class="table">
									<tr>
										<td width=14%><label>时间表达式</label></td>
										<td><input type=text name="timeExpress" value="${scheduler.timeExpress}" class="required"  maxlength="20" 
													<c:if test="${empty scheduler.triggerType or scheduler.triggerType eq 'Simple' }">disabled="disabled"</c:if>/></td>
									</tr>
								</table>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/bootstrap-datetimepicker/master/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
	<script src="${ctx}/static/bootstrap-datetimepicker/master/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.additional_methods.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.messages_bs_zh.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.main');
			// -------------------绑定表单元素事件-------------------
			var $form = $main.find('form[name=editForm]');
			var $heroUnit = $form.find('div.jumbotron');
			// 触发器类型
			$form.on('click', 'input[name=triggerType]:radio', function() {
				if(this.value == 'Simple') {
					$heroUnit.eq(1).removeClass('hidden');
					$heroUnit.eq(1).find(':input').removeAttr('disabled');
					$heroUnit.eq(2).addClass('hidden');
					$heroUnit.eq(2).find(':input').attr('disabled', 'disabled');
				} else {
					$heroUnit.eq(1).addClass('hidden');
					$heroUnit.eq(1).find(':input').attr('disabled', 'disabled');
					$heroUnit.eq(2).removeClass('hidden');
					$heroUnit.eq(2).find(':input').removeAttr('disabled');
				}
			});
			// 开始时间，结束时间
			var datetimepickerConfig = {
					language : 'zh-CN',
					format : 'yyyy-mm-dd',
					autoclose : 1,
					todayHighlight: 1,
					todayBtn : 'linked',
					startView : 2,
					minView : 2,
					forceParse : 0
			};
			$form.find('#start_time').datetimepicker(datetimepickerConfig);
			$form.find('#end_time').datetimepicker(datetimepickerConfig);
			// ---------------------------------------------------------
			// 操作按钮
			$main.on('click', '.btn-toolbar .btn-group a', function(event) {
				event.preventDefault();
				$(this).button('loading');
				switch($(this).attr('href')) {
				case 'save':
					if($form.validate().form()) {
						$form.submit();
					}
					break;
				case 'saveadd':
					if($form.validate().form()) {
						$form.find('input[name=opt]').val('saveadd');
						$form.submit();
					}
					break;
				case 'reback':
					window.location = 'index';
					break;
				}
				$(this).button('reset');
			});
		});
	</script>
</body>
</html>