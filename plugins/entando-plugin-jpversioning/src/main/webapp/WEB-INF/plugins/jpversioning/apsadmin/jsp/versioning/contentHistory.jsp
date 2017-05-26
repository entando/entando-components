<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="contentVersionsInfo" value="%{getContentVersion(versionId)}" />
<s:set var="id" value="contentId" />

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li>
        <a href="<s:url action="list" namespace="do/jpversioning/Content/Versioning" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpversioning.content" />">
            <s:text name="jpversioning.admin.menu" /></a>
    </li>
    <li class="page-title-container"><s:text name="title.jpversioning.content" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="page-title-container">
                <s:text name="jpversioning.admin.menu"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpversioning.admin.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>

<br/>

<div id="main">
    <s:form action="history" >
    <wpsa:subset source="contentVersions" count="10" objectName="groupContent" advanced="true" offset="5">
        <s:set var="group" value="#groupContent" />

    <div class="pager">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
    </div>

    <p class="sr-only">
        <s:hidden name="versionId" />
        <s:hidden name="backId" />
        <s:hidden name="fromEdit" />
    </p>

    <div class="col-xs-12 no-padding">
        <table class="table table-striped table-bordered table-hover no-mb">
            <caption class="sr-only"><s:text name="title.jpversioning.versionList" /></caption>
            <thead>
            <tr>
                <th class="text-center"><s:text name="jpversioning.version.full" /></th>
                <th style="width: 240px"><s:text name="jpversioning.label.description" /></th>
                <th class="text-center col-sm-1"><s:text name="jpversioning.label.lastVersion" /></th>
                <th class="text-center"><s:text name="jpversioning.label.username" /></th>
                <th class="text-center"><s:text name="label.state" /></th>
                <th class="text-center col-sm-1"><s:text name="label.actions" /></th>
            </tr>
            </thead>

            <tbody>
            <s:iterator var="id" status="statusIndex">
                <s:set var="contentVersion" value="%{getContentVersion(#id)}" />
                <tr>
                    <td class="text-center col-sm-2"><code><s:property value="#contentVersion.version" /></code></td>
                    <td>
                        <s:set var="fileNameVar" value="#contentVersion.descr"/>
                        <s:if test='%{#fileNameVar.length()>43}'>
                            <s:set var="fileNameVar" value='%{#fileNameVar.substring(0,40)+"..."}'/>
                            <s:property value="#fileNameVar"/>
                        </s:if>
                        <s:else>
                            <s:property value="#fileNameVar"/>
                        </s:else>
                    </td>
                    <td class="text-center col-sm-2"><code><s:date name="#contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></code> </td>
                    <td class="text-center"><s:property value="#contentVersion.username" /></td>
                    <td class="text-center">
                        <s:if test="(#contentVersion.status == 'PUBLIC')">
                            <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                            <span class="fa fa-circle green" aria-hidden="true" title="${isOnlineStatus}"></span>
                        </s:if>
                        <s:if test="(#contentVersion.status != 'PUBLIC')">
                            <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                            <span class="fa fa-circle yellow" aria-hidden="true" title="${isOnlineStatus}"></span>
                        </s:if>
                    </td>
                    <td class="text-center">
                        <div class="dropdown dropdown-kebab-pf">
                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                <span class="fa fa-ellipsis-v"></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                                <li>
                                    <a href="<s:url action="preview" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>">
                                        <s:text name="label.view" />
                                    </a>
                                </li>
                                <li>
                                    <a href="<s:url action="entryRecover" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>">
                                        <s:text name="jpversioning.label.restore" />
                                    </a>
                                </li>
                                <li>
                                    <a href="<s:url action="trash" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>">
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

        <div class="pager">
            <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
        </div>
        </wpsa:subset>
        </s:form>
    </div>
