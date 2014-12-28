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
		<input type="hidden" name="districtId" value="${district.districtId}"/>
		<table class="table table-bordered">
			<tr>
				<td width=14%><label>地区名称</label></td>
				<td><input type=text  name="districtName" value="${district.districtName}" class="required" maxlength="10"/></td>
				<td width=14%><label>地区代码</label></td>
				<td><input type=text name="districtCode" value="${district.districtCode}" maxlength="10"/></td>
			</tr>
			<tr>
				<td width=14%><label>地区邮编</label></td>
				<td><input type=text name="districtPostal" value="${district.districtPostal}" class="digits" maxlength="6"/></td>
				<td width=14%><label>电话区号</label></td>
				<td><input type=text name="districtTelcode" value="${district.districtTelcode}" class="digits" maxlength="10"/></td>
			</tr>
			<tr>
				<td width=14%><label>地区级别</label></td>
				<td>
					<c:import url="/platform/typeDictionaryMgr/getSmallTypeDicByBigTypeCode">
						<c:param name="selectName" value="districtLevel" />
						<c:param name="selectCode" value="${district.districtLevel}" />
						<c:param name="selectClass" value="required" />
						<c:param name="bigTypeCode" value="district" />
					</c:import>
				</td>
				<td width=14%><label>地区状态</label></td>
				<td>
					<select name="status" class="required">
						<option value="enable" <c:if test="${district.status eq 'enable'}">selected="selected"</c:if>>启用</option>
						<option value="disable" <c:if test="${district.status eq 'disable'}">selected="selected"</c:if>>停用</option>
					</select>
				</td>
			</tr>
			<tr>
				<td width=14%><label>上级地区</label></td>
				<td>
					<input type="hidden" name="upDistrictId" value="${district.upDistrictId}" />
					<input type="text" name="upDistrictName" value="${district.upDistrictName}" class="required" />
					<span class="btn btn-warning btn-xs select"><span class="glyphicon glyphicon-search"></span></span>
				</td>
				<td width=14%><label>备注</label></td>
				<td><input type="text" name="memo" value="${district.memo}" maxlength="50"/></td>
			</tr>
		</table>
	</form>
</div>