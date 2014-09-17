<%@ taglib prefix="jptc" uri="/jpcmstagcloud-aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<jptc:contentList listName="contentList" />
<jptc:tagCloudBuilder occurrencesVar="occurrences" cloudBeansVar="cloudBeans" />
<h2><wp:i18n key="jpcmstagcloud_TAGGEDCONTENTS"></wp:i18n> </h2>
<jacms:searcher listName="result" />
<p><wp:i18n key="jpcmstagcloud_TAG" />:&#32;<em><strong><c:forEach items="${cloudBeans}" var="tag"><c:if test="${tag.cloudNode.code == tagCategoryCode}"><c:out value="${tag.title}" /></c:if></c:forEach></strong></em></p>
<c:if test="${!empty contentList}">
	<wp:pager listName="contentList" objectName="groupContent" pagerIdFromFrame="true" max="10" advanced="true" offset="5" >
		<c:set var="group" value="${groupContent}" scope="request" />
		<p><em><wp:i18n key="jpcmstagcloud_INTRO" />&#32;<c:out value="${groupContent.size}" />&#32;<wp:i18n key="jpcmstagcloud_OUTRO" />&#32;[<c:out value="${groupContent.currItem}" />/<c:out value="${groupContent.maxItem}" />]:</em></p>
		<c:import url="/WEB-INF/plugins/jpcmstagcloud/aps/jsp/widgets/inc/pagerBlock.jsp" />
		<ul>
			<c:forEach var="contentId" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
				<li><jacms:content contentId="${contentId}" modelId="list" /></li>
			</c:forEach>
		</ul>
		<c:import url="/WEB-INF/plugins/jpcmstagcloud/aps/jsp/widgets/inc/pagerBlock.jsp" />
	</wp:pager>
</c:if>