<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%--<h1 class="panel panel-default title-page">--%>
	<%--<span class="panel-body display-block">--%>
		<%--<a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />">--%>
			<%--<s:text name="jprssaggregator.title.rssAggregator.rssManagement" />--%>
		<%--</a>--%>
		<%--&#32;/&#32;--%>
		<%--<s:text name="jprssaggregator.title.rssAggregator.remove" />--%>
	<%--</span>--%>
<%--</h1>--%>
<%--<s:form action="doDelete">--%>
	<%--<p class="sr-only">--%>
		<%--<wpsf:hidden name="code" />--%>
	<%--</p>--%>
	<%--<div class="alert alert-warning">--%>
		<%--<p>--%>
			<%--<s:text name="message.are.you.sure.to.delete" />&#32;--%>
			<%--<code><s:property value="%{code}" /></code>ASDFGHJKLKJHGFDSFGHJKL?--%>
		<%--</p>--%>
		<%--<div class="text-center margin-large-top">--%>
			<%--<wpsf:submit type="button" cssClass="btn btn-warning btn-lg">--%>
			    <%--<span class="icon fa fa-times-circle"></span>&#32;--%>
				<%--<s:text name="label.confirm" />--%>
			<%--</wpsf:submit>--%>
			<%--<a class="btn btn-link" href="<s:url action="list" />">--%>
			<%--<s:text name="note.goToSomewhere" />:&#32;<s:text name="jprssaggregator.title.rssAggregator.rssManagement" /></a>--%>
		<%--</div>--%>
	<%--</div>--%>
<%--</s:form>--%>





<%--<s:set var="targetNS" value="%{'/do/jprssaggregator/Aggregator'}" />--%>
<%--<h1><s:text name="jprssaggregator.title.rssAggregator" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>--%>
<%--<div id="main">--%>
	<%--<h2><s:text name="jprssaggregator.title.rssAggregator.add" /></h2>--%>
	<%--<s:form>--%>
		<%--<p class="noscreen">--%>
			<%--<wpsf:hidden name="code" />--%>
		<%--</p>--%>

		<%--<p>--%>
			<%--<s:text name="message.are.you.sure.to.delete" /> <em><s:property value="code"/></em>&#32;?--%>
			<%--<%//TODO insert description of the current item here. %>--%>
			<%--&#32;--%>
			<%--<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" action="doDelete" value="%{getText('label.confirm')}" />--%>
		<%--</p>--%>
	<%--</s:form>--%>

<%--</div>--%>



<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpavatar.admin.menu.integration"/></li>
	<li>
		<s:text name="jpavatar.admin.menu.uxcomponents"/>
	</li>
	<li>
		<a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />">
			<s:text name="jprssaggregator.title.rssAggregator.rssManagement" />
		</a>
	</li>
	<li class="page-title-container">
		<s:text name="jprssaggregator.title.rssAggregator.remove" />
	</li>
</ol>
<h1 class="page-title-container">
	<s:text name="jprssaggregator.title.rssAggregator.remove" />
</h1>
<div class="text-right">
	<div class="form-group-separator"></div>
</div>
<br>

<div class="text-center">
	<s:form action="delete">
		<i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
		<p class="esclamation-underline"><s:text name="label.deleteAvatar"/></p>
		<p>
			<s:text name="message.are.you.sure.to.delete"/>&#32;<em><s:property value="code"/></em>&#32;?

		</p>
		<div class="text-center margin-large-top">
			<s:submit type="button" cssClass="btn btn-danger button-fixed-width">
				<s:text name="label.delete"/>
			</s:submit>
		</div>
		<div class="text-center margin-large-top">
			<a class="btn btn-default button-fixed-width"
			   href='<s:url action="management" namespace="/do/jpavatar/Config" />'>
				<s:text name="label.back"/>
			</a>
			<a class="btn btn-default button-fixed-width" href="<s:url action="list" />">
				<s:text name="note.goToSomewhere" />:&#32;<s:text name="jprssaggregator.title.rssAggregator.rssManagement" /></a>
		</div>
	</s:form>
</div>



