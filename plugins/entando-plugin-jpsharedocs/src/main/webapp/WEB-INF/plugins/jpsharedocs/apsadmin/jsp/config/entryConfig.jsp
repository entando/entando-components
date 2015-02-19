<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.jpsharedocs" /></span>
</h1>

<div id="main">
	<s:form action="save">
	
		<s:if test="hasActionMessages()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="messages.error" /></h2>
				<ul class="margin-base-top">
					<s:iterator value="actionMessages">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none">
					<s:text name="messages.error" />
					&ensp;
					<span
						class=" text-muted icon fa fa-question-circle cursor-pointer"
						title="<s:text name="label.all" />"
						data-toggle="collapse"
						data-target="#content-field-messages"></span>
				</h2>
				<ul class="collapse margin-small-top" id="content-field-messages">
					<s:iterator value="fieldErrors" var="e">
						<s:iterator value="#e.value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>


		<fieldset class="col-xs-12 margin-large-top">
			<legend><s:text name="label.tempPath" /></legend>

			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.tmpFolderPath']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.tmpFolderPath"><s:text name="label.tmpFolderPath" /></label>
				<wpsf:textfield placeholder="/secret/temporary/folder/" name="config.tmpFolderPath" id="config.tmpFolderPath" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />
		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><s:text name="label.deletion" /></legend>
			<div class="form-group">
				<label class="checkbox">
					<wpsf:checkbox name="config.deleteOnRemove" id="config.deleteOnRemove" cssClass="radiocheck"/>
					&#32;<s:text name="label.deleteOnRemove" />
				</label>
				<label class="checkbox">
					<wpsf:checkbox name="config.deleteResources" id="config.deleteResources" cssClass="radiocheck"/>
					&#32;<s:text name="label.deleteResources" />
				</label>
			</div>
		</fieldset>

		<div class="form-horizontal">
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
						<span class="icon fa fa-floppy-o"></span>&#32;
						<s:text name="label.save" />
					</wpsf:submit>
				</div>
			</div>
		</div>

	</s:form>
</div>
