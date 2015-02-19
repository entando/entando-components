<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="jpcrowdsourcing userNotAllowed">
	<wp:currentWidget param="code" var="currentShowletCode" />
	<h1>
	<c:choose>
		<c:when test="${currentShowletCode=='jpcrowdsourcing_entryIdea'}">
			<wp:i18n key="jpcollaboration_NEW_IDEA_TITLE" />
		</c:when>
		<c:when test="${currentShowletCode=='jpcrowdsourcing_idea'}">
			<wp:i18n key="jpcollaboration_LIST_IDEA_TITLE" />
		</c:when>
		<c:when test="${currentShowletCode=='jpcrowdsourcing_ideaList'}">
			<wp:i18n key="jpcollaboration_LIST_IDEA_TITLE" />
		</c:when>
	</c:choose>
	</h1>
	<p class="alert alert-warning">
		<wp:i18n key="jpcollaboration_DO_LOGIN" />
	</p>
</div>
