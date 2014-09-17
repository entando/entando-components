<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:info key="currentLang" var="currentLang" />
<form 
	class="form-horizontal jpwebdynamicform-choose-type"  
	action="<wp:action path="/ExtStr2/do/jpwebdynamicform/Message/User/new.action"/>" 
	method="post"
	>
		<div class="control-group">
			<label class="control-label" for="jpwebdynamicform_typecode"><wp:i18n key="jpwebdynamicform_MESSAGETYPE" /></label>
			<div class="controls">
				<select name="typeCode" tabindex="<wpsa:counter/>" id="jpwebdynamicform_typecode" class="text">
					<s:iterator value="messageTypes" var="messageType" >
						<s:set name="optionDescr">jpwebdynamicform_TITLE_<s:property value="#messageType.code"/></s:set>
						<option value="<s:property value="#messageType.code"/>"><wp:i18n key="${optionDescr}" /></option>
					</s:iterator>
				</select>
			</div>
		</div>
		<p class="form-actions">
			<wp:i18n key="jpwebdynamicform_CHOOSE_TYPE" var="jpwebdynamicform_CHOOSE_TYPE" />
			<wpsf:submit 
				useTabindexAutoIncrement="true" 
				value="%{#attr.jpwebdynamicform_CHOOSE_TYPE}" 
				cssClass="btn btn-primary" 
				/>
		</p>
</form>