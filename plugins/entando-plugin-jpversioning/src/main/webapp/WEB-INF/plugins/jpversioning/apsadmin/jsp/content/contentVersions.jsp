<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<fieldset>
	<legend><s:text name="jpversioning.label.history" /></legend>
	<s:set name="contentVersionsList" value="%{contentVersions}"/>

	<s:if test="%{#contentVersionsList == null || !#contentVersionsList.size() > 0}">
		<p><s:text name="jpversioning.message.no.previous.revisions" /></p>
	</s:if>
	<s:else>
		<wpsa:subset source="#contentVersionsList" count="5" objectName="groupContent" advanced="true" offset="5">
			<p class="sr-only">
				<wpsf:hidden name="contentId" value="%{content.id}" />
			</p>

			<s:set name="group" value="#groupContent" />
			<div class="pager">
				<p><s:text name="list.pagerIntro" />&#32;<s:property value="#group.size" />&#32;<s:text name="list.pagerOutro" /><br />
				<s:text name="label.page" />: [<s:property value="#group.currItem" />/<s:property value="#group.maxItem" />].</p>
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>

    		<div class="table-responsive">
    			<table class="table table-bordered">
				<caption class="sr-only"><span><s:text name="title.jpversioning.versionList" /></span></caption>
				<tr>
					<th class="text-center padding-large-left padding-large-right"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
					<th  class="text-center"><abbr title="<s:text name="jpversioning.version.full" />"><s:text name="jpversioning.version.short" /></abbr></th>
					<th class="text-center"><s:text name="jpversioning.label.description" /></th>
					<th class="text-center"><s:text name="jpversioning.label.lastVersion" /></th>
					<th><s:text name="jpversioning.label.username" /></th>
 				</tr>

				<s:iterator id="versionId">
				<s:set name="contentVersion" value="%{getContentVersion(#versionId)}" />
				<tr>
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<%-- edit --%>
							<a class="btn btn-default" href="<s:url action="preview" namespace="/do/jpversioning/Content/Versioning">
							   <s:param name="versionId" value="#contentVersion.id" />
							   <s:param name="contentId" value="%{content.id}" />
							   <s:param name="fromEdit" value="true" />
							   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>" >
							<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#model.description" /></span>
								<span class="icon fa fa-info"></span>
							</a>
							<a class="btn btn-default" href="<s:url action="entryRecover" namespace="/do/jpversioning/Content/Versioning" >
							   <s:param name="versionId" value="#contentVersion.id" />
							   <s:param name="contentId" value="%{content.id}" />
							   <s:param name="fromEdit" value="true" />
							   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" /></s:url>" title="<s:text name="jpversioning.label.restore" />:&#32;<s:property value="#contentVersion.version" />" >
						   <span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#model.description" /></span>
							<span class="icon fa fa-reply"></span>
							</a>
				    </td>
				    <td class="text-center text-nowrap"><code><s:property value="#contentVersion.version" /></code></td>
					<td><s:property value="#contentVersion.descr" /></td>
					<td class="text-center text-nowrap">
						<code>
						<s:date name="#contentVersion.versionDate" format="dd/MM/yyyy HH:mm" />
						</code>
					</td>
					<td><s:property value="#contentVersion.username" /></td>
				</tr>
				</s:iterator>
			</table>
			</div>
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:else>
</fieldset>
