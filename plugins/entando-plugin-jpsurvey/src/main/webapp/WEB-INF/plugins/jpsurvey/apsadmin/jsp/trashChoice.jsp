<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="title.surveyList" /></li>
	<s:if test="choice.questionnaire">
		<li><a
			href="<s:url action="listSurveys" ><s:param name="questionnaire" value="choice.questionnaire"></s:param></s:url>"
			title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text
					name="title.jpsurvey.survey.main" /></a></li>
	</s:if>
	<s:else>
		<li><a
			href="<s:url action="listSurveys" ><s:param name="questionnaire" value="choice.questionnaire"></s:param></s:url>"
			title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text
					name="title.jpsurvey.poll.main" /></a></li>
	</s:else>
	<li><s:text name="title.jpsurvey.question.edit" /></li>
	<li class="page-title-container"><s:text
			name="title.jpsurvey.trash.choice" /></li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:text name="title.jpsurvey.trash.choice" />
				<span class="pull-right"> <a tabindex="0" role="button"
					data-toggle="popover" data-trigger="focus" data-html="true"
					title="" data-content="TO be inserted" data-placement="left"
					data-original-title=""> <i class="fa fa-question-circle-o"
						aria-hidden="true"></i>
				</a>
				</span>
			</h1>
		</div>
		<wp:ifauthorized permission="superuser">
			<div class="col-sm-6">
				<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
					<s:if test="questionnaire">
						<li class="active"><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
								<s:text name="jpsurvey.label.questionnaires" />
						</a></li>
					</s:if>
					<s:else>
						<li><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
								<s:text name="jpsurvey.label.questionnaires" />
						</a></li>
					</s:else>
					<s:if test="questionnaire">
						<li><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
								<s:text name="jpsurvey.label.polls" />
						</a></li>
					</s:if>
					<s:else>
						<li class="active"><a
							href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
								<s:text name="jpsurvey.label.polls" />
						</a></li>
					</s:else>
				</ul>
			</div>
		</wp:ifauthorized>
	</div>
</div>
<br>
<div class="text-center">
	<s:text name="label.workingOn" />
	:&#32; <em><s:property value="%{getLabel(choice.surveyTitles)}" /></em>

	<s:form action="deleteChoice" class="form-horizontal">
		<p class="noscreen">
			<s:hidden name="questionId" value="%{choice.questionId}" />
			<s:hidden name="choiceId" />
		</p>
		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="label.delete" />
		</p>
		<p class="esclamation-underline-text">
			<s:text name="jpsurvey_delete_confirm" />
			&#32;
			<s:if test="!choice.freeText">
				<s:text name="jpsurvey_the_answer" />:&#32;
                <em class="important"><s:property
						value="%{getLabel(choice.choices)}" /></em>
			</s:if>
			<s:else>
				<span class="important"><s:text name="jpsurvey_the_free_text" /></span>
			</s:else>
			&#32;
			<s:text name="jpsurvey_delete_confirm_from" />
			&#32; <em><s:property value="%{getLabel(choice.questions)}" /></em>?&#32;
		</p>
		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width"
				href="<s:url action="editQuestion" namespace="/do/jpsurvey/Survey"><s:param name="questionId" value="choice.questionId" /></s:url>">
				<s:text name="title.jpsurvey.question.edit" />
			</a>
			<wpsf:submit type="button"
				cssClass="btn btn-danger button-fixed-width">
				<s:text name="%{getText('label.jpsurvey.delete')}" />
			</wpsf:submit>
		</div>
	</s:form>
</div>