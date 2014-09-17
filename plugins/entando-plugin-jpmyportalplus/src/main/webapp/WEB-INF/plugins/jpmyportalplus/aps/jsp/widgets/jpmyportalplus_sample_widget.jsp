<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="currentLocale"><wp:info key="currentLang" var="lowerLang" /><c:out value="${lowerLang}" />_<c:out value="${fn:toUpperCase(lowerLang)}" /></c:set>
<fmt:setLocale value="${currentLocale}" />
<jsp:useBean id="now" class="java.util.Date" />
<c:set var="today"><fmt:formatDate value="${now}" pattern="EEEE d MMMM yyyy, HH:mm" /></c:set>
<p>
  Current date and time: <br /><strong><c:out value="${today}" /></strong>
</p>