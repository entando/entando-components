<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
    <li>
        <s:text name="jpwebdynamicform.name"/>
    </li>
    <li class="page-title-container">
        <s:text name="breadcrumb.capcha"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpwebdynamicform.name"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpwebdynamicform.menu.configuration.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />"><s:text
                            name="breadcrumb.messageList"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Message/Config" action="list" />"><s:text
                            name="breadcrumb.configuration"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Entity" action="viewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>"><s:text
                            name="breadcrumb.messageType"/></a>
                </li>
                <li class="active">
                    <a href="<s:url namespace="/do/jpwebdynamicform/Config" action="systemParams"></s:url>"><s:text
                            name="breadcrumb.capcha"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">
    <s:form action="updateSystemParams" class="form-horizontal">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                    <span class="pficon pficon-close"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <strong><s:text name="message.title.ActionErrors" />
                </strong>
                <ul class="margin-base-top">
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-success">
                <span class="pficon pficon-ok"></span>
                <strong><s:text name="messages.confirm" /></strong>
                <s:iterator value="actionMessages">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </div>
        </s:if>

        <div class="form-group">
            <s:set var="jpwebdynamicform_paramName" value="'jpwebdynamicform_recaptcha_publickey'" />
            <label class="control-label col-sm-2 text-right" for="<s:property value="#jpwebdynamicform_paramName"/>"><s:text name="jpwebdynamicform.recaptcha.publickey" /></label>
            <div class="col-sm-9">
                <wpsf:textfield name="%{#jpwebdynamicform_paramName}" id="%{#jpwebdynamicform_paramName}" value="%{systemParams.get(#jpwebdynamicform_paramName)}" cssClass="form-control" />
                <wpsf:hidden name="%{#jpwebdynamicform_paramName + externalParamMarker}" value="true"/>
            </div>
        </div>

        <div class="form-group">
            <s:set var="jpwebdynamicform_paramName" value="'jpwebdynamicform_recaptcha_privatekey'" />
            <label class="control-label col-sm-2 text-right" for="<s:property value="#jpwebdynamicform_paramName"/>"><s:text name="jpwebdynamicform.recaptcha.privatekey" /></label>
            <div class="col-sm-9">
                <wpsf:textfield name="%{#jpwebdynamicform_paramName}" id="%{#jpwebdynamicform_paramName}" value="%{systemParams.get(#jpwebdynamicform_paramName)}" cssClass="form-control" />
                <wpsf:hidden name="%{#jpwebdynamicform_paramName + externalParamMarker}" value="true"/>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="form-group pull-right">
                <div class="btn-group">
                    <wpsf:submit type="button" cssClass="btn btn-primary ">
                        <s:text name="label.save"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
