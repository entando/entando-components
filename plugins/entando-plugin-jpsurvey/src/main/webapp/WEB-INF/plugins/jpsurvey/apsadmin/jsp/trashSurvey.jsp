<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
        <s:if test="survey.questionnaire">
        <li>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>"
               title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text
                    name="title.jpsurvey.survey.main" />
            </a>
        </li>
    </s:if>
    <s:else>
        <li>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>"
               title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text
                    name="title.jpsurvey.poll.main" />
            </a>
        </li>
    </s:else>
    <s:if test="survey.questionnaire">
        <li class="page-title-container">
            <s:text name="title.jpsurvey.trash.survey" />
        </li>
        <s:set var="surveyType"
               value="%{getText('message.jpsurvey.survey.type')}" />
    </s:if>
    <s:else>
        <li class="page-title-container">
            <s:text name="title.jpsurvey.trash.poll" />
        </li>
        <s:set var="surveyType" value="%{getText('message.jpsurvey.poll.type')}" />
    </s:else>
</ol>

<h1>
    <s:if test="survey.questionnaire">
        <s:text name="title.jpsurvey.trash.survey" />
        <s:set var="surveyType" value="%{getText('message.jpsurvey.survey.type')}" />
    </s:if>
    <s:else>
        <s:text name="title.jpsurvey.trash.poll" />
        <s:set var="surveyType" value="%{getText('message.jpsurvey.poll.type')}" />
    </s:else>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
    <s:form cssClass="form-horizontal">
        <p class="noscreen">
            <s:hidden name="questionnaire" value="%{survey.questionnaire}" />
            <s:hidden name="surveyId" />
        </p>
        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="message.surveyAction.deleteWarning" />
        </p>
        <p class="esclamation-underline-text">
            <s:text name="jpsurvey_delete_confirm" />
            &#32; <em><s:property value="%{getLabel(survey.titles)}" /></em>&#32;?&#32;
        </p>
        <div class="text-center margin-large-top">
            <a class="btn btn-default"
               href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey"><s:param name="questionnaire" value="survey.questionnaire"/></s:url>">
                <s:if test="survey.questionnaire">
                    <s:text name="title.jpsurvey.survey.main" />
                </s:if> <s:else>
                    <s:text name="title.jpsurvey.poll.main" />
                </s:else>
            </a>
            <wpsf:submit type="button" action="deleteSurvey"
                         cssClass="btn btn-danger button-fixed-width">
                <s:text name="%{getText('label.jpsurvey.delete')}" />
            </wpsf:submit>
        </div>
    </s:form>
</div>
