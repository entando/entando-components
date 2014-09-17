<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<script type="text/javascript">
<!--//--><![CDATA[//><!--
window.addEvent('domready', function(){
<s:if test="#categoryTreeStyleVar == 'classic'">
	var catTree  = new Wood({
		menuToggler: "subTreeToggler",
		rootId: "categoryTree",
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
</s:if>

<s:if test="#myClient == 'advanced'">
	<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/js_trees_context_menu.jsp" />
</s:if>
	
});

//--><!]]></script>