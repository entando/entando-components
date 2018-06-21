<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<div>
<legend><s:text name="jpseo.legend.seoPageSettings" /></legend>

<div class="row">
<%-- defaultFilePath --%>
<div class="form-group">
    <label class="col-sm-2 control-label" for="defaultRobotFilePath">
        <s:text name="jpseo.label.defaultRobotFilePath" />
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" 
           title="" data-content="<s:text name="jpseo.label.defaultRobotFilePath.help" />" 
           data-placement="top" data-original-title=""><span class="fa fa-info-circle"></span></a>
    </label>
    <div class="col-sm-10">
        <code>robots.txt</code>
    </div>
</div>
</div>

<div class="row">
<%-- robotFilePath --%>
<s:set var="fieldErrorsVar" value="%{fieldErrors['robotFilePath']}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <label class="col-sm-2 control-label" for="robotFilePath">
        <s:text name="jpseo.label.robotFilePath" />
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" 
           title="" data-content="<s:text name="jpseo.label.robotFilePath.help" />" 
           data-placement="top" data-original-title=""><span class="fa fa-info-circle"></span></a>
    </label>
    <div class="col-sm-8">
        <wpsf:textfield name="robotFilePath" id="robotFilePath" value="%{#attr.robotFilePath}" cssClass="form-control" />
        <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
            </span>
        </s:if>
    </div>
    <div class="col-sm-2"> 
        <wpsf:submit name="/do/jpseo/Page/Settings" action="setRobotsPath" type="button" cssClass="btn btn-primary pull-right">
            <s:text name="jpseo.label.setRobotsFilePath" />
        </wpsf:submit>
    </div>
</div>
</div>
        
<br />

<div class="row">
<%-- robot.txt --%>
<s:set var="fieldErrorsVar" value="%{fieldErrors['robotContent']}" />
<s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
<s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
<div class="form-group<s:property value="#controlGroupErrorClass" />">
    <div class="col-sm-12">
        <wpsf:textarea cols="50" rows="5" name="robotContent" id="robotContent" value="%{#attr.robotContent}" cssClass="form-control" />
        <s:if test="#hasFieldErrorVar">
            <span class="help-block text-danger">
                <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
            </span>
        </s:if>
    </div>
</div>
</div>

</div>
        
<br />
