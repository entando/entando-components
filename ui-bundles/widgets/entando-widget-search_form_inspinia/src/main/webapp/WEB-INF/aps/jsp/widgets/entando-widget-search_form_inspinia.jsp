<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" listResult="false" />
<form role="search" class="navbar-form-custom" action="<wp:url page="${searchResultPageVar.code}" />" method="get">
<div class="form-group">
	<input type="text" name="search" class="form-control" placeholder="<wp:i18n key="ESSF_SEARCH" />" />
</div>
</form>