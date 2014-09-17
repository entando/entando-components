<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="webmail" uri="/webmail-core" %>

<webmail:addressComponents ctxName="fromAddress" fullAddressString="${getFromAddress}" roundTo="20" defaultValue="${labelEmpty}" />
<webmail:addressComponents ctxName="fromAddressFull" fullAddressString="${getFromAddress}" /> 
<c:choose>
<c:when test="${not empty fromAddress.fullname}"><abbr title="<c:out value="${fromAddressFull.fullname} &ndash; ${fromAddress.email}" />"><c:out value="${fromAddress.fullname}" /></abbr></c:when>
<c:otherwise><abbr title="<c:out value="${fromAddressFull.email}" />"><c:out value="${fromAddress.email}" /></abbr></c:otherwise>
</c:choose>