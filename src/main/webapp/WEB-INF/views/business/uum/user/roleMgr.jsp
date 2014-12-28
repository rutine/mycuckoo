<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ page import="com.mycuckoo.common.utils.JsonUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>统一用户</title>
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
						<!-- 查询表单 -->
						<div class="page-header">
							<form class="form-inline" name="searchForm" action="index">
								<div class="form-group">
									<label class="sr-only">角色名称:&nbsp;</label>
									<input type="text" class="form-control input-sm" name="roleName" value="${param.roleName}" placeholder=角色名称 />
								</div>
								<button type="submit" class="btn btn-info btn-sm" name="search">&nbsp;查询
									<span class="glyphicon glyphicon-search"></span>
								</button>
								<button type="button" class="btn btn-default btn-sm" name="clear">&nbsp;清空
									<span class="glyphicon glyphicon-repeat"></span>
								</button>
							</form>
						</div>
						<!-- 模块操作按钮 -->
						<tags:toolbar name="moduleOpt" value="${sessionScope.fourthMap[sessionScope.modEnId]}"></tags:toolbar>
						<!-- 内容列表 -->
						<table class="table table-striped table-hover table-condensed">
							<tr>
								<th><input type="checkbox" name="all" /></th>
								<th>序号</th>
								<th>角色名称</th>
								<th>角色级别</th>
								<th>角色状态</th>
								<th>创建人</th>
								<th>创建日期</th>
								<th>备注</th>
							</tr>
							<c:forEach var="role" varStatus="stat" items="${page.content}">
							<%
								String json = JsonUtils.toJson(pageContext.getAttribute("role"));
								pageContext.setAttribute("json", json);
							%>
							<tr>
								<td><input type="checkbox" name="single" /><em class="hidden">${json}</em></td>
								<td>${stat.index + 1}</td>
								<td>${role.roleName}</td>
								<td><tags:data name="number" value="${role.roleLevel}"/></td>
								<td><tags:data name="status" value="${role.status}"/></td>
								<td>${role.creator}</td>
								<td><fmt:formatDate value="${role.createDate}" pattern="yyyy-MM-dd" /></td>
								<td>${role.memo}</td>
							</tr>
							</c:forEach>
						</table>
						<!-- 分页 -->
						<tags:pagination paginationSize="10" page="${page}"></tags:pagination>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.main');
			var $form = $main.find('form[name=searchForm]');
			// 表单清空
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
				var opt = $(this).attr('href');
				switch(opt) {
				case BasicConstant.OPT_ADD_LINK:
					addOrUpdateOrView('createForm');
					break;
				case BasicConstant.OPT_MODIFY_LINK:
					addOrUpdateOrView('updateForm');
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
					optpri();
					break;
				}
			});
			// 复选框
			MyCuckoo.checkbox();
			
			// 增加、更新、查看操作
			function addOrUpdateOrView(_url) {
				if(/view|update/.test(_url)) {
					var $singleCheck = $main.find('.table input:checked[name=single]');
					if($singleCheck.size() != 1) {
						MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
						return;
					}
					var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
					_url = _url + '?id=' + _jsonObj.roleId;
				}
				window.location = _url;
			}
			
			// 删除操作
			function del() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() == 0) {
					MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
					return;
				}
				MyCuckoo.showDialog({
					title: '警告提示',
					msg: '您确认删除此记录吗?',
					okBtn: '是',
					cancelBtn: '否',
					ok: function() {
						var ids = [];
						$singleCheck.each(function() {
							var _jsonObj = $.parseJSON($(this).next(':first').html());
							var _id = _jsonObj.afficheId;
							ids.push(_id);
						});
						$.getJSON('delete', {ids: ids.join(',')}, function(json) {
							if(json.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
								// 刷新页面
								window.location.reload();
							} else {
								MyCuckoo.showMsg({state:'error', title: '提示', msg: json.msg});
							}
						});
					}
				});
			}
			
			// 启用、停用操作
			function disEnable(flag) {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() == 0) {
					MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (flag == _jsonObj.status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此角色已经启用!' : '此角色已经停用!');
					MyCuckoo.alertMsg({ state: 'warning', title : '提示', msg : msg });
					return;
				}
				if (flag == BasicConstant.OPT_DISABLE_LINK) {
					MyCuckoo.showDialog({
						msg : '您确认停用此角色? 如停用,相应机构下的此角色将自动清除.',
						okBtn: '是',
						cancelBtn: '否',
						ok : function() {
							disableEnalbeOpt(flag, _jsonObj.roleId);
						}
					});
				} else {
					disableEnalbeOpt(flag, _jsonObj.roleId);
				}
				function disableEnalbeOpt(flag, id) {
					$.getJSON('disEnable', {id: id, disEnableFlag: flag}, function(json) {
						if(json.status) {
							var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '角色启用成功!!' : '角色停用成功');
							MyCuckoo.showMsg({state:'success', title: '提示', msg: msg});
							// 刷新页面
							window.location.reload();
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: '角色已分配用户, 您不能停用此角色!如停用,请先将用户角色删除.'});
						}
					});
				}
			}
			
			// 普通权限分配
			function optpri() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state : 'warning', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				
				/**
				 * 删除结点
				 * @param	delNode 结点对象
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
				
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				var $modal = $(MyCuckoo.modalTemplate);
				$modal.on('hidden.bs.modal', function() { $(this).off().find('.btn').off().end().remove(); });
				$modal.find('h3').text('角色分配普通权限');
				$modal.find('.modal-body').load('getRolePrivilege', {id : _jsonObj.roleId, roleName : _jsonObj.roleName}, function() {
					var setting1 = {
							check : { enable : true },
							data : {
								key : { name : 'text' },
								simpleData : { enable : true, pIdKey : 'parentId', rootPId : null }
							},
							callback : {
								onCheck : function(event, treeId, treeNode) {
									if(treeNode.checked) {
										 var leftTreeObj = $.fn.zTree.getZTreeObj('left_tree');
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
										 var rightTreeObj = $.fn.zTree.getZTreeObj('right_tree');
										 rightTreeObj.expandNode(treeNode, true, true, true);
									}
								}
							}
					};
					var $this = $modal; // modal
					var leftTreeData = $.parseJSON($this.find('#left_tree').attr('data'));
					var rightTreeData = $.parseJSON($this.find('#right_tree').attr('data'));
					$this.find('#left_tree').removeAttr('data');
					$this.find('#right_tree').removeAttr('data');
//					leftTreeData = leftTreeData.replace(/(\w+)\s*:\s*'([^']*)'/g, '"$1" : "$2"');
					var leftTreeObj = $.fn.zTree.init($this.find('#left_tree'), setting1, leftTreeData);
					var rightTreeObj = $.fn.zTree.init($this.find('#right_tree'), setting2, rightTreeData);
					// 添加勾上的模块
					$this.off('click', '.to-right').on('click', '.to-right', function() {
						selUnselOpt(leftTreeObj, rightTreeObj);
					});
					// 移除勾上的模块
					$this.off('click', '.to-left').on('click', '.to-left', function() {
						selUnselOpt(rightTreeObj, leftTreeObj);
					});
					// 保存权限相关
					$form = $this.find('div.tab-content form.form-inline');
					$this.on('click', '.panel-footer:eq(0) .btn', function() {
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
						$.post('saveOptPrivilege', {id : _jsonObj.roleId, privilegeScope : optPrivilegeScope, optIdList : optIdList.join(',')}, function(data) {
							if(data.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: data.msg});
							} else {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: data.msg});
							}
						});
					});
					$this.on('click', '.panel-footer:eq(1) .btn', function() {
						if($form.eq(1).find('input:checked').length == 0) {
							MyCuckoo.alertMsg({ title : '提示', msg : '请选择权限范围!' });
							return;
						}
						var rowPrivilege = $form.eq(1).find('input:checked').val();
						$.post('saveRowPrivilege', {id : _jsonObj.roleId, rowPrivilege : rowPrivilege}, function(data) {
							if(data.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: data.msg});
							} else {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: data.msg});
							}
						});
					});
				});
				$modal.find('.modal-footer .btn:first').remove();
				$modal.modal();
				$modal.appendTo($('body'));
			}
			// the end....
		});
	</script>
</body>
</html>