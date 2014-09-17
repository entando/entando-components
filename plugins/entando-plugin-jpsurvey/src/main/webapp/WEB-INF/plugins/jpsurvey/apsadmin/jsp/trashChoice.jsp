<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="choice.questionnaire">
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="choice.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
        </s:if>
        <s:else>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="choice.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
        </s:else>&#32;/&#32;
        <s:text name="title.jpsurvey.question.edit" />
        &#32;/&#32;
        <s:text name="title.jpsurvey.trash.choice" />
    </span>
</h1>
<div id="main">
    <div class="alert alert-warning">
        <s:text name="label.workingOn" />:&#32;
        <em><s:property value="%{getLabel(choice.surveyTitles)}"/></em>

        <s:form action="deleteChoice" class="form-horizontal">
            <s:text name="jpsurvey_delete_confirm" />&#32;

            <s:if test="!choice.freeText">
                <s:text name="jpsurvey_the_answer" />:&#32;
                <em class="important"><s:property value="%{getLabel(choice.choices)}"/></em>
            </s:if>

            <s:else>
                <span class="important"><s:text name="jpsurvey_the_free_text" /></span>
            </s:else>

            &#32;<s:text name="jpsurvey_delete_confirm_from" />&#32;
            <em><s:property value="%{getLabel(choice.questions)}"/></em>?&#32; 
            <p class="noscreen">
                <s:hidden name="questionId" value="%{choice.questionId}" />
                <s:hidden name="choiceId" />
                <%-- 
                <s:hidden name="questionnaire" />
                <s:hidden name="surveyId" />
                <s:hidden name="deletingElement" />
                --%>
            </p>

            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;    
                    <s:text name="%{getText('label.jpsurvey.delete')}"/>
                </wpsf:submit>
                <p class="text-center margin-small-top">
                    <s:text name="jpsurvey_delete_choice_go_back" />&#32;
                    <a href="<s:url action="editQuestion" namespace="/do/jpsurvey/Survey"><s:param name="questionId" value="choice.questionId" /></s:url>">
                        <s:text name="title.jpsurvey.question.edit" />
                    </a>
                </p>
            </div>
        </s:form>
    </div>
</div>