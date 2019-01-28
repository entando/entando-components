<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="<s:url action="configure" namespace="/do/Page"><s:param name="pageCode"><s:property value="currentPage.code"/></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget" />
	</span>
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

<s:form action="saveConfig" namespace="/do/dashboard/DashboardBarChart/Page/SpecialWidget/dashboardDashboardBarChartConfig" cssClass="form-horizontal">
<div class="panel panel-default">
	<div class="panel-heading">
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
	</div>

	<div class="panel-body">

		<h2 class="h5 margin-small-vertical">
			<label class="sr-only"><s:text name="name.widget" /></label>
			<span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
			<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
		</h2>

		<p class="sr-only">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="widgetTypeCode" value="%{widget.type.code}" />
		</p>

		<s:if test="hasFieldErrors()">
			<div class="alert alert-danger alert-dismissable">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
				<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escapeHtml="false" /></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
		</s:if>

		<div class="form-group margin-base-top">
			<div class="col-xs-12">
				<label for="<s:property value="id" />"><s:text name="label.id"/></label>
				<div class="input-group">
					<wpsf:select list="dashboardBarChartsId" name="id" id="id" cssClass="form-control" />
					<span class="input-group-btn">
						<wpsf:submit type="button" cssClass="btn btn-success">
							<span class="icon fa fa-check"></span>&#32;
							<s:text name="label.confirm" />
						</wpsf:submit>
					</span>
				</div>
			</div>
		</div>
	</div>
</div>

</s:form>
</div>
