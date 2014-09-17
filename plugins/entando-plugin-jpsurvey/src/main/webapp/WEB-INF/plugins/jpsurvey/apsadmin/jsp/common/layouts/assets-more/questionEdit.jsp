<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpsurvey/administration/css/administration.css" />
<script type="text/javascript">
<!--//--><![CDATA[//><!--

//per tab di contenuto
window.addEvent('domready', function(){
	 var tabSet = new Taboo({
			tabs: "tab",
			tabTogglers: "tab-toggle",
			activeTabClass: "tab-current"
		});
});

window.addEvent('domready', function(){
	$$('.js_noscreen').each(function(delendum) {
		delendum.removeClass("js_noscreen");
		delendum.addClass("noscreen");
	});
});

//fine tab di contenuto
//per cateogorie di contenuto
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
		startIndex: "<s:property value="selectedNode" />"
	});

});

//--><!]]></script>