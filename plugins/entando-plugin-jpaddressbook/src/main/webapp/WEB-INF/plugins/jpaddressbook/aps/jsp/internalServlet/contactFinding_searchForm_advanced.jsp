<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpaddressbook/static/css/jpaddressbook.css"/>

<div class="jpaddressbook_searchform"> 
	 
	<h2 class="title"><wp:i18n key="jpaddressbook_SEARCH_CONTACT" /></h2>
	<form action="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/searchIntroAdvanced.action" />" method="post" >
	
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
	
		<s:set var="searcheableAttributes" value="searcheableAttributes" />
		
		<s:iterator var="attribute" value="#searcheableAttributes">
			<s:if test="#attribute.active">
			<s:set var="currentFieldId" value="%{'jpaddressbook_form_'+#attribute.name}" /> 
			<s:set var="currentFieldLabel"><wp:i18n key="jpaddressbook_ATTR${attribute.name}" /></s:set> 
			
			<s:if test="#attribute.textAttribute">
				<p class="attribute">
					<label for="<s:property value="%{#currentFieldId}"/>"><s:property value="#currentFieldLabel" /></label>:<br />
					<s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
				</p>
			</s:if>
			
			<s:elseif test="#attribute.type == 'Date'">
				<s:set name="dateStartInputFieldName"><s:property value="#attribute.name" />_dateStartFieldName</s:set>
				<p class="attribute">
					<label for="<s:property value="%{currentFieldId}"/>_startValue"><s:property value="#currentFieldLabel" />&#32;<wp:i18n key="jpaddressbook_ATTRIBUTE_START_VALUE" /></label>:<br />
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId+'_startValue'}" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" /> dd/MM/yyyy
				</p>
	
				<s:set name="dateEndInputFieldName"><s:property value="#attribute.name" />_dateEndFieldName</s:set>
				<p class="attribute">
					<label for="<s:property value="%{currentFieldId}"/>_endValue"><s:property value="#currentFieldLabel" />&#32;<wp:i18n key="jpaddressbook_ATTRIBUTE_END_VALUE" /></label><br />
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId+'_endValue'}" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />dd/MM/yyyy
				</p>
			</s:elseif>
			
			<s:elseif test="#attribute.type == 'Number'">
				<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
				<p class="attribute">
					<label for="<s:property value="%{currentFieldId}"/>_startValue"><s:property value="#currentFieldLabel" />&#32;<wp:i18n key="jpaddressbook_ATTRIBUTE_START_VALUE" /></label>:<br />
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId+'_startValue'}" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
				</p>
				<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
				<p class="attribute">
					<label for="<s:property value="%{currentFieldId}"/>_endValue"><s:property value="#currentFieldLabel" />&#32;<wp:i18n key="jpaddressbook_ATTRIBUTE_END_VALUE" /></label>:<br />
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#currentFieldId+'_endValue'}" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
				</p>
			</s:elseif>
			
			<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'"> 
				<p class="attribute">
					<s:property value="#currentFieldLabel" />
				</p>
				<s:set name="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
				<s:set name="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
				<ul>
					<li><wpsf:radio useTabindexAutoIncrement="true" id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" cssClass="radio" /><label for="none_<s:property value="#booleanInputFieldName" />" class="normal" ><wp:i18n key="jpaddressbook_ATTRIBUTE_BOTH_VALUE" /></label></li>
					<li><wpsf:radio useTabindexAutoIncrement="true" id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" cssClass="radio" /><label for="true_<s:property value="#booleanInputFieldName" />" class="normal" ><wp:i18n key="jpaddressbook_ATTRIBUTE_YES_VALUE" /></label></li>
					<li><wpsf:radio useTabindexAutoIncrement="true" id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" cssClass="radio" /><label for="false_<s:property value="#booleanInputFieldName" />" class="normal"><wp:i18n key="jpaddressbook_ATTRIBUTE_NO_VALUE" /></label></li>
				</ul>
			</s:elseif>
			</s:if>
		</s:iterator>
	
		 
		<s:set var="submitLabel"><wp:i18n key="jpaddressbook_SEARCH" /></s:set>
		<p class="attribute_submit"><wpsf:submit useTabindexAutoIncrement="true" value="%{#submitLabel}" cssClass="button" /></p> 
		
		<p class="linkitem">
			<a href="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/searchIntro.action" />"><wp:i18n key="jpaddressbook_BASICSEARCH" /></a>
		</p>
		
		
	</form>
</div>