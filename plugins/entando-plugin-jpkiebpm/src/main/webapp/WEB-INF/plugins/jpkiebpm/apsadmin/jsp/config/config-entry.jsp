<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations" /></li>
    <li><s:text name="breadcrumb.integrations.components" /></li>
    <li><s:text name="jpkiebpm.admin.menu.config" /></li>
    <li class="page-title-container"><s:text name="label.kie.settings" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:text name="jpkiebpm.admin.menu.config" />
                <span class="pull-right"> <a tabindex="0" role="button"
                                             data-toggle="popover" data-trigger="focus" data-html="true"
                                             title="" data-content="<s:text name="title.kiebpms.help" />"
                                             data-placement="left" data-original-title=""> <i
                            class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>
<br />

<div class="mb-20">

    <s:form id="configurationForm" name="configurationForm" method="post" action="save" cssClass="form-horizontal">
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"
                        aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span> <strong><s:text
                        name="message.title.ActionErrors" /></strong>
                <ul class="margin-base-top">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-success alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"
                        aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-ok"></span> <strong><s:text
                        name="messages.confirm" /></strong>
                <ul class="margin-base-top">
                    <s:iterator value="actionMessages">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <fieldset class="col-xs-12">
            <legend>
                <s:text name="legend.generalSettings" />
            </legend>
            <div class="form-group">
                <div class="col-xs-4">
                    <div class="col-xs-2 control-label ">
                        <s:text name="label.active" />
                    </div>
                    <div class="col-xs-10 ">
                        <wpsf:checkbox name="active" id="active"
                                       cssClass="bootstrap-switch" />
                    </div>
                </div>

                <div class="col-xs-4">
                    <div class="col-xs-2 control-label ">
                        <s:text name="label.debug" />
                    </div>
                    <div class="col-xs-10 ">
                        <wpsf:checkbox name="debug" id="debug" cssClass="bootstrap-switch" />
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend>
                <s:text name="legend.connection" />
            </legend>
            <wpsf:hidden name="id" id="id" cssClass="form-control" placeholder="id" />
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="name"><s:text name="label.name" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="name" id="name"
                                    cssClass="form-control" placeholder="name" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="hostName"><s:text name="label.hostName" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="hostName" id="hostName"
                                    cssClass="form-control" placeholder="hostName" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="schema"><s:text name="label.schema" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="schema" id="schema" cssClass="form-control" />
                </div>
            </div>

            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="port"><s:text name="label.port" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="port" id="port" cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="webappName"><s:text name="label.webappName" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="webappName" id="webappName"
                                    cssClass="form-control" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="userName"><s:text name="label.userName" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="userName" id="userName"
                                    cssClass="form-control" placeholder="userName" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="password"><s:text name="label.password" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="password" id="password"
                                    cssClass="form-control" type="password" placeholder="password" />
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-2 control-label ">
                    <label for="password"><s:text name="label.timeout" /></label>
                </div>
                <div class="col-xs-10 ">
                    <wpsf:textfield name="timeout" id="timeout" cssClass="form-control" />
                </div>
            </div>
        </fieldset>

        <div class="form-group">

            <div class="col-xs-12">
                <wpsf:submit name="test" type="button" action="test"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="%{getText('label.test')}" />
                </wpsf:submit>
            </div>

        </div>
        <div class="form-group">
            <div class="col-xs-6">
                <a href="<s:url action="list" namespace="/do/jpkiebpm/Config" />" class="btn btn-warning"> 
                    <s:text name="Cancel" />
                </a>
            </div>
            <div class="col-xs-6">
                <wpsf:submit name="save" type="button" action="save"
                             cssClass="btn btn-primary pull-right">
                    <s:text name="%{getText('label.save')}" />
                </wpsf:submit>
            </div>

        </div>

    </s:form>
</div>
