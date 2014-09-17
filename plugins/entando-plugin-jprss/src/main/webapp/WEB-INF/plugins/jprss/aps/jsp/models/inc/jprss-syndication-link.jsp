<%--
	Title:
		Syndication Includer for Page Models (jprss-syndication-link.jsp)
	
	Description:
		This is a snippet useful when you want to publish all the syndication
		channels in inside the <head> tag of your pages.
		The result will be something like:
		<head>
			<!-- if RSS type --> 
			<link rel="alternate" type="application/rss+xml" title="Channel One" href="...." />
			<!-- if Atom type --> 
			<link rel="alternate" type="application/atom+xml" title="Channel Two" href="...." />
		</head>
		
	Author:
		Andrea Dessì <a.dessi@agiletec.it>
		
	Date:
		16/mag/2011 16.59.37
	
	Usage:
		Include this JSP inside the <head> html tag of your Page Models
		usign the <jsp:include> tag.
		(example <jsp:include page="/WEB-INF/plugins/jprss/aps/jsp/models/inc/jprss-syndication-link.jsp" />)
--%>
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