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
        <s:if test="getStrutsAction() == 1"><s:text name="title.jpsurvey.choice.new" /></s:if>
        <s:if test="getStrutsAction() == 2"><s:text name="title.jpsurvey.choice.edit" /></s:if>
        </span>
    </h1>
    <div id="main"> 
        <div class="panel panel-default">
            <div class="panel-body">
            <s:text name="label.workingOn" />:&#32;
            <em><s:property value="%{getLabel(choice.surveyTitles)}" /></em>, <s:text name="label.workingOn.question" />:&#32;<em><s:property value="%{getLabel(choice.questions)}" /></em>
        </div>
    </div>

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
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
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:form cssClass="tab-container" action="saveChoice" class="form-horizontal">

        <%-- HIDDEN --%>
        <p class="noscreen">
            <s:hidden name="choiceId" value="%{choice.id}"/>
            <s:hidden name="questionId" value="%{choice.questionId}" />
            <%-- 
            <s:hidden name="questionnaire" />
            <s:hidden name="surveyId" />
            --%>
            <s:hidden name="strutsAction" />
        </p>
        <%-- FINE HIDDEN --%>

        <ul class="nav nav-tabs tab-togglers" id="tab-togglers">
            <s:iterator value="langs" id="lang" status="langStatusVar">
                <li <s:if test="#langStatusVar.first"> class="active" </s:if>>
                    <a data-toggle="tab" href="#<s:property value="#lang.code" />_tab"><s:property value="#lang.descr" /></a>
                </li>
            </s:iterator>
        </ul>
        <!-- INIZIO ITERAZIONE LOCALIZZAZIONE OPZIONI -->
        <div class="panel panel-default" id="tab-container">
            <div class="panel-body">
                <div class="tab-content">
                    <s:iterator id="localizedLang" value="langs" status="langStatusVar">
                        <div id="<s:property value="#localizedLang.code" />_tab" class="tab-pane <s:if test="#langStatusVar.first"> active </s:if>">
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <label for="choice-<s:property value="#localizedLang.code" />">
                                        <s:if test="choice.questionnaire">
                                            <s:text name="label.answer" />
                                        </s:if>
                                        <s:else>
                                            <s:text name="jpsurvey_choice" />
                                        </s:else>
                                    </label>
                                    <s:set name="localization" value="%{getChoices()[#localizedLang.code]}"/>
                                    <wpsf:textarea cssClass="form-control" name="%{'choice-'+#localizedLang.code}" id="%{'choice-'+#localizedLang.code}" value="%{#localization}" cols="60" rows="3" />
                                </div>
                            </div>
                        </div>
                    </s:iterator>
                </div>
            </div>
        </div>
        <!-- FINE ITERAZIONE LOCALIZZAZIONE OPZIONI -->
        <div class="form-group">
            <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                    <span class="icon fa fa-floppy-o"></span>&#32;
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>