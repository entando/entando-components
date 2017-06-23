<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpcrowdsourcing.admin.title" /></li>
    <li class="page-title-container">
        <s:text name="jpcrowdsourcing.title.idea.delete" />
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpnewsletter.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                        data-content="<s:text name="jpcrowdsourcing.title.idea.delete.help" />" data-placement="left"
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
                    <li class="active">
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
<div class="text-center">
    <s:form action="deleteReference">
        <p class="noscreen">
            <wpsf:hidden name="strutsAction" value="%{strutsAction}" />
            <wpsf:hidden name="ideaId" value="%{ideaId}" />
            <s:hidden name="selectedNode" value="%{#parameters['selectedNode']}" />
        </p>

        <s:set var="idea" value="%{getIdea(ideaId)}" />

        <table class="table table-bordered table-hover no-mb">
            <tr>
                <th class="text-right col-xs-2"><s:text
                        name="jpcrowdsourcing.label.title" /></th>
                <td class="col-xs-10"><s:property value="#idea.title" /></td>
            </tr>
            <tr>
                <th class="text-right col-xs-2"><s:text
                        name="jpcrowdsourcing.label.description" /></th>
                <td><s:property value="#idea.descr" /></td>
            <tr>
            <tr>
                <th class="text-right col-xs-2"><s:text name="label.state" /></th>
                    <s:if test="#idea.status == 3">
                        <s:set var="iconImage" id="iconImage">icon fa fa-check text-success</s:set>
                    <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                </s:if>
                <s:if test="#idea.status == 2">
                    <s:set var="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
                    <s:set var="isOnlineStatus"
                           value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
                </s:if>
                <s:if test="#idea.status == 1">
                    <s:set var="iconImage" id="iconImage">icon fa fa-pause text-warning</s:set>
                    <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                </s:if>
                <td><span class="<s:property value="iconImage" />"
                          title="<s:property value="isOnlineStatus" />"></span></td>
            </tr>
            <tr>
                <th class="text-right col-xs-2"><s:text
                        name="jpcrowdsourcing.label.author" /></th>
                <td><code>
                        <s:property value="#idea.username" />
                    </code></td>
            </tr>
        </table>

        <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
        <p class="esclamation-underline">
            <s:text name="label.delete" />
        </p>
        <p>
            <s:text name="jpcrowdsourcing.note.idea.areYouSureDelete" />
            ?
        </p>
        <div class="text-center margin-large-top">
            <s:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="%{getText('label.remove')}" />
            </s:submit>
        </div>
        <div class="text-center margin-large-top">
            <a class="btn btn-default button-fixed-width"
               href="<s:url action="list"/>"> <s:text
                    name="%{getText('label.back')}" />
            </a>
        </div>
    </s:form>
</div>
