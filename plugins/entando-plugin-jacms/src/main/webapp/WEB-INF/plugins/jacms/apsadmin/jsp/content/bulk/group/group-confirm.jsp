<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:if test="strutsAction == 1">
	<s:set var="labelTitle" value="%{getText('title.bulk.content.group.join')}"/>
</s:if>
<s:elseif test="strutsAction == 4" >
	<s:set var="labelTitle" value="%{getText('title.bulk.content.group.rem')}"/>
</s:elseif>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><a href="<s:url action="list" namespace="/do/jacms/Content"/>"><s:text name="jacms.menu.contentAdmin" /></a></li>
    <li>
		<s:property value="%{#labelTitle}" />
    </li>
</ol>

<h1 class="page-title-container"><s:property value="%{#labelTitle}" />&#32;-&#32;<s:text name="title.bulk.confirm" /></h1>

<div id="main" role="main">
	<s:if test="hasErrors()">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">
				<span class="pficon pficon-close"></span>
			</button>
			<span class="pficon pficon-error-circle-o"></span>
			<s:text name="message.title.ActionErrors" />
			<ul>
			<s:if test="hasActionErrors()">
				<s:iterator value="actionErrors">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:if>
			<s:if test="hasFieldErrors()">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
				</s:iterator>
			</s:iterator>
			</s:if>
			</ul>
		</div>
	</s:if>
	<s:form action="apply" namespace="/do/jacms/Content/Group" >
        <s:hidden name="_csrf" value="%{csrfToken}"/>
		<p class="sr-only">
			<wpsf:hidden name="strutsAction"/>
		<s:iterator var="contentId" value="selectedIds" >
			<wpsf:hidden name="selectedIds" value="%{#contentId}" />
		</s:iterator>
		<s:iterator var="groupCode" value="extraGroupNames" >
			<wpsf:hidden name="extraGroupNames" value="%{#groupCode}" />
		</s:iterator>
		</p>
		<div>
			<p>
			<s:if test="strutsAction == 1">
				<s:text name="note.bulk.content.group.join.doYouConfirm" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</s:if>
			<s:elseif test="strutsAction == 4">
				<s:text name="note.bulk.content.group.rem.doYouConfirm" ><s:param name="items" value="%{selectedIds.size()}" /></s:text>
			</s:elseif>
			</p>
		</div>
		<div>
			<ul>
			<s:iterator var="groupCode" value="extraGroupNames" >
				<s:set var="group" value="%{getGroup(#groupCode)}" />
				<li>
					<s:property value="#group.description" />
				</li>
			</s:iterator>
			</ul>
		</div>
		<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/bulk/inc/inc_contentSummary.jsp" />
		
		<div class="col-xs-12">
			<s:if test="strutsAction == 1">
				<s:set var="labelAction" value="%{getText('label.bulk.content.group.join.confirm')}"/>
			</s:if>
			<s:elseif test="strutsAction == 4" >
				<s:set var="labelAction" value="%{getText('label.bulk.content.group.rem.confirm')}"/>
			</s:elseif>
			<wpsf:submit type="button" title="%{#labelAction}" cssClass="btn btn-success">
				<span class="icon fa fa-times-circle"></span>
				<s:property value="%{#labelAction}" />
			</wpsf:submit>
		</div>
	</s:form>
</div>