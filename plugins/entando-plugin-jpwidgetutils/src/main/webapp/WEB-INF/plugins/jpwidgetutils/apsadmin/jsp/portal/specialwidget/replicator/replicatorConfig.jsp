<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="name.widget" />
	</span>
</h1>
<wpsa:set name="showletConfig" value="showlet.config" />

<div id="main" role="main">
	
	<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
	<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
	
	<s:form action="saveConfig" cssClass="form-horizontal">
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
			</div>
		
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
					<ul class="margin-base-top">
						<s:iterator value="actionErrors">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
			<ul class="margin-base-vertical">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
		</s:if>
		
		<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>

		<p class="sr-only">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}"/>
			<wpsf:hidden name="frameIdParam" value="%{#showletConfig.get('frameIdParam')}"/>
			<s:if test="#pageTreeStyleVar == 'classic'">
				<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
			</s:if>
		</p>

		<s:if test="%{#showletConfig.get('frameIdParam') == null}">
			<div class="panel-body">
			<fieldset class="margin-large-top">
				<legend><s:text name="title.selectSourcePage" /></legend>
				<div class="well">
					<ul id="pageTree" class="fa-ul list-unstyled">
					<s:set name="inputFieldName" value="'pageCodeParam'" />
					<s:set name="selectedTreeNode" value="%{#showletConfig.get('pageCodeParam')}" />
					<s:set name="liClassName" value="'page'" />
					<s:set var="treeItemIconName" value="'fa-folder'" />
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
				</div>
			</fieldset>
			
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit action="browseFrames" type="button" cssClass="btn btn-primary btn-block">
						<s:text name="label.continue" />
					</wpsf:submit>
				</div>
			</div>
			
			</div>
		</s:if>

		<s:else>
			<div class="panel-body">
			<fieldset class="margin-large-top">
				<legend><s:text name="title.configuration" /></legend>
				<s:set var="targetPage" value="%{getPage(#showletConfig.get('pageCodeParam'))}" />
				<input type="hidden" name="pageCodeParam" value="<s:property value="%{#targetPage.code}" />" />
				<div class="input-group">
					<strong><s:text name="label.selectedPage" />:</strong><br />
					<s:iterator value="langs" status="rowStatus">
						<s:if test="#rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span>&#32;<s:property value="#targetPage.getTitles()[code]" />
					</s:iterator>
					&#32;
					<span class="input-group-btn">
						<wpsf:submit action="reset" value="%{getText('label.reconfigure')}" cssClass="btn btn-info" />
					</span>
				</div>
				<div class="input-group">
					<strong><s:text name="label.selectedFrame" />:</strong><br />
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
					<span class="input-group-btn">
						<wpsf:submit action="browseFrames" value="%{getText('label.browseFrames')}" cssClass="btn btn-info" />
					</span>
				</div>
				<%--
				<div class="form-group">
					<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
						<wpsf:submit action="reset" type="button" cssClass="btn btn-primary btn-block">
							<s:text name="label.reconfigure" />
						</wpsf:submit>
					</div>
				</div>
				--%>
			</fieldset>
			
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
						<span class="icon fa fa-floppy-o"></span>&#32;
						<s:text name="label.save" />
					</wpsf:submit>
				</div>
			</div>
				
			</div>
			
		</s:else>
		</div>

	</s:form>

</div>