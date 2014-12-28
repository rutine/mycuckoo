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
									<label class="sr-only">公告标题:&nbsp;</label>
									<input type="text" class="form-control input-sm" name="afficheTitle" value="${param.afficheTitle}" placeholder=公告标题 />
								</div>
								<div class="form-group">
									<label>是否发布:&nbsp;</label>
									<label class="radio-inline">
										<input type="radio" name="affichePulish" value="0" <c:if test="${param.affichePulish ne 1}">checked</c:if>> 是&nbsp;&nbsp;
									</label>
									<label class="radio-inline">
										<input type="radio" name="affichePulish" value="1" <c:if test="${param.affichePulish eq 1}">checked</c:if>> 否&nbsp;&nbsp;
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
								<th>公告标题</th>
								<th>有效日期</th>
								<th>是否发布</th>
								<th class="hide">公告正文</th>
								<th class="hide">公告附件</th>
							</tr>
							<c:forEach var="affiche" varStatus="stat" items="${page.content}">
							<%
								String json = JsonUtils.toJson(pageContext.getAttribute("affiche"));
								pageContext.setAttribute("json", json);
							%>
							<tr>
								<td><input type="checkbox" name="single" /><em class="hide">${json}</em></td>
								<td>${stat.index + 1}</td>
								<td>${affiche.afficheTitle}</td>
								<td><fmt:formatDate value="${affiche.afficheInvalidate}" pattern="yyyy年MM月dd日" /></td>
								<td>
								<c:choose>
									<c:when test="${affiche.affichePulish eq 0}">已发布</c:when>
									<c:otherwise>未发布</c:otherwise>
								</c:choose>
								</td>
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
					_url = _url + '?id=' + _jsonObj.afficheId;
				}
				window.location = _url;
			}
			
			// 删除操作
			function del() {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if($singleCheck.size() == 0) {
					MyCuckoo.showMsg({state: 'danger', title: '提示', msg: '请选择一条件记录!'});
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
			// the end....
		});
	</script>
</body>
</html>