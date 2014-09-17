<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- js escape setup //start --%>
<% pageContext.setAttribute("carriageReturn", "\r");%> 
<% pageContext.setAttribute("newLine", "\n");%> 
<c:set var="singleQuotes">'</c:set>
<c:set var="singleQuotesReplace">\'</c:set>
<c:set var="doubleQuotes">"</c:set>
<c:set var="doubleQuotesReplace">\"</c:set>
<%-- js escape setup //end --%>
<%-- your string --%>
<wp:i18n key="jpfrontshortcut_POPUP_TITLE" var="WIDGET_EDIT_POPUP_TITLE" />
<c:set var="WIDGET_EDIT_POPUP_TITLE" value="${fn:replace(fn:replace(fn:replace(fn:replace(WIDGET_EDIT_POPUP_TITLE,carriageReturn,' '),newLine,' '), singleQuotes, singleQuotesReplace),doubleQuotes,doubleQuotesReplace)}" />
<div id="widgetDialog"></div>
<script type='text/javascript'>
	<!--//--><![CDATA[//><!--
	jQuery(document).ready(function() {
		jQuery.struts2_jquery_ui.bind(jQuery('#widgetDialog'), {
			//options
			minWidth: 500,
			title: "<c:out value="${WIDGET_EDIT_POPUP_TITLE}" escapeXml="false" />",
			autoOpen: false,
			modal: true,
			closetopics: "<wp:info key="systemParam" paramName="applicationBaseURL" />dialog/close",
			jqueryaction: "dialog",
			id: "widgetDialog",
			href: "#",
			draggable: true,
			resizable: true
		});
	});
	//--><!]]>
</script>