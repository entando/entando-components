<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
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

<h1>
    <s:text name="title.jpsurvey.trash.choice" />
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<s:text name="label.workingOn" />
:&#32; <em><s:property value="%{getLabel(choice.surveyTitles)}" /></em>

<div class="text-center">
    <s:form action="deleteChoice" cssClass="form-horizontal">
        <p class="noscreen">
            <s:hidden name="questionId" value="%{choice.questionId}" />
            <s:hidden name="choiceId" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="jpsurvey_delete_confirm" />
        </p>
        <p class="esclamation-underline-text">
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
            <a class="btn btn-default"
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
