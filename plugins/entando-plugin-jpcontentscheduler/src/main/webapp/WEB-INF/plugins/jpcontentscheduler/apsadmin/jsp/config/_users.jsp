<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<legend><s:text name="legend.content.users" /></legend>




<s:iterator var="userEntry" value="threadConfig.usersContentType">
<s:property value="key"/>
	<s:iterator var="userEntryValue" value="value">
	<s:select list="contentTypes" listKey="code" listValue="description" />
	<s:submit action=""></s:submit>
		<p>
			<s:textfield name="threadConfig.usersContentType[key]" value="%{#userEntryValue}"></s:textfield>
		</p>
	</s:iterator>
<br />

</s:iterator>