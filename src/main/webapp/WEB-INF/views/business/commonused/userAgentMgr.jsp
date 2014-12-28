<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>常用功能设置</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
	<link href="${ctx}/static/bootstrap-datetimepicker/master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
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
						<ul class="nav nav-tabs" role="tablist">
							<li class="<c:if test="${param.tab ne 'tabList'}">active</c:if>"><a href="#agent_form" role="tab" data-toggle="tab">代理授权</a></li>
							<li class="<c:if test="${param.tab eq 'tabList'}">active</c:if>"><a href="#agent_list" role="tab" data-toggle="tab">代理列表</a></li>
						</ul>
						
						<div class="tab-content">
							<div class="tab-pane fade <c:if test="${param.tab ne 'tabList'}">in active</c:if>" id="agent_form" role="tabpanel">
								<div class="agent-form"><%@ include file="/WEB-INF/views/business/commonused/userAgentMgrForm.jsp" %></div>
								<div class="panel-footer"><button type="button" class="btn btn-primary btn-sm">授权</button></div>
							</div>
							<div class="tab-pane fade <c:if test="${param.tab eq 'tabList'}">in active</c:if>" id="agent_list" role="tabpanel">
								<div class="agent-list"><%@ include file="/WEB-INF/views/business/commonused/userAgentMgrList.jsp" %></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/bootstrap-datetimepicker/master/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
	<script src="${ctx}/static/bootstrap-datetimepicker/master/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.main');
			var $form = $main.find('#agent_form form');
			var $list = $main.find('#agent_list .agent-list');
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
			
			var $userId = $form.find('input[name=userId]');
			var $userName = $form.find('input[name=userName]');
			// 授权代理人
			$userName.on('keyup', function(event) {
				var $this = $(this);
				var $li = $this.nextAll();
				var str = $this.val().trim();
				
				if(!str) return; 
				
				switch(event.keyCode) {
				case 13: // enter键
					$this.prev().val($li.children('.active').prop('id'));
					$this.val($li.children('.active').text());
					$(document).off('mouseup');
					$li.off().find('a').off().end().remove();
					break;
				case 38: // 方向键上
					var $next = $li.children('.active');
					var $prev = $next.prev().size() ? $next.prev() : $li.children('a:last');
					$next.removeClass('active');
					$prev.addClass('active');
					break;
				case 40: // 方向键下
					var $prev = $li.children('.active');
					var $next = $prev.next().size() ? $prev.next() : $li.children('a:first');
					$prev.removeClass('active');
					$next.addClass('active');
					break;
				default:
					$.get('queryUsersByUserNamePy', {q : str}, function(data) {
						if(data) {
							$(document).off('mouseup').one('mouseup', function(e){
								if(!$li.is(e.target) && $li.has(e.target).length === 0) {
									$userId.val('');
									$userName.val('');
									$li.off().find('a').off().end().remove();
								}
							});
							$li.off().find('a').off().end().remove();
							
							$li = $('<div class="list-group"></div>');
							for(var i = 0; i < data.length; i++) {
								var $a = $('<a href="#" class="list-group-item"></a>');
								i || $a.addClass('active');
								$a.text(data[i].userName);
								$a.prop('id', data[i].userId);
								$a.appendTo($li);
							}
							
							// 选手输入提示
							$li.css({
											position: 'absolute', 
											top: $this.position.top + $this.parent().height(), 
											left: $this.position.left, 
											width: $this.parent().width(),
											overflow: 'hidden',
											zIndex: 1001
									})
									.on('click', 'a', function() {
										$userId.val($(this).prop('id'));
										$userName.val($(this).text());
										$(document).off('mouseup');
										$li.off().find('a').off().end().remove();
									})
									.insertAfter($this);
						}
					});
					break;
				}
			});
			// 用户有效期
			$form.find('#begin_date, #end_date').datetimepicker({
				language : 'zh-CN',
				format : 'yyyy-mm-dd hh:ii',
				autoclose : 1,
				todayHighlight: 1,
				todayBtn : 'linked',
				startView : 2,
				minView : 0,
				maxView : 1,
				forceParse : 0
			});
			// 代理权限范围
			$form.on('click', ':radio[name=agentScope]', function() {
				if($(this).val() == 'all') {
					$form.find('.agent-privilege-setting').removeClass('in');
				} else {
					$form.find('.agent-privilege-setting').addClass('in');
				}
			});
			// 代理权限设置
			var setting1 = {
					check : { enable : true },
					data : {
						key : { name : 'text' },
						simpleData : { enable : true, pIdKey : 'parentId', rootPId : null }
					},
					callback : {
						onCheck : function(event, treeId, treeNode) {
							if(treeNode.checked) {
								 var leftTreeObj = $.fn.zTree.getZTreeObj("left_tree");
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
								 var rightTreeObj = $.fn.zTree.getZTreeObj("right_tree");
								 rightTreeObj.expandNode(treeNode, true, true, true);
							}
						}
					}
			};
			var $leftTree = $form.find('#left_tree');
			var $rightTree = $form.find('#right_tree');
			var leftTreeData = $.parseJSON($leftTree.attr('data-tree'));
			$leftTree.removeAttr('data');
			var leftTreeObj = $.fn.zTree.init($leftTree, setting1, leftTreeData);
			var rightTreeObj = $.fn.zTree.init($rightTree, setting2, []);
			// 添加勾上的模块
			$form.off('click', '.to-right').on('click', '.to-right', function() {
				selUnselOpt(leftTreeObj, rightTreeObj);
			});
			// 移除勾上的模块
			$form.off('click', '.to-left').on('click', '.to-left', function() {
				selUnselOpt(rightTreeObj, leftTreeObj);
			});
			// 保存相关
			$main.on('click', '#agent_form .panel-footer .btn', function() {
				if(!$userName.val()) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请输入授权代理人!'});
					return;
				} else if(!$form.find('input[name=beginDate]').val()) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请选择开始时间!'});
					return;
				} else if(!$form.find('input[name=endDate]').val()) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请选择结束时间!'});
					return;
				} else if($form.find('input:checked').length == 0) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请选择权限范围!'});
					return;
				}
				
				var agentScope = $form.find('input:checked').val();
				var optIdList = [];
				if(agentScope == 'part') {
					var selectedNodes = rightTreeObj.getNodesByFilter(function(node) {
						if(!node.isParent) return true;
						return false;
					});
					$.each(selectedNodes, function(index, node) {
						optIdList.push(Number(node.id) - 1000);
					});
				} else {
					optIdList.push('all');
				}
				
				var params = {};
				params.agentUserId = $userId.val();
				params.beginDate = $form.find('input[name=beginDate]').val();
				params.endDate = $form.find('input[name=endDate]').val();
				params.reason = $form.find('textarea[name=reason]').val();
				params.optIdList = optIdList.join(',');
				$.post('saveUserAgent', params, function(json) {
					if(!json.status || json.data.exists) {
						MyCuckoo.alertMsg({ title : '提示', msg : '此用户已被授权！'});
					} else {
						window.location = 'index?tab=agentList';
					}
				});
			});
			
			// 列表页面相关
			$list.on('click', '.table tr a.btn', function(e) {
				e.stopPropagation();
				var id = $(this).attr('data-id');
				var url = $(this).attr('data-url');
				if(!id) { return; }
				// 查看权限
				if(url != 'delete') {
					var setting = {
							data : {
								key : { name : 'text' },
								simpleData : { enable : true, pIdKey : 'parentId', rootPId : null }
							}
					};
					$.post(url, {id: id}, function(json) {
						MyCuckoo.showMsg({ title : '提示', msg : '查询成功.'});
						$.fn.zTree.init($list.find('#tree_agent_user'), setting, json);
					});
					
					return;
				}
				// 删除
				MyCuckoo.showDialog({
					title: '提示',
					msg: '您确认要取消对该用户的授权吗?',
					okBtn: '是',
					cancelBtn: '否',
					ok: function() {
						$.post(url, {id: id}, function(json) {
							if(json.status) {
								MyCuckoo.showMsg({state: 'success', title: '提示', msg: json.msg});
								window.location = 'index?tab=agentList';// 刷新列表
							} else {
								MyCuckoo.showMsg({state: 'danger', title: '提示', msg: json.msg});
							}
						});
					}
				});
			});
		});
	</script>
</body>
</html>