<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>统一用户</title>
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
					<div class="col-sm-3 col-md-2 mycuckoo-sidebar">
						<ul class="nav">
							<li style="font-size:13px">
								<strong>机构角色树</strong>
							</li>
							<li><ul id="tree_user" class="ztree"></ul></li>
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
				async : { enable : true, type : 'get', url : 'getChildNodes', autoParam : [ 'id=treeId' ] },
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
			var zTree = $.fn.zTree.init($('#tree_user'), setting);
			
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
					case BasicConstant.OPT_OPTPRI_LINK:
						assignPriv();
						break;
					case 'assignrole':
						assignRole();
						break;
					case 'resetpwd':
						resetPwd();
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
					_url = _url + '?id=' + _jsonObj.userId;
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
					// 用户有效期
					$html.find('#user_avidate').datetimepicker({
						language : 'zh-CN',
						format : 'yyyy-mm-dd',
						autoclose : 1,
						todayHighlight: 1,
						todayBtn : 'linked',
						startView : 2,
						minView : 2,
						forceParse : 0
					});
					// 选择角色
					$html.on('focus click', 'input[name=uumRoleName], .btn.select', function() {
						var $this = $(this);
						var _setting = MyCuckoo.cloneObject(setting); // 复制机构角色树对象
						var callback = {
							onClick : function(evane, treeId, treeNode) {
								var _flag = treeNode.id.indexOf('_');
								if(treeNode.id.substr(_flag + 1) == '1' || treeNode.id == '0') return;
								var id = treeNode.id.substr(0, _flag);
								var name = treeNode.getParentNode() ? (treeNode.getParentNode().text + '-' + treeNode.text) : treeNode.text;
								$this.siblings('input:hidden').val(id);
								$this.hasClass('btn') ? $this.siblings('input:not(:hidden)').val(name) : $this.val(name);
								$modal.modal('hide');
							}
						};
						var $modal = $(MyCuckoo.modalTemplate);
						$modal.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); });
						$modal.find('.modal-dialog').addClass('modal-sm');
						$modal.find('h3').text('选择角色');
						$modal.find('.modal-body').append('<ul class="nav nav-list">' +
											'<li style="font-size:13px">' + 
												'<strong>机构角色树</strong>' + 
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
				var $singleCheck = $main.find('.main input:checked[name=single]');
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
						var _id = _jsonObj.userId;
						$.getJSON('delete', {id: _id}, function(json) {
							if(json.status) {
								MyCuckoo.showMsg({state: 'success', title: '提示', msg: json.msg});
								reload(config); // 刷新列表
							} else {
								MyCuckoo.showMsg({state: 'danger', title: '提示', msg: json.msg});
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
				if (flag == _jsonObj.status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此用户已经启用!' : '此用户已经停用!');
					MyCuckoo.showMsg({ state: 'info', title : '提示', msg : msg });
					return;
				}
				if (flag == BasicConstant.OPT_DISABLE_LINK) {
					MyCuckoo.showDialog({
						msg : '您确认停用此用户?如停用,此用户将归入无角色用户并自动清除所有权限。',
						okBtn: '是',
						cancelBtn: '否',
						ok : function() {
							disableEnalbeOpt(flag, _jsonObj.userId);
						}
					});
				} else {
					disableEnalbeOpt(flag, _jsonObj.userId);
				}
				function disableEnalbeOpt(flag, id) {
					$.getJSON('disEnable', {id: id, disEnableFlag: flag}, function(json) {
						if(json.status) {
							var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '用户启用成功!' : '用户停用成功!此用户将不能在使用本系统。');
							MyCuckoo.showMsg({state:'success', title: '提示', msg: msg});
							reload(config); // 刷新列表
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: '操作失败!'});
						}
					});
				}
			}
			
			// 分配特殊权限
			function assignPriv() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state: 'warning', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (BasicConstant.OPT_DISABLE_LINK == _jsonObj.status) {
					MyCuckoo.showMsg({ state : 'danger', title : '提示', msg : '请先启用此用户！' });
					return;
				} else if(_jsonObj.uumOrgRoleId == 0) {
					MyCuckoo.showMsg({ state : 'danger', title : '提示', msg : '请先为此用户分配有效角色！' });
					return;
				}
				
				/**
				 * 删除结点
				 * @param delNode 结点对象
				 */
				var deleteNode = function(delNode, treeObj) {
					if (delNode != null) {
						var parentNode = delNode.getParentNode();
						if (delNode.leaf) {
							treeObj.checkNode(delNode, false, true);
							treeObj.removeNode(delNode);
						} else if (!delNode.children || delNode.children.length == 0) {
							treeObj.checkNode(delNode, false, true);
							treeObj.removeNode(delNode);
						}
						if(parentNode && parentNode.children.length == 0) {
							deleteNode(parentNode, treeObj);
						}
					}
				};
				/**
				 *移动增加结点 
				 *
				 *@param node 节点
				 *@param fromTree 源树
				 *@param toTree 目标树
				 */
				var moveNode = function(node, fromTree, toTree) {
					var obj = toTree.getNodeByParam('id', node.id);
					if (obj == null) {
						var parentNode = toTree.getNodeByParam('id', node.parentId, null);
						var newNode = {id : node.id, parentId : node.parentId, text : node.text, iconSkin : node.iconSkin, isParent : node.isParent, checked : false };
						if (parentNode) {
							toTree.addNodes(parentNode, newNode);
						} else {
							toTree.addNodes(null, newNode);
						}
					}
				};
				/**
				 * 取消、分配操作
				 */
				var selUnselOpt = function(fromTree, toTree) {
					var checkedNodes = fromTree.getCheckedNodes(true);
					if(checkedNodes.length == 0) {
						MyCuckoo.alertMsg({ title : '提示', msg : '请选择相应的模块操作!' });
						return;
					}
					for (var i = 0; i < checkedNodes.length; i++) {
						moveNode(checkedNodes[i], fromTree, toTree);
						deleteNode(checkedNodes[i], fromTree);
					}
				};
				
				/**
				 * 选择添加机构、角色或用户
				 * 
				 * @param multiselectObj 多选机构、角色或用户框
				 * @param treeObj 机构、机构角色或用户树
				 */
				var selected = function(multiselectObj, treeObj) {
					var $checked = multiselectObj.parents('div.tab-pane').find(':radio[name=rowPri]:checked');
					var checkedNodes = treeObj.getCheckedNodes(true);
					if(checkedNodes.length == 0) {
						MyCuckoo.alertMsg({ title : '提示', msg : '请选择左边列表!' });
						return;
					}
					if($checked.val() == 'rol') {
						for (var i = 0; i < checkedNodes.length; i++) {
							var checkedNode = checkedNodes[i];
							var orgRoleId = checkedNode.id;
							var roleId = orgRoleId.substr(0, orgRoleId.indexOf('_'));
							var options = multiselectObj.find('option[value=' + roleId + ']');
							if(!options.length) {
								multiselectObj.append($('<option>').val(roleId).text(checkedNode.text));
							}
						}
					} else {
						if($checked.val() == 'usr' && checkedNodes[0].id == 0) {
							MyCuckoo.alertMsg({title : '提示', msg : '请选择用户!'});
							return;
						}
						for (var i = 0; i < checkedNodes.length; i++) {
							var checkedNode = checkedNodes[i];
							var orgRoleId = checkedNode.id;
							var options = multiselectObj.find('option[value=' + orgRoleId + ']');
							if(!options.length) {
								multiselectObj.append($('<option>').val(orgRoleId).text(checkedNode.text));
							}
						}
					}
				};
				
				/**
				 * 选择移除机构、角色或用户
				 * 
				 * @param 多选机构、角色或用户框
				 * @param 机构、机构角色或用户树
				 */
				var unselected = function(multiselectObj, treeObj) {
					var $checked = $(this).find(':radio[name=rowPri]:checked');
					var $selected = multiselectObj.find(':selected');
					if (!$selected.length) {
						MyCuckoo.alertMsg({ title : '提示', msg : '请您选择右边列表!' });
						return;
					}
					$.each($selected, function(index, item) {
						var node = null;
						if($checked.val == 'rol') {
							node = treeObj.getNodeByParam('id', item.value + '_2');
						} else {
							node = treeObj.getNodeByParam('id', item.value);
						}
						if(node) {
							treeObj.checkNode(node, false);
						}
					});
					multiselectObj.html(multiselectObj.find(':not(:selected)'));
				};
				
				/**
				 * 行权限改变事件
				 * 
				 * @param multiselectObj 复选框对象
				 * @param treeObj 树对象
				 * @param privilegeType 权限类型
				 * @param initBol 是否进行初始化 
				 */
				var radioChangeEvent = function(multiselectObj, treeObj, userId, privilegeType, initBol) {
					$.post('${ctx}/uum/userMgr/getSelectRowPrivilege', {id : userId}, function(data) {
						var privilegeTypeSelected = data.rowPrivilege;
						var rowList = data.rowList;
						var dataUrl = '';
						//初始化
						if(!privilegeType && initBol) {
							if(privilegeTypeSelected) {
								if(privilegeTypeSelected == 'org') {
									dataUrl = '${ctx}/uum/organMgr/getChildNodes';
									treeObj.parent().prev('span').text('未选机构');
									multiselectObj.prev('span').text('已选机构');
								} else if(privilegeTypeSelected == 'rol') {
									dataUrl = '${ctx}/uum/userMgr/getChildNodes?isCheckbox=Y';
									treeObj.parent().prev('span').text('未选角色');
									multiselectObj.prev('span').text('已选角色');
								} else if(privilegeTypeSelected == 'usr') {
									treeObj.parent().prev('span').text('未选用户');
									multiselectObj.prev('span').text('已选用户');
								}
								for (var i = 0; i < rowList.length; i++) {
									multiselectObj.append($('<option>').val(rowList[i].orgId).text(rowList[i].orgSimpleName));
								}
							}
						} else {
							if(privilegeType == 'org') {
								dataUrl = '${ctx}/uum/organMgr/getChildNodes';
								treeObj.parent().prev('span').text('未选机构');
								multiselectObj.prev('span').text('已选机构');
							} else if(privilegeType == 'rol') {
								dataUrl = '${ctx}/uum/userMgr/getChildNodes?isCheckbox=Y';
								treeObj.parent().prev('span').text('未选角色');
								multiselectObj.prev('span').text('已选角色');
							} else if(privilegeType == 'usr') {
								treeObj.parent().prev('span').text('未选用户');
								multiselectObj.prev('span').text('已选用户');
							}
							multiselectObj.empty();
							//显示对应列表
							if(privilegeType == privilegeTypeSelected) {
								for (var i = 0; i < rowList.length; i++) {
									multiselectObj.append($('<option>').val(rowList[i].orgId).text(rowList[i].orgSimpleName));
								}
							}
						}
						var $searchUsr = treeObj.parents('div.tab-pane').find('label.searchUsr').hide();
						if(privilegeType != 'usr') {//非用户重新加载左边列表
							var setting = {
									async : { enable : true, type : 'get', url : dataUrl, autoParam : [ 'id=treeId' ] },
									check : { enable : true },
									data : { key : { name : 'text', icon : 'iconCls' } },
									view : {
										dblClickExpand : false, showLine : true, selectedMulti : false,
										expandSpeed : ($.browser.msie && parseInt($.browser.version) <= 6) ? '' : 'fast'
									}
							};
							$.fn.zTree.init(treeObj, setting);
						} else {
							var setting = {
									check : { enable : true },
									callback : {
										onCheck : function(event, treeId, treeNode) {
											if(treeNode.checked) {
												 var rightTreeObj = $.fn.zTree.getZTreeObj(treeObj.attr('id'));
												 rightTreeObj.expandNode(treeNode, true, true, true);
											}
										}
									}
							};
							$.fn.zTree.init(treeObj, setting);
							$searchUsr.off().children().off().end().show();
							$searchUsr.on('keyup', 'input', function(event) {
								if(event.keyCode != 13) return;
								$.post('queryUsersByUserName', {userName : this.value}, function(data) {
									var nodes = [];
									$.each(data, function() {
										nodes.push({id : this.userId, text : this.userName, name : this.userName, isParent : false});
									});
									$.fn.zTree.getZTreeObj(treeObj.attr('id')).addNodes(null, nodes);
								});
							});
							$searchUsr.on('click', '.btn', function() {
								$.post('queryUsersByUserName', {userName : $searchUsr.children()[0].value}, function(data) {
									var nodes = [];
									$.each(data, function() {
										nodes.push({id : this.userId, text : this.userName, name : this.userName, isParent : false});
									});
									$.fn.zTree.getZTreeObj(treeObj.attr('id')).addNodes(null, nodes);
								});
							});
						}
					});
				};
				
				var $modal = $(MyCuckoo.modalTemplate);
				$modal.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); });
				$modal.find('h3').text('用户分配特殊权限');
				$modal.find('.modal-body:first').load('queryUserPrivilegeList', {id :  _jsonObj.userId, userName : _jsonObj.userName}, function() {
					var setting1 = {
							check : { enable : true },
							data : {
								key : { name : 'text' },
								simpleData : { enable : true, pIdKey : 'parentId', rootPId : null }
							},
							callback : {
								onCheck : function(event, treeId, treeNode) {
									if(treeNode.checked) {
										 var leftTreeObj = $.fn.zTree.getZTreeObj('left_user_tree');
										 leftTreeObj.expandNode(treeNode, true, true, true);
									}
								}
							}
					};
					var setting2 = {
							check : { enable : true },
							data : {
								key : { name : 'text' },
								simpleData : { enable : true, pIdKey : 'parentId', rootPId : null }
							},
							callback : {
								onCheck : function(event, treeId, treeNode) {
									if(treeNode.checked) {
										 var rightTreeObj = $.fn.zTree.getZTreeObj('right_user_tree');
										 rightTreeObj.expandNode(treeNode, true, true, true);
									}
								}
							}
					};
					var leftTreeData = $.parseJSON($modal.find('#left_user_tree').attr('data'));
					var rightTreeData = $.parseJSON($modal.find('#right_user_tree').attr('data'));
					$modal.find('#left_user_tree').removeAttr('data');
					$modal.find('#right_user_tree').removeAttr('data');
					var leftTreeObj = $.fn.zTree.init($modal.find('#left_user_tree'), setting1, leftTreeData);
					var rightTreeObj = $.fn.zTree.init($modal.find('#right_user_tree'), setting2, rightTreeData);
					// 添加勾上的模块
					$modal.off('click', '.to-right:eq(0)').on('click', '.to-right:eq(0)', function() {
						selUnselOpt(leftTreeObj, rightTreeObj);
					});
					// 移除勾上的模块
					$modal.off('click', '.to-left:eq(0)').on('click', '.to-left:eq(0)', function() {
						selUnselOpt(rightTreeObj, leftTreeObj);
					});
					// 行权限选择事件
					var $leftRowTree = $modal.find('#left_row_tree');
					var $rightRowMultiSelect = $modal.find('#right_row_multi_select');
					radioChangeEvent($rightRowMultiSelect, $leftRowTree, _jsonObj.userId, '', true);
					$modal.off('change', ':radio[name=rowPri]').on('change', ':radio[name=rowPri]', function() {
						radioChangeEvent($rightRowMultiSelect, $leftRowTree, _jsonObj.userId, this.value);
					});
					// 添加勾上的选择
					$modal.off('click', '.to-right:eq(1)').on('click', '.to-right:eq(1)', function() {
						selected($rightRowMultiSelect, $.fn.zTree.getZTreeObj($leftRowTree.attr('id')));
					});
					// 移除勾上的选择
					$modal.off('click', '.to-left:eq(1)').on('click', '.to-left:eq(1)', function() {
						unselected($rightRowMultiSelect, $.fn.zTree.getZTreeObj($leftRowTree.attr('id')));
					});
					// 保存权限相关
					$form = $modal.find('div.tab-pane form.form-inline');
					$modal.on('click', '.panel-footer:eq(0) .btn', function() {
						if($form.eq(0).find('input:checked').length == 0) {
							MyCuckoo.alertMsg({ title : '提示', msg : '请选择权限范围!' });
							return;
						}
						var optPrivilegeScope = $form.eq(0).find('input:checked').val();
						var selectedNodes = rightTreeObj.getNodesByFilter(function(node) {
							if(!node.isParent) return true;
							return false;
						});
						var optIdList = [];
						$.each(selectedNodes, function(index, node) {
							optIdList.push(Number(node.id) - 1000);
						});
						$.post('saveOptPrivilege', {id : _jsonObj.userId, privilegeScope : optPrivilegeScope, optIdList : optIdList.join(',')}, function(data) {
							if(data.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: data.msg});
							} else {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: data.msg});
							}
						});
					});
					$modal.on('click', '.panel-footer:eq(1) .btn', function() {
						if($form.eq(1).find('input:checked').length == 0) {
							MyCuckoo.alertMsg({ title : '提示', msg : '请选择行权限类别!' });
							return;
						}
						var rowPrivilege = $form.eq(1).find('input:checked').val();
						var $selected = $modal.find('#right_row_multi_select > option');
						var roleIdList = [];
						$.each($selected, function(index, item) {
							roleIdList.push(item.value);
						});
						$.post('saveRowPrivilege', {id : _jsonObj.userId, rowPrivilege : rowPrivilege, roleIdList : roleIdList.join(',')}, 
								function(data) {
							if(data.status) {
								MyCuckoo.showMsg({state: 'success', title: '提示', msg: data.msg});
							} else {
								MyCuckoo.showMsg({state: 'danger', title: '提示', msg: data.msg});
							}
						});
					});
				});
				$modal.find('.modal-footer .btn:first').remove();
				$modal.modal();
				$modal.appendTo($('body'));
			}
			
			// 分配角色
			function assignRole() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state : 'warning', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (BasicConstant.OPT_DISABLE_LINK == _jsonObj.status) {
					MyCuckoo.showMsg({ state : 'warning', title : '提示', msg : '请先启用此用户！' });
					return;
				}
				var $modal = $(MyCuckoo.modalTemplate);
				$modal.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); });
				$modal.find('h3').text('用户分配角色');
				$modal.find('.modal-body:first').load('queryUserRolePrivilegeList', {
							id : _jsonObj.userId, 
							userName : _jsonObj.userName,
							defaultRoleId : _jsonObj.uumOrgRoleId,
							defaultRoleName : _jsonObj.uumOrgName + '-' + _jsonObj.uumRoleName
						}, function() {
							
							var setting1 = {
									async : { enable : true, type : 'get', url : 'getChildNodes?isCheckbox=Y', autoParam : [ 'id=treeId' ] },
									check : { enable : true },
									data : {
										key : { name : 'text' },
										simpleData : { enable : true, pIdKey : 'parentId', rootPId : null }
									},
									callback : {
										onCheck : function(event, treeId, treeNode) {
											if(treeNode.checked) {
												 var _leftTreeObj = $.fn.zTree.getZTreeObj('left_role_tree');
												 _leftTreeObj.expandNode(treeNode, true, true, true);
											}
										}
									}
							};
							// tab事件
							$modal.on('click', 'ul.nav-tabs a', function (e) {
								e.preventDefault();
								$(this).tab('show');
							});
							var leftTreeData = $.parseJSON($modal.find('#left_role_tree').attr('data'));
							$modal.find('#left_role_tree').removeAttr('data');
//							leftTreeData = _iNodes.replace(/(\w+)\s*:\s*'([^']*)'/g, '"$1" : "$2"');
							var leftTreeObj = $.fn.zTree.init($modal.find('#left_role_tree'), setting1, leftTreeData);
							// 添加勾上的角色
							$modal.off('.to-right').on('click', '.to-right', function() {
								selected($modal.find('#right_role_multi_select'), leftTreeObj);
							});
							// 移除勾上的角色
							$modal.off('.to-left').on('click', '.to-left', function() {
								unselected($modal.find('#right_role_multi_select'), leftTreeObj);
							});
							// 保存权限相关
							$modal.on('click', '.panel-footer .btn:eq(0)', function() {
								var $selected = $modal.find('#right_role_multi_select > :selected:first');
								if(!$selected.length) {
									MyCuckoo.alertMsg({ title : '提示', msg : '请您在右边列表选择一个角色!' });
									return;
								}
								$modal.find('input[name=defaultRoleId]').val($selected.val());
								$modal.find('p.defaultRoleName').text($selected.text());
							});
							$modal.on('click', '.panel-footer .btn:eq(1)', function() {
								var $selected = $modal.find('#right_role_multi_select > option');
								var $defaultRoleId = $modal.find('input[name=defaultRoleId]');
								if(!$defaultRoleId.val()) {
									MyCuckoo.alertMsg({ title : '提示', msg : '请为用户选择一个默认角色!' });
								} else if(!$selected.length) {
									MyCuckoo.alertMsg({ title : '提示', msg : '请为用户分配一个角色!' });
								} else {
									var roleIdList = [];
									$.each($selected, function(index, item) {
										roleIdList.push(item.value);
									});
									$.post('saveRolePrivilege', 
											{id : _jsonObj.userId, defaultRoleId : $defaultRoleId.val(), roleIdList : roleIdList.join(',')}, 
												function(data) {
												
										if(data.status) {
											MyCuckoo.showMsg({state:'success', title: '提示', msg: data.message});
										} else {
											MyCuckoo.showMsg({state:'danger', title: '提示', msg: data.message});
										}
									});
								}
							});
				});
				$modal.find('.modal-footer .btn:first').remove();
				$modal.modal();
				$modal.appendTo($('body'));
				
				/**
				 * 选择添加角色
				 * 
				 * @param 多选角色框
				 * @param 机构角色树
				 */
				var selected = function(multiselectObj, treeObj) {
					var checkedNodes = treeObj.getCheckedNodes(true);
					if(checkedNodes.length == 0) {
						MyCuckoo.alertMsg({ title : '提示', msg : '请您选择相应角色!' });
						return;
					}
					for (var i = 0; i < checkedNodes.length; i++) {
						var checkedNode = checkedNodes[i];
						var parentNode = checkedNode.getParentNode();
						var orgRoleId = checkedNode.id;
						var roleId = orgRoleId.substr(0, orgRoleId.indexOf('_'));
						var options = multiselectObj.find('option[value=' + roleId + ']');
						if(!options.length) {
							var $option = $('<option>');
							$option.val(roleId);
							parentNode ? $option.text(parentNode.text + '-' + checkedNode.text) : $option.text(checkedNode.text);
							multiselectObj.append($option);
						}
					}
					if(multiselectObj.find('option').length == 1) {
						var option = multiselectObj.find('option')[0];
						$modal.find('input[name=defaultRoleId]').val(option.value);
						$modal.find('input[name=defaultRoleName]').val(option.text);
					}
				};
				
				/**
				 * 选择移除角色
				 * 
				 * @param 多选角色框
				 * @param 机构角色树
				 */
				var unselected = function(multiselectObj, treeObj) {
					var $selected = multiselectObj.find(':selected');
					if (!$selected.length) {
						MyCuckoo.alertMsg({ title : '提示', msg : '请您选择角色列表!' });
						return;
					}
					var defaultRoleId = $modal.find('input[name=defaultRoleId]').val();
					$.each($selected, function(index, item) {
						var node = treeObj.getNodeByParam('id', item.value + '_2');
						if(node) {
							treeObj.checkNode(node, true);
							if(defaultRoleId == item.value) {
								$modal.find('input[name=defaultRoleId]').val('');
								$modal.find('input[name=defaultRoleName]').val('');
							}
						}
					});
					multiselectObj.html(multiselectObj.find(':not(:selected)'));
					
					if(multiselectObj.find('option').length == 1) {
						var option = multiselectObj.find('option')[0];
						$modal.find('input[name=defaultRoleId]').val(option.value);
						$modal.find('input[name=defaultRoleName]').val(option.text);
					} else if(multiselectObj.find('option').length == 0) {
						$modal.find('input[name=defaultRoleId]').val('');
						$modal.find('input[name=defaultRoleName]').val('');
					}
				};
			}
			
			// 重置密码
			function resetPwd() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.alertMsg({ state: 'warning', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				$.post('resetPwd', {id : _jsonObj.userId, userName : _jsonObj.userName},  function(data) {
					if(data.status) {
						MyCuckoo.showMsg({state:'success', title: '提示', msg: data.msg});
					} else {
						MyCuckoo.showMsg({state:'danger', title: '提示', msg: data.msg});
					}
				});
			}
			// the end...
			
		});
	</script>
</body>
</html>