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
									<label class="sr-only">任务名称&nbsp;</label>
									<input type="text" class="form-control input-sm" name="jobName" value="${param.jobName}" placeholder=任务名称 />
								</div>
								<div class="form-group">
									<label>触发器类型:&nbsp;</label>
									<label class="radio-inline">
										<input type="radio" name="triggerType" value="Simple" <c:if test="${param.triggerType eq 'Simple'}">checked</c:if>> 是&nbsp;&nbsp;
									</label>
									<label class="radio-inline">
										<input type="radio" name="triggerType" value="Cron" <c:if test="${param.triggerType eq 'Cron'}">checked</c:if>> 否&nbsp;&nbsp;
									</label>
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
								<th>任务名称</th>
								<th>任务类描述</th>
								<th>触发器类型</th>
								<th class="hide">时间表达式</th>
								<th class="hide">开始时间</th>
								<th class="hide">结束时间</th>
								<th class="hide">重复次数</th>
								<th class="hide">时间间隔(秒)</th>
								<th>任务状态</th>
								<th class="hide">参数备注</th>
								<th>创建者</th>
								<th>创建日期</th>
							</tr>
							<c:forEach var="scheduler" varStatus="stat" items="${page.content}">
							<%
								String json = JsonUtils.toJson(pageContext.getAttribute("scheduler"));
								pageContext.setAttribute("json", json);
							%>
							<tr>
								<td><input type="checkbox" name="single" /><em class="hide">${json}</em></td>
								<td>${stat.index + 1}</td>
								<td>${scheduler.jobName}</td>
								<td>${scheduler.jobClassDescript}</td>
								<td>${scheduler.triggerType }</td>
								<td class="hide"></td>
								<td class="hide"></td>
								<td class="hide"></td>
								<td class="hide"></td>
								<td class="hide"></td>
								<td><tags:data name="status" value="${scheduler.status}"></tags:data></td>
								<td class="hide"></td>
								<td>${scheduler.creator}</td>
								<td><fmt:formatDate value="${scheduler.createDate}" pattern="yyyy-MM-dd" /></td>
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
				case BasicConstant.OPT_DELETE_LINK:
					del();
					break;
				case 'schedulerStart':
					schedulerStart();
					break;
				case 'schedulerStop':
					schedulerStop();
					break;
				case 'jobStart':
					startOrStopJob(BasicConstant.OPT_ENABLE_LINK);
					break;
				case 'jobStop':
					startOrStopJob(BasicConstant.OPT_DISABLE_LINK);
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
						MyCuckoo.showMsg({state: 'danger', title: '提示', msg: '请选择一条件记录!'});
						return;
					}
					var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
					_url = _url + '?id=' + _jsonObj.jobId;
				}
				window.location = _url;
			}
			
			// 删除操作
			function del() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state: 'danger', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				MyCuckoo.showDialog({
					title: '警告提示',
					msg: '您确认删除此记录吗?',
					okBtn: '是',
					cancelBtn: '否',
					ok: function() {
						var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
						var _id = _jsonObj.jobId;
						$.getJSON('delete', {id: _id}, function(json) {
							if(json.status) {
								MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
								// 刷新页面
								window.location.reload();
							} else {
								MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.msg});
							}
						});
					}
				});
			}
			
			// 启动调度器
			function schedulerStart() {
				$.post('startScheduler', function(json) {
					if(json.status) {
						MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
						// 刷新页面
						window.location.reload();
					} else {
						MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.message});
					}
				}, 'json');
			}
			
			// 停止调度器
			function schedulerStop() {
				$.post('stopScheduler', function(json) {
					if(json.status) {
						MyCuckoo.showMsg({state:'success', title: '提示', msg: json.message});
						// 刷新页面
						window.location.reload();
					} else {
						MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.message});
					}
				}, 'json');
			}
			
			// 启用、停用操作
			function startOrStopJob(flag) {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.alertMsg({ state : 'danger', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (flag == _jsonObj.status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此任务调度已经启用!' : '此任务调度已经停止!');
					MyCuckoo.alertMsg({ title : '提示', msg : msg });
					return;
				}
				if (flag == 'enable') {
					disableEnalbeOpt('startJob', _jsonObj.jobId, _jsonObj.jobName);
				} else {
					disableEnalbeOpt('stopJob', _jsonObj.jobId, _jsonObj.jobName);
				}
				function disableEnalbeOpt(url, id, name) {
					$.post(url, {jobId: id, jobName : name}, function(json) {
						if(json.status) {
							MyCuckoo.showMsg({state:'success', title: '提示', msg: json.msg});
							// 刷新页面
							window.location.reload();
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: json.msg});
						}
					}, 'json');
				}
			}
		// the end....
		});
	</script>
</body>
</html>