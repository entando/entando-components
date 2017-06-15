<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="title.surveyList" /></li>
        <s:if test="questionnaire">
        <li><a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="true"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text
                    name="title.jpsurvey.survey.main" /></a></li>
            </s:if>
            <s:else>
        <li><a
                href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>"
                title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text
                    name="title.jpsurvey.poll.main" /></a></li>
            </s:else>
            <s:if test="getStrutsAction() == 1">
                <s:if test="questionnaire">
            <li class="page-title-container"><s:text
                    name="title.jpsurvey.survey.new" /></li>
            </s:if>
            <s:else>
            <li class="page-title-container"><s:text
                    name="title.jpsurvey.poll.new" /></li>
            </s:else>
        </s:if>
        <s:if test="getStrutsAction() == 2">
            <s:if test="questionnaire">
            <li class="page-title-container"><s:text
                    name="title.jpsurvey.survey.edit" /></li>
            </s:if>
            <s:else>
            <li class="page-title-container"><s:text
                    name="title.jpsurvey.poll.edit" /></li>
            </s:else>
        </s:if>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:if test="getStrutsAction() == 1">
                    <s:if test="questionnaire">
                        <s:text name="title.jpsurvey.survey.new" />
                        <span class="pull-right"> <a tabindex="0" role="button"
                                                     data-toggle="popover" data-trigger="focus" data-html="true"
                                                     title=""
                                                     data-content="<s:text name="title.jpsurvey.survey.new.help" />"
                                                     data-placement="left" data-original-title=""> <i
                                    class="fa fa-question-circle-o" aria-hidden="true"></i>
                            </a>
                        </span>
                    </s:if>
                    <s:else>
                        <s:text name="title.jpsurvey.poll.new" />
                        <span class="pull-right"> <a tabindex="0" role="button"
                                                     data-toggle="popover" data-trigger="focus" data-html="true"
                                                     title=""
                                                     data-content="<s:text name="title.jpsurvey.poll.new.help" />"
                                                     data-placement="left" data-original-title=""> <i
                                    class="fa fa-question-circle-o" aria-hidden="true"></i>
                            </a>
                        </span>
                    </s:else>
                </s:if>
                <s:if test="getStrutsAction() == 2">
                    <s:if test="questionnaire">
                        <s:text name="title.jpsurvey.survey.edit" />
                        <span class="pull-right"> <a tabindex="0" role="button"
                                                     data-toggle="popover" data-trigger="focus" data-html="true"
                                                     title=""
                                                     data-content="<s:text name="title.jpsurvey.survey.edit.help" />"
                                                     data-placement="left" data-original-title=""> <i
                                    class="fa fa-question-circle-o" aria-hidden="true"></i>
                            </a>
                        </span>
                    </s:if>
                    <s:else>
                        <s:text name="title.jpsurvey.poll.edit" />
                        <span class="pull-right"> <a tabindex="0" role="button"
                                                     data-toggle="popover" data-trigger="focus" data-html="true"
                                                     title=""
                                                     data-content="<s:text name="title.jpsurvey.poll.edit.help" />"
                                                     data-placement="left" data-original-title=""> <i
                                    class="fa fa-question-circle-o" aria-hidden="true"></i>
                            </a>
                        </span>
                    </s:else>
                </s:if>
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
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
    <s:if test="getStrutsAction() == 2">
        <div class="panel panel-default">
            <div class="panel-body">
                <s:if test="surveyId != null">
                    <p>
                        <s:text name="label.workingOn" />
                        : <em><s:property value="%{getLabel(survey.titles)}" /></em>.
                    </p>
                </s:if>

                <s:if test="questionnaire">
                    <s:text name="title.jpsurvey.survey.edit.intro" />
                </s:if>
                <s:else>
                    <s:text name="title.jpsurvey.poll.edit.intro" />
                </s:else>
            </div>

        </div>
    </s:if>

    <s:form cssClass="tab-container">
        <p class="noscreen">
            <s:if test="surveyId != null">
                <s:hidden name="questionnaire" value="%{survey.questionnaire}" />
            </s:if>
            <s:else>
                <s:hidden name="questionnaire" />
            </s:else>
            <s:hidden name="strutsAction" />
            <s:if test="imageId != null">
                <s:hidden name="imageId" />
            </s:if>
        </p>

        <s:if test="getStrutsAction() == 2">
            <p class="noscreen">
                <s:hidden name="surveyId" />
            </p>
        </s:if>

        <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
            <li class="sr-only"><a data-toggle="tab" href="#info_tab"><s:text
                        name="title.contentInfo" /></a></li>
            <li class="active"><a data-toggle="tab" href="#info"><s:text
                        name="label.info" /></a></li>
                    <s:iterator value="langs" var="lang">
                <li><a data-toggle="tab"
                       href="#<s:property value="#lang.code" />_tab"><s:property
                            value="#lang.descr" /></a></li>
                    </s:iterator>
        </ul>
        <div class="panel" id="tab-container">
            <div class="panel-body">
                <div class="tab-content">
                    <div id="info" class="tab-pane active">
                        <fieldset class="col-xs-12">
                            <legend>
                                <s:text name="label.info" />
                            </legend>

                            <p class="noscreen">
                                <s:hidden name="active" />
                            </p>

                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="startDate_cal"><s:text
                                        name="jpsurvey_start_date" /></label>
                                <div class="col-sm-10">
                                    <wpsf:textfield name="startDate" id="startDate_cal"
                                                    cssClass="form-control datepicker" placeholder="dd/MM/yyyy" />
                                </div>
                                <br>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="endDate_cal"><s:text
                                        name="jpsurvey_end_date" /></label>
                                <div class="col-sm-10">
                                    <wpsf:textfield name="endDate" id="endDate_cal"
                                                    cssClass="form-control datepicker" placeholder="dd/MM/yyyy" />
                                </div>
                                <br>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="groupName"><s:text
                                        name="jpsurvey_group" /></label>
                                <div class="col-sm-10">
                                    <wpsf:select name="groupName" id="groupName" list="groups"
                                                 listKey="name" listValue="descr" cssClass="form-control" />
                                </div>
                                <br>
                            </div>

                            <s:if test="questionnaire">
                            </s:if>

                            <s:else>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label" for="publicPartialResult"><s:text
                                            name="jpsurvey_publicPartialResult" /></label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="publicPartialResult"
                                                id="publicPartialResult" tabindex="<wpsa:counter />">
                                            <option value="1"
                                                    <s:if test="%{publicPartialResult > 0}">selected="selected"</s:if>><s:text
                                                    name="jpsurvey_isPublicPartialResult" /></option>
                                            <option value="0"
                                                    <s:if test="%{publicPartialResult == null || publicPartialResult == 0}">selected="selected"</s:if>><s:text
                                                    name="jpsurvey_notPublicPartialResult" /></option>
                                        </select>
                                    </div>
                                    <br>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label" for="publicResult"><s:text
                                            name="jpsurvey_publicResult" /></label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="publicResult"
                                                id="publicResult" tabindex="<wpsa:counter />">
                                            <option value="1"
                                                    <s:if test="%{publicResult > 0}">selected="selected"</s:if>><s:text
                                                    name="jpsurvey_isPublicResult" /></option>
                                            <option value="0"
                                                    <s:if test="%{publicResult == null || publicResult == 0}">selected="selected"</s:if>><s:text
                                                    name="jpsurvey_notPublicResult" /></option>
                                        </select>
                                    </div>
                                    <br>
                                </div>
                            </s:else>

                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="gatherUserInfo"><s:text
                                        name="jpsurvey_profileUser" /></label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="gatherUserInfo"
                                            id="gatherUserInfo" tabindex="<wpsa:counter />">
                                        <option value="1"
                                                <s:if test="%{gatherUserInfo > 0}">selected="selected"</s:if>><s:text
                                                name="jpsurvey_isProfileUser" /></option>
                                        <option value="0"
                                                <s:if test="%{gatherUserInfo == null || gatherUserInfo == 0}">selected="selected"</s:if>><s:text
                                                name="jpsurvey_notProfileUser" /></option>
                                    </select>
                                </div>
                                <br>
                            </div>
                        </fieldset>
                        <fieldset class="col-xs-12">
                            <legend>
                                <s:text name="jpsurvey_vote_control" />
                            </legend>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="checkCookie"><s:text
                                        name="jpsurvey_cookie" /></label>
                                <div class="col-sm-10">
                                    <wpsf:checkbox name="checkCookie" id="checkCookie"
                                                   cssClass="bootstrap-switch" />
                                </div>
                                <br>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="checkIpAddress"><s:text
                                        name="jpsurvey_ip_username" /></label>
                                <div class="col-sm-10">
                                    <wpsf:checkbox name="checkIpAddress" id="checkIpAddress"
                                                   cssClass="bootstrap-switch" />
                                </div>
                                <br>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="checkUsername"><s:text
                                        name="jpsurvey_check_username" /></label>
                                <div class="col-sm-10">
                                    <wpsf:checkbox name="checkUsername" id="checkUsername"
                                                   cssClass="bootstrap-switch" />
                                </div>
                                <br>
                            </div>
                        </fieldset>
                    </div>

                    <s:iterator var="currentLang" value="langs">
                        <div id="<s:property value="#currentLang.code" />_tab"
                             class="tab-pane">

                            <div class="contentAttributeBox">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"
                                           for="title-<s:property value="#currentLang.code"/>"> <code
                                            class="label label-info">
                                            <s:property value="#currentLang.code" />
                                        </code> <s:text name="jpsurvey_title" />
                                    </label>
                                    <div class="col-sm-10">
                                        <wpsf:textfield name="%{'title-'+#currentLang.code}"
                                                        id="%{'title-'+#currentLang.code}"
                                                        value="%{titles[#currentLang.code]}" cssClass="form-control" />
                                    </div>
                                    <br>
                                </div>
                            </div>

                            <div class="contentAttributeBox">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"
                                           for="description-<s:property value="%{#currentLang.code}"/>">
                                        <code class="label label-info">
                                            <s:property value="%{#currentLang.code}" />
                                        </code> <s:text name="jpsurvey_description" />
                                    </label>
                                    <div class="col-sm-10">
                                        <wpsf:textarea name="%{'description-'+#currentLang.code}"
                                                       id="%{'description-'+#currentLang.code}" cols="45" rows="6"
                                                       value="%{descriptions[#currentLang.code]}"
                                                       cssClass="form-control" />
                                    </div>
                                    <br>
                                </div>
                            </div>

                            <s:if test="strutsAction == 2">
                                <div class="contentAttributeBox">
                                    <s:if test="%{imageId == null}">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"
                                                   for="imageDescription-<s:property value="%{#currentLang.code}"/>"><s:text
                                                    name="jpsurvey_imageDescription" /></label>
                                                <wpsa:actionParam action="associateSurveyImageEntry"
                                                                  var="associateImageAction">
                                                    <wpsa:actionSubParam name="resourceTypeCode" value="Image" />
                                                </wpsa:actionParam>
                                            <div class="col-sm-10">
                                                <wpsf:submit type="button" action="%{#associateImageAction}"
                                                             cssClass="btn btn-default"
                                                             title="%{getText('label.choose')}">
                                                    <span class="icon fa fa-picture-o"></span>
                                                    &#32;
                                                    <s:text name="%{getText('jpsurvey_new_image')}" />
                                                </wpsf:submit>
                                            </div>
                                        </div>
                                    </s:if>

                                    <s:else>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"
                                                   for="imageDescription-<s:property value="%{#currentLang.code}"/>"><s:text
                                                    name="jpsurvey_imageDescription" /></label>

                                            <div class="col-sm-10">
                                                <div class="panel panel-default margin-small-top">
                                                    <div class="panel-heading text-right">
                                                        <wpsf:submit type="button" action="removeSurveyImage"
                                                                     cssClass="btn btn-warning btn-xs"
                                                                     title="%{getText('jpsurvey_remove_image')}">
                                                            <span class="icon fa fa-eraser"></span>
                                                            &#32;
                                                            <s:text name="%{getText('jpsurvey_remove_image')}" />
                                                        </wpsf:submit>

                                                    </div>

                                                    <div class="row panel-body">
                                                        <div class="col-xs-12 col-sm-3 col-lg-2 text-center">
                                                            <s:set var="resource" value="%{loadResource(imageId)}" />
                                                            <a class="pull-left"
                                                               href="<s:property value="%{#resource.getImagePath(0)}" />"
                                                               title="<s:property value="%{#resource.descr}" />"> <img
                                                                    class="img-thumbnail"
                                                                    src="<s:property value="%{#resource.getImagePath(1)}"/>"
                                                                    alt="<s:property value="%{#resource.descr}" />"
                                                                    style="height: 90px; max-width: 130px" />
                                                            </a>
                                                        </div>

                                                        <div
                                                            class="col-xs-12 col-sm-9 col-lg-10 form-horizontal margin-large-top">
                                                            <div class="form-group">
                                                                <div class="col-xs-12">
                                                                    <div class="input-group">
                                                                        <span class="input-group-addon"><span
                                                                                class="icon fa fa-picture-o"></span></span>
                                                                            <wpsf:textfield
                                                                                name="%{'imageDescription-'+#currentLang.code}"
                                                                                id="%{'imageDescription-'+#currentLang.code}"
                                                                                value="%{imageDescriptions[#currentLang.code]}"
                                                                                cssClass="form-control" />
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </s:else>
                                </div>
                            </s:if>
                        </div>
                    </s:iterator>
                </div>
            </div>
        </div>

        <s:if test="%{strutsAction == 2}">
            <fieldset class="col-xs-12">
                <legend>
                    <s:text name="jpsurvey_questions_list" />
                </legend>
                <s:if test="%{surveyId != null}">
                    <div class="form-group">
                        <wpsf:submit type="button" action="addQuestion" value=""
                                     cssClass="btn btn-primary pull-right">
                            <s:text name="%{getText('jpsurvey_new_questions')}" />
                        </wpsf:submit>
                        <br>
                    </div>
                </s:if>
                <s:if test="survey.questions.isEmpty">
                    <div class="alert alert-info">
                        <s:text name="jpsurvey.noQuestions" />
                    </div>
                </s:if>

                <s:else>
                    <table class="table table-striped table-bordered table-hover no-mb">
                        <thead>
                            <tr>
                                <th class="text-center"><s:text
                                        name="jpsurvey_questionsTable_questions" /></th>
                                <th class="text-center"><s:text
                                        name="jpsurvey_questionsTable_occurences" /></th>
                                <th class="text-center"><s:text
                                        name="jpsurvey_questionsTable_answers_number" /></th>
                                <th class="text-center"><s:text
                                        name="jpsurvey_questionsTable_actions" /></th>
                            </tr>
                        </thead>
                        <s:set var="currentSurveyQuestions" value="survey.questions" />
                        <tbody>
                            <s:iterator var="currentQuestion" value="#currentSurveyQuestions"
                                        status="status">
                                <tr class="text-center">
                                    <td><s:property
                                            value="%{getLabel(#currentQuestion.questions)}" /></td>
                                    <td><s:property
                                            value="%{getResponseOccurences(#currentQuestion.id)}" /></td>
                                    <td><s:property value="#currentQuestion.AnswersNumber" /></td>
                                    <td class="table-view-pf-actions">
                                        <button class="btn btn-menu-right dropdown-toggle"
                                                type="button" data-toggle="dropdown" aria-haspopup="true"
                                                aria-expanded="false">
                                            <span class="fa fa-ellipsis-v"></span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right">
                                            <li><a
                                                    href="<s:url action="editQuestion">
                                                        <s:param name="surveyId" value="surveyId"/>
                                                        <s:param name="questionId" value="#currentQuestion.id"/>
                                                        <s:param name="strutsAction" value="2"/></s:url>"
                                                    title="<s:text name="label.edit" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
                                                    <s:text name="label.edit" />
                                                </a></li>
                                            <li><a
                                                    href="<s:url action="moveQuestionUp">
                                                        <s:param name="surveyId" value="surveyId"/>
                                                        <s:param name="questionId" value="#currentQuestion.id"/></s:url>"
                                                    title="<s:text name="label.moveUp" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
                                                    <s:text name="label.moveUp" />
                                                </a></li>
                                            <li><a
                                                    href="<s:url action="moveQuestionDown">
                                                        <s:param name="surveyId" value="surveyId"/>
                                                        <s:param name="questionId" value="#currentQuestion.id"/></s:url>"
                                                    title="<s:text name="label.moveDown" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
                                                    <s:text name="label.moveDown" />
                                                </a></li>
                                            <li><a
                                                    href="<s:url action="trashQuestion">
                                                        <s:param name="surveyId" value="surveyId"/>
                                                        <s:param name="questionId" value="#currentQuestion.id"/></s:url>"
                                                    title="<s:text name="label.remove" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
                                                    <s:text name="label.remove" />
                                                </a></li>
                                        </ul>
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                    <br>
                </s:else>
            </fieldset>
        </s:if>

        <div class="form-group">
            <div class="col-xs-12">
                <s:if test="getStrutsAction() == 1">
                    <wpsf:submit type="button" action="saveSurvey"
                                 cssClass="btn btn-primary pull-right">
                        <s:text name="%{getText('label.continue')}" />
                    </wpsf:submit>
                </s:if>
                <s:elseif test="getStrutsAction() == 2">
                    <wpsf:submit type="button" action="saveSurvey"
                                 cssClass="btn btn-primary pull-right">
                        <s:text name="%{getText('label.save')}" />
                    </wpsf:submit>
                </s:elseif>
            </div>
        </div>

    </s:form>
</div>
