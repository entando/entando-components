<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE" /></h1>
<s:include value="linkAttributeConfigIntro.jsp" />
<h2><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_1_OF_2" /></h2>
<s:include value="linkAttributeConfigReminder.jsp" />
<p>
	<wp:i18n key="jpfastcontentedit_CHOOSE_LINK_TYPE" />:
</p>

<form 
	class="form-horizontal"
	action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/configLink.action" />" 
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
	
	<p class="noscreen">
		<wpsf:hidden name="contentOnSessionMarker" />
	</p>
	<ul class="unstyled">
		<s:iterator value="linkDestinations" var="typeId">
			<s:if test="#typeId != 4">
				<s:if test="#typeId == 1">
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-url.png</s:set>
					<s:set name="linkDestination" ><wp:i18n key="jpfastcontentedit_LINK_TO_URL"/></s:set>
				</s:if>
				<s:if test="#typeId == 2">
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-page.png</s:set>
					<s:set name="linkDestination" ><wp:i18n key="jpfastcontentedit_LINK_TO_PAGE"/></s:set>
				</s:if>
				
				<s:if test="#typeId == 3">
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-content.png</s:set>
					<s:set name="linkDestination" ><wp:i18n key="jpfastcontentedit_LINK_TO_CONTENT"/></s:set>
				</s:if>
				<li>
						<label class="radio" for="linkType_<s:property value="#typeId"/>">
							<input 
								type="radio" 
								<s:if test="#typeId == symbolicLink.destType"> checked="checked" </s:if> 
								name="linkType" id="linkType_<s:property value="#typeId"/>" 
								value="<s:property value="#typeId"/>" 
								/>
							<img src="<s:property value="iconImagePath" />" alt=" " />&#32;
							<s:property value="linkDestination" />
						</label>
					</li>
			</s:if>	
		</s:iterator>
	</ul>
	<wp:i18n key="jpfastcontentedit_CONTINUE" var="jpfastcontentedit_CONTINUE" />
	<p class="form-actions">
		<wpsf:submit 
			cssClass="btn btn-primary" 
			useTabindexAutoIncrement="true" 
			value="%{#attr.jpfastcontentedit_CONTINUE}" 
			title="%{#attr.jpfastcontentedit_CONTINUE}" 
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