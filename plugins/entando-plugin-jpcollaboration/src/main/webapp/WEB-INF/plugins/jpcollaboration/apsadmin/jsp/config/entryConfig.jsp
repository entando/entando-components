<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpcrowdsourcing.admin.title" /></li>
	<li class="page-title-container"><s:text
			name="jpcrowdsourcing.title.configure" /></li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:text name="jpcrowdsourcing.title.configure" />
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
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
								name="jpcrowdsourcing.ideaInstance.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea" />"><s:text
								name="jpcrowdsourcing.idea.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />"><s:text
								name="jpcrowdsourcing.comment.list" /></a></li>
					<li class="active"><a
						href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />"><s:text
								name="jpcrowdsourcing.config" /></a></li>
				</ul>
			</div>
		</wp:ifauthorized>
	</div>
</div>
<br>
<div id="messages">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
	<s:form action="saveConfig">
		<fieldset class="col-xs-12">
			<legend>
				<s:text name="jpcrowdsourcing.title.moderation" />
			</legend>
			<p>
				<s:text name="jpcrowdsourcing.note.moderation" />
			</p>

			<div class="form-group">
				<div class="checkbox">
					<label class="col-sm-2 control-label" for="moderateEntries"><s:text
							name="jpcrowdsourcing.label.idea.moderation" /></label>
					<div class="col-sm-10">
						<wpsf:checkbox name="moderateEntries" id="moderateEntries"
							value="%{moderateEntries}" cssClass="bootstrap-switch" />
					</div>
				</div>
				<br> <br>
				<div class="checkbox">
					<label class="col-sm-2 control-label" for="moderateComments"><s:text
							name="jpcrowdsourcing.label.comment.moderation" /></label>
					<div class="col-sm-10">
						<wpsf:checkbox name="moderateComments" id="moderateComments"
							value="%{moderateComments}" cssClass="bootstrap-switch" />
					</div>
				</div>
			</div>
		</fieldset>

		<div class="form-group">
			<div class="col-xs-12">
				<s:submit type="button" cssClass="btn btn-primary pull-right">
					<s:text name="%{getText('label.save')}" />
				</s:submit>
			</div>
		</div>

	</s:form>
</div>