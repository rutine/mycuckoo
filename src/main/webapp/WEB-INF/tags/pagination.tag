<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="page" type="com.mycuckoo.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
int current =  page.getNumber() + 1;
int begin = Math.max(1, current - paginationSize/2);
int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

request.setAttribute("current", current);
request.setAttribute("begin", begin);
request.setAttribute("end", end);
%>

<div class="text-center">
	<ul class="pagination pagination-sm">
		<% if (page.hasPreviousPage()) { %>
				<li><a href="?pageNo=1&${searchParams}">«</a></li>
				<li><a href="?pageNo=${current - 1}&${searchParams}">‹</a></li>
		<% } else { %>
				<li class="disabled"><a href="#">«</a></li>
				<li class="disabled"><a href="#">‹</a></li>
		<% } %>
		
		<c:forEach var="i" begin="${begin}" end="${end}">
			<c:choose>
				<c:when test="${i == current}">
					<li class="active"><a href="?pageNo=${i}&${searchParams}">${i}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="?pageNo=${i}&${searchParams}">${i}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		
		<% if (page.hasNextPage()) { %>
				<li><a href="?pageNo=${current+1}&${searchParams}">›</a></li>
			<li><a href="?pageNo=${page.totalPages}&${searchParams}">»</a></li>
		<% } else { %>
			<li class="disabled"><a href="#">›</a></li>
			<li class="disabled"><a href="#">»</a></li>
		<% } %>
	</ul>
</div>

