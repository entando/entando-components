<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="ibox-content">
<h2><wp:i18n key="SEARCH_RESULTS" /></h2>
<jacms:searcher listName="result" />
<p><wp:i18n key="SEARCHED_FOR" />: <em><strong><c:out value="${search}" /></strong></em></p>
<c:choose>
<c:when test="${empty result}">
	<h2><wp:i18n key="SEARCH_RESULTS" /></h2>
	<p class="text-danger"><wp:i18n key="SEARCH_NOTHING_FOUND" /></p>
</c:when>
<c:otherwise>
	<wp:pager listName="result" objectName="groupContent" max="10" pagerIdFromFrame="true" advanced="true" offset="5">
		<c:set var="group" value="${groupContent}" scope="request" />
	
		<h2> <wp:i18n key="SEARCHED_FOR" />: <span class="text-navy">&#34;<c:out value="${RequestParameters.search}" />&#34;</span> </h2>
		<small><c:out value="${groupContent.size}" /> [<c:out value="${groupContent.begin + 1}" /> &ndash; <c:out value="${groupContent.end + 1}" />]:</small>
	
		<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
		<div class="hr-line-dashed"></div>
		<c:forEach var="contentId" items="${result}" begin="${groupContent.begin}" end="${groupContent.end}">
			<jacms:content contentId="${contentId}" modelId="list" />
		</c:forEach>
		<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
	
	</wp:pager>

</c:otherwise>
</c:choose>
</div>