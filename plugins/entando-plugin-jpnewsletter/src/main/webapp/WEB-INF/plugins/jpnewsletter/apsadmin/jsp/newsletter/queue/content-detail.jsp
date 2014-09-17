<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpnewsletter.title.newsletterQueue" />"><s:text name="jpnewsletter.title.newsletterManagement" /></a></h1>

<h2><s:text name="jpnewsletter.title.newsletterEntry" /></h2>

<s:include value="../include/content-detail.jsp" />