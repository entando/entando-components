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
				<s:iterator var="auth" value="config.groups">
					<wpsf:hidden name="config.groups" value="%{#auth}" />
				</s:iterator>
				<s:iterator var="auth" value="config.roles">
					<wpsf:hidden name="config.roles" value="%{#auth}" />
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
			<legend><s:text name="jpuserreg.groups" /></legend>

			<div class="form-group">
				<label for="groupName"><s:text name="jpuserreg.label.group" /></label>
				<div class="input-group">
					<wpsf:select name="groupName" id="groupName" list="systemGroups" listKey="name" listValue="descr" cssClass="form-control"/>
					<span class="input-group-btn">
						<wpsf:submit action="addGroup" type="button" cssClass="btn btn-default" value="%{getText('label.add')}" title="%{getText('label.add')}" /> 
					</span>                              
				</div>
			</div>
            <p><s:text name="jpuserreg.note.groupList" /></p>
            <s:if test="%{config.groups.size() > 0}">
                <s:iterator var="auth" value="config.groups">

                    <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                        <span class="icon fa fa-tag"></span>&#32;
                        <abbr><s:property value="#auth" /></abbr>&#32;
                        <wpsa:actionParam action="removeGroup" var="removeGroupActionName" >
                            <wpsa:actionSubParam name="groupName" value="%{#auth}" />
                        </wpsa:actionParam>
                        <wpsf:submit
                            type="button"
                            action="%{#removeGroupActionName}"
                            title="%{getText('label.remove')}"
                            cssClass="btn btn-default btn-xs badge">
                            <span class="icon fa fa-times"></span>
                            <span class="sr-only">x</span>
                        </wpsf:submit>
                    </span>
                </s:iterator>
            </s:if>

		</fieldset>

		<fieldset class="col-xs-12">
			<legend><s:text name="jpuserreg.roles" /></legend>
			<div class="form-group">
				<label for="roleName"><s:text name="jpuserreg.label.role" /></label>
				<div class="input-group">
					<wpsf:select name="roleName" id="roleName" list="systemRoles" listKey="name" listValue="description" cssClass="form-control" />
					<span class="input-group-btn">
						<wpsf:submit action="addRole" type="button" cssClass="btn btn-default" value="%{getText('label.add')}" title="%{getText('label.add')}" />

					</span>
				</div>
			</div>
			<p><s:text name="jpuserreg.note.roleList" /></p>

			<s:if test="%{config.roles.size > 0}">
				<s:iterator var="auth" value="config.roles">
					<span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                        <span class="icon fa fa-tag"></span>&#32;
                        <abbr><s:property value="#auth" /></abbr>&#32;
						<wpsa:actionParam action="removeRole" var="removeRoleActionName" >
							<wpsa:actionSubParam name="roleName" value="%{#auth}" />
						</wpsa:actionParam>
						<wpsf:submit type="button" 
									 action="%{#removeRoleActionName}"
									 title="%{getText('label.remove')}"
									 cssClass="btn btn-default btn-xs badge">        
                            <span class="icon fa fa-times"></span>
                            <span class="sr-only">x</span>
						</wpsf:submit>
					</span>
				</s:iterator>
			</s:if>
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