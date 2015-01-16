<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cal" uri="/jpcalendar-aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:info key="currentLang" var="currentLang" />
<fmt:setLocale value="${currentLang}" />
<h1>
	<wp:i18n key="jpcalendar_EVENTS_TITLE" /><c:if test="${!(empty param.selectedDate)}">: <fmt:parseDate value="${param.selectedDate}" pattern="yyyyMMdd" var="currentDate"/><fmt:formatDate value="${currentDate}" dateStyle="full" />
	</c:if>
</h1>
<cal:eventsOfDay listName="contentList" />
<c:if test="${!(empty contentList)}">
	<wp:pager listName="contentList" objectName="groupContent" pagerIdFromFrame="true" max="10" >
		<c:set var="group" value="${groupContent}" scope="request" />
		<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
		<c:forEach var="content" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
			<jacms:content contentId="${content}" modelId="list"/>
		</c:forEach>
		<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
	</wp:pager>
</c:if>
<%-- Important: reset variables --%>
<c:set var="contentList" value="${null}"  scope="request" />
<c:set var="group" value="${null}"  scope="request" />