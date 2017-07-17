<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
        <s:if test="question.questionnaire">
        <li>
            <a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />">
                <s:text name="title.jpsurvey.survey.main" /></a></li>
                <s:set var="titleSurveyQuestionnary" value="QUESTIONARIO_LABEL" />
            </s:if>
            <s:else>
        <li>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>"
               title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />">
                <s:text name="title.jpsurvey.poll.main" />
            </a>
        </li>
        <s:set var="titleSurveyQuestionnary" value="SONDAGGIO_LABEL" />
    </s:else>
    <s:if test="getStrutsAction() == 2">
        <li class="page-title-container">
            <s:text name="title.jpsurvey.question.edit" />
        </li>
    </s:if>
    <s:if test="getStrutsAction() == 1">
        <li class="page-title-container">
            <s:text name="title.jpsurvey.question.new" />
        </li>
        <s:set var="isModifiable" value="true" />
    </s:if>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:if test="getStrutsAction() == 2">
                    <s:text name="title.jpsurvey.question.edit" />
                    <span class="pull-right"> 
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                           title=""  data-content="<s:text name="title.jpsurvey.question.edit.help" />"
                           data-placement="left" data-original-title=""> 
                            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                        </a>
                    </s:if>
                    <s:if test="getStrutsAction() == 1">
                        <s:text name="title.jpsurvey.question.new" />
                        <span class="pull-right">
                            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                               title=""  data-content="<s:text name="title.jpsurvey.question.new.help" />"
                               data-placement="left" data-original-title=""> 
                                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                            </a> 
                            <s:set var="isModifiable" value="true" />
                        </s:if>
                    </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <s:if test="questionnaire">
                        <li class="active">
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
                                <s:text  name="title.jpsurvey.survey.main" />
                            </a>
                        </li>
                    </s:if>
                    <s:else>
                        <li>
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>">
                                <s:text  name="title.jpsurvey.survey.main" />
                            </a>
                        </li>
                    </s:else>
                    <s:if test="questionnaire">
                        <li>
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
                                <s:text name="title.jpsurvey.poll.main" />
                            </a>
                        </li>
                    </s:if>
                    <s:else>
                        <li class="active">
                            <a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>">
                                <s:text name="title.jpsurvey.poll.main" />
                            </a>
                        </li>
                    </s:else>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div class="mb-20">
    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="label.workingOn" />
            :&#32; 
            <em>
                <s:property value="%{getLabel(question.surveyTitles)}" />
            </em>
        </div>
    </div>
    <s:form cssClass="form-horizontal">
        <p class="noscreen">
            <s:hidden name="strutsAction" />
            <s:if test="question != null">
                <s:hidden name="questionnaire" value="%{question.questionnaire}" />
                <s:hidden name="surveyId" value="%{question.surveyId}" />
            </s:if>
            <s:else>
                <s:hidden name="questionnaire" />
                <s:hidden name="surveyId" />
            </s:else>
            <s:if test="getStrutsAction() == 2">
                <s:hidden name="questionId" />
            </s:if>
        </p>
        <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
            <s:iterator value="langs" var="lang" status="langStatusVar">
                <li <s:if test="#langStatusVar.first"> class="active" </s:if>>
                    <a data-toggle="tab" href="#<s:property value="#lang.code" />_tab"><s:property
                            value="#lang.descr" /></a>
                </li>
            </s:iterator>
        </ul>
        <fieldset>
            <div class="panel" id="tab-container">
                <div class="panel-body" style="padding: 26px 0 0 0">
                    <div class="tab-content">
                        <s:iterator var="cyclingLang" value="langs" status="langStatusVar">
                            <div id="<s:property value="#cyclingLang.code" />_tab"
                                 class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>">
                                <label class="col-sm-2 control-label" for="question-<s:property value="#lang.code" />">
                                    <s:text name="jpsurvey_question" />
                                </label>
                                <s:set var="localization" value="%{getQuestions()[#cyclingLang.code]}" />
                                <div class="col-sm-10">
                                    <wpsf:textarea name="%{'question-'+#cyclingLang.code}" id="%{'question-'+#cyclingLang.code}" value="%{#localization}" cssClass="form-control" cols="60" rows="3" />
                                </div>
                            </div>
                        </s:iterator>
                    </div>
                </div>
            </div>
        </fieldset>

        <s:if test="%{strutsAction == 2}">
            <fieldset class="col-xs-12">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="singleChoice">
                        <s:text name="jpsurvey_singleChoice" />
                    </label>
                    <div class="col-sm-10">
                        <select name="singleChoice" id="singleChoice"
                                tabindex="<wpsa:counter />" class="form-control">
                            <option value="1" <s:if test="%{singleChoice == null || singleChoice > 0}">selected="selected"</s:if>>
                                <s:text name="jpsurvey_isSingleChoice" />
                            </option>
                            <option value="0" <s:if test="%{singleChoice == 0}">selected="selected"</s:if>>
                                <s:text name="jpsurvey_notSingleChoice" />
                            </option>
                        </select>
                    </div>
                    <br>
                </div>
                <br>
            </fieldset>

            <fieldset class="col-xs-12">
                <legend>
                    <s:text name="jpsurvey_multipleChoice_configuration" />
                </legend>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="minResponseNumber">
                        <s:text  name="jpsurvey_minResponseNumber" />
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textfield name="minResponseNumber" id="minResponseNumber" cssClass="form-control" />
                    </div>
                    <br>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="maxResponseNumber">
                        <s:text name="jpsurvey_maxResponseNumber" />
                    </label>
                    <div class="col-sm-10">
                        <wpsf:textfield name="maxResponseNumber" id="maxResponseNumber"  cssClass="form-control" />
                    </div>
                    <br>
                </div>
            </fieldset>

            <fieldset class="col-xs-12">
                <s:if test="%{questionId != null && questionId != ''}">
                    <div class="pull-right">
                        <wpsa:actionParam action="addChoice" var="addChoiceAction">
                            <wpsa:actionSubParam name="overrideResponseNumberFieldsCheck" value="true"></wpsa:actionSubParam>
                        </wpsa:actionParam>
                        <wpsf:submit type="button" action="%{#addChoiceAction}" cssClass="btn btn-success">
                            <s:text name="%{getText('jpsurvey_new_choice')}" />
                        </wpsf:submit>
                        <s:if test="question.questionnaire">
                            <wpsa:actionParam action="addFreeText" var="addFreeTextAction">
                                <wpsa:actionSubParam name="overrideResponseNumberFieldsCheck" value="true"></wpsa:actionSubParam>
                            </wpsa:actionParam>
                            <wpsf:submit type="button" action="%{#addFreeTextAction}" value="%{getText('jpsurvey_new_freeText')}" cssClass="btn btn-success" />
                        </s:if>
                        <br><br>
                    </div>
                </s:if>


                <s:if test="question.choices.isEmpty">
                    <fieldset class="col-xs-12">
                        <div class="alert alert-info"> 
                            <s:text name="jpsurvey.noAnswers" />
                        </div>
                    </fieldset>
                </s:if>
                <s:else>
                    <div class="tab-separation">
                        <table class="table table-striped table-bordered table-hover no-mb">
                            <thead>
                                <tr>
                                    <th>
                                        <s:text name="jpsurvey_answersTable_answers" />
                                    </th>
                                    <s:if test="%{!#isModifiable}">
                                        <th class="text-center table-w-10">
                                            <s:text name="jpsurvey_answersTable_occurences" />
                                        </th>
                                    </s:if>
                                    <th class="text-center table-w-5">
                                        <s:text name="label.actions" />
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator var="currentChoice" value="question.choices"
                                            status="status">
                                    <tr>
                                        <s:set var="currentStat" value="choiceStats[#currentChoice.id]" />
                                        <s:if test="%{#currentChoice.freeText== false}">
                                            <s:set var="localizedString" value="%{getText('jpsurvey_freeText')}" />
                                        </s:if>
                                        <s:else>
                                            <s:set var="localizedString" value="%{getText('jpsurvey_freeText')}" />
                                        </s:else>
                                        <s:if test="%{#currentChoice.freeText== false}">
                                            <th>
                                                <s:property value="%{getLabel(#currentChoice.choices)}" />
                                                </td>
                                            </s:if>
                                            <s:else>
                                            <td>
                                                <s:text name="jpsurvey_freeText" />
                                            </td>
                                        </s:else>
                                        <td class="text-center">
                                            <s:if test="%{#currentStat == '' || #currentStat==null}"> 0
                                            </s:if> 
                                            <s:else>
                                                <s:property value="#currentStat" />
                                            </s:else>
                                        </td>
                                        <td class="table-view-pf-actions text-center">
                                            <div class="dropdown dropdown-kebab-pf">
                                                <button class="btn btn-menu-right dropdown-toggle"
                                                        type="button" data-toggle="dropdown" aria-haspopup="true"
                                                        aria-expanded="false">
                                                    <span class="fa fa-ellipsis-v"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <s:if test="%{#currentChoice.freeText== false}">
                                                        <li>
                                                            <a href="<s:url action="editChoice">
                                                                   <s:param name="choiceId" value="#currentChoice.id"/>
                                                                   <s:param name="strutsAction" value="2"/>
                                                               </s:url>"
                                                               title="<s:text name="label.edit" />: <s:property value="%{getLabel(#currentChoice.choices)}"/>">
                                                                <s:text name="label.edit" />
                                                            </a>
                                                        </li>
                                                    </s:if>
                                                    <li>
                                                        <a href="<s:url action="moveChoiceUp">
                                                               <s:param name="choiceId" value="#currentChoice.id"/>
                                                               <s:param name="questionId" value="question.id"/>
                                                           </s:url>"
                                                           title="<s:text name="label.moveUp" />:&#32;<s:if test="%{#currentChoice.freeText== false}"><s:property value="%{getLabel(#currentChoice.choices)}"/></s:if><s:else><s:property value="#localizedString"/></s:else>">
                                                            <s:text name="label.moveUp" />
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="<s:url action="moveChoiceDown">
                                                               <s:param name="choiceId" value="#currentChoice.id"/>
                                                               <s:param name="questionId" value="question.id"/>
                                                           </s:url>"
                                                           title="<s:text name="label.moveDown" />:&#32;<s:if test="%{#currentChoice.freeText== false}"><s:property value="%{getLabel(#currentChoice.choices)}"/></s:if><s:else><s:property value="#localizedString"/></s:else>">
                                                            <s:text name="label.moveDown" />
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="<s:url action="trashChoice">
                                                               <s:param name="choiceId" value="#currentChoice.id"/>
                                                           </s:url>"
                                                           title="<s:text name="label.remove" />:&#32;<s:if test="%{#currentChoice.freeText== false}"><s:property value="%{getLabel(#currentChoice.choices)}"/></s:if><s:else><s:property value="#localizedString"/></s:else>">
                                                            <s:text name="label.remove" />
                                                        </a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </s:else>
            </fieldset>
        </s:if>
        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" action="saveQuestion"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="%{getText('label.save')}" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
