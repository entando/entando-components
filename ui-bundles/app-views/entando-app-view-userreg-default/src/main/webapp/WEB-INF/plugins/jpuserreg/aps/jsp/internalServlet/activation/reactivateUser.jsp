<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1><wp:i18n key="jpuserreg_PASSWORD_RECOVERY" /></h1>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/reactivate.action"/>" method="post" class="form-horizontal" >

	<s:if test="hasFieldErrors()">
		<div class="alert alert-block">
			<h2><wp:i18n key="ERRORS" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
			            <li><s:property/></li>
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
					<li><s:property/></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<p class="noscreen">
		<wpsf:hidden name="token" value="%{token}" />
	</p>
	<div class="control-group">
		<label for="password" class="control-label"><wp:i18n key="jpuserreg_PASSWORD"/>&nbsp;<abbr class="icon icon-asterisk" title="<wp:i18n key="jpuserreg_REQUIRED" />">
			<span class="noscreen"><wp:i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
		<div class="controls">
			<wpsf:password useTabindexAutoIncrement="true" name="password" id="password" maxlength="20" />
		</div>
	</div>
	<div class="control-group">
		<label for="passwordConfirm" class="control-label"><wp:i18n key="jpuserreg_PASSWORD_CONFIRM"/>&nbsp;<abbr class="icon icon-asterisk" title="<wp:i18n key="jpuserreg_REQUIRED" />">
			<span class="noscreen"><wp:i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
		<div class="controls">
			<wpsf:password useTabindexAutoIncrement="true" name="passwordConfirm" id="passwordConfirm" maxlength="20" />
		</div>
	</div>
	<p class="form-actions">
		<input type="submit" value="<wp:i18n key="jpuserreg_SAVE"/>" cssClass="btn btn-primary" />
	</p>
	
</form>