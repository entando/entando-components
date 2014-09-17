<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.workflowManagement" />
	</span>
</h1>
<div id="main">
	<div class="table-responsive">
		<table class="table table-bordered">
			<caption class="sr-only"><s:text name="content.types.list" /></caption>
			<tr>
				<th class="text-center padding-large-left padding-large-right col-xs-4 col-sm-3 col-md-2 col-lg-2">
					<abbr title="<s:text name="label.actions" />">
						&ndash;
					</abbr>
				</th>
				<th>
					<s:text name="label.contentType" />
				</th>
			</tr>
			<s:iterator value="contentTypes" var="contentType">
				<tr>
					<td class="text-center text-nowrap">
						<div class="btn-group btn-group-xs">
							<a
								class="btn btn-default"
								title="<s:text name="label.editSteps" />:&#32;<s:text name="label.contentType" />"
								href="<s:url action="editSteps" ><s:param name="typeCode" value="%{#contentType.code}" /></s:url>">
									<span class="icon fa fa-edit"></span>
									<span class="sr-only"><s:text name="label.editSteps" />:&#32;<s:text name="label.contentType" /></span>
							</a>
							<a
								class="btn btn-default"
								title="<s:text name="label.editMainRole" />:&#32;<s:text name="label.contentType" />"
								href="<s:url action="editRole" ><s:param name="typeCode" value="%{#contentType.code}" /></s:url>">
									<span class="icon fa fa-users"></span>
									<span class="sr-only"><s:text name="label.editMainRole" />:&#32;<s:text name="label.contentType" /></span>
							</a>
						</div>
					</td>
					<td><s:property value="#contentType.descr"/></td>
				</tr>
			</s:iterator>
		</table>
	</div>
</div>
