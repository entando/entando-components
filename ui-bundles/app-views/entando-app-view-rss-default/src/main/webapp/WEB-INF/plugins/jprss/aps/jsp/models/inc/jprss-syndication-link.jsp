<%@ taglib prefix="jprss" uri="/jprss-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- retrieve the list --%>
<jprss:rssList listName="rssList" />
<%-- retrieve the current lang --%>
<wp:info key="currentLang" var="currentLang" />
<%-- if the list is not empty --%>
<c:if test="${!empty rssList}">
    <%-- for each channel --%>
    <c:forEach var="channel" items="${rssList}">
        <c:choose>
            <%-- when the feed type starts with "rss" then is RSS --%>
            <c:when test="${fn:startsWith(channel.feedType, 'rss')}">
                <link rel="alternate" type="application/rss+xml" title="<c:out value="${channel.title}"/>" href="<wp:info key="systemParam" paramName="applicationBaseURL"/>do/jprss/Rss/Feed/show.action?id=<c:out value="${channel.id}"/>&amp;lang=<c:out value="${currentLang}"/>" />
            </c:when>
            <%-- when the feed type starts with "atom" then is ATOM --%>
            <c:when test="${fn:startsWith(channel.feedType, 'atom')}">
                <link rel="alternate" type="application/atom+xml" title="<c:out value="${channel.title}"/>" href="<wp:info key="systemParam" paramName="applicationBaseURL"/>do/jprss/Rss/Feed/show.action?id=<c:out value="${channel.id}"/>&amp;lang=<c:out value="${currentLang}"/>" />
            </c:when>
        </c:choose>
    </c:forEach>
</c:if>
