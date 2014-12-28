<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
	<c:if test="${not empty param.error}">
		<div class="alert alert-danger">提示: ${param.error}</div>
	</c:if>
	<form class="form-horizontal" name="editForm" action="${action}Form" method="post">
		<div class="form-group">
			<label class="col-sm-4 col-md-2 control-label">授权代理人</label>
			<div class="col-sm-6 col-md-3">
				<input type="hidden" name="userId">
				<input type="text" name="userName" class="form-control" placeholder="授权代理人">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 col-md-2 control-label">开始时间</label>
			<div class="col-sm-6 col-md-3">
				<div id="begin_date" class="input-group date form_datetime">
					<input type="text" class="form-control" name="beginDate" readOnly />
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 col-md-2 control-label">结束时间</label>
			<div class="col-sm-6 col-md-3">
				<div id="end_date" class="input-group date form_datetime">
					<input type="text" class="form-control" name="endDate" readOnly />
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 col-md-2 control-label">备注说明</label>
			<div class="col-sm-6 col-md-3">
				<textarea class="form-control" rows="3" name="reason"></textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 col-md-2 control-label">代理范围</label>
			<div class="col-sm-6 col-md-3">
				<label class="radio-inline"><input type="radio" name="agentScope" value="all"> 代理全部操作</label>
				<label class="radio-inline"><input type="radio" name="agentScope" value="part"> 代理部分操作</label>
			</div>
		</div>
		<div class="form-group agent-privilege-setting fade">
			<label class="col-sm-4 col-md-2 control-label">代理权限设置</label>
			<div class="col-sm-6 col-md-3">
				<table>
						<tr>
							<td>
								<div class="select-side">
									<span>未选模块</span>
									<div style="width: 200px; height:200px; border: 1px solid #0088CC; overflow-y: auto;">
										<ul id="left_tree" class="ztree" data-tree='${userMenuList}'></ul>
									</div>
								</div>
							</td>
							<td>
								<div class="select-opt">
									<p class="to-right" title="添加">&lt;&lt;</p>
									<p class="to-left" title="移除">&gt;&gt;</p>
								</div>
							</td>
							<td>
								<div class="select-side">
									<span>已选模块</span>
									<div style="width: 200px; height:200px; border: 1px solid #0088CC; overflow-y: auto;">
										<ul id="right_tree" class="ztree"></ul>
									</div>
								</div>
							</td>
						</tr>
					</table>
			</div>
		</div>
	</form>
</div>