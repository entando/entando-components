<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li>
		<a
			href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />"
			id="menu_rssAggregator">
				<s:text name="jprssaggregator.name" />
		</a>
	</li>
</wp:ifauthorized>
