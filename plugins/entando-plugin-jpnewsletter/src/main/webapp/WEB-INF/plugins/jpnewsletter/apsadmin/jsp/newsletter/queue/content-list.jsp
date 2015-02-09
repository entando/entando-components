<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpnewsletter.title.newsletterManagement" />&#32;/&#32;<s:text name="jpnewsletter.title.newsletterQueue" />
	</span>
</h1>

<s:if test="newsletterConfig.active">
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.intro" />
	</p>
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.intro.bis" />
	</p>
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.startInfo" >
			<s:param ><em class="important"><s:date name="newsletterConfig.nextTaskTime" format="dd/MM/yyyy" /></em></s:param>
			<s:param ><em class="important"><s:date name="newsletterConfig.nextTaskTime" format="HH:mm" /></em></s:param>
			<s:param value="hoursDelay" />
			<s:param value="minutesDelay" />
		</s:text>
	</p>
</s:if>
<s:else>
	<p>
		<s:text name="jpnewsletter.label.newsletterQueue.notActive" />
	</p>
</s:else>

<div id="main" role="main">
	
	<s:set var="contentIdsVar" value="contentIds" />
	
	<s:if test="%{#contentIdsVar.size() > 0}">
	
	<s:form action="search" cssClass="form-horizontal" role="search">
	
	<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable fade in margin-base-top">
		<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
		<ul class="margin-base-top">
		<s:iterator value="ActionErrors">
			<li><s:property escape="false" /></li>
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
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
	</s:if>
	
	<wpsa:subset source="#contentIdsVar" count="10" objectName="groupContent" advanced="true" offset="5">
	<s:set name="group" value="#groupContent" />
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
		
	<div class="table-responsive">
		<table class="table table-bordered" id="contentListTable">
		<caption class="sr-only"><s:text name="title.contentList" /></caption>
		<tr>
			<th class="text-center padding-large-left padding-large-right"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
			<th><s:text name="label.description" /></th>
			<th><s:text name="label.code" /></th>
		</tr>
		<s:iterator var="contentId">
		<s:set var="content" value="%{getContentVo(#contentId)}"></s:set>
		<tr>
		<td class="text-center text-nowrap">
			<div class="btn-group btn-group-xs">
				<a class="btn btn-warning" title="<s:text name="label.remove" />"
					href="<s:url action="removeFromQueue" ><s:param name="contentId" value="#content.id" /></s:url>">
					<span class="icon fa fa-times-circle-o"></span>
					<span class="sr-only"><s:text name="label.remove" /></span>
				</a>
			</div>
		</td>
		<td><label><s:property value="#content.descr" /></label></td>
		<td><code><s:property value="#content.id" /></code></td>
		</tr>
		</s:iterator>
		</table>
	</div>
	
	<div class="text-center">
		<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
	</div>
	
	</wpsa:subset>
		
<s:if test="newsletterConfig.active">
	<p class="margin-large-top btn-group btn-group-sm">
		<a class="btn btn-default" 
			href="<s:url action="send" />">
			<span class="icon fa fa-arrow-right"></span>&#32;
			<s:text name="jpnewsletter.label.sendNow" />
		</a>
	</p>
</s:if>
	</s:form>
	</s:if>
	<s:else>
	<p><s:text name="jpnewsletter.label.newsletterQueue.empty" /></p>
	</s:else>
</div>