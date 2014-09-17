<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE" /></h1>

<s:include value="linkAttributeConfigIntro.jsp" />
<h2><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_2_OF_2" /></h2>
<s:include value="linkAttributeConfigReminder.jsp"></s:include>


<form 
	class="form-horizontal" 
	action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/joinUrlLink.action" />" 
	method="post">

		<s:if test="hasFieldErrors()">
			<div class="alert alert-block">
				<p><strong><wp:i18n key="jpfastcontentedit_ERRORS" /></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
							<s:iterator value="value">
									<li><s:property escape="false" /></li>
							</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		
	<p class="noscreen hide">
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>	

	<div class="control-group">
		<label class="control-label" for="jpfastcontentedit-configlink-url"><wp:i18n key="jpfastcontentedit_URL" /></label>
		<div class="controls">
			<wpsf:textfield 
				useTabindexAutoIncrement="true" 
				name="url" 
				id="jpfastcontentedit-configlink-url" 
				/>
			<span class="help help-block">
				<wp:i18n key="jpfastcontentedit_CONFIGURE_URL" />
			</span>
		</div>
	</div>
	<p class="form-actions">
		<wp:i18n key="jpfastcontentedit_CONFIRM" var="jpfastcontentedit_CONFIRM" />
		<wpsf:submit 
			cssClass="btn btn-primary"
			useTabindexAutoIncrement="true" 
			value="%{#attr.jpfastcontentedit_CONFIRM}" 
			title="%{#attr.jpfastcontentedit_CONFIRM}" 
			/>
		&emsp;
		<a class="btn btn-link"
			href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/backToEntryContent.action">
			<wp:parameter name="contentOnSessionMarker"><s:property value="%{contentOnSessionMarker}" /></wp:parameter>
			</wp:action>
			" >
			<wp:i18n key="jpfastcontentedit_BACK_TO_CONTENT" />
		</a>
	</p>
</form>