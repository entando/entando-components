<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<legend><s:text name="jpseo.label.config" /></legend>

<%-- friendlyCode --%>
<s:set var="fieldErrorsVar" value="%{fieldErrors['friendlyCode']}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <label class="col-sm-2 control-label" for="friendlyCode">
        <s:text name="jpseo.label.friendlyCode" />
        <%-- &nbsp;<i class="fa fa-asterisk required-icon" style="position: relative; top: -4px; right: 0px"></i> --%>
        <%--
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="page.edit.code" />" data-placement="right"><span class="fa fa-info-circle"></span></a>
        --%>
    </label>
    <div class="col-sm-10">
        <wpsf:textfield name="friendlyCode" id="friendlyCode" value="%{#attr.friendlyCode}" cssClass="form-control" />
        <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
            </span>
        </s:if>
    </div>
</div>

<div class="form-checkbox form-group<s:property value="#controlGroupErrorClass" />">
    <div class="col-sm-3 control-label">
        <label class="display-block" for="useExtraDescriptions"><s:text name="jpseo.label.useBetterDescriptions" /></label>
    </div>
    <div class="col-sm-4">
        <wpsf:checkbox name="useExtraDescriptions" id="useExtraDescriptions" value="#attr.useExtraDescriptions" cssClass="bootstrap-switch" />
    </div>
</div>
