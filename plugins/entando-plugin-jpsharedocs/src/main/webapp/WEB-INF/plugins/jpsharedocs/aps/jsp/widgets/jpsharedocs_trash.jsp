<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>

<h3><wp:i18n key="jpsharedocs_TRASH"/></h3>

	<form action="<wp:action path="/ExtStr2/do/jpsharedocs/Document/removeContent.action" />" method="post">

	<s:hidden name="contentId"/>
	<s:set value="contentId" var="cid"/>


	<jacms:content modelId="20095"/>
	
	<br/>

	<wpsf:submit type="button" title="%{getText('jpsharedocs.content.remove')}">
		<wp:i18n key="jpsharedocs_REMOVE"/>
	</wpsf:submit>

	<br/>

	<wp:pageWithWidget var="sharedocsFullListPage" widgetTypeCode="jpsharedocs_list" />
	<p class="edit_comment">
		<a href="<wp:url page="${sharedocsFullListPage.code}" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_LIST" /></a>
	</p>


	</form>

