<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.jpwebMail" /></span>
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
			<legend><s:text name="title.jpwebMail.config" /></legend>

			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.domainName']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.domainName"><s:text name="label.domainName" /></label>
				<wpsf:textfield name="config.domainName" id="config.domainName" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />





			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.localhost']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.localhost"><s:text name="label.localhost" /></label>
				<wpsf:textfield name="config.localhost" id="config.localhost" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />

			<div class="form-group">
				<label class="checkbox">
					<wpsf:checkbox name="config.useEntandoUserPassword" id="config.useEntandoUserPassword" cssClass="radiocheck"/>
					&#32;<s:text name="label.useEntandoUserPassword" />
				</label>
			</div>

		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><s:text name="label.certificates" /></legend>

			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.certificatePath']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.certificatePath"><s:text name="label.certificatePath" /></label>
				<wpsf:textfield name="config.certificatePath" id="config.certificatePath" cssClass="form-control"/>
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />


			<div class="form-group">
				<label class="checkbox">
					<wpsf:checkbox name="config.certificateEnable" id="config.certificateEnable" cssClass="radiocheck"/>
					&#32;<s:text name="label.certificateEnable" />
				</label>
				<label class="checkbox">
					<wpsf:checkbox name="config.certificateLazyCheck" id="config.certificateLazyCheck" cssClass="radiocheck"/>
					&#32;<s:text name="label.certificateLazyCheck" />
				</label>
				<label class="checkbox">
					<wpsf:checkbox name="config.certificateDebugOnConsole" id="config.certificateDebugOnConsole" cssClass="radiocheck"/>
					&#32;<s:text name="label.certificateDebugOnConsole" />
				</label>
			</div>
		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><abbr title="Internet Message Access Protocol"><s:text name="label.imap" /></abbr></legend>
			<div class="row">

					<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.imapHost']}" />
					<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="''" />
					<s:if test="#currentFieldHasFieldErrorVar">
						<s:set var="controlGroupErrorClassVar" value="' has-error'" />
					</s:if>
					<div class="form-group<s:property value="#controlGroupErrorClassVar" /> col-md-4 col-lg-4">
						<label for="config.imapHost"><s:text name="label.imapHost" /></label>
						<wpsf:textfield name="config.imapHost" id="config.imapHost" cssClass="form-control"/>
						<s:if test="#currentFieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
						</s:if>
					</div>
					<s:set var="currentFieldErrorsVar" value="%{null}" />
					<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
					<s:set var="controlGroupErrorClassVar" value="%{null}" />

					<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.imapPort']}" />
					<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="''" />
					<s:if test="#currentFieldHasFieldErrorVar">
						<s:set var="controlGroupErrorClassVar" value="' has-error'" />
					</s:if>
					<div class="form-group<s:property value="#controlGroupErrorClassVar" /> col-md-4 col-lg-4">
						<label for="config.imapPort"><s:text name="label.imapPort" /></label>
						<wpsf:textfield placeholder="usualy 143, or IMAPS 993" name="config.imapPort" id="config.imapPort" cssClass="form-control"/>
						<s:if test="#currentFieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
						</s:if>
					</div>
					<s:set var="currentFieldErrorsVar" value="%{null}" />
					<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
					<s:set var="controlGroupErrorClassVar" value="%{null}" />


					<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.imapProtocol']}" />
					<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
					<s:set var="controlGroupErrorClassVar" value="''" />
					<s:if test="#currentFieldHasFieldErrorVar">
						<s:set var="controlGroupErrorClassVar" value="' has-error'" />
					</s:if>
					<div class="form-group<s:property value="#controlGroupErrorClassVar" /> col-md-4 col-lg-4">
						<label for="config.imapProtocol"><s:text name="label.imapProtocol" /></label>
						<wpsf:textfield name="config.imapProtocol" id="config.imapProtocol" cssClass="form-control" />
						<s:if test="#currentFieldHasFieldErrorVar">
							<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
						</s:if>
					</div>
					<s:set var="currentFieldErrorsVar" value="%{null}" />
					<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
					<s:set var="controlGroupErrorClassVar" value="%{null}" />

			</div>
		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><abbr title="Simple Mail Transfer Protocol"><s:text name="label.smtp" /></abbr></legend>
			<div class="row">

				<s:set var="smptHostErrorsVar" value="%{fieldErrors['config.smtpHost']}" />
				<s:set var="smptHostHasErrorVar" value="#smptHostErrorsVar != null && !#smptHostErrorsVar.isEmpty()" />
				<s:set var="smtpHostcontrolGroupErrorClassVar" value="''" />
				<s:if test="#smptHostHasErrorVar">
					<s:set var="smtpHostcontrolGroupErrorClassVar" value="' has-error'" />
				</s:if>
				<s:set var="smtpPortErrorsVar" value="%{fieldErrors['config.smtpPort']}" />
				<s:set var="smtpPortHasFieldErrorVar" value="#smtpPortErrorsVar != null && !#smtpPortErrorsVar.isEmpty()" />
				<s:set var="smtpPortcontrolGroupErrorClassVar" value="''" />
				<s:if test="#smtpPortHasFieldErrorVar">
					<s:set var="smtpPortcontrolGroupErrorClassVar" value="' has-error'" />
				</s:if>

				<div class="form-group<s:property value="#smtpHostcontrolGroupErrorClassVar" /> col-md-6 col-lg-6">
					<label for="config.smtpHost"><s:text name="label.smtpHost" /></label>
					<wpsf:textfield name="config.smtpHost" id="config.smtpHost" cssClass="form-control" />
					<s:if test="#smptHostHasErrorVar||#smtpPortHasFieldErrorVar">
						<p class="text-danger padding-small-vertical">&nbsp;<s:iterator value="#smptHostErrorsVar"><s:property />&emsp;</s:iterator></p>
					</s:if>
				</div>

				<div class="form-group<s:property value="#smtpPortcontrolGroupErrorClassVar" /> col-md-6 col-lg-6">
					<label for="config.smtpPort"><s:text name="label.smtpPort" /></label>
					<wpsf:textfield placeholder="usually 25, for smtps 465" name="config.smtpPort" id="config.smtpPort" cssClass="form-control" />
					<s:if test="#smptHostHasErrorVar||#smtpPortHasFieldErrorVar">
						<p class="text-danger padding-small-vertical">&nbsp;<s:iterator value="#smtpPortErrorsVar"><s:property />&emsp;</s:iterator></p>
					</s:if>
				</div>
			</div>
			
			<div class="form-group">
				<label for="smtpAuth"><s:text name="label.smtp.auth" /></label>
				<div class="radio">
					<s:text name="label.smtp.auth.anonymous"/>
					<wpsf:radio id="smtpAuthAnonymous" name="config.smtpAuth" value="0" checked="%{config.smtpAuth == 0}" cssClass="radio" />
				</div>
				<div class="radio">
					<s:text name="label.smtp.auth.entando"/>
					<wpsf:radio id="smtpAuthEntando" name="config.smtpAuth" value="1" checked="%{config.smtpAuth == 1}" cssClass="radio" />
				</div>
				<div class="radio">
					<s:text name="label.smtp.auth.entandoWithDomain"/>
					<wpsf:radio id="smtpAuthEntandoWithDomain" name="config.smtpAuth" value="2" checked="%{config.smtpAuth == 2}" cssClass="radio" />    
				</div>
				<div class="radio">
					<s:text name="label.smtp.auth.custom"/>
					<wpsf:radio id="smtpAuthCustom" name="config.smtpAuth" value="3" checked="%{config.smtpAuth == 3}" cssClass="radio" />    
				</div>
			</div>
			
			<div class="row">

				<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.smtpUserName']}" />
				<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="''" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<s:set var="controlGroupErrorClassVar" value="' has-error'" />
				</s:if>
				<div class="form-group<s:property value="#controlGroupErrorClassVar" /> col-md-6 col-lg-6">
					<label for="config.smtpUserName"><s:text name="label.smtpUserName" /></label>
					<wpsf:textfield name="config.smtpUserName" id="config.smtpUserName" cssClass="form-control"/>
					<s:if test="#currentFieldHasFieldErrorVar">
						<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
					</s:if>
				</div>
				<s:set var="currentFieldErrorsVar" value="%{null}" />
				<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
				<s:set var="controlGroupErrorClassVar" value="%{null}" />

				<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.smtpPassword']}" />
				<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
				<s:set var="controlGroupErrorClassVar" value="''" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<s:set var="controlGroupErrorClassVar" value="' has-error'" />
				</s:if>
				<div class="form-group<s:property value="#controlGroupErrorClassVar" /> col-md-6 col-lg-6">
					<label for="config.smtpPassword"><s:text name="label.smtpPassword" /></label>
					<wpsf:password name="config.smtpPassword" id="config.smtpPassword" cssClass="form-control" />
					<s:if test="#currentFieldHasFieldErrorVar">
						<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
					</s:if>
				</div>
				<s:set var="currentFieldErrorsVar" value="%{null}" />
				<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
				<s:set var="controlGroupErrorClassVar" value="%{null}" />
				
			</div>
			
			
			<div class="form-group">
				<label for="smtpPort"><s:text name="label.smtp.security" /></label>

				<div class="radio">
					<s:text name="label.smtp.standard"/>
					<wpsf:radio id="smtpstd" name="config.smtpProtocol" value="0" checked="%{config.smtpProtocol == 0}" cssClass="radio" />
				</div>
				<div class="radio">
					<s:text name="label.smtp.ssl"/>
					<wpsf:radio id="smtpssl" name="config.smtpProtocol" value="1" checked="%{config.smtpProtocol == 1}" cssClass="radio" />
				</div>    
				<div class="radio">
					<s:text name="label.smtp.tls"/>
					<wpsf:radio id="smtptls" name="config.smtpProtocol" value="2" checked="%{config.smtpProtocol == 2}" cssClass="radio" />    
				</div>
				<%--
				<div class="form-group">
					<label for="config.smtpTimeout"><s:text name="label.smtpTimeout" /></label>
					<wpsf:textfield name="config.smtpTimeout" id="config.smtpTimeout" cssClass="form-control" />
				</div>
				--%>
			</div>
			
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label class="checkbox">
					<wpsf:checkbox name="config.debug" id="config.debug" cssClass="radiocheck"/>
					&#32;<s:text name="label.debug" />
				</label>
				<%--
				<label class="checkbox">
					<wpsf:checkbox name="config.smtpEntandoUserAuth" id="config.smtpEntandoUserAuth" cssClass="radiocheck" />
					&#32;<s:text name="label.smtpEntandoUserAuth" />
				</label>
				--%>
			</div>
		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><s:text name="label.folders" /></legend>

			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.trashFolderName']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.trashFolderName"><s:text name="label.trashFolderName" /></label>
				<wpsf:textfield name="config.trashFolderName" id="config.trashFolderName" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />

			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.sentFolderName']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.sentFolderName"><s:text name="label.sentFolderName" /></label>
				<wpsf:textfield name="config.sentFolderName" id="config.sentFolderName" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />

		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><s:text name="label.tempDiskRootFolder" /></legend>

			<s:set var="currentFieldErrorsVar" value="%{fieldErrors['config.tempDiskRootFolder']}" />
			<s:set var="currentFieldHasFieldErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			<s:set var="controlGroupErrorClassVar" value="''" />
			<s:if test="#currentFieldHasFieldErrorVar">
				<s:set var="controlGroupErrorClassVar" value="' has-error'" />
			</s:if>
			<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
				<label for="config.tempDiskRootFolder"><s:text name="label.tempDiskRootFolder" /></label>
				<wpsf:textfield placeholder="/secret/temporary/folder/" name="config.tempDiskRootFolder" id="config.tempDiskRootFolder" cssClass="form-control" />
				<s:if test="#currentFieldHasFieldErrorVar">
					<p class="text-danger padding-small-vertical"><s:iterator value="#currentFieldErrorsVar"><s:property />&emsp;</s:iterator></p>
				</s:if>
			</div>
			<s:set var="currentFieldErrorsVar" value="%{null}" />
			<s:set var="currentFieldHasFieldErrorVar" value="%{null}" />
			<s:set var="controlGroupErrorClassVar" value="%{null}" />

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
