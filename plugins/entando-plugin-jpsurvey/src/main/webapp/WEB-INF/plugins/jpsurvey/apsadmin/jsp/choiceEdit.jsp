<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="title.surveyList" /></li>
	<s:if test="questionnaire">
		<li><a
			href="<s:url action="listSurveys" ><s:param name="questionnaire" value="true"></s:param></s:url>"
			title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text
					name="title.jpsurvey.survey.main" /></a></li>
	</s:if>
	<s:else>
		<li><a
			href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>"
			title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text
					name="title.jpsurvey.poll.main" /></a></li>
	</s:else>
	<s:if test="getStrutsAction() == 1">
		<li class="page-title-container"><s:text
				name="title.jpsurvey.choice.new" /></li>
	</s:if>
	<s:if test="getStrutsAction() == 2">
		<li class="page-title-container"><s:text
				name="title.jpsurvey.choice.edit" /></li>
	</s:if>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:if test="getStrutsAction() == 1">
					<s:text name="title.jpsurvey.choice.new" />
					<span class="pull-right"> <a tabindex="0" role="button"
						data-toggle="popover" data-trigger="focus" data-html="true"
						title=""
						data-content="<s:text name="title.jpsurvey.choice.new.help" />"
						data-placement="left" data-original-title=""> <i
							class="fa fa-question-circle-o" aria-hidden="true"></i>
					</a>
					</span>
				</s:if>
				<s:if test="getStrutsAction() == 2">
					<s:text name="title.jpsurvey.choice.edit" />
					<span class="pull-right"> <a tabindex="0" role="button"
						data-toggle="popover" data-trigger="focus" data-html="true"
						title=""
						data-content="<s:text name="title.jpsurvey.choice.edit.help" />"
						data-placement="left" data-original-title=""> <i
							class="fa fa-question-circle-o" aria-hidden="true"></i>
					</a>
					</span>
				</s:if>
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
<div id="messages">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
	<div class="panel panel-default">
		<div class="panel-body">
			<s:text name="label.workingOn" />
			:&#32; <em><s:property value="%{getLabel(choice.surveyTitles)}" /></em>,
			<s:text name="label.workingOn.question" />
			:&#32;<em><s:property value="%{getLabel(choice.questions)}" /></em>
		</div>
	</div>

	<s:form cssClass="tab-container" action="saveChoice"
		class="form-horizontal">

		<p class="noscreen">
			<s:hidden name="choiceId" value="%{choice.id}" />
			<s:hidden name="questionId" value="%{choice.questionId}" />
			<s:hidden name="strutsAction" />
		</p>

		<ul class="nav nav-tabs tab-togglers" id="tab-togglers">
			<s:iterator value="langs" var="lang" status="langStatusVar">
				<li <s:if test="#langStatusVar.first"> class="active" </s:if>>
					<a data-toggle="tab" href="#<s:property value="#lang.code" />_tab"><s:property
							value="#lang.descr" /></a>
				</li>
			</s:iterator>
		</ul>
		<div class="panel" id="tab-container">
			<div class="panel-body">
				<div class="tab-content">
					<s:iterator var="localizedLang" value="langs"
						status="langStatusVar">
						<div id="<s:property value="#localizedLang.code" />_tab"
							class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>">
							<div class="form-group">
								<div class="col-xs-12">
									<label class="col-sm-2 control-label"
										for="choice-<s:property value="#localizedLang.code" />">
										<s:if test="choice.questionnaire">
											<s:text name="label.answer" />
										</s:if> <s:else>
											<s:text name="jpsurvey_choice" />
										</s:else>
									</label>
									<s:set var="localization"
										value="%{getChoices()[#localizedLang.code]}" />
									<div class="col-sm-10">
										<wpsf:textarea cssClass="form-control"
											name="%{'choice-'+#localizedLang.code}"
											id="%{'choice-'+#localizedLang.code}"
											value="%{#localization}" cols="60" rows="3" />
									</div>
								</div>
							</div>
						</div>
					</s:iterator>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>