<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
	<li><s:text name="jpnewsletter.integrations"/></li>
	<li><s:text name="jpnewsletter.components"/></li>
	<li><s:text name="jpnewsletter.title.newsletterManagement"/></li>
	<li class="page-title-container">
		<s:text name="jpnewsletter.title.newsletterConfig"/>
	</li>
</ol>
<div class="page-tabs-header">
	<div class="row">
		<div class="col-sm-4">
			<h1>
				<s:text name="jpnewsletter.admin.menu"/>
				<span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
					   data-content="TO be inserted" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
			</h1>
		</div>
		<div class="col-sm-8">
			<ul class="nav nav-tabs nav-justified nav-tabs-pattern">
				<li>
					<a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />">
						<s:text name="jpnewsletter.admin.list"/>
					</a>
				</li>
				<li>
					<a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />">
						<s:text name="jpnewsletter.admin.queue"/>
					</a>
				</li>
				<li>
					<a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />">
						<s:text name="jpnewsletter.admin.subscribersList"/>
					</a>
				</li>
				<li class="active">
					<a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />">
						<s:text name="jpnewsletter.admin.config"/>
					</a>
				</li>
			</ul>
		</div>
	</div>
</div>
<br>

<s:form action="addContentTypeConfig">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />

	<fieldset class="margin-large-top form-horizontal">
		<legend><s:text name="jpnewsletter.contentType" /></legend>

		<p class="sr-only"><wpsf:hidden name="newsletterContentType.contentTypeCode" /></p>
		<p><s:text name="jpnewsletter.contenttypeConfig.intro" />: <s:property value="getSmallContentType(contentTypeCode).descr" />.</p>

		<div class="form-group">
			<label class="col-sm-2 control-label" for="jpnewsletter_text_model">
				<s:text name="jpnewsletter.plainModel" />
			</label>
			<div class="col-sm-10">
				<wpsf:select id="jpnewsletter_text_model" name="newsletterContentType.simpleTextModel" list="getContentModelByType(newsletterContentType.contentTypeCode)" listKey="id" listValue="description" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="jpnewsletter_html_model">
				<s:text name="jpnewsletter.htmlModel" />
			</label>
			<div class="col-sm-10">
				<wpsf:select id="jpnewsletter_html_model" name="newsletterContentType.htmlModel" list="getContentModelByType(newsletterContentType.contentTypeCode)" headerKey="" headerValue="" listKey="id" listValue="description" cssClass="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<wpsf:submit type="button" cssClass="btn btn-primary pull-right">
					<s:text name="label.add" />
				</wpsf:submit>
			</div>
		</div>
	</fieldset>
</s:form>