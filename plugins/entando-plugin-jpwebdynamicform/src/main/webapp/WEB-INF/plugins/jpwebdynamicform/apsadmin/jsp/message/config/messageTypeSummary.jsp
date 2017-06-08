<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
    <li class="page-title-container">
        <s:text name="breadcrumb.configuration"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="title.message.original"/>
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
                <li class="active">
                    <a href="<s:url namespace="/do/jpwebdynamicform/Message/Config" action="list" />"><s:text
                            name="breadcrumb.configuration"/></a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Entity" action="viewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>"><s:text
                            name="breadcrumb.messageType"/></a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<div id="main">

    <s:if test="%{messageTypes.size() > 0}">
        <div class="panel panel-default">
            <div class="panel-body">
                <s:text name="messageTypes.intro"/>
            </div>
        </div>

        <s:form class="form-horizontal" action="edit">
            <%--<legend><s:text name="label.info" /></legend>--%>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="jpwebdynamicform_message_type"><s:text name="label.messageType" /></label>
                <div class="col-sm-10">
                    <wpsf:select id="jpwebdynamicform_message_type" name="typeCode" list="%{messageTypes}" listKey="code" listValue="descr" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group pull-right">
                <div class="col-xs-12">
                    <div class="btn-group">
                        <wpsf:submit type="button" cssClass="btn btn-primary ">
                            <s:text name="%{getText('label.continue')}" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </s:form>
    </s:if>
    <s:else>
        <div class="alert alert-info">
            <span class="pficon pficon-info"></span>
                <s:text name="no.messageType.to.configure" />
            </div>
    </s:else>
</div>