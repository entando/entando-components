<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<%-- PULSANTE CERCA RISORSA --%>
<s:set var="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpsa:actionParam action="chooseResource" var="chooseResourceActionName" >
	<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpsa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpsa:actionParam>
<s:set var="iconImagePath" id="iconImagePath"><%= request.getParameter("iconImagePath")%></s:set>
<wpsf:submit 
	cssClass="btn"
	useTabindexAutoIncrement="true" 
	type="image" 
	action="%{#chooseResourceActionName}" 
	value="%{getText('label.choose')}" 
	title="%{#attribute.name + ': ' + getText('label.choose')}" 
	src="%{#iconImagePath}" />