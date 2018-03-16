<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<style type="text/css">
 .card-pf-view .card-pf-item .fa-times {
    color: #8b0000;
 }
</style>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations" /></li>
    <li><s:text name="breadcrumb.integrations.components" /></li>
    <li><s:text name="jpkiebpm.admin.menu.config" /></li>
    <li class="page-title-container">List</li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpkiebpm.admin.menu.config" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.kiebpms.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern" id="frag-tab">
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpkiebpm/Config" />" role="tab"> 
                        list
                    </a>
                </li>
                <!--                <li>
                                    <a href="<s:url action="edit" namespace="/do/jpkiebpm/Config" />" role="tab"> 
                <s:text name="label.kie.settings" />
            </a>
        </li>-->
                <li>
                    <a href="<s:url action="list" namespace="/do/jpkiebpm/form/override"/>" role="tab"> 
                        <s:text name="label.kie.list" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br/>


<!-- Tab panes -->
<div class="tab-content margin-large-bottom">
    <div class="tab-pane active" id="frag-list">

        <div class="col-xs-12 mb-20">                               

            <!--Action messages-->
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
            <!--Action messages-->
            <div class="container-fluid container-cards-pf">
                <div class="row row-cards-pf">
                    <s:iterator value="knowledgeSource"> 

                        <s:form id="configurationForm" name="configurationForm" method="post" action="edit" cssClass="form-horizontal">
                            <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4">

                                <input type="hidden" name="active" id="active" value="${value.active}"/>
                                <input type="hidden" name="debug" id="debug" value="${value.debug}"/>
                                <input type="hidden" name="id" id="id" value="${value.id}"/>
                                <input type="hidden" name="name" id="name" value="${value.name}"/>
                                <input type="hidden" name="hostName" id="hostName" value="${value.hostname}"/>
                                <input type="hidden" name="schema" id="schema" value="${value.schema}"/>
                                <input type="hidden" name="port" id="port" value="${value.port}"/>
                                <input type="hidden" name="webappName" id="webappName" value="${value.webapp}"/>
                                <input type="hidden" name="userName" id="userName" value="${value.username}"/>
                                <input type="hidden" name="password" id="password" value="${value.password}"/>
                                <input type="hidden" name="timeout" id="timeout" value="${value.timeoutMsec}"/>
                                <div class="card-pf card-pf-view card-pf-view-select card-pf-view-single-select">
                                    <div class="card-pf-title">
                                        <div class="dropdown  dropdown-kebab-pf  pull-left">
                                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebab" data-toggle="dropdown" aria-haspopup="true"
                                                    aria-expanded="true">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu" aria-labelledby="dropdownKebab">
                                                <li>
                                                    <wpsf:submit name="testinlist" type="button" action="testinlist"
                                                                 cssClass="btn btn-link">
                                                        <s:text name="TEST" />
                                                    </wpsf:submit>
                                                </li>
                                                <li>             
                                                    <wpsf:submit name="edit" type="button" action="edit"
                                                                 cssClass="btn btn-link">
                                                        <s:text name="EDIT" />
                                                    </wpsf:submit>
                                                </li>
                                                <li>
                                                    <wpsf:submit name="delete" type="button" action="delete"
                                                                 cssClass="btn btn-link">
                                                        <s:text name="REMOVE" />
                                                    </wpsf:submit>

                                                </li>
                                            </ul>
                                        </div>
                                    </div>


                                    <div class="card-pf-body">
                                        <div class="card-pf-top-element">
                                            <span class="fa fa-server card-pf-icon-circle"></span>
                                        </div>
                                        <h3 class="card-pf-title text-center">
                                            <b>SERVER</b>
                                            <br>${value.name}
                                        </h3>
                                        <div class="card-pf-items text-center">
                                            <div class="card-pf-item">

                                                <strong>Status</strong>
                                                </span>
                                            </div>
                                            <div class="card-pf-item">

                                                <s:if test="value.active">
                                                    <span class="fa fa-check"></span>
                                                </s:if>
                                                <s:else>
                                                    <span class="fafa-times"></span>
                                                </s:else>
                                            </div>
                                        </div>
                                            <%--<p class="card-pf-info text-center">
                                            <strong>ID </strong>${value.id}</p>--%>
                                        <p class="card-pf-info text-center">
                                            <strong>Debug</strong>  <s:if test="value.debug">
                                                   Active
                                                </s:if>
                                                <s:else>
                                                   Disabled
                                                </s:else>
                                        </p>
                                        <p class="card-pf-info text-center">
                                            <strong>Version</strong> WIP
                                        </p>
                                       <%--<p class="card-pf-info text-center">

                                            <strong>Connection Timeout</strong> ${value.timeoutMsec}
                                        </p>--%>
                                        <p class="card-pf-info text-center">

                                            <strong>Address</strong>
                                            ${value.schema}://${value.hostname}:${value.port}/${value.webapp}
                                        </p>
                                    </div>
                                </div>


                            </div>
                        </s:form>
                    </s:iterator>
                </div>


            </div>
            <a href="<s:url action="addConf" namespace="/do/jpkiebpm/Config" />" class="btn btn-primary pull-right"> 
                <s:text name="Add Configuration" />
            </a>
        </div>
        <div class="col-xs-12 mb-20">
            <br />
            <br />
            <br />
            <h3>Server Status</h3>
            <s:property value="knowledgeSourceStatus" escapeHtml="false" escapeJavaScript="false"/>

        </div>
        <div class="col-xs-12 mb-20">
        </div>
    </div>
    <div class="col-xs-12">
    </div>
</div>

<div class="tab-pane" id="frag-settings">
</div>
</div>
