<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpsharedocs/static/css/jpsharedocs.css" />


<wp:ifauthorized permission="jpsharedocs_edit">

	
	<%--
	<p class="edit_comment">
		<a href="<wp:url page="sharedocs_comment" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_COMMENT_THIS_DOC" /></a>
	</p>
	--%>
	
	<wp:pageWithWidget var="sharedocsCommentPage" widgetTypeCode="jpsharedocs_comment" />
	
	<p class="edit_comment">
		<a href="<wp:url page="${sharedocsCommentPage.code}" paramRepeat="true" />" ><wp:i18n key="jpsharedocs_COMMENT_THIS_DOC" /></a>
	</p>
	 
</wp:ifauthorized>

<jacms:content modelId="20093" />

