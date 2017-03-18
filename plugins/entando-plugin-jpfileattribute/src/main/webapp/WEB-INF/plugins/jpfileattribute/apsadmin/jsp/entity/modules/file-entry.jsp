<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>

<s:set var="targetNS" value="%{'/do/jacms/Content'}" />
<h1><s:text name="jacms.menu.contentAdmin" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">

<h2 class="margin-more-bottom"><s:text name="title.contentEditing" /></h2>

<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/include/snippet-content.jsp" />
<h3 class="margin-bit-bottom"><s:text name="jpfileattribute.title.uploadFile" /></h3> 	

<s:form action="save" method="post" enctype="multipart/form-data" cssClass="action-form">

<wpsf:hidden name="contentOnSessionMarker" />

<s:if test="hasFieldErrors()">
<div class="message message_error">
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

<fieldset><legend><s:text name="label.info" /></legend>

<p>
	<label for="upload" class="basic-mint-label"><s:text name="label.file" />:</label>
	<s:file name="upload" id="upload" label="label.file"/>
</p>

</fieldset>

<p class="centerText">
	<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
	<wpsf:submit useTabindexAutoIncrement="true" action="backToEntryContent" value="%{getText('label.cancel')}" cssClass="button" />
</p>

</s:form>

</div>