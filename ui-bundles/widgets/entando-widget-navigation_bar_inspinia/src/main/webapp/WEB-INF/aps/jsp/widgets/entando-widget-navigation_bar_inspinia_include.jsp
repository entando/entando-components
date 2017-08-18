<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="liClass" value="" />
<c:if test="${previousPage.code == currentPageCode}">
    <c:set var="liClass" value=' class="active"' />
</c:if>
<c:if test="${previousPage.voidPage}">
    <c:set var="liClass" value=' class="nav-header"' />
</c:if>
<li<c:out value="${liClass}" escapeXml="false" />>
<c:if test="${!previousPage.voidPage}">
    <a href="<c:out value="${previousPage.url}" />">
</c:if>
<!-- [ <c:out value="${previousLevel}" /> ]-->
<c:out value="${homeIcon}" escapeXml="false" />
<!-- <i class="fa fa-th-large"></i> -->
<span class="nav-label">

<c:out value="${previousPage.title}" />

</span>
<c:if test="${!previousPage.voidPage}">
</a>
</c:if>
<c:if test="${previousLevel == level}">
</li>
</c:if>
<c:if test="${previousLevel < level}">
    <ul class="nav nav-list">
</c:if>
<c:if test="${previousLevel > level}">
    <c:forEach begin="${1}" end="${previousLevel - level}">
        </li>
        </ul>
    </c:forEach>
</li>
</c:if>
