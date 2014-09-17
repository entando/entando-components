<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<c:if test="${group.size > group.max}">
<p>
	<c:choose>
	<c:when test="${'1' == group.currItem}">&laquo;&#32;<wp:i18n key="PREV" /></c:when>
	<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${group.paramItemName}" ><c:out value="${group.prevItem}"/></wp:parameter></wp:url>">&laquo;&#32;<wp:i18n key="PREV" /></a></c:otherwise>					
	</c:choose>
	<c:forEach var="item" items="${group.items}">
		<c:choose>
		<c:when test="${item == group.currItem}">&#32;[<c:out value="${item}"/>]&#32;</c:when>
		<c:otherwise>&#32;<a href="<wp:url paramRepeat="true" ><wp:parameter name="${group.paramItemName}" ><c:out value="${item}"/></wp:parameter></wp:url>"><c:out value="${item}"/></a>&#32;</c:otherwise>
		</c:choose>
	</c:forEach>
	<c:choose>
	<c:when test="${group.maxItem == group.currItem}"><wp:i18n key="NEXT" />&#32;&raquo;</c:when>
	<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${group.paramItemName}" ><c:out value="${group.nextItem}"/></wp:parameter></wp:url>"><wp:i18n key="NEXT" />&#32;&raquo;</a></c:otherwise>					
	</c:choose>
</p>
</c:if>
		
	