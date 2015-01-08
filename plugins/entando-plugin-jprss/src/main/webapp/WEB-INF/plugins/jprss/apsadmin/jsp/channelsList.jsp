<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jprss.title.rssManagement" />
	</span>
</h1>

<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable fade in">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<ul class="margin-base-top">
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>

<s:set var="channelsVar" value="channels" />

<s:if test="%{#channelsVar.size > 0}" >
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th><s:text name="title" /></th>
				<th><s:text name="description" /></th>
				<th><s:text name="status" /></th>
				<th><s:text name="active" /></th>
				<th><s:text name="contentType" /></th>
				<th><s:text name="feedType" /></th>
			</tr>
			<s:iterator value="#channelsVar" var="channelVar">
			<tr>
				<td class="text-center text-nowrap">
					<%-- edit --%>
					<div class="btn-group btn-group-xs">
						<a href="<s:url action="edit"><s:param name="id" value="#channelVar.id"/></s:url>" 
						   title="<s:text name="label.edit" />: <s:property value="#channelVar.title" />" 
						   class="btn btn-default" >
							<span class="sr-only"><s:text name="label.edit" />: <s:property value="#channelVar.title" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</a>
					</div>
					<%-- remove --%>
					<div class="btn-group btn-group-xs">
						<a href="<s:url action="trash"><s:param name="id" value="#channelVar.id"/></s:url>"
							title="<s:text name="label.remove" />: <s:property value="#channelVar.title" />"
							class="btn btn-warning">
							<span class="icon fa fa-times-circle-o"></span>&#32;
							<span class="sr-only"><s:text name="label.alt.clear" /></span>
						</a>
					</div>
				</td>
				<td><s:property value="#channelVar.title" /></td>
				<td><s:property value="#channelVar.description" /></td>
				<td>
					<s:set var="rssMappingChannelVar" value="%{getContentMapping(#channelVar.contentType)}" ></s:set>
					<s:if test="null == #rssMappingChannelVar">
						<s:text name="label.status.invalid" />
					</s:if>
					<s:else>
						<s:text name="label.status.ok" />
					</s:else>
				</td>
				<td class="icon">
					<s:if test="#channelVar.active"><s:text name="label.yes" /></s:if>
					<s:else><s:text name="label.no" /></s:else>
				</td>
				<td>
					<s:property value="%{getSmallContentType(#channelVar.contentType).descr}" />
				</td>
				<td><s:property value="%{getAvailableFeedTypes()[#channelVar.feedType]}" /></td> 
			</tr>
			</s:iterator>
		</table>
	</div>
</s:if>
<s:else>
	<p><s:text name="note.channels.noChannels" /></p> 
</s:else>

<a href="<s:url namespace="/do/jprss/Rss" action="new" />" class="btn btn-default">
	<span class="icon fa fa-plus-circle"></span>&#32;
	<s:text name="jprss.options.new" />
</a>

<s:set var="contentTypesVar" value="contentTypes" />
<s:if test="%{#contentTypesVar.size > 0}">
<wp:ifauthorized permission="superuser" var="isSuperuser" />
<s:set var="isSuperuserVar">${isSuperuser}</s:set>
<div class="table-responsive margin-base-vertical">
<table class="table table-bordered">
	<tr>
		<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
		<th><s:text name="contentType" /></th>
		<th><s:text name="label.status.info" /></th>
	</tr> 
	<s:iterator value="#contentTypesVar" var="contentTypeVar" status="counter">
	<s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
	<tr>
		<td class="text-center text-nowrap">
			<div class="btn-group btn-group-xs">
				<s:if test="#isSuperuserVar == 'true'">
				<a class="btn btn-default" id="<s:property value="#entityAnchor" />" href="
				<s:url namespace="/do/Entity" action="initEditEntityType">
					<s:param name="entityManagerName">jacmsContentManager</s:param>
					<s:param name="entityTypeCode"><s:property value="#contentTypeVar.typeCode" /></s:param>
				</s:url>" title="<s:text name="label.edit" />: <s:property value="#contentTypeVar.typeDescr" />">
					<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#contentTypeVar.typeDescr" /></span>
					<span class="icon fa fa-pencil-square-o"></span>
				</a>
				</s:if>
				<s:else>&ndash;</s:else>
			</div>
		</td>
		<td>
			<s:property value="#contentTypeVar.typeDescr" />&#32;<code><s:property value="#contentTypeVar.typeCode" /></code>
		</td>
		<td>
			<s:set var="rssMappingVar" value="%{getContentMapping(#contentTypeVar.typeCode)}" />
			<s:if test="null == #rssMappingVar">
				<s:text name="contenttype.not.configurable" />
			</s:if>
			<s:else>
				<s:text name="title" />:&#32;<code><s:property value="#rssMappingVar.titleAttributeName" /></code>
				<s:if test="null != #rssMappingVar.descriptionAttributeName">,
					<s:text name="description" />:&#32;<code><s:property value="#rssMappingVar.descriptionAttributeName" /></code>
				</s:if>
			</s:else>
		</td>
	</tr>
	</s:iterator>
</table>
</div>
</s:if>
<s:else>
	<p><s:text name="no.contentytpes" /></p>
</s:else>