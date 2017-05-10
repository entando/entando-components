<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li class="page-title-container"><s:text
			name="title.workflowManagement" /></li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:text name="title.workflowManagement" />
				<span class="pull-right"> <a tabindex="0" role="button"
					data-toggle="popover" data-trigger="focus" data-html="true"
					title="" data-content="TO be inserted" data-placement="left"
					data-original-title=""> <i class="fa fa-question-circle-o"
						aria-hidden="true"></i>
				</a>
				</span>
			</h1>
		</div>
		<div class="col-sm-6">
			<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
				<li class="active"><a
					href="<s:url action="list" namespace="/do/jpcontentworkflow/Workflow" />"><s:text
							name="jpcontentworkflow.menu.workflow" /></a></li>
				<li><a
					href="<s:url action="config" namespace="/do/jpcontentworkflow/Notifier" />"><s:text
							name="jpcontentworkflow.menu.notifier" /></a></li>
			</ul>
		</div>
	</div>
</div>
<br>
<div id="main">
	<table class="table table-striped table-bordered table-hover no-mb">
		<caption class="sr-only">
			<s:text name="content.types.list" />
		</caption>
		<thead>
			<tr>
				<th><s:text name="label.contentType" /></th>
				<th class="text-center col-sm-1"><s:text name="label.actions" /></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="contentTypes" var="contentType">
				<tr>
					<td><s:property value="#contentType.descr" /></td>
					<td class="table-view-pf-actions text-center">
						<div class="dropdown dropdown-kebab-pf">
							<div class="btn-group btn-group-xs">
								<button class="btn btn-menu-right dropdown-toggle" type="button"
									data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false">
									<span class="fa fa-ellipsis-v"></span>
								</button>
								<ul class="dropdown-menu dropdown-menu-right">
									<li><a
										title="<s:text name="label.editSteps" />:&#32;<s:text name="label.contentType" />"
										href="<s:url action="editSteps" ><s:param name="typeCode" value="%{#contentType.code}" /></s:url>">
											<span><s:text name="label.editSteps" /></span>
									</a></li>
									<li><a
										title="<s:text name="label.editMainRole" />:&#32;<s:text name="label.contentType" />"
										href="<s:url action="editRole" ><s:param name="typeCode" value="%{#contentType.code}" /></s:url>">
											<span><s:text name="label.editMainRole" /></span>
									</a></li>
								</ul>
							</div>
						</div>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
