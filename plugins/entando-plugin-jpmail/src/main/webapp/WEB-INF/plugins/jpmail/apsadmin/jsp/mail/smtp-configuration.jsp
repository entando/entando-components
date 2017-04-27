<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.uxcomponents"/></li>
    <li><s:text name="breadcrumb.jpmail"/></li>
    <li><s:text name="breadcrumb.smtpConfig" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="jpmail.admin.menu.smtp"/>
                <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-content="TO be inserted" data-placement="left" data-original-title="">
                    <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url namespace="/do/jpmail/MailConfig" action="viewSenders" />"><s:text name="title.eMailManagement.sendersConfig"/></a>
                </li>
                <li class="active">
                    <a href="<s:url namespace="/do/jpmail/MailConfig" action="editSmtp" />"><s:text name="jpmail.admin.menu.smtp"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>

<br/>

<div id="main">
    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="label.jpmail.intro" />
        </div>
    </div>
    <div id="messages">
        <s:include value="/WEB-INF/plugins/jpmail/apsadmin/jsp/mail/inc/smtp-messages.jsp" />
    </div>

    <s:form id="configurationForm" name="configurationForm" method="post" action="testSmtp" cssClass="form-horizontal">
    <div class="col-xs-12 margin-large-top">
        <legend><s:text name="legend.generalSettings" /></legend>
        <div class="form-group">
            <div class="row">
                <label class="col-sm-2 control-label">
                    <s:text name="label.active" />
                </label>
                <div class="col-sm-10">
                    <s:set var="paramName" value="'active'" />
                    <input type="hidden"
                           value="65"
                           id="<s:property value="#paramName"/>"
                           name="<s:property value="#paramName"/>" />
                    <input type="checkbox"
                           id="ch_<s:property value="#paramName"/>"
                           value="true"
                           class="bootstrap-switch"
                           <s:if test="%{systemParams[#paramName] == 'true'%}">checked="checked"</s:if>/>
                </div>
            </div>

            <div class="row">
                <label class="col-sm-2 control-label">
                    <s:text name="label.debug" />
                </label>
                <div class="col-sm-10">
                    <s:set var="paramName" value="'debug'" />
                    <input type="hidden"
                           value="<s:property value="systemParams[#paramName]" />"
                           id="<s:property value="#paramName"/>"
                           name="<s:property value="#paramName"/>" />
                    <input type="checkbox"
                           id="ch_<s:property value="#paramName"/>"
                           value="<s:property value="systemParams[#paramName]" />"
                           class="bootstrap-switch"
                           <s:if test="systemParams[#paramName] == 'true'">checked="checked"</s:if>/>
                </div>
            </div>
        </div>

        <legend><s:text name="legend.connection" /></legend>
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
                <%-- smtpHost --%>
            <s:set var="fieldErrorsVar" value="%{fieldErrors['smtpHost']}" />
            <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
            <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
            <div class="row">
                <label class="col-sm-2 control-label" for="smtpHost">
                    <s:text name="label.smtpHost" />
                    <i class="fa fa-asterisk required-icon"></i>
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield name="smtpHost" id="smtpHost" cssClass="form-control" />
                    <s:if test="#hasFieldErrorVar">
                            <span class="help-block text-danger">
                                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                            </span>
                    </s:if>
                </div>
            </div>

            <div class="row">
                <label class="col-sm-2 control-label" for="smtpPort">
                    <s:text name="label.smtpPort" />
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield name="smtpPort" id="smtpPort" cssClass="form-control" />
                </div>
            </div>

            <div class="row">
                <label class="col-sm-2 control-label">
                    <s:text name="label.security" />
                </label>
                <div class="col-sm-10">
                    <s:set var="paramName" value="'smtpProtocol'" />
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-default <s:if test="%{null == smtpProtocol || smtpProtocol == 'default'}"> active</s:if>">
                            <input type="radio" class="radiocheck" id="smtpstd"
                                   name="<s:property value="#paramName"/>"
                                   value="0"
                                   <s:if test="systemParams[#paramName] == '1'">checked="checked"</s:if>/>
                            <s:text name="label.smtp.standard"/>
                        </label>
                        <label class="btn btn-default <s:if test="systemParams[#paramName] == '1')">active</s:if>">
                            <input type="radio" class="radiocheck" id="smtpssl"
                                   name="<s:property value="#paramName"/>"
                                   value="1"
                                   <s:if test="systemParams[#paramName] == '1'">checked="checked"</s:if>/>
                            <s:text name="label.smtp.ssl"/>
                        </label>
                        <label class="btn btn-default <s:if test="systemParams[#paramName] == '2'">active</s:if>">
                            <input type="radio" class="radiocheck" id="smtptls"
                                   name="<s:property value="#paramName"/>"
                                   value="2"
                                   <s:if test="systemParams[#paramName] == '2'">checked="checked"</s:if>/>
                            <s:text name="label.smtp.tls"/>
                        </label>
                    </div>
                </div>
            </div>

            <div class="row">
                <label class="col-sm-2 control-label" for="smtpTimeout">
                    <s:text name="label.smtpTimeout" />
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield name="smtpTimeout" id="smtpTimeout" cssClass="form-control" />
                </div>
            </div>
        </div>

        <legend><s:text name="legend.authentication" /></legend>
        <div class="form-group">
            <div class="row">
                <label class="col-sm-2 control-label" for="smtpUserName">
                    <s:text name="smtpUserName" />
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield name="smtpUserName" id="smtpUserName" cssClass="form-control" />
                </div>
            </div>

            <div class="row">
                <label class="col-sm-2 control-label" for="smtpPassword">
                    <s:text name="smtpPassword" />
                </label>
                <div class="col-sm-10">
                    <wpsf:hidden value="%{getSmtpPassword()}" />
                    <wpsf:password  name="smtpPassword" id="smtpPassword" cssClass="form-control" />
                </div>
            </div>
        </div>
        </fieldset>

        <div class="col-xs-12">
            <div class="form-group pull-right">
                <sj:submit parentTheme="simple" formIds="configurationForm" value="%{getText('label.testConnection')}" targets="messages" cssClass="btn btn-default"/>
                <wpsf:submit name="testMail"  value="%{getText('label.sendEmail')}" action="testMail" cssClass="btn btn-default" onclick="overrideSubmit('testMail')"/>
                <wpsf:submit name="save" type="button" action="saveSmtp" cssClass="btn btn-primary" onclick="overrideSubmit('saveSmtp')">
                    <s:text name="%{getText('label.save')}"/>
                </wpsf:submit>
            </div>
        </div>

        </s:form>
    </div>
