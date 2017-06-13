<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
        <s:if test="questionnaire">
        <li><a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="questionnaire"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text
                    name="title.jpsurvey.survey.main" /></a></li>
            </s:if>
            <s:else>
        <li><a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="questionnaire"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text
                    name="title.jpsurvey.poll.main" /></a></li>
            </s:else>
    <li class="page-title-container"><s:text
            name="title.jpsurvey.survey.results" /></li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="title.jpsurvey.survey.results" />
                <span class="pull-right"> <a tabindex="0" role="button"
                                             data-toggle="popover" data-trigger="focus" data-html="true"
                                             title=""
                                             data-content="<s:text name="title.jpsurvey.survey.results.help" />"
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
<div id="main">
    <table class="table table-bordered table-hover no-mb">
        <tr>
            <th class="text-right col-sm-2"><s:text name="jpsurvey_title" /></th>
            <td class="col-sm-10"><s:property
                    value="%{getLabel(survey.titles)}" /></td>
        </tr>
        <tr>
            <th class="text-right col-sm-2"><s:text
                    name="jpsurvey_description" /></th>
            <td class="col-sm-10"><s:property
                    value="%{getLabel(survey.descriptions)}" /></td>
        </tr>
        <tr>
            <th class="text-right col-sm-2"><s:text name="jpsurvey_voters" /></th>
            <td class="col-sm-10"><s:property value="%{totalVoters}" /></td>
        </tr>
    </table>
    <s:iterator var="question" value="survey.questions"
                status="questionIndex">
        <div class="subsection-light">
            <s:set var="occurrences"
                   value="%{getQuestionStatistics(#question.id)}" />
            <h2>
                <s:text name="jpsurvey_question" />
                &#32;
                <s:property value="#questionIndex.index + 1" />
                .
                <s:property value="%{getLabel(#question.questions)}" />
            </h2>
            <h3>
                <s:text name="jpsurvey_answers" />
                :
            </h3>
            <ol>
                <s:iterator var="choice" value="#question.choices"
                            status="rowstatus">
                    <li><s:if test="#choice.freeText">
                            <s:set var="occurrence" value="#occurrences[#choice.id]" />
                            <s:if test="%{#occurrence != null && #occurrence != ''}">
                                <a
                                    href="<s:url action="freeTextListEntry"><s:param name="choiceId" value="#choice.id"/><s:param name="questionId" value="%{#question.id}"/><s:param name="surveyId" value="%{survey.id}"/><s:param name="questionnaire" value="questionnaire"/><s:param name="strutsAction" value="2"/></s:url>"
                                    title="<s:text name="label.view.freeText" />"> <s:text
                                        name="jpsurvey_freeText" />
                                </a>
                            </s:if>
                            <s:else>
                                <s:text name="jpsurvey_freeText" />
                            </s:else>
                        </s:if> <s:else>
                            <s:property value="%{getLabel(#choice.choices)}" />
                        </s:else></li>
                    </s:iterator>
            </ol>
            <div class="graphic">
                <dl class="graph">
                    <s:iterator var="choice" value="#question.choices"
                                status="rowstatus">
                        <s:if test="#choice.freeText">
                            <dt>
                                <s:text name="jpsurvey_freeText" />
                            </dt>
                        </s:if>
                        <s:else>
                            <dt>
                                <s:property value="%{getLabel(#choice.choices)}" />
                            </dt>
                        </s:else>
                        <s:set var="occurrence" value="#occurrences[#choice.id]" />
                        <s:set var="roundedPercentage"
                               value="%{getChoicePercentage(#occurrences, #choice.id)}" />

                        <s:if test="#occurrence && (#roundedPercentage > 20)">
                            <dd>
                                <span
                                    class="p<s:text name="format.roundedPercentage"><s:param name="value" value="#roundedPercentage"/></s:text>">
                                    <span class="noscreen"><s:text
                                            name="jpsurvey.answer.number" /></span>&#32;<em class="leftEm"><s:property
                                            value="#rowstatus.index + 1" /></em>&#32;<span class="noscreen"><s:text
                                            name="jpsurvey.obtained" /></span>&#32;<em class="rightEm"><s:property
                                            value="#occurrence" />&#32;<s:text name="jpsurvey.votes" /></em>
                                </span>
                            </dd>
                        </s:if>
                        <s:elseif test="#occurrence && (#roundedPercentage < 20)">
                            <dd>
                                <span
                                    class="p<s:text name="format.roundedPercentage"><s:param name="value" value="#roundedPercentage"/></s:text>">
                                    <span class="noscreen"><s:text
                                            name="jpsurvey.answer.number" /></span>&#32;<em class="leftEm"><s:property
                                            value="#rowstatus.index + 1" /></em>&#32;<span class="noscreen"><s:text
                                            name="jpsurvey.obtained" /></span>
                                </span>&#32;<em class="rightEmZero"><s:property
                                        value="#occurrence" />&#32;<s:text name="jpsurvey.votes" /></em>
                            </dd>
                        </s:elseif>
                        <s:else>
                            <dd>
                                <span class="p0"><span class="noscreen"><s:text
                                            name="jpsurvey.answer.number" /></span>&#32;<em class="leftEm"><s:property
                                            value="#rowstatus.index + 1" /></em></span>&#32;<span class="noscreen"><s:text
                                        name="jpsurvey.obtained" /></span>&#32;<em class="rightEmZero">0
                                    &#32;<s:text name="jpsurvey.votes" />
                                </em>
                            </dd>
                        </s:else>
                    </s:iterator>
                </dl>
            </div>
        </div>
    </s:iterator>
</div>
