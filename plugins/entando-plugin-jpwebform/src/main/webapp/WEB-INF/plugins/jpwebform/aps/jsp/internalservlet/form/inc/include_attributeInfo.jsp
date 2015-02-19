<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<s:if test="#attributeVar.required || #attributeVar.indexingType != 'NONE' || (#attributeVar.textAttribute && (#attributeVar.minLength != -1 || #attributeVar.maxLength != -1))">
	<s:if test="#attributeVar.required"><abbr title="<wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_MANDATORY_FULL"/>"><wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_MANDATORY_SHORT"/></abbr> </s:if>
	<s:if test="#attributeVar.textAttribute">
		<s:if test="#attributeVar.minLength != -1">&#32;<abbr title="<wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_MINLENGTH_FULL" />" ><wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT" /></abbr>:<s:property value="#attributeVar.minLength" /> </s:if>
		<s:if test="#attributeVar.maxLength != -1">&#32;<abbr title="<wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL" />" ><wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT" /></abbr>:<s:property value="#attributeVar.maxLength" /> </s:if>
	</s:if>
	<s:set var="validationRules" value="#attributeVar.validationRules"></s:set>
	<s:if test="#validationRules != null && #validationRules.ognlValidationRule != null && #validationRules.ognlValidationRule.expression != null">
		<abbr title="<s:if test="#validationRules.ognlValidationRule.helpMessageKey != null">
				  <s:set var="labelKey" value="#validationRules.ognlValidationRule.helpMessageKey" scope="page" />
				  <s:set var="langCode" value="currentLang.code" scope="page" />
				  <wp:i18n key="${attributeLabelKeyVar}" lang="${langCode}" />
			  </s:if>
			  <s:else><s:property value="#validationRules.ognlValidationRule.helpMessage" /></s:else>">
			<wp:i18n key="jpwebform_ENTITY_ATTR_FLAG_HELP_SHORT" /></abbr>
	</s:if>
</s:if>