<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:set var="titleKey">jpwebdynamicform_TITLE_<s:property value="typeCode"/></s:set>
<h1><wp:i18n key="${titleKey}" /></h1>
<p class="alert alert-success">
	<strong><wp:i18n key="jpwebdynamicform_MESSAGE_SAVE_CONFIRMATION" /></strong>
	<a href="<wp:url />">&#32;<wp:i18n key="jpwebdynamicform_MESSAGE_REQUEST_LINK" /></a>
</p>