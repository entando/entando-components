<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>

<div class="jpcrowdsourcing jpcrowdsourcing_idea_tags">

	<h1><wp:i18n key="jpcollaboration_TAGS_TITLE" /></h1>
<jpcrwsrc:currentPageWidget param="config" configParam="instanceCode" widget="collaboration_ideaInstance" var="instanceVar"/>
	<%//FIXME : what is this tag?
		//<jpcrwsrc:ideaTagList var="categoryInfoList" onlyLeaf="false" onlyCrawdSourcingNode="true"/>
	%>
	<jpcrwsrc:ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>

	<jpcrwsrc:pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceVar}" listResult="false"/>

	<%-- <c:set var="tagSeparator">&#32;|&#32;</c:set> --%>
		<c:choose>
			<c:when test="${null != categoryInfoList && !empty categoryInfoList}">
			<ul class="nav nav-pills">
				<li<c:if test="${empty param.ideaTag}"> class="active"</c:if>>
						<a href="<wp:url  page="${listIdea_page.code}" />">
							<wp:i18n key="jpcollaboration_TAGS_ALL" />
						</a>
				</li>
				<c:forEach var="categoryInfo" items="${categoryInfoList}">
					<wp:url var="listIdea_pageUrl" page="${listIdea_page.code}"><wp:urlPar name="ideaTag" ><c:out value="${categoryInfo.category.code}" /></wp:urlPar></wp:url>
				<li<c:if test="${!empty param.ideaTag && ( param.ideaTag == categoryInfo.category.code )}"> class="active"</c:if>>
					<a href="<c:out value="${listIdea_pageUrl}" />">
						<c:out value="${categoryInfo.title}" />
					</a>
				</li>
				</c:forEach>
			</ul>
			</c:when>
			<c:otherwise>
				<p class="alert alert-warning"><wp:i18n key="jpcollaboration_TAGS_EMPTY" /></p>
			</c:otherwise>
		</c:choose>
</div>