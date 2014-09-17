<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

<%-- PULSANTE RIMUOVI --%>
<s:set name="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpfssa:actionParam action="removeResource" var="removeResourceActionNameVar" >
	<wpfssa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpfssa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpfssa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpfssa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpfssa:actionParam>
<s:url var="removeResourceActionVar" action="%{#removeResourceActionNameVar}" />
<s:set name="iconImagePath" id="iconImagePath"><%= request.getParameter("iconImagePath")%></s:set>
<sj:submit type="image" targets="form-container" value="%{getText('label.remove')}" 
		   button="true" href="%{#removeResourceActionVar}" src="%{#iconImagePath}" />
<%--
<s:set name="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpsa:actionParam action="removeResource" var="removeResourceActionName" >
	<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpsa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><%= request.getParameter("iconImagePath")%></s:set>
<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#removeResourceActionName}" 
	value="%{getText('label.remove')}" title="%{getText('label.remove')}" src="%{#iconImagePath}" />
--%>