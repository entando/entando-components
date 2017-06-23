<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="jpcontentscheduler.integrations" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.components" />
    </li>
    <li>
        <s:text name="jpcontentscheduler.admin.menu" />
    </li>
    <li class="page-title-container">
        <s:text name="title.contentList" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-4">
            <h1>
                <s:text name="jpcontentscheduler.admin.menu" />
                <span class="pull-right">
                    <s:text name="jpcontentscheduler.section.help" var="helpVar" />
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="${helpVar}" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-8">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.general" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewItem"/>">
                        <s:text name="jpcontentscheduler.admin.mail" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="viewUsers"/>">
                        <s:text name="jpcontentscheduler.admin.users" />
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="viewContentTypes"/>">
                        <s:text name="jpcontentscheduler.admin.contentTypes" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>
<div id="main" role="main">
    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>
    
    <s:form id="configurationForm" name="configurationForm" method="post" action="addContentTypes" cssClass="form-horizontal">
        <legend>
            <s:text name="jpcontentscheduler.label.addContentType" />
        </legend>
        <!-- Content Type -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.contentType" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.contentTypes']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:select name="threadConfig.contentTypes" id="threadConfig_contentTypes"
                    list="%{getContentTypes()}" listKey="code" listValue="description" headerKey=""
                    headerValue="%{getText('note.choose')}" cssClass="form-control" />
            </div>
        </div>
        <!-- dateStart attribute -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.startDateAttribute" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.startAttr']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="threadConfig.startAttr" id="threadConfig_startAttr"
                    placeholder="%{getText('jpcontentscheduler.label.startDateAttribute')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
        <!-- dateEnd attribute -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.endDateAttribute" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.idContentReplace']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="threadConfig.idContentReplace" id="threadConfig_idContentReplace"
                    placeholder="%{getText('jpcontentscheduler.label.endDateAttribute')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
        <!-- idContentReplace -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.contentReplaceId" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.idContentReplace']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="threadConfig.idContentReplace" id="threadConfig_idContentReplace"
                    placeholder="%{getText('jpcontentscheduler.label.contentReplaceId')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
<!--         modelIdContentReplace -->
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.contentReplaceModelId" />
            </label>
            <div class="col-sm-10">
                <s:set var="fieldErrorsVar" value="%{fieldErrors['threadConfig.modelIdContentReplace']}" />
                <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
                <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
                <wpsf:textfield name="threadConfig.modelIdContentReplace" id="threadConfig_modelIdContentReplace"
                    placeholder="%{getText('jpcontentscheduler.label.contentReplaceModelId')}" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}">
                            <s:property />
                            &#32;
                        </s:iterator>
                    </span>
                </s:if>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="jpcontentscheduler.label.suspend" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="active" id="active" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-12">
                <div class="pull-right">
                    <wpsf:submit name="save" type="button" action="#" cssClass="btn btn-primary">
                        <s:text name="%{getText('label.save')}" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>