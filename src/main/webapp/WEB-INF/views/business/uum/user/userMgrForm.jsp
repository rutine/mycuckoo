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
		<input type="hidden" name="userId" value="${user.userId}"/>
		<table class="table table-bordered">
			<tr>
				<td width=14%><label>用户号</label></td>
				<td><input type=text  name="userCode" value="${user.userCode}" class="required alphanumeric" maxlength="10"/></td>
				<td width=14% rowspan="3"><label>用户照片</label></td>
				<td rowspan="3">
					<c:if test="${empty user.userPhotoUrl}">
						<c:set var="userPhotoUrl" value="${ctx}/static/images/form/blank_userphoto.gif"></c:set>
					</c:if>
					<c:if test="${not empty user.userPhotoUrl}">
						<c:set var="userPhotoUrl" value="${ctx}${user.userPhotoUrl }"></c:set>
					</c:if>
					<img src="${userPhotoUrl}" class="img-circle" />
					<span class="btn btn-success btn-sm">
						<span class="glyphicon glyphicon-plus"></span>
						<span>照片</span>
						<input type="file" name="userPhoto" >
					</span>
				</td>
			</tr>
			<tr>
				<td width=14%><label>用户名</label></td>
				<td><input type=text name="userName" value="${user.userName}" maxlength="60"/></td>
			</tr>
			<tr>
				<td width=14%><label>密码</label></td>
				<td><input type=password name="userPassword" value="${user.userPassword}" class="alphanumeric" maxlength="20"/></td>
			</tr>
			<tr>
				<td width=14%><label>性别</label></td>
				<td>
					<input type="radio" name="userGender" value="0" <c:if test="${0 eq user.userGender}">checked</c:if>> 男
					<input type="radio" name="userGender" value="1" <c:if test="${1 eq user.userGender}">checked</c:if>> 女
				</td>
				<td width=14%><label>职位</label></td>
				<td><input type=text name="userPosition" value="${user.userPosition}" maxlength="20"/></td>
			</tr>
			<tr>
				<td width=14%><label>用户QQ</label></td>
				<td><input type=text name="userQq" value="${user.userQq}" class="digits" maxlength="20"/></td>
				<td width=14%><label>用户MSN</label></td>
				<td><input type=text name="userMsn" value="${user.userMsn}" class="email" maxlength="30"/></td>
			</tr>
			<tr>
				<td width=14%><label>用户手机</label></td>
				<td><input type=text name="userMobile" value="${user.userMobile}" class="digits" maxlength="20"/></td>
				<td width=14%><label>用户手机2</label></td>
				<td><input type=text name=userMobile2 value="${user.userMobile2}" class="digits" maxlength="20"/></td>
			</tr>
			<tr>
				<td width=14%><label>办公电话</label></td>
				<td><input type=text name="userOfficeTel" value="${user.userOfficeTel}" class="digits" maxlength="20"/></td>
				<td width=14%><label>家庭电话</label></td>
				<td><input type=text name=userFamilyTel value="${user.userFamilyTel}" class="digits" maxlength="20"/></td>
			</tr>
			<tr>
				<td width=14%><label>家庭住址</label></td>
				<td><input type=text name="userAddress" value="${user.userAddress}"  maxlength="100"/></td>
				<td width=14%><label>用户邮件</label></td>
				<td><input type=text name=userEmail value="${user.userEmail}" class="email" maxlength="30"/></td>
			</tr>
			<tr>
				<td width=14%><label>用户有效期</label></td>
				<td>
					<div id="user_avidate" class="input-group date">
						<input type="text" class="form-control required" name="userAvidate" 
							value="<fmt:formatDate value="${user.userAvidate}" pattern="yyyy-MM-dd" />" readOnly />
						<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span> 
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</td>
				<td width=14%><label>所属角色</label></td>
				<td>
					<input type="hidden" name="belongOrganId" value="${user.belongOrganId}" /> <!-- 机构ID -->
					<input type="hidden" name="uumOrgRoleId" value="${user.uumOrgRoleId}" /> <!-- 机构角色ID -->
					<input type="text" name="uumRoleName" value="${user.uumRoleName}" class="required" />
					<span class="btn btn-warning btn-sm select"><span class="glyphicon glyphicon-search"></span></span>
				</td>
			</tr>
			<tr>
				<td width=14%><label>备注</label></td>
				<td><input type="text" name="memo" value="${user.memo}" maxlength="50"/></td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</form>
</div>