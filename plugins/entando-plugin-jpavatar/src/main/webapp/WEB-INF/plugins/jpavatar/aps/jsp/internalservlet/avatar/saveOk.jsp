<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<div class="jpavatar">
	<h1><wp:i18n key="jpavatar_TITLE" /></h1>
	<div class="alert alert-success">
		<wp:i18n key="jpavatar_AVATAR_SAVED" />.&#32;
		<a href="<s:url action="edit" />"><span class="icon icon-circle-arrow-right"></span>&#32;<wp:i18n key="jpavatar_OK_THANKYOU" /></a>
	</div>
</div>
