<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>

<div class="jpcrowdsourcing jpcrowdsourcing_fastEntryIdea">
	<h1><wp:i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h1>

<jpcrwsrc:currentPageWidget param="config" configParam="instanceCode" widget="collaboration_ideaInstance" var="instanceVar"/>

	<c:choose>
		<c:when test="${sessionScope.currentUser != 'guest'}">
			<jpcrwsrc:pageWithWidget var="entryIdea_page" widgetTypeCode="jpcollaboration_entryIdea" listResult="false" />
			<form action="<wp:url page="${entryIdea_page.code}" />" method="post" accept-charset="UTF-8">
				<c:if test="${null != instanceVar}">
					<input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value="<c:out value="${instanceVar}" />" />
				</c:if>
				<p>
					<label for="jpcrowdsourcing_fastDescr"><wp:i18n key="jpcollaboration_LABEL_DESCR" /></label>
					<textarea rows="5" cols="40" name="jpcrowdsourcing_fastDescr" id="jpcrowdsourcing_fastDescr" class="input-block-level"></textarea>
				</p>
				<p>
					<input type="submit" value="<wp:i18n key="jpcollaboration_SUBMIT_IDEA" />" class="btn" />
				</p>
			</form>
		</c:when>
		<c:otherwise>
			<p class="alert alert-warning"><wp:i18n key="jpcollaboration_DO_LOGIN" /></p>
		</c:otherwise>
	</c:choose>
</div>