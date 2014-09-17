<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset class="col-xs-12">
    <legend><s:text name="jpwebdynamicform.name" /></legend>

    <div class="form-group">
        <s:set name="jpwebdynamicform_paramName" value="'jpwebdynamicform_recaptcha_publickey'" />
        <label for="<s:property value="#jpwebdynamicform_paramName"/>"><s:text name="jpwebdynamicform.recaptcha.publickey" /></label>
        <wpsf:textfield name="%{#jpwebdynamicform_paramName}" id="%{#jpwebdynamicform_paramName}" value="%{systemParams.get(#jpwebdynamicform_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpwebdynamicform_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpwebdynamicform_paramName" value="'jpwebdynamicform_recaptcha_privatekey'" />
        <label for="<s:property value="#jpwebdynamicform_paramName"/>"><s:text name="jpwebdynamicform.recaptcha.privatekey" /></label>
        <wpsf:textfield name="%{#jpwebdynamicform_paramName}" id="%{#jpwebdynamicform_paramName}" value="%{systemParams.get(#jpwebdynamicform_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpwebdynamicform_paramName + externalParamMarker}" value="true"/>
    </div>
</fieldset>