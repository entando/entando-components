<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12">
	<legend>
		<s:text name="jpweatherforecast.hookpoint.name" />
	</legend>

<%--
	<Param name="jpweatherforecast_username">username</Param>
--%>

	<div class="form-group">
		<s:set var="jpweatherforecast_paramName" value="%{'jpweatherforecast_username'}" />
		<label for="<s:property value="#jpweatherforecast_paramName"/>">
			<s:text name="jpweatherforecast.hookpoint.configSystemParams.username" />
		</label>
		<wpsf:textfield
			name="%{#jpweatherforecast_paramName}"
			id="%{#jpweatherforecast_paramName}"
			value="%{systemParams.get(#jpweatherforecast_paramName)}"
			cssClass="form-control"
			/>
		<wpsf:hidden name="%{#jpweatherforecast_paramName + externalParamMarker}" value="true" />
	</div>
	

</fieldset>
