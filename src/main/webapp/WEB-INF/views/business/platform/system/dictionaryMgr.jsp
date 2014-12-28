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
									<label class="sr-only">字典大类名称&nbsp;</label>
									<input type="text" class="form-control input-sm" name="bigTypeName" value="${param.bigTypeName}" placeholder=字典大类名称 />
								</div>
								<div class="form-group">
									<label class="sr-only">字典大类代码&nbsp;</label>
									<input type="text" class="form-control input-sm" name="bigTypeCode" value="${param.bigTypeCode}" placeholder=字典大类代码 />
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
								<th>字典大类名称</th>
								<th>字典大类代码</th>
								<th class="hide">字典小类名称</th>
								<th class="hide">字典小类代码</th>
								<th>字典状态</th>
								<th>创建者</th>
								<th>创建日期</th>
								<th class="hide">字典备注</th>
							</tr>
							<c:forEach var="dictionary" varStatus="stat" items="${page.content}">
							<%
								String json = JsonUtils.toJson(pageContext.getAttribute("dictionary"));
								pageContext.setAttribute("json", json);
							%>
							<tr>
								<td><input type="checkbox" name="single"	/><em class="hide">${json}</em></td>
								<td>${stat.index + 1}</td>
								<td>${dictionary.bigTypeName}</td>
								<td>${dictionary.bigTypeCode}</td>
								<td class="hide"></td>
								<td class="hide"></td>
								<td><tags:data name="status" value="${dictionary.status}"></tags:data></td>
								<td>${dictionary.creator}</td>
								<td><fmt:formatDate value="${dictionary.createDate}" pattern="yyyy年MM月dd日" /></td>
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
						MyCuckoo.showMsg({state: 'danger', title: '提示', msg: '请选择一条件记录!'});
						return;
					}
					var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
					_url = _url + '?id=' + _jsonObj.bigTypeId;
				}
				window.location = _url;
			}
			
			// 启用、停用操作
			function disEnable(flag) {
				var $singleCheck = $main.find('.table input:checked[name=single]');
				if ($singleCheck.size() != 1) {
					MyCuckoo.showMsg({ state : 'danger', title : '提示', msg : '请选择一条件记录!' });
					return;
				}
				var _jsonObj = $.parseJSON($singleCheck.next(':first').html());
				if (flag == _jsonObj.status) {
					var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '此类别字典已经启用!' : '此类别字典已经停用!');
					MyCuckoo.showMsg({ title : '提示', msg : msg });
					return;
				}
				disableEnalbeOpt(flag, _jsonObj.bigTypeId);
				// 启用、停用请求函数
				function disableEnalbeOpt(flag, id) {
					$.getJSON('disEnable', {id: id, disEnableFlag: flag}, function(json) {
						if(json.status) {
							var msg = (flag != BasicConstant.OPT_DISABLE_LINK ? '类别字典启用成功' : '类别字典停用成功!');
							MyCuckoo.showMsg({state:'success', title: '提示', msg: msg});
							// 刷新页面
							window.location.reload();
						} else {
							MyCuckoo.showMsg({state:'danger', title: '提示', msg: '操作失败!'});
						}
					});
				}
			}
		// the end....
		});
	</script>
</body>
</html>