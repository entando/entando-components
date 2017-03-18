<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="jpnewsletter.title.newsletterManagement" />&#32;/&#32;<s:text name="jpnewsletter.title.newsletterConfig" />
	</span>
</h1>

<s:form action="addContentTypeConfig">
	<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable fade in margin-base-top">
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
	
	<fieldset class="margin-large-top"><legend><s:text name="jpnewsletter.contentType" /></legend>
		<p class="sr-only"><wpsf:hidden name="newsletterContentType.contentTypeCode" /></p>
		<p><s:text name="jpnewsletter.contenttypeConfig.intro" />: <s:property value="getSmallContentType(contentTypeCode).descr" />.</p>
		
		<div class="form-group">
			<label for="jpnewsletter_text_model"><s:text name="jpnewsletter.plainModel" /></label>
			<wpsf:select id="jpnewsletter_text_model" name="newsletterContentType.simpleTextModel" 
						 list="getContentModelByType(newsletterContentType.contentTypeCode)" listKey="id" listValue="description" cssClass="form-control" />
		</div>

		<div class="form-group">
			<label for="jpnewsletter_html_model"><s:text name="jpnewsletter.htmlModel" /></label>
		<wpsf:select id="jpnewsletter_html_model" name="newsletterContentType.htmlModel" list="getContentModelByType(newsletterContentType.contentTypeCode)" 
				headerKey="" headerValue="" listKey="id" listValue="description" cssClass="form-control" />
		</div>
	</fieldset>
	<div class="form-group">
		<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
			<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
				<span class="icon fa fa-floppy-o"></span>&#32;
				<s:text name="label.add" />
			</wpsf:submit>
		</div>
	</div>
</s:form>