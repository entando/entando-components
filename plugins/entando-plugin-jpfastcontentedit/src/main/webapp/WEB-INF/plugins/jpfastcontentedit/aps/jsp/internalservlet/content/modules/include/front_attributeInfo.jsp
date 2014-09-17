<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="#attribute.required">
	<abbr class="icon icon-asterisk" title="<s:text name="Entity.attribute.flag.mandatory.full"/>"><span class="noscreen"><s:text name="Entity.attribute.flag.mandatory.short"/></span></abbr>
</s:if>