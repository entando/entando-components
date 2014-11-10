<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
		<s:text name="jpuserreg.title.config" />
    </span>
</h1>
<div id="main">

	<s:form action="save">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
				<ul class="margin-base-vertical">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property/></li>
							</s:iterator>
						</s:iterator>
				</ul>
			</div>
		</s:if>

		<s:if test="hasActionErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
				<ul class="margin-base-vertical">
					<s:iterator value="actionErrors">
						<li><s:property/></li>
						</s:iterator>
				</ul>
			</div>
		</s:if>

		<fieldset class="col-xs-12">
			<legend><s:text name="label.info" /></legend>
			<p>
				<s:iterator var="defaultAuthVar" value="config.defaultCsvAuthorizations">
					<wpsf:hidden name="config.defaultCsvAuthorizations" value="%{#defaultAuthVar}" />
				</s:iterator>
			</p>

			<div class="form-group">
				<label for="tokenValidityMinutes" ><s:text name="jpuserreg.tokenValidityMinutes" /></label>
				<wpsf:select id="tokenValidityMinutes" name="config.tokenValidityMinutes" 
							 list="#{ 
							 1440: '1 '+getText('day'),
								 2880: '2 '+getText('days'),
								 4320: '3 '+getText('days'),
								 5760: '4 '+getText('days'),
								 7200: '5 '+getText('days'),
								 8640: '6 '+getText('days'),
								 10080: '7 '+getText('days'), 
								 20160: '14 '+getText('days'),
								 43200: '30 '+getText('days')
							 }" cssClass="form-control" /> 
			</div>

			<div class="form-group">
				<label for="eMailSenderCode"><s:text name="jpuserreg.eMailSenderCode" />:</label>
				<wpsf:select name="config.eMailSenderCode" id="eMailSenderCode" 
							 list="mailSenders" listKey="key" 
							 listValue="value" 
							 cssClass="form-control">

				</wpsf:select>
			</div>
		</fieldset>
		
		<fieldset class="col-xs-12">
			<legend><s:text name="jpuserreg.defaultAuthorizations" /></legend>
			
			<div class="form-group">
				<div class="col-xs-12">
					<label for="userGroup"><s:text name="jpuserreg.label.userGroup" /></label>
					<wpsf:select id="userGroup" name="groupName" list="groups" 
						listKey="name" listValue="description" cssClass="form-control" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-12">
					<label for="userRole"><s:text name="jpuserreg.label.userRole" /></label>
					<wpsf:select id="userRole" name="roleName" list="roles" 
						headerKey="" headerValue="" 
						listKey="name" listValue="description" cssClass="form-control" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-12">
					<span class="input-group-btn">
						<wpsa:actionParam action="addAuthorization" var="actionName" />
						<wpsf:submit type="button" action="%{#actionName}" cssClass="btn btn-info">
							<span class="icon fa fa-plus"></span>
							<s:text name="jpuserreg.label.addAuthorization"></s:text>
						</wpsf:submit>
					</span>
				</div>
				&#32;
			</div>
			
			<p><s:text name="jpuserreg.note.authorizationsList" /></p>
			
			<s:set var="userCvsAuthorizationsVar" value="%{config.defaultCsvAuthorizations}" />
			<s:if test="%{#userCvsAuthorizationsVar.size()>0}">
				<div class="table-responsive">
					<table class="table table-striped table-hover table-condensed">
						<tr>
							<th class="text-center text-nowrap"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
							<th><s:text name="jpuserreg.label.userGroup" /></th>
							<th><s:text name="jpuserreg.label.userRole" /></th>
						</tr>
						<s:iterator value="#userCvsAuthorizationsVar" var="userCvsAuthorizationVar" status="elementStatus">
							<s:set var="userCvsAuthorizationParamsVar" value="#userCvsAuthorizationVar.split('','')" />
							<tr>
								<td class="text-center text-nowrap">
									<s:set name="elementIndexVar" value="#elementStatus.index" />
									<div class="btn-group btn-group-xs">
										<wpsa:actionParam action="removeAuthorization" var="actionName" >
											<wpsa:actionSubParam name="csvAuthorization" value="%{#userCvsAuthorizationVar}" />
										</wpsa:actionParam>
										<wpsf:submit action="%{#actionName}" type="button" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
											<span class="icon fa fa-times-circle"></span>
										</wpsf:submit>
									</div>
								</td>
								<td>
									<s:set var="groupVar" value="%{getGroup(#userCvsAuthorizationParamsVar[0])}" />
									<s:if test="null != #groupVar">
										<s:property value="#groupVar.description" />&#32;<code><s:property value="#groupVar.name" /></code>
									</s:if>
									<s:else><code>&ndash;</code></s:else>
								</td>
								<td>
									<s:set var="roleVar" value="%{getRole(#userCvsAuthorizationParamsVar[1])}" />
									<s:if test="null != #roleVar">
										<s:property value="#roleVar.description" />&#32;<code><s:property value="#roleVar.name" /></code>
									</s:if>
									<s:else><code>&ndash;</code></s:else>
								</td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</s:if>
			<s:else>
				<p><s:text name="jpuserreg.note.userAuthorizations.empty" /></p>
			</s:else>
		</fieldset>
		
		<fieldset class="col-xs-12">
			<legend><s:text name="jpuserreg.activation" /></legend>

			<p>
				<s:property value="jpuserreg.parameters.summary" />
			</p>

			<table class="table table-bordered">
				<tr>
                    <th class="text-right"><s:text name="jpuserreg.activationPageCode" /></th>
					<td><s:property value="%{getText('jpuserreg.activationPageCode.help')}" escape="false" /></td>
				</tr>
				<tr>
					<th class="text-right"><s:text name="jpuserreg.label.subject" /></th>
					<td><s:property value='%{getText("jpuserreg.label.activation.subject.help")}' escape="false" /></td>
				</tr>
				<tr>
					<th class="text-right"><s:text name="jpuserreg.label.body" /></th>
					<td><s:property value='%{getText("jpuserreg.label.activation.body.help")}' escape="false"  /></td> 
				</tr>
			</table>
			<%--
			<div class="form-group">
				<label for="activationPageCode"><s:text name="jpuserreg.activationPageCode" /></label>
				<wpsf:select  name="config.activationPageCode" id="activationPageCode" list="%{pages}" listKey="code" listValue="titles[currentLang.code]" cssClass="form-control" />
			</div>
			--%>
			<div class="form-group">
				<label for="activationPageCode"><s:text name="jpuserreg.activationPageCode" /></label>
				<wpsf:select name="config.activationPageCode" id="activationPageCode" 
							 list="%{activationPages}" listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" cssClass="form-control" />
			</div>
			
			<s:iterator var="lang" value="langs" > 
				<s:set var="template" value="%{config.activationTemplates.get(#lang.code)}" />
				<div class="form-group">
					<label class="control-label" for="config_activationTemplates_subject_<s:property value="#lang.code"/>">
						<code class="label label-info">(<s:property value="#lang.code"/>)</code>
						&#32;<s:text name="jpuserreg.label.subject" />
					</label>
					<wpsf:textfield value="%{#template.subject}" name="config.activationTemplates['%{#lang.code}'].subject" 
									id="config_activationTemplates_subject_%{#lang.code}" cssClass="form-control"/>
				</div>
				<div class="form-group">
					<label class="control-label" for="config_activationTemplates_body_<s:property value="#lang.code"/>">
						<code class="label label-info">(<s:property value="#lang.code"/>)</code>
						&#32;<s:text name="jpuserreg.label.body" /></label>
						<wpsf:textarea value="%{#template.body}" name="config.activationTemplates['%{#lang.code}'].body" id="config_activationTemplates_body_%{#lang.code}" cssClass="form-control" rows="8" cols="50" />
				</div>
			</s:iterator>
		</fieldset>

		<fieldset class="col-xs-12">
			<legend><s:text name="jpuserreg.reactivation" /></legend>

			<p>
				<s:property value="jpuserreg.parameters.summary" />
			</p>

			<table class="table table-bordered">
				<tr>
					<th class="text-right"><s:text name="jpuserreg.reactivationPageCode" /></th>
					<td><s:property value="%{getText('jpuserreg.reactivationPageCode.help')}" escape="false" /></td>
				</tr>
				<tr>
					<th class="text-right"><s:text name="jpuserreg.label.subject" /></th>
					<td><s:property value='%{getText("jpuserreg.label.reactivation.subject.help")}' escape="false" /></td>
				</tr>
				<tr>
					<th class="text-right"><s:text name="jpuserreg.label.body" /></th>
					<td><s:property value='%{getText("jpuserreg.label.reactivation.body.help")}' escape="false"  /></td> 
				</tr>
			</table>
			<%--
			<div class="form-group">
				<label for="reactivationPageCode"><s:text name="jpuserreg.reactivationPageCode" /></label>
				<wpsf:select  name="config.reactivationPageCode" id="reactivationPageCode" list="%{pages}" listKey="code" listValue="titles[currentLang.code]" cssClass="form-control" />
			</div>
			--%>
			<div class="form-group">
				<label for="reactivationPageCode"><s:text name="jpuserreg.reactivationPageCode" /></label>
				<wpsf:select name="config.reactivationPageCode" id="reactivationPageCode" 
							 list="%{reactivationPages}" listKey="code" listValue="%{getShortFullTitle(currentLang.code)}" cssClass="form-control" />
			</div>
			
			<s:iterator var="lang" value="langs">
				<s:set var="template" value="%{config.reactivationTemplates.get(#lang.code)}" />
				<div class="form-group">
					<label class="control-label" for="config_reactivationTemplates_subject_<s:property value="#lang.code"/>">
						<code class="label label-info">(<s:property value="#lang.code"/>)</code>
						&#32;<s:text name="jpuserreg.label.subject" /></label>
						<wpsf:textfield value="%{#template.subject}" name="config.reactivationTemplates['%{#lang.code}'].subject" 
										id="config_reactivationTemplates_subject_%{#lang.code}" cssClass="form-control"/>
				</div>
				<div class="form-group">
					<label class="control-label" for="config_reactivationTemplates_body_<s:property value="#lang.code"/>">
						<code class="label label-info">(<s:property value="#lang.code"/>)</code>
						&#32;<s:text name="jpuserreg.label.body" /></label>
						<wpsf:textarea value="%{#template.body}" name="config.reactivationTemplates['%{#lang.code}'].body" 
									   id="config_reactivationTemplates_body_%{#lang.code}" cssClass="form-control" rows="8" cols="50" />
				</div>
			</s:iterator>
		</fieldset>

		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
						<span class="icon fa fa-floppy-o"></span>&#32;
						<s:text name="%{getText('label.save')}"/>
					</wpsf:submit>
				</div>
			</div>
		</div>

	</s:form>

</div>