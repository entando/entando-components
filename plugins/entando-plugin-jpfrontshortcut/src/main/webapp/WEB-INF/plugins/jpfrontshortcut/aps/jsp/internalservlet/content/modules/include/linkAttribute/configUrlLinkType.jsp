<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

<%-- <s:include value="linkAttributeConfigIntro.jsp"></s:include> --%>
<h3 class="margin-more-bottom"><s:text name="title.configureLinkAttribute" />&#32;(<s:text name="title.step2of2" />)</h3>
<%-- <s:include value="linkAttributeConfigReminder.jsp"></s:include> --%>

<s:form id="formform" action="joinUrlLink" namespace="/do/jpfrontshortcut/Content/Link" theme="simple">
	<wpsf:hidden name="contentOnSessionMarker" />
	
<s:if test="hasFieldErrors()">
	<div class="alert">
	<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
	<ul class="unstyled">
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
	            <li><s:property escape="false" /></li>
			</s:iterator>
		</s:iterator>
	</ul>
	</div>
</s:if>
<fieldset><legend><s:text name="title.insertURL" /></legend>
<p><s:text name="note.typeValidURL" /></p>

<p>
	<label for="url" class="basic-mint-label"><s:text name="label.url" />:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" name="url" id="url" cssClass="text" />
</p>

<p class="centerText">
	<s:url var="joinUrlLinkActionVar" action="joinUrlLink" />
	<sj:submit value="%{getText('label.confirm')}" href="%{#joinUrlLinkActionVar}" button="true" targets="form-container" />
</p>


</fieldset>

<p class="lower-actions">
	<s:url var="entryContentActionVar" action="backToEntryContent" />
	<sj:submit value="Cancel, Back to Edit Content" href="%{#entryContentActionVar}" button="true" targets="form-container" />
</p>

</s:form>
