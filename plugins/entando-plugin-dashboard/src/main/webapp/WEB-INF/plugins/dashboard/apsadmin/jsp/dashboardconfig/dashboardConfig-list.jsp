<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="dashboard.title.dashboardConfigManagement" />
	</span>
</h1>
<s:form action="list" cssClass="form-horizontal" role="search">
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
	<div class="form-group">
		<div class="input-group col-sm-12 col-md-12">
			<span class="input-group-addon">
				<span class="icon fa fa-file-text-o fa-lg" title="<s:text name="label.search.by"/>&#32;<s:text name="label.id"/>"></span>
			</span>
			<label for="search-id" class="sr-only"><s:text name="label.search.by"/>&#32;<s:text name="label.id"/></label>
			<wpsf:textfield
				id="dashboardConfig_id"
				name="id"
				cssClass="form-control input-lg"
				title="%{getText('label.search.by')+' '+getText('label.id')}"
				placeholder="%{getText('label.id')}" />
			<div class="input-group-btn">
				<wpsf:submit type="button" name="search-id" id="search-id" cssClass="btn btn-primary btn-lg" title="%{getText('label.search')}">
					<span class="sr-only"><s:text name="label.search" /></span>
					<span class="icon fa fa-search" title="<s:text name="label.search" />"></span>
				</wpsf:submit>
				<button type="button" class="btn btn-primary btn-lg dropdown-toggle" data-toggle="collapse" data-target="#search-advanced" title="<s:text name="title.searchFilters" />">
					<span class="sr-only"><s:text name="title.searchFilters" /></span>
					<span class="caret"></span>
				</button>
			</div>
		</div>

	  <div class="input-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div id="search-advanced" class="collapse well collapse-input-group">
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_serverDescription">
						<s:text name="label.serverDescription"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_serverDescription"
							name="serverDescription"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_serverURI">
						<s:text name="label.serverURI"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_serverURI"
							name="serverURI"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_username">
						<s:text name="label.username"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_username"
							name="username"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_password">
						<s:text name="label.password"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_password"
							name="password"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_token">
						<s:text name="label.token"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_token"
							name="token"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_timeConnection">
						<s:text name="label.timeConnection"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_timeConnection"
							name="timeConnection"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_active">
						<s:text name="label.active"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_active"
							name="active"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="dashboardConfig_debug">
						<s:text name="label.debug"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="dashboardConfig_debug"
							name="debug"
							cssClass="form-control" />
					</div>
				</div>
			<div class="form-group">
				<div class="col-sm-5 col-sm-offset-2">
					<s:submit type="button" cssClass="btn btn-primary">
						<span class="icon fa fa-search"></span>&#32;<s:text name="label.search" />
					</s:submit>
				</div>
			</div>
		</div>
	</div>
	</div>
</s:form>

<a href="<s:url action="new" />" class="btn btn-default">
	<span class="icon fa fa-plus-circle" />
	&#32;<s:text name="dashboard.dashboardConfig.label.new" />
</a>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="id" />
		<wpsf:hidden name="serverDescription" />
		<wpsf:hidden name="serverURI" />
		<wpsf:hidden name="username" />
		<wpsf:hidden name="password" />
		<wpsf:hidden name="token" />
		<wpsf:hidden name="timeConnection" />
		<wpsf:hidden name="active" />
		<wpsf:hidden name="debug" />
	</p>

	<s:set var="dashboardConfigsId_list" value="dashboardConfigsId" />
	<s:if test="#dashboardConfigsId_list.size > 0">
	<wpsa:subset source="#dashboardConfigsId_list" count="10" objectName="groupDashboardConfigs" advanced="true" offset="5">
	<s:set var="group" value="#groupDashboardConfigs" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th class="text-right"><s:text name="label.id" /></th>
				<th><s:text name="label.serverDescription" /></th>
				<th><s:text name="label.serverURI" /></th>
				<th><s:text name="label.username" /></th>
				<th><s:text name="label.password" /></th>
				<th><s:text name="label.token" /></th>
				<th class="text-right"><s:text name="label.timeConnection" /></th>
				<th class="text-right"><s:text name="label.active" /></th>
				<th class="text-right"><s:text name="label.debug" /></th>
			</tr>
			<s:iterator var="id">
			<s:set var="dashboardConfig_var" value="%{getDashboardConfig(#id)}" />
			<s:url action="edit" var="editDashboardConfigActionVar"><s:param name="id" value="#dashboardConfig_var.id"/></s:url>
			<s:url action="trash" var="trashDashboardConfigActionVar"><s:param name="id" value="#dashboardConfig_var.id"/></s:url>
			<tr>
			<td class="text-center text-nowrap">
				<div class="btn-group btn-group-xs">
					<%-- edit --%>
						<a class="btn btn-default" title="<s:text name="label.edit" />&#32;<s:property value="#dashboardConfig_var.id" />" href="<s:property value="#editDashboardConfigActionVar" escapeHtml="false" />">
							<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#dashboardConfig_var.id" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</a>
				</div>
				<%-- remove --%>
				<div class="btn-group btn-group-xs">
					<a
						href="<s:property value="#trashDashboardConfigActionVar" escapeHtml="false" />"
						title="<s:text name="dashboard.dashboardConfig.label.delete" />: <s:property value="#dashboardConfig_var.id" />"
						class="btn btn-warning"
						>
						<span class="icon fa fa-times-circle-o"></span>&#32;
						<span class="sr-only"><s:text name="dashboard.dashboardConfig.label.delete" /></span>
					</a>
				</div>
			</td>
					<td class="text-right"><code><s:property value="#dashboardConfig_var.id"/></code></td>
					<td><s:property value="#dashboardConfig_var.serverDescription"/></td>
					<td><s:property value="#dashboardConfig_var.serverURI"/></td>
					<td><s:property value="#dashboardConfig_var.username"/></td>
					<td><s:property value="#dashboardConfig_var.password"/></td>
					<td><s:property value="#dashboardConfig_var.token"/></td>
					<td class="text-right"><s:property value="#dashboardConfig_var.timeConnection"/></td>
					<td class="text-right"><s:property value="#dashboardConfig_var.active"/></td>
					<td class="text-right"><s:property value="#dashboardConfig_var.debug"/></td>
			</tr>
			</s:iterator>
		</table>
	</div>
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	</wpsa:subset>
	</s:if>
	<s:else>
		<div class="alert alert-info margin-base-vertical">
			<s:text name="dashboard.dashboardConfig.message.list.empty" />
		</div>
	</s:else>
</s:form>