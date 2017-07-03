<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

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
        <s:text name="jpcrowdsourcing.title.idea.delete" />
    </li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpcrowdsourcing.admin.title" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
           data-content="<s:text name="jpcrowdsourcing.title.idea.delete.help" />" data-placement="left"
           data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main" role="main">
    <s:form action="delete">
        <p class="noscreen">
            <wpsf:hidden name="strutsAction" value="%{strutsAction}" />
            <wpsf:hidden name="ideaId" value="%{ideaId}" />
        </p>

        <s:set var="idea" value="%{getIdea(ideaId)}" />
        <table class="table table-bordered no-mb">
            <tr>
                <th class="text-right table-w-10">
                    <s:text name="jpcrowdsourcing.label.title" />
                </th>
                <td>
                    <s:property value="#idea.title" />
                </td>
            </tr>
            <tr>
                <th class="text-right table-w-10">
                    <s:text name="jpcrowdsourcing.label.description" />
                </th>
                <td>
                    <s:property value="#idea.descr" />
                </td>
            </tr>
            <tr>
                <th class="text-right table-w-10">
                    <s:text name="label.state" />
                </th>
                <s:if test="%{#idea.status == 3}">
                    <s:set var="iconImage">icon fa fa-check text-success</s:set>
                    <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                </s:if>
                <s:if test="%{#idea.status == 2}">
                    <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                    <s:set var="isOnlineStatus" value="%{getText('jpcrowdsourcing.label.status_to_approve.singular')}" />
                </s:if>
                <s:if test="%{#idea.status == 1}">
                    <s:set var="iconImage">icon fa fa-pause text-warning</s:set>
                    <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                </s:if>
                <td>
                    <span class="${iconImage}" title="${isOnlineStatus}"></span>
                </td>
            </tr>
            <tr>
                <th class="text-right col-xs-2">
                    <s:text name="jpcrowdsourcing.label.author" />
                </th>
                <td><s:property value="#idea.username" /></td>
            </tr>
        </table>
        <div class="text-center mt-20">
            <i class="fa fa-exclamation esclamation-big" aria-hidden="true"></i>
            <p class="esclamation-underline">
                <s:text name="label.delete" />
            </p>
            <p>
                <s:text name="jpcrowdsourcing.note.idea.areYouSureDelete" />
            </p>
            <a class="btn btn-default button-fixed-width" href="<s:url action="list" />">
                <s:text name="%{getText('label.back')}" />
            </a>
            <wpsf:submit type="button" cssClass="btn btn-danger button-fixed-width">
                <s:text name="%{getText('label.remove')}" />
            </wpsf:submit>
        </div>
    </s:form>
</div>
