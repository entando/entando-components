<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="liClass" value="" />
<c:set var="homeIcon" value="" />
<c:set var="caret" value="" />
<c:set var="ulClass" value=' class="dropdown-menu"' />
<c:set var="aClassAndData" value="" />
<c:set var="aURL" value="${previousPage.url}" />

<c:if test="${previousPage.voidPage}">
	<c:set var="aURL" value='#' />
</c:if>

<c:if test="${fn:containsIgnoreCase(previousPage.code, 'homepage')}">
	<c:set var="homeIcon"><i class="icon-home"></i>&#32;</c:set>
</c:if>

<c:if test="${previousPage.code == currentPageCode}">
	<c:set var="liClass" value=' class="active"' />
</c:if>

<c:if test="${previousLevel < level}">
	<c:set var="liClass" value=' class="dropdown"' />
	<c:if test="${previousPage.code == currentPageCode}">
		<c:set var="liClass" value=' class="dropdown active"' />
	</c:if>
	<c:if test="${previousPage.voidPage}">
		<c:set var="liClass" value=' class=" dropdown"' />
	</c:if>

	<c:if test="${previousLevel > 0}">
		<c:set var="liClass" value=' class="dropdown-submenu"' />
		<c:if test="${previousPage.code == currentPageCode}">
			<c:set var="liClass" value=' class="dropdown-submenu active"' />
		</c:if>

		<c:set var="ulClass" value=' class="dropdown-menu"' />
	</c:if>

	<c:set var="aClassAndData" value=' class="dropdown-toggle" data-toggle="dropdown"' />
	<c:if test="${previousLevel == 0}">
		<c:set var="caret"> <span class="caret"></span></c:set>
	</c:if>
</c:if>

	<li<c:out value="${liClass}" escapeXml="false" />><a href="<c:out value="${aURL}" escapeXml="false" />"<c:out value="${aClassAndData}" escapeXml="false" />><!-- [ <c:out value="${previousLevel}" /> ] --><c:out value="${homeIcon}" escapeXml="false" /><c:out value="${previousPage.title}" /><c:out value="${caret}" escapeXml="false" /></a>

	<c:if test="${previousLevel == level}"></li></c:if>
	<c:if test="${previousLevel < level}"><ul<c:out value="${ulClass}" escapeXml="false" />></c:if>
	<c:if test="${previousLevel > level}">
		<c:forEach begin="${1}" end="${previousLevel - level}"></li></ul></c:forEach></li>
	</c:if>