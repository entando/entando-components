<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />

<sj:head />

<script type="text/javascript"><!--
//--><![CDATA[//>
	$.noConflict();
	function overrideSubmit(actionName) {
		document.forms['configurationForm'].action = actionName + '.action';
        document.forms['configurationForm'].submit();
    }
	
//<!]]>-->--</script>
