<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<h1><wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE" /></h1>
<div class="alert alert-block">
	<p><strong><wp:i18n key="jpfastcontentedit_GENERIC_ERROR" /></strong></p>
	<s:if test="hasActionErrors()">
		<ul class="unstyled">
		<s:iterator value="actionErrors">
			<li><s:property escape="false" /></li>
		</s:iterator>
		</ul>
	</s:if>
</div>
