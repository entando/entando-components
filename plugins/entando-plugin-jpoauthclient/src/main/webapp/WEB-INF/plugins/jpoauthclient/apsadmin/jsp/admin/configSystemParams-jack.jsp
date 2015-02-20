<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jpoauthclient.name" /></legend>
	
	<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpoauthclient.hookpoint.configSystemParams.providerBaseURL" />:</label>
		<s:set name="jpoauthclient_paramName" value="'jpoauthclient_entandoProvider_baseURL'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpoauthclient_paramName}" id="%{#jpoauthclient_paramName}" value="%{systemParams.get(#jpoauthclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpoauthclient_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpoauthclient.hookpoint.configSystemParams.consumerKey" />:</label>
		<s:set name="jpoauthclient_paramName" value="'jpoauthclient_consumer_consumerKey'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpoauthclient_paramName}" id="%{#jpoauthclient_paramName}" value="%{systemParams.get(#jpoauthclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpoauthclient_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpoauthclient.hookpoint.configSystemParams.consumerSecret" />:</label>
		<s:set name="jpoauthclient_paramName" value="'jpoauthclient_consumer_consumerSecret'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpoauthclient_paramName}" id="%{#jpoauthclient_paramName}" value="%{systemParams.get(#jpoauthclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpoauthclient_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpoauthclient.hookpoint.configSystemParams.callbackURL" />:</label>
		<s:set name="jpoauthclient_paramName" value="'jpoauthclient_consumer_callbackURL'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpoauthclient_paramName}" id="%{#jpoauthclient_paramName}" value="%{systemParams.get(#jpoauthclient_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpoauthclient_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
		<s:text name="jpoauthclient.hookpoint.configSystemParams.reloadConfigurationInfo" />
	</p>
	
</fieldset>