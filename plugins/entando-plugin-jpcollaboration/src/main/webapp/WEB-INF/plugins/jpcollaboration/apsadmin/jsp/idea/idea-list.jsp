<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcrowdsourcing.admin.title" /></li>
    <li class="page-title-container"><s:text
            name="jpcrowdsourcing.title.ideaList" /></li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpcrowdsourcing.admin.title" />
                <span class="pull-right"> <a tabindex="0" role="button"
                                             data-toggle="popover" data-trigger="focus" data-html="true"
                                             title=""
                                             data-content="<s:text name="jpcrowdsourcing.title.ideaList.help" />"
                                             data-placement="left" data-original-title=""> <i
                            class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <wp:ifauthorized permission="superuser">
            <div class="col-sm-6">
                <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                    <li><a
                            href="<s:url action="list" namespace="/do/collaboration/IdeaInstance" />"><s:text
                                name="jpcrowdsourcing.ideaInstance.list" /></a></li>
                    <li class="active"><a
                            href="<s:url action="list" namespace="/do/collaboration/Idea" />"><s:text
                                name="jpcrowdsourcing.idea.list" /></a></li>
                    <li><a
                            href="<s:url action="list" namespace="/do/collaboration/Idea/Comment" />"><s:text
                                name="jpcrowdsourcing.comment.list" /></a></li>
                    <li><a
                            href="<s:url action="entryConfig" namespace="/do/collaboration/Config" />"><s:text
                                name="jpcrowdsourcing.config" /></a></li>
                </ul>
            </div>
        </wp:ifauthorized>
    </div>
</div>
<br>

<div id="main">
    <s:form action="search" cssClass="form-horizontal">
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
                        <wpsf:textfield name="text" id="text" cssClass="form-control" placeholder="${pholder}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="status">
                        <s:text name="label.state" />
                    </label>
                    <div class="col-sm-9">
                        <s:set var="statusMap"
                            value="#{
                               1:'jpcrowdsourcing.label.status_not_approved',
                                   2:'jpcrowdsourcing.label.status_to_approve',
                                   3:'jpcrowdsourcing.label.status_approved'
                               }" />
                        <wpsf:select cssClass="form-control" name="searchStatus" id="status"
                            list="%{#statusMap}" headerKey="" listValue="%{getText(value)}"
                            headerValue="%{getText('jpcrowdsourcing.label.anystatus')}" />
                    </div>
                 </div>
                 <div class="form-group">
                    <label class="col-sm-2 control-label" for="ideaInstance">
                        <s:text name="jpcrowdsourcing.label.instance" />
                    </label>
                    <div class="col-sm-9">
                        <wpsf:select cssClass="form-control" name="ideaInstance" id="ideaInstance"
                            list="ideaInstances" listKey="code" listValue="code" headerKey=""
                            headerValue="%{getText('label.all')}" />
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
    <div class="col-xs-12 no-padding">
        <s:form action="search">
            <div class="subsection-light">
                <p class="noscreen">
                    <wpsf:hidden name="text" />
                    <wpsf:hidden name="searchStatus" />
                </p>

                <div id="messages">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
                </div>

                <wpsa:subset source="ideas" count="10" objectName="groupIdea"
                             advanced="true" offset="5">
                    <s:set var="group" value="#groupIdea" />

                    <s:if test="%{getIdeas().size() > 0}">
                        <table
                            class="table table-striped table-bordered table-hover no-mb">
                            <thead>
                                <tr>
                                    <th class="table-w-20">
                                        <s:text name="jpcrowdsourcing.label.title" />
                                    </th>
                                    <th class="table-w-20">
                                        <s:text name="jpcrowdsourcing.label.instance" />
                                    </th>
                                    <th class="table-w-20">
                                        <s:text name="jpcrowdsourcing.label.author" />
                                    </th>
                                    <th class="text-center table-w-10">
                                        <s:text name="jpcrowdsourcing.label.date" />
                                    </th>
                                    <th class="text-center table-w-10">
                                        <s:text name="jpcrowdsourcing.label.comments" />
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
                                <s:iterator var="ideaId">
                                    <tr>
                                        <s:set var="idea" value="%{getIdea(#ideaId)}" />
                                        <td><s:property value="#idea.title" /></td>
                                        <td><s:property value="#idea.instanceCode" /></td>
                                        <td><s:property value="#idea.username" /></td>
                                        <td class="text-center"><s:date name="#idea.pubDate" format="dd/MM/yyyy HH:mm" /></td>
                                        <td class="text-center">
                                            <s:if test="#idea.comments.size > 0">
                                                <s:property value="#idea.comments.size" />
                                            </s:if>
                                            <s:else>
                                                <span class="monospace">
                                                    <s:text name="jpcrowdsourcing.label.zero" />
                                                </span>
                                            </s:else>
                                        </td>
                                        <td class="text-center"><s:if test="#idea.status == 3">
                                                <s:set var="iconImage">icon fa fa-check text-success</s:set>
                                                <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                                            </s:if> <s:if test="#idea.status == 2">
                                                <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                                                <s:set var="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
                                            </s:if> <s:if test="#idea.status == 1">
                                                <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                                                <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                                            </s:if> <span class="<s:property value="iconImage" />"
                                                  title="<s:property value="isOnlineStatus" />"></span>
                                        </td>
                                        <td class="text-center table-view-pf-actions">
                                            <div class="dropdown dropdown-kebab-pf">
                                                <button class="btn btn-menu-right dropdown-toggle"
                                                        type="button" data-toggle="dropdown" aria-haspopup="true"
                                                        aria-expanded="false">
                                                    <span class="fa fa-ellipsis-v"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li>
                                                        <s:url action="edit" var="editAction">
                                                            <s:param name="ideaId" value="#idea.id" />
                                                        </s:url>
                                                        <a title="<s:text name="label.edit" />: <s:property value="#idea.title" />" 
                                                            href="${editAction}">
                                                            <s:text name="label.edit" />
                                                        </a>
                                                    </li>
                                                    <s:iterator value="#statusMap" var="entry">
                                                        <s:if test="#idea.status != #entry.key">
                                                            <s:url action="changeStatus" var="changeStatusAction">
                                                                <s:param name="ideaId" value="#idea.id" />
                                                                <s:param name="status" value="#entry.key" />
                                                            </s:url>
                                                            <li>
                                                                <a href="<s:property value="#changeStatusAction" escapeHtml="false" />">
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
                                                        <a
                                                            href="<s:url action="trash"><s:param name="ideaId" value="#idea.id" /></s:url>"
                                                            title="<s:text name="jpcrowdsourcing.label.delete" />: <s:property value="#idea.title"/>">
                                                            <s:text name="jpcrowdsourcing.label.delete" />
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <s:url action="list" namespace="/do/collaboration/IdeaInstance"
                                                            var="link_to_instancelist">
                                                            <s:param name="code" value="#idea.instanceCode" />
                                                        </s:url>
                                                        <a href="${link_to_instancelist}"
                                                            title="<s:text name="jpcrowdsourcing.label.view"/>: <s:property value="#idea.instanceCode" />">
                                                            <s:text name="jpcrowdsourcing.label.instanceList" />
                                                        </a>
                                                    </li>
                                                    <s:if test="#idea.comments.size > 0">
                                                        <li>
                                                            <s:url var="commentListAction" action="list"
                                                                   namespace="/do/collaboration/Idea/Comment">
                                                                <s:param name="ideaId" value="#idea.id" />
                                                            </s:url>
                                                            <a href="${commentListAction}"
                                                                title="<s:text name="jpcrowdsourcing.label.goToComment" />">
                                                                <s:text name="jpcrowdsourcing.label.commentList" />
                                                            </a>
                                                        </li>
                                                    </s:if>
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
                    </s:if>
                    <s:else>
                        <div class="alert alert-info">
                          <span class="pficon pficon-info"></span>
                          <s:text name="jpcrowdsourcing.idea.null" />.
                        </div>
                    </s:else>
                </wpsa:subset>
            </div>
        </s:form>
    </div>
</div>
