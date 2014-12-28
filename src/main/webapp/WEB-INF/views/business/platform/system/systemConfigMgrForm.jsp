<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统平台</title>
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
						<h3 class="page-header">系统配置管理</h3>
						<div class="row">
							<div class="col-sm-6 col-md-6">
								<div class="thumbnail">
									<div class="caption">
										<form action="#" class="form-inline">
											<div class="form-group">
												<label class="control-label" for="systemName">系统名称&nbsp;</label>
												<input type="text" class="form-control" name="systemName" value="${systemConfigBean.systemName}" required/>
											</div>
											<p></p>
											<p><button type="button" class="btn btn-primary" data-loading-text="处理中...">设置</button></p>
										</form>
									</div>
								</div>
							</div>
							<div class="col-sm-6 col-md-6">
								<div class="thumbnail">
									<div class="caption">
										<form action="#" class="form-inline">
											<div class="form-group">
												<label class="control-label" for="systemMgr">管理员列表&nbsp;</label>
												<select class="form-control" name="systemMgr">
													<c:forEach var="systemMgr" items="${systemConfigBean.systemMgr}">
														<option value="${systemMgr}">${systemMgr}</option>
													</c:forEach>
												</select>
											</div>
											<p></p>
											<p>
												<button type="button" class="btn btn-primary" data-loading-text="处理中...">增加</button>
												<button type="button" class="btn" data-loading-text="处理中...">删除</button>
											</p>
										</form>
									</div>
								</div>
							</div>
							<div class="col-sm-6 col-md-6">
								<div class="thumbnail">
									<div class="caption">
										<form action="#" class="form-inline">
											<label class="control-label">日志级别</label>
											<div class="radio">
												<label class="radio-inline"><input type="radio" name=loggerLevel value="1" 
														<c:if test="${systemConfigBean.loggerLevel eq '1'}">checked</c:if> />增、删、改</label>
												<label class="radio-inline"><input type="radio" name=loggerLevel value="2" 
														<c:if test="${systemConfigBean.loggerLevel eq '2'}">checked</c:if> />删、改、其它</label>
												<label class="radio-inline"><input type="radio" name=loggerLevel value="3" 
														<c:if test="${systemConfigBean.loggerLevel eq '3'}">checked</c:if> />删、其它</label>
												<label class="radio-inline"><input type="radio" name=loggerLevel value="0" 
														<c:if test="${systemConfigBean.loggerLevel eq '0'}">checked</c:if> />关闭</label>
											</div>
											<p></p>
											<p><button type="button" class="btn btn-primary" data-loading-text="处理中...">设置</button></p>
										</form>
									</div>
								</div>
							</div>
							<div class="col-sm-6 col-md-6">
								<div class="thumbnail">
									<div class="caption">
										<form action="#" class="form-inline">
											<div class="form-group">
												<label class="control-label" for="logRecordKeepDays">保留天数&nbsp;</label>
												<select class="form-control" name="logRecordKeepDays">
													<option value="-1" <c:if test="${systemConfigBean.logRecordKeepDays eq '-1'}">selected</c:if>>不设置</option>
													<option value="3" <c:if test="${systemConfigBean.logRecordKeepDays eq '3'}">selected</c:if>>3天</option>
													<option value="7" <c:if test="${systemConfigBean.logRecordKeepDays eq '7'}">selected</c:if>>7天</option>
													<option value="15" <c:if test="${systemConfigBean.logRecordKeepDays eq '15'}">selected</c:if>>15天</option>
													<option value="30" <c:if test="${systemConfigBean.logRecordKeepDays eq '30'}">selected</c:if>>30天</option>
													<option value="60" <c:if test="${systemConfigBean.logRecordKeepDays eq '60'}">selected</c:if>>60天</option>
													<option value="90" <c:if test="${systemConfigBean.logRecordKeepDays eq '90'}">selected</c:if>>90天</option>
													<option value="180" <c:if test="${systemConfigBean.logRecordKeepDays eq '180'}">selected</c:if>>180天</option>
													<option value="360" <c:if test="${systemConfigBean.logRecordKeepDays eq '360'}">selected</c:if>>360天</option>
												</select>
											</div>
											<p></p>
											<p><button type="button" class="btn btn-primary" data-loading-text="处理中...">设置</button></p>
										</form>
									</div>
								</div>
							</div>
							<div class="col-sm-6 col-md-6">
								<div class="thumbnail">
									<div class="caption">
										<form action="#" class="form-inline">
											<div class="form-group">
												<label>监控设置</label><br>
												<b>windows tomcat监控,在catalina.bat加入:</b><br>
												<code>set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_18</code><br>
												<code>set PATH=%PATH%;%JAVA_HOME%\bin</code><br>
												<code>set CLASSPATH=%CLASSPATH%;%JAVA_HOME%</code><br>
												<code>set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote</code><br>
												<code>-Dsun.io.useCanonCaches=false -Dcom/sun/management/jmxremote/port=9999</code><br>
												<code>-Dcom/sun/management/jmxremote/ssl=false</code><br>
												<code>-Dcom/sun/management/jmxremote/authenticate=false</code><br>
												<code>-Xms256m -Xmx512m -XX:MaxNewSize=256m -XX:MaxPermSize=256m</code>
											</div>
											<p></p>
											<p><button type="button" class="btn btn-primary" data-loading-text="处理中...">启动</button></p>
										</form>
									</div>
								</div>
							</div>
							<div class="col-sm-6 col-md-6">
								<div class="thumbnail">
									<div class="caption">
										<form action="#" class="form-inline">
											<label class="control-label">行权限级别</label>
											<div class="radio">
												<label class="radio-inline"><input type="radio" name=rowPrivilegeLevel value="org"  <c:if test="${systemConfigBean.rowPrivilegeLevel eq 'org'}">checked</c:if> />机构</label>
												<label class="radio-inline"><input type="radio" name=rowPrivilegeLevel value="rol"  <c:if test="${systemConfigBean.rowPrivilegeLevel eq 'rol'}">checked</c:if> />角色</label>
												<label class="radio-inline"><input type="radio" name=rowPrivilegeLevel value="usr"  <c:if test="${systemConfigBean.rowPrivilegeLevel eq 'usr'}">checked</c:if> />用户</label>
											</div>
											<p></p>
											<p><button type="button" class="btn btn-primary" data-loading-text="处理中...">设置</button></p>
										</form>
									</div>
								</div>
							</div>
						</div>
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
			var $forms = $main.find('.thumbnail form');
			$forms.off('click', 'button');
			$forms.eq(0).on('click', 'button', function() { // 系统名称设置
				submitForm(this, 0);
			});
			$forms.eq(1).on('click', 'button', function() { // 系统管理员设置
				setSystemAdmin(this);
			});
			$forms.eq(2).on('click', 'button', function() { // 系统日志级别设置
				submitForm(this, 2);
			});
			$forms.eq(3).on('click', 'button', function() { // 系统日志保留天数设置
				submitForm(this, 3);
			});
			$forms.eq(4).on('click', 'button', function() { // 系统监控设置
				$.post('startJConsole', function(json) {
					if(json.status) {
						MyCuckoo.showMsg({state:'success', title: '提示', msg: '启动成功'});
					} else {
						MyCuckoo.showMsg({state:'danger', title: '提示', msg: '启动失败'});
					}
				});
			});
			$forms.eq(5).on('click', 'button', function() { // 系统行权限设置
				submitForm(this, 5);
			});
			/**
			 * 异步提交表单
			 * 
			 * @param element 按钮
			 * @param index 表单索引
			 */
			function submitForm(element, index) {
				var $form = $forms.eq(index);
				// 验证表单
				if($form.validate().form()) {
					$(element).button('loading');
					var params = $form.serialize();
					$.post('setSystemConfigInfo', params, function(json) {
						if(json.status) {
							MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.msg});
						}
					}, 'json')
					.always(function() {
						$(element).button('reset');
					});
				}
			}
			/**
			 * 设置管理员账号
			 * 
			 * @param 按钮
			 */
			function setSystemAdmin(element) {
				var flag = 'delete';
				if($(element).hasClass('btn-primary')) {
					flag = 'add';
				}
				var $modal = $(MyCuckoo.modalTemplate);
				$modal.find('.modal-header h3').text('选择用户');
				$modal.find('.modal-body').load('getUserForSetAdmin', {userAddDelFlag :  flag}, function() {
					$modal.appendTo($('body'));
					$modal.modal();
				});
				$modal.on('show.bs.modal', function() {
					modalCallBack();
				})
					.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); })
					.off('click', '.modal-footer > .btn-primary')
					.on('click', '.modal-footer > .btn-primary', function() {
						var $singleCheck = $modal.find(".table input:checked[name='single']");
						if($singleCheck.size() == 0) {
							MyCuckoo.alertMsg({title: '提示', msg: '请选择一条或多条记录!'});
							return;
						}
						var userCodeList = [];
						$singleCheck.each(function() {
							userCodeList.push($(this).next().val());
						});
						$.post('setSystemConfigInfo', {userAddDelFlag :  flag, systemMgr : userCodeList.join(',')}, function(json) {
							if(json.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
							} else {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.msg});
							}
							
							$modal.modal('hide');
						}, 'json');
					});
				function modalCallBack() {
					// form查询
					var $form = $modal.find('form[name="searchForm"]');
					$form.off('click', 'button[name="search"]');
					$form.on('click', 'button[name="search"]', function() {
						var params = MyCuckoo.fromQueryString($form.serialize());
						// 刷新列表
						$modal.find('.modal-body:first').load('getUserForSetAdmin', params, function() {
							modalCallBack();
						});
					});
					// 清空
					$form.off('click', 'button[name="clear"]');
					$form.on('click', 'button[name="clear"]', function() {
						$(':input', $form)
							.not(':button, :submit, :reset, :hidden')
							.val('')
							.removeAttr('checked')
							.removeAttr('selected');
					});
					// page分页
					$modal.off('click', '.pagination li > a');
					$modal.on('click', '.pagination li > a', function(event) {
						event.preventDefault(); // 阻止超链接点击事件
						var cls = $(this).parent('li').attr('class');
						if(cls == 'disabled' || cls == 'active') return;
						
						var params = MyCuckoo.fromQueryString($(this).attr('href'));
						// 刷新列表
						$modal.find('.modal-body:first').load('getUserForSetAdmin', params, function() {
							modalCallBack();
						});
					});
					// 复选框
					MyCuckoo.checkbox($modal);
				}
			}
		});
	</script>
</body>
</html>