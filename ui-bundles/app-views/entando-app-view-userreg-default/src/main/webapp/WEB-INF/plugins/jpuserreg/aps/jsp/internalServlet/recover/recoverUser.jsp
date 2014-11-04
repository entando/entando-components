<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1><wp:i18n key="jpuserreg_REACTIVATION_PASSWORD_LOST_MSG"/></h1>

<form method="post" action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/recoverFromUsername.action" />" class="form-horizontal" >
	<s:if test="hasFieldErrors()">
		<div class="alert alert-block">
		<h2><wp:i18n key="ERRORS" /></h2>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<s:set name="label">
						<s:property />
					</s:set>
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
				<s:set name="label">
					<s:property />
				</s:set>
				<li><s:property /></li>
			</s:iterator>
		</ul>
		</div>
	</s:if>
	
	<div class="control-group">
		<label for="username" class="control-label"><wp:i18n key="jpuserreg_USERNAME"/></label>
		<div class="controls">
			<wpsf:textfield useTabindexAutoIncrement="true" name="username" id="username" />
		</div>
	</div>
	<p class="form-actions">
		<input type="submit" value="<wp:i18n key="jpuserreg_SEND"/>" class="btn btn-primary" />
	</p>
</form>

<h1><wp:i18n key="jpuserreg_REACTIVATION_USERNAME_LOST_MSG"/></h1>

<form method="post" action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/recoverFromEmail.action" />" class="form-horizontal" >
	
	<div class="control-group">
		<label for="email" class="control-label"><wp:i18n key="jpuserreg_EMAIL"/></label>
		<div class="controls">
			<wpsf:textfield useTabindexAutoIncrement="true" name="email" id="email" />
		</div>
	</div>
	<p class="form-actions">	
		<input type="submit" value="<wp:i18n key="jpuserreg_SEND"/>" class="btn btn-primary" />
	</p>
</form>
