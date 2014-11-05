<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />

<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

<script type="text/javascript">
<!--//--><![CDATA[//><!--

window.addEvent('domready', function(){
<s:if test="#pageTreeStyleVar == 'classic'">
	var pageTree = new Wood({
		rootId: "pageTree",
		menuToggler: "subTreeToggler",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll"/>",
		collapseAllLabel: "<s:text name="label.collapseAll"/>",
		type: "tree",
		<s:set var="jpshowletreplicatore_startIndex" value="%{showlet.config.get('pageCodeParam')}" />
		<s:if test="%{#jpshowletreplicatore_startIndex != null && !(#jpshowletreplicatore_startIndex.equalsIgnoreCase(''))}">
		startIndex: "<s:property value="%{#jpshowletreplicatore_startIndex}" />",
		</s:if>
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
	});
</s:if>

<s:if test="#myClient == 'advanced'">
	<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/js_trees_context_menu.jsp" />
</s:if>

});

//--><!]]></script>