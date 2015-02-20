<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wpbl" uri="/jpblog" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jpcontentfeedback" uri="/jpcontentfeedback-aps-core" %>
<wp:info key="currentLang" var="currentLang" />
<wpbl:blogList listName="contentList"  />
<wp:currentWidget param="config" configParam="contentType" var="currentTypeCode" />

<wp:pageWithWidget var="jpblog_post_list_page" widgetTypeCode="jpblog_post_list"  listResult="false" />
<wp:url var="listPageUrl" page="${jpblog_post_list_page.code}" />

<wpbl:blogCategory var="categoryInfoBeanList" typeCode="${currentTypeCode}"/>
<c:set var="extraInfoList" value="${null}" />
<c:choose>
	<c:when test="${!empty param.jpblogcat}">
		<c:forEach items="${categoryInfoBeanList}" var="catInfoBean" varStatus="status">
			<c:if test="${catInfoBean.category.code == param.jpblogcat}">
				<c:set var="extraInfoList" value="${catInfoBean.category.titles[currentLang]}" />
			</c:if>
		</c:forEach>
	</c:when>
	<c:when test="${!empty param.jpblogmonth}">
		<wp:info key="currentLang" var="currentLang" />
		<c:set var="date"><c:out value="${param.jpblogmonth}" />01</c:set>
		<fmt:parseDate pattern="yyyyMMdd" value="${date}" var="parsedDate" />
		<fmt:setLocale value="${currentLang}" />
		<fmt:formatDate value="${parsedDate}" pattern="MMMM yyyy" var="extraInfoList" />
	</c:when>
</c:choose>
<c:if test="${extraInfoList!=null}">
	<p><wp:i18n key="jpblog_ARCHIVE_TITLE" />:&#32;<span class="label label-info"><span class="icon-filter icon-white"></span>&#32;<c:out value="${extraInfoList}" />&#32;<a href="${listPageUrl}" title="<wp:i18n key="jpblog_VIEW_ALL" />"><span class="icon-remove icon-white"></span></a></span></p>
	<hr />
</c:if>
<div class="contenuto">
	<c:if test="${!empty contentList && contentList != null}">
		<wpbl:blogpager pagerId="contentsPager" listName="contentList"  objectName="groupContent" pagerIdFromFrame="true" >
			<c:forEach var="contentId" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
				<jacms:content contentId="${contentId}" />
					<c:if test="${not empty contentsPager_singleContent}">
						<jpcontentfeedback:feedbackIntro contentId="${contentId}" extraParamsRedirect="jpblogcat,jpblogmonth,contentsPager_item"/>
					</c:if>
			</c:forEach>
			<c:set var="group" value="${groupContent}" scope="request" />
			<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
		</wpbl:blogpager>

		<%-- Important: reset variables --%>
		<c:set var="contentList" value="${null}" scope="request" />
		<c:set var="group" value="${null}" scope="request" />
	</c:if>
</div>