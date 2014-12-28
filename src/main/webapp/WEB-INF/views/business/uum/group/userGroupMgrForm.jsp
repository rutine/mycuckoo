<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
						<h3 class="page-header">用户组编辑/查看</h3>
						<!-- 表单操作按钮 -->
						<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
						<form name="editForm" action="${action}Form" method="post">
							<input type="hidden" name="opt" value="" />
							<input type="hidden" name="groupId" value="${group.groupId}"/>
							<table class="table">
								<tr>
									<td width=14%><label>组名称</label></td>
									<td><input type=text	name="groupName" value="${group.groupName}" class="required" maxlength="10"/></td>
									<td width=14%><label>状态</label></td>
									<td>
										<select name="status" class="required">
											<option value="enable" <c:if test="${group.status eq 'enable'}">selected="selected"</c:if>>启用</option>
											<option value="disable" <c:if test="${group.status eq 'disable'}">selected="selected"</c:if>>停用</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label>机构</label></td>
									<td>
										<input type=hidden	name="userGroupOrgId"	/>
										<input type=text	name="userGroupOrgName" />
									</td>
									<td><label>角色</label></td>
									<td><select name="userGroupRoleId"></select></td>
								<tr>
									<td></td>
									<td colspan="3">
										<table>
											<tr>
												<td>
													<div class="select-side">
														<span>待选用户</span>
														<select id="left_multi_select" multiple="multiple" style="width: 200px; height:200px; border: 1px solid #0088CC;"></select>
													</div>
												</td>
												<td>
													<div class="select-opt">
														<p class="to-right" title="添加">&lt;&lt;</p>
														<p class="to-left" title="移除">&gt;&gt;</p>
													</div>
												</td>
												<td>
													<div class="select-side">
														<span>已选用户</span>
														<select id="right_multi_select" multiple="multiple" style="width: 200px; height:200px; border: 1px solid #0088CC;">
															<c:forEach var="groupMember" items="${group.uumGroupMembers}">
																<option value="${groupMember.memberResourceId}" selected>${groupMember.memberResourceName}</option>
															</c:forEach>
														</select>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td width=14%><label>备注</label></td>
									<td><input type="text" name="memo" value="${group.memo}" maxlength="50"/></td>
									<td width=14%></td>
									<td></td>
								</tr>
							</table>
							<c:forEach var="groupMember" items="${group.uumGroupMembers}">
								<input type="hidden" name="userIdList" value="${groupMember.memberResourceId}">
							</c:forEach>
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
			var $form = $main.find('form[name=editForm]');
			// 选择机构
			var $orgId = $form.find('input[name=userGroupOrgId]');
			var $orgName = $form.find('input[name=userGroupOrgName]');
			var $roleId = $form.find('select[name=userGroupRoleId]');
			var _setting = {
					async : { enable : true, type : 'get', url : '${ctx}/uum/organMgr/getChildNodes', autoParam : [ 'id=treeId' ] },
					data : {key : {name : 'text'}},
					callback : {
						onClick : function(evane, treeId, treeNode) {
							$orgId.val(treeNode.id);
							$orgName.val(treeNode.text);
							$.getJSON('queryRoleList', {orgId : treeNode.id}, function(json) {
								var _html = '';
								$.each(json, function() {
									_html += '<option value="' + this.roleId + '">' + this.roleName + '</option>';
								});
								$roleId.html(_html).off().on('change', userFind);
								json.length && $roleId.trigger('change');
							});
						}
					}
			};
			var $menu = $('<div class="org-tree" style="position: absolute; display: none; background: #F8FBFE; ' 
						+ 'border: 1px solid #CCCCCC;"><ul class="ztree" style="margin-top:0;"></ul></div>').appendTo('body');
			$.fn.zTree.init($menu.find('.ztree'), _setting);
			
			$orgName.on('click', function() {
				if($menu.find(':visible').length) return;
				
				var h = $(this).outerHeight();
				var w = $(this).outerWidth();
				var offset = $(this).offset();
				$menu.width(w);
				$menu.css({left : offset.left + 'px', top : offset.top + h + 'px'}).slideDown('fast');
				$('body').bind('mousedown', onBodyDown);
			});
				
			function onBodyDown(event) {
				if (!(event.target.name == 'userGroupOrgName' 
						|| event.target.class == 'ztree' 
						|| $(event.target).parents('.org-tree').length > 0)) {
					$menu.fadeOut('fast');
					$('body').unbind('mousedown', onBodyDown);
				}
			}
			//用户查询
			function userFind() {
				if(!$orgId.val()) {
					alert('对不起，请先选择相应的机构');
					return;
				}
				var params = {};
				params.orgId = $orgId.val();
				params.roleId = $roleId.val() || '-1';
				$.getJSON('queryUserList', params, function(json) {
					var _html = '';
					$.each(json, function() {
						_html += '<option value="' + this.userId + '">' + this.userName + '</option>';
					});
					$main.find('#left_multi_select').html(_html);
				});
			}
			// 用户选择、取消
			var $leftSel = $main.find('#left_multi_select');
			var $rightSel = $main.find('#right_multi_select');
			$main.on('click', '.to-right', function() {
				$leftSel.find('option:selected').each(function() {
					$form.append($('<input type="hidden" name="userIdList"/>').val(this.value));
					$(this).remove().appendTo($rightSel);
				});
			});
			$main.on('click', '.to-left', function() {
				$rightSel.find('option:selected').each(function() {
					$form.find('input[name=userIdList][value=' + this.value + ']').remove();
					$(this).remove().appendTo($leftSel);
				});
			});
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