<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><s:text name="title.solrclientConfig" /></h1>
<div id="main">
	<s:form namespace="/do/jpsolrclient/Config">
	    <s:hidden name="_csrf" value="%{csrfToken}"/>
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
				<h4><s:text name="message.title.FieldErrors" /></h4>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escapeHtml="false" /></li>
							</s:iterator>
						</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
			<div class="message message_confirm">
				<h4><s:text name="messages.confirm" /></h4>
				<ul>
					<s:iterator value="actionMessages">
						<li><s:property escapeHtml="false" /></li>
						</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="message message_error">
				<h4><s:text name="message.title.ActionErrors" /></h4>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property escapeHtml="false" /></li>
						</s:iterator>
				</ul>
			</div>
		</s:if>


		<fieldset><legend><s:text name="title.insert.configuration" /></legend>
			<p class="margin-more-top"><s:text name="note.typeValidProviderURL" /></p>
			<p>

				<label for="providerUrl" class="basic-mint-label"><s:text name="label.providerUrl" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="providerUrl" id="providerUrl" value="%{providerUrl}" cssClass="text" />
				<wpsf:submit
					cssClass="button"
					useTabindexAutoIncrement="true" 
					action="test"
					value="%{getText('label.test')}" 
					/>
			</p>
			<p class="margin-more-top"><s:text name="note.maxResultSize.info" /></p>
			<p>
				<label for="maxResultSize" class="basic-mint-label"><s:text name="label.maxResultSize" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="maxResultSize" id="maxResultSize" value="%{maxResultSize}" cssClass="text" />
			</p>
			<p class="centerText">
				<wpsf:submit
					cssClass="button"
					action="save"
					useTabindexAutoIncrement="true" 
					value="%{getText('label.save')}"
					/>
			</p>
		</fieldset>
	</s:form>

</div>