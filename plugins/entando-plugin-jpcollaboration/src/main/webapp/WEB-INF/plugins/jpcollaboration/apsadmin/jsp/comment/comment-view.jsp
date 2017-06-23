<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>
<%@ taglib prefix="jpcrowdsourcing" uri="/jpcrowdsourcing-apsadmin-core"%>

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
        <s:text name="jpcrowdsourcing.title.comments" />
    </li>
    <li class="page-title-container">
        <s:text name="jpcrowdsourcing.title.comments.view" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.title" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:text name="jpcrowdsourcing.title.comments.view.help" />" data-placement="left"
                        data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
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
    </div>
</div>
<div id="main">
    <h2 class="margin-more-bottom"></h2>
    <s:set var="statusMap"
        value="#{
           1:'jpcrowdsourcing.label.status_not_approved',
               2:'jpcrowdsourcing.label.status_to_approve',
               3:'jpcrowdsourcing.label.status_approved'
           }" />
    <s:form action="changeStatus">
        <p class="noscreen">
            <wpsf:hidden name="strutsAction" value="%{strutsAction}" />
            <wpsf:hidden name="commentId" value="%{commentId}" />
        </p>
        <s:set var="comment" value="%{getComment(commentId)}" />
        <jpcrowdsourcing:idea key="%{#comment.ideaId}" var="idea" />
        <div class="table-responsive">
            <table class="table table-bordered table-hover no-mb">
                <tr>
                    <th class="text-right table-w-10">
                        <s:text name="jpcrowdsourcing.label.author" />
                    </th>
                    <td>
                        <s:property value="#comment.username" />
                    </td>
                </tr>
                <tr>
                    <th class="text-right table-w-10">
                        <s:text name="jpcrowdsourcing.label.description" />
                    </th>
                    <td>
                        <s:property value="#comment.comment" />
                    </td>
                </tr>
                <tr>
                    <th class="text-right table-w-10">
                        <s:text name="jpcrowdsourcing.label.date" />
                    </th>
                    <td>
                        <s:date name="#comment.creationDate" />
                    </td>
                </tr>
                <tr>
                    <th class="text-right table-w-10">
                        <s:text name="label.state" />
                    </th>
                    <s:if test="#comment.status == 3">
                        <s:set var="iconImage">icon fa fa-check text-success</s:set>
                        <s:set var="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_approved.singular')}" />
                    </s:if>
                    <s:if test="#comment.status == 2">
                        <s:set var="iconImage">icon fa fa-adjust</s:set>
                        <s:set var="isOnlineStatus"
                            value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
                    </s:if>
                    <s:if test="#comment.status == 1">
                        <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                        <s:set var="isOnlineStatus"
                            value="%{getText('jpcrowdsourcing.label.status_not_approved.singular')}" />
                    </s:if>
                    <td class="col-sm-10">
                        <span title="<s:property value="isOnlineStatus"/>" class="<s:property value="iconImage"/>"></span>
                    </td>
                </tr>
            </table>
        </div>
        <s:iterator value="#statusMap" var="entry">
            <s:if test="#comment.status != #entry.key">
                <s:url action="changeStatus" var="changeStatusAction">
                    <s:param name="commentId" value="#comment.id" />
                    <s:param name="status" value="#entry.key" />
                </s:url>
                <s:if test="#entry.key == 1">
                    <s:set var="label" value="%{getText('jpcrowdsourcing.label.suspend')}" />
                    <s:set var="btnClass" value="%{'btn btn-warning'}" />
                    <s:set var="btnIcon" value="%{'icon fa fa-pause'}" />
                </s:if>
                <s:if test="#entry.key == 3">
                    <s:set var="label" value="%{getText('jpcrowdsourcing.label.approve')}" />
                    <s:set var="btnClass" value="%{'btn btn-success'}" />
                    <s:set var="btnIcon" value="%{'icon fa fa-check'}" />
                </s:if>
            </s:if>
        </s:iterator>
        <%--
        <p class="centerText">
            <a class="<s:property value="btnClass"/>"
                href="<s:property value="#changeStatusAction" escapeHtml="false" />">
                <span class="<s:property value="btnIcon"/>"></span>
                &#32;
                <s:property value="#label" />
            </a>
        </p>
         --%>
        <div class="mt-20">
            <legend>
                <s:text name="jpcrowdsourcing.label.referring.idea" />
            </legend>
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover no-mb">
                    <thead>
                        <tr>
                            <th class="table-w-20">
                                <s:text name="jpcrowdsourcing.label.title" />
                            </th>
                            <th>
                                <s:text name="jpcrowdsourcing.label.description" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <s:url action="edit" namespace="/do/collaboration/Idea" var="editIdeaActionUrl">
                                    <s:param name="ideaId" value="#idea.id" />
                                </s:url>
                                <a href="<s:property value="#editIdeaActionUrl" />">
                                    <s:property value="#idea.title" />
                                </a>
                            </td>
                            <td>
                                <s:property value="#idea.descr" />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </s:form>
</div>
