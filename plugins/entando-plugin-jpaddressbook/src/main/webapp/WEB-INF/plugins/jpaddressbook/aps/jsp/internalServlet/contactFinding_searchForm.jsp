<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpaddressbook/static/css/jpaddressbook.css"/>

<div class="jpaddressbook_searchform">
<h2 class="title"><wp:i18n key="jpaddressbook_SEARCH_CONTACT" /></h2> 

	<form action="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/searchIntro.action" />" method="post" >
	
		<s:if test="hasFieldErrors()">
			<h3><wp:i18n key="FIELD_ERROS" /></h3>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</s:if>
	
		<s:set var="userProfilePrototype" value="userProfilePrototype" />
		<s:set var="searcheableAttributes" value="searcheableAttributes" />
		
		<s:if test="#userProfilePrototype.surnameAttributeName != null">
			<s:set var="currentFieldId" value="%{'jpaddressbook_form_'+#userProfilePrototype.surnameAttributeName}" /> 
			<s:set var="currentFieldLabel"><wp:i18n key="jpaddressbook_ATTR${userProfilePrototype.surnameAttributeName}" /></s:set> 
			<p class="attribute"> 
				<label for="<s:property value="%{#currentFieldId}"/>"><s:property value="#currentFieldLabel" /></label>:<br />
				<s:set name="textInputFieldName" ><s:property value="#userProfilePrototype.surnameAttributeName" />_textFieldName</s:set>
				<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
			</p>
		</s:if>
	
		<s:if test="#userProfilePrototype.firstNameAttributeName != null">
			<s:set var="currentFieldId" value="%{'jpaddressbook_form_'+#userProfilePrototype.firstNameAttributeName}" /> 
			<s:set var="currentFieldLabel"><wp:i18n key="jpaddressbook_ATTR${userProfilePrototype.firstNameAttributeName}" /></s:set> 
			<p class="attribute"> 
				<label for="<s:property value="%{#currentFieldId}"/>"><s:property value="#currentFieldLabel" /></label>:<br />
				<s:set name="textInputFieldName" ><s:property value="#userProfilePrototype.firstNameAttributeName" />_textFieldName</s:set>
				<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
			</p>
		</s:if>
		
		<s:if test="#userProfilePrototype.mailAttributeName != null">
			<s:set var="currentFieldId" value="%{'jpaddressbook_form_'+#userProfilePrototype.mailAttributeName}" /> 
			<s:set var="currentFieldLabel"><wp:i18n key="jpaddressbook_ATTR${userProfilePrototype.mailAttributeName}" /></s:set> 
			<p class="attribute">  
				<label for="<s:property value="%{#currentFieldId}"/>"><s:property value="#currentFieldLabel" /></label>:<br />
				<s:set name="textInputFieldName" ><s:property value="#userProfilePrototype.mailAttributeName" />_textFieldName</s:set>
				<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
			</p>
		</s:if>  
			 
		<s:set var="submitLabel"><wp:i18n key="jpaddressbook_SEARCH" /></s:set>
		
		<p class="attribute_submit"><wpsf:submit useTabindexAutoIncrement="true" value="%{#submitLabel}" cssClass="button" /></p> 
		
		<p class="linkitem">
			<a href="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/searchIntroAdvanced.action" />"><wp:i18n key="jpaddressbook_ADVANCEDSEARCH" /></a>
		</p>
		
	</form> 
</div>