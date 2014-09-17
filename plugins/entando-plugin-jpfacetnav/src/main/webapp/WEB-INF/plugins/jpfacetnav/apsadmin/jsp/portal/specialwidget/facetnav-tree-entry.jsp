<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a>&#32;/&#32;
		<a href="<s:url action="configure" namespace="/do/Page"><s:param name="pageCode"><s:property value="currentPage.code"/></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />"><s:text name="title.configPage" /></a>&#32;/&#32;
		<s:text name="name.widget"/>
	</span>
</h1>
<div id="main">
<%--Error management--%>
		<s:if test="hasFieldErrors()">
				<div class="alert alert-danger alert-dismissable fade in">
						<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
						<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
						<ul class="margin-small-top">
								<s:iterator value="fieldErrors">
								 <s:iterator value="value"><li><s:property escape="false" /></li></s:iterator>
								</s:iterator>
						</ul>
				</div>
		</s:if>
	<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
	<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
	<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
	<s:form action="saveConfig" namespace="/do/jpfacetnav/Page/SpecialWidget/FacetNavTree" cssClass="form-horizontal action-form" id="facetnav-actionform">
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
          <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
        </p>
        <p class="text-info">
          <s:text name="jpfacetnav.note.facetNavTree.intro" />
        </p>
          <wpsa:include value="/WEB-INF/plugins/jpfacetnav/apsadmin/jsp/portal/specialwidget/include/content-type-list.jsp" />
          <wpsa:include value="/WEB-INF/plugins/jpfacetnav/apsadmin/jsp/portal/specialwidget/include/facet-list.jsp" />
			</div>
		</div>
		<%--Button Save--%>
			<div class="form-group">
				<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
					<wpsf:submit type="button" cssClass="btn btn-primary btn-block" value="%{getText('label.save')}">
						<span class="icon fa fa-floppy-o"></span>&#32;
						<s:text name="label.save" />
					</wpsf:submit>
				</div>
		</div>
	</s:form>
</div>