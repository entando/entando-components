<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpta" uri="/jptokenapi-core" %>

<c:if test="${sessionScope.currentUser != 'guest'}">
	<wpta:myToken var="myTokenVar" />
	<wp:i18n key="jptokenapi_USERNAME" />, <em><c:out value="${sessionScope.currentUser}"/></em><br />
	<wp:i18n key="jptokenapi_TOKEN" />, 
	<c:choose>
		<c:when test="${null != myTokenVar}"><em><c:out value="${myTokenVar}"/></em></c:when>
		<c:otherwise><wp:i18n key="jptokenapi_NULL_TOKEN" /></c:otherwise>
	</c:choose>
</c:if>