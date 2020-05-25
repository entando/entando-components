<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcontentworkflow.menu.workflowAdmin"/></li>
    <li class="page-title-container">
        <s:text name="title.workflowNotifierManagement.config"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1>
                <s:text name="jpcontentworkflow.menu.workflowAdmin"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpcontentworkflow.title.general.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="list" namespace="/do/jpcontentworkflow/Workflow" />">
                        <s:text name="jpcontentworkflow.menu.workflow"/>
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="config" namespace="/do/jpcontentworkflow/Notifier" />">
                        <s:text name="jpcontentworkflow.menu.notifier"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>
<div>
    <s:form action="save" cssClass="">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert">
                    <span class="icon fa fa-times"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.FieldErrors"/>
                <ul>
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escapeHtml="false"/></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionErrors()">
            <div class="alert alert-danger alert-dismissable fade in">
                <button class="close" data-dismiss="alert">
                    <span class="icon fa fa-times"></span>
                </button>
                <span class="pficon pficon-error-circle-o"></span>
                <s:text name="message.title.FieldErrors"/>
                <ul>
                    <s:iterator value="actionErrors">
                        <li><s:property escapeHtml="false"/></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <fieldset class="col-xs-12">
            <legend>
                <s:text name="notifier.generalSettings"/>
            </legend>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="active">
                            <s:text name="label.active"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:checkbox name="config.active" id="active" cssClass="bootstrap-switch"/>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend>
                <s:text name="label.schedulerSettings"/>
            </legend>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="hoursDelay">
                            <s:text name="label.hoursDelay"/>
                        </label>
                        <div class="col-sm-10">
                            <s:set var="hoursDelayVar" value="%{hoursDelay}" scope="page"/>
                            <select name="config.hoursDelay" id="hoursDelay" class="form-control">
                                <c:forEach begin="1" end="10" varStatus="status">
                                    <option
                                        <c:if test="${(status.count*24) == hoursDelayVar}">selected="selected"</c:if>
                                        value="<c:out value="${status.count*24}" />">
                                        <c:out value="${status.count*24}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="jpcontentworkflownotifier_date_cal">
                            <s:text name="label.startDate"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:textfield name="startDate" id="jpcontentworkflownotifier_date_cal" placeholder="dd/mm/yyyy" cssClass="form-control datepicker"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label">
                            <s:text name="notifier.time"/>
                        </label>
                        <div class="col-sm-3" style="display: inline-flex;">
                            <span>
                                <label for="hour" class="sr-only">Hour</label>
                                <wpsf:select list="%{getCounterArray(0, 24)}" name="hour" id="hour" cssClass="form-control"/>
                            </span>
                            <span>
                                &nbsp;:&nbsp;
                            </span>
                            <span>
                                <label for="minute" class="sr-only">Minute</label>
                                <wpsf:select list="%{getCounterArray(0, 60)}" name="minute" id="minute" cssClass="form-control"/>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

        <fieldset class="col-xs-12">
            <legend>
                <s:text name="label.mailSettings"/>
            </legend>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="senderCode">
                            <s:text name="label.senderCode"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:select name="config.senderCode" id="senderCode" list="senderCodes"
                                         cssClass="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="html">
                            <s:text name="label.html"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:checkbox name="config.html" id="html" cssClass="bootstrap-switch"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="subject">
                            <s:text name="label.subject"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:textfield name="config.subject" id="subject" cssClass="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="jpcontentworkflow_header">
                            <s:text name="label.header"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:textarea name="config.header" id="jpcontentworkflow_header" cols="50" rows="10"
                                           cssClass="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="jpcontentworkflow_template">
                            <s:text name="label.template"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:textarea name="config.template" id="jpcontentworkflow_template" cols="50" rows="10"
                                           cssClass="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <label class="col-sm-2 control-label" for="jpcontentworkflow_footer">
                            <s:text name="label.footer"/>
                        </label>
                        <div class="col-sm-10">
                            <wpsf:textarea name="config.footer" id="jpcontentworkflow_footer" cols="50" rows="10"
                                           cssClass="form-control"/>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
        <div class="col-xs-12 mb-20">
            <div class="form-group">
                <div class="col-xs-12">
                    <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="label.save"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>
