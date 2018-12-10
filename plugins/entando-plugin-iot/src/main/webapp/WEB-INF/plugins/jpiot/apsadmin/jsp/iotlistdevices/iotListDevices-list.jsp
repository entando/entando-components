<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpiot.title.iotListDevicesManagement" />
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
				id="iotListDevices_id"
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
					<label class="control-label col-sm-2 text-right" for="iotListDevices_widgetTitle">
						<s:text name="label.widgetTitle"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_widgetTitle"
							name="widgetTitle"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotListDevices_datasource">
						<s:text name="label.datasource"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_datasource"
							name="datasource"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotListDevices_context">
						<s:text name="label.context"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_context"
							name="context"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotListDevices_download">
						<s:text name="label.download"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_download"
							name="download"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotListDevices_filter">
						<s:text name="label.filter"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_filter"
							name="filter"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotListDevices_allColumns">
						<s:text name="label.allColumns"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_allColumns"
							name="allColumns"
							cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2 text-right" for="iotListDevices_columns">
						<s:text name="label.columns"/>
					</label>
					<div class="col-sm-5">
						<wpsf:textfield
							id="iotListDevices_columns"
							name="columns"
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
	&#32;<s:text name="jpiot.iotListDevices.label.new" />
</a>

<s:form action="search">
	<p class="sr-only">
		<wpsf:hidden name="id" />
		<wpsf:hidden name="widgetTitle" />
		<wpsf:hidden name="datasource" />
		<wpsf:hidden name="context" />
		<wpsf:hidden name="download" />
		<wpsf:hidden name="filter" />
		<wpsf:hidden name="allColumns" />
		<wpsf:hidden name="columns" />
	</p>

	<s:set var="iotListDevicessId_list" value="iotListDevicessId" />
	<s:if test="#iotListDevicessId_list.size > 0">
	<wpsa:subset source="#iotListDevicessId_list" count="10" objectName="groupIotListDevicess" advanced="true" offset="5">
	<s:set var="group" value="#groupIotListDevicess" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	<div class="table-responsive">
		<table class="table table-bordered">
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				<th class="text-right"><s:text name="label.id" /></th>
				<th><s:text name="label.widgetTitle" /></th>
				<th><s:text name="label.datasource" /></th>
				<th><s:text name="label.context" /></th>
				<th class="text-right"><s:text name="label.download" /></th>
				<th class="text-right"><s:text name="label.filter" /></th>
				<th class="text-right"><s:text name="label.allColumns" /></th>
				<th><s:text name="label.columns" /></th>
			</tr>
			<s:iterator var="id">
			<s:set var="iotListDevices_var" value="%{getIotListDevices(#id)}" />
			<s:url action="edit" var="editIotListDevicesActionVar"><s:param name="id" value="#iotListDevices_var.id"/></s:url>
			<s:url action="trash" var="trashIotListDevicesActionVar"><s:param name="id" value="#iotListDevices_var.id"/></s:url>
			<tr>
			<td class="text-center text-nowrap">
				<div class="btn-group btn-group-xs">
					<%-- edit --%>
						<a class="btn btn-default" title="<s:text name="label.edit" />&#32;<s:property value="#iotListDevices_var.id" />" href="<s:property value="#editIotListDevicesActionVar" escapeHtml="false" />">
							<span class="sr-only"><s:text name="label.edit" />&#32;<s:property value="#iotListDevices_var.id" /></span>
							<span class="icon fa fa-pencil-square-o"></span>
						</a>
				</div>
				<%-- remove --%>
				<div class="btn-group btn-group-xs">
					<a
						href="<s:property value="#trashIotListDevicesActionVar" escapeHtml="false" />"
						title="<s:text name="jpiot.iotListDevices.label.delete" />: <s:property value="#iotListDevices_var.id" />"
						class="btn btn-warning"
						>
						<span class="icon fa fa-times-circle-o"></span>&#32;
						<span class="sr-only"><s:text name="jpiot.iotListDevices.label.delete" /></span>
					</a>
				</div>
			</td>
					<td class="text-right"><code><s:property value="#iotListDevices_var.id"/></code></td>
					<td><s:property value="#iotListDevices_var.widgetTitle"/></td>
					<td><s:property value="#iotListDevices_var.datasource"/></td>
					<td><s:property value="#iotListDevices_var.context"/></td>
					<td class="text-right"><s:property value="#iotListDevices_var.download"/></td>
					<td class="text-right"><s:property value="#iotListDevices_var.filter"/></td>
					<td class="text-right"><s:property value="#iotListDevices_var.allColumns"/></td>
					<td><s:property value="#iotListDevices_var.columns"/></td>
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
			<s:text name="jpiot.iotListDevices.message.list.empty" />
		</div>
	</s:else>
</s:form>