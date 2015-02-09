<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpnewsletter.title.newsletterManagement" />"><s:text name="jpnewsletter.title.newsletterManagement" /></a>
		&#32;/&#32;
		<s:text name="jpnewsletter.title.newsletterEntry" />
	</span>
</h1>

<s:include value="/WEB-INF/plugins/jpnewsletter/apsadmin/jsp/newsletter/include/content-detail.jsp" />