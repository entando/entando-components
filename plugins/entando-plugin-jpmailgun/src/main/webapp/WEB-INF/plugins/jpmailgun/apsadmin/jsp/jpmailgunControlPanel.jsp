<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="jpmailgun.name" /></span>
</h1>
<div id="main">

	<s:if test="%{_messageErrorDB!=null}">
		<div class="alert alert-danger  alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<s:property value="_messageErrorDB"/>
		</div>
	</s:if>

	<s:form name="Configuration" namespace="/do/Mailgun" method="post">
		<div class="form-group">
			<label for="jpmailgun-apikey">APIKEY</label>
			<wpsf:textfield name="_updateAPIKey" value="%{getAPI()}" id="jpmailgun-apikey" cssClass="form-control" />
		</div>

		<div class="form-group">
			<label for="jpmailgun-domain">Dominio</label>
			<wpsf:textfield name="_updateDomain" value="%{getDomain()}" id="jpmailgun-domain" cssClass="form-control" />
		</div>

		<div class="form-group">
			<wpsf:submit type="button" cssClass="btn btn-primary" action="saveConfig">
				<span class="icon fa fa-save"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>

		<fieldset class="margin-large-top col-xs-12">
			<legend class="margin-large-top cursor-pointer">
				<a href="#jpmailgun-test" data-toggle="collapse" data-target="#jpmailgun-test">
					Test&#32;
					<span class="icon fa fa-chevron-down pull-right2"></span>
				</a>
			</legend>
			<s:if test="%{_messageError!=null}">
				<div class="alert alert-danger alert-dismissable fade in">
					<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
					<s:property value="_messageError"/>
				</div>
			</s:if>
			<s:if test="%{_messageSuccess!=null}">
				<div class="alert alert-success alert-dismissable fade in">
					<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
					<s:property value="_messageSuccess"/>
			 </div>
			</s:if>

			<div id="jpmailgun-test" class="collapse">
				<div class="form-group">
					<label for="jpmailgun-test-sender">Sender (From)</label>
					<input type="text" name="_emailFrom" id="jpmailgun-test-sender" placeholder="mailgun-test@entando.com" class="form-control">
				</div>

				<div class="form-group">
					<label for="jpmailgun-test-recipient">Recipient (To)</label>
					<input type="text" name="_emailTo" id="jpmailgun-test-recipient" placeholder="youremail@domain.ddd" class="form-control">
				</div>


				<div class="form-horizontal">
	        <div class="form-group">
	          <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
							<wpsf:submit type="button" cssClass="btn btn-default btn-block" action="sendTestEmail">
								<span class="icon fa fa-share"></span>&#32;
								<s:text name="label.button.sendTestEmail" />
							</wpsf:submit>
						</div>
					</div>
				</div>
			</div>
		</fieldset>
	</s:form>
</div>
