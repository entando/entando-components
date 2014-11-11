<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jps3awsclient.name" /></legend>
	<p>
        <s:set name="jps3awsclient_paramName" value="'jps3awsclient_S3_active'" />
        <input type="checkbox" class="radiocheck" id="<s:property value="#jps3awsclient_paramName"/>" name="<s:property value="#jps3awsclient_paramName"/>" value="true" <s:if test="systemParams.get(#jps3awsclient_paramName)">checked="checked"</s:if> />
        <wpsf:hidden name="%{#jps3awsclient_paramName + externalParamMarker}" value="true"/>
        <label for="<s:property value="#jps3awsclient_paramName" />"><s:text name="jps3awsclient.label.s3amazon.active" /></label>
	</p>
	<p>
        <s:set name="jps3awsclient_paramName" value="'jps3awsclient_S3_accessKey'" />
        <label for="<s:property value="#jps3awsclient_paramName"/>" class="basic-mint-label"><s:text name="jps3awsclient.hookpoint.configSystemParams.s3key" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jps3awsclient_paramName}" id="%{#jps3awsclient_paramName}" value="%{systemParams.get(#jps3awsclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jps3awsclient_paramName + externalParamMarker}" value="true"/>
	</p>
	<p>
        <s:set name="jps3awsclient_paramName" value="'jps3awsclient_S3_secretKey'" />
        <label for="<s:property value="#jps3awsclient_paramName"/>" class="basic-mint-label"><s:text name="jps3awsclient.hookpoint.configSystemParams.s3secret" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jps3awsclient_paramName}" id="%{#jps3awsclient_paramName}" value="%{systemParams.get(#jps3awsclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jps3awsclient_paramName + externalParamMarker}" value="true"/>
	</p>
	<p>
        <s:set name="jps3awsclient_paramName" value="'jps3awsclient_S3_bucketName'" />
        <label for="<s:property value="#jps3awsclient_paramName"/>" class="basic-mint-label"><s:text name="jps3awsclient.hookpoint.configSystemParams.bucketName" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jps3awsclient_paramName}" id="%{#jps3awsclient_paramName}" value="%{systemParams.get(#jps3awsclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jps3awsclient_paramName + externalParamMarker}" value="true"/>
	</p>
	
</fieldset>