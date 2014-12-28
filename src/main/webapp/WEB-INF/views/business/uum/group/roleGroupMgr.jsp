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
									<label class="sr-only">组名称:&nbsp;</label>
									<input type="text" class="form-control input-sm" name="roleName" value="${param.roleName}" placeholder=组名称 />
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
						<div class="panel-group" id="accordion">
							<div class="panel panel-default">
								<div class="panel-heading">
									<div class="row panel-title">
										<div class="col-md-1"><strong>序号</strong></div>
										<div class="col-md-2"><strong>组名称</strong></div>
										<div class="col-md-2"><strong>组状态</strong></div>
										<div class="col-md-2"><strong>创建者</strong></div>
										<div class="col-md-2"><strong>创建日期</strong></div>
										<div class="col-md-2"><strong>组备注</strong></div>
									</div>
								</div>
							</div>
							<c:forEach var="group" varStatus="stat" items="${page.content}">
							<div class="panel panel-default">
								<div class="panel-heading">
									<div class="row panel-title" data-toggle="collapse" data-parent="#accordion" href="#collapse${stat.index}">
										<div class="col-md-1">
											<input type="hidden" name="id" value="${group.groupId }"/>
											<input type="hidden" name="status" value="${group.status }"/>
											<code>${stat.index + 1}</code>
										</div>
										<div class="col-md-2">${group.groupName}</div>
										<div class="col-md-2"><tags:data name="status" value="${group.status}"/></div>
										<div class="col-md-2">${group.creator}</div>
										<div class="col-md-2"><fmt:formatDate value="${group.createDate}" pattern="yyyy-MM-dd" /></div>
										<div class="col-md-2">${group.memo}</div>
									</div>
								</div>
								<div id="collapse${stat.index}" class="panel-collapse collapse">
									<div class="panel-body">
										<table class="table">
											<tr>
												<th>角色号</th>
												<th>角色名</th>
											</tr>
											<c:forEach var="groupMember" varStatus="stat" items="${group.uumGroupMembers}">
											<tr>
												<td>${groupMember.memberResourceCode}</td>
												<td>${groupMember.memberResourceName}</td>
											</tr>
											</c:forEach>
										</table>
									</div>
								</div>
							</div>
							</c:forEach>
						</div>
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
				}
			});
			// 复选框
			MyCuckoo.checkbox();
			
			// 增加、更新、查看操作
			function addOrUpdateOrView(_url) {
				if(/view|update/.test(_url)) {
					var $collapseBody = $main.find('#accordion .in');
					if(!$collapseBody.length) {
						MyCuckoo.alertMsg({ title : '提示', msg : '请选择一条件记录!' });
						return;
					}
					var id = $collapseBody.prev().find('input[name=id]').val();
					_url = _url + '?id=' + id;
				}
				window.location = _url;
			}
			
			// 启用、停用操作
			function disEnable(flag) {
				var $collapseBody = $main.find('#accordion .in');
				if (!$collapseBody.length) {
					MyCuckoo.alertMsg({ title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var id = $collapseBody.prev().find('input[name=id]').val();
				var status = $collapseBody.prev().find('input[name=status]').val();
				if (flag == status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此角色组已经启用!' : '此角色组已经停用!');
					MyCuckoo.alertMsg({ title : '提示', msg : msg });
					return;
				}
				disableEnalbeOpt(flag, id);
				function disableEnalbeOpt(flag, id) {
					$.getJSON('disEnable', {id: id, disEnableFlag: flag}, function(json) {
						if(json.status) {
							var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '角色组启用成功!' : '角色组停用成功!');
							MyCuckoo.showMsg({state:'success', title: '提示', msg: msg});
							// 刷新页面
							window.location = 'index';
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: '操作失败.'});
						}
					});
				}
			}
			// the end....
		});
	</script>
</body>
</html>