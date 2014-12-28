<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
	<form class="form-horizontal">
		<div class="form-group">
			<label class="control-label col-sm-4 col-md-2">角色名称:</label>
			<div class="col-sm-4 col-md-2"><p class="form-control-static">${param.roleName}</p></div>
		</div>
	</form>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="#role_assign_mod_opt" role="tab" data-toggle="tab">模块操作权限分配</a></li>
		<li><a href="#role_assign_row_priv" role="tab" data-toggle="tab">行权限分配</a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane fade in active" id="role_assign_mod_opt" role="tabpanel">
			<strong>权限范围</strong>
			<form class="form-inline">
				<label><input type="radio" name="priScope" value="inc" <c:if test="${'inc' eq privilegeScope}">checked</c:if>> 包含操作</label>
				<label><input type="radio" name="priScope" value="exc" <c:if test="${'exc' eq privilegeScope}">checked</c:if>> 不包含操作</label>
				<label><input type="radio" name="priScope" value="all" <c:if test="${'all' eq privilegeScope}">checked</c:if>> 全部(无需分配操作)</label>
			</form>
			<table>
				<tr>
					<td>
						<div class="select-side">
							<span>未选模块</span>
							<div style="width: 200px; height:200px; border: 1px solid #0088CC; overflow-y: auto;">
								<ul id="left_tree" class="ztree" data='${unassignedModList}'></ul>
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
							<span>已选模块</span>
							<div style="width: 200px; height:200px; border: 1px solid #0088CC; overflow-y: auto;">
								<ul id="right_tree" class="ztree" data='${assignedModList}'></ul>
							</div>
						</div>
					</td>
				</tr>
			</table>
			<div class="panel-footer"><button type="button" class="btn btn-primary btn-sm">保存</button></div>
		</div>
		<div class="tab-pane fade" id="role_assign_row_priv" role="tabpanel">
			<form class="form-inline">
				<label><input type="radio" name="rolPri" value="org" 
						<c:if test="${'org' eq rowPrivilege or ('rol' ne rowPrivilege and 'urs' ne rowPrivilege)}">checked</c:if>> 机构</label>
				<label><input type="radio" name="rolPri" value="rol" <c:if test="${'rol' eq rowPrivilege}">checked</c:if>> 角色</label>
				<label><input type="radio" name="rolPri" value="urs"  <c:if test="${'urs' eq rowPrivilege}">checked</c:if>> 用户</label>
			</form>
			<div class="panel-footer"><button type="button" class="btn btn-primary btn-sm">保存</button></div>
		</div>
	</div>
</div>