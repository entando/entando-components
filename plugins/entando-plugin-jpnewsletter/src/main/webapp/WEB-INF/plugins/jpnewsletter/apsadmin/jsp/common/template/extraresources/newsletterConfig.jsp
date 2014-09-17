<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpnewsletter/administration/css/administration.css" />

<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/snippet-calendar.jsp" />
<s:if test="#myClient == 'advanced'">	
<script type="text/javascript">
<!--//--><![CDATA[//><!--
	  function disable(obj_checkbox, obj_textarea)
	  {  if(obj_checkbox.checked)
	     { obj_textarea.disabled = false;
	     }
	     else
	     { obj_textarea.disabled = true;
	     }
	  }
//--><!]]></script>
 </s:if>