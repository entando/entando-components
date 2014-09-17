<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set name="currentLang" value="%{getCurrentLang().code}" />
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="listSurveys" >
               <s:param name="questionnaire" value="questionnaire"></s:param>
           </s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />">
            <s:text name="title.jpsurvey.survey.main" />
        </a>&#32;/&#32;<s:text name="jpsurvey_freeText_results" />	
    </span>
</h1>

<div id="main">
    <s:if test="%{freeTextMap != NULL && freeTextMap.size() > 0}">
        <table class="table table-bordered">
            <tr>
                <th><s:text name="jpsurvey_freeText_answers" /></th>
                <th class="text-right"><s:text name="jpsurvey_freeText_occurences" /></th>		
            </tr>
            <s:iterator id="currentFreeText" value="freeTextMap">
                <tr>
                    <td><s:property value="#currentFreeText.key" /></td>
                    <td class="text-right"><s:property value="#currentFreeText.value" /></td>
                </tr>
            </s:iterator>
        </table>
    </s:if>
    <s:else>
        <p><s:text name="jpsurvey_freeText_noAnswers" /></p>
    </s:else>
</div>

<%-- FARE UN BOTTONE / LINK INDIETRO? 
<s:hidden name="choiceId" />
<s:hidden name="questionId" />
<s:hidden name="surveyId" />
<s:hidden name="questionnaire" />
--%>