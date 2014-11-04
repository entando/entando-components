<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<s:set name="surveyInfo" value="surveyInfo" />
<div class="jpsurvey submit-done alert alert-success">
	<s:if test="#surveyInfo.questionnaire">
		<wp:pageWithWidget var="archivePageVar" widgetTypeCode="jpsurvey_questionnaireList" />
		<wp:i18n key="JPSURVEY_GO_TO_ACTIVE_QUESTIONNAIRE" var="titleLabelVar" />
		<wp:i18n key="JPSURVEY_ACTIVE_QUESTIONNAIRE" var="descrLabelVar" />
	</s:if>
	<s:else>
		<wp:pageWithWidget var="archivePageVar" widgetTypeCode="jpsurvey_pollList" />
		<wp:i18n key="JPSURVEY_GO_TO_ACTIVE_POLLS" var="titleLabelVar" />
		<wp:i18n key="JPSURVEY_GO_ACTIVE_POLLS" var="descrLabelVar" />
	</s:else>
	
	<strong><wp:i18n key="JPSURVEY_THANKS_FOR" /></strong>
	&#32;
	<a href="<wp:url page="${archivePageVar.code}"></wp:url>" title="<c:out value="${titleLabelVar}" />" >
		<c:out value="${descrLabelVar}" escapeXml="false" />
	</a>
	
</div>

