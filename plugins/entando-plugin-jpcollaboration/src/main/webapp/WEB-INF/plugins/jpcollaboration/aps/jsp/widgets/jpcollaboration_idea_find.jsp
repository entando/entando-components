<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<div class="jpcrowdsourcing jpcrowdsourcing_idea_find">
	<h1><wp:i18n key="jpcollaboration_TITLE_SEARCH" /></h1>
<jpcrwsrc:currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
<c:choose>
	<c:when test="${null != instanceVar}">
		<jpcrwsrc:pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceVar}" listResult="false"/>
	</c:when>
	<c:otherwise>
		<jpcrwsrc:pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_search_result" listResult="false"/>
	</c:otherwise>
</c:choose>
<jpcrwsrc:ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>

	<form action="<wp:url page="${listIdea_page.code}" />" method="get">
	<input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value="<c:out value="${instanceVar}" />" />
		<p>
			<label for="jpcrowdsourcing_search_for"><wp:i18n key="jpcollaboration_SEARCH_FOR" /></label>
			<input id="jpcrowdsourcing_search_for" name="ideaText" type="text" value="<c:if test="${!empty param.ideaText}"><c:out value="${param.ideaText}" /></c:if>" class="input-block-level" />
		</p>
			<c:if test="${not empty categoryInfoList}">
		<p>
			<label for="jpcrowdsourcing_search_in">In</label>
			<select id="jpcrowdsourcing_search_in" name="ideaTag" class="input-block-level">
				<option value="" <c:if test="${empty param.ideaTag}"> selected="selected" </c:if>><wp:i18n key="jpcollaboration_SEARCH_IN_ALL" /></option>
				<c:forEach items="${categoryInfoList}" var="categoryInfo" varStatus="status">
					<option value="<c:out value="${categoryInfo.category.code}" />" <c:if test="${!empty param.ideaTag && param.ideaTag==categoryInfo.category.code}"> selected="selected" </c:if>>
						<c:out value="${categoryInfo.title}" />
					</option>
				</c:forEach>
			</select>
			</p>
			</c:if>
			<p><input type="submit" value="<wp:i18n key="jpcollaboration_SEARCH" />" class="btn" /></p>
		</p>
	</form>
</div>