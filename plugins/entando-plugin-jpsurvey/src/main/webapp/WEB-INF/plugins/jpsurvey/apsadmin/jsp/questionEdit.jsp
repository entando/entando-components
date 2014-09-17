<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="question.questionnaire">
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
            <s:set name="titleSurveyQuestionnary" value="QUESTIONARIO_LABEL"/>
        </s:if>
        <s:else>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
            <s:set name="titleSurveyQuestionnary" value="SONDAGGIO_LABEL"/>
        </s:else>&#32;/&#32;
        <s:if test="getStrutsAction() == 2">
            <s:text name="title.jpsurvey.question.edit" />
        </s:if>
        <s:if test="getStrutsAction() == 1">
            <s:text name="title.jpsurvey.question.new" />
            <s:set name="isModifiable" value="true"/>
        </s:if>
    </span>
</h1>

<div id="main">

    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="label.workingOn" />:&#32;
            <em><s:property value="%{getLabel(question.surveyTitles)}" /></em>
        </div>
    </div>

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
        </div>
    </s:if>

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h3>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li><s:property /></li>
                        </s:iterator>
                </ul>
        </div>
    </s:if>

    <s:form cssClass="tab-container" >

        <p class="noscreen">
            <s:hidden name="strutsAction" />
            <s:if test="question != null">
                <s:hidden name="questionnaire" value="%{question.questionnaire}"/>
                <s:hidden name="surveyId" value="%{question.surveyId}"/>
            </s:if>
            <s:else> <%-- NEL CASO SIANO EREDITATI DA QUALCHE PARTE PROVARE A LEVARE!--%>
                <s:hidden name="questionnaire" />
                <s:hidden name="surveyId" />
            </s:else>
            <s:if test="getStrutsAction() == 2">
                <s:hidden name="questionId" />
            </s:if>
        </p>
        <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
            <s:iterator value="langs" id="lang" status="langStatusVar">
                <li <s:if test="#langStatusVar.first"> class="active" </s:if>>
                    <a data-toggle="tab" href="#<s:property value="#lang.code" />_tab"><s:property value="#lang.descr" /></a>
                </li>
                </s:iterator>
        </ul>
        <div class="panel panel-default" id="tab-container">
            <div class="panel-body">
                <div class="tab-content">
                    <s:iterator id="cyclingLang" value="langs" status="langStatusVar">
                        <div id="<s:property value="#cyclingLang.code" />_tab" class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>">
                            <p>
                                <label for="question-<s:property value="#lang.code" />"><s:text name="jpsurvey_question" /></label>
                                <s:set name="localization" value="%{getQuestions()[#cyclingLang.code]}"/>
                                <wpsf:textarea name="%{'question-'+#cyclingLang.code}" id="%{'question-'+#cyclingLang.code}" value="%{#localization}" cssClass="form-control" cols="60" rows="3" />
                            </p>
                        </div>
                    </s:iterator>
                </div>
            </div>
        </div>

    <s:if test="%{strutsAction == 2}">
        <div class="form-group">
            <label for="singleChoice"><s:text name="jpsurvey_singleChoice" /></label>
            <select name="singleChoice" id="singleChoice" tabindex="<wpsa:counter />" class="form-control">
                <option value="1" <s:if test="%{singleChoice == null || singleChoice > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isSingleChoice" /></option>
                <option value="0" <s:if test="%{singleChoice == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notSingleChoice" /></option>
                </select>
            </div>

            <fieldset class="col-xs-12"> 
                <legend><s:text name="jpsurvey_multipleChoice_configuration" /></legend>
            <div class="form-group">
                <label for="minResponseNumber"><s:text name="jpsurvey_minResponseNumber" /></label>
                <wpsf:textfield name="minResponseNumber" id="minResponseNumber" cssClass="form-control" />
            </div>
            <div class="form-group">
                <label for="maxResponseNumber"><s:text name="jpsurvey_maxResponseNumber" /></label>
                <wpsf:textfield name="maxResponseNumber" id="maxResponseNumber" cssClass="form-control" />
            </div>
        </fieldset>

        <s:if test="question.choices.isEmpty"> 
            <div class="alert alert-info"><s:text name="jpsurvey.noAnswers" /></div>
        </s:if>
        <s:else>
            <div class="tab-separation">
                <table class="table table-bordered">
                    <tr>
                        <th class="text-center"><abbr title="<s:text name="jpsurvey_answersTable_actions" />">-</abbr></th>
                        <th><s:text name="jpsurvey_answersTable_answers" /></th>
                            <s:if test="%{!#isModifiable}">
                            <th class="text-right"><s:text name="jpsurvey_answersTable_occurences"></s:text></th>
                            </s:if>
                    </tr>
                    <%-- STAMPA LE OPZIONI DISPONIBILI STAMPATE NELLA LINGUA DEL BACKEND--%>
                    <s:iterator id="currentChoice" value="question.choices" status="status">
                        <tr>
                            <s:set name="currentStat" value="choiceStats[#currentChoice.id]" />
                            <s:if test="%{#currentChoice.freeText== false}">
                                <s:set name="localizedString" value="%{getText('jpsurvey_freeText')}" />
                            </s:if>
                            <s:else> 
                                <s:set name="localizedString" value="%{getText('jpsurvey_freeText')}" />
                            </s:else>
                            <%-- actions: up/down/delete --%>
                            <td class="text-center">
                                <div class="btn-group btn-group-xs">
                                <s:if test="%{#currentChoice.freeText== false}">
                                    <a class="btn btn-default" href="<s:url action="editChoice">
                                           <s:param name="choiceId" value="#currentChoice.id"/>
                                           <s:param name="strutsAction" value="2"/>
                                       </s:url>" title="<s:text name="label.edit" />: <s:property value="%{getLabel(#currentChoice.choices)}"/>" >
                                        <span class="icon fa fa-pencil-square-o"></span>   
                                    </a>
                                </s:if>
                                    <s:else>
                                        <button class="btn btn-default disabled">
                                            <span class="icon fa fa-pencil-square-o"></span>   
                                        </button> 
                                    </s:else>
                                </div>
                                <div class="btn-group btn-group-xs">
                                    
                                <a class="btn btn-default" href="<s:url action="moveChoiceUp">
                                       <s:param name="choiceId" value="#currentChoice.id"/>
                                       <s:param name="questionId" value="question.id"/>
                                   </s:url>" 
                                   title="<s:text name="label.moveUp" />:&#32;<s:if test="%{#currentChoice.freeText== false}"><s:property value="%{getLabel(#currentChoice.choices)}"/></s:if><s:else><s:property value="#localizedString"/></s:else>">
                                    <span class="icon fa fa-sort-desc"></span>
                                    <span class="sr-only"><s:text name="label.moveUp" /></span>
                                </a>
                            <a class="btn btn-default" href="<s:url action="moveChoiceDown">
                                   <s:param name="choiceId" value="#currentChoice.id"/>
                                   <s:param name="questionId" value="question.id"/>
                               </s:url>" 
                               title="<s:text name="label.moveDown" />:&#32;<s:if test="%{#currentChoice.freeText== false}"><s:property value="%{getLabel(#currentChoice.choices)}"/></s:if><s:else><s:property value="#localizedString"/></s:else>"> 
                                    <span class="icon fa fa-sort-asc"></span>
                                    <span class="sr-only"><s:text name="label.moveDown" /></span>
                            </a>
                                </div>
                            <div class="btn-group btn-group-xs">
                            <a class="btn btn-warning btn-xs" 
                               href="<s:url action="trashChoice">
                                   <s:param name="choiceId" value="#currentChoice.id"/>
                               </s:url>" title="<s:text name="label.remove" />:&#32;<s:if test="%{#currentChoice.freeText== false}"><s:property value="%{getLabel(#currentChoice.choices)}"/></s:if><s:else><s:property value="#localizedString"/></s:else>">
                                    <span class="icon fa fa-times-circle-o"></span>
                                    <span class="sr-only"><s:text name="label.remove" /></span>                                
                            </a>
                            </div>
                            </td>
                            <s:if test="%{#currentChoice.freeText== false}">
                                <%-- possible answer column --%>
                                <td>
                                    <!-- OTTIENE LA STRINGA LOCALIZZATA -->
                                    <%-- cancellare
                                    <s:set name="localizedString" value="#currentChoice.choices[currentLang.code]" />
                                    --%>
                                        <s:property value="%{getLabel(#currentChoice.choices)}"/>
                                    </a>
                                </td>
                            </s:if>
                            <s:else> 
                                <%-- possible answer column --%>
                                <td><s:text name="jpsurvey_freeText" /></td>
                            </s:else>

                            <%-- Number of occurrences colums --%>
                            <td class="text-right">
                                <s:if test="%{#currentStat == '' || #currentStat==null}">
                                    0
                                </s:if>
                                <s:else>
                                    <%--<s:if test="%{#currentChoice.freeText== true}">
                                            <a href="<s:url action="freeTextListEntry"><s:param name="choiceId" value="#currentChoice.id"/><s:param name="questionId" value="%{getQuestionId()}"/><s:param name="surveyId" value="%{getSurveyId()}"/><s:param name="questionnaire" value="questionnaire"/><s:param name="strutsAction" value="2"/></s:url>" title="<s:text name="label.view.freeText" />" >
                                                    <s:property value="#currentStat" />
                                            </a>
                                    </s:if>
                                    <s:else>--%>
                                    <s:property value="#currentStat" />
                                    <%--</s:else>--%>
                                </s:else>
                            </td>
                            </tr>
                    </s:iterator>
                </table>
            </div>
        </s:else>
    </s:if>

    <s:if test="%{questionId != null && questionId != ''}">
        <div class="form-group">
        <div class="btn-group">
            <wpsa:actionParam action="addChoice" var="addChoiceAction">
                <wpsa:actionSubParam name="overrideResponseNumberFieldsCheck" value="true"></wpsa:actionSubParam>
            </wpsa:actionParam>
            <wpsf:submit type="button" action="%{#addChoiceAction}" cssClass="btn btn-default">
                <span class="icon fa fa-plus-circle"></span>&#32;
                <s:text name="%{getText('jpsurvey_new_choice')}"/>
            </wpsf:submit>
            <s:if test="question.questionnaire">
                <wpsa:actionParam action="addFreeText" var="addFreeTextAction">
                    <wpsa:actionSubParam name="overrideResponseNumberFieldsCheck" value="true"></wpsa:actionSubParam>
                </wpsa:actionParam>
                <wpsf:submit type="button" action="%{#addFreeTextAction}" value="%{getText('jpsurvey_new_freeText')}" cssClass="btn btn-default" />
            </s:if>
        </div>
        </div>
    </s:if>
    <div class="form-horizontal">
        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" action="saveQuestion" cssClass="btn btn-primary btn-block">
                    <span class="icon fa fa-floppy-o"></span>&#32;
                    <s:text name="%{getText('label.save')}"/>
                </wpsf:submit>
            </div>
        </div>
    </div>
</s:form>
</div>