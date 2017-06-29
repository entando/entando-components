<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li class="page-title-container">
        <s:text name="jpuserreg.title.config" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="jpuserreg.title.config" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                data-content="<s:text name="jpuserreg.menu.userregAdmin.help"/>" data-placement="left"
                data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator">
        <s:text name="label.requiredFields" />
    </div>
</div>
<br>
<div id="main" role="main">
    <s:form action="save" class="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong>
                    <s:text name="message.title.FieldErrors" />
                </strong>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li>
                                <s:property />
                            </li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong>
                    <s:text name="message.title.ActionErrors" />
                </strong>
                <ul class="margin-base-vertical">
                    <s:iterator value="actionErrors">
                        <li>
                            <s:property />
                        </li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>
        <legend>
            <span class="control-label label-group-name">
                <s:text name="label.info" />
            </span>
        </legend>
        <p>
            <s:iterator var="defaultAuthVar" value="config.defaultCsvAuthorizations">
                <wpsf:hidden name="config.defaultCsvAuthorizations" value="%{#defaultAuthVar}" />
            </s:iterator>
        </p>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="tokenValidityMinutes">
                <s:text name="jpuserreg.tokenValidityMinutes" />
            </label>
            <div class="col-sm-10">
                <wpsf:select id="tokenValidityMinutes" name="config.tokenValidityMinutes"
                    list="#{1440: '1 '+getText('day'),
                             2880: '2 '+getText('days'),
                             4320: '3 '+getText('days'),
                             5760: '4 '+getText('days'),
                             7200: '5 '+getText('days'),
                             8640: '6 '+getText('days'),
                             10080: '7 '+getText('days'),
                             20160: '14 '+getText('days'),
                             43200: '30 '+getText('days')
                            }"
                    cssClass="form-control" />
            </div>
        </div>
        <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
            <s:param name="inputCode" value="%{'config.eMailSenderCode'}"/>
        </s:include>
        <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
            <label class="col-sm-2 control-label" for="eMailSenderCode">
                <s:text name="jpuserreg.eMailSenderCode" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="config.senderCode" id="eMailSenderCode" list="mailSenders" listKey="key"
                    listValue="value" headerKey="" headerValue="%{getText('note.choose')}" cssClass="form-control">
                </wpsf:select>
                <s:if test="#inputHasFieldErrorVar">
                    <p class="text-danger no-mb"><s:iterator value="#inputFieldErrorsVar">
                            <s:property />
                        </s:iterator>
                    </p>
                </s:if>
            </div>
        </div>
        <legend>
            <span class="control-label label-group-name">
                <s:text name="jpuserreg.defaultAuthorizations" />
            </span>
        </legend>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="userGroup">
                <s:text name="jpuserreg.label.userGroup" />
            </label>
            <div class="col-sm-10">
                <wpsf:select id="userGroup" name="groupName" list="systemGroups" listKey="name" listValue="description"
                    cssClass="form-control" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="userRole">
                <s:text name="jpuserreg.label.userRole" />
            </label>
            <div class="col-sm-10">
                <div class="input-group">
                    <wpsf:select id="userRole" name="roleName" list="systemRoles" headerKey="" headerValue="%{getText('note.choose')}"
                        listKey="name" listValue="description" cssClass="combobox form-control" />
                    <span class="input-group-btn">
                        <wpsf:submit type="button" action="addAuthorization" cssClass="btn btn-primary">
                            <span class="icon fa fa-plus"></span>
                        </wpsf:submit>
                    </span>
                </div>
                <h4 class="mt-20">
                    <s:text name="jpuserreg.note.authorizationsList" />
                </h4>
                <s:set var="userCvsAuthorizationsVar" value="%{config.defaultCsvAuthorizations}" />
                <s:if test="%{#userCvsAuthorizationsVar.size()>0}">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered">
                            <thead>
                                <tr>
                                    <th>
                                        <s:text name="jpuserreg.label.userGroup" />
                                    </th>
                                    <th>
                                        <s:text name="jpuserreg.label.userRole" />
                                    </th>
                                    <th class="text-center text-nowrap col-sm-1">
                                        <s:text name="label.actions" />
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="#userCvsAuthorizationsVar" var="userCvsAuthorizationVar"
                                    status="elementStatus">
                                    <s:set var="userCvsAuthorizationParamsVar"
                                        value="%{#userCvsAuthorizationVar.split(',')}" />
                                    <tr>
                                        <td>
                                            <s:set var="groupVar" value="%{getGroup(#userCvsAuthorizationParamsVar[0])}" />
                                            <s:if test="null != #groupVar">
                                                <s:property value="#groupVar.description" />
                                                &#32;
                                                <s:property value="#groupVar.name" />
                                            </s:if>
                                            <s:else>&ndash;</s:else>
                                        </td>
                                        <td>
                                            <s:set var="roleVar" value="%{getRole(#userCvsAuthorizationParamsVar[1])}" />
                                            <s:if test="null != #roleVar">
                                                <s:property value="#roleVar.description" />
                                                &#32;
                                                <s:property value="#roleVar.name" />
                                            </s:if>
                                            <s:else>&ndash;</s:else>
                                        </td>
                                        <td class="text-center text-nowrap">
                                            <s:set var="elementIndexVar" value="#elementStatus.index" />
                                            <div class="btn-group btn-group-xs">
                                                <wpsa:actionParam action="removeAuthorization" var="actionName">
                                                    <wpsa:actionSubParam name="csvAuthorization"
                                                        value="%{#userCvsAuthorizationVar}" />
                                                </wpsa:actionParam>
                                                <wpsf:submit action="%{#actionName}" type="button"
                                                    value="%{getText('label.remove')}"
                                                    title="%{getText('label.remove')}" cssClass="btn btn-menu-right btn-link">
                                                    <span class="icon fa fa-trash-o fa-lg"></span>
                                                </wpsf:submit>
                                            </div>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </s:if>
                <s:else>
                    <div class="alert alert-info">
                        <span class="pficon pficon-info"></span>
                        <s:text name="jpuserreg.note.userAuthorizations.empty" />
                    </div>
                </s:else>
            </div>
        </div>
        <!-- Activation -->
        <legend>
            <span class="control-label label-group-name">
                <s:text name="jpuserreg.activation" />
            </span>
        </legend>
        <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
            <s:param name="inputCode" value="%{'config.activationPageCode'}" />
        </s:include>
        <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
            <label class="col-sm-2 control-label" for="activationPageCode">
                <s:text name="jpuserreg.activationPageCode" />&nbsp;
                <i class="fa fa-asterisk required-icon"></i>
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                    data-content="<s:property value="%{getText('jpuserreg.activationPageCode.help')}" escapeHtml="false" />"
                    data-placement="top">
                    <span class="fa fa-info-circle"></span>
                </a>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="config.activationPageCode" id="activationPageCode" list="%{activationPages}"
                    listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" headerKey=""
                    headerValue="%{getText('note.choose')}" cssClass="form-control" />
                <s:if test="#inputHasFieldErrorVar">
                    <p class="text-danger no-mb"><s:iterator value="#inputFieldErrorsVar">
                            <s:property />
                        </s:iterator>
                    </p>
                </s:if>
            </div>
        </div>
        <s:iterator var="langVar" value="langs">
            <s:set var="template" value="%{config.activationTemplates.get(#langVar.code)}" />
            <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
                <s:param name="inputCode" value="%{'config.activationTemplates'}" />
            </s:include>
            <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
                <label class="col-sm-2 control-label" for="config_activationTemplates_subject_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code" /></code>&nbsp;
                    <s:text name="jpuserreg.label.subject" />&nbsp;
                    <i class="fa fa-asterisk required-icon"></i>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:property value='%{getText("jpuserreg.label.activation.subject.help")}' escapeHtml="false" />"
                        data-placement="top">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield value="%{#template.subject}"
                        name="config.activationTemplates['%{#langVar.code}'].subject"
                        id="config_activationTemplates_subject_%{#langVar.code}" cssClass="form-control" />
                    <s:if test="#inputHasFieldErrorVar">
                        <p class="text-danger no-mb">
                            <s:property value="#inputFieldErrorsVar[0]"/>
                        </p>
                    </s:if>
                </div>
            </div>
            <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
                <s:param name="inputCode" value="%{'config.activationTemplates'}" />
            </s:include>
            <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
                <label class="col-sm-2 control-label" for="config_activationTemplates_body_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code" /></code>&nbsp;
                    <s:text name="jpuserreg.label.body" />&nbsp;
                    <i class="fa fa-asterisk required-icon"></i>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:property value='%{getText("jpuserreg.label.activation.body.help")}' escapeHtml="false" />"
                        data-placement="top">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-10">
                    <wpsf:textarea value="%{#template.body}" name="config.activationTemplates['%{#langVar.code}'].body"
                        id="config_activationTemplates_body_%{#langVar.code}" cssClass="form-control" rows="8" cols="50" />
                    <s:if test="#inputHasFieldErrorVar">
                        <p class="text-danger no-mb">
                            <s:property value="#inputFieldErrorsVar[1]"/>
                        </p>
                    </s:if>
                </div>
            </div>
        </s:iterator>
        <!-- Reactivation -->
        <legend>
            <span class="control-label label-group-name">
                <s:text name="jpuserreg.reactivation" />
            </span>
        </legend>
        <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
            <s:param name="inputCode" value="%{'config.reactivationPageCode'}" />
        </s:include>
        <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
            <label class="col-sm-2 control-label" for="reactivationPageCode">
                <s:text name="jpuserreg.reactivationPageCode" />&nbsp;
                <i class="fa fa-asterisk required-icon"></i>
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                    data-content="<s:property value="%{getText('jpuserreg.reactivationPageCode.help')}" escapeHtml="false" />"
                    data-placement="top">
                    <span class="fa fa-info-circle"></span>
                </a>
            </label>
            <div class="col-sm-10">
                <wpsf:select name="config.reactivationPageCode" id="reactivationPageCode" list="%{reactivationPages}"
                    listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" headerKey=""
                    headerValue="%{getText('note.choose')}" cssClass="form-control" />
                <s:if test="#inputHasFieldErrorVar">
                    <p class="text-danger no-mb"><s:iterator value="#inputFieldErrorsVar">
                            <s:property />
                        </s:iterator>
                    </p>
                </s:if>
            </div>
        </div>
        <s:iterator var="lang" value="langs">
            <s:set var="template" value="%{config.reactivationTemplates.get(#lang.code)}" />
            <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
                <s:param name="inputCode" value="%{'config.reactivationTemplates'}" />
            </s:include>
            <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
                <label class="col-sm-2 control-label"
                    for="config_activationTemplates_subject_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code" /></code>&nbsp;
                    <s:text name="jpuserreg.label.subject" />&nbsp;
                    <i class="fa fa-asterisk required-icon"></i>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:property value='%{getText("jpuserreg.label.reactivation.subject.help")}' escapeHtml="false" />"
                        data-placement="top">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-10">
                    <wpsf:textfield value="%{#template.subject}"
                        name="config.reactivationTemplates['%{#lang.code}'].subject"
                        id="config_reactivationTemplates_subject_%{#lang.code}" cssClass="form-control" />
                    <s:if test="#inputHasFieldErrorVar">
                        <p class="text-danger no-mb">
                            <s:property value="#inputFieldErrorsVar[0]"/>
                        </p>
                    </s:if>
                </div>
            </div>
            <s:include value="/WEB-INF/plugins/jpuserreg/apsadmin/jsp/config/inc/form-errors.jsp">
                <s:param name="inputCode" value="%{'config.reactivationTemplates'}" />
            </s:include>
            <div class="form-group ${(inputHasFieldErrorVar)?'has-error':''}">
                <label class="col-sm-2 control-label"
                    for="config_activationTemplates_body_<s:property value="#langVar.code"/>">
                    <code class="label label-info"><s:property value="#langVar.code" /></code>&nbsp;
                    <s:text name="jpuserreg.label.body" />&nbsp;
                    <i class="fa fa-asterisk required-icon"></i>
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:property value='%{getText("jpuserreg.label.reactivation.body.help")}' escapeHtml="false" />"
                        data-placement="top">
                        <span class="fa fa-info-circle"></span>
                    </a>
                </label>
                <div class="col-sm-10">
                    <wpsf:textarea value="%{#template.body}" name="config.reactivationTemplates['%{#lang.code}'].body"
                        id="config_reactivationTemplates_body_%{#lang.code}" cssClass="form-control" rows="8" cols="50" />
                    <s:if test="#inputHasFieldErrorVar">
                        <p class="text-danger no-mb">
                            <s:property value="#inputFieldErrorsVar[1]"/>
                        </p>
                    </s:if>
                </div>
            </div>
        </s:iterator>
        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-12 margin-small-vertical">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="%{getText('label.save')}" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
