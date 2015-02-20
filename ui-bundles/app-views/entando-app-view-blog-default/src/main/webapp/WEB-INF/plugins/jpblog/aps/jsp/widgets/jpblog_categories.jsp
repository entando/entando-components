<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wpbl" uri="/jpblog" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<div class="jpblog">
<h1><wp:i18n key="jpblog_CATEGORIES_TITLE" /></h1>
	<wpbl:blogCategory var="categoryInfoBeanList" />
	<ul class="unstyled">
		<wpbl:pageWithWidget var="jpblog_post_list_page" widgetTypeCode="jpblog_post_list" listResult="false"/>
		<c:forEach var="categoryInfoBean" items="${categoryInfoBeanList}">
		<li>
			<wp:url var="listPageUrl" page="${jpblog_post_list_page.code}">
				<wp:urlPar name="jpblogcat" ><c:out value="${categoryInfoBean.category.code}" /></wp:urlPar><wp:urlPar name="modelId">10061</wp:urlPar>
			</wp:url>
			<a href="${listPageUrl}">
			<c:out value="${categoryInfoBean.title}" />&#32;<span class="badge"><c:out value="${categoryInfoBean.occurrences}" /></span>
			</a>
		</li>
		</c:forEach>
	</ul>
</div>