<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wpsa:set name="showletConfig" value="showlet.config" />
<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>

<p class="noscreen"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>
<div id="main">
<h2><s:text name="title.configPage" /></h2>

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<div class="subsection-light">
<h3><s:text name="title.configPage.youAreDoing" /></h3>

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
<h3 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" /></h3>

	<s:form action="saveConfig">
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
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
			<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
		</s:if>

		<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

		<p class="noscreen">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}"/>
			<wpsf:hidden name="frameIdParam" value="%{#showletConfig.get('frameIdParam')}"/>
			<s:if test="#pageTreeStyleVar == 'classic'">
			<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
			</s:if>
		</p>

		<s:if test="%{#showletConfig.get('frameIdParam') == null}">
			<fieldset class="margin-more-top">
				<legend><s:text name="title.selectSourcePage" /></legend>
				<ul id="pageTree">
					<s:set name="inputFieldName" value="'pageCodeParam'" />
					<s:set name="selectedTreeNode" value="%{#showletConfig.get('pageCodeParam')}" />
					<s:set name="liClassName" value="'page'" />
					<s:if test="#pageTreeStyleVar == 'classic'">
					<s:set name="currentRoot" value="allowedTreeRootNode" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
					</s:if>
					<s:elseif test="#pageTreeStyleVar == 'request'">
					<s:set name="currentRoot" value="showableTree" />
					<s:set name="openTreeActionName" value="'backToChoosePage'" />
					<s:set name="closeTreeActionName" value="'backToChoosePage'" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
					</s:elseif>
				</ul>
			</fieldset>
			<p>
				<wpsf:submit useTabindexAutoIncrement="true" action="browseFrames" value="%{getText('label.continue')}" cssClass="button" />
			</p>
		</s:if>

		<s:else>
			<fieldset class="margin-more-top">
				<legend><s:text name="title.configuration" /></legend>
				<s:set var="targetPage" value="%{getPage(#showletConfig.get('pageCodeParam'))}" />
				<input type="hidden" name="pageCodeParam" value="<s:property value="%{#targetPage.code}" />" />
				<p>
					<span class="important"><s:text name="label.selectedPage" />:</span><br />
					<s:iterator value="langs" status="rowStatus">
						<s:if test="#rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span>&#32;<s:property value="#targetPage.getTitles()[code]" />
					</s:iterator>
				</p>
				<p>
					<span class="important"><s:text name="label.selectedFrame" />:</span><br />
					<s:set var="targetFrame"  value="%{#showletConfig.get('frameIdParam')}" />
					<s:set name="targetShowlet" value="#targetPage.getShowlets()[(#targetFrame - 0)]" />
					<s:property value="#targetFrame"/> &ndash;
					<s:property value="#targetPage.getModel().getFrames()[(#targetFrame - 0)]"/> &ndash;
					<s:if test="%{#targetShowlet != null}">
						<s:property value="getTitle(#targetShowlet.type.code, #targetShowlet.type.titles)"/>
					</s:if>
					<s:else>
						<s:text name="note.emptyFrame"></s:text>
					</s:else>
					&#32;
					<wpsf:submit useTabindexAutoIncrement="true" action="browseFrames" value="%{getText('label.browseFrames')}" cssClass="button" />
				</p>

				<p>
					<wpsf:submit useTabindexAutoIncrement="true" action="reset" value="%{getText('label.reconfigure')}" cssClass="button"  />
				</p>

			</fieldset>
			<p class="centerText">
				<wpsf:submit useTabindexAutoIncrement="true"  value="%{getText('label.save')}" cssClass="button" />
			</p>
		</s:else>

	</s:form>

</div>
</div>