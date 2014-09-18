<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:currentPage param="code" var="currentViewCode" />

<p class="breadcrumb"><span class="noscreen"><wp:i18n key="ESNB_YOU_ARE_HERE" />:</span>
<c:set var="first" value="true" />
<wp:nav spec="current.path" var="currentTarget">
	<c:set var="currentCode"><c:out value="${currentTarget.code}" /></c:set>
	<c:if test="${first != 'true'}"> <span class="divider">/</span> </c:if>
	<c:choose>
		<c:when test="${!currentTarget.voidPage}">
			<c:choose>
				<c:when test="${currentCode == currentViewCode}">
					<span class="active"><c:out value="${currentTarget.title}" /></span>
				</c:when>
				<c:otherwise>
					<a href="<c:out value="${currentTarget.url}" />"><c:out value="${currentTarget.title}" /></a>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:out value="${currentTarget.title}" />
		</c:otherwise>
	</c:choose>
	<c:set var="first" value="false" />
</wp:nav>
</p>
