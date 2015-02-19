<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />

<%--
<wp:headInfo type="JS" info="../../plugins/jpsharedocs/static/js/mootools-1.2-core.js" />
<wp:headInfo type="JS" info="../../plugins/jpsharedocs/static/js/mootools-1.2-more.js" />
<wp:headInfo type="JS" info="../../plugins/jpsharedocs/static/js/moo-jAPS-0.2.js" />

<c:set var="categories_js">
	window.addEvent('domready', function(){
		var catTree  = new Wood({
			menuToggler: "subTreeToggler",
			rootId: "categoryTree",
			closedClass: "node_closed",
			showTools: "true",
			expandAllLabel: "<s:text name="label.expandAll" />",
			collapseAllLabel: "<s:text name="label.collapseAll" />",
			type: "tree",
			<s:if test="%{categoryCode != null && !(categoryCode.equalsIgnoreCase(''))}">
			startIndex: "fagianonode_<s:property value="categoryCode" />",
			</s:if>
			toolTextIntro: "<s:text name="label.introExpandAll" />",
			toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
			toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"	
		});
	});
</c:set>
--%>
<wp:headInfo type="JS" info="../../administration/js/jquery.entando.js" />
<c:set var="categories_js">
jQuery(function(){
	jQuery("#categoryTree").EntandoWoodMenu({
		menuToggler: "subTreeToggler",
		menuRetriever: function(toggler) {
			return $(toggler).parent().children("ul");
		},
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: true,
		onStart: function() {
			this.collapseAll();
		},
		expandAllLabel: "<s:text name="label.expandAll" />",
		collapseAllLabel: "<s:text name="label.collapseAll" />",

		<s:if test="%{categoryCode != null && !(categoryCode.equalsIgnoreCase(''))}">
		startIndex: "fagianonode_<s:property value="categoryCode" />",
		</s:if>
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
	});
});
</c:set>
<wp:headInfo type="JS_RAW" info="${categories_js}" />

<h3><wp:i18n key="jpsharedocs_EDIT_STEP2" /></h3>

<p>
	<wp:i18n key="jpsharedocs_EDIT_CATEGORIES"/>
</p>

<form action="<wp:action path="/ExtStr2/do/jpsharedocs/Document/submitCategories" />" method="post" enctype="multipart/form-data" >
	
	<s:if test="hasActionErrors()">
		<h4><wp:i18n key="ERRORS" /></h4>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</s:if>
	
	<s:if test="hasFieldErrors()">
		<h4><wp:i18n key="ERRORS" /></h4>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	
	<p class="noscreen">
		<wpsf:hidden name="step" value="2" />
		<s:if test="%{isEdit()}"><wpsf:hidden name="contentId" /></s:if>
		<wpsf:hidden name="title" />
		<wpsf:hidden name="author" />
		<wpsf:hidden name="description" />
		<wpsf:hidden name="docDescription" />
		<wpsf:hidden name="documentContentType" />
		<wpsf:hidden name="documentFileName" />
		<wpsf:hidden name="tmpFileName" />
		<s:iterator value="categories" id="categoryCode" status="rowstatus">
			<wpsf:hidden name="categories" value="%{#categoryCode}" id="categories-%{#rowstatus.index}"/>
		</s:iterator>
		<s:iterator value="extraGroups" id="groupName" status="rowstatus">
			<wpsf:hidden name="extraGroups" value="%{#groupName}" id="extraGroups-%{#rowstatus.index}"/>
		</s:iterator>
		<wpsf:hidden name="mainGroup" />
	</p>
	
	<ul id="categoryTree">
		<s:set name="inputFieldName" value="'categoryCode'" />
		<s:set name="selectedTreeNode" value="categoryCode" />
		<s:set name="liClassName" value="'category'" />
		<s:set name="currentRoot" value="categoryRoot" />
		<s:include value="/WEB-INF/plugins/jpsharedocs/aps/jsp/internalservlet/sharedocs/include/treeBuilder.jsp" />
	</ul>
	
	<s:set name="joinLabel"><wp:i18n key="jpsharedocs_JOIN" /></s:set>
	<p><wpsf:submit action="joinCategory" value="%{#joinLabel}" cssClass="button" /></p>
	
	<table class="generic" summary="<wp:i18n key="jpsharedocs_CATEGORIES_TABLE_SUMMARY"/>: <s:property value="description" />">
		<caption><wp:i18n key="jpsharedocs_CATEGORIES_TABLE_CAPTION"/></caption>
		
		<s:set name="removeCategoryLabel"><wp:i18n key="jpsharedocs_REMOVE_CATEGORY" /></s:set>
		<tr>	
			<th><wp:i18n key="jpsharedocs_CATEGORY"/></th>
			<th class="icon"><abbr title="<s:property value="#removeCategoryLabel" />">&ndash;</abbr></th>
		</tr>
		<s:iterator value="categories" id="categoryCode">
			<s:set name="category" value="%{getCategory(#categoryCode)}" />
			<tr>
				<td><s:property value="#category.defaultFullTitle"/></td>
				<td class="icon">
					<wpsa:actionParam action="removeCategory" var="actionName" ><wpsa:actionSubParam name="categoryCode" value="%{#categoryCode}" /></wpsa:actionParam>
					<s:set var="iconImagePath" ><wp:resourceURL />plugins/jpsharedocs/static/img/list-remove.png</s:set>
					<wpsf:submit type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{#removeCategoryLabel}" title="%{#removeCategoryLabel + ' ' + #category.defaultFullTitle}" />
				</td>
			</tr>
		</s:iterator>
	</table>
	
	<p>
		<s:set name="backLabel" ><wp:i18n key="jpsharedocs_EDIT_PREV_STEP" /></s:set>
		<wpsf:submit cssClass="button" action="compileFields" value="%{#backLabel}" />&#32;
		<s:set name="submitLabel" ><wp:i18n key="jpsharedocs_EDIT_NEXT_STEP" /></s:set>
		<wpsf:submit cssClass="button" value="%{#submitLabel}" />
	</p>
	
</form>