<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpiot.title.iotConfigManagement" />
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
				id="iotConfig_id"
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
					<label class="control-label col-sm-2 text-right" for="iotConfig_name">
						<s:text name="label.name"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_name"
							name="name"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotConfig_hostname">
						<s:text name="label.hostname"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_hostname"
							name="hostname"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotConfig_port">
						<s:text name="label.port"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_port"
							name="port"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotConfig_webapp">
						<s:text name="label.webapp"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_webapp"
							name="webapp"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotConfig_username">
						<s:text name="label.username"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_username"
							name="username"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotConfig_password">
						<s:text name="label.password"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_password"
							name="password"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotConfig_token">
						<s:text name="label.token"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotConfig_token"
							name="token"
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
	&#32;<s:text name="jpiot.iotConfig.label.new" />
</a>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="id" />
		<wpsf:hidden name="name" />
		<wpsf:hidden name="hostname" />
		<wpsf:hidden name="port" />
		<wpsf:hidden name="webapp" />
		<wpsf:hidden name="username" />
		<wpsf:hidden name="password" />
		<wpsf:hidden name="token" />
	</p>

	<s:set var="iotConfigsId_list" value="iotConfigsId" />
	<s:if test="#iotConfigsId_list.size > 0">
	<wpsa:subset source="#iotConfigsId_list" count="10" objectName="groupIotConfigs" advanced="true" offset="5">
	<s:set var="group" value="#groupIotConfigs" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th class="text-right"><s:text name="label.id" /></th>
				<th><s:text name="label.name" /></th>
				<th><s:text name="label.hostname" /></th>
				<th class="text-right"><s:text name="label.port" /></th>
				<th><s:text name="label.webapp" /></th>
				<th><s:text name="label.username" /></th>
				<th><s:text name="label.password" /></th>
				<th><s:text name="label.token" /></th>
			</tr>
			<s:iterator var="id">
			<s:set var="iotConfig_var" value="%{getIotConfig(#id)}" />
			<s:url action="edit" var="editIotConfigActionVar"><s:param name="id" value="#iotConfig_var.id"/></s:url>
			<s:url action="trash" var="trashIotConfigActionVar"><s:param name="id" value="#iotConfig_var.id"/></s:url>
			<tr>
			<td class="text-center text-nowrap">
				<div class="btn-group btn-group-xs">
					<%-- edit --%>
						<a class="btn btn-default" title="<s:text name="label.edit" />&#32;<s:property value="#iotConfig_var.id" />" href="<s:property value="#editIotConfigActionVar" escapeHtml="false" />">
							<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#iotConfig_var.id" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</a>
				</div>
				<%-- remove --%>
				<div class="btn-group btn-group-xs">
					<a
						href="<s:property value="#trashIotConfigActionVar" escapeHtml="false" />"
						title="<s:text name="jpiot.iotConfig.label.delete" />: <s:property value="#iotConfig_var.id" />"
						class="btn btn-warning"
						>
						<span class="icon fa fa-times-circle-o"></span>&#32;
						<span class="sr-only"><s:text name="jpiot.iotConfig.label.delete" /></span>
					</a>
				</div>
			</td>
					<td class="text-right"><code><s:property value="#iotConfig_var.id"/></code></td>
					<td><s:property value="#iotConfig_var.name"/></td>
					<td><s:property value="#iotConfig_var.hostname"/></td>
					<td class="text-right"><s:property value="#iotConfig_var.port"/></td>
					<td><s:property value="#iotConfig_var.webapp"/></td>
					<td><s:property value="#iotConfig_var.username"/></td>
					<td><s:property value="#iotConfig_var.password"/></td>
					<td><s:property value="#iotConfig_var.token"/></td>
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
			<s:text name="jpiot.iotConfig.message.list.empty" />
		</div>
	</s:else>
</s:form>