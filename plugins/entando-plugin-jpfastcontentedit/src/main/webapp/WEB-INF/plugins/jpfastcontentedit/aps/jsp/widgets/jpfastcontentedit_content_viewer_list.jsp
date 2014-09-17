<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="fce" uri="/jpfastcontentedit-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%/*FIXME: fastcontenteditVar this page should be configurable from the admin area! */%>
<c:set var="fastcontenteditVar">fastcontentedit</c:set>
<jacms:contentList 
	listName="contentList" 
	titleVar="titleVar" 
	pageLinkVar="pageLinkVar" 
	pageLinkDescriptionVar="pageLinkDescriptionVar" 
	userFilterOptionsVar="userFilterOptionsVar" />

<c:if test="${!(empty titleVar)}">
	<h1><c:out value="${titleVar}" /></h1>
</c:if>

<c:set var="userFilterOptionsVar" value="${userFilterOptionsVar}" scope="request" />
<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module.jsp" />

<c:choose>
	<c:when test="${!(empty contentList)}">
		<fce:allowedContents var="allowedContents" />
		<wp:pager listName="contentList" objectName="groupContent" pagerIdFromFrame="true" advanced="true" offset="5">
			<c:set var="group" value="${groupContent}" scope="request" />
			<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
			<c:forEach var="contentId" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
				<jacms:content contentId="${contentId}" />
				<wp:ifauthorized permission="jpfastcontentedit_editContents">
					<fce:isContentAllowed listName="allowedContents" contentId="${contentId}" >
						<p class="btn-group">
							<a class="btn btn-primary" href="<wp:url page="${fastcontenteditVar}" >
									<wp:parameter name="contentId" ><c:out value="${contentId}" /></wp:parameter>
									<wp:parameter name="contentTypeCode" ><wp:currentWidget param="config" configParam="contentType" /></wp:parameter>
									<wp:parameter name="finalPageDest" ><wp:currentPage param="code" /></wp:parameter>
								</wp:url>" ><span class="icon icon-edit"></span> <wp:i18n key="jpfastcontentedit_EDIT_CONTENT" />
							</a>

							<a class="btn btn-danger" href="<wp:url page="${fastcontenteditVar}" >
									<wp:parameter name="delete" >true</wp:parameter>
									<wp:parameter name="contentId" ><c:out value="${contentId}" /></wp:parameter>
									<wp:parameter name="contentTypeCode" ><wp:currentWidget param="config" configParam="contentType" /></wp:parameter>
									<wp:parameter name="finalPageDest" ><wp:currentPage param="code" /></wp:parameter>
								</wp:url>" ><span class="icon icon-remove-sign"></span> <wp:i18n key="jpfastcontentedit_REMOVE_CONTENT" />
							</a>
						</p>
					</fce:isContentAllowed>
				</wp:ifauthorized>
			</c:forEach>
			<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
		</wp:pager>
	</c:when>
	<c:otherwise>
		<c:if test="${not empty userFilterOptionsVar}">
			<p class="alert alert-info"><wp:i18n key="LIST_EMPTY" /></p>
		</c:if>
	</c:otherwise>
</c:choose>
<c:if test="${(!empty pageLinkVar) && (!empty pageLinkDescriptionVar)}">
	<p><a href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
</c:if>
<wp:ifauthorized permission="jpfastcontentedit_editContents">
	<p>
			<a 
				class="btn btn-info" 
				href="<wp:url page="${fastcontenteditVar}">
				<wp:parameter name="contentTypeCode" ><wp:currentWidget param="config" configParam="contentType" /></wp:parameter>
				<wp:parameter name="finalPageDest" ><wp:currentPage param="code" /></wp:parameter>
			</wp:url>" ><span class="icon icon-plus"></span> <wp:i18n key="jpfastcontentedit_LABEL_NEW_CONTENT" />
		</a>
	</p>
</wp:ifauthorized>