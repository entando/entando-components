<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<wp:ifauthorized permission="superuser">
	<li class="list-group-item"><a
		href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
			<s:text name="title.surveyList" />
	</a></li>
</wp:ifauthorized>