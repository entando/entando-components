<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="survey.questionnaire">
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
        </s:if>
        <s:else>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
        </s:else>&#32;/&#32;
        <s:if test="survey.questionnaire">
            <s:text name="title.jpsurvey.trash.survey" />
            <s:set name="surveyType" value="%{getText('message.jpsurvey.survey.type')}"/>
        </s:if>

        <s:else>
            <s:text name="title.jpsurvey.trash.poll" />
            <s:set name="surveyType" value="%{getText('message.jpsurvey.poll.type')}"/>
        </s:else>
    </span>
</h1>
<div id="main">

    <s:form>
        <p class="noscreen">
            <s:hidden name="questionnaire" value="%{survey.questionnaire}"/>
            <s:hidden name="surveyId" />
        </p>
        <%-- FINE HIDDEN --%>

        <div class="alert alert-warning">
            <p><s:text name="message.surveyAction.deleteWarning" /></p>
            <p>
                <s:text name="jpsurvey_delete_confirm" />&#32;
                <%-- <s:property value="%{surveyType}" />&#32; --%>
                <em><s:property value="%{getLabel(survey.titles)}"/></em>&#32;?&#32;
                <%-- (<s:text name="jpsurvey_id" />&#32;<s:property value="%{surveyId}" />)? --%>
            </p>
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" action="deleteSurvey" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;    
                    <s:text name="%{getText('label.jpsurvey.delete')}"/>
                </wpsf:submit>
                <p class="text-center margin-small-top">
                    <s:text name="jpsurvey_delete_survey_go_back" />&#32;
                    <a class="btn btn-link" href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey"><s:param name="questionnaire" value="survey.questionnaire"/></s:url>">
                        <s:if test="survey.questionnaire">
                            <s:text name="title.jpsurvey.survey.main" />
                        </s:if>
                        <s:else>
                            <s:text name="title.jpsurvey.poll.main" />
                        </s:else>
                    </a>
                </p>
            </div>
        </div>
    </s:form>
</div>