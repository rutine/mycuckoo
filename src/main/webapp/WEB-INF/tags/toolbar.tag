<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="value" type="java.lang.Object" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	
	<%-- 模块操作按钮 --%>
	<c:when test="${name eq 'moduleOpt'}">
		<div class="btn-toolbar">
			<div class="btn-group">
				<c:forEach var="modOpt" items="${value}">
					<a class="btn btn-primary btn-sm" href="${modOpt.optFunLink}">
						<span class="glyphicon glyphicon-${modOpt.modImgCls}"></span>${modOpt.modName}
					</a>
				</c:forEach>
			</div>
		</div>
	</c:when>
	
	<%-- 表单操作按钮 --%>
	<c:when test="${name eq 'formOpt'}">
		<div class="btn-toolbar">
			<div class="btn-group">
				<c:choose>
					<c:when test="${value eq 'create' }">
					<a href="save" class="btn btn-primary btn-sm" data-loading-text="执行中...">保存
						<span class="glyphicon glyphicon-hdd"></span>
					</a>
					<a href="saveadd" class="btn btn-primary btn-sm" data-loading-text="执行中...">保存增加
						<span class="glyphicon glyphicon-plus-sign"></span>
					</a>
					<a href="reback" class="btn btn-primary btn-sm" data-loading-text="执行中...">返回
						<span class="glyphicon glyphicon-circle-arrow-left"></span>
					</a>
					</c:when>
					<c:when test="${value eq 'update' }">
					<a href="save" class="btn btn-primary btn-sm" data-loading-text="执行中...">保存
						<span class="glyphicon glyphicon-hdd"></span>
					</a>
					<a href="reback" class="btn btn-primary btn-sm" data-loading-text="执行中...">返回
						<span class="glyphicon glyphicon-circle-arrow-left"></span>
					</a>
					</c:when>
					<c:when test="${value eq 'view' }">
					<a href="reback" class="btn btn-primary btn-sm" data-loading-text="执行中...">返回
						<span class="glyphicon glyphicon-circle-arrow-left"></span>
					</a>
					</c:when>
					<c:otherwise>
					<a href="reback" class="btn btn-primary btn-sm" data-loading-text="执行中...">返回
						<span class="glyphicon glyphicon-circle-arrow-left"></span>
					</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</c:when>
</c:choose>