<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="title.workflowManagement" />
		&#32;/&#32;
		<s:text name="title.workflowNotifierManagement.config" />
	</span>
</h1>
<div id="main">
	<s:form action="save" cssClass="">
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

		<fieldset class="col-xs-12">
			<legend><s:text name="notifier.generalSettings" /></legend>
				<div class="form-group">
					<div class="checkbox">
						<label for="active">
							<wpsf:checkbox name="config.active" id="active" />&nbsp;
							<s:text name="label.active" />
						</label>
					</div>
				</div>
		</fieldset>

		<fieldset class="col-xs-12">
			<legend><s:text name="label.schedulerSettings" /></legend>
			<div class="form-group">
				<label for="hoursDelay"><s:text name="label.hoursDelay" /></label>
				<s:set name="hoursDelayVar" value="%{hoursDelay}" scope="page" />
				<select name="config.hoursDelay" id="hoursDelay" class="form-control">
					<c:forEach begin="1" end="10" varStatus="status">
						<option <c:if test="${(status.count*24) == hoursDelayVar}">selected="selected"</c:if>
							value="<c:out value="${status.count*24}" />" ><c:out value="${status.count*24}" /></option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label for="jpcontentworkflownotifier_date_cal"><s:text name="label.startDate" /></label>
				<wpsf:textfield name="startDate" id="jpcontentworkflownotifier_date_cal" cssClass="form-control datepicker" />
				<span clasS="help-block">
					<s:text name="label.startDatePattern" />
				</span>
			</div>
			<div class="form-group row">
				<div class="col-xs-12">
					<label><s:text name="notifier.time"/></label>
				</div>
				<div class="col-xs-3">
					<label for="hour" class="sr-only">Hour</label>
					<wpsf:select list="%{getCounterArray(0, 24)}" name="hour" id="hour" cssClass="form-control" />
				</div>
				<div class="col-xs-1 text-center" style="max-width: 0.5em; width: 0.5em;">:</div>
				<div class="col-xs-3">
					<label for="minute" class="sr-only">Minute</label>
					<wpsf:select list="%{getCounterArray(0, 60)}" name="minute" id="minute" cssClass="form-control" />
				</div>
			</div>
		</fieldset>

		<fieldset class="col-xs-12 margin-large-top">
			<legend><s:text name="label.mailSettings" /></legend>
			<div class="form-group">
				<label for="senderCode"><s:text name="label.senderCode"/></label>
				<wpsf:select name="config.senderCode" id="senderCode" list="senderCodes" cssClass="form-control" />
			</div>
			<div class="form-group">
				<div class="form-group">
					<div class="checkbox">
						<label for="html">
							<wpsf:checkbox name="config.html" id="html" />&nbsp;
							<s:text name="label.html" />
						</label>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="subject"><s:text name="label.subject" /></label>
				<wpsf:textfield name="config.subject" id="subject" cssClass="form-control"/>
			</div>
			<div class="form-group">
				<label for="jpcontentworkflow_header"><s:text name="label.header" /></label>
				<wpsf:textarea name="config.header" id="jpcontentworkflow_header" cols="50" rows="10" cssClass="form-control"/>
			</div>
			<div class="form-group">
				<label for="jpcontentworkflow_template"><s:text name="label.template" /></label>
				<wpsf:textarea name="config.template" id="jpcontentworkflow_template" cols="50" rows="10" cssClass="form-control"/>
			</div>
			<div class="form-group">
				<label for="jpcontentworkflow_footer"><s:text name="label.footer" /></label>
				<wpsf:textarea name="config.footer" id="jpcontentworkflow_footer" cols="50" rows="10" cssClass="form-control"/>
			</div>
		</fieldset>
		<div class="form-horizontal">
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
						<s:text name="label.save" />
					</wpsf:submit>
				</div>
			</div>
		</div>
	</s:form>
</div>
