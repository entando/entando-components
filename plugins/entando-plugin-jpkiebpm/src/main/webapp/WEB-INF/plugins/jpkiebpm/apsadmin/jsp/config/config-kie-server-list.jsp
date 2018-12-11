<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/administration/js/jbpm-servers-config.js"></script>
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
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:text name="jpkiebpm.admin.menu.config" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.kiebpms.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>
<br/>

<!-- Tab panes -->
<div class="tab-content margin-large-bottom" >
    <div class="tab-pane active" id="frag-list">

        <div class="col-xs-12 mb-20">
            <div class="form-group">
                <div class="col-xs-6">
                    <a href="<s:url action="testall" namespace="/do/jpkiebpm/Config" />" class="btn btn-warning">
                        <s:text name="Test All" />
                    </a>
                </div>
                <div class="col-xs-6">
                    <a href="<s:url action="addConf" namespace="/do/jpkiebpm/Config" />" class="btn btn-primary pull-right">
                        <s:text name="Add Configuration" />
                    </a>
                </div>
            </div>
            <br />
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

            <div class="container-fluid container-cards-pf"  ng-controller="CaseServersController as vm" ng-app="caseServersApp">
                <div class="row row-cards-pf">
                    <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4" ng-repeat="ks in vm.data.servers track by ks.id">
                        <div class="card-pf card-pf-view card-pf-view-select card-pf-view-single-select">
                            <div class="card-pf-title">
                                <div class="dropdown pull-right dropdown-kebab-pf">
                                    <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebab" data-toggle="dropdown" aria-haspopup="true"
                                            aria-expanded="true">
                                        <span class="fa fa-ellipsis-v"></span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebab" >
                                        <ng-include src="vm.ui.getForm(ks)"></ng-include>
                                    </ul>
                                </div>
                            </div>


                            <div class="card-pf-body">
                                <div class="card-pf-top-element">
                                    <span class="fa fa-server card-pf-icon-circle"></span>
                                </div>
                                <h3 class="card-pf-title text-center">
                                    <strong>SERVER</strong><br> {{ks.config.name}}
                                </h3>

                                <div class="card-pf-items" ng-init="hc = vm.ui.connectionTest(ks)" ng-show="hc">
                                    <div class="card-pf-item">

                                        <strong>Connection Test</strong>
                                        </span>
                                    </div>
                                    <div class="card-pf-item">
                                        <span class="fa" ng-class="{'fa-check':hc.passed , 'fa-times': !hc.passed  }"></span>
                                    </div>
                                </div>
                                <div class="card-pf-items">
                                    <div class="card-pf-item">

                                        <strong>Active</strong>
                                        </span>
                                    </div>
                                    <div class="card-pf-item">
                                        <span class="fa" ng-class="{'fa-check':ks.config.active , 'fa-times': !ks.config.active }"></span>
                                    </div>
                                </div>
                                <p class="card-pf-info">
                                    <strong>Debug</strong>  {{ks.config.debug ? "Active" : "Disabled"}}
                                </p>
                                <p class="card-pf-info">
                                    <strong>Version</strong> {{ks.status.result['kie-server-info'].version}}
                                </p>
                                <p class="card-pf-info">
                                    <strong>Address</strong>{{ks.config.schema+"://" + ks.config.hostname+":"+ks.config.port+"/"+ks.config.webapp}}
                                </p>
                            </div>
                            <hr>
                            <div class="card-pf-body">
                                <strong>Capabilities</strong>
                                <p class="card-pf-info" ng-if="ks.status">
                                    <span ng-repeat="capability in ks.status.result['kie-server-info'].capabilities" class="label label-default" style="display:inline-block; padding-top:0; padding-bottom:0; line-height:1.5em;margin-right: 2px;">{{capability}}</span>
                                </p>
                            </div>
                        </div>
                    </div>

                </div>
                <!--Action messages-->
                <s:iterator value="knowledgeSource">


                    <script type="text/ng-template" id="srv-${value.id}">
                        <s:form id="configurationForm" name="configurationForm" method="post" action="edit" cssClass="form-horizontal" >
                            <input type="hidden" name="active" id="active" value="${value.active}" />
                            <input type="hidden" name="debug" id="debug" value="${value.debug}"/>
                                   <input type="hidden" name="id" id="id" value="${value.id}"/>
                                   <input type="hidden" name="name" id="name" value="${value.name}"/>
                                   <input type="hidden" name="hostName" id="hostName" value="${value.hostname}"/>
                                   <input type="hidden" name="schema" id="schema" value="${value.schema}"/>
                                   <input type="hidden" name="port" id="port" value="${value.port}"/>
                                   <input type="hidden" name="webappName" id="webappName" value="${value.webapp}" />
                                   <input type="hidden" name="userName" id="userName" value="${value.username}"                                                                  />
                            <input type="hidden" name="password" id="password" value="${value.password}" />
                            <input type="hidden" name="timeout" id="timeout" value="${value.timeoutMsec}" />
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
                            </s:form>
                            </script>
                        </s:iterator>
                </div>
            </div>
        </div>
    </div>

    <div class="tab-pane" id="frag-settings">
    </div>
    <script type="text/javascript">
        var allServerTest = undefined;
        <s:if test="knowledgeSourceTestAllResult != null">
        allServerTest = <s:property value="knowledgeSourceTestAllResult" escapeJavaScript="false" escapeHtml="false"/>;
        </s:if>

        <s:if test="knowledgeSourceStatus != null">
        var knowledgeSourceStatus = <s:property value="knowledgeSourceStatus" escapeJavaScript="false" escapeHtml="false"/>;
        bootBpmComponent(knowledgeSourceStatus, allServerTest);
        </s:if>
    </script>
