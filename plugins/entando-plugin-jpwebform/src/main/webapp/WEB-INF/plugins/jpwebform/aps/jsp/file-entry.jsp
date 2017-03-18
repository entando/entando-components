<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>
<s:set var="typeCode" value="%{typeCode}" />
<div class="jpfileattribute-entry-file jpwebform-<s:property value="#typeCode" />">
	<form 
		action="<wp:action path="/ExtStr2/do/jpwebform/Form/Resource/save.action"/>" 
		method="post" 
		enctype="multipart/form-data">
			
			<%/* FIXME: add the title of the current step */ %>
			<%--
			<h2 class="jpfileattribute-entry-file-title">Step ?????</h2>
			--%>

			<s:if test="hasFieldErrors()">
				<div class="jpfileattribute-entry-file-errors message message_error">
					<h3><s:text name="message.title.FieldErrors" /></h3>
					<ul>
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property escapeHtml="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>
	
			<p class="jpfileattribute-entry-file-formblock">
				<input type="hidden" name="currentStepCode" value="${currentStepCode}" />
				<s:hidden name="typeCode" value="%{#typeCode}" />
				<label for="<s:property value="%{#typeCode+'-'+#attributeNme+'-upload'}" />">
					<wp:i18n key="jpwebform_FILEATTRIBUTE_LABEL_FILE" />
				</label>
				<s:file name="upload" id="%{#typeCode+'-'+#attributeNme+'-upload'}" label="label.file"/>
			</p>
			
			<p class="jpfileattribute-entry-file-submit">
				<wpsf:submit 
					useTabindexAutoIncrement="true" 
					value="%{getText('label.save')}"
					/>
				<wpsf:submit 
					useTabindexAutoIncrement="true" 
					action="backToEntryMessage" 
					value="%{getText('label.cancel')}" 
					/>
			</p>
	</form>
</div>