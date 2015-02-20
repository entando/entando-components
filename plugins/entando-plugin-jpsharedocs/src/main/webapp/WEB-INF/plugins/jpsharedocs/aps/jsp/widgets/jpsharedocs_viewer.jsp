<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpshadoc" uri="/jpsharedoc-aps-core" %>

<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />

<jacms:content modelId="default" />

<wp:ifauthorized permission="jpsharedocs_edit">

	<%--
	<p class="edit_link">
		<a href="<wp:url page="sharedocs_edit" paramRepeat="true" />"><wp:i18n key="jpsharedocs_EDIT_DOC" /></a>
	</p>
	<p class="edit_comment">
		<a href="<wp:url page="sharedocs_comment" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_COMMENT_THIS_DOC" /></a>
	</p>
	--%>

<jacms:contentInfo param="contentId" var="contentIdVar"/>
<jacms:contentInfo param="authToEdit" var="editableVar"/>
<jpshadoc:contentOwner var="lastUserVar" contentId="${contentIdVar}"/>
<jpshadoc:contentCheckin var="checkinVar" contentId="${contentIdVar}"/>


	<wp:pageWithWidget var="sharedocsCommentPage" widgetTypeCode="jpsharedocs_comment" />

<%-- 	${sessionScope.currentUser. --%>
	
	<c:set var="currentUserVar" value="${sessionScope.currentUser.username}"/>
	
	<c:choose>
		<c:when test="${checkinVar ne null}">
		
			<wp:pageWithWidget var="sharedocsEditPage" widgetTypeCode="jpsharedocs_edit" />
			
			<c:choose>
				<c:when test="${checkinVar.checkinUser == currentUserVar }">
					<a href="<wp:url page="${sharedocsEditPage.code}" paramRepeat="true" />"><wp:i18n key="jpsharedocs_EDIT_CONTINUE" /></a>
				</c:when>
				<c:otherwise>
					<wp:i18n key="jpsharedocs_LOCKED_BY" />&nbsp;<c:out value="${checkinVar.checkinUser}" />, <c:out value="${checkinVar.checkinDate}" />
				</c:otherwise>
			</c:choose>

		
		</c:when>
		<c:otherwise>
			<wp:pageWithWidget var="sharedocsEditPage" widgetTypeCode="jpsharedocs_edit" />
			
			<p class="edit_link">
				<a href="<wp:url page="${sharedocsEditPage.code}" paramRepeat="true" />"><wp:i18n key="jpsharedocs_EDIT_DOC" /></a>
			</p>
			<p class="edit_link">
				<a href="<wp:url page="${sharedocsEditPage.code}" paramRepeat="true" />&checkin=true"><wp:i18n key="jpsharedocs_CHECK_IN" /></a>
			</p>
			<c:if test="${editableVar}">
				<c:if test="${lastUserVar}"><%-- comment this tag to check group permissions only --%>
					<wp:pageWithWidget var="sharedocsTrashPage" widgetTypeCode="jpsharedocs_trash" />
					<p class="edit_link">
						<a href="<wp:url page="${sharedocsTrashPage.code}"><wp:parameter name="contentId"><c:out value="${param['contentId']}" /></wp:parameter></wp:url> "><wp:i18n key="jpsharedocs_TRASH" /></a>
					</p>
				</c:if>
			</c:if>
		
		</c:otherwise>
	</c:choose>
	
	<p class="edit_comment">
		<a href="<wp:url page="${sharedocsCommentPage.code}" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_COMMENT_THIS_DOC" /></a>
	</p>
	
</wp:ifauthorized>