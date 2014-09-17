<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<div id="form-container" class="widget_form">
	<s:if test="hasActionErrors()">
		<div class="alert">
			<p><strong><s:text name="message.title.ActionErrors" /></strong></p>
			<ul class="unstyled">
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
			</ul>
		</div>
	</s:if>
</div>