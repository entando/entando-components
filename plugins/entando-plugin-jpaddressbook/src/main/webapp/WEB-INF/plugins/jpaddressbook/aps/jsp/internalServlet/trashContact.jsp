<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpaddressbook/static/css/jpaddressbook.css"/>

<h2 class="title"><wp:i18n key="jpaddressbook_TITLE" /></h2> 

<h3><wp:i18n key="jpaddressbook_TRASH_CONTACT" /></h3>

<form action="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/delete.action" />" method="post" >
	<p class="noscreen">
		<wpsf:hidden name="entityId"/>
	</p>
	   
	<p>
		<wp:i18n key="jpaddressbook_RU_SURE_DELETE" />&#32;<em><s:property value="entityId" /></em>&#32;?
		<s:set var="submitValue"><wp:i18n key="jpaddressbook_YESDELETE" /></s:set>
		<wpsf:submit useTabindexAutoIncrement="true" value="%{#submitValue}" cssClass="button" />
	</p>
	
</form>

<p> 
	<a href="<wp:action path="/ExtStr2/do/jpaddressbook/Front/AddressBook/resultIntro.action" />"><wp:i18n key="jpaddressbook_GOTO_ADDRESSBOOK" /></a>
</p>