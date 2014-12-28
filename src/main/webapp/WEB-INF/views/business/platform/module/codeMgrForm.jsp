<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
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
						<h3 class="page-header">系统编码管理</h3>
						<!-- 表单操作按钮 -->
						<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
						<form name="editForm" action="${action}Form" method="post">
							<input type="hidden" name="opt" value="" />
							<input type="hidden" name="codeId" value="${code.codeId}"/>
							<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<p><strong  class="text-info">系统编码参数</strong></p>
								<table class="table">
									<tr>
										<td width=14%><label>英文编码</label></td>
										<td><input type=text  name="codeEngName" value="${code.codeEngName}" class="required alphanumeric" maxlength="20"/></td>
										<td width=14%><label>中文编码</label></td>
										<td><input type=text name="codeName" value="${code.codeName}" class="required" maxlength="20"/></td>
									</tr>
									<tr>
										<td width=14%><label>模块名称</label></td>
										<td>
											<input type=text name="moduleName" value="${code.moduleName}" class="required" />
											<span class="btn btn-warning badge"><span class="glyphicon glyphicon-search"></span></span>
										</td>
										<td width=14%><label>编码状态</label></td>
										<td>
											<select name="status" class="required">
												<option value="enable" <c:if test="${code.status eq 'enable'}">selected="selected"</c:if>>启用</option>
												<option value="disable" <c:if test="${code.status eq 'disable'}">selected="selected"</c:if>>停用</option>
											</select>
										</td>
									</tr>
									<tr>
										<td width=14%><label>分隔符</label></td>
										<td><tags:data name="delimite" value="${code.delimite}"></tags:data></td>
										<td width=14%><label>编码备注</label></td>
										<td><input type="text" name="memo" value="${code.memo}" maxlength="50"/></td>
									</tr>
									<tr>
										<td width=14%><label>段数</label></td>
										<td><tags:data name="partNum" value="${code.partNum}"></tags:data></td>
										<td width=14%><label>编码效果</label></td>
										<td><input type="text" name="codeEffect" value="${code.codeEffect}" readOnly/></td>
									</tr>
								</table>
							</div>
							<div class="jumbotron" style="margin-bottom:10px; padding:15px; font-size:12px;">
								<p><strong  class="text-info">编码格式设置</strong></p>
								<table class="table">
									<tr>
										<td width=14%><label>段一格式</label></td>
										<td>
											<tags:data name="codeFormat" value="${code.part1}" field="part1"></tags:data>
										</td>
										<td width=14%><label>段一格式内容</label></td>
										<td>
											<c:choose>
												<c:when test="${code.part1 eq 'date'}">
													<tags:data name="dateFormat" value="${code.part1Con}" field="part1Con"></tags:data>
												</c:when>
												<c:when test="${code.part1 eq 'number'}">
													<tags:data name="numberFormat" value="${code.part1Con}" field="part1Con"></tags:data>
												</c:when>
												<c:when test="${code.part1 eq 'sysPara'}">
													<tags:data name="sysParaFormat" value="${code.part1Con}" field="part1Con" ></tags:data>
												</c:when>
												<c:otherwise>
													<input type="text" name="part1Con" value="${code.part1Con}" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td width=14%><label>段二格式</label></td>
										<td><tags:data name="codeFormat" value="${code.part2}" field="part2" 
												disabled="${empty code.partNum or code.partNum lt 2 ? 'disabled' : ''}"></tags:data></td>
										<td width=14%><label>段二格式内容</label></td>
										<td>
											<c:choose>
												<c:when test="${code.part2 eq 'date'}">
													<tags:data name="dateFormat" value="${code.part2Con}" field="part2Con" 
														disabled="${empty code.partNum or code.partNum lt 2 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:when test="${code.part2 eq 'number'}">
													<tags:data name="numberFormat" value="${code.part2Con}" field="part2Con" 
														disabled="${empty code.partNum or code.partNum lt 2 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:when test="${code.part2 eq 'sysPara'}">
													<tags:data name="sysParaFormat" value="${code.part2Con}" field="part2Con" 
														disabled="${empty code.partNum or code.partNum lt 2 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:otherwise>
													<input type="text" name="part2Con" value="${code.part2Con}" 
														disabled="${empty code.partNum or code.partNum lt 2 ? 'disabled' : ''}" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td width=14%><label>段三格式</label></td>
										<td><tags:data name="codeFormat" value="${code.part3}" field="part3" 
												disabled="${empty code.partNum or code.partNum lt 3 ? 'disabled' : ''}"></tags:data></td>
										<td width=14%><label>段三格式内容</label></td>
										<td>
											<c:choose>
												<c:when test="${code.part3 eq 'date'}">
													<tags:data name="dateFormat" value="${code.part3Con}" field="part3Con" 
														disabled="${empty code.partNum or code.partNum lt 3 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:when test="${code.part3 eq 'number'}">
													<tags:data name="numberFormat" value="${code.part3Con}" field="part3Con" 
														disabled="${empty code.partNum or code.partNum lt 3 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:when test="${code.part3 eq 'sysPara'}">
													<tags:data name="sysParaFormat" value="${code.part3Con}" field="part3Con" 
														disabled="${empty code.partNum or code.partNum lt 3 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:otherwise>
													<input type="text" name="part3Con" value="${code.part3Con}" 
														disabled="${empty code.partNum or code.partNum lt 3 ? 'disabled' : ''}"/>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td width=14%><label>段四格式</label></td>
										<td><tags:data name="codeFormat" value="${code.part4}" field="part4" 
												disabled="${empty code.partNum or code.partNum ne 4 ? 'disabled' : ''}"></tags:data></td>
										<td width=14%><label>段四格式内容</label></td>
										<td>
											<c:choose>
												<c:when test="${code.part4 eq 'date'}">
													<tags:data name="dateFormat" value="${code.part4Con}" field="part4Con" 
														disabled="${empty code.partNum or code.partNum ne 4 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:when test="${code.part4 eq 'number'}">
													<tags:data name="numberFormat" value="${code.part4Con}" field="part4Con" 
														disabled="${empty code.partNum or code.partNumne ne 4 ? 'disabled' : ''}" ></tags:data>
												</c:when>
												<c:when test="${code.part4 eq 'sysPara'}">
													<tags:data name="sysParaFormat" value="${code.part4Con}" field="part4Con" 
														disabled="${empty code.partNum or code.partNum ne 4 ? 'disabled' : ''}"></tags:data>
												</c:when>
												<c:otherwise>
													<input type="text" name="part4Con" value="${code.part4Con}" 
														disabled="${empty code.partNum or code.partNum ne 4 ? 'disabled' : ''}" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</table>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal -->
	<div id="code_mgr_form_modal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3>选择模块</h3>
				</div>
				<div class="modal-body">
					<ul class="nav nav-list">
						<li style="font-size:13px"><strong>模块树</strong></li>
						<li><ul class="ztree"></ul></li>
					</ul>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/layout/script.jsp" %>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.additional_methods.min.js" type="text/javascript"></script>
	<script src="${ctx}/static/jquery-validation/1.10.0/js/jquery-validate.messages_bs_zh.js" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery(function() {
			var $main = $('.main');
			// -------------------绑定表单元素事件-------------------
			var $form = $main.find('form[name=editForm]');
			var $delimite = $form.find(':input[name=delimite]');
			var $partNum = $form.find(':input[name=partNum]');
			var $codeEffect = $form.find(':input[name=codeEffect]');
			var $parts = $form.find(':input[name^=part]:gt(0)'); // 包含所有的段格式和段格式内容元素
			var $modal = $('#code_mgr_form_modal');
			// 选择模块名称
			var setting = {
				async : { enable : true, type : 'get', autoParam : [ 'id=moduleId' ], url : '${ctx}/platform/moduleMgr/getChildNodes' },
				callback : {
					onClick : function(evane, treeId, treeNode) {
						$form.find('input[name=moduleName]').val(treeNode.text);
						$modal.modal('hide');
					}
				},
				data : { key : { name : 'text', icon : 'iconCls' } },
				view : { dblClickExpand : false, showLine : true, selectedMulti : false, 
						expandSpeed : ($.browser.msie && parseInt($.browser.version) <= 6) ? '' : 'fast' 
				}
			};
			$form.on('focus click', 'input[name=moduleName], span.badge', function() {
				$.fn.zTree.init($modal.find('.modal-body .ztree'), setting);
				$modal.modal();
			});
			// 分隔符改变事件
			$delimite.on('change', function() {
				codeEffectSet();
			});
			// 改变段数
			$partNum.on('change', function() {
				var _num = parseInt($(this).children('option:selected').val());
				$parts.attr('disabled', 'disabled');
				$parts.each(function(index) {
					if(_num > (index / 2)) {
						$(this).removeAttr('disabled');
					}
				});
				codeEffectSet();
			});
			// 编码格式配置元素change事件
			$parts.on('change', function() {
				codeFormatChange(this);
			});
			function codeFormatChange(elDom) {
				switch(elDom.name) {
				case 'part1':
					codeFormatSelect($(elDom).children('option:selected').val(), 1, 'part1Con');
					break;
				case 'part2':
					codeFormatSelect($(elDom).children('option:selected').val(), 3, 'part2Con');
					break;
				case 'part3':
					codeFormatSelect($(elDom).children('option:selected').val(), 5, 'part3Con');
					break;
				case 'part4':
					codeFormatSelect($(elDom).children('option:selected').val(), 7, 'part4Con');
					break;
				}
				codeEffectSet();
			}
			// 段格式选择处理函数
			function codeFormatSelect(codeFormatType, index, codeFormatContentValue) {
				var _char = '<input type="text" name="' + codeFormatContentValue + '" value="" />';
				var _dateFormat = '<select name="' + codeFormatContentValue + '"><option value="yyyy">年(yyyy)</option><option value="yyyyMM">年月(yyyyMM)</option><option value="yyyyMMdd">年月日(yyyyMMdd)</option></select>';
				var _numberFormat = '<select name="' + codeFormatContentValue + '"><option value="01">两位(01)</option><option value="001">三位(001)</option><option value="0001">四位(0001)</option><option value="00001">五位(00001)</option><option value="000001">六位(000001)</option></select>';
				var _sysParaFormat = '<select name="' + codeFormatContentValue + '"><option value="userName">用户名</option><option value="roleUser">角色+用户名</option><option value="organRoleUser">机构+角色+用户名</option></select>';
				switch(codeFormatType) {
				case 'char':
					$parts.eq(index).replaceWith(_char);
					break;
				case 'date':
					$parts.eq(index).replaceWith(_dateFormat);
					break;
				case 'number':
					$parts.eq(index).replaceWith(_numberFormat);
					break;
				case 'sysPara':
					$parts.eq(index).replaceWith(_sysParaFormat);
					break;
				}
				// 替换了元素后重新查找元素并绑定change事件 
				
				$parts = $form.find(':input[name^=part]:gt(0)');
				$parts.off('change');
				$parts.on('change', function() {
					codeFormatChange(this);
				});
			}
			// format code date and system parameters
			function formatDate(format) {
				if (format == undefined) return '';
				if (format == 'yyyy') {
					d = new Date();
					return d.getFullYear();
				} else if (format == 'yyyyMM') {
					d = new Date();
					month = d.getMonth() + 1;
					var monthStr = month < 10 ? '0' + month : month + '';
					return d.getFullYear() + '' + monthStr;
				} else if (format == 'yyyyMMdd') {
					d = new Date();
					month = d.getMonth() + 1;
					var monthStr = month < 10 ? '0' + month : month + '';
					return d.getFullYear() + '' + monthStr + '' + d.getDate();
				} else if (format == 'userName') {
					return 'zhangsan';
				} else if (format == 'roleUser') {
					return '管理员~zhangsan';
				} else if (format == 'organRoleUser') {
					return '广州~管理员~zhangsan';
				} else {
					return format;
				}
			}
			// 显示编码效果
			function codeEffectSet() {
				var partNum = parseInt($partNum.children('option:selected').val());
				var delimite = $delimite.children('option:selected').val();
				var codeFormatContentId1 = formatDate($parts.eq(1).is('input') ? $parts.eq(1).val() : $parts.eq(1).children('option:selected').val());
				var codeFormatContentId2 = formatDate($parts.eq(3).is('input') ? $parts.eq(3).val() : $parts.eq(3).children('option:selected').val());
				var codeFormatContentId3 = formatDate($parts.eq(5).is('input') ? $parts.eq(5).val() : $parts.eq(5).children('option:selected').val());
				var codeFormatContentId4 = formatDate($parts.eq(7).is('input') ? $parts.eq(7).val() : $parts.eq(7).children('option:selected').val());
				var value;
				switch (partNum) {
				case 2:
					value = codeFormatContentId1 + delimite + codeFormatContentId2;
					break;
				case 3:
					value = codeFormatContentId1 + delimite + codeFormatContentId2 + delimite + codeFormatContentId3;
					break;
				case 4:
					value = codeFormatContentId1 + delimite + codeFormatContentId2 + delimite + codeFormatContentId3 + delimite + codeFormatContentId4;
					break;
				default:
					value = codeFormatContentId1;
				}
				$codeEffect.val(value);
			}
			// -------------------------------------------------------
			
			// 操作按钮
			$main.on('click', '.btn-toolbar .btn-group a', function(event) {
				event.preventDefault();
				$(this).button('loading');
				switch($(this).attr('href')) {
				case 'save':
					if($form.validate().form()) {
						$form.submit();
					}
					break;
				case 'saveadd':
					if($form.validate().form()) {
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