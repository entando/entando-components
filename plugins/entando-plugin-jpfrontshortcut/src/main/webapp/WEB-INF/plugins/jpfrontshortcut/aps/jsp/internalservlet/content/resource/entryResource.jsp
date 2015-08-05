<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="jacms" uri="/jacms-apsadmin-core" %>

<%-- not used at the moment --%>
<%-- not used at the moment --%>
<%-- not used at the moment --%>
<%-- not used at the moment --%>
<%-- not used at the moment --%>
<%-- not used at the moment --%>
<%-- not used at the moment --%>
<%-- not used at the moment --%>

<wp:headInfo type="CSS" info="showlets/showlet.css" />
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/moo-japs/moo-jAPS-0.2.js" />

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<s:if test="#categoryTreeStyleVar == 'classic'">
<s:set name="js_for_tab">
//for categories
window.addEvent('domready', function(){
	var catTree  = new Wood({
		menuToggler: "subTreeToggler",
		rootId: "categoryTree",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll" />",
		collapseAllLabel: "<s:text name="label.collapseAll" />",
		type: "tree",
		<s:if test="%{selectedNode != null && !(selectedNode.equalsIgnoreCase(''))}">
		startIndex: "fagianonode_<s:property value="selectedNode" />",
		</s:if>
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"			
	});
});
</s:set>
<wp:headInfo type="JS_RAW" info="${js_for_tab}" />
</s:if>

<div class="showlet">
<s:include value="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" />

<div class="boxFastCrowd">
<div id="int">
<p>
<a href="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/backToEntryContent.action" />" ><wp:i18n key="jpfastcontentedit_BACK_TO_CONTENT" /></a>
</p>
<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Resource/save.action" />"  enctype="multipart/form-data" method="post">

<s:if test="hasFieldErrors()">
<div class="alert">
<p><strong><wp:i18n key="ERRORS" /></strong></p>
<ul class="unstyled">
<s:iterator value="fieldErrors">
	<s:iterator value="value">
	<li><s:property escape="false" /></li>
	</s:iterator>
</s:iterator>
</ul>
</div>
</s:if>
<p class="noscreen">
	<wpsf:hidden name="strutsAction" />
	<wpsf:hidden name="resourceTypeCode" />
<s:iterator value="categoryCodes" id="categoryCode" status="rowstatus">
<input type="hidden" name="categoryCodes" value="<s:property value="#categoryCode" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
</s:iterator>
	<wpsf:hidden name="resourceId" />
<s:if test="#categoryTreeStyleVar == 'request'">
	<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
</s:if>
</p>

<fieldset>
<legend><wp:i18n key="jpfastcontentedit_INFO" /></legend>
<p>
	<label for="descr"><wp:i18n key="jpfastcontentedit_DESCRIPTION" />&#32;<abbr title="<wp:i18n key="jpfastcontentedit_REQUIRED_FULL" />"><wp:i18n key="jpfastcontentedit_REQUIRED_SHORT" /></abbr></label><br />
	<wpsf:textfield useTabindexAutoIncrement="true" name="descr" id="descr" cssClass="text" />
</p>

<p>
	<label for="mainGroup"><wp:i18n key="jpfastcontentedit_GROUP" /></label><br />
	<wpsf:select useTabindexAutoIncrement="true" name="mainGroup" id="mainGroup" list="allowedGroups" value="mainGroup"	listKey="name" listValue="descr" cssClass="text" />
</p>

<p class="buttonFast">
	<label for="upload"><wp:i18n key="jpfastcontentedit_FILE" />&#32;<abbr title="<wp:i18n key="jpfastcontentedit_REQUIRED_FULL" />"><wp:i18n key="jpfastcontentedit_REQUIRED_SHORT" /></abbr></label><br />
	<s:file name="upload" id="upload" label="label.file"/>
</p>
<%--
<p>
	<wpsf:checkbox useTabindexAutoIncrement="true" name="normalizeFileName" id="normalizeFileName" cssClass="radiocheck" />&#32;<label for="normalizeFileName"><s:text name="label.normalize" />&#32;<s:text name="label.filename" /></label>
</p>
--%>
</fieldset>

<fieldset>
<legend><wp:i18n key="jpfastcontentedit_CATEGORIES_MANAGEMENT" /></legend>
<ul id="categoryTree">
	<s:set name="inputFieldName" value="'categoryCode'" />
	<s:set name="selectedTreeNode" value="categoryCode" />
	<s:set name="liClassName" value="'category'" />
	<s:if test="#categoryTreeStyleVar == 'classic'">
	<s:set name="currentRoot" value="categoryRoot" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
	</s:if>
	<s:elseif test="#categoryTreeStyleVar == 'request'">
	<s:set name="currentRoot" value="showableTree" />
	<s:set name="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
	<s:set name="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
	</s:elseif>
</ul>

<div id="actions-container">
	<p class="buttons">
		<s:set name="labelSubmit"><wp:i18n key="jpfastcontentedit_JOIN" /></s:set>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/join-to-resource.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="joinCategory" type="image" src="%{#iconImagePath}" value="%{#labelSubmit}" title="%{#labelSubmit}" />		
	</p>
</div>

<s:if test="categoryCodes != null && categoryCodes.size() > 0">

<table class="generic" summary="<wp:i18n key="jpfastcontentedit_RESOURCES_CATEGORIES_SUMMARY" />">
<caption><wp:i18n key="jpfastcontentedit_RESOURCES_CATEGORIES" /></caption>
<tr>	
	<th><wp:i18n key="jpfastcontentedit_RESOURCES_CATEGORY" /></th>
	<th class="icon"><abbr title="<wp:i18n key="jpfastcontentedit_REMOVE" />">&ndash;</abbr></th>
</tr>
<s:iterator value="categoryCodes" id="categoryCode">
<s:set name="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
<tr>
	<td><s:property value="#resourceCategory.getFullTitle(currentLang.code)"/></td>
	<td class="centerText">
		<wpsa:actionParam action="removeCategory" var="actionName" >
			<wpsa:actionSubParam name="categoryCode" value="%{#resourceCategory.code}" />
		</wpsa:actionParam>
		<s:set name="labelSubmitRemove"><wp:i18n key="jpfastcontentedit_REMOVE" /></s:set>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />plugins/jpfastcontentedit/static/img/delete.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{#labelSubmitRemove}" title="%{#labelSubmit}" />
	</td>
</tr>
</s:iterator>
</table>
</s:if>

</fieldset>

<p class="centerText">
	<s:set name="labelSave"><wp:i18n key="jpfastcontentedit_SAVE" /></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSave}" cssClass="button" />
</p>

</form>
</div>
</div>

<s:include value="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" />
</div>