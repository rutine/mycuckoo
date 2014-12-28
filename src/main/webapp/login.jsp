<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>登陆界面</title>
	
	<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
	<link href="${ctx}/static/bootstrap/3.2.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctx}/static/css/login.css" type="text/css" rel="stylesheet" />
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<form id="login_form" action="${ctx}/login" method="post" class="form-horizontal form-signin">
					<div class="form-group">
						<div class="col-md-8 col-md-offset-3">
							<h1 class="form-control-static">系统平台&统一用户</h1>
						</div>
					</div>
					<div class="form-group has-feedback">
						<div class="col-md-4 col-md-offset-4">
							<div class="input-group">
								<span class="input-group-addon glyphicon glyphicon-user"></span>
								<input type="text" class="form-control" id="user_code" name="userCode" 
										value="${username}" placeholder="用户" required autofocus/>
							</div>
						</div>
						<div class="col-md-4"><span class="text-danger hidden error">请输入用户名!</span></div>
					</div>
					<div class="form-group has-feedback">
						<div class="col-md-4 col-md-offset-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
								<input type="password" class="form-control" id="password" name="password" placeholder="密码" required />
							</div>
						</div>
						<div class="col-md-4"><span class="text-danger hidden error">请输入密码!</span></div>
					</div>
					<div class="form-group hidden">
						<div class="col-md-4 col-md-offset-4">
							<div class="input-group">
								<span class="input-group-addon role">角色</span>
								<select class="form-control" id="org_role"></select>
							</div>
						</div>
					</div>
					<div class="form-group hidden">
						<div class="col-md-4 col-md-offset-4">
							<div class="input-group">
								<span class="input-group-addon role">代理</span>
								<select class="form-control" id="agent"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-4 col-md-offset-4">
							<input type="button" class="btn btn-primary btn-lg" 
									id="submit_btn" value="&nbsp;&nbsp;&nbsp;登录&nbsp;&nbsp;&nbsp;" data-loading-text="登陆中..."/>
						</div>
					</div>
					<!-- 多角色值 -->
					<input type="hidden" id="user_role_login_hidden_params_id" />
					<!-- 多角色标志 -->
					<input type="hidden" id="multi_role_flag_id" />
					<!-- 多代理值 -->
					<input type="hidden" id="user_agent_login_hidden_params_id" />
				</form>
			</div>
		</div>
	</div>
	<script src="${ctx}/static/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/bootstrap/3.2.0/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/js/util/commUtils.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function($) {
			var $bground = $('<div class="background"></div>');
			var $loginForm = $('#login_form');
			var $userCode = $('#user_code');
			var $password = $('#password');
			var $orgRole = $('#org_role');
			var $agent = $('#agent');
			var $helpText = $loginForm.find('.text-danger');
			var $hiddenFormGroup = $loginForm.find('div[class*=hidden]'); // 角色、代理wrap<div>
			
			$('#submit_btn').button('reset');
			// 登陆
			$loginForm.on('click', '#submit_btn', function(e) {
				if (!$userCode.val()) {
					$userCode.focus();
				} else if (!$password.val()) {
					$password.focus();
				} else if(!$orgRole.data('userRoleLoginParams') 
						&& !$agent.data('userAgentLoginParams')) {
					MyCuckoo.showMsg({state: 'danger', title: '警告', msg: '无角色权限!', duration: 1000});
				} else {
					loginSys();
				}
			});
			// 捕获enter
			$loginForm.on('keyup', function(e) {
				switch(e.keyCode) {
				case 13 :
					if (!$userCode.val()) {
						$userCode.focus();
					} else if (!$password.val()) {
						$password.focus();
					} else if(!$orgRole.data('userRoleLoginParams') 
							&& !$agent.data('userAgentLoginParams')) {
						MyCuckoo.showMsg({state: 'danger', title: '警告', msg: '无角色权限!', duration: 1000});
					} else {
						loginSys();
					}
					break;
				}
			});
			// 用户改变事件
			$loginForm.on('blur', '#user_code', function() {
				if (!$(this).val()) {
					$helpText.eq(0).removeClass('hidden');
					// 隐藏和清空角色、代理选项
					$hiddenFormGroup.addClass('hidden');
					$orgRole.data('userRoleLoginParams', null);
					$agent.data('userAgentLoginParams', null);
					$('#multi_role_flag_id').val(''); // 多角色清空
				} else {
					$helpText.eq(0).addClass('hidden');
					preLogin();
				}
			});
			// 密码改变事件
			$loginForm.on('blur', '#password', function() {
				if (!$(this).val()) {
					$helpText.eq(1).removeClass('hidden');
				} else {
					$helpText.eq(1).addClass('hidden');
				}
			});
			
			var preLogin = function() {
				$bground.appendTo(($('body')));
				MyCuckoo.showMsg({state: 'info', title: '提示', msg: ' 预登录中,请稍候... '});
				$.getJSON('${ctx}/login/preLogin', {userCode : $userCode.val()}, function(json) {
					// 角色列表
					var roleList = json.roleList;
					if(roleList) {
						if(roleList.length > 1) {
							$('#multi_role_flag_id').val('Y'); // 多角色用户
							var _html = '';
							for(var i = 0, len = roleList.length; i < len; i++) {
								var roleUser = roleList[i];
								if(roleUser.isDefault == 'Y') {
									roleUser.roleName = roleUser.organName + ' - ' + roleUser.roleName + '(默认)';
									_html += '<option value=' + i + ' selected="selected">' + roleUser.roleName + '</option>';
									$orgRole.data('userRoleLoginParams', roleUser);
								} else {
									roleUser.roleName = roleUser.organName + ' - ' + roleUser.roleName;
									_html += '<option value=' + i + '>' + roleUser.roleName + '</option>';
								}
								
							}
							$orgRole.html(_html);
							$hiddenFormGroup.eq(0).removeClass('hidden');
						} else {
							$hiddenFormGroup.eq(0).addClass('hidden');
							$orgRole.data('userRoleLoginParams', roleList[0]);
							$('#multi_role_flag_id').val(''); // 多角色清空
						}
					} else {
						$hiddenFormGroup.eq(0).addClass('hidden');
						$orgRole.data('userRoleLoginParams', null);
						$('#multi_role_flag_id').val(''); // 多角色清空
					}
					$orgRole.off('change').on('change', function() {
						$hiddenFormGroup.eq(1).addClass('hidden');
						$agent.data('userAgentLoginParams', null); // 代理值清空
						$orgRole.data('userRoleLoginParams', roleList[$(this).children('option:selected').val()]);
					});
					// 用户代理列表
					var userAgentList = json.userAgentList;
					if(userAgentList && userAgentList.length > 0) {
						var _html = '<option>请选择代理...</option>';
						for(var i = 0, len = userAgentList.length; i < len; i++) {
							var userAgent = userAgentList[i];
							_html += '<option value=' + i + '>' + userAgent.userName + '</option>';
						}
						$agent.html(_html);
						$hiddenFormGroup.eq(1).removeClass('hidden');
					} else {
						$hiddenFormGroup.eq(1).addClass('hidden');
						$agent.data('userAgentLoginParams', null);
					}
					$agent.off('change').on('change', function() {
						$hiddenFormGroup.eq(0).addClass('hidden');
						var index = $(this).children('option:selected').val();
						if(index >= 0) {
							$agent.data('userAgentLoginParams', userAgentList[index]);
						} else {
							$agent.data('userAgentLoginParams', null);
						}
						$('#multi_role_flag_id').val(''); // 选择代理时，多角色为''
					});
				}).always(function() {
					setTimeout(function() { 
						$bground.remove();
					}, 1000);
				});
			};
			var loginSys = function() {
				$('#submit_btn').button('loading');
				var userCode = $userCode.val();
				var password = $password.val();
				var userRoleLoginParams = JSON.stringify($orgRole.data('userRoleLoginParams'));
				var userAgentLoginParams = JSON.stringify($agent.data('userAgentLoginParams'));
				$.post('${ctx}/login/loginSysFirstDo', 
						{
							userCode: userCode, 
							password: password, 
							roleInfoJson: userRoleLoginParams, 
							agentInfoJson: userAgentLoginParams
						}, function(json) {
							var code = json.code;
							var msg = json.msg;
							switch(code) {
							case 1:
								msg = '对不起,您输入的用户名或密码有误!';
								break;
							case 2:
								msg = '对不起,当前用户没有使用系统的权限,或请联系系统管理员!';
								break;
							case 3:
								msg = '对不起,当前用户已被停用,或请联系系统管理员!';
								break;
							case 4:
								msg = '对不起,当前用户已过有效期,或请联系系统管理员!';
								break;
							}
							if(msg) {
								MyCuckoo.showMsg({state: 'info', title: '提示', msg: msg});
							} else {
								window.location = "${ctx}/login/loginForward?multiRole=" + $('#multi_role_flag_id').val() +"&userCode=" + userCode;
							}
				}, "json").always(function() {
					setTimeout(function() { 
						$('#submit_btn').button('reset');
					}, 2000);
				});
			};
		});
	</script>
</body>
</html>