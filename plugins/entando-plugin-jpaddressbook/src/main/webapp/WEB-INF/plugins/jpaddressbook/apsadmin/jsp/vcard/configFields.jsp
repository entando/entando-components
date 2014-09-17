<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" 		uri="/struts-tags"%>
<%@ taglib prefix="wp" 		uri="/aps-core" %>
<%@ taglib prefix="wpsa" 	uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" 	uri="/apsadmin-form"%>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="/testdevelop/do/jpaddressbook/VCard/edit.action">
			<s:text name="jpaddressbook.title.vcard" />
		</a>
		&#32;/&#32;
		<s:text name="jpaddressbook.title.mappingActiveFields" />
</h1>

<div id="main">
	<%--
			<legend class="accordion_toggler"><s:text name="jpaddressbook.title.mappingActiveFields" /></legend>
	--%>
	<s:if test="hasActionErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<%--
			<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
			--%>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="alert alert-danger alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
			<%--
			<ul class="margin-base-top">
				<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
			</ul>
			--%>
		</div>
	</s:if>

	<p class="text-info"><s:text name="jpaddressbook.mappingActiveFields.intro" /></p>
	<s:form action="save" cssClass="form-horizontal">
		<%-- <legend><s:text name="label.info" /></legend> --%>
		<p class="hidden sr-only">
			<s:iterator var="field" value="selectedFields" status="rowStatus">
				<wpsf:hidden name="selectedFields" value="%{#field}" id="selectedFields_%{#rowStatus.index}" />
			</s:iterator>
		</p>
		<s:set var="selectedFieldsVar" value="getSelectedFields()" />
		<s:set var="profileMappingVar" value="getProfileMapping()" />
		<s:iterator var="vcardField" value="vCardFields">
			<s:set var="code" value="#vcardField.code" />
			<s:if test="%{#selectedFieldsVar.contains(#code)}">
				<s:set var="profileAttribute" value="%{profileMappingVar.get(#code)}" />
			  <s:set var="currentFieldErrorsVar" value="%{fieldErrors['sel_'+#code]}" />
			  <s:set var="currentFieldHasErrorVar" value="#currentFieldErrorsVar != null && !#currentFieldErrorsVar.isEmpty()" />
			  <s:set var="controlGroupErrorClassVar" value="''" />

			  <s:if test="#currentFieldHasErrorVar">
			    <s:set var="controlGroupErrorClassVar" value="' has-error'" />
			  </s:if>

				<div class="form-group<s:property value="#controlGroupErrorClassVar" />">
      		<div class="col-xs-12">
						<label
						 for="jpaddressbook_vcard_sel_<s:property value="%{#code}" />">
					 		<span class="label label-default display-inline">
					 			<dfn><s:property value="#vcardField.code"/></dfn>
					 		</span>
					 		&ensp;
						 	<s:text name="%{'jpaddressbook.vcard.field.'+#vcardField.code}" />
				 		</label>
							<wpsf:select
								cssClass="form-control"
								name="sel_%{#code}"
								id="jpaddressbook_vcard_sel_%{#code}"
								list="entityFields"
								listKey="name"
								listValue="name"
								headerKey=""
								headerValue=""
								value="%{#profileAttribute}" />
							<s:if test="#currentFieldHasErrorVar">
					      <p class="text-danger padding-small-vertical">
					      	<s:iterator value="#currentFieldErrorsVar"><s:property /> </s:iterator>
				      	</p>
					    </s:if>
					    <s:set var="currentFieldErrorsVar" value="%{null}" />
							<s:set var="currentFieldHasErrorVar" value="%{null}" />
							<s:set var="controlGroupErrorClassVar" value="%{null}" />
					</div>
				</div>
			</s:if>
		</s:iterator>

		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-base-top">
				<wpsf:submit  value="%{getText('label.save')}" type="button" cssClass="btn btn-primary btn-block">
					 <span class="icon fa fa-floppy-o">&#32;<s:text name="label.save" /></span>
				 </wpsf:submit>
			</div>
		</div>

	</s:form>
</div>
