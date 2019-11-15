<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<s:set var="targetNS" value="%{'/do/collaboration/IdeaInstance'}" />
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <s:text name="jpcrowdsourcing.admin.title" />
    </li>
    <li>
        <a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />">
            <s:text name="jpcrowdsourcing.title.ideaInstanceManagement" />
        </a>
    </li>
    <s:if test="strutsAction ==  1">
        <li class="page-title-container">
            <s:text name="jpcrowdsourcing.title.ideaInstance.add" />
        </li>
    </s:if>
    <s:if test="strutsAction ==  2">
        <li class="page-title-container">
            <s:text name="jpcrowdsourcing.title.ideaInstance.edit" />
        </li>
    </s:if>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.menu"/>
                <s:if test="strutsAction ==  1">
                    <span class="pull-right">
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                            title="" data-content="<s:text name="jpcrowdsourcing.title.ideaInstance.add.help" />"
                            data-placement="left" data-original-title="">
                            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                        </a>
                    </span>
                </s:if>
                <s:if test="strutsAction ==  2">
                    <span class="pull-right">
                        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                            title="" data-content="<s:text name="jpcrowdsourcing.title.ideaInstance.edit.help" />"
                            data-placement="left" data-original-title="">
                            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                        </a>
                    </span>
                </s:if>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <li class="active">
                        <a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />">
                            <s:text name="jpcrowdsourcing.ideaInstance.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea" />">
                            <s:text name="jpcrowdsourcing.idea.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />">
                            <s:text name="jpcrowdsourcing.comment.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />">
                            <s:text name="jpcrowdsourcing.config" />
                        </a>
                    </li>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main">
    <s:form action="save" cssClass="form-horizontal">
        <p class="noscreen">
            <wpsf:hidden name="strutsAction" />
        </p>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="ideaInstance_code">
                <s:text name="label.code" />
            </label>
            <s:if test="strutsAction == 1"></s:if>
            <div class="col-sm-10">
                <wpsf:textfield name="code" id="ideaInstance_code" cssClass="form-control" readonly="strutsAction != 1" />
            </div>
        </div>
        <s:if test="strutsAction == 2">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="createdat">
                    <s:text name="label.createdat" />
                </label>
                &#32;
                <div class="col-sm-10">
                    <s:date name="createdat" format="dd/MM/yyyy" />
                    <wpsf:hidden name="createdat" />
                </div>
            </div>
        </s:if>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="groupName">
                <s:text name="label.join" />
                &#32;
                <s:text name="label.group" />
            </label>
            <div class="col-sm-10">
                <div class="input-group">
                    <wpsf:select name="groupName" id="groupName" list="systemGroups" listKey="name" listValue="descr"
                        cssClass="form-control" />
                    <span class="input-group-btn">
                        <wpsf:submit action="joinGroup" type="button" cssClass="btn btn-primary">
                            <s:text name="label.join" />
                        </wpsf:submit>
                    </span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <s:if test="null == groups || groups.size==0">
                    <div class="alert alert-info">
                      <span class="pficon pficon-info"></span>
                      <s:text name="jpcrowdsourcing.ideaInstance.label.noGroups" />
                    </div>
                </s:if>
                <s:else>
                    <ul class="list-inline">
                        <s:iterator value="groups" var="group_name">
                            <wpsf:hidden name="groups" value="%{#group_name}" />
                            <li>
                                <span class="label label-info">
                                    <s:property value="%{getSystemGroup(#group_name).getDescr()}" />
                                    <wpsa:actionParam action="removeGroup" var="actionName">
                                        <wpsa:actionSubParam name="groupName" value="%{#group_name}" />
                                    </wpsa:actionParam>
                                    <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #group_name}" cssClass="btn btn-link">
                                        <span class="pficon pficon-close white"></span>
                                        <span class="sr-only">x</span>
                                    </wpsf:submit>
                                </span>
                            </li>
                        </s:iterator>
                    </ul>
                </s:else>
            </div>
        </div>
        <div class="col-md-12">
            <div class="form-group pull-right">
                <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                    <s:text name="%{getText('label.save')}" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
