<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统平台</title>
	<%@ include file="/WEB-INF/layout/link.jsp" %>
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
					<div class="col-sm-12 col-md-12">
						<c:if test="${not empty param.error}">
							<div class="alert alert-danger">提示: ${param.error}</div>
						</c:if>
						<h3 class="page-header">类别字典管理</h3>
						<!-- 表单操作按钮 -->
						<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
						<form class="form-inline" name="editForm" action="${action}Form" method="post">
							<input type="hidden" name="opt" value="" />
							<input type="hidden" name="bigTypeId" value="${dictionary.bigTypeId}"/>
							<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<c:choose>
									<c:when test="${action eq 'view' }"><p><strong  class="text-info">类别字典大类</strong></p></c:when>
									<c:otherwise><p><strong  class="text-info">类别字典大类添加</strong></p></c:otherwise>
								</c:choose>
								<table class="table table-bordered">
									<tr>
										<td width=14%><label>字典大类名称</label></td>
										<td><input type=text  name="bigTypeName" value="${dictionary.bigTypeName}" class="required" maxlength="20"/></td>
										<td width=14%><label>字典大类代码</label></td>
										<td><input type=text name="bigTypeCode" value="${dictionary.bigTypeCode}" class="required alphanumeric" maxlength="15"/></td>
									</tr>
									<tr>
										<td width=14%><label>字典状态</label></td>
										<td>
											<select name="status" class="required">
												<option value="enable" <c:if test="${dictionary.status eq 'enable'}">selected="selected"</c:if>>启用</option>
												<option value="disable" <c:if test="${dictionary.status eq 'disable'}">selected="selected"</c:if>>停用</option>
											</select>
										</td>
										<td width=14%><label>备注</label></td>
										<td><input type="text" name="memo" value="${dictionary.memo}" maxlength="50"/></td>
									</tr>
								</table>
							</div>
						<!-- </form>
						<form name="editForm2"> -->
							<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<c:choose>
									<c:when test="${action eq 'view' }"><p><strong class="text-info">类别字典小类</strong></p></c:when>
									<c:otherwise>
										<p><strong class="text-info">类别字典小类添加</strong>&nbsp;&nbsp;
										<span class="btn btn-info btn-xs add"><span class="glyphicon glyphicon-plus"></span></span></p>
									</c:otherwise>
								</c:choose>
								<table class="table table-condensed">
									<c:choose>
										<c:when test="${ fn:length(smallDictionarys) > 0 }">
										<c:forEach var="smallDict" varStatus="stat" items="${smallDictionarys}">
										<tr>
											<td width=14%><label>字典小类名称</label></td>
											<td><input type="text" name="sysplDicSmallTypes[${stat.index}].smallTypeName" 
													value="${smallDict.smallTypeName}" class="required" maxlength="20" /></td>
											<td width=14%><label>字典小类代码</label></td>
											<td><input type="text" name="sysplDicSmallTypes[${stat.index}].smallTypeCode" 
													value="${smallDict.smallTypeCode}" class="required alphanumeric" maxlength="10" /></td>
											<td <c:if test="${action eq 'view' }">class="hide"</c:if>>
												<span class="btn btn-danger btn-xs delete"><span class="glyphicon glyphicon-remove"></span></span>
											</td>
										</tr>
										</c:forEach>
										</c:when>
										<c:otherwise>
										<tr>
											<td width=14%><label>字典小类名称</label></td>
											<td><input type="text" name="sysplDicSmallTypes[0].smallTypeName" class="required" maxlength="20" /></td>
											<td width=14%><label>字典小类代码</label></td>
											<td><input type="text" name="sysplDicSmallTypes[0].smallTypeCode" class="required alphanumeric" maxlength="10" /></td>
											<td><span class="btn btn-danger btn-xs delete"><span class="glyphicon glyphicon-remove"></span></span></td>
										</tr>
										</c:otherwise>
									</c:choose>
								</table>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.additional_methods.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.messages_bs_zh.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.main');
			// -------------------绑定表单元素事件-------------------
			var $form = $main.find('form[name="editForm"]');
			var _index = parseInt('${fn:length(smallDictionarys)}');
			var $table = $main.find('.jumbotron:eq(1) .table');
			$main.on('click', '.btn.add', function() {
				var $row = $('<tr>'
					+ '<td width=14%><label>字典小类名称</label></td>'
					+ '<td><input type="text" name="sysplDicSmallTypes[' + _index + '].smallTypeName" class="required" maxlength="20" /></td>'
					+ '<td width=14%><label>字典小类代码</label></td>'
					+ '<td><input type="text" name="sysplDicSmallTypes[' + _index + '].smallTypeCode" class="required alphanumeric" maxlength="10" /></td>'
					+ '<td><span class="btn btn-danger btn-xs delete"><span class="glyphicon glyphicon-remove"></span></span></td>'
					+ '</tr>');
				
				$row.on('click', '.btn.delete', function() {
					$(this).off().parents('tr').remove();
				});
				$table.append($row);
				_index++;
			});
			$table.on('click', '.btn.delete', function() {
				$(this).off().parents('tr').remove();
				_index = $table.find('input[name$="smallTypeName"]').size();
				$table.find('input[name$="smallTypeName"]').each(function(index, input) {
					$(input).prop('name', 'sysplDicSmallTypes[' + index + '].smallTypeName');
				});
				$table.find('input[name$="smallTypeCode"]').each(function(index, input) {
					$(input).prop('name', 'sysplDicSmallTypes[' + index + '].smallTypeCode');
				});
				
			});
			// ---------------------------------------------------------
			// 操作按钮
			$main.on('click', '.btn-toolbar .btn-group a', function(event) {
				event.preventDefault();
				$(this).button('loading');
				switch($(this).attr('href')) {
				case "save":
					if($form.validate().form()) {
						$form.submit();
					}
					break;
				case "saveadd":
					if($form.validate().form()) {
						$form.find('input[name="opt"]').val('saveadd');
						$form.submit();
					}
					break;
				case "reback":
					window.location = 'index';
					break;
				}
				$(this).button('reset');
			});
		});
	</script>
</body>
</html>