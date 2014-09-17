<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="jpcasclient.title.casConfigManagement" /></h1>
<div id="main">
	
	<h2 class="margin-bit-bottom"><s:text name="jpcasclient.label.casclientConfig" /></h2> 

	<p>
		<s:text name="jpcasclient.label.casclientConfig.intro" />
	</p>
	
	<s:form action="save" >
		<s:if test="hasFieldErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.ActionErrors" /></h3>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
	 
		 <fieldset class="margin-more-top">
			<legend><s:text name="jpcasclient.legend.status" /></legend>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.active" id="active" cssClass="radiocheck" />&nbsp;<label for="active"><s:text name="jpcasclient.label.active" /></label>
			</p>
		</fieldset>
		
		<fieldset class="margin-more-top">
			<legend><s:text name="jpcasclient.legend.configuration" /></legend>

			<p>
				<label for="casLoginURL" class="basic-mint-label"><s:text name="jpcasclient.label.casLoginURL" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.casLoginURL" id="casLoginURL" cssClass="text" />
			</p>
			<p>
				<label for="casLogoutURL" class="basic-mint-label"><s:text name="jpcasclient.label.casLogoutURL" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.casLogoutURL" id="casLogoutURL" cssClass="text" />
			</p>
			<p>
				<label for="casValidateURL" class="basic-mint-label"><s:text name="jpcasclient.label.casValidateURL" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.casValidateURL" id="casValidateURL" cssClass="text" />
			</p>
			<p>
				<label for="serverBaseURL" class="basic-mint-label"><s:text name="jpcasclient.label.serverBaseURL" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.serverBaseURL" id="serverBaseURL" cssClass="text" />
			</p>
			<p>
				<label for="notAuthPage" class="basic-mint-label"><s:text name="jpcasclient.label.notAuthPage" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.notAuthPage" id="notAuthPage" cssClass="text" />
			</p>
			<p>
				<label for="realm" class="basic-mint-label"><s:text name="jpcasclient.label.realm" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.realm" id="realm" cssClass="text" />
			</p>

		</fieldset> 
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
		
	</s:form>
	
</div>