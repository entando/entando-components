<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:pageWithWidget var="sharedocsListPage" widgetTypeCode="jpsharedocs_list" />

<p>
	<wp:i18n key="jpsharedocs_COMMENT_SAVED_CONFIRMATION" />
</p>

<ul>
	<c:set var="viewPage"><s:property value="%{content.viewPage}"/></c:set>
	<li><a href="<wp:url page="${viewPage}"><wp:parameter name="contentId" ><s:property value="contentId" /></wp:parameter></wp:url>" ><wp:i18n key="jpsharedocs_DOC_INFO" /></a></li>
	<li><a href="<wp:url><wp:parameter name="contentId" ><s:property value="contentId" /></wp:parameter></wp:url>" ><wp:i18n key="jpsharedocs_COMMENT_ADD_NEW" /></a></li>
	<li><a href="<wp:url page="${sharedocsListPage.code}" />" ><wp:i18n key="jpsharedocs_LIST" /></a>
</ul>