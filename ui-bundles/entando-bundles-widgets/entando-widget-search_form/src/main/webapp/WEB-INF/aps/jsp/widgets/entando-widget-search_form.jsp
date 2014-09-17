<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:pageWithWidget var="searchResultPageVar" widgetTypeCode="search_result" />
<form class="navbar-search pull-left" action="<wp:url page="${searchResultPageVar.code}" />" method="get">
	<input type="text" name="search" class="search-query span2" placeholder="<wp:i18n key="ESSF_SEARCH" />" x-webkit-speech="x-webkit-speech" />
</form>