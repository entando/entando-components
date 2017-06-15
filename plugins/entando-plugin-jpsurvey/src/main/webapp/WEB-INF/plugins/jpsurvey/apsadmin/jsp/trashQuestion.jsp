<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
        <s:if test="question.questionnaire">
        <li><a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />">
                <s:text name="title.jpsurvey.survey.main" />
            </a></li>
        <li><s:text name="title.jpsurvey.survey.edit" /></li>
        </s:if>
        <s:else>
        <li><a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />">
                <s:text name="title.jpsurvey.poll.main" />
            </a></li>
        <li><s:text name="title.jpsurvey.poll.edit" /></li>
        </s:else>
    <li class="page-title-container"><s:text
            name="title.jpsurvey.trash.question" /></li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.jpsurvey.trash.question" />
                <span class="pull-right"> <a tabindex="0" role="button"
                                             data-toggle="popover" data-trigger="focus" data-html="true"
                                             title=""
                                             data-content="<s:text name="title.jpsurvey.trash.question.help" />"
                                             data-placement="left" data-original-title=""> <i
                            class="fa fa-question-circle-o" aria-hidden="true"></i>
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
    <p>
        <s:text name="label.workingOn" />
        :&#32;<em><s:property value="%{getLabel(question.surveyTitles)}" /></em>
    </p>
    <s:form action="deleteQuestion" cssClass="form-horizontal">
        <p class="noscreen">
            <s:hidden name="surveyId" />
            <s:hidden name="questionId" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="label.delete" />
        </p>
        <p class="esclamation-underline-text">
            <s:text name="message.questionAction.deleteWarning" />
            <s:text name="jpsurvey_delete_confirm" />
            &#32;
            <s:text name="jpsurvey_the_question" />
            &#32; <em><s:property value="%{getLabel(question.questions)}" /></em>
            <%-- &#32;(<s:text name="jpsurvey_id" /><s:property value="%{questionId}" />)&#32; --%>
            <s:text name="jpsurvey_delete_confirm_from" />
            &#32; <em><s:property value="%{getLabel(question.surveyTitles)}" /></em>?&#32;
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default"
               href="<s:url action="editSurvey" namespace="/do/jpsurvey/Survey"><s:param name="surveyId" value="surveyId" /></s:url>">
                <s:if test="question.questionnaire">
                    <s:text name="title.jpsurvey.survey.edit" />
                </s:if> <s:else>
                    <s:text name="title.jpsurvey.poll.edit" />
                </s:else>
            </a>
            <wpsf:submit type="button"
                         cssClass="btn btn-danger button-fixed-width">
                <s:text name="%{getText('label.jpsurvey.delete')}" />
            </wpsf:submit>
        </div>
    </s:form>
</div>
