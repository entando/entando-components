<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="jpcrowdsourcing" uri="/jpcrowdsourcing-apsadmin-core"%>

<jpcrowdsourcing:idea key="%{#comments.ideaId}" var="idea" />
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
        <s:text name="jpcrowdsourcing.title.comments" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.title" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:text name="jpcrowdsourcing.title.comments.help"/>" data-placement="left"
                        data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />">
                            <s:text name="jpcrowdsourcing.ideaInstance.list" />
                        </a>
                    </li>
                    <li>
                        <a href="<s:url action="list" namespace="/do/collaboration/Idea" />">
                            <s:text name="jpcrowdsourcing.idea.list" />
                        </a>
                    </li>
                    <li class="active">
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
<div id="main">
    <s:form action="search" cssClass="form-horizontal">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <s:set var="statusMap"
            value="#{
               1:'jpcrowdsourcing.label.status_not_approved_m',
                   2:'jpcrowdsourcing.label.status_to_approve_m',
                   3:'jpcrowdsourcing.label.status_approved_m'
               }" />
        <div class="searchPanel form-group">
            <div class="well col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-offset-1 col-sm-10">
                <p class="search-label">
                    <s:text name="label.search.label" />
                </p>
                <div class="form-group">
                    <label class="col-sm-2 control-label">
                        <s:text name="label.code" />
                    </label>
                    <div class="col-sm-9">
                        <wpsf:textfield name="commentText" id="commentText" cssClass="form-control" placeholder="%{getText('label.code')}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="status" class="control-label col-sm-2">
                        <s:text name="label.state" />
                    </label>
                    <div class="col-sm-9" id="content_list-changeContentType">
                        <wpsf:select cssClass="form-control" name="searchStatus" id="status"
                            list="%{#statusMap}" headerKey="" listValue="%{getText(value)}"
                            headerValue="%{getText('jpcrowdsourcing.label.anystatus_m')}" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-9 col-sm-offset-2">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <s:text name="%{getText('label.search')}" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>
    </s:form>
    <div class="subsection-light">
        <s:form action="search">
            <s:hidden name="_csrf" value="%{csrfToken}"/>
            <p class="noscreen">
                <wpsf:hidden name="commentText" />
                <wpsf:hidden name="searchStatus" />
            </p>
            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert">
                        <span class="icon fa fa-times"></span>
                    </button>
                    <h2 class="h4 margin-none">
                        <s:text name="message.title.ActionErrors" />
                    </h2>
                    <ul class="margin-base-vertical">
                        <s:iterator value="ActionErrors">
                            <li>
                                <s:property escapeHtml="false" />
                            </li>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>
            <s:if test="hasActionMessages()">
                <div class="alert alert-info alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert">
                        <span class="icon fa fa-times"></span>
                    </button>
                    <h2 class="h4 margin-none">
                        <s:text name="messages.confirm" />
                    </h2>
                    <ul class="margin-base-vertical">
                        <s:iterator value="actionMessages">
                            <li>
                                <s:property escapeHtml="false" />
                            </li>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>
                <s:if test="%{getComments().size() > 0}">
                    <wpsa:subset source="comments" count="10" objectName="groupComment" advanced="true" offset="5">
                        <s:set var="group" value="#groupComment" />
                            <table class="table table-striped table-bordered table-hover no-mb">
                                <thead>
                                    <tr>
                                        <th>
                                            <s:text name="jpcrowdsourcing.label.description" />
                                        </th>
                                        <th class="table-w-20">
                                            <s:text name="jpcrowdsourcing.label.author" />
                                        </th>
                                        <th class="text-center table-w-10">
                                            <s:text name="jpcrowdsourcing.label.date" />
                                        </th>
                                        <th class="text-center table-w-5">
                                            <s:text name="jpcrowdsourcing.label.approved" />
                                        </th>
                                        <th class="text-center table-w-5">
                                            <s:text name="label.actions" />
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:iterator var="commentId">
                                        <s:set var="comment" value="%{getComment(#commentId)}" />
                                        <tr>
                                            <td>
                                                <s:property value="#comment.title" />
                                                <s:set var="currentComment" value="%{#comment.comment.trim()}" />
                                                <s:if test="%{#currentComment.length()>80}">
                                                    <s:property value="%{#currentComment.substring(0,80)}" />&hellip;
                                                </s:if>
                                                <s:else>
                                                    <s:property value="%{#currentComment}" />
                                                </s:else>
                                            </td>
                                            <td>
                                                <s:property value="#comment.username" />
                                            </td>
                                            <td class="text-center text-nowrap">
                                                <s:date name="#comment.creationDate" format="dd/MM/yyyy HH:mm" />
                                            </td>
                                            <td class="text-center">
                                                <s:if test="#comment.status == 3">
                                                    <s:set var="iconImage">icon fa fa-check text-success</s:set>
                                                    <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                                                </s:if>
                                                <s:if test="#comment.status == 2">
                                                    <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                                                    <s:set var="isOnlineStatus"
                                                        value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
                                                </s:if>
                                                <s:if test="#comment.status == 1">
                                                    <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                                                    <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                                                </s:if>
                                                <span class="<s:property value="iconImage" />"
                                                    title="<s:property value="isOnlineStatus" />"></span>
                                            </td>
                                            <td class="text-center">
                                                <div class="dropdown dropdown-kebab-pf">
                                                    <span class="btn btn-menu-right dropdown-toggle" type="button"
                                                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                        <span class="fa fa-ellipsis-v"></span>
                                                    </span>
                                                    <ul class="dropdown-menu dropdown-menu-right">
                                                        <li>
                                                            <a title="<s:text name="jpcrowdsourcing.title.comments.view" />"
                                                                href="<s:url action="view"><s:param name="commentId" value="#comment.id" /></s:url>">
                                                                <s:text name="jpcrowdsourcing.title.comments.view" />
                                                            </a>
                                                        </li>
                                                        <s:iterator value="#statusMap" var="entry">
                                                            <s:if test="#comment.status != #entry.key">
                                                                <s:url action="changeStatus" var="changeStatusAction">
                                                                    <s:param name="commentId" value="#comment.id" />
                                                                    <s:param name="status" value="#entry.key" />
                                                                </s:url>
                                                                <li>
                                                                    <a title="<s:text name="jpcrowdsourcing.status.approved" />. <s:text name="jpcrowdsourcing.label.suspend" />"
                                                                        href="<s:property value="#changeStatusAction" escapeHtml="false" />">
                                                                   <s:if test="#entry.key == 1">
                                                                        <s:text name="jpcrowdsourcing.label.suspend" />
                                                                    </s:if>
                                                                    <s:if test="#entry.key == 3">
                                                                        <s:text name="jpcrowdsourcing.label.approve" />
                                                                    </s:if>
                                                                    </a>
                                                                </li>
                                                            </s:if>
                                                        </s:iterator>
                                                        <li>
                                                            <a href="<s:url action="trash"><s:param name="commentId" value="#comment.id" /></s:url>"
                                                                title="<s:text name="jpcrowdsourcing.label.delete" />">
                                                                <s:text name="jpcrowdsourcing.label.delete" />
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
                        <s:text name="jpcrowdsourcing.note.comments.empty"></s:text>
                    </div>
                </s:else>
        </s:form>
    </div>
</div>
