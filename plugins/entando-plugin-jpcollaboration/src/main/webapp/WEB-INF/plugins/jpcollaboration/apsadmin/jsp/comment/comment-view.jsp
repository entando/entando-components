<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpcrowdsourcing" uri="/jpcrowdsourcing-apsadmin-core" %>

<h1 class="panel panel-default title-page"><span class="panel-body display-block"><s:text name="jpcrowdsourcing.admin.title" />&#32;/&#32;<s:text name="jpcrowdsourcing.title.comments.view" /></span></h1>
<div id="main">
    <h2 class="margin-more-bottom"></h2>

    <s:set var="statusMap" value="#{
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
        <jpcrowdsourcing:idea key="%{#comment.ideaId}" var="idea"/>

        <table class="table table-bordered">
            <tr>
                <th class="text-right"><s:text name="jpcrowdsourcing.label.author" /></th>
                <td><code><s:property value="#comment.username"/></code></td>        
            </tr>
            <tr>
                <th class="text-right"><s:text name="jpcrowdsourcing.label.description" /></th>	
                <td><s:property value="#comment.comment"/></td>        
            </tr>
            <tr>
                <th class="text-right"><s:text name="jpcrowdsourcing.label.date" /></th>
                <td><code><s:date name="#comment.creationDate"/></code></td>
            </tr>
            <tr>
                <th class="text-right"><s:text name="label.state" /></th>
                <s:if test="#comment.status == 3"><s:set var="iconImage">icon fa fa-check text-success</s:set><s:set var="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_approved.singular')}" /></s:if>
                <s:if test="#comment.status == 2"><s:set var="iconImage">icon fa fa-adjust</s:set><s:set var="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" /></s:if>
                <s:if test="#comment.status == 1"><s:set var="iconImage">icon fa fa-pause text-warning</s:set><s:set var="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_not_approved.singular')}" /></s:if>
                <td><span title="<s:property value="isOnlineStatus"/>" class="<s:property value="iconImage"/>"></span></td>
            </tr>
        </table>
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
        <p class="centerText">
            <a class="<s:property value="btnClass"/>" href="<s:property value="#changeStatusAction" escapeHtml="false" />" >
                <span class="<s:property value="btnIcon"/>"></span>&#32;
                <s:property value="#label" /></a>
        </p>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="margin-none"><s:text name="jpcrowdsourcing.label.referring.idea"/></h3>
            </div>
            <div class="panel-body">
                <table class="table table-bordered">
                    <tr>
                        <th class="text-right">
                            <s:url action="edit" namespace="/do/collaboration/Idea" var="editIdeaActionUrl">
                                <s:param name="ideaId" value="#idea.id" />
                            </s:url>
                            <a href="<s:property value="#editIdeaActionUrl" />">
                                <s:property value="#idea.title"/>
                            </a>
                        </th>
                        <td>
                            <s:property value="#idea.descr"/>
                        </td>
                    </tr>
                </table>        
            </div>
        </div>
    </s:form>
</div>