b prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations" /></li>
    <li><s:text name="breadcrumb.integrations.components" /></li>
    <li class="page-title-container"><s:text name="jpcalendar.admin.config" /></li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1>
                <s:text name="jpcalendar.admin.config" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                       title="" data-content="<s:text name="title.jpcalendar.configManagement.help" />"
                       data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>
<br>
<div class="mb-20">
    <s:form action="save" namespace="/do/jpcalendar/Config" class="form-horizontal">
        <div id="messages">
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
        </div>

        <s:if test="null == contentType">
            <fieldset class="col-xs-12">
                <legend>
                    <s:text name="title.contentInfo" />
                </legend>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="contentType">
                        <s:text name="label.type" />
                    </label>
                    <div class="col-sm-10">
                        <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="description" cssClass="form-control" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <wpsf:submit type="button" action="configContentType"
                                     cssClass="btn btn-primary pull-right">
                            <s:text name="%{getText('label.continue')}" />
                        </wpsf:submit>
                    </div>
                </div>
            </fieldset>
        </s:if>
        <s:else>

            <fieldset class="col-xs-12">
                <legend>
                    <s:text name="title.contentInfo" />
                </legend>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="contentType">
                        <s:text name="label.type" />:
                    </label>
                    <div class="col-sm-10">
                        <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="description" disabled="true" cssClass="form-control" />
                    </div>
                </div>
                <p class="noscreen">
                    <wpsf:hidden name="contentType" />
                </p>
                <div class="form-group">
                    <div class="col-xs-12">
                        <wpsf:submit action="changeContentType" type="button"
                                     cssClass="btn btn-primary pull-right">
                            <s:text name="%{getText('label.change')}" />
                        </wpsf:submit>
                    </div>
                </div>
            </fieldset>

            <fieldset class="col-xs-12">
                <legend>
                    <s:text name="title.attributes" />
                </legend>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="startDateAttributeName">
                        <s:text name="label.startDateAttribute" />
                    </label>
                    <div class="col-sm-10">
                        <wpsf:select name="startDateAttributeName" id="startDateAttributeName" list="allowedDateAttributes" listKey="key" listValue="value" cssClass="form-control" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="endDateAttributeName">
                        <s:text  name="label.endDateAttribute" />
                    </label>
                    <div class="col-sm-10">
                        <wpsf:select name="endDateAttributeName" id="endDateAttributeName" list="allowedDateAttributes" listKey="key" listValue="value" cssClass="form-control" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <wpsf:submit action="save" type="button"
                                     cssClass="btn btn-primary pull-right">
                            <s:text name="%{getText('label.save')}" />
                        </wpsf:submit>
                    </div>
                </div>
            </fieldset>
        </s:else>
    </s:form>
</div>

