<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" 		uri="/struts-tags"%>
<%@ taglib prefix="wp" 		uri="/aps-core" %>
<%@ taglib prefix="wpsa" 	uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" 	uri="/apsadmin-form"%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpaddressbook.title.vcard" />
		&#32;/&#32;
		<s:text name="jpaddressbook.title.manageActiveFields" />
	</span>
</h1>
<div id="main">
	<p class="text-info"><s:text name="jpaddressbook.manageActiveFields.intro" /></p>
	<s:form cssClass="form-horizontal" action="filterFields">
		<s:if test="hasActionErrors()">
			<div class="alert alert-danger  alert-dismissable fade in">
				<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
				<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
					<ul class="unstyled margin-small-top">
						<s:iterator value="actionErrors">
							<s:iterator value="value">
								<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
			</div>
		</s:if>
			<%-- <legend><s:text name="label.info" /></legend> --%>
		<s:set var="selectedFieldsVar" value="%{getSelectedFields()}" />
		<ul class="list-group">
			<s:iterator var="vcardField" value="vCardFields">
				<s:set var="code" value="#vcardField.code"/>
				<s:set var="profileAttribute" value="#vcardField.profileAttribute"/>
				<li class="list-group-item">
						<div class="checkbox">
							<dfn class="pull-right badge badge-default"><small><s:property value="#vcardField.code"/></small></dfn>
							<label for="selectedFields_<s:property value="#code"/>">
								<wpsf:checkbox
									name="selectedFields"
									fieldValue="%{#code}"
									value="%{#selectedFieldsVar.contains(#code)}"
									id="selectedFields_%{#code}"
									cssClass="radiocheck"
									/>
									<s:text name="%{'jpaddressbook.vcard.field.'+#vcardField.code}" />

							</label>
						</div>
				</li>
			</s:iterator>
		</ul>
		<%-- Button save configuration--%>
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-base-vertical">
				<wpsf:submit
					type="button"
					cssClass="btn btn-primary btn-block">
						<span class="icon fa fa-play-circle-o"></span>
						<s:text name="jpaddressbook.vcardSubmit.continue" />
				</wpsf:submit>
			</div>
		</div>
	</s:form>
</div>
