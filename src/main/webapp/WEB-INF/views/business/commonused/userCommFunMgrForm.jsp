<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>常用配置</title>
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
						<c:if test="${not empty param.error}">
							<div class="alert alert-danger">提示: ${param.error}</div>
						</c:if>
						<h3 class="page-header">请分配您的常用功能菜单</h3>
						<form class="form" name="editForm">
							<table>
								<tr>
									<td>
										<div class="select-side">
											<span>未选菜单</span>
											<div style="width:200px; height:200px; border:1px solid #0088CC; overflow-y:auto;">
												<ul id="left_tree" class="ztree" data-tree='${userMenuList}'></ul>
											</div>
										</div>
									</td>
									<td>
										<div class="select-opt">
											<p class="to-right" title="添加">&lt;&lt;</p>
											<p class="to-left"  title="移除">&gt;&gt;</p>
										</div>
									</td>
									<td>
										<div class="select-side">
											<span>已选菜单</span>
											<select id="right_multi_select" name="menus" multiple="multiple" style="width:200px; height:200px; border:1px solid #0088CC;">
												<c:forEach var="userCommfun" items="${sessionScope.assignUumUserCommfunList}">
													<option value="${userCommfun.moduleId}">${userCommfun.modName}</option>
												</c:forEach>
											</select>
										</div>
									</td>
								</tr>
							</table>
							<div class="col-md-offset-2" style="margin-top:20px;">
								<a class="btn btn-primary"><span class="glyphicon glyphicon-hdd"></span> 提交</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.mycuckoo-main');
			var $form = $main.find('form[name=editForm]');
			
			/**
			 * 选择添加模块
			 * 
			 * @param 多选框对象
			 * @param 树对象
			 */
			var selected = function(multiselectObj, treeObj) {
				var checkedNodes = treeObj.getCheckedNodes(true);
				if(checkedNodes.length == 0) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请您选择模块!' });
					return;
				}
				for (var i = 0; i < checkedNodes.length; i++) {
					var checkedNode = checkedNodes[i];
					var id = checkedNode.id;
					var options = multiselectObj.find('option[value="' + id + '"]');
					if(!options.length) {
						var $option = $('<option>');
						$option.val(id);
						$option.text(checkedNode.text);
						multiselectObj.append($option);
					}
				}
			};
			
			/**
			 * 选择移除模块
			 * 
			 * @param 多选框对象
			 * @param 树对象
			 */
			var unselected = function(multiselectObj, treeObj) {
				var $selected = multiselectObj.find(':selected');
				if (!$selected.length) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请您选择模块!' });
					return;
				}
				$.each($selected, function(index, item) {
					var node = treeObj.getNodeByParam('id', item.value);
					if(node) {
						treeObj.checkNode(node, true);
					}
				});
				multiselectObj.html(multiselectObj.find(':not(:selected)'));
			};
			var setting = {
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
			
			var $leftSide = $form.find('#left_tree');
			var $rightSide = $form.find('#right_multi_select');
			var treeData = $.parseJSON($leftSide.attr('data-tree'));
			var leftTreeObj = $.fn.zTree.init($leftSide, setting, treeData);
			
			$leftSide.removeAttr('data-tree');
			// 添加勾上的角色
			$form.off('.to-right').on('click', '.to-right', function() {
				selected($rightSide, leftTreeObj);
			});
			// 移除勾上的角色
			$form.off('.to-left').on('click', '.to-left', function() {
				unselected($rightSide, leftTreeObj);
			});
			$form.on('click', '.btn:last', function() {
				var $selected = $form.find('#right_multi_select > option');
				if(!$selected.length) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请从左边的模块树中选择模块并添加到右边!' });
				} else {
					var moduleIdList = [];
					$.each($selected, function(index, item) {
						moduleIdList.push(item.value);
					});
					$.post('createForm', {moduleIdList : moduleIdList.join(',')}, function(json) {
						if(json.status) {
							MyCuckoo.showMsg({state:'success', title: '提示', msg: json.message});
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.message});
						}
					});
				}
			});
			// the end...
		});
	</script>
</body>
</html>