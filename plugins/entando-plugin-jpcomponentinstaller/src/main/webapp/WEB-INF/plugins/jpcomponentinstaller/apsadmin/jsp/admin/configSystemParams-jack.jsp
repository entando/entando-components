<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset class="col-xs-12">
	<legend><s:text name="jpcomponentinstaller.name" /></legend>
	<div class="form-group">
        <s:set name="jpcomponentinstaller_paramName" value="'jpcomponentinstaller_nexus_active'" />
        <input type="checkbox" class="radiocheck" id="<s:property value="#jpcomponentinstaller_paramName"/>" name="<s:property value="#jpcomponentinstaller_paramName"/>" value="true" <s:if test="systemParams.get(#jpcomponentinstaller_paramName)">checked="checked"</s:if> />
        <wpsf:hidden name="%{#jpcomponentinstaller_paramName + externalParamMarker}" value="true"/>
        <label for="<s:property value="#jpcomponentinstaller_paramName" />"><s:text name="jpcomponentinstaller.hookpoint.configSystemParams.nexus.active" /></label>
	</div>
	<div class="form-group">
        <s:set name="jpcomponentinstaller_paramName" value="'jpcomponentinstaller_nexus_url'" />
        <label for="<s:property value="#jpcomponentinstaller_paramName"/>"><s:text name="jpcomponentinstaller.hookpoint.configSystemParams.nexus.url" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpcomponentinstaller_paramName}" id="%{#jpcomponentinstaller_paramName}" value="%{systemParams.get(#jpcomponentinstaller_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpcomponentinstaller_paramName + externalParamMarker}" value="true"/>
	</div>
	<div class="form-group">
        <s:set name="jpcomponentinstaller_paramName" value="'jpcomponentinstaller_nexus_username'" />
        <label for="<s:property value="#jpcomponentinstaller_paramName"/>"><s:text name="jpcomponentinstaller.hookpoint.configSystemParams.nexus.username" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpcomponentinstaller_paramName}" id="%{#jpcomponentinstaller_paramName}" value="%{systemParams.get(#jpcomponentinstaller_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpcomponentinstaller_paramName + externalParamMarker}" value="true"/>
	</div>
	<div class="form-group">
        <s:set name="jpcomponentinstaller_paramName" value="'jpcomponentinstaller_nexus_password'" />
        <label for="<s:property value="#jpcomponentinstaller_paramName"/>"><s:text name="jpcomponentinstaller.hookpoint.configSystemParams.nexus.password" />:</label>
		<wpsf:password useTabindexAutoIncrement="true" name="%{#jpcomponentinstaller_paramName}" id="%{#jpcomponentinstaller_paramName}" value="%{systemParams.get(#jpcomponentinstaller_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpcomponentinstaller_paramName + externalParamMarker}" value="true"/>
	</div>
</fieldset>