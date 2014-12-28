<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="field" type="java.lang.String" required="false"%>
<%@ attribute name="disabled" type="java.lang.String" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${name eq 'status'}">
		<c:choose>
			<c:when test="${value eq 'enable'}">
				<c:out value="启用"></c:out>
			</c:when>
			<c:when test="${value eq 'disable'}">
				<c:out value="停用"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<c:when test="${name eq 'systemType'}">
		<c:choose>
			<c:when test="${value eq 'platform'}">
				<c:out value="系统平台"></c:out>
			</c:when>
			<c:when test="${value eq 'uum'}">
				<c:out value="统一用户"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<c:when test="${name eq 'modPageType'}">
		<c:choose>
			<c:when test="${value eq 'js'}">
				<c:out value="js"></c:out>
			</c:when>
			<c:when test="${value eq 'jsp'}">
				<c:out value="jsp"></c:out>
			</c:when>
			<c:when test="${value eq 'html'}">
				<c:out value="html"></c:out>
			</c:when>
			<c:when test="${value eq 'flex'}">
				<c:out value="flex"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<c:when test="${name eq 'modLevel'}">
		<c:choose>
			<c:when test="${value eq '1'}">
				<c:out value="第一"></c:out>
			</c:when>
			<c:when test="${value eq '2'}">
				<c:out value="第二"></c:out>
			</c:when>
			<c:when test="${value eq '3'}">
				<c:out value="第三"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<%-- 页面功能区 --%>
	<c:when test="${name eq 'funArea'}">
		<c:choose>
			<c:when test="${value eq 'header'}">
				<c:out value="页面头部"></c:out>
			</c:when>
			<c:when test="${value eq 'navigate'}">
				<c:out value="页面导航菜单"></c:out>
			</c:when>
			<c:when test="${value eq 'outlookBar'}">
				<c:out value="页面左侧菜单"></c:out>
			</c:when>
			<c:when test="${value eq 'mainPage'}">
				<c:out value="主页面"></c:out>
			</c:when>
			<c:when test="${value eq 'stateBar'}">
				<c:out value="页面状态栏"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<%-- 页面样式 --%>
	<c:when test="${name eq 'pageStyle'}">
		<c:choose>
			<c:when test="${value eq 'general'}">
				<c:out value="general"></c:out>
			</c:when>
			<c:when test="${value eq 'winxp'}">
				<c:out value="winxp"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<c:when test="${name eq 'number'}">
		<c:choose>
			<c:when test="${value eq '1'}">
				<c:out value="1"></c:out>
			</c:when>
			<c:when test="${value eq '2'}">
				<c:out value="2"></c:out>
			</c:when>
			<c:when test="${value eq '3'}">
				<c:out value="3"></c:out>
			</c:when>
			<c:when test="${value eq '4'}">
				<c:out value="4"></c:out>
			</c:when>
			<c:when test="${value eq '5'}">
				<c:out value="5"></c:out>
			</c:when>
			<c:when test="${value eq '6'}">
				<c:out value="6"></c:out>
			</c:when>
			<c:when test="${value eq '7'}">
				<c:out value="7"></c:out>
			</c:when>
			<c:when test="${value eq '8'}">
				<c:out value="8"></c:out>
			</c:when>
			<c:when test="${value eq '9'}">
				<c:out value="9"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<%-- 性别 --%>
	<c:when test="${name eq 'gender'}">
		<c:choose>
			<c:when test="${value eq '0'}">
				<c:out value="男"></c:out>
			</c:when>
			<c:when test="${value eq '1'}">
				<c:out value="女"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<%-- 日志 --%>
	<c:when test="${name eq 'logSaveTime'}">
		<c:choose>
			<c:when test="${value eq '-1'}">
				<c:out value="不设置"></c:out>
			</c:when>
			<c:when test="${value eq '3'}">
				<c:out value="3天"></c:out>
			</c:when>
			<c:when test="${value eq '7'}">
				<c:out value="7天"></c:out>
			</c:when>
			<c:when test="${value eq '15'}">
				<c:out value="15天"></c:out>
			</c:when>
			<c:when test="${value eq '30'}">
				<c:out value="30天"></c:out>
			</c:when>
			<c:when test="${value eq '60'}">
				<c:out value="60天"></c:out>
			</c:when>
			<c:when test="${value eq '90'}">
				<c:out value="90天"></c:out>
			</c:when>
			<c:when test="${value eq '180'}">
				<c:out value="180天"></c:out>
			</c:when>
			<c:when test="${value eq '360'}">
				<c:out value="360天"></c:out>
			</c:when>
		</c:choose>
	</c:when>
	
	<%-- 编码段数 --%>
	<c:when test="${name eq 'partNum'}">
		<select name="partNum" class="required">
			<option value="1" <c:if test="${value eq '1'}">selected="selected"</c:if>>1</option>
			<option value="2" <c:if test="${value eq '2'}">selected="selected"</c:if>>2</option>
			<option value="3" <c:if test="${value eq '3'}">selected="selected"</c:if>>3</option>
			<option value="4" <c:if test="${value eq '4'}">selected="selected"</c:if>>4</option>
		</select>
	</c:when>
	
	<%--  分隔符 --%>
	<c:when test="${name eq 'delimite'}">
		<select name="delimite">
			<option value="-" <c:if test="${value eq '-'}">selected="selected"</c:if>>-</option>
			<option value="_" <c:if test="${value eq '_'}">selected="selected"</c:if>>_</option>
			<option value=":" <c:if test="${value eq ':'}">selected="selected"</c:if>>:</option>
			<option value="/" <c:if test="${value eq '/'}">selected="selected"</c:if>>/</option>
			<option value="|" <c:if test="${value eq '|'}">selected="selected"</c:if>>|</option>
		</select>
	</c:when>
	
	<%--  编码格式 --%>
	<c:when test="${name eq 'codeFormat'}">
		<select name="${field}" <c:if test="${disabled eq 'disabled'}">disabled</c:if>>
			<option value="char" <c:if test="${value eq 'char'}">selected="selected"</c:if>>字符</option>
			<option value="date" <c:if test="${value eq 'date'}">selected="selected"</c:if>>日期</option>
			<option value="number" <c:if test="${value eq 'number'}">selected="selected"</c:if>>序号</option>
			<option value="sysPara" <c:if test="${value eq 'sysPara'}">selected="selected"</c:if>>系统参数</option>
		</select>
	</c:when>
	
	<%--  编码-日期格式 --%>
	<c:when test="${name eq 'dateFormat'}">
		<select name="${field}" <c:if test="${disabled eq 'disabled'}">disabled</c:if>>
			<option value="yyyy" <c:if test="${value eq 'yyyy'}">selected="selected"</c:if>>年(yyyy)</option>
			<option value="yyyyMM" <c:if test="${value eq 'yyyyMM'}">selected="selected"</c:if>>年月(yyyyMM)</option>
			<option value="yyyyMMdd" <c:if test="${value eq 'yyyyMMdd'}">selected="selected"</c:if>>年月日(yyyyMMdd)</option>
		</select>
	</c:when>
	
	<%--  编码-序号格式 --%>
	<c:when test="${name eq 'numberFormat'}">
		<select name="${field}" <c:if test="${disabled eq 'disabled'}">disabled</c:if>>
			<option value="01" <c:if test="${value eq '01'}">selected="selected"</c:if>>两位(01)</option>
			<option value="001" <c:if test="${value eq '001'}">selected="selected"</c:if>>三位(001)</option>
			<option value="0001" <c:if test="${value eq '0001'}">selected="selected"</c:if>>四位(0001)</option>
			<option value="00001" <c:if test="${value eq '00001'}">selected="selected"</c:if>>五位(00001)</option>
			<option value="000001" <c:if test="${value eq '000001'}">selected="selected"</c:if>>六位(000001)</option>
		</select>
	</c:when>
	
	<%--  编码-系统参数格式 --%>
	<c:when test="${name eq 'sysParaFormat'}">
		<select name="${field}" <c:if test="${disabled eq 'disabled'}">disabled</c:if>>
			<option value="userName" <c:if test="${value eq 'userName'}">selected="selected"</c:if>>用户名</option>
			<option value="roleUser" <c:if test="${value eq 'roleUser'}">selected="selected"</c:if>>角色+用户名</option>
			<option value="organRoleUser" <c:if test="${value eq 'organRoleUser'}">selected="selected"</c:if>>机构+角色+用户名</option>
		</select>
	</c:when>
</c:choose>