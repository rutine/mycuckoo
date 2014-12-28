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
	<title>系统平台</title>
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
									<label class="sr-only">操作名称&nbsp;</label>
									<input type="text" class="form-control input-sm" name="operateName" value="${param.operateName}" placeholder=操作名称 />
								</div>
								<button type="submit" class="btn btn-info btn-sm">&nbsp;查询
									<span class="glyphicon glyphicon-search"></span>
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
								<th>操作名称</th>
								<th>图片链接</th>
								<th>功能链接</th>
								<th>操作顺序</th>
								<th>操作组</th>
								<th>模块状态</th>
								<th>创建者</th>
								<th>创建日期</th>
								<th class="hide">模块备注</th>
							</tr>
							<c:forEach var="operate" varStatus="stat" items="${page.content}">
								<%
									String json = JsonUtils.toJson(pageContext.getAttribute("operate"));
									pageContext.setAttribute("json", json);
								%>
								<tr>
									<td><input type="checkbox" name="single"  /><em class="hidden">${json}</em></td>
									<td>${stat.index + 1}</td>
									<td>${operate.operateName}</td>
									<td>${operate.optImgLink}</td>
									<td>${operate.optFunLink}</td>
									<td>${operate.optOrder}</td>
									<td>${operate.optGroup}</td>
									<td><tags:data name="status" value="${operate.status}"></tags:data></td>
									<td>${operate.creator}</td>
									<td><fmt:formatDate value="${operate.createDate}" pattern="yyyy-MM-dd" /></td>
									<td class="hide"></td>
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
					var $singleCheck = $main.find('.table input:checked[name=single]');
					if($singleCheck.size() != 1) {
						MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
						return;
					}
					var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
					_url = _url + '?id=' + _jsonObj.operateId;
				}
				window.location = _url;
			}
			
			// 启用、停用操作
			function disEnable(flag) {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() != 1) {
					MyCuckoo.showMsg({state: 'warning', title: '提示', msg: '请选择一条件记录!'});
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (flag == _jsonObj.status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此操作已经启用!' : '此操作已经停用!');
					MyCuckoo.showMsg({state: 'info', title: '提示', msg: msg});
					return;
				}
				if (flag == BasicConstant.OPT_DISABLE_LINK) {
					MyCuckoo.showDialog({
						msg : '您确认停用此操作吗? 如果停用,相应模块下的此操作将自动清除,并重登生效。',
						okBtn: '是',
						cancelBtn: '否',
						ok : function() {
							disableEnalbeOpt(flag, _jsonObj.operateId);
						}
					});
				} else {
					MyCuckoo.showDialog({ okBtn: '是', cancelBtn: '否', ok : function() {
							disableEnalbeOpt(flag, _jsonObj.operateId);
						}
					});
				}
				// 启用、停用请求函数
				function disableEnalbeOpt(flag, id) {
					$.getJSON('disEnable', {id: id, disEnableFlag: flag}, function(json) {
						if(json.status) {
							var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '操作启用成功!' : '操作停用成功!');
							MyCuckoo.showMsg({state:'success', title: '提示', msg: msg});
							// 刷新页面
							window.location.reload();
						} else {
							MyCuckoo.showMsg({state:'error', title: '提示', msg: '操作失败!'});
						}
					});
				}
			}
		});
	</script>
</body>
</html>