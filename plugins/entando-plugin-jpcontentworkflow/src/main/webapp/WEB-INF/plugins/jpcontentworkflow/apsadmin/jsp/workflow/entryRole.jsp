<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.workflowManagement" /></a>
		&#32;/&#32;
		<s:text name="title.workflowManagement.editRole" />
	</span>
</h1>
<div id="main">
	<s:form action="saveRole" cssClass="form-horizontal">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
					<ul class="margin-base-top">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
					<ul class="margin-base-top">
						<s:iterator value="actionErrors">
						<li><s:property escape="false"/></li>
					</s:iterator>
					</ul>
			</div>
		</s:if>
		<div class="form-group">
			<div class="col-xs-12">
				<label><s:text name="label.contentType" /></label>
				<span class="form-control-static display-block"><s:property value="contentType.descr"/></span>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<wpsf:hidden name="typeCode" />
					<label for="jpcontentworkflow_role"><s:text name="label.role" /></label>
					<wpsf:select
						name="role"
						id="jpcontentworkflow_role"
						headerKey=""
						headerValue="%{getText('label.none')}"
						list="roles"
						listKey="name" listValue="description"
						cssClass="form-control"
						/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-save"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
