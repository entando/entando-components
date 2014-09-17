<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:if test="questionnaire">
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
        </s:if>
        <s:else>
            <a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
        </s:else>
        &#32;/&#32;
        <%-- new title --%>
        <s:if test="getStrutsAction() == 1">
            <s:if test="questionnaire">
                <s:text name="title.jpsurvey.survey.new" />
            </s:if>
            <s:else>
                <s:text name="title.jpsurvey.poll.new" />
            </s:else>
        </s:if>

        <%-- edit title --%>
        <s:if test="getStrutsAction() == 2">
            <s:if test="questionnaire">
                <s:text name="title.jpsurvey.survey.edit" />
            </s:if>
            <s:else>
                <s:text name="title.jpsurvey.poll.edit" />
            </s:else>
        </s:if>
    </span>
</h1>

<div id="main">
    <%-- error messages --%>
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
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
        </div>
    </s:if>  

    <s:if test="getStrutsAction() == 2">
        <div class="panel panel-default">
            <div class="panel-body">
                <%-- workin on --%>
                <s:if test="surveyId != null">
                    <p><s:text name="label.workingOn" />: <em><s:property value="%{getLabel(survey.titles)}" /></em>.</p>
                </s:if>

                <%-- edit help --%>
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
        <%-- HIDDEN FIELDS --%>
        <p class="noscreen">
            <s:if test="surveyId != null">
                <s:hidden name="questionnaire" value="%{survey.questionnaire}"/>
            </s:if>
            <s:else>
                <s:hidden name="questionnaire"/>
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
        <%-- FINE HIDDEN FIELDS --%>

        <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
            <li class="sr-only"><a data-toggle="tab" href="#info_tab">Content Info</a></li>
            <li class="active"><a data-toggle="tab" href="#info">Info</a></li>
                <s:iterator value="langs" id="lang">
                <li><a data-toggle="tab" href="#<s:property value="#lang.code" />_tab"><s:property value="#lang.descr" /></a></li>
                </s:iterator>
        </ul>
        <%-- INIZIO ITERAZIONE CAMPI LOCALIZZATI--%>
        <div class="panel panel-default" id="tab-container">
            <div class="panel-body">
                <div class="tab-content">
                    <div id="info" class="tab-pane active">

                        <fieldset class="col-xs-12">
                            <legend><s:text name="label.info" /></legend>

                            <div class="form-group">
                                <label for="startDate_cal"><s:text name="jpsurvey_start_date" /></label>
                                <wpsf:textfield name="startDate" id="startDate_cal" cssClass="form-control datepicker" />
                                <span class="help help-block">dd/MM/yyyy</span>
                            </div>

                            <div class="form-group">
                                <label for="endDate_cal"><s:text name="jpsurvey_end_date" /></label>
                                <wpsf:textfield name="endDate" id="endDate_cal" cssClass="form-control datepicker" />
                                <span class="help help-block">dd/MM/yyyy</span>
                            </div>

                            <div class="form-group">
                                <label for="groupName"><s:text name="jpsurvey_group" /></label>
                                <wpsf:select name="groupName" id="groupName" list="groups" listKey="name" listValue="descr" cssClass="form-control" />
                            </div>

                            <p class="noscreen"><s:hidden name="active" /></p>

                            <s:if test="questionnaire">
                                <%--<p>NEI QUESTIONARI RISULTATI PARZIALI E DEFINITIVI NON SONO PUBBLICI</p>--%>
                            </s:if>

                            <s:else>
                                <div class="form-group">
                                    <label for="publicPartialResult"><s:text name="jpsurvey_publicPartialResult" /></label>	
                                    <select class="form-control" name="publicPartialResult" id="publicPartialResult" tabindex="<wpsa:counter />">
                                        <option value="1" <s:if test="%{publicPartialResult > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isPublicPartialResult" /></option>
                                        <option value="0" <s:if test="%{publicPartialResult == null || publicPartialResult == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notPublicPartialResult" /></option>
                                        </select>	
                                    </div>
                                    <div class="form-group">
                                        <label for="publicResult"><s:text name="jpsurvey_publicResult" /></label>
                                    <select class="form-control" name="publicResult" id="publicResult" tabindex="<wpsa:counter />">
                                        <option value="1" <s:if test="%{publicResult > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isPublicResult" /></option>
                                        <option value="0" <s:if test="%{publicResult == null || publicResult == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notPublicResult" /></option>
                                        </select>
                                    </div>
                            </s:else>

                            <div class="form-group">
                                <label for="gatherUserInfo"><s:text name="jpsurvey_profileUser" /></label>
                                <select class="form-control" name="gatherUserInfo" id="gatherUserInfo" tabindex="<wpsa:counter />">
                                    <option value="1" <s:if test="%{gatherUserInfo > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isProfileUser" /></option>
                                    <option value="0" <s:if test="%{gatherUserInfo == null || gatherUserInfo == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notProfileUser" /></option>
                                    </select>
                                </div>

                            </fieldset>
                            <fieldset class="col-xs-12">
                                <legend><s:text name="jpsurvey_vote_control" /></legend>
                            <div class="form-group">
                                <div class="checkbox">
                                    <wpsf:checkbox name="checkCookie" id="checkCookie" cssClass="radiocheck"/><label for="checkCookie"><s:text name="jpsurvey_cookie" /></label><br />
                                </div>
                                <div class="checkbox">
                                    <wpsf:checkbox name="checkIpAddress" id="checkIpAddress" cssClass="radiocheck" /><label for="checkIpAddress"><s:text name="jpsurvey_ip_username" /></label>
                                </div>
                                <div class="checkbox">
                                    <wpsf:checkbox name="checkUsername" id="checkUsername" cssClass="radiocheck" /><label for="checkUsername"><s:text name="jpsurvey_check_username" /></label>
                                </div>
                            </div>
                        </fieldset>
                    </div>



                    <s:iterator id="currentLang" value="langs">
                        <div id="<s:property value="#currentLang.code" />_tab" class="tab-pane">

                            <div class="contentAttributeBox">	
                                <div class="form-group">
                                    <label class="control-label" for="title-<s:property value="#currentLang.code"/>">
                                        <code class="label label-info"><s:property value="#currentLang.code"/></code>
                                        <s:text name="jpsurvey_title" />
                                    </label>
                                    <wpsf:textfield name="%{'title-'+#currentLang.code}" id="%{'title-'+#currentLang.code}" value="%{titles[#currentLang.code]}" cssClass="form-control" />		
                                </div>
                            </div>

                            <div class="contentAttributeBox">	
                                <div class="form-group">
                                    <label class="control-label" for="description-<s:property value="%{#currentLang.code}"/>">
                                        <code class="label label-info"><s:property value="%{#currentLang.code}"/></code>
                                        <s:text name="jpsurvey_description" />
                                    </label>
                                    <wpsf:textarea name="%{'description-'+#currentLang.code}" id="%{'description-'+#currentLang.code}" cols="45" rows="6" value="%{descriptions[#currentLang.code]}" cssClass="form-control" />
                                </div>
                            </div>		

                            <%-- PULSANTIERA IMMAGINE SURVEY--%>
                            <s:if test="strutsAction == 2">
                                <div class="contentAttributeBox">
                                    <s:if test="%{imageId == null}">
                                        <%-- BOTTONE ASSOCIA IMMAGINE --%>
                                        <div class="form-group">
                                            <label class="display-block" for="imageDescription-<s:property value="%{#currentLang.code}"/>"><s:text name="jpsurvey_imageDescription" /></label>
                                            <wpsa:actionParam action="associateSurveyImageEntry" var="associateImageAction"><wpsa:actionSubParam name="resourceTypeCode" value="Image" /></wpsa:actionParam>
                                            <wpsf:submit type="button" action="%{#associateImageAction}" cssClass="btn btn-default" title="%{getText('label.choose')}">
                                                <span class="icon fa fa-picture-o"></span>&#32; 
                                                <s:text name="%{getText('jpsurvey_new_image')}"/>
                                            </wpsf:submit>
                                        </div>
                                    </s:if>

                                    <s:else>
                                        <%-- BOTTONE RIMUOVI IMMAGINE --%>

                                        <div class="form-group">
                                            <label for="imageDescription-<s:property value="%{#currentLang.code}"/>"><s:text name="jpsurvey_imageDescription" /></label>

                                            <div class="panel panel-default margin-small-top">
                                                <div class="panel-heading text-right">
                                                    <wpsf:submit type="button" action="removeSurveyImage" cssClass="btn btn-warning btn-xs" title="%{getText('jpsurvey_remove_image')}">
                                                        <span class="icon fa fa-eraser"></span>&#32; 
                                                        <s:text name="%{getText('jpsurvey_remove_image')}"/>
                                                    </wpsf:submit>

                                                </div>

                                                <div class="row panel-body">
                                                    <div class="col-xs-12 col-sm-3 col-lg-2 text-center">
                                                        <s:set name="resource" value="%{loadResource(imageId)}" />
                                                        <a class="pull-left" href="<s:property value="%{#resource.getImagePath(0)}" />" title="<s:property value="%{#resource.descr}" />" >
                                                            <img class="img-thumbnail" src="<s:property value="%{#resource.getImagePath(1)}"/>" alt="<s:property value="%{#resource.descr}" />" style="height:90px; max-width:130px" />
                                                        </a>
                                                    </div>

                                                    <div class="col-xs-12 col-sm-9 col-lg-10 form-horizontal margin-large-top">
                                                        <div class="form-group">

                                                            <div class="col-xs-12">
                                                                <div class="input-group">
                                                                    <span class="input-group-addon"><span class="icon fa fa-picture-o"></span></span>
                                                                        <wpsf:textfield name="%{'imageDescription-'+#currentLang.code}" id="%{'imageDescription-'+#currentLang.code}" value="%{imageDescriptions[#currentLang.code]}" cssClass="form-control" />
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
                            <%-- FINE ITERAZIONE CAMPI LOCALIZZATI--%>
                        </div>
                    </s:iterator>
                </div>
            </div>
        </div>

        <s:if test="%{strutsAction == 2}"> 
            <fieldset class="col-xs-12">
                <legend><s:text name="jpsurvey_questions_list" /></legend>
                <%-- INIZIO ITERAZIONE DOMANDE NELLA LINGUA CORRENTE --%>
                <s:if test="survey.questions.isEmpty">
                    <div class="alert alert-info"><s:text name="jpsurvey.noQuestions" /></div>
                </s:if>

                <s:else>
                    <table class="table table-bordered">
                        <tr>
                            <th class="text-center"><abbr title="<s:text name="jpsurvey_questionsTable_actions" />">-</abbr></th>		
                            <th><s:text name="jpsurvey_questionsTable_questions" /></th>
                            <th class="text-right"><s:text name="jpsurvey_questionsTable_occurences" /></th>
                            <th class="text-right"><s:text name="jpsurvey_questionsTable_answers_number" /></th>
                        </tr>
                        <s:set var="currentSurveyQuestions" value="survey.questions" />
                        <s:iterator var="currentQuestion" value="#currentSurveyQuestions" status="status" >
                            <tr>
                                <td class="text-center">
                                    <div class="btn-group btn-group-xs">
                                        <a class="btn btn-default" href="<s:url action="editQuestion">
                                               <s:param name="surveyId" value="surveyId"/>
                                               <s:param name="questionId" value="#currentQuestion.id"/>
                                               <s:param name="strutsAction" value="2"/></s:url>" 
                                           title="<s:text name="label.edit" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
                                            <span class="sr-only"><s:text name="label.edit" /></span>
                                            <span class="icon fa fa-pencil-square-o"></span>                                                                                    
                                        </a>
                                    </div>
                                    <div class="btn-group btn-group-xs">
                                        <a class="btn btn-default" href="<s:url action="moveQuestionUp">
                                               <s:param name="surveyId" value="surveyId"/>
                                               <s:param name="questionId" value="#currentQuestion.id"/></s:url>" 
                                           title="<s:text name="label.moveUp" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>" >
                                            <span class="sr-only"><s:text name="label.moveUp" /></span>
                                            <span class="icon fa fa-sort-desc"></span>
                                        </a>
                                        <a class="btn btn-default" href="<s:url action="moveQuestionDown">
                                               <s:param name="surveyId" value="surveyId"/>
                                               <s:param name="questionId" value="#currentQuestion.id"/></s:url>" 
                                           title="<s:text name="label.moveDown" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>" >
                                            <span class="sr-only"><s:text name="label.moveDown" /></span>
                                            <span class="icon fa fa-sort-asc"></span>
                                        </a>
                                    </div>
                                    <div class="btn-group btn-group-xs">

                                        <a class="btn btn-warning" href="<s:url action="trashQuestion">
                                               <s:param name="surveyId" value="surveyId"/>
                                               <s:param name="questionId" value="#currentQuestion.id"/></s:url>" 
                                           title="<s:text name="label.remove" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
                                            <span class="sr-only"><s:text name="label.remove" /></span>
                                            <span class="icon fa fa-times-circle-o"></span>
                                        </a>
                                    </div>
                                </td>
                                <td>
                                    <%-- DOMANDA LINKATA --%>
                                    <s:property value="%{getLabel(#currentQuestion.questions)}"/>
                                </td>
                                <td class="text-right">
                                    <s:property value="%{getResponseOccurences(#currentQuestion.id)}" />
                                </td>
                                <td class="text-right"><s:property value="#currentQuestion.AnswersNumber" /></td>
                            </tr>
                        </s:iterator>
                    </table>
                </s:else>
                <%-- FINE ITERAZIONE DOMANDE --%>
                <s:if test="%{surveyId != null}">
                    <div class="form-group">
                        <wpsf:submit type="button" action="addQuestion" value="" cssClass="btn btn-default">
                            <span class="icon fa fa-plus-circle"></span>
                            <s:text name="%{getText('jpsurvey_new_questions')}"/>
                        </wpsf:submit>
                    </div>
                </s:if>
            </fieldset>
        </s:if>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <s:if test="getStrutsAction() == 1">
                        <wpsf:submit type="button" action="saveSurvey" cssClass="btn btn-default btn-block">
                            <span class="icon fa fa-play-circle-o"></span>&#32;
                            <s:text name="%{getText('label.continue')}"/>                            
                        </wpsf:submit>
                    </s:if>
                    <s:elseif test="getStrutsAction() == 2">
                        <wpsf:submit type="button" action="saveSurvey" cssClass="btn btn-primary btn-block">
                            <span class="icon fa fa-floppy-o"></span>&#32;
                            <s:text name="%{getText('label.save')}"/>
                        </wpsf:submit>
                    </s:elseif>
                </div>
            </div>
        </div>

    </s:form>
</div>