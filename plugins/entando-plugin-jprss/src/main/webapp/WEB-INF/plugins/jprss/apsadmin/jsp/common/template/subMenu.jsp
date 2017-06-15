<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jprss_channels_edit">
    <li class="list-group-item">
        <a href="<s:url action="list" namespace="/do/jprss/Rss" />" id="menu_rss" >
            <span class="list-group-item-value"><s:text name="jprss.name" /></span>
        </a>
    </li>
</wp:ifauthorized>