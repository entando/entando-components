<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<%-- PULSANTE RIMUOVI --%>
<s:set var="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpsa:actionParam action="removeResource" var="removeResourceActionName" >
	<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpsa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpsa:actionParam>
<s:set var="iconImagePath" id="iconImagePath"><%= request.getParameter("iconImagePath")%></s:set>
<wpsf:submit 
	cssClass="btn btn-small"
	useTabindexAutoIncrement="true" 
	type="image" 
	action="%{#removeResourceActionName}" 
	value="%{getText('label.remove')}" 
	title="%{getText('label.remove')}" 
	src="%{#iconImagePath}" />