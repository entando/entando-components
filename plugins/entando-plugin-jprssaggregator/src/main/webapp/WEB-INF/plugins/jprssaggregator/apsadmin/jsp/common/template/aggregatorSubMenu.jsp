<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li class="list-group-item">
		<a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />" id="menu_rssagg" >
			<span class="list-group-item-value"><s:text name="jprssaggregator.name" /></span>
		</a>
	</li>
</wp:ifauthorized>
