<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.uxcomponents"/></li>
    <li><s:text name="breadcrumb.jpmail"/></li>
    <s:if test="%{strutsAction==1}" ><li><s:text name="title.eMailManagement.newSender" /></li></s:if>
    <s:else><li><s:text name="title.eMailManagement.editSender" />:&nbsp;<s:property value="code"/></li></s:else>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:if test="%{strutsAction==1}"><s:text name="title.eMailManagement.newSender" /></s:if>
                <s:else><s:text name="title.eMailManagement.editSender" /></s:else>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="TO be inserted" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
            <div class="text-right">
                <s:text name="label.requiredFields" />
            </div>
        </div>
    </div>
</div>
<br>

<div id="main">
    <s:form action="saveSender" cssClass="form-horizontal">
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false" /></li>
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
                        <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
                </ul>
            </div>
        </s:if>

        <p class="noscreen">
            <wpsf:hidden name="strutsAction"/>
            <s:if test="%{strutsAction==2}" ><wpsf:hidden name="code"/></s:if>
        </p>

        <%-- code --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['code']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="code">
                <s:text name="code" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="code" id="code" cssClass="form-control" disabled="%{strutsAction==2}" />
                <s:if test="#hasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
                </s:if>
            </div>
        </div>

        <%-- mail --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['mail']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />

        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="mail">
                <s:text name="mail" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="mail" id="mail" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
				<span class="help-block text-danger">
					<s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
				</span>
                </s:if>
            </div>
        </div>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-12 margin-small-vertical">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.save" />
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>