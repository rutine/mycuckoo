<%@ page import="com.mycuckoo.domain.platform.SysplAccessory" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8" %>
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
	<link href="${ctx}/static/bootstrap-datetimepicker/master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
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
						<h3 class="page-header">系统公告管理</h3>
						<!-- 表单操作按钮 -->
						<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
						<form name="editForm" action="${action}Form" method="post" class="form form-inline">
							<input type="hidden" name="opt" value="" />
							<input type="hidden" name="afficheId" value="${affiche.afficheId}"/>
							<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<p><strong class="text-info">公告信息</strong></p>
								<table class="table">
									<tr>
										<td width=14%><label>公告标题</label></td>
										<td><input type=text name="afficheTitle" value="${affiche.afficheTitle}" 
													class="form-control required" maxlength="100"/></td>
										<td width=14%><label>有效日期</label></td>
										<td>
											<div id="affiche_invalidate" class="input-group date">
												<input type="text" class="form-control required" name="afficheInvalidate" 
													value="<fmt:formatDate value="${affiche.afficheInvalidate}" pattern="yyyy-MM-dd" />" readOnly />
												<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span> 
												<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
											</div> 
										</td>
									</tr>
									<tr>
										<td width=14%><label>是否发布</label></td>
										<td colspan="3">
											<label class="radio-inline">
												<input type="radio" name="affichePulish" value="0" 
														<c:if test="${affiche.affichePulish ne 1}">checked</c:if>> 是&nbsp;&nbsp;
											</label>
											<label class="radio-inline">
												<input type="radio" name="affichePulish" value="1" 
														<c:if test="${affiche.affichePulish eq 1}">checked</c:if>> 否&nbsp;&nbsp;
											</label>
										</td>
									</tr>
									<tr>
										<td width=14%><label>公告正文</label></td>
										<td colspan="3"><textarea rows="6" name="afficheContent" class="form-control">${affiche.afficheContent}</textarea></td>
									</tr>
								</table>
							</div>
						</form>
						<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
							<p>
								<strong class="text-info">公告附件</strong>&nbsp;&nbsp;
							</p>
							<c:if test="${action ne 'view' }">
							<div id="file_picker"><span class="glyphicon glyphicon-plus"></span> 添加文件</div>
							</c:if>
							<table class="table table-condensed file-list">
							<c:forEach var="accessory" varStatus="stat" items="${affiche.accessoryList}">
								<c:set var="imgType" value="${accessory.accessoryName.substring(accessory.accessoryName.lastIndexOf('.'))}" />
								<c:set var="imgIcon" value="/static/images/button/no-format.png" />
								<c:choose>
									<c:when test="${imgType eq 'docx' or imgType eq 'doc'}">
										<c:set var="imgIcon" value="/static/images/button/word.png"></c:set>
									</c:when>
									<c:when test="${imgType eq 'pdf'}">
										<c:set var="imgIcon" value="/static/images/button/pdf.png"></c:set>
									</c:when>
									<c:when test="${imgType eq 'pptx' or imgType eq 'ppt'}">
										<c:set var="imgIcon" value="/static/images/button/powerpoint.png"></c:set>
									</c:when>
									<c:when test="${imgType eq 'xlsx' or imgType eq 'xls'}">
										<c:set var="imgIcon" value="/static/images/button/excel.png"></c:set>
									</c:when>
								</c:choose>
								<tr class="template-download fade in">
									<td width="25%" class="name">
										<a href="${ctx}/platform/accessoryMgr/download/${accessory.accessoryId}">
											<img src="${ctx}/${imgIcon}">&nbsp;&nbsp;
											<span>${accessory.accessoryName.substring(accessory.accessoryName.indexOf('_'))}</span>
										</a>
									</td>
									<td width="8%" class="size"></td>
									<td>
									<c:if test="${action eq 'update'}">
										<button class="btn btn-danger btn-sm delete" data-type="get" 
												data-url="${ctx}/platform/accessoryMgr/delete/${accessory.accessoryId}">
											<span class="glyphicon glyphicon-trash"></span>
										</button>
									</c:if>
									</td>
									<td></td>
								</tr>
							</c:forEach>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/bootstrap-datetimepicker/master/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
	<script src="${ctx}/static/bootstrap-datetimepicker/master/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.additional_methods.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.messages_bs_zh.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function($) {
			var $main = $('.main');
			var $fileList = $main.find('table.file-list');
			// -------------------绑定表单元素事件-------------------
			var $form = $main.find('form[name=editForm]');
			// 公告时间选择
			$form.find('#affiche_invalidate').datetimepicker({
				language : 'zh-CN',
				format : 'yyyy-mm-dd',
				autoclose : 1,
				todayHighlight: 1,
				todayBtn : 'linked',
				startView : 2,
				minView : 2,
				forceParse : 0
			});
			// 附件图标
			var getThumbnail = function(fileName) {
				var splitFlag = fileName.indexOf('_');
				fileName = fileName.substr(splitFlag + 1);
				var suffixName = fileName.substr(fileName.lastIndexOf('.') + 1);
				var imgPictureUrl = '${ctx}/static/images/button/no-format.png';
				if (suffixName == 'docx' || suffixName == 'doc') {
					imgPictureUrl = '${ctx}/static/images/button/word.png';
				} else if (suffixName == 'pdf') {
					imgPictureUrl = '${ctx}/static/images/button/pdf.png';
				} else if (suffixName == 'pptx' || suffixName == 'ppt') {
					imgPictureUrl = '${ctx}/static/images/button/powerpoint.png';
				} else if (suffixName == 'xlsx' || suffixName == 'xls') {
					imgPictureUrl = '${ctx}/static/images/button/excel.png';
				}
				return imgPictureUrl;
			};
			// 上传模板
			var uploadTemplate = function(file) {
				var row = $('<tr class="template-upload fade in">' + 
					'		<td width="25%" class="name"><img /><span></span></td>' + 
					'		<td width="8%" class="size"></td>' + 
					(	file.error ? 
					'		<td class="error"></td><td></td>' : 
					'		<td>' + 
					'			<button class="btn btn-info btn-sm start" data-loading-text="正在上传...">开始上传' +
					'					<span class="glyphicon glyphicon-upload"></span></button>' + 
					'			<button class="btn btn-warning btn-sm cancel">取消' + 
					'					<span class="glyphicon glyphicon-ban-circle"></span></button>' + 
					'		</td>' +
					'		<td width="30%">' + 
					'			<div class="progress hidden"><div class="progress-bar progress-bar-success ' + 
					'					progress-bar-striped" role="progressbar" style="width:0%;"></div></div>' + 
					'		</td>'
					) + 
					'</tr>');
				row.prop('id', file.id);
				row.find('.name img').prop('src', getThumbnail(file.name));
				row.find('.name span').text(file.name);
				row.find('.size').text(WebUploader.Base.formatSize(file.size));
				if (file.error) {
					row.find('.error').text(file.error);
				}
				
				return row;
			};
			// 下载模板
			var downloadTemplate = function(file) {
					var row = $('<tr class="template-download fade in">' + 
						'		<td width="25%" class="name"><a><img /><span></span></a></td>' + 
						'		<td width="8%" class="size"></td>' + 
						(		file.error ? '<td class="error"></td>' : '<td></td>') + 
						'		<td><button class="btn btn-danger btn-sm delete">' + 
						'					<span class="glyphicon glyphicon-trash"></span></button></td>' + 
						'		<td></td>' + 
						'</tr>'); 
					row.prop('id', file.id);
					row.find('.name a img').prop('src', getThumbnail(file.name));
					row.find('.name a span').text(file.name);
					row.find('.size').text(WebUploader.Base.formatSize(file.size));
					if (file.error) {
						row.find('.error').text(file.error);
					} else {
						row.find('a').prop('href', '${ctx}' + file.url);
						row.find('.delete').attr('data-type', file.delete_type).attr('data-url', '${ctx}' + file.delete_url);
					}
				return row;
			};
			// 文件上传
			var uploader = WebUploader.create({
				// swf文件路径
				swf: '${ctx}/webuploader/0.1.5/js/Uploader.swf',
				// 文件接收服务端。
				server: '${ctx}/platform/accessoryMgr/upload',
				// 选择文件的按钮, 可选
				// 内部根据当前运行是创建，可能是input元素，也可能是flash.
				pick: $main.find('#file_picker'),
				// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
				resize: false,
				// 额外参数
				formData: {
					afficheId: '${affiche.afficheId}'
				}
			});
			// 当有文件被添加进队列的时候
			uploader.on('fileQueued', function(file) {
				var row = uploadTemplate(file);
				row.on('click', '.btn', function() {
					var index = $(this).index();
					switch(index) {
					case 0 : 
						$(this).button('loading');
						uploader.upload(file);
						break;
					case 1 : 
						uploader.cancelFile(file);
						$(this).prev().button('reset');
						break;
					}
				});
				$fileList.append(row);
			});
			uploader.on('uploadProgress', function(file, percentage) {
				var $percent = $fileList.find('#' + file.id).find('.progress .progress-bar').parent().removeClass('hidden').end();
				$percent.css('width', percentage * 100 + '%');
				$percent.text(parseInt(percentage * 100) + '%');
			});
			uploader.on('uploadSuccess', function(file, json) {
				if(!json || !json.files.length) return;
				
				file = MyCuckoo.apply(file, json.files[0]);
				var row = downloadTemplate(file);
				row.on('click', '.btn.delete', function() {
					var $this = $(this);
					$.ajax({
						url : $this.attr('data-url'),
						type : $this.attr('data-type'),
						success : function(json) {
							if(json.status) {
								$this.off();
								row.remove();
								$form.find('input[name=accessoryNameList][value=' + file.name + ']').remove();
								uploader.removeFile(file);
							} else {
								row.find('td:eq(2)').addClass('error').html('删除失败!');
							}
						} 
					});
				});
				$fileList.find('#' + file.id).find('td:eq(3) .btn').off().end().remove();
				$fileList.append(row);
				$form.append($('<input type="hidden" name="accessoryNameList">').val(file.name));
			});
			uploader.on('uploadError', function(file) {
				$fileList.find('#' + file.id).find('td:eq(2)').addClass('error').html('上传出错!');
				$fileList.find('#' + file.id).find('td:eq(3) .progress').fadeOut();
				$fileList.find('#' + file.id).find('td:eq(4)) .btn').off().remove();
			});
			// -------------------------------------------------------
			
			// 删除附件
			$main.on('click', '.btn.delete', function() {
				var $this = $(this);
				var row = $this.parents('.template-download');
				$.ajax({
					url : $this.attr('data-url'),
					type : $this.attr('data-type'),
					success : function(json) {
						if(json.status) {
							$this.off();
							row.remove();
						} else {
							row.find('td:eq(2)').addClass('error').html('删除失败!');
						}
					} 
				});
			});
			// 操作按钮
			$main.on('click', '.btn-toolbar .btn-group a', function(event) {
				event.preventDefault();
				$(this).button('loading');
				switch ($(this).attr('href')) {
				case 'save':
					if ($form.validate().form()) {
						$form.submit();
					}
					break;
				case 'saveadd':
					if ($form.validate().form()) {
						$form.find('input[name=opt]').val('saveadd');
						$form.submit();
					}
					break;
				case 'reback':
					window.location = 'index';
					break;
				}
				$(this).button('reset');
			});
		});
	</script>
</body>
</html>