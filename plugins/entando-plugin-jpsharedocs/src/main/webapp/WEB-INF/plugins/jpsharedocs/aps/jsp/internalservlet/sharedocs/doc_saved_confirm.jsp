<%@ taglib prefix="wp" uri="/aps-core" %>

<p>
	<wp:i18n key="jpsharedocs_DOC_SAVED_CONFIRMATION" />
</p>

<wp:pageWithWidget var="sharedocsListPage" widgetTypeCode="jpsharedocs_list" />

<ul>
	<li><a href="<wp:url />" class="edit_comment"><wp:i18n key="jpsharedocs_DOC_ADD_NEW" /></a></li>
	<li><a href="<wp:url page="${sharedocsListPage.code}" />" class="edit_comment"><wp:i18n key="jpsharedocs_LIST" /></a></li>
</ul>
