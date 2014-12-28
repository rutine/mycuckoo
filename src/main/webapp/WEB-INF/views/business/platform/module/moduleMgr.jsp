<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
					<div class="col-sm-3 col-md-2 mycuckoo-sidebar">
						<ul class="nav">
							<li style="font-size:13px">
								<strong>模块树</strong>
							</li>
							<li><ul id="tree_module" class="ztree"></ul></li>
						</ul>
					</div>
					<div class="col-sm-9 col-md-10 mycuckoo-submain">
						<div class="page-header">
							<form class="form-inline" name="searchForm">
								<div class="form-group">
									<label class="sr-only">名称&nbsp;</label>
									<input type="text" class="form-control input-sm" placeholder="名称" />
								</div>
								<div class="form-group">
									<label class="sr-only">密码&nbsp;</label>
									<input type="password" class="form-control input-sm" placeholder="密码" />
								</div>
								<button type="button" class="btn btn-info btn-sm">&nbsp;查询
									<span class="glyphicon glyphicon-search"></span>
								</button>
							</form>
						</div>
						<div class="btn-toolbar">
							<div class="btn-group">
								<a href="#" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a>
								<a href="#" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-edit"></span>&nbsp;编辑</a>
								<a href="#" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-list"></span>&nbsp;查看</a>
								<a href="#" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a>
							</div>
						</div>
						<table class="table table-striped table-hover table-condensed">
							<tr><th><input type="checkbox"/></th><th>序号</th><th>firstName</th><th>lastName</th><th>userName</th></tr>
							<tr><td><input type="checkbox"/></td><td>1</td><td>tine</td><td>ru</td><td>rutine</td></tr>
							<tr><td><input type="checkbox"/></td><td>2</td><td>tine</td><td>ru</td><td>rutine</td></tr>
							<tr><td><input type="checkbox"/></td><td>3</td><td>tine</td><td>ru</td><td>rutine</td></tr>
							<tr><td><input type="checkbox"/></td><td>4</td><td>tine</td><td>ru</td><td>rutine</td></tr>
							<tr><td><input type="checkbox"/></td><td>5</td><td>tine</td><td>ru</td><td>rutine</td></tr>
						</table>
						<!-- 分页 -->
						<div class="text-center">
							<ul class="pagination pagination-sm">
								<li><a href="#">«</a></li>
								<li><a href="#">1</a></li>
								<li><a href="#">2</a></li>
								<li><a href="#">3</a></li>
								<li><a href="#">»</a></li>
							</ul>
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
			var config = {};
			var url = 'list';
			var $main = $('.mycuckoo-submain');
			var setting = {
				async : { enable : true, type : 'get', url : 'getChildNodes', autoParam : [ 'id=moduleId' ] },
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
			var zTree = $.fn.zTree.init($('#tree_module'), setting);
			
			// 刷新列表
			reload(config);
			
			// 刷新内容列表
			function reload(params) {
				$main.load(url, params, loadCallBack);
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
					case BasicConstant.OPT_ADD_LINK:
						addOrUpdateOrView('createForm');
						break;
					case BasicConstant.OPT_MODIFY_LINK:
						addOrUpdateOrView('updateForm');
						break;
					case BasicConstant.OPT_DELETE_LINK:
						del();
						break;
					case BasicConstant.OPT_VIEW_LINK:
						addOrUpdateOrView('viewForm');
						break;
					case BasicConstant.OPT_ENABLE_LINK:
						disEnable(BasicConstant.OPT_ENABLE_LINK);
						break;
					case BasicConstant.OPT_DISABLE_LINK:
						disEnable(BasicConstant.OPT_DISABLE_LINK);
						break;
					case BasicConstant.OPT_OPTASSIGN_LINK:
						optAssign();
						break;
					case BasicConstant.OPT_OPTPRI_LINK:
						break;
					case 'moduleLabel':
						alert('开发中....');
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
			
			// 增加、更新、查看操作
			function addOrUpdateOrView(_url) {
				if(/view|update/.test(_url)) {
					var $singleCheck = $main.find('.table input:checked[name=single]');
					if($singleCheck.size() != 1) {
						MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
						return;
					}
					var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
					_url = _url + '?id=' + _jsonObj.moduleId;
				}
				$.get(_url, function(html) {
					$('.mycuckoo-main .row').removeClass('active').addClass('hidden');
					var $html = $('<div class="row active"></div>').append(html).appendTo('.mycuckoo-main');
					// 操作按钮
					$html.on('click', '.btn-toolbar .btn-group a', function(event) {
						event.preventDefault();
						$(this).button('loading');
						switch($(this).attr('href')) {
						case 'save':
							btnCommit((/update/.test(_url)) ? 'update' : 'save');
							break;
						case 'saveadd':
							btnCommit('saveadd');
							$(this).button('reset');
							break;
						case 'reback':
							$('.mycuckoo-main .row:not(:first)').remove();
							$('.mycuckoo-main .row').removeClass('hidden').addClass('active');
							break;
						}
					});
					// 选择上级模块
					$html.on('focus click', 'input[name=upModName], .btn.select', function() {
						var $this = $(this);
						var _setting = MyCuckoo.cloneObject(setting); // 复制模块树对象
						var callback = {
							onClick : function(evane, treeId, treeNode) {
								$this.siblings('input:hidden').val(treeNode.id);
								$this.hasClass('btn') ? $this.siblings('input:not(:hidden)').val(treeNode.text) : $this.val(treeNode.text);
								$modal.modal('hide');
							}
						};
						var $modal = $(MyCuckoo.modalTemplate);
						$modal.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); });
						$modal.find('.modal-dialog').addClass('modal-sm');
						$modal.find('h3').text('选择上级模块');
						$modal.find('.modal-body').append('<ul class="nav nav-list">' +
											'<li style="font-size:13px">' + 
												'<strong>模块树</strong>--<strong><a style="cursor: pointer">[展开]</a></strong>' + 
											'</li>' +
											'<li><ul class="ztree"></ul></li>' +
										'</ul>');
						$modal.find('.modal-footer .btn:first').remove();
						$modal.modal();
						$modal.appendTo($('body'));
						
						_setting.callback = callback;
						$.fn.zTree.init($modal.find('.modal-body .ztree'), _setting);
					});
					// 保存函数
					function btnCommit(btn) {
						var $form = $html.find('form[name=editForm]');
						// 验证表单
						if($form.validate().form()) {
							var params = $form.serialize();
							$.post(_url, params, function(json) {
								MyCuckoo.showMsg({state: json.status ? 'success' : 'danger', title: '提示', msg: json.msg});
								if(json.status) {
									reload(config); // 刷新列表
									if(btn == 'saveadd') {
										$form[0].reset();
									} else {
										$('.mycuckoo-main .row:not(:first)').remove();
										$('.mycuckoo-main .row').removeClass('hidden').addClass('active');
									}
								}
							}, 'json');
						}
					}
				});
			}
			
			// 删除操作
			function del() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() != 1) {
					MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
					return;
				}
				MyCuckoo.showDialog({
					title: '警告提示',
					msg: '您确认删除此记录吗?',
					okBtn: '是',
					cancelBtn: '否',
					ok: function() {
						var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
						var _id = _jsonObj.moduleId;
						$.getJSON('delete', {id: _id}, function(json) {
							if(json.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
								reload(config); // 刷新列表
							} else {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.msg});
							}
						});
					}
				});
			}
			
			// 启用、停用操作
			function disEnable(flag) {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state: 'warning', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (_jsonObj.modLevel != 3) {
					MyCuckoo.showMsg({ state: 'warning', title : '提示', msg : '请您选择第三级模块菜单!' });
					return;
				}
				if (flag == _jsonObj.status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此模块已经启用!' : '此模块已经停用!');
					MyCuckoo.showMsg({ state: 'info', title : '提示', msg : msg });
					return;
				}
				if (flag == BasicConstant.OPT_DISABLE_LINK) {
					MyCuckoo.showDialog({
						msg : '您确认停用此模块吗?如果停用,此模块下的操作将自动清除,并重登生效。',
						okBtn: '是',
						cancelBtn: '否',
						ok : function() {
							disableEnalbeOpt(flag, _jsonObj.moduleId);
						}
					});
				} else {
					disableEnalbeOpt(flag, _jsonObj.moduleId);
				}
				function disableEnalbeOpt(flag, id) {
					$.getJSON('disEnable', {id: id, disEnableFlag: flag}, function(json) {
						if(json.status) {
							var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '模块菜单启用成功!' : '模块菜单停用成功!');
							MyCuckoo.showMsg({state:'success', title: '提示', msg: msg});
							reload(config); // 刷新列表
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: '模块菜单已有下级, 您不能停用此模块!'});
						}
					});
				}
			}
			
			// 模块分配操作
			function optAssign() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state: 'warning', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if(_jsonObj.modLevel != 3) {
					MyCuckoo.showMsg({ state: 'warning', title: '提示', msg: '请您选择第三级模块菜单!'});
					return;
				}
				
				// 加载模块已分配和未分配权限
				var $modal = $(MyCuckoo.modalTemplate);
				var $leftSel = null;
				var $rightSel = null;
				$modal.find('.modal-header h3').text('模块分配操作');
				$modal.find('.modal-body').load('getAssignedAUnAssignedOpt', _jsonObj, function() {
					$leftSel = $modal.find('#left_multi_select');
					$rightSel = $modal.find('#right_multi_select');
					$modal.appendTo($('body'));
					$modal.modal();
				});
				$modal.on('show.bs.modal', function() {
					$('.to-right').off('click').click(function() {
						$leftSel.find('option:selected').each(function() {
							$(this).remove().appendTo($rightSel);
						});
					});
					$('.to-left').off('click').click(function() {
						$rightSel.find('option:selected').each(function() {
							$(this).remove().appendTo($leftSel);
						});
					});
					$leftSel.dblclick(function() {
						$(this).find('option:selected').each(function() {
							$(this).remove().appendTo($rightSel);
						});
					});
					$rightSel.dblclick(function() {
						$(this).find('option:selected').each(function() {
							$(this).remove().appendTo($leftSel);
						});
					}); 
					// 处理并保存模块权限
				})
						.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); })
						.off('click', '.modal-footer > .btn-primary')
						.on('click', '.modal-footer > .btn-primary', function() {
							$(this).button('loading');
							MyCuckoo.showMsg({state: 'info', title: '提示', msg: '正在保存数据...'});
							var $children = $rightSel.children();
							if($children.size() == 0) {
								MyCuckoo.showMsg({state: 'danger', title: '提示', msg: '请您分配相应的操作!'});
							} else {
								var operateIdList = new Array();
								$children.each(function() {
									operateIdList.push($(this).val());
								});
								var id = $modal.find('input:hidden:first').val();
								$.post('createModuleOptRefs', {moduleId: id, operateIdList: operateIdList}, function(json) {
									if(json.status) {
										MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
									} else {
										MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.msg});
									}
								}, 'json').always(function() {
									$(this).button('reset');
									$modal.modal('hide');
								});
							}
						});
			}
		});
	</script>
</body>
</html>