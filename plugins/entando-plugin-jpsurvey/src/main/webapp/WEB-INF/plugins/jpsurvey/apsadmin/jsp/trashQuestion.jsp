<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="question.questionnaire">
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />">
                <s:text name="title.jpsurvey.survey.main" />
            </a>&#32;/&#32;
            <s:text name="title.jpsurvey.survey.edit" />
        </s:if>
        <s:else>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />">
                <s:text name="title.jpsurvey.poll.main" />
            </a>
            <s:text name="title.jpsurvey.poll.edit" />
        </s:else>
        &#32;/&#32;
        <s:text name="title.jpsurvey.trash.question" />
    </span>
</h1>
<div id="main">

    <div class="alert alert-warning">      

        <s:form action="deleteQuestion" cssClass="form-horizontal"> 
            <!-- HIDDEN --> 
            <p class="noscreen">
                <s:hidden name="surveyId" />
                <s:hidden name="questionId" />
            </p>

            <p><s:text name="label.workingOn" />:&#32;<em><s:property value="%{getLabel(question.surveyTitles)}" /></em></p>
            <%-- warning message --%>
            <p><s:text name="message.questionAction.deleteWarning" /></p>

            <%-- do you really want to delete? --%>
            <p>
                <s:text name="jpsurvey_delete_confirm" />&#32;<s:text name="jpsurvey_the_question" />&#32;
                <em><s:property value="%{getLabel(question.questions)}"/></em>
                <%-- &#32;(<s:text name="jpsurvey_id" /><s:property value="%{questionId}" />)&#32; --%>
                <s:text name="jpsurvey_delete_confirm_from" />&#32;
                <em><s:property value="%{getLabel(question.surveyTitles)}"/></em>?&#32;
            </p>
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;    
                    <s:text name="%{getText('label.jpsurvey.delete')}"/>
                </wpsf:submit>

                <%-- back link to survey edit --%>
                <p class="text-center margin-small-top">
                    <s:text name="jpsurvey_delete_go_back" />&#32;
                    <a href="<s:url action="editSurvey" namespace="/do/jpsurvey/Survey"><s:param name="surveyId" value="surveyId" /></s:url>">
                        <s:if test="question.questionnaire"><s:text name="title.jpsurvey.survey.edit" /></s:if><s:else><s:text name="title.jpsurvey.poll.edit" /></s:else>
                        </a>
                    </p>
                </div>
        </s:form>
    </div>
</div>