<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<h2><s:text name="title.pageManagement.pageTrash" /></h2>

<div id="form-container" class="widget_form">

<s:form action="delete" id="formform" theme="simple">
<p class="noscreen">
	<wpsf:hidden name="selectedNode"/>
	<wpsf:hidden name="nodeToBeDelete" />
</p>
<p>
	<s:text name="note.deletePage.areYouSure" />&#32;<em class="important"><s:property value="%{getPage(nodeToBeDelete).getTitle(currentLang.getCode())}" /></em>?
	<sj:submit targets="form-container" value="%{getText('page.options.delete')}" indicator="indicator" button="true" />
</p>
</s:form>

</div>