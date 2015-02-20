<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:if test="%{#myClient == 'advanced'}">
	<script src="<wp:resourceURL />administration/mint/js/codemirror/codemirror-compressed.js"></script>
	<link rel="stylesheet" href="<wp:resourceURL />administration/mint/js/codemirror/codemirror.css">
	<script type="text/javascript" src="<wp:resourceURL />administration/mint/js/codemirror/overlay.js"></script>
	<link rel="stylesheet" href="<wp:resourceURL />administration/mint/js/codemirror/mode/xml/xml-eclipse.css" />
	
	<script type="text/javascript">
	<!--//--><![CDATA[//><!--
		document.addEvent('domready', function(){
			CodeMirror.fromTextArea(document.getElementById("gui-css-code"),{
				mode: "css",
				readOnly: false,
				lineNumbers: true,
				gutter: true
			});
			CodeMirror.fromTextArea(document.getElementById("gui-html-code"),{
				mode: "htmlmixed",
				readOnly: false,
				lineNumbers: true,
				gutter: true
			});
		})
	//--><!]]>
	</script>
	<link rel="stylesheet" href="<wp:resourceURL />plugins/jpwebform/administration/mint/css/jpwebform-entryGui.css">
</s:if>
