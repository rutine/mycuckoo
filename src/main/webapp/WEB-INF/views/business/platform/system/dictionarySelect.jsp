<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<select name="${param.selectName}" class="${param.selectClass}">
	<c:forEach var="smallDictionary" items="${sysplDicSmallTypeList}">
		<option value="${smallDictionary.smallTypeCode}" 
			<c:if test="${smallDictionary.smallTypeCode eq param.selectCode}">selected="selected"</c:if>>${smallDictionary.smallTypeName}</option>
	</c:forEach>
</select>