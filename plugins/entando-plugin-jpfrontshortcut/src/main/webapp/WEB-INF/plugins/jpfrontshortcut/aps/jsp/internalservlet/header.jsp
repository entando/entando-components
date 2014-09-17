<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<c:set var="jquery_libraries">
	<sj:head debug="false" jqueryui="true" ajaxcache="false" compressed="true" />
	<script type="text/javascript">
		<!--//--><![CDATA[//><!--
		jQuery.noConflict();
		//--><!]]>
	</script>
</c:set> 
<wp:headInfo type="JS_JQUERY" var="jquery_libraries" />