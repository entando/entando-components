<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jprss_channels_edit">
	<li><a href="<s:url action="intro" namespace="/do/jprss/Rss" />" id="menu_rss" ><s:text name="jprss.name" /></a></li>
</wp:ifauthorized>