<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<div>
	<!-- 表单操作按钮 -->
	<tags:toolbar name="formOpt" value="${action}"></tags:toolbar>
	<form class="form-inline" name="editForm" action="">
		<input type="hidden" name="orgId" value="${organ.orgId}"/>
		<table class="table table-bordered">
			<tr>
				<td width=14%><label>机构简称</label></td>
				<td><input type=text  name="orgSimpleName" value="${organ.orgSimpleName}" class="required" maxlength="10"/></td>
				<td width=14%><label>机构全称</label></td>
				<td><input type=text name="orgFullName" value="${organ.orgFullName}" maxlength="60"/></td>
			</tr>
			<tr>
				<td width=14%><label>机构代码</label></td>
				<td><input type=text name="orgCode" value="${organ.orgCode}" class="alphanumeric" maxlength="10"/></td>
				<td width=14%><label>所属地区</label></td>
				<td>
					<input type="hidden" name="orgBelongDist" value="${organ.orgBelongDist}" />
					<input type="text" name="orgBelongDistName" value="${organ.orgBelongDistName}" class="required" />
					<span class="btn btn-warning btn-xs select"><span class="glyphicon glyphicon-search"></span></span>
				</td>
			</tr>
			<tr>
				<td width=14%><label>联系地址1</label></td>
				<td><input type=text name="orgAddress1" value="${organ.orgAddress1}" maxlength="100"/></td>
				<td width=14%><label>联系地址2</label></td>
				<td><input type=text name="orgAddress2" value="${organ.orgAddress2}" maxlength="100"/></td>
			</tr>
			<tr>
				<td width=14%><label>联系电话1</label></td>
				<td><input type=text name="orgTel1" value="${organ.orgTel1}" class="digits" maxlength="15"/></td>
				<td width=14%><label>联系电话2</label></td>
				<td><input type=text name="orgTel2" value="${organ.orgTel2}" class="digits" maxlength="15"/></td>
			</tr>
			<tr>
				<td width=14%><label>成立日期</label></td>
				<td>
					<div id="org_begin_date" class="input-group date">
						<input type="text" class="form-control required" name="orgBeginDate" 
							value="<fmt:formatDate value="${organ.orgBeginDate}" pattern="yyyy-MM-dd" />" readOnly />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</td>
				<td width=14%><label>机构类型</label></td>
				<td>
					<c:import url="/platform/typeDictionaryMgr/getSmallTypeDicByBigTypeCode">
						<c:param name="selectName" value="orgType" />
						<c:param name="selectCode" value="${organ.orgType}" />
						<c:param name="selectClass" value="required" />
						<c:param name="bigTypeCode" value="organType" />
					</c:import>
			</td>
			</tr>
			<tr>
				<td width=14%><label>机构邮编</label></td>
				<td><input type=text name="orgPostal" value="${organ.orgPostal}" class="digits" maxlength="6"/></td>
				<td width=14%><label>法人代表</label></td>
				<td><input type=text name="orgLegal" value="${organ.orgLegal}" maxlength="10"/></td>
			</tr>
			<tr>
				<td width=14%><label>税务号</label></td>
				<td><input type=text name="orgTaxNo" value="${organ.orgTaxNo}" class="alphanumeric" maxlength="25"/></td>
				<td width=14%><label>注册登记号</label></td>
				<td><input type=text name="orgRegNo" value="${organ.orgRegNo}" class="alphanumeric" maxlength="25"/></td>
			</tr>
			<tr>
				<td width=14%><label>上级机构</label></td>
				<td>
					<input type="hidden" name="upOrgId" value="${organ.upOrgId}" />
					<input type="text" name="upOrgName" value="${organ.upOrgName}" class="required" />
					<span class="btn btn-warning btn-xs select2"><span class="glyphicon glyphicon-search"></span></span>
				</td>
				<td width=14%><label>备注</label></td>
				<td><input type="text" name="memo" value="${organ.memo}" maxlength="50"/></td>
			</tr>
		</table>
	</form>
</div>