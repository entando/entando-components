<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpcrowdsourcing.admin.title" /></li>
	<li><a
		href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
				name="jpcrowdsourcing.title.ideaInstanceManagement" /></a></li>
	<s:if test="strutsAction ==  1">
		<li class="page-title-container"><s:text
				name="jpcrowdsourcing.title.ideaInstance.add" /></li>
	</s:if>
	<s:if test="strutsAction ==  2">
		<li class="page-title-container"><s:text
				name="jpcrowdsourcing.title.ideaInstance.edit" /></li>
	</s:if>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-6">
			<h1>
				<s:if test="strutsAction ==  1">
					<s:text name="jpcrowdsourcing.title.ideaInstance.add" />
				</s:if>
				<s:if test="strutsAction ==  2">
					<s:text name="jpcrowdsourcing.title.ideaInstance.edit" />
				</s:if>
				<span class="pull-right"> <a tabindex="0" role="button"
					data-toggle="popover" data-trigger="focus" data-html="true"
					title="" data-content="TO be inserted" data-placement="left"
					data-original-title=""> <i class="fa fa-question-circle-o"
						aria-hidden="true"></i>
				</a>
				</span>
			</h1>
		</div>
		<wp:ifauthorized permission="superuser">
			<div class="col-sm-6">
				<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
					<li class="active"><a
						href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
								name="jpcrowdsourcing.ideaInstance.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea" />"><s:text
								name="jpcrowdsourcing.idea.list" /></a></li>
					<li><a
						href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />"><s:text
								name="jpcrowdsourcing.comment.list" /></a></li>
					<li><a
						href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />"><s:text
								name="jpcrowdsourcing.config" /></a></li>
				</ul>
			</div>
		</wp:ifauthorized>
	</div>
</div>
<br>
<div id="messages">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
	<s:form action="save" cssClass="form-horizontal">
		<p class="noscreen">
			<wpsf:hidden name="strutsAction" />
		</p>

		<div class="form-group">
			<label class="col-sm-2 control-label" for="ideaInstance_code"><s:text
					name="label.code" /></label>
			<s:if test="strutsAction == 1"></s:if>
			<div class="col-sm-10">
				<wpsf:textfield name="code" id="ideaInstance_code"
					cssClass="form-control" readonly="strutsAction != 1" />
			</div>
		</div>
		<s:if test="strutsAction == 2">
			<div class="form-group">
				<label class="col-sm-2 control-label" for="createdat"><s:text
						name="label.createdat" />:</label>&#32;
				<div class="col-sm-10">
					<code>
						<s:date name="createdat" format="dd/MM/yyyy" />
					</code>
					<wpsf:hidden name="createdat" />
				</div>
			</div>
		</s:if>

		<div class="form-group">
			<label class="col-sm-2 control-label" for="groupName"><s:text
					name="label.join" />&#32;<s:text name="label.group" /></label>
			<div class="col-sm-10">
				<div class="input-group">
					<wpsf:select name="groupName" id="groupName" list="systemGroups"
						listKey="name" listValue="descr" cssClass="form-control" />
					<span class="input-group-btn"> <wpsf:submit
							action="joinGroup" type="button" cssClass="btn btn-default">
							<span class="icon fa fa-plus"></span>
							&#32;
							<s:text name="%{getText('label.join')}" />
						</wpsf:submit>
					</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-2"></div>
			<div class="col-sm-10">
				<s:if test="null == groups || groups.size==0">
					<s:text name="jpcrowdsourcing.ideaInstance.label.noGroups" />
				</s:if>
				<s:else>
					<s:iterator value="groups" var="group_name">
						<wpsf:hidden name="groups" value="%{#group_name}" />
						<wpsa:actionParam action="removeGroup" var="actionName">
							<wpsa:actionSubParam name="groupName" value="%{#group_name}" />
						</wpsa:actionParam>
						<div class="label label-default label-tag label-sm">
							<s:property value="%{getSystemGroup(#group_name).getDescr()}" />
							&#32;
							<wpsf:submit type="button" cssClass="btn btn-tag"
								action="%{#actionName}" value="%{getText('label.remove')}"
								title="%{getText('label.remove')}">
								<span class="icon fa fa-times"></span>
								<span class="sr-only">x</span>
							</wpsf:submit>
						</div>
					</s:iterator>
				</s:else>
			</div>
		</div>
		<div class="col-md-12">
			<div class="form-group pull-right">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<s:text name="%{getText('label.save')}" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
