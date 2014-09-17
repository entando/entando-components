<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" 		uri="/struts-tags" %>
<%@ taglib prefix="wp" 		uri="/aps-core"%>
<%@ taglib prefix="wpsa" 	uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" 	uri="/apsadmin-form"%>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
			<s:text name="jprssaggregator.title.rssAggregator.rssManagement" />
	</span>
</h1>

<%-- New Button--%>
<div class="btn-group margin-large-bottom">
	<a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="new" />" class="btn btn-default">
		<span class="icon fa fa-plus-circle"></span>
		<s:text name="label.new" />
	</a>
</div>

<div id="main">
	<div class="table-responsive">
		<table class="table table-bordered">
			<caption class="sr-only"><s:text name="jprssaggregator.title.rssAggregator.list" /></caption>
			<tr>
				<%--
					<th><abbr title="Codice">Cod.</abbr></th>
				--%>
				<th class="text-center text-nowrap"><abbr title="<s:text name="jprssaggregator.rssAggregator.actions" />">&ndash;</abbr></th>
				<th><abbr title="<s:text name="jprssaggregator.rssAggregator.description" />"><s:text name="jprssaggregator.rssAggregator.descr" /></abbr></th>
				<%-- <th><s:text name="jprssaggregator.rssAggregator.url" /></th> --%>
				<th><s:text name="jprssaggregator.rssAggregator.delay" /></th>
				<th class="text-center"><abbr title="<s:text name="jprssaggregator.rssAggregator.lastUpdate" />"><s:text name="jprssaggregator.rssAggregator.updated" /></abbr></th>
			</tr>
			<s:set var="aggregatorItems" value="aggregatorItems" />
			<s:iterator value="aggregatorItems" var="item">
				<tr>
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<a
								class="btn btn-default"
								href="<s:url action="edit"><s:param name="code" value="#item.code"/></s:url>"
								title="<s:text name="label.edit" />:&#32;<s:property value="#item.descr" />">
									<span class="icon fa fa-edit"></span>
									<span class="sr-only"><s:text name="label.edit" /></span>
							</a>
							<a
								class="btn btn-default"
								href="<s:url action="syncronize"><s:param name="code" value="#item.code"/></s:url>"
								title="<s:text name="label.rssSync" />:&#32;<s:property value="#item.descr" />">
									<span class="icon fa fa-refresh"></span>
									<span class="sr-only"><s:text name="label.rssSync" /></span>
							</a>
						</div>
						<div class="btn-group btn-group-xs">
							<a
								class="btn btn-warning"
								href="<s:url action="delete"><s:param name="code" value="#item.code"/></s:url>"
								title="<s:text name="label.remove" />:&#32;<s:property value="#item.descr" />">
									<span class="icon fa fa-times-circle-o"></span>
									<span class="sr-only"><s:text name="label.remove" /></span>
							</a>
						</div>
					</td>
					<td>
							<s:property value="#item.descr" />
							<a
								title="<s:property value="#item.link" />"
								href="<s:property value="#item.link" />"
								>
									<span class="icon fa fa-globe"></span>
							</a>
					</td>
					<%-- <td><s:property value="#item.link" /></td> --%>
					<td>
						<% /*FIXME fare in modo che il valore delay del singolo canale sia compatibile con le chiavi della mappa "delays"*/ %>
						<s:property value="%{delays.get(#item.delay.intValue())}"/>
					</td>
					<td class="text-center">
						<code title="<s:date name="#item.lastUpdate" format="EEEE dd MMMM yyyy HH:mm:ss" />">
							<s:date name="#item.lastUpdate" nice="true" format="dd/MM/yyyy HH:mm:ss" />
						</code>
					</td>
				</tr>
			</s:iterator>
		</table>
	</div>
</div>
