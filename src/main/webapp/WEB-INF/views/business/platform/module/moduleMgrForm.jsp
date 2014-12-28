<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<div>
	<!-- 表单操作按钮 -->
	<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
	<form name="editForm" action="">
		<input type="hidden" name="moduleId" value="${module.moduleId}"/>
		<table class="table table-bordered">
			<tr>
				<td width=14%><label>模块名称</label></td>
				<td><input type=text  name="modName" value="${module.modName}" class="required" maxlength="10"/></td>
				<td width=14%><label>模块英文ID</label></td>
				<td><input type=text name="modEnId" value="${module.modEnId}" class="required" maxlength="40"/></td>
			</tr>
			<tr>
				<td width=14%><label>模块图片样式</label></td>
				<td><input type=text name="modImgCls" value="${module.modImgCls}" class="alphanumeric" /></td>
				<td width=14%><label>模块级别</label></td>
				<td>
					<select name="modLevel" class="required">
						<option value="1" <c:if test="${module.modLevel eq 1}">selected="selected"</c:if>>第一</option>
						<option value="2" <c:if test="${module.modLevel eq 2}">selected="selected"</c:if>>第二</option>
						<option value="3" <c:if test="${module.modLevel eq 3}">selected="selected"</c:if>>第三</option>
					</select>
				</td>
			</tr>
			<tr>
				<td width=14%><label>模块顺序</label></td>
				<td>
					<select name="modOrder" class="required">
						<option value="1" <c:if test="${module.modOrder eq 1}">selected="selected"</c:if>>1</option>
						<option value="2" <c:if test="${module.modOrder eq 2}">selected="selected"</c:if>>2</option>
						<option value="3" <c:if test="${module.modOrder eq 3}">selected="selected"</c:if>>3</option>
						<option value="4" <c:if test="${module.modOrder eq 4}">selected="selected"</c:if>>4</option>
						<option value="5" <c:if test="${module.modOrder eq 5}">selected="selected"</c:if>>5</option>
						<option value="6" <c:if test="${module.modOrder eq 6}">selected="selected"</c:if>>6</option>
						<option value="7" <c:if test="${module.modOrder eq 7}">selected="selected"</c:if>>7</option>
						<option value="8" <c:if test="${module.modOrder eq 8}">selected="selected"</c:if>>8</option>
						<option value="9" <c:if test="${module.modOrder eq 9}">selected="selected"</c:if>>9</option>
					</select>
				</td>
				<td width=14%><label>所属系统</label></td>
				<td>
					<c:import url="/platform/typeDictionaryMgr/getSmallTypeDicByBigTypeCode">
						<c:param name="selectName" value="belongToSys" />
						<c:param name="selectCode" value="${module.belongToSys}" />
						<c:param name="selectClass" value="required" />
						<c:param name="bigTypeCode" value="systemType" />
					</c:import>
				</td>
			</tr>
			<tr>
				<td width=14%><label>页面类型</label></td>
				<td>  
					<c:import url="/platform/typeDictionaryMgr/getSmallTypeDicByBigTypeCode">
						<c:param name="selectName" value="modPageType" />
						<c:param name="selectCode" value="${module.modPageType}" />
						<c:param name="selectClass" value="required" />
						<c:param name="bigTypeCode" value="modPageType" />
					</c:import>
				</td>
				<td width=14%><label>模块状态</label></td>
				<td>
						<select name="status" class="required">
							<option value="enable" <c:if test="${module.status eq 'enable'}">selected="selected"</c:if>>启用</option>
							<option value="disable" <c:if test="${module.status eq 'disable'}">selected="selected"</c:if>>停用</option>
						</select>
				</td>
			</tr>
			<tr>
				<td width=14%><label>模块的上级名称</label></td>
				<td>
					<input type="hidden" name="upModId" value="${module.upModId}" />
					<input type="text" name="upModName" value="${module.upModName}" class="required" />
					<span class="btn btn-warning btn-xs select"><span class="glyphicon glyphicon-search"></span></span>
				</td>
				<td width=14%><label>备注</label></td>
				<td><input type="text" name="memo" value="${module.memo}" maxlength="50"/></td>
			</tr>
		</table>
	</form>
</div>