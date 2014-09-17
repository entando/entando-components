<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
	A 8-year-long effort, lovely brought to you by:

	- Marco Diana <m.diana@entando.com>
	- Eugenio Santoboni <e.santoboni@entando.com>
	- William Ghelfi <w.ghelfi@@entando.com>
	- Andrea Dessì <a.dessi@agiletec.it>
--%>

<wp:currentPage param="code" var="currentPageCode" />
<c:set var="currentPageCode" value="${currentPageCode}" />

<div class="well well-small">

<ul class="unstyled">
<wp:nav var="page">

<c:if test="${previousPage.code != null}">
	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${page.level}" />
	<%@ include file="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/widgets/jpfrontshortcut_navigation_menu_include.jsp" %>
</c:if>

	<c:set var="previousPage" value="${page}" />
</wp:nav>

	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${0}"  scope="request" /> <%-- we are out, level is 0 --%>
	<%@ include file="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/widgets/jpfrontshortcut_navigation_menu_include.jsp" %>
	<c:if test="${previousLevel != 0}">
		<c:forEach begin="${0}" end="${previousLevel -1}"></ul></li></c:forEach>
	</c:if>

</ul>

</div>