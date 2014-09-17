<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h1><wp:i18n key="jpuserreg_REGISTRATION"/></h1>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/initRegistration"/>" method="post" class="form-horizontal" >
		<div class="control-group">
			<label for="profileTypeCode" class="control-label"><wp:i18n key="jpuserreg_PROFILETYPE" />:</label>
			<div class="controls">
			<select name="profileTypeCode" tabindex="<wpsa:counter/>" id="profileTypeCode" class="text">
				<s:iterator value="profileTypes" var="profileType" >
					<s:set name="optionDescr">jpuserreg_TITLE_<s:property value="#profileType.typeCode"/></s:set>
				    <option value="<s:property value="#profileType.typeCode"/>"><wp:i18n key="${optionDescr}" /></option>
				</s:iterator>
			</select>
			</div>
		</div>
		<p class="form-actions">
			<s:set name="labelChoose"><wp:i18n key="jpuserreg_CHOOSE_TYPE" /></s:set>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{#labelChoose}" cssClass="btn btn-primary"/>
		</p>
</form> 