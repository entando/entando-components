<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpsu" uri="/jpsurvey-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLine", "\n"); %> 
<wp:pageWithWidget var="surveyDetailsPageCode" widgetTypeCode="jpsurvey_detailsSurvey" />

<wp:info key="currentLang" var="currentLang" />
<wp:info key="defaultLang" var="defaultLang" />

<jpsu:surveyList ctxName="pollList" category="poll" expired="true" />
<c:if test="${not empty pollList}">
	<ul class="unstyled jpsurvey-list">
		<c:forEach var="currentSurveyItem" items="${pollList}">
			<jpsu:loadSurvey ctxName="currentSurvey" surveyId="${currentSurveyItem}" preferredLang="${currentLang}" votedParamName="voted" ctxImageUrl="imageURL" imageDimension="1" />
			<li class="media">
				<%/*Vote*/%>
				<c:choose>
					<c:when test="${voted}"><wp:i18n key="JPSURVEY_YOU_HAVE_VOTED" var="votationVar" /></c:when>
					<c:when test="${currentSurvey.checkUsername && sessionScope.currentUser.username == 'guest' }"><wp:i18n key="JPSURVEY_DO_LOGIN" var="votationVar" /></c:when>
					<c:otherwise><wp:i18n key="JPSURVEY_YOU_HAVE_NOT_VOTED" var="votationVar" /></c:otherwise>
				</c:choose>
				<%/*Title*/%>
				<c:choose>
					<c:when test="${not empty currentSurvey.titles[currentLang]}"><c:set var="surveyTitle" value="${currentSurvey.titles[currentLang]}"/></c:when>
					<c:otherwise><c:set var="surveyTitle" value="${currentSurvey.titles[defaultLang]}"/></c:otherwise>
				</c:choose>
				<%/*Url*/%>
				<wp:url page="${surveyDetailsPageCode.code}" var="surveyUrlVar"><wp:parameter name="surveyId"><c:out value="${currentSurvey.id}"/></wp:parameter></wp:url>
				<h2>
					<a 
						href="<c:out value="${surveyUrlVar}" />" 
						title="<wp:i18n key="JPSURVEY_GO_TO_QUESTIONNAIRE" />:&#32;<c:out value="${currentSurvey.titles[defaultLang]}"/>"
						>
						<c:out value="${currentSurvey.titles[defaultLang]}"/>
					</a>
					&ensp;
					<span class="label"><c:out value="${votationVar}" /></span>
				</h2>
			<c:if test="${not empty currentSurvey.imageId}">
				<c:choose>
					<c:when test="${not empty currentSurvey.imageDescriptions[currentLang]}"><c:set var="imageDescr" value="${currentSurvey.imageDescriptions[currentLang]}" /></c:when>
					<c:otherwise><c:set var="imageDescr" value="${currentSurvey.imageDescriptions[defaultLang]}" /></c:otherwise>
				</c:choose>
				<a 
					href="<c:out value="${surveyUrlVar}" />"
					title="<wp:i18n key="JPSURVEY_GO_TO_QUESTIONNAIRE" />:&#32;<c:out value="${currentSurvey.titles[defaultLang]}"/>"
					class=" pull-left " 
					>
					<img class="img-polaroid" alt="<c:out value="${imageDescr}" />" src="<c:out value="${imageURL}" />"/>
				</a>
			</c:if>
				<%/* Descr */%>
				<c:choose>
					<c:when test="${not empty currentSurvey.descriptions[currentLang]}"><c:set var="surveyDescriptionVar"><c:out value="${currentSurvey.descriptions[currentLang]}"/></c:set></c:when>
					<c:otherwise><c:set var="surveyDescriptionVar"><c:out value="${currentSurvey.descriptions[defaultLang]}"/></c:set></c:otherwise>
				</c:choose>
				<p>
					<c:out value="${fn:replace(surveyDescriptionVar, newLine, '<br />')}" escapeXml="false"  />
				</p>
			</li>
		</c:forEach>
	</ul>
</c:if>
<wp:pageWithWidget var="archivePageVar" widgetTypeCode="jpsurvey_pollList" />
<p>
	<span class="label label-info">
	<wp:i18n key="JPSURVEY_POLLS_ARCHIVE_INTRO" />&#32;
	<a 
		href="<wp:url page="${archivePageVar.code}" />" 
		title="<wp:i18n key="JPSURVEY_GO_TO_ACTIVE_POLLS" />">
			<wp:i18n key="JPSURVEY_GO_ACTIVE_POLLS" />
	</a>.
	</span>
</p>