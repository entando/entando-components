<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpcrowdsourcing.admin.title" /></li>
	<li><a
		href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
				name="jpcrowdsourcing.title.ideaInstanceManagement" /></a></li>
	<li class="page-title-container"><s:text name="label.delete" /></li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:text name="label.delete" />
				<span class="pull-right"> <a tabindex="0" role="button"
					data-toggle="popover" data-trigger="focus" data-html="true"
					title="" data-content="TO be inserted" data-placement="left"
					data-original-title=""> <i class="fa fa-question-circle-o"
						aria-hidden="true"></i>
				</a>
				</span>
			</h1>
		</div>
		<wp:ifauthorized permission="superuser">
			<div class="col-sm-6">
				<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
					<li class="active"><a
						href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
								name="jpcrowdsourcing.ideaInstance.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea" />"><s:text
								name="jpcrowdsourcing.idea.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />"><s:text
								name="jpcrowdsourcing.comment.list" /></a></li>
					<li><a
						href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />"><s:text
								name="jpcrowdsourcing.config" /></a></li>
				</ul>
			</div>
		</wp:ifauthorized>
	</div>
</div>
<br>

<div class="text-center">
	<s:form action="delete">
		<wpsf:hidden name="strutsAction" />
		<wpsf:hidden name="code" />
		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="label.delete" />
		</p>
		<p>
			<s:text name="note.deleteIdeaInstance.areYouSure" />
			&#32;
			<s:property value="code" />
			?
		</p>
		<div class="text-center margin-large-top">
			<wpsf:submit type="button"
				cssClass="btn btn-danger button-fixed-width">
				<s:text name="%{getText('label.remove')}" />
			</wpsf:submit>
		</div>
		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width"
				href="<s:url action="list" />"><s:text
					name="%{getText('label.back')}" /></a>
		</div>

		<s:set var="ideaInstance_var" value="%{getIdeaInstance(code)}" />
		<s:if test="#ideaInstance_var.children.size > 0">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="margin-none">
						<s:text name="jpcrowdsourcing.label.delete.reference" />
					</h3>
				</div>
				<div class="panel-body">
					<table class="table table-striped table-bordered table-hover no-mb">
						<thead>
							<tr>
								<th class="text-center"><s:text
										name="jpcrowdsourcing.label.title" /></th>
								<th class="text-center"><s:text
										name="jpcrowdsourcing.label.instance" /></th>
								<th class="text-center"><s:text
										name="jpcrowdsourcing.label.author" /></th>
								<th class="text-center"><s:text
										name="jpcrowdsourcing.label.date" /></th>
								<th class="text-center"><s:text
										name="jpcrowdsourcing.label.comments" /></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="#ideaInstance_var.children" var="ideaId">
								<tr class="text-center">
									<s:set var="idea" value="%{getIdea(#ideaId)}" />
									<s:url action="edit" namespace="/do/collaboration/Idea"
										var="editAction">
										<s:param name="ideaId" value="#idea.id" />
									</s:url>
									<td><a href="<s:property value="#editAction"/>"
										title="<s:text name="label.edit" />"><s:property
												value="#idea.title" /></a></td>
									<td><s:url action="list"
											namespace="/do/collaboration/IdeaInstance"
											var="link_to_instancelist">
											<s:param name="code" value="#idea.instanceCode" />
										</s:url> <a href="<s:property value="#link_to_instancelist" />"> <s:property
												value="#idea.instanceCode" />
									</a></td>
									<td><code>
											<s:property value="#idea.username" />
										</code></td>
									<td class="text-center"><code>
											<s:date name="#idea.pubDate" format="dd/MM/yyyy" />
										</code></td>
									<td class="text-right"><s:if
											test="#idea.comments.size > 0">
											<s:url var="commentListAction" action="list"
												namespace="/do/collaboration/Idea/Comment">
												<s:param name="ideaId" value="#idea.id" />
											</s:url>
											<span class="monospace"><a
												href="<s:property value="#commentListAction" />"
												title="<s:text name="jpcrowdsourcing.label.goToComment" />"><s:property
														value="#idea.comments.size" /></a></span>
										</s:if> <s:else>
											<span class="monospace"><s:text
													name="jpcrowdsourcing.label.zero" /></span>
										</s:else></td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</s:if>
	</s:form>
</div>