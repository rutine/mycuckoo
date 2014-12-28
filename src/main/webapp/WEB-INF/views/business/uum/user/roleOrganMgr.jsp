<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>统一用户</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
	<link href="${ctx}/static/jquery-validation/1.10.0/css/jquery-validate.css" type="text/css" rel="stylesheet" />
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
					<div class="col-sm-3 col-md-2 mycuckoo-sidebar">
						<ul class="nav">
							<li style="font-size:13px">
								<strong>机构树(请首先选择机构)</strong>
							</li>
							<li><ul id="tree_role_organ" class="ztree"></ul></li>
						</ul>
					</div>
					<div class="col-sm-9 col-md-10 mycuckoo-submain">
						<!-- 列表内容 -->
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
			var config = {};
			var $main = $('.mycuckoo-submain');
			var setting = {
				async : { enable : true, type : 'get', url : '${ctx}/uum/organMgr/getChildNodes', autoParam : [ 'id=treeId' ] },
				callback : {
					onClick : function(evane, treeId, treeNode) {
						var queryString = $main.find('form[name=searchForm]').serialize();
						var params = MyCuckoo.fromQueryString(queryString);
						MyCuckoo.apply(params, config);
						params.treeId = treeNode.id;
						reload(params);
					}
				},
				data : { key : { name : 'text', icon : 'iconCls' } },
				view : {
					dblClickExpand : false, showLine : true, selectedMulti : false,
					expandSpeed : ($.browser.msie && parseInt($.browser.version) <= 6) ? '' : 'fast'
				}
			};
			var zTree = $.fn.zTree.init($('#tree_role_organ'), setting);
			
			// 刷新列表
			reload(config);
			
			// 刷新内容列表
			function reload(params) {
				$main.load('list', params, loadCallBack);
			}
			
			// 回调
			function loadCallBack() {
				// form查询
				var $form = $main.find('form[name=searchForm]');
				$form.off('click', 'button[name=search]');
				$form.on('click', 'button[name=search]', function() {
					var params = MyCuckoo.fromQueryString($form.serialize());
					MyCuckoo.apply(params, config);
					reload(params); // 刷新列表
				});
				// 清空
				$form.off('click', 'button[name=clear]');
				$form.on('click', 'button[name=clear]', function() {
					$(':input', $form)
						.not(':button, :submit, :reset, :hidden')
						.val('')
						.removeAttr('checked')
						.removeAttr('selected');
				});
				// 操作按钮
				$main.off('click', '.btn-toolbar .btn-group .btn');
				$main.on('click', '.btn-toolbar .btn-group .btn', function(event) {
					event.preventDefault();
					var opt = $(this).attr('href'); // modId = 53
					switch(opt) {
					case 'roleassign':
						assignRole();
						break;
					case 'roledel':
						del();
						break;
					}
				});
				// page分页
				$main.off('click', '.pagination li > a');
				$main.on('click', '.pagination li > a', function(event) {
					event.preventDefault(); // 阻止超链接点击事件
					var cls = $(this).parent('li').attr('class');
					if(cls == 'disabled' || cls == 'active') return;
					
					var params = MyCuckoo.fromQueryString($(this).attr('href'));
					MyCuckoo.apply(params, config);
					reload(params); // 刷新列表
				});
				// 复选框
				MyCuckoo.checkbox();
			}
			
			// 为机构分配角色
			function assignRole() {
				var orgNodes = zTree.getSelectedNodes();
				if(orgNodes.length != 1) {
					MyCuckoo.showMsg({ state : 'warning', title : '提示', msg : '请选择要分配角色的机构' });
					return;
				}
				var $modal = $(MyCuckoo.modalTemplate);
				$modal.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); });
				$modal.find('h3').text('选择角色');
				$modal.find('.modal-body').load('queryUnselectedRoleList', 
						{treeId : orgNodes[0].id}, function() {
							
					modalCallBack();
				});
				// 保存为机构分配的角色
				$modal.on('click', '.modal-footer .btn:first', function() {
					var $singleCheck = $modal.find('.table input:checked[name=single]');
					if($singleCheck.size() == 0) {
						MyCuckoo.showMsg({state: 'danger', title: '提示', msg: '请选择一条或多条记录!'});
						return;
					}
					var roleIdList = [];
					$singleCheck.each(function() {
						var _json = $.parseJSON($(this).next(':first').html());
						roleIdList.push(_json.roleId);
					});
					$.post('save', {id : orgNodes[0].id, roleIdList : roleIdList.join(',')}, function(data) {
						if(data.status) {
							MyCuckoo.showMsg({state:'success', title: '提示', msg: data.msg});
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: data.msg});
						}
						// 刷新列表
						reload(MyCuckoo.apply({treeId : orgNodes[0].id}, config));
						$modal.modal('hide');
					});
				});
				$modal.modal();
				$modal.appendTo($('body'));
				
				function modalCallBack() {
					// page分页
					$modal.off('click', '.pagination li > a');
					$modal.on('click', '.pagination li > a', function(event) {
						event.preventDefault(); // 阻止超链接点击事件
						var cls = $(this).parent('li').attr('class');
						if(cls == 'disabled' || cls == 'active') return;
						
						var params = MyCuckoo.fromQueryString($(this).attr('href'));
						MyCuckoo.apply(params, config);
						// 刷新列表
						$modal.find('.modal-body').load('unselectedRoleList', params, function() {
							modalCallBack();
						});
					});
					// 复选框
					MyCuckoo.checkbox($modal);
				}
			}
			
			// 删除操作
			function del() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() == 0) {
					MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请至少选择一条记录!'});
					return;
				}
				MyCuckoo.showDialog({
					title: '警告提示',
					msg: '您确认删除此记录吗?',
					okBtn: '是',
					cancelBtn: '否',
					ok: function() {
						var roleIdList = [];
						$singleCheck.each(function() {
							var _json = $.parseJSON($(this).next(':first').html());
							roleIdList.push(_json.roleId);
						});
						var orgId = zTree.getSelectedNodes()[0].id;
						$.getJSON('delete', {id :  orgId, roleIdList : roleIdList.join(',')}, function(json) {
							if(json.code == 1) {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: '您不能删除选择角色,角色下已有用户,请先将相应用户的角色删除!'});
							} else if(json.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
								// 刷新页面
								reload(MyCuckoo.apply({treeId : orgId}, config));
							} else {
								MyCuckoo.showMsg({state:'error', title: '提示', msg: json.msg});
							}
						});
					}
				});
			}
		});
	</script>
</body>
</html>