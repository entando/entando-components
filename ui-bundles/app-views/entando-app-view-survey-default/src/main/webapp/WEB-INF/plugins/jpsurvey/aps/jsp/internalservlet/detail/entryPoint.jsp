<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpsu" uri="/jpsurvey-aps-core" %>
<% pageContext.setAttribute("newLine", "\n"); %>
<s:set var="surveyInfo" value="survey" />
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
			<s:set var="imageURL" value="%{getSurveyImageURL(surveyInfo.imageId,2)}" />
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
				<s:else>
					<%-- you did NOT vote --%><wp:i18n key="JPSURVEY_YOU_NOT_VOTED" />
				</s:else>
			</p>
		</dd>
	<dt><wp:i18n key="JPSURVEY_VOTED_TOT" /></dt>
		<dd>
			<p><s:property value="%{getTotalVoters(#surveyInfo.id)}" />&#32;<wp:i18n key="JPSURVEY_PERSON" /></p>
		</dd>
</dl>

<s:iterator value="#surveyInfo.questions" var="question" status="questionsStatus">
	
	<hr />

	<s:set var="occurrences" value="%{getQuestionStatistics(#question.id)}" />
	<h2>
		<span class="badge"><wp:i18n key="JPSURVEY_QUESTION" />&#32;<s:property value="#questionsStatus.count"/></span><span class="noscreen">:</span>&#32;
		<c:set var="questionText"><s:property value="%{getLabel(#question.questions)}" /></c:set>
		<c:out value="${fn:replace(questionText, newLine, '<br />')}" escapeXml="false"  />
	</h2>
	
	<div class="row-fluid">
		<div class="span12">
			<h3><wp:i18n key="JPSURVEY_ANSWERS" /></h3>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span6">
			<ol>
				<s:iterator value="#question.choices" var="choice">	
					<li>
						<c:set var="answerText"><s:property value="%{getLabel(#choice.choices)}" /></c:set>
						<c:out value="${fn:replace(answerText, newLine, '<br />')}" escapeXml="false"  />
					</li>
				</s:iterator>
			</ol>
		</div>

		<div class="span6">
			<dl>
			<s:iterator id="choice" value="#question.choices" status="rowstatus" >	
				<s:set var="occurrence" value="#occurrences[#choice.id]" />
				<s:set var="occurrence" value="%{#occurrence==null ? 0 : #occurrence}" />
				<s:set var="roundedPercentage" value="%{getChoicePercentage(#occurrences, #choice.id)}" />
				<s:set var="roundedPercentage" value="%{#roundedPercentage==null ? 0 : #roundedPercentage}" />
				<s:set var="barRoundedPercentage" value="%{(#roundedPercentage<=0 ) ? 5 : #roundedPercentage}" />
				<dt class="noscreen"><s:property value="%{getLabel(#choice.choices)}" /></dt>
					<dd>
						<div class="progress" title="<wp:i18n key="JPSURVEY_ANSWER_NUMBER" />&#32;<s:property value="#rowstatus.count"/>&#32;<wp:i18n key="JPSURVEY_OBTAINED" />&#32;<s:property value="#occurrence" />&#32;<wp:i18n key="JPSURVEY_VOTES" /> - &#32;<s:property value="%{#roundedPercentage}"/>%">
							<div class="bar" style="width: <s:property value="%{#barRoundedPercentage}"/>%;">							
								<span class="noscreen"><wp:i18n key="JPSURVEY_ANSWER_NUMBER" /></span>
	 							<em><s:property value="#rowstatus.count"/></em>
								<span class="noscreen">
									&#32;
									<wp:i18n key="JPSURVEY_OBTAINED" />&#32;
									<s:property value="#occurrence" />&#32;
									<wp:i18n key="JPSURVEY_VOTES" />
								</span>
								&#32;-&#32;<s:property value="%{#roundedPercentage}"/>%
							</div>
						</div>
					</dd>
			</s:iterator>	
			</dl>
		</div>
	</div>
</s:iterator>
</div>