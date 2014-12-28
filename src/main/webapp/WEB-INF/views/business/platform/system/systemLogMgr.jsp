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
								<table>
									<tr>
										<td><label>操作模块名&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="optModName" value="${param.optModName}" placeholder="操作模块名" />&nbsp;&nbsp;</td>
										<td><label>操作名&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="optName" value="${param.optName}" placeholder="操作名" />&nbsp;&nbsp;</td>
										<td><label>操作人姓名&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="optUserName" value="${param.optUserName}" placeholder="操作人姓名" />&nbsp;&nbsp;</td>
										<td><label>操作人角色&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="optUserRole" value="${param.optUserRole}" placeholder="操作人角色" />&nbsp;&nbsp;</td>
									</tr>
									<tr>
										<td><label>计算机IP&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="optPcIp" value="${param.optPcIp}" placeholder="计算机IP" />&nbsp;&nbsp;</td>
										<td><label>业务数据ID&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="optBusinessId" value="${param.optBusinessId}" placeholder="请输入数字" />&nbsp;&nbsp;</td>
										<td><label>开始时间&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="startTime" value="${param.startTime}" placeholder="开始时间" />&nbsp;&nbsp;</td>
										<td><label>结束时间&nbsp;</label></td>
										<td><input class="form-control input-sm" type="text" name="endTime" value="${param.endTime}" placeholder="结束时间" />&nbsp;&nbsp;</td>
									</tr>
									<tr>
										<td></td>
										<td colspan=7>
											<button class="btn btn-info" type="submit" name="search"><span class="glyphicon glyphicon-search"></span>查询</button>
											<button class="btn" type="button" name="clear"><span class="glyphicon glyphicon-repeat"></span>清空</button>
										</td>
									</tr>
								</table>
							</form>
						</div>
						<!-- 模块操作按钮 -->
						<tags:toolbar name="moduleOpt" value="${sessionScope.fourthMap[sessionScope.modEnId]}"></tags:toolbar>
						<!-- 内容列表 -->
						<table class="table table-striped table-hover table-condensed">
							<tr>
								<th><input type="checkbox" name="all" /></th>
								<th>序号</th>
								<th>操作模块名</th>
								<th>操作名</th>
								<th class="hide">操作内容</th>
								<th>业务数据ID</th>
								<th>计算机名称</th>
								<th>计算机IP</th>
								<th>操作人姓名</th>
								<th>操作人角色</th>
								<th>操作人机构</th>
								<th>操作时间</th>
								<th class="hide">开始时间</th>
								<th class="hide">结束时间</th>
							</tr>
							<c:forEach var="sysOptLog" varStatus="stat" items="${page.content}">
							<%
								String json = JsonUtils.toJson(pageContext.getAttribute("sysOptLog"));
								pageContext.setAttribute("json", json);
							%>
							<tr>
								<td><input type="checkbox" name="single" /><em class="hide">${json}</em></td>
								<td>${stat.index + 1}</td>
								<td>${sysOptLog.optModName}</td>
								<td>${sysOptLog.optName}</td>
								<td class="hide">${sysOptLog.optContent }</td>
								<td>${sysOptLog.optBusinessId}</td>
								<td>${sysOptLog.optPcName}</td>
								<td>${sysOptLog.optPcIp}</td>
								<td>${sysOptLog.optUserName}</td>
								<td>${sysOptLog.optUserRole}</td>
								<td>${sysOptLog.optUserOgan}</td>
								<td><fmt:formatDate value="${sysOptLog.optTime}" pattern="yyyy-MM-dd" /></td>
								<td class="hide"></td>
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
				case BasicConstant.OPT_VIEW_LINK:
					view();
					break;
				}
			});
			// 复选框
			MyCuckoo.checkbox();
			
			// 查看 
			function view() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() != 1) {
					MyCuckoo.showMsg({'state': 'danger', title: '提示', msg: '请选择一条件记录!'});
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				$.get('viewForm?id=' + _jsonObj.optId, function(data) {
					MyCuckoo.showDialog({
						title : '日志内容查看',
						msg : data
					});
				});
			}
			// the end....
		});
	</script>
</body>
</html>