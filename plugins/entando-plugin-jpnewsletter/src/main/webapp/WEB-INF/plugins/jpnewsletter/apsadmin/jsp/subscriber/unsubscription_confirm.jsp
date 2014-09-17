<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%--
Jsp che contiene la richiesta di conferma di cancellazione di un sottoscritto al
servizio
--%>
<h1><s:text name="jpnewsletter.title.newsletterManagement" /></h1>
<div id="main">
<h2><s:text name="title.remove.subscription"/></h2>

<s:form action="delete">
	<p>
		<wpsf:hidden name="mailAddress" />
		<s:text name="are.you.sure" />&#32;<em><s:property value="mailAddress"/></em>?
	</p>
	<p>
		<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.remove.full')}" cssClass="button" /> 
	</p>
</s:form>
<p>
	<s:text name="cancel.this.operation.and.go.to" />&#32;<a href="<s:url action="list" />"><s:text name="jpnewsletter.subscribersList.caption"/></a>
</p>
</div>