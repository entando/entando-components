<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser" >
<li><a href="<s:url action="viewItem" namespace="/do/jpcontentscheduler/config" />"><s:text name="jpcontentscheduler.title.management" /></a></li>
</wp:ifauthorized>