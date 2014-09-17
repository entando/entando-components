<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1><wp:i18n key="jpuserreg_REGISTRATION"/></h1>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/register.action" />" method="post" class="form-horizontal" >
	
	<s:if test="hasFieldErrors()">
		<div class="alert alert-block">
			<h2><wp:i18n key="ERRORS" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<s:set name="label" ><s:property/></s:set>
						<li><s:property /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="alert alert-block">
			<h2><wp:i18n key="ERRORS" /></h2>
			<ul>
				<s:iterator value="actionErrors">
					<s:set name="label" ><s:property/></s:set>
					<li><s:property /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<s:set name="lang" value="defaultLang"></s:set>
	
	<p class="noscreen" >
		<wpsf:hidden name="profileTypeCode" />
	</p>
	
	<div class="control-group">
		<div class="controls">
			<label for="privacyPolicyAgreement" class="control-label">
			<wpsf:checkbox useTabindexAutoIncrement="true" name="privacyPolicyAgreement" id="privacyPolicyAgreement" />&nbsp;
			<wp:i18n key="jpuserreg_PRIVACY_AGREEMENT"/>&nbsp;<abbr class="icon icon-asterisk" title="<wp:i18n key="jpuserreg_REQUIRED" />">
			<span class="noscreen"><wp:i18n key="jpuserreg_REQUIRED" /></span></abbr>
			</label>
		</div>
	</div>
	
	<div class="control-group">
		<label for="username" class="control-label"><wp:i18n key="jpuserreg_USERNAME"/><abbr class="icon icon-asterisk" title="<wp:i18n key="jpuserreg_REQUIRED" />">
		<span class="noscreen"><wp:i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
		<div class="controls">	
			<wpsf:textfield useTabindexAutoIncrement="true" name="username" id="username" maxlength="40" />
		</div>
	</div>
	
	<%-- START CICLO ATTRIBUTI --%>
	<s:iterator value="userProfile.attributeList" id="attribute">
	<s:if test="%{#attribute.active}">
		<%-- INIZIALIZZAZIONE TRACCIATORE --%>
		<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
		<s:set var="i18n_attribute_name">userprofile_<s:property value="userProfile.typeCode" />_<s:property value="#attribute.name" /></s:set>
		<s:set var="attribute_id">userprofile_<s:property value="#attribute.name" /></s:set>
		<s:include value="/WEB-INF/aps/jsp/internalservlet/user/inc/iteratorAttribute.jsp" />
		<s:if test="%{#attribute.name == emailAttrName}" >
		<div class="control-group">
			<label for="eMailConfirm" class="control-label"><wp:i18n key="jpuserreg_EMAIL_CONFIRM"/> <abbr class="icon icon-asterisk" title="<wp:i18n key="jpuserreg_REQUIRED" />">
			<span class="noscreen"><wp:i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
			<div class="controls">
				<wpsf:textfield useTabindexAutoIncrement="true" name="emailConfirm" id="eMailConfirm" />
			</div>
		</div>
		</s:if>
	</s:if>
	</s:iterator>
	<%-- END CICLO ATTRIBUTI --%>
	
	<s:set var="savelabel"><wp:i18n key="jpuserreg_SAVE" /></s:set>
	<p class="form-actions"><wpsf:submit useTabindexAutoIncrement="true" value="%{savelabel}" cssClass="btn btn-primary" /></p>
	
</form>