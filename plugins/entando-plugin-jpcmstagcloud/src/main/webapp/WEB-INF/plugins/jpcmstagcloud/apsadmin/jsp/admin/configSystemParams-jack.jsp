<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jpcmstagcloud.name" /></legend>
<p>
	<label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpcmstagcloud.hookpoint.configSystemParams.categoryRoot" />:</label>
	<s:set name="jpcmstagcloud_paramName" value="'jpcmstagcloud_categoryRoot'" />
        <s:if test="null == systemParams.get(#jpcmstagcloud_paramName) || systemParams.get(#jpcmstagcloud_paramName).trim().length() == 0" >
            <s:set var="jpcmstagcloud_categoryRootValueVar" value="'jptagcloud_categoryRoot'" />
        </s:if>
        <s:else><s:set var="jpcmstagcloud_categoryRootValueVar" value="systemParams.get(#jpcmstagcloud_paramName)" /></s:else>
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpcmstagcloud_paramName}" id="%{#jpcmstagcloud_paramName}" value="%{#jpcmstagcloud_categoryRootValueVar}" cssClass="text" />
        <wpsf:hidden name="%{#jpcmstagcloud_paramName + externalParamMarker}" value="true"/>
</p>
<p>
	<label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpcmstagcloud.hookpoint.configSystemParams.delayDays" />:</label>
	<s:set name="jpcmstagcloud_paramName" value="'jpcmstagcloud_delayDays'" />
        <s:if test="null == systemParams.get(#jpcmstagcloud_paramName)" >
            <s:set var="jpcmstagcloud_delayDaysValueVar" value="'-1'" />
        </s:if>
        <s:else><s:set var="jpcmstagcloud_delayDaysValueVar" value="systemParams.get(#jpcmstagcloud_paramName)" /></s:else>
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpcmstagcloud_paramName}" id="%{#jpcmstagcloud_paramName}" value="%{#jpcmstagcloud_delayDaysValueVar}" cssClass="text" />
	<wpsf:hidden name="%{#jpcmstagcloud_paramName + externalParamMarker}" value="true"/>
</p>
</fieldset>