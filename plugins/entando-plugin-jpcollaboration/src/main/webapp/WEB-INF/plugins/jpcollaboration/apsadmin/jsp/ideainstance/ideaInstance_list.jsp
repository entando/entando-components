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
    <li class="page-title-container">
        <s:text name="jpcrowdsourcing.title.ideaInstanceManagement" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.title" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:text name="jpcrowdsourcing.title.ideaInstanceManagement.help" />"
                        data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
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
    <s:form action="list" cssClass="form-horizontal">
        <div class="searchPanel form-group">
            <div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10">
                <p class="search-label">
                    <s:text name="label.search.label" />
                </p>
                <div class="form-group">
                    <c:set var="pholder">
                        <s:text name="label.code" />
                    </c:set>
                    <label class="col-sm-2 control-label">${pholder}</label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="code" id="code" cssClass="form-control" placeholder="${pholder}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="groupName" class="control-label col-sm-2 text-right">
                        <s:text name="label.group.search" />
                    </label>
                    <div class="col-sm-9">
                        <wpsf:select name="groupName" id="groupName" list="systemGroups" listKey="name"
                            listValue="descr" cssClass="form-control" headerKey=""
                            headerValue="%{getText('label.all')}" />
                    </div>
                </div>
                 <div class="form-group">
                    <div class="col-sm-9 col-sm-offset-2">
                        <wpsf:submit action="search" type="button" cssClass="btn btn-primary pull-right"
                            title="%{getText('label.search')}">
                            <s:text name="label.search"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </s:form>
    <a class="btn btn-primary margin-base-bottom pull-right" href="<s:url action="new" />">
        <s:text name="label.add" />
    </a>
    <div class="col-xs-12 no-padding">
        <s:form action="search">
            <div class="subsection-light">
                <p class="noscreen">
                    <s:hidden name="groupName" />
                </p>
                <s:if test="%{ideaInstancesId.size() > 0}">
                <wpsa:subset source="ideaInstancesId" count="10" objectName="groupIdeaInstances" advanced="true"
                    offset="5">
                    <s:set var="group" value="#groupIdeaInstances" />
                    <table class="table table-striped table-bordered table-hover no-mb">
                        <thead>
                            <tr>
                                <th>
                                    <s:text name="label.code" />
                                </th>
                                <th class="text-center table-w-10">
                                    <s:text name="label.idea.count" />
                                </th>
                                <th class="text-center table-w-10">
                                    <s:text name="label.createdat" />
                                </th>
                                <th class="text-center table-w-5">
                                    <s:text name="label.actions" />
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <s:iterator var="code">
                                <s:set var="ideaInstance_var" value="%{getIdeaInstance(#code)}" />
                                <tr>
                                    <td><s:property value="#ideaInstance_var.code" /></td>
                                    <td class="text-center">
                                        <s:property value="#ideaInstance_var.children.size" />
                                    </td>
                                    <td class="text-center"><s:date name="#ideaInstance_var.createdat" format="dd/MM/yyyy HH:mm" /></td>
                                    <td class="table-view-pf-actions text-center">
                                        <div class="dropdown dropdown-kebab-pf">
                                            <button class="btn btn-menu-right dropdown-toggle" type="button"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <li>
                                                    <a
                                                        title="<s:text name="label.edit" />: <s:property value="#ideaInstance_var.code" />"
                                                        href="<s:url action="edit"><s:param name="code" value="#ideaInstance_var.code"/></s:url>">
                                                        <s:text name="label.edit" />
                                                    </a>
                                                </li>
                                                <s:if test="%{#ideaInstance_var.children.size > 0}">
                                                <li>
                                                    <s:url action="list" namespace="/do/collaboration/Idea"
                                                        var="link_to_idea_list">
                                                        <s:param name="ideaInstance" value="#ideaInstance_var.code" />
                                                    </s:url>
                                                    <a href="<s:property value="#link_to_idea_list"/>">
                                                        <s:text name="jpcrowdsourcing.ideaInstance.ideaList" />
                                                    </a>
                                                </li>
                                                </s:if>
                                                <li>
                                                    <a
                                                        href="<s:url action="trash"><s:param name="code" value="#ideaInstance_var.code"></s:param></s:url>"
                                                        title="<s:text name="label.remove" />: <s:property value="#ideaInstance_var.code"/>">
                                                        <s:text name="label.remove" />
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </td>
                                </tr>
                            </s:iterator>
                        </tbody>
                    </table>
                    <div class="content-view-pf-pagination table-view-pf-pagination clearfix mb-20">
                        <div class="form-group">
                            <span><s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                            <div class="mt-5">
                                <s:include
                                    value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
                </s:if>
                <s:else>
                    <div class="alert alert-info">
                      <span class="pficon pficon-info"></span>
                      <s:text name="jpcrowdsourcing.label.ideaInstance.noInstances"/>
                    </div>
                </s:else>
            </div>
        </s:form>
    </div>
</div>
