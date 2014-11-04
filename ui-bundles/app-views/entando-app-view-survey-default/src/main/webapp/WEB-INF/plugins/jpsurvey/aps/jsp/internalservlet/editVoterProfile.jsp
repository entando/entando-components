<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLine", "\n"); %> 
<s:set name="surveyInfo" value="voterResponse.survey" />
<div class="jpsurvey-voter-profile">
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
	</dl>
	<form 
		class="form-horizontal"
		action="<wp:action path="/ExtStr2/do/jpsurvey/Front/Survey/saveVoterProfile.action" />" 
		method="post" 
		>

		<s:if test="hasFieldErrors()">
			<div class="alert alert-block">
				<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-block">
				<p><strong><s:text name="message.title.ActionErrors" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<p class="noscreen">
			<wpsf:hidden name="surveyId" />
		</p>
		<div class="control-group">
			<div class="controls">
				<p class="label label-info"><wp:i18n key="JPSURVEY_PROFILE_NEEDED" /></p>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="jpsurvey-age"><wp:i18n key="JPSURVEY_AGE" /></label>
			<div class="controls">
				<wpsf:textfield useTabindexAutoIncrement="true" name="age" id="jpsurvey-age" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="jpsurvey-country"><wp:i18n key="JPSURVEY_COUNTRY" /></label>
			<div class="controls">
				<wpsf:textfield useTabindexAutoIncrement="true" name="country" id="jpsurvey-country" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="jpsurvey-sex"><wp:i18n key="JPSURVEY_SEX" /></label>
			<div class="controls">
				<wpsf:select 
					name="sex" 
					id="jpsurvey-sex" 
					useTabindexAutoIncrement="true" 
					list="#{'M':getText('label.sex.male'),'F':getText('label.sex.female')}"
					/>
			</div>
		</div>

		<p class="form-actions">
			<wpsf:submit 
				useTabindexAutoIncrement="true" 
				value="%{getText('label.save')}" 
				cssClass="btn btn-primary" 
				/>
		</p>
	</form>
</div>