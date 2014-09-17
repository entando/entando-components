<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
			<a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />">
				<s:text name="jprssaggregator.title.rssAggregator.rssManagement" />
			</a>
			&#32;/&#32;
			<s:if test="strutsAction == 1">
				<s:text name="jprssaggregator.title.rssAggregator.add" />
			</s:if><s:elseif test="strutsAction == 2">
				<s:text name="jprssaggregator.title.rssAggregator.edit" />
			</s:elseif>
	</span>
</h1>
<div id="main">
	<s:form action="save" cssClass="form-horizontal action-form">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /> &ensp; <small
						class="icon fa fa-question-circle cursor-pointer text-danger"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#content-error-messages"></small>
					<span class="sr-only"><s:text name="label.all" /></span></h2>
				<ul class="unstyled collapse margin-small-top" id="content-error-messages">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><%-- <s:property value="key" />&emsp;|--%><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
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
		<div class="sr-only">
				<wpsf:hidden name="strutsAction" />
				<wpsf:hidden name="code" />
				<wpsf:hidden name="lastUpdate" />
				<wpsf:hidden name="xmlCategories" />
				<s:if test="strutsAction == 2">
					<wpsf:hidden name="contentType" />
				</s:if>
				<s:if test="strutsAction == 1"><s:set var="delay" value="86400" /></s:if>
				<s:else><s:set var="delay" value="delay" /></s:else>
				<s:if test="#categoryTreeStyleVar == 'request'">
					<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
						<wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
					</s:iterator>
				</s:if>
		</div>

		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['contentType']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="rss_contentType"><s:text name="jprssaggregator.rssAggregator.contentType" /></label>
				<s:if test="strutsAction == 1">
					<wpsf:select cssClass="form-control" name="contentType" id="rss_contentType" list="contentTypes"  listKey="code" listValue="descr" />
				</s:if>
				<s:elseif test="strutsAction == 2">
					<wpsf:textfield cssClass="form-control" name="contentType" id="rss_contentType" value="%{getSmallContentType(contentType).descr}" disabled="true" />
				</s:elseif>
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>

		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['descr']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="rss_descr"><s:text name="jprssaggregator.rssAggregator.description" /></label>
				<wpsf:textfield id="rss_descr" name="descr" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>

		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['link']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="rss_link"><s:text name="jprssaggregator.rssAggregator.url" /></label>
				<wpsf:textfield id="rss_link" name="link" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>

		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['delay']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="rss_delay"><s:text name="jprssaggregator.rssAggregator.delay" /></label>
				<wpsf:select cssClass="form-control" name="delay" id="rss_delay" list="delays" listKey="key" listValue="value" value="#delay" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>

		<div class="form-group">
			<div class="col-xs-12">
					<label><s:text name="title.categoriesManagement" /></label>
					<div class="well">
						<ul id="categoryTree" class="fa-ul list-unstyled">
							<s:set var="inputFieldName" value="%{'categoryCode'}" />
							<s:set var="selectedTreeNode" value="%{categoryCode}" />
							<s:set var="liClassName" value="'category'" />
							<s:set var="treeItemIconName" value="'fa-folder'" />

							<s:if test="#categoryTreeStyleVar == 'classic'">
								<s:set var="currentRoot" value="categoryRoot" />
								<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
							</s:if>
							<s:elseif test="#categoryTreeStyleVar == 'request'">
								<s:set var="openTreeActionName" value="'openCloseCategoryTreeNode'" />
								<s:set var="closeTreeActionName" value="'openCloseCategoryTreeNode'" />
								<s:set var="currentRoot" value="showableTree" />
								<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
							</s:elseif>
						</ul>
					</div>
					<fieldset data-toggle="tree-toolbar"><legend><s:text name="label.actions" /></legend>
						<div class="btn-toolbar" data-toggle="tree-toolbar-actions">
							<div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
								<wpsf:submit type="button" action="joinCategory" title="%{getText('label.join')}" data-toggle="tooltip" cssClass="btn btn-info">
									<span class="sr-only"><s:text name="label.join" /></span>
									<span class="icon fa fa-plus"></span>
								</wpsf:submit>
							</div>
						</div>
					</fieldset>

					<s:set var="categoriesVar" value="categories" />
					<label class="sr-only"><s:text name="title.contentCategories.list" /></label>
					<s:if test="#categoriesVar != null && #categoriesVar.size() > 0">
						<s:iterator value="#categoriesVar" id="contentCategory">
							<s:set var="contentCategory" value="%{getCategory(#contentCategory.key)}"></s:set>
							<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
								<span class="icon fa fa-tag"></span>&#32;
								<abbr title="<s:property value="#contentCategory.getFullTitle(currentLang.code)"/>"><s:property value="#contentCategory.getShortFullTitle(currentLang.code)" /></abbr>&#32;
								<wpsa:actionParam action="removeCategory" var="actionName" >
									<wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
								</wpsa:actionParam>
								<wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #contentCategory.defaultFullTitle}" cssClass="btn btn-default btn-xs badge">
									<span class="icon fa fa-times"></span>
									<span class="sr-only">x</span>
								</wpsf:submit>
							</span>
						</s:iterator>
					</s:if>
			</div>
		</div>
		<div class="form-group margin-large-top">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<s:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-save"></span>&#32;
					<s:text name="label.save" />
				</s:submit>
			</div>
		</div>
	</s:form>
</div>
