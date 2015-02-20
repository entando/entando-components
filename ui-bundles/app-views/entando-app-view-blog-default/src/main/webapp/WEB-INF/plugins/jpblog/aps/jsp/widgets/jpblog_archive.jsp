<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wpbl" uri="/jpblog" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<wp:info key="currentLang" var="currentLang" />
<div class="jpblog">
<h1><wp:i18n key="jpblog_ARCHIVE_TITLE" /></h1>
	<wpbl:blogArchive var="blogArchiveInfoBeans" />
	<wpbl:pageWithWidget var="jpblog_post_list_page" widgetTypeCode="jpblog_post_list" listResult="false"/>
	<ul class="unstyled">
		<c:forEach var="blogArchiveInfoBean" items="${blogArchiveInfoBeans}">
			<li>
				<wp:url var="listPageUrl" page="${jpblog_post_list_page.code}"><wp:urlPar name="jpblogmonth" ><c:out value="${blogArchiveInfoBean.year}" /><c:out value="${blogArchiveInfoBean.month}" /></wp:urlPar><wp:urlPar name="modelId">10061</wp:urlPar></wp:url>
				<a href="${listPageUrl}">
				<%--
				<c:out value="${blogArchiveInfoBean.month}" /> / <c:out value="${blogArchiveInfoBean.year}" />
				--%>
				<fmt:setLocale value="${currentLang}" />
				<c:set var="date"><c:out value="${blogArchiveInfoBean.year}" />/<c:out value="${blogArchiveInfoBean.month}" />/01</c:set>
				<fmt:parseDate pattern="yyyy/MM/dd" value="${date}" var="parsedDate" />
				<fmt:formatDate value="${parsedDate}" pattern="yyyy MMMM" />
				&#32;<span class="badge"><c:out value="${blogArchiveInfoBean.occurrences}" /></span>
				</a>
			</li>
		</c:forEach>
	</ul>
</div>