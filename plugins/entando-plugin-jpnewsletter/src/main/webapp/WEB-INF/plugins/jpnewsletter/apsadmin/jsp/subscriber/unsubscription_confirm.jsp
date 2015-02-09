<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpnewsletter.title.newsletterManagement" />&#32;/&#32;
		<s:text name="title.remove.subscription" />
	</span>
</h1>

<div id="main" role="main">

<s:form action="delete">
	<p class="sr-only">
		<wpsf:hidden name="mailAddress" />
	</p>
	<div class="alert alert-warning">
		<p>
			<s:text name="are.you.sure" />&#32;
			<code><s:property value="mailAddress"/></code>?
		</p>
		<div class="text-center margin-large-top">
		<wpsf:submit type="button" action="delete" cssClass="btn btn-warning btn-lg">
			<span class="icon fa fa-times-circle"></span>&#32;
			<s:text name="label.remove.full" />
		</wpsf:submit>
		<a class="btn btn-link" href="<s:url action="list" />" ><s:text name="note.goToSomewhere" />: <s:text name="jpnewsletter.subscribersList.caption" /></a>
		</div>
	</div>
</s:form>
</div>