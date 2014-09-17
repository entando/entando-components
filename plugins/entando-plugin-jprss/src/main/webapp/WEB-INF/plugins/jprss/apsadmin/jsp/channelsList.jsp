<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="targetNS" value="%{'/do/jprss/Rss'}" />
<h1><s:text name="jprss.title.rssManagement" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>

<div id="main">
	<s:if test="%{channels.size > 0}" >
		<s:form>
			<s:if test="hasActionErrors()">
				<div class="message message_error">
				<h2><s:text name="message.title.ActionErrors" /></h2>	
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
				</ul>
				</div>
			</s:if>
		
			<table class="generic" summary="<s:text name="TODO" />">
				<caption><span><s:text name="jprss.rss.rssList" /></span></caption>
				<tr>
					<th><s:text name="title" /></th>
					<th><s:text name="status" /></th>
					<th><s:text name="description" /></th>
					<th><s:text name="active" /></th>
					<th><s:text name="contentType" /></th>
					<th><s:text name="feedType" /></th>
					<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
				</tr>
				<s:iterator var="channel" value="channels">
					<tr>
						<td> 
							<a href="<s:url action="edit"><s:param name="id" value="#channel.id"/></s:url>" title="<s:text name="label.edit" />: <s:property value="#channel.title" />">
								<s:property value="#channel.title" />
							</a>
						</td>
						<td>
							<s:set var="rssMapping" value="%{getContentMapping(#channel.contentType)}" ></s:set>
							<s:if test="null == #rssMapping">
								<s:text name="label.status.invalid" />
							</s:if>
							<s:else>
								<s:text name="label.status.ok" />
							</s:else>
						</td>
						<td><s:property value="#channel.description" /></td>
						<td class="icon">
							<s:if test="#channel.active"><s:text name="label.yes" /></s:if>
							<s:else><s:text name="label.no" /></s:else>
						</td>
						<td>
							<s:property value="%{getSmallContentType(#channel.contentType).descr}" />
						</td>
						<td><s:property value="%{getAvailableFeedTypes()[#channel.feedType]}" /></td> 
						<td class="icon">
							<a href="<s:url action="trash"><s:param name="id" value="#channel.id"/></s:url>" title="<s:text name="label.remove" />: <s:property value="#channel.title" />"><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.remove" />" /></a>
						</td> 
					</tr>	
				</s:iterator>
			</table>
		</s:form>
	</s:if>
	<s:else>
		<p><s:text name="note.channels.noChannels" />.</p> 
	</s:else>
	
	<div class="subsection-light">
		<h2><s:text name="title.contenttypes.manage" /> </h2>
		<p><s:text name="contenttypes.manage.intro.1" /></p>
		<p><s:text name="contenttypes.manage.intro.2" /></p>
		
		<s:set var="contentTypes" value="contentTypes" />
		<s:if test="%{#contentTypes.size > 0}">
			<wp:ifauthorized permission="superuser" var="isSuperuser" />
			<s:set var="isSuperuserVar">${isSuperuser}</s:set>
			<table class="generic" summary="<s:text name="contenttypes.manage.summary" />" >
				<caption><span><s:text name="contenttypes.list" /></span></caption>
				<tr>
					<th><s:text name="label.code" /></th>
					<th><s:text name="label.description" /></th>
					<th><s:text name="label.status.info" /></th>
				</tr>
				<s:iterator value="#contentTypes" var="entityType" status="counter">
					<s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
					<tr>
						<td><span class="monospace"><s:property value="#entityType.typeCode" /></span></td>
						<td>
							<s:if test="#isSuperuserVar == 'true'">
							<a title="<s:text name="label.edit" />:&#32;<s:property value="#entityType.typeDescr" />" href="<s:url action="initEditEntityType" namespace="/do/Entity" />?entityManagerName=jacmsContentManager&entityTypeCode=<s:property value="#entityType.typeCode" />">
								<s:property value="#entityType.typeDescr" />
							</a>
							</s:if>
							<s:else><s:property value="#entityType.typeDescr" /></s:else>
						</td>
						<td>
							<s:set var="rssMapping" value="%{getContentMapping(#entityType.typeCode)}" />
							<s:if test="null == #rssMapping">
								<s:text name="contenttype.not.configurable" />
							</s:if>
							<s:else>
								<s:text name="title" />:&#32;<em><s:property value="#rssMapping.titleAttributeName" /></em>
								<s:if test="null != #rssMapping.descriptionAttributeName">,
									<s:text name="description" />:&#32;<em><s:property value="#rssMapping.descriptionAttributeName" /></em>
								</s:if>
							</s:else>
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
		<s:else>
			<p><s:text name="no.contentytpes" /></p> 
		</s:else>
	</div>
</div>