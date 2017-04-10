<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpcontentscheduler.title.management" /></h1>

<div id="main">

<h2>[<s:property value="item" />]</h2>

<s:form action="saveItem">
	
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
	
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escapeHtml="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<s:if test="hasActionMessages()">
		<div class="alert alert-success alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

<p>
	<s:hidden name="item" />
</p>
	<p>
		<s:text name="jpcontentscheduler.label.config" />:<br />
		<s:textarea name="config" cols="75" rows="25" />
	</p>
	<p>
		<s:submit value="%{getText('jpcontentscheduler.label.save')}" />
	</p>
</s:form>

<h2><s:text name="jpcontentscheduler.saveItem.instructions" /></h2>

</div>