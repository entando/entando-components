<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:currentPage param="code" var="currentPageCode" />
<c:set var="currentPageCode" value="${currentPageCode}" />
<c:set var="previousPage" value="${null}" />

<div class="well well-small">

<ul class="nav nav-list">
<wp:nav var="page">
<c:if test="${previousPage.code != null}">
	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${page.level}" />
	<%@ include file="entando-widget-navigation_menu_include.jsp" %>
</c:if>

	<c:set var="previousPage" value="${page}" />
</wp:nav>
<c:if test="${previousPage != null}">
	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${0}"  scope="request" /> <%-- we are out, level is 0 --%>
	<%@ include file="entando-widget-navigation_menu_include.jsp" %>
	<c:if test="${previousLevel != 0}">
		<c:forEach begin="${0}" end="${previousLevel -1}"></ul></li></c:forEach>
	</c:if>
</c:if>
</ul>

</div>