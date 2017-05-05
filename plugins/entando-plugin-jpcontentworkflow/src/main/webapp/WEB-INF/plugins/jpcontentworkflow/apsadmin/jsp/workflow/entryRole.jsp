<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><a href="<s:url action="list" />"><s:text
				name="title.workflowManagement" /></a></li>
	<li class="page-title-container"><s:text
			name="title.workflowManagement.editRole" /></li>
</ol>
<h1 class="page-title-container">
	<div>
		<s:text name="title.workflowManagement.editRole" />
		<span class="pull-right"> <a tabindex="0" role="button"
			data-toggle="popover" data-trigger="focus" data-html="true" title=""
			data-content="TO be inserted" data-placement="left"
			data-original-title=""> <i class="fa fa-question-circle-o"
				aria-hidden="true"></i>
		</a>
		</span>
	</div>
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>
<div id="main">
	<s:form action="saveRole" cssClass="form-horizontal">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert">
					<span class="icon fa fa-times"></span>
				</button>
				<span class="pficon pficon-error-circle-o"></span>
				<s:text name="message.title.FieldErrors" />
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escapeHtml="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert">
					<span class="icon fa fa-times"></span>
				</button>
				<span class="pficon pficon-error-circle-o"></span>
				<s:text name="message.title.FieldErrors" />
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property escapeHtml="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<div class="form-group">
			<div class="col-xs-12">
				<label><s:text name="label.contentType" /></label> <span
					class="form-control-static display-block"><s:property
						value="contentType.descr" /></span>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<wpsf:hidden name="typeCode" />
				<label class="col-sm-2 control-label" for="jpcontentworkflow_role"><s:text
						name="label.role" /></label>
				<div class="col-sm-10">
					<wpsf:select name="role" id="jpcontentworkflow_role" headerKey=""
						headerValue="%{getText('label.none')}" list="roles" listKey="name"
						listValue="description" cssClass="form-control" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<div
				class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical pull-right">
				<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
