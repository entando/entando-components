<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

<wpfssa:actionParam action="addListElement" var="addListElementActionNameVar" >
	<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpfssa:actionSubParam name="listLangCode" value="%{#lang.code}" />
</wpfssa:actionParam>
<s:url var="addListElementActionVar" action="%{#addListElementActionNameVar}" />
<sj:submit targets="form-container" value="%{getText('label.add')}" button="true" href="%{#addListElementActionVar}" />

<%--
<wpsa:actionParam action="addListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
</wpsa:actionParam>

<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" value="%{getText('label.add')}" cssClass="button" />
--%>