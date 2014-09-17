<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpsu" uri="/jpsurvey-aps-core" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<s:set name="surveyInfo" value="surveyInfo" />
<div class="jpsurvey-detail">
<h1><s:property value="%{getLabel(#surveyInfo.titles)}" /></h1>
<dl class="dl-horizontal">
	<%--
	<dt><wp:i18n key="JPSURVEY_TITLE" /></dt>
		<dd><p><s:property value="%{getLabel(#surveyInfo.titles)}" /></p></dd>
	--%>
	<dt><p><wp:i18n key="JPSURVEY_DESCRIPTION" /></p></dt>
		<dd>
			<%-- Image --%>
			<s:set name="imageURL" value="%{getSurveyImageURL(surveyInfo.imageId,2)}" />
			<s:if test="#imageURL != null && #imageURL != '' ">
				<p>
					<img 
						class="img-polaroid" 
						alt="<s:property value="%{getLabel(#surveyInfo.imageDescriptions)}" />" 
						src="<s:property value="#imageURL"/>" />
				</p>
			</s:if>
			<%-- Description --%>
			<c:set var="surveyDescriptionVar"><s:property value="%{getLabel(#surveyInfo.descriptions)}" /></c:set>
			<p><c:out value="${fn:replace(surveyDescriptionVar, newLine, '<br />')}" escapeXml="false"  /></p>
		</dd>
		<dd>
			<p>
			<s:if test="#surveyInfo.questionnaire"><%-- Questionnaire publishing info --%><wp:i18n key="JPSURVEY_SURVEY_STARTDAY" /></s:if>
			<s:else><%-- Poll publishing info --%><wp:i18n key="JPSURVEY_POLL_STARTDAY" /></s:else>
			&#32;
				<time class="label" datetime="<s:date name="#surveyInfo.startDate" format="yyyy-MM-dd" />">
					<s:date name="#surveyInfo.startDate" format="EEEE dd/MM/yyyy" />
				</time>
				<s:if test="null != #surveyInfo.endDate">&#32;
					<s:if test="!#surveyInfo.archive" >
						<wp:i18n key="JPSURVEY_ENDDAY" />&#32;
					</s:if>
					<s:else>
						<wp:i18n key="JPSURVEY_ARCHIVE_ENDDAY" />&#32;
					</s:else>
					<time class="label" datetime="<s:date name="#surveyInfo.endDate" format="yyyy-MM-dd" />">
						<s:date name="#surveyInfo.endDate" format="EEEE dd/MM/yyyy" />
					</time>
				</s:if>
			</p>
		</dd>
		<dd>
			<p>
				<s:if test="voted">
					<%-- you did vote --%><span class="label label-success"><wp:i18n key="JPSURVEY_YOU_VOTED" /></span>
				</s:if>
				<s:else><%-- you did NOT vote || login --%>
					<s:if test="#surveyInfo.checkUsername && #session.currentUser.username=='guest'">
						<%-- go login --%><wp:i18n key="JPSURVEY_DO_LOGIN" />
					</s:if>
					<s:else>
						<%-- go voting --%><wp:i18n key="JPSURVEY_YOU_NOT_VOTED" />
					</s:else>
				</s:else>
			</p>
		</dd>
		<dd>
			<p>
				<s:if test="%{!voted}">
					<s:if test="%{!(#surveyInfo.checkUsername && #session.currentUser.username=='guest')}">
						<s:if test="!#surveyInfo.archive" >
							<a 
								class="btn btn-primary" 
								href="<wp:action path="/ExtStr2/do/jpsurvey/Front/Survey/startSurvey.action" ><wp:parameter name="surveyId"><s:property value="#surveyInfo.id" /></wp:parameter></wp:action>"
								>
								<wp:i18n key="JPSURVEY_BEGIN" />&#32;
								<s:if test="#surveyInfo.questionnaire"><wp:i18n key="JPSURVEY_SURVEY" /></s:if>
								<s:else><wp:i18n key="JPSURVEY_POLL" /></s:else>
								<span class="icon  icon-chevron-right"></span>
							</a>
						</s:if>
					</s:if>
				</s:if>
				<s:if test="!#surveyInfo.questionnaire">
					<s:if test="%{(#surveyInfo.archive && #surveyInfo.publicResult) || (!#surveyInfo.archive && #surveyInfo.publicPartialResult && #surveyInfo.open) }">
							&#32;
						<wp:pageWithWidget var="resultsPageVar" widgetTypeCode="jpsurvey_resultsSurvey" />
							<a 
								class="btn"
								href="<wp:url page="${resultsPageVar.code}"><wp:parameter name="surveyId"><s:property value="#surveyInfo.id" /></wp:parameter></wp:url>">
								<s:if test="%{#surveyInfo.archive}"><wp:i18n key="JPSURVEY_FINAL_RESULTS" /></s:if>
								<s:else><wp:i18n key="JPSURVEY_PARTIAL_RESULTS" /></s:else>
							</a>
					</s:if>
				</s:if>
			</p>
		</dd>
</dl>
</div>