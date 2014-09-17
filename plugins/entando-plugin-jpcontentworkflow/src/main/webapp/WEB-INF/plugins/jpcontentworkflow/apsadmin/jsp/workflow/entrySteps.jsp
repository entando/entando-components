<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="list" />"><s:text name="title.workflowManagement" /></a>
		&#32;/&#32;
		<s:text name="title.workflowManagement.editSteps" />
	</span>
</h1>
<div id="main">
	<s:form action="saveSteps" cssClass="form-horizontal">
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
		<p class="text-info">
			<s:text name="note.workingOn" />:&#32;
			<s:text name="label.contentType" />&#32;
			<em><s:property value="contentType.descr"/></em>
		</p>
		<p class="sr-only hide">
			<wpsf:hidden name="typeCode" />
			<wpsf:hidden name="stepCodes" />
		</p>
		<s:if test="%{steps==null || steps.size()==0}">
			<p class="sr-only"><s:text name="note.stepList.empty" /></p>
		</s:if>
		<s:else>
			<div class="table-responsive">
				<table class="table table-bordered">
					<caption class="sr-only">
						<s:text name="note.stepList.caption" />
					</caption>
					<tr>
						<th class="text-center text-nowrap"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
						<th><s:text name="label.code" /></th>
						<th><s:text name="label.descr" /></th>
						<th><s:text name="label.role" /></th>
					</tr>
					<s:iterator value="steps" var="step" status="rowstatus" >
						<tr>
							<td class="text-center text-nowrap">
								<div class="btn-group btn-group-xs">
									<wpsa:actionParam action="moveStep" var="actionName" >
										<wpsa:actionSubParam name="movement" value="UP" />
										<wpsa:actionSubParam name="elementIndex" value="%{#rowstatus.index}" />
									</wpsa:actionParam>
									<wpsf:submit
											action="%{#actionName}"
											cssClass="btn btn-default %{(#rowstatus.first) ? ' disabled ' : '' }"
											type="button"
											title="%{getText('label.moveUp')+': '+#step.code}"
											>
											<span class="icon fa fa-sort-desc"></span>
											<span class="sr-only"><s:property value="%{getText('label.moveUp')+': '+#step.code}" /></span>
									</wpsf:submit>

									<wpsa:actionParam action="moveStep" var="actionName" >
										<wpsa:actionSubParam name="movement" value="DOWN" />
										<wpsa:actionSubParam name="elementIndex" value="%{#rowstatus.index}" />
									</wpsa:actionParam>
									<wpsf:submit
											action="%{#actionName}"
											cssClass="btn btn-default  %{(#rowstatus.last) ? ' disabled ' : '' }"
											type="button"
											title="%{getText('label.moveDown')+': '+#step.code}"
											>
												<span class="icon fa fa-sort-asc"></span>
												<span class="sr-only"><s:property value="%{getText('label.moveDown')+': '+#step.code}" /></span>
									</wpsf:submit>
								</div>
								<div class="btn-group btn-group-xs">
									<wpsa:actionParam action="removeStep" var="actionName" >
										<wpsa:actionSubParam name="stepCode" value="%{#step.code}" />
									</wpsa:actionParam>
									<wpsf:submit
											action="%{#actionName}"
											cssClass="btn btn-warning"
											type="button"
											value="%{getText('label.remove')}"
											title="%{getText('label.remove')+': '+#step.code}"
											>
											<span class="icon fa fa-times-circle-o"></span>
											<span class="sr-only"><s:property value="%{getText('label.remove')+': '+#step.code}" /></span>
									</wpsf:submit>
								</div>
							</td>
							<td><code><s:property value="#step.code"/></code></td>
							<td>
								<wpsf:textfield name="%{code + '_SEP_descr'}" value="%{#step.descr}" cssClass="form-control input-sm display-block" />
							</td>
							<td>
								<wpsf:select name="%{code + '_SEP_role'}" headerKey="" headerValue=" - " list="roles" listKey="name" listValue="description" value="%{#step.role}" cssClass="form-control input-sm display-block" />
							</td>

						</tr>
					</s:iterator>
				</table>
			</div>
		</s:else>
			<fieldset>
				<legend><s:text name="title.newStep" /></legend>
				<div class="form-group">
					<div class="col-xs-12">
						<label for="jpcontentworkflow_stepCode"><s:text name="label.code" /></label>
						<wpsf:textfield maxlength="12" name="stepCode" id="jpcontentworkflow_stepCode" cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
						<label for="jpcontentworkflow_stepDescr"><s:text name="label.descr" /></label>
						<wpsf:textfield name="stepDescr" id="jpcontentworkflow_stepDescr" cssClass="form-control" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-xs-12">
						<label for="jpcontentworkflow_stepRole"><s:text name="label.role" /></label>
						<div class="input-group">
							<wpsf:select
								id="jpcontentworkflow_stepRole"
								name="stepRole"
								headerKey=""
								headerValue=""
								list="roles"
								listKey="name"
								listValue="description" cssClass="form-control" />
								<span class="input-group-btn">
									<wpsa:actionParam action="addStep" var="actionName" />
									<wpsf:submit
										type="button"
										action="%{#actionName}"
										cssClass="btn btn-info">
											<span class="icon fa fa-plus"></span>
											<s:text name="label.addStep"></s:text>
										</wpsf:submit>
								</span>
						</div>
					</div>
					&#32;
				</div>
			</fieldset>
			<div class="form-group margin-large-top">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
						<s:text name="label.save" />
					</wpsf:submit>
				</div>
			</div>
	</s:form>
</div>
