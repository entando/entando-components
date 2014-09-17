<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="manageSurvey">
	<li class="margin-large-bottom"><span class="h5"><s:text name="title.surveyList" /></span>
		<ul class="nav nav-pills nav-stacked">
			<li>
				<a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
					<s:text name="jpsurvey.label.questionnaires" />
				</a>
			</li>
			<li>
				<a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
					<s:text name="jpsurvey.label.polls" />
				</a>
			</li>
		</ul>
	</li>
</wp:ifauthorized>