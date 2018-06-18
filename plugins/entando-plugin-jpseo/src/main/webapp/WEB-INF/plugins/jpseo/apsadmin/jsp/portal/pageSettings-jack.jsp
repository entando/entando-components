<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<%-- robot.txt --%>
<s:set var="fieldErrorsVar" value="%{fieldErrors['robotContent']}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <label class="col-sm-2 control-label" for="xmlConfig">
        <s:text name="jpseo.label.robotContent" />
    </label>
    <div class="col-sm-10">
        <wpsf:textarea cols="50" rows="5" name="robotContent" id="robotContent" value="%{#attr.robotContent}" cssClass="form-control" />
        <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
            </span>
        </s:if>
    </div>
</div>
