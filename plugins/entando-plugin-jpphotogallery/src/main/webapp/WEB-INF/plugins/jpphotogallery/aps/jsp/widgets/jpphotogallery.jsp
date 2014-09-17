<%@ taglib prefix="jpph" uri="/jpphotogallery-aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:currentWidget param="config" configParam="modelIdMaster" var="modelIdMaster" />
<wp:currentWidget param="config" configParam="modelIdPreview" var="modelIdPreview" />

<jacms:contentList listName="contentList" titleVar="titleVar" 
	pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" userFilterOptionsVar="userFilterOptionsVar" /> 

<c:if test="${contentList != null}">
	<wp:headInfo type="CSS" info="../../plugins/jpphotogallery/jpphotogallery.css"/>
	<div class="jpphotogallery">
	
	<c:if test="${not empty titleVar}">
		<h2 class="title"><c:out value="${titleVar}" /></h2>
	</c:if>
	<c:if test="${empty titleVar}">
		<h2 class="title"><wp:i18n key="jpphotogallery_TITLE" /> </h2>
	</c:if>
	
	<c:set var="userFilterOptionsVar" value="${userFilterOptionsVar}" scope="request" />
	<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module.jsp" />
		
	<jpph:pager listName="contentList" objectName="groupContent" max="1" advanced="true" offset="5" >
		<c:set var="group" value="${groupContent}" scope="request" />
		
		<%--  paginatore alto --%>
		<c:import url="/WEB-INF/plugins/jpphotogallery/aps/jsp/widgets/inc/jpphotogallery_pagerBlock.jsp" />
		<%--  paginatore alto--%>
		
		<%-- lista immagini precedenti start--%>
		<c:if test="${groupContent.begin > 0}">
				
				<h3 class="noscreen"><wp:i18n key="jpphotogallery_PREV_IMAGES" /></h3>
				
				<ul class="thumbnails">
				<%-- controllo sulla lista delle immagini precedenti --%>
				<c:set var="precedentiBegin" value="${groupContent.begin - 3}" />
				<c:if test="${precedentiBegin < 0}"><c:set var="precedentiBegin" value="0" /></c:if>
				<c:set var="precendentiEnd" value="${groupContent.end - 1}" />
				<c:if test="${precendentiEnd < 0}"><c:set var="precendentiEnd" value="0" /></c:if>
				<%-- fine controllo sulla lista delle immagini precedenti --%>
				
				<c:forEach var="content" items="${contentList}" begin="${precedentiBegin}" end="${precendentiEnd}" varStatus="status">
					<%-- controllo per stabilire qual'e' l'immagine anteprima di inizio e quella di fine --%>
					<li  class="img_preview_<c:out value="${status.count}" />">
					<jacms:content contentId="${content}" modelId="${modelIdPreview}" />
					</li>
					<%-- fine controllo end  --%>
				</c:forEach>
				</ul>
		</c:if>
		<%-- lista immagini precedenti --%>
		
		<%--  immagine centrale start --%>
			<c:forEach var="content" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
				<div class="main_image">
					<jacms:content contentId="${content}" modelId="${modelIdMaster}" />
				</div>
			</c:forEach>
		<%--  immagine centrale end --%>
		
		<%--  lista immagini successive start --%>
		<c:if test="${(groupContent.size-groupContent.end - 1)>0}">
			<h3 class="noscreen"><wp:i18n key="jpphotogallery_NEXT_IMAGES" /></h3>
			<ul class="thumbnails">
				<c:forEach var="content" items="${contentList}" begin="${groupContent.end + 1}" end="${groupContent.end + 3}" varStatus="status" >
					<%-- controllo per stabilire qual'e' l'immagine anteprima di inizio e quella di fine --%>
						<%-- controllo per stabilire qual'e' l'immagine anteprima di inizio e quella di fine --%>
						<li  class="img_preview_<c:out value="${status.count}" />">
						<jacms:content contentId="${content}" modelId="${modelIdPreview}" />
						</li>
						<%-- fine controllo end  --%>
				<%-- fine controllo --%>
				</c:forEach>
			</ul>
		</c:if>
		<%--  lista immagini successive end--%>
		
		
		<%--  paginatore basso --%>
		<c:import url="/WEB-INF/plugins/jpphotogallery/aps/jsp/widgets/inc/jpphotogallery_pagerBlock.jsp" />
		<%--  paginatore basso--%>
		
	</jpph:pager>
	
	<c:if test="${null != pageLinkVar && null != pageLinkDescriptionVar}">
		<p><a href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
	</c:if>
	
	<%-- Important: reset variables --%>
	<c:set var="userFilterOptionsVar" value="${null}" scope="request" />
	<c:set var="contentList" value="${null}"  scope="request" />
	<c:set var="group" value="${null}"  scope="request" />
	
</div>
</c:if>	