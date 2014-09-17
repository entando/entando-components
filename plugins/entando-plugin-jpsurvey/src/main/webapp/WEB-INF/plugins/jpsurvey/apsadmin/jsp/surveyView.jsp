<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="questionnaire">
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
        </s:if>
        <s:else>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
        </s:else>
        &#32;/&#32;<s:text name="title.jpsurvey.survey.results" />
    </span>
</h1>
<div id="main">
    <table class="table table-bordered">
        <tr>
            <th class="text-right"><s:text name="jpsurvey_title" /></th>
            <td><s:property value="%{getLabel(survey.titles)}" /></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="jpsurvey_description" /></th>
            <td><s:property value="%{getLabel(survey.descriptions)}" /></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="jpsurvey_voters" /></th>
            <td><s:property value="%{totalVoters}" /></td>
        </tr>
    </table>
    <s:iterator var="question" value="survey.questions" status="questionIndex">
        <div class="subsection-light">
            <s:set name="occurrences" value="%{getQuestionStatistics(#question.id)}" />
            <h2><s:text name="jpsurvey_question"/>&#32;<s:property value="#questionIndex.index + 1"/>. <s:property value="%{getLabel(#question.questions)}" /></h2>
            <h3><s:text name="jpsurvey_answers" />:</h3>
            <ol>	
                <s:iterator id="choice" value="#question.choices" status="rowstatus" >
                    <li>
                        <s:if test="#choice.freeText">
                            <s:set name="occurrence" value="#occurrences[#choice.id]" />
                            <s:if test="%{#occurrence != null && #occurrence != ''}">
                                <a href="<s:url action="freeTextListEntry"><s:param name="choiceId" value="#choice.id"/><s:param name="questionId" value="%{#question.id}"/><s:param name="surveyId" value="%{survey.id}"/><s:param name="questionnaire" value="questionnaire"/><s:param name="strutsAction" value="2"/></s:url>" title="<s:text name="label.view.freeText" />" >
                                    <s:text name="jpsurvey_freeText" />
                                </a>
                            </s:if>
                            <s:else>
                                <s:text name="jpsurvey_freeText" />
                            </s:else>
                        </s:if>
                        <s:else>
                            <s:property value="%{getLabel(#choice.choices)}" />
                        </s:else>
                    </li>
                </s:iterator>
            </ol>
            <div class="graphic">
                <dl class="graph">		
                    <s:iterator id="choice" value="#question.choices" status="rowstatus" >
                        <s:if test="#choice.freeText">
                            <dt><s:text name="jpsurvey_freeText" /></dt>
                        </s:if>
                        <s:else>
                            <dt><s:property value="%{getLabel(#choice.choices)}" /></dt>
                        </s:else>
                        <s:set name="occurrence" value="#occurrences[#choice.id]" />
                        <s:set name="roundedPercentage" value="%{getChoicePercentage(#occurrences, #choice.id)}" />

                        <s:if test="#occurrence && (#roundedPercentage > 20)">
                            <dd>
                                <span class="p<s:text name="format.roundedPercentage"><s:param name="value" value="#roundedPercentage"/></s:text>">
                                    <span class="noscreen"><s:text name="jpsurvey.answer.number" /></span>&#32;<em class="leftEm"><s:property value="#rowstatus.index + 1"/></em>&#32;<span class="noscreen"><s:text name="jpsurvey.obtained" /></span>&#32;<em class="rightEm"><s:property value="#occurrence" />&#32;<s:text name="jpsurvey.votes" /></em></span>
                            </dd>
                        </s:if>
                        <s:elseif test="#occurrence && (#roundedPercentage < 20)">
                            <dd>
                                <span class="p<s:text name="format.roundedPercentage"><s:param name="value" value="#roundedPercentage"/></s:text>">
                                    <span class="noscreen"><s:text name="jpsurvey.answer.number" /></span>&#32;<em class="leftEm"><s:property value="#rowstatus.index + 1"/></em>&#32;<span class="noscreen"><s:text name="jpsurvey.obtained" /></span></span>&#32;<em class="rightEmZero"><s:property value="#occurrence" />&#32;<s:text name="jpsurvey.votes" /></em>
                            </dd>
                        </s:elseif>
                        <s:else>
                            <dd>
                                <span class="p0"><span class="noscreen"><s:text name="jpsurvey.answer.number" /></span>&#32;<em class="leftEm"><s:property value="#rowstatus.index + 1"/></em></span>&#32;<span class="noscreen"><s:text name="jpsurvey.obtained" /></span>&#32;<em class="rightEmZero">0 &#32;<s:text name="jpsurvey.votes" /></em>
                            </dd>
                        </s:else>
                    </s:iterator>
                </dl>
            </div>
        </div>
    </s:iterator>
</div>