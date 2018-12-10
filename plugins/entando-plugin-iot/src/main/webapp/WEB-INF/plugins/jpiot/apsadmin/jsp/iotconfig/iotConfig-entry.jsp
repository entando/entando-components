<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="jpiot.title.iotConfigManagement" /></a>
		&#32;/&#32;
		<s:if test="getStrutsAction() == 1">
			<s:text name="jpiot.iotConfig.label.new" />
		</s:if>
		<s:elseif test="getStrutsAction() == 2">
			<s:text name="jpiot.iotConfig.label.edit" />
		</s:elseif>
	</span>
</h1>
	<s:form action="save" cssClass="form-horizontal">
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
		</div>
	</s:if>
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

	<p class="sr-only">
		<wpsf:hidden name="strutsAction" />
	<s:if test="getStrutsAction() == 2">
		<wpsf:hidden name="id" />
	</s:if>
	</p>

	<%-- name --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['name']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_name"><s:text name="label.name" /></label>
				<wpsf:textfield name="name" id="iotConfig_name" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- hostname --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['hostname']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_hostname"><s:text name="label.hostname" /></label>
				<wpsf:textfield name="hostname" id="iotConfig_hostname" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- port --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['port']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_port"><s:text name="label.port" /></label>
				<wpsf:textfield name="port" id="iotConfig_port" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- webapp --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['webapp']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_webapp"><s:text name="label.webapp" /></label>
				<wpsf:textfield name="webapp" id="iotConfig_webapp" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- username --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['username']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_username"><s:text name="label.username" /></label>
				<wpsf:textfield name="username" id="iotConfig_username" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- password --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['password']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_password"><s:text name="label.password" /></label>
				<wpsf:textfield name="password" id="iotConfig_password" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>
	<%-- token --%>
		<s:set var="fieldFieldErrorsVar" value="%{fieldErrors['token']}" />
		<s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
		<s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
		<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
			<div class="col-xs-12">
				<label for="iotConfig_token"><s:text name="label.token" /></label>
				<wpsf:textfield name="token" id="iotConfig_token" cssClass="form-control" />
				<s:if test="#fieldHasFieldErrorVar">
					<span class="help-block text-danger">
						<s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
					</span>
				</s:if>
			</div>
		</div>

	<%-- save button --%>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>

	</s:form>

</div>
