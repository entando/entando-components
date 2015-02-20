<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js" />

<wp:ifauthorized permission="jpsharedocs_edit" var="isGrantedVar" >

<jacms:contentList listName="contentList" titleVar="titleVar"
	pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" userFilterOptionsVar="userFilterOptionsVar" />

<c:if test="${null != titleVar}">
	<h1><c:out value="${titleVar}" /></h1>
</c:if>

<c:set var="userFilterOptionsVar" value="${userFilterOptionsVar}" scope="request" />
<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module.jsp" />

<c:choose>
<c:when test="${contentList != null && !empty contentList}">
	<wp:pager listName="contentList" objectName="groupContent" pagerIdFromFrame="true" advanced="true" offset="5">
		<c:set var="group" value="${groupContent}" scope="request" />
		<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
		<c:forEach var="contentId" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
			<jacms:content contentId="${contentId}" />
		</c:forEach>
		<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
	</wp:pager>
</c:when>
<c:when test="${not empty userFilterOptionsVar}">
	<p class="alert alert-info"><wp:i18n key="LIST_VIEWER_EMPTY" /></p>
</c:when>
<c:otherwise>
	<wp:i18n key="jpsharedocs_LIST_NO_CONTENT_FOUND" />
</c:otherwise>
</c:choose>

<c:if test="${null != pageLinkVar && null != pageLinkDescriptionVar}">
	<p class="text-right"><a class="btn btn-primary" href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
</c:if>

</wp:ifauthorized>

<%-- Important: reset variables --%>
<c:set var="userFilterOptionsVar" value="${null}" scope="request" />
<c:set var="contentList" value="${null}" scope="request" />
<c:set var="group" value="${null}" scope="request" />

<c:if test="${isGrantedVar}">
	
<wp:pageWithWidget var="sharedocsEditPage" widgetTypeCode="jpsharedocs_edit" />
	<wp:pageWithWidget var="sharedocsFullListPage" widgetTypeCode="jpsharedocs_list" />
	
	<p class="edit_comment">
		<a href="<wp:url page="${sharedocsEditPage.code}" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_NEW" /></a>
	</p>
	
	<wp:currentWidget param="code" var="widgetCodeVar"/>
	
	<c:if test="${widgetCodeVar ne 'jpsharedocs_list' }">
		<p class="edit_comment">
			<a href="<wp:url page="${sharedocsFullListPage.code}" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_LIST_FULL" /></a>
		</p>
	</c:if>
</c:if>
<c:if test="${!isGrantedVar}">\
	<p class="alert alert-info"><wp:i18n key="jpsharedocs_INSUFFICENT_PERMISSION" /></p>
</c:if>