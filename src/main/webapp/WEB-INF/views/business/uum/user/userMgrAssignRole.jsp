<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div>
	<form class="form-horizontal">
		<input type="hidden" name="defaultRoleId"  value="${param.defaultRoleId}" />
		<div class="form-group">
			<label class="control-label col-md-2">用户名称:</label>
			<div class="col-md-4"><p class="form-control-static">${param.userName}</p></div>
			<label class="control-label col-md-2">默认角色:</label>
			<div class="col-md-4"><p class="form-control-static defaultRoleName">${param.defaultRoleName}</p></div>
		</div>
	</form>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#usr-assign-mod-opt">用户分配角色</a></li>
	</ul>

	<div class="tab-content">
		<div class="tab-pane active" id="usr-assign-mod-opt">
			<table>
				<tr>
					<td>
						<div class="select-side">
							<span>未选角色</span>
							<div style="width: 200px; height:200px; border: 1px solid #0088CC; overflow-y: auto;">
								<ul id="left_role_tree" class="ztree" data='${orgRoleList}'></ul>
							</div>
						</div>
					</td>
					<td>
						<div class="select-opt">
							<p class="to-right" title="添加">&lt;&lt;</p>
							<p class="to-left"  title="移除">&gt;&gt;</p>
						</div>
					</td>
					<td>
						<div class="select-side">
							<span>已选角色</span>
							<select id="right_role_multi_select" name="roles" multiple="multiple">
								<c:forEach var="role" items="${roleUserRefList}">
									<option value="${role.orgRoleId}">${role.organName}-${role.roleName}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
			</table>
			<div class="panel-footer">
				<button type="button" class="btn btn-default btn-sm">默认角色</button>
				<button type="button" class="btn btn-primary btn-sm">保存</button>
			</div>
		</div>
	</div>
</div>