<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- 
<s:if test="%{isEmailAttribute(#attribute.name) && #attribute.getTextForLang(#lang.code)!=null}" >
	<a href="mailto:<s:property value="%{#attribute.getTextForLang(#lang.code)}" />" ><s:property value="%{#attribute.getTextForLang(#lang.code)}" /></a>
</s:if>
<s:else>
	<s:property value="%{#attribute.getTextForLang(#lang.code)}" />
</s:else>
 --%>

<s:property value="%{#attribute.getTextForLang(#lang.code)}" />