<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpcrowdsourcing.admin.title" /></li>
	<li><s:text name="jpcrowdsourcing.title.comments" /></li>
	<li class="page-title-container"><s:text
			name="jpcrowdsourcing.title.comments.delete" /></li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:text name="jpcrowdsourcing.title.comments.delete" />
				<span class="pull-right"> <a tabindex="0" role="button"
					data-toggle="popover" data-trigger="focus" data-html="true"
					title=""
					data-content="<s:text name="jpcrowdsourcing.title.comments.delete.help"/>"
					data-placement="left" data-original-title=""> <i
						class="fa fa-question-circle-o" aria-hidden="true"></i>
				</a>
				</span>
			</h1>
		</div>
		<div class="col-sm-6">
			<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
				<li><a
					href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
							name="jpcrowdsourcing.ideaInstance.list" /></a></li>
				<li><a
					href="<s:url action="list" namespace="/do/collaboration/Idea" />"><s:text
							name="jpcrowdsourcing.idea.list" /></a></li>
				<li class="active"><a
					href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />"><s:text
							name="jpcrowdsourcing.comment.list" /></a></li>
				<li><a
					href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />"><s:text
							name="jpcrowdsourcing.config" /></a></li>
			</ul>
		</div>
	</div>
</div>

<div id="main">

	<s:form action="delete" class="form-horizontal">
		<p class="noscreen">
			<wpsf:hidden name="strutsAction" value="%{strutsAction}" />
			<wpsf:hidden name="commentId" value="%{commentId}" />
		</p>

		<s:set var="comment" value="%{getComment(commentId)}" />

		<div class="table-responsive">
			<table class="table table-bordered table-hover no-mb">
				<tr>
					<th class="text-right col-sm-2"><s:text
							name="jpcrowdsourcing.label.description" /></th>
					<td class="col-sm-10"><s:property value="#comment.comment" /></td>
				</tr>
				<tr>
					<th class="text-right col-sm-2"><s:text
							name="jpcrowdsourcing.label.date" /></th>
					<td class="col-sm-10"><code>
							<s:property value="#comment.creationDate" />
						</code></td>
				</tr>
				<tr>
					<th class="text-right col-sm-2"><s:text name="label.state" /></th>
					<td class="col-sm-10"><s:property value="#comment.status" /></td>
				</tr>
				<tr>
					<th class="text-right col-sm-2"><s:text
							name="jpcrowdsourcing.label.author" /></th>
					<td class="col-sm-10"><code>
							<s:property value="#comment.username" />
						</code></td>
				</tr>
			</table>
		</div>

		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline">
			<s:text name="label.delete" />
		</p>
		<p>
			<s:text name="jpcrowdsourcing.note.areYouSureDelete" />
		</p>
		<div class="text-center margin-large-top">
			<s:submit type="button" cssClass="btn btn-danger button-fixed-width">
				<s:text name="%{getText('label.remove')}" />
			</s:submit>
		</div>
		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width"
				href="<s:url action="list" />"> <s:text
					name="%{getText('label.back')}" />
			</a>
		</div>
	</s:form>
</div>
