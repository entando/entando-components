<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

<s:if test="null == #operationButtonDisabled">
	<s:set var="operationButtonDisabled" value="false" />
</s:if>
<%--
<wpsa:actionParam action="moveListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="movement" value="UP" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up-disabled-<s:property value="%{#operationButtonDisabled}" />.png</s:set>
<wpsf:submit useTabindexAutoIncrement="true" disabled="%{#operationButtonDisabled}" action="%{#actionName}" 
type="image" src="%{#iconImagePath}" value="%{getText('label.moveUp')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" />
--%>

<wpfssa:actionParam action="moveListElement" var="moveListElementActionNameVar" >
	<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpfssa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpfssa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpfssa:actionSubParam name="movement" value="UP" />
</wpfssa:actionParam>
<s:url var="moveListElementActionVar" action="%{#moveListElementActionNameVar}" />
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up-disabled-<s:property value="%{#operationButtonDisabled}" />.png</s:set>
<sj:submit targets="form-container" value="%{getText('label.moveUp')}" button="true" type="image" src="%{#iconImagePath}" 
		   title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" href="%{#moveListElementActionVar}" />

<%--
<wpsa:actionParam action="moveListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="movement" value="DOWN" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down-disabled-<s:property value="%{#operationButtonDisabled}" />.png</s:set>
<wpsf:submit useTabindexAutoIncrement="true" disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveDown')}" title="%{getText('label.moveInPositionNumber')}: %{#elementIndex+2}" />
--%>
<wpfssa:actionParam action="moveListElement" var="moveListElementActionNameVar" >
	<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpfssa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpfssa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpfssa:actionSubParam name="movement" value="DOWN" />
</wpfssa:actionParam>
<s:url var="moveListElementActionVar" action="%{#moveListElementActionNameVar}" />
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down-disabled-<s:property value="%{#operationButtonDisabled}" />.png</s:set>
<sj:submit targets="form-container" value="%{getText('label.moveDown')}" button="true" type="image" src="%{#iconImagePath}" 
		   title="%{getText('label.moveInPositionNumber')}: %{#elementIndex}" href="%{#moveListElementActionVar}" />

<%--
<wpsa:actionParam action="removeListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete-disabled-<s:property value="%{#operationButtonDisabled}" />.png</s:set>
<wpsf:submit useTabindexAutoIncrement="true" disabled="%{#operationButtonDisabled}" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />
--%>

<wpfssa:actionParam action="removeListElement" var="removeListElementActionNameVar" >
	<wpfssa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpfssa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpfssa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
</wpfssa:actionParam>
<s:url var="removeListElementActionVar" action="%{#removeListElementActionNameVar}" />
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete-disabled-<s:property value="%{#operationButtonDisabled}" />.png</s:set>
<sj:submit targets="form-container" button="true" type="image" src="%{#iconImagePath}" 
		   value="%{getText('label.remove')}" title="%{getText('label.remove')}" href="%{#removeListElementActionVar}" />