<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

<%-- PULSANTE CERCA RISORSA --%>
<s:set name="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpfssa:actionParam action="chooseResource" var="chooseResourceActionNameVar" >
	<wpfssa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpfssa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpfssa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpfssa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpfssa:actionParam>
<s:url var="chooseResourceActionVar" action="%{#chooseResourceActionNameVar}" />
<s:set name="iconImagePath" id="iconImagePath"><%= request.getParameter("iconImagePath")%></s:set>
<sj:submit type="image" targets="form-container" value="%{getText('label.choose')}" 
		   button="true" href="%{#chooseResourceActionVar}" src="%{#iconImagePath}" />
<%--
<s:set name="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></s:set>
<wpsa:actionParam action="chooseResource" var="chooseResourceActionName" >
	<wpsa:actionSubParam name="parentAttributeName" value="%{#parentAttribute.name}" />
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="resourceTypeCode" value="%{#resourceTypeCode}" />
	<wpsa:actionSubParam name="resourceLangCode" value="%{#lang.code}" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><%= request.getParameter("iconImagePath")%></s:set>
<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#chooseResourceActionName}" 
	value="%{getText('label.choose')}" title="%{#attribute.name + ': ' + getText('label.choose')}" src="%{#iconImagePath}" />
--%>