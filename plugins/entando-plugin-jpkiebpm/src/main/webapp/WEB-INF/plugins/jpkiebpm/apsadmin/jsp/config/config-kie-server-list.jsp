<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
<script src="<wp:resourceURL />administration/js/lodash.js"></script>

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

            <div class="table-responsive overflow-visible">

                <table id="sort" class="grid table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th class="text-center table-w-5">Active</th>
                            <th class="text-center table-w-5">Debug</th>
                            <th class="text-center table-w-5">Name</th>
                            <th class="text-center table-w-5">Hostname</th>
                            <th class="text-center table-w-5">Schema</th>
                            <th class="text-center table-w-5">Port</th>
                            <th class="text-center table-w-5">Webapp</th>
                            <th class="text-center table-w-5">Username</th>
                            <th class="text-center table-w-5">Password</th>
                            <th class="text-center table-w-5">Timeout</th>
                            <th class="text-center table-w-5">Test</th>
                            <th class="text-center table-w-5">Edit</th>
                            <th class="text-center table-w-5">Delete</th>
                        </tr>
                    </thead>
                    <tbody>

                        <!--knowledgeSource is HashMap<String, KieBpmConfig>, this can be replaced with angularjs iterator along 
                         config data in knowledgeSourceStatus json. In order to get the Strut form work, the following are needed:
                        - form tag
                        - Action messages (not essential)
                        - input with same name and id
                        - submit button with relative action  
                        -->
                        <s:iterator value="knowledgeSource"> 

                            <s:form id="configurationForm" name="configurationForm" method="post" action="edit" cssClass="form-horizontal">

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
                            <%--<wpsf:textfield type="hidden" name="timeout" id="timeout" cssClass="form-control" />--%>

                            <tr>
                                <td class="index text-center">
                                    ${value.active}
                                </td>
                                <td class="index text-center">
                                    ${value.debug}
                                </td>
                                <td class="index text-center">
                                    ${value.name}
                                </td>
                                <td class="index text-center">
                                    ${value.hostname}
                                </td>
                                <td class="index text-center">
                                    ${value.schema}
                                </td>
                                <td class="index text-center">
                                    ${value.port}
                                </td>
                                <td class="index text-center">
                                    ${value.webapp}
                                </td>
                                <td class="index text-center">
                                    ${value.username}
                                </td>
                                <td class="index text-center">
                                    ${value.password}
                                </td>
                                <td class="index text-center">
                                    ${value.timeoutMsec}
                                </td>

                                <td class="index text-center">
                                    <wpsf:submit name="testinlist" type="button" action="testinlist"
                                                 cssClass="btn btn-primary pull-right">
                                        <s:text name="Test" />
                                    </wpsf:submit>
                                </td>
                                <td class="index text-center">
                                    <wpsf:submit name="edit" type="button" action="edit"
                                                 cssClass="btn btn-primary pull-right">
                                        <s:text name="Edit" />
                                    </wpsf:submit>
                                </td>
                                <td class="index text-center">
                                    <wpsf:submit name="delete" type="button" action="delete"
                                                 cssClass="btn btn-primary pull-right">
                                        <s:text name="Delete" />
                                    </wpsf:submit>
                                </td>
                            </tr>
                        </s:form>
                    </s:iterator>

                    </tbody>
                </table>

                <style>
                    .ui-sortable-helper {
                        display: table;
                    }
                </style>
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
