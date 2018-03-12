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

            <div class="table-responsive overflow-visible">
                <table id="sort" class="grid table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th class="text-center table-w-5">active</th>
                            <th class="text-center table-w-5">hostname</th>
                            <th class="text-center table-w-5">schema</th>
                            <th class="text-center table-w-5">port</th>
                            <th class="text-center table-w-5">webapp</th>
                            <th class="text-center table-w-5">debug</th>
                        </tr>
                    </thead>
                    <tbody>

                        <s:iterator value="knowledgeSource">
                            <tr>
                                <td class="index text-center">${value.active}</td>
                                <td class="index text-center">${value.hostname}</td>
                                <td class="index text-center">${value.schema}</td>
                                <td class="index text-center">${value.port}</td>
                                <td class="index text-center">${value.webapp}</td>
                                <td class="index text-center">${value.debug}</td>
                            </tr>
                        </s:iterator>

                    </tbody>
                </table>

                <style>
                    .ui-sortable-helper {
                        display: table;
                    }
                </style>
            </div>


        </div>
        <div class="col-xs-12 mb-20">
            <br />
            <br />
            <br />
            <h3>Server Status</h3>
            <s:property value="knowledgeSourceStatus" escapeHtml="false" escapeJavaScript="false"/>

        </div>
        <div class="col-xs-12 mb-20">
            <s:iterator value="knowledgeSource">

                <!--
                
                knowledgeSource value fields:
                Boolean _active;
                String _username;
                String _password;
                String _hostname;
                String _schema;
                Integer _port;
                String _webapp;
                Integer _timeoutMsec;
                Boolean _debug;
                
                
                -->
                <br />
                <br />
                <!--Generate this form for each server-->
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

                        <div class="form-group">
                            <div class="col-xs-2 control-label ">
                                <label for="hostName"><s:text name="label.hostName" /></label>
                            </div>
                            <div class="col-xs-10 ">
                                <input type="text" name="hostName" id="hostName" value="${value.hostname}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-2 control-label ">
                                <label for="schema"><s:text name="label.schema" /></label>
                            </div>
                            <div class="col-xs-10 ">
                                <input type="text" name="schema" id="schema" value="${value.schema}"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-xs-2 control-label ">
                                <label for="port"><s:text name="label.port" /></label>
                            </div>
                            <div class="col-xs-10 ">
                                <input type="text" name="port" id="port" value="${value.port}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-2 control-label ">
                                <label for="webappName"><s:text name="label.webappName" /></label>
                            </div>
                            <div class="col-xs-10 ">
                                <input type="text" name="webappName" id="webappName" value="${value.webapp}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-2 control-label ">
                                <label for="userName"><s:text name="label.userName" /></label>
                            </div>
                            <div class="col-xs-10 ">
                                <input type="text" name="userName" id="userName" value="${value.username}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-2 control-label ">
                                <label for="password"><s:text name="label.password" /></label>
                                <input type="text" name="" id="" value=""/>
                            </div>
                            <div class="col-xs-10 ">
                                <input type="password" name="password" id="password" value="${value.password}"/>
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
                        <div class="col-xs-12">
                            <wpsf:submit name="save" type="button" action="save"
                                         cssClass="btn btn-primary pull-right">
                                <s:text name="%{getText('label.save')}" />
                            </wpsf:submit>
                        </div>

                    </div>

                </s:form>
                <!--/Generate this form for each server-->
            </s:iterator>


        </div>
    </div>
    <div class="col-xs-12">
    </div>
</div>




<div class="tab-pane" id="frag-settings">
</div>
</div>
