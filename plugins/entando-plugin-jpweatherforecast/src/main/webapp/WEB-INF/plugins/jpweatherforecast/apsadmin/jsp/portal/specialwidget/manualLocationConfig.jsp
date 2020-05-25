<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
			<s:text name="title.pageManagement" /></a>&#32;/&#32;
		<s:text name="title.configPage" />
	</span>
</h1>

<div id="main" role="main">

<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
	<s:param name="selectedNode" value="currentPage.code"></s:param>
</s:action>

	<div class="alert alert-info">
			<s:text name="jpweatherforecast.label.insert.manual.location" />
	</div>

<%-- error //start --%>
			<s:if test="hasFieldErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.FieldErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
								<li><s:property /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</s:if>
			<s:if test="hasActionErrors()">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert">
						<span class="icon fa fa-times"></span>
					</button>
					<h2 class="h4 margin-none">
						<s:text name="message.title.ActionErrors" />
					</h2>
					<ul class="margin-base-vertical">
						<s:iterator value="actionErrors">
							<li><s:property /></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
		<%-- error //end --%>

<s:form action="saveConfig" namespace="/do/jpweatherforecast/Page/SpecialWidget/location" cssClass="form-horizontal">
    <s:hidden name="_csrf" value="%{csrfToken}"/>

	<p class="noscreen">
		<wpsf:hidden name="pageCode" />
		<wpsf:hidden name="frame" />
		<wpsf:hidden name="widgetTypeCode" />
		<wpsf:hidden name="strutsAction" />
	</p>

	<div class="form-group">
		<div class="col-xs-12">
			<s:text name="jpweatherforecast.label.city.reference" /><br/>
			<s:textfield name="city" /> 
		</div>
	</div>
	
	<%--
	<div class="form-group">
		<div class="col-xs-12">
			<s:text name="jpweatherforecast.label.countryCode.reference" /><br/>
			<s:textfield name="country" />
		</div> 
	</div>
	--%>
	<div class="form-group">
		<div class="col-xs-12">
			<s:text name="jpweatherforecast.label.countryCode" />
			<wpsf:select 
			list="availableLocales"
			name="country"
			id="jpweatherforecast-location-field-form"
			cssClass="form-control"
			/>
		</div>
	</div>

	<div class="form-group">
		<div class="col-xs-12">
			<s:text name="jpweatherforecast.label.latitude.reference" /><br/>
			<s:textfield name="latitude"/>
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-xs-12">
			<s:text name="jpweatherforecast.label.longitude.reference" /><br/>
			<s:textfield name="longitude"/>
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit action="saveConfig" type="button" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.save" />
			</wpsf:submit>
		</div>
	</div>

</s:form>

</div>