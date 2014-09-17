<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<s:set var="url"><wp:url /></s:set>
<p class="widget-center">
	<a href="<wp:url  />">
		<s:text name="jpfrontshortcut.dialog.content.changed" />
	</a>
</p>
<script type="text/javascript">
<!--//--><![CDATA[//><!--
var href = "<s:property value="#url" escapeJavaScript="true" />";
var contentId = "<s:property value="contentId" />";
var random = 'reload='+Math.floor(Math.random()*999999);
if (/\?/.test(href)) {
	href = '&'+random + "&contentId=" + contentId;
}
else {
	href = '?'+random + "&contentId=" + contentId;
}
window.location.href= href;
//--><!]]>
</script>