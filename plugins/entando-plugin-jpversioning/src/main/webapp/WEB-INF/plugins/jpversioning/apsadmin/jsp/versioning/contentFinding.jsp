<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpversioning.admin.menu"/></li>
    <li class="page-title-container"><s:text name="title.jpversioning.content" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="jpversioning.admin.menu"/>
                <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-content="TO be inserted" data-placement="left" data-original-title="">
                    <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url namespace="/do/jpversioning/Content/Versioning" action="list" />"><s:text name="jpversioning.menu.contentList"/></a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Image</s:param></s:url>" ><s:text name="jpversioning.menu.images" /></a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Attach</s:param></s:url>" ><s:text name="jpversioning.menu.attaches" /></a>
                </li>
            </ul>
        </div>
    </div>
</div>

<br/>

<div id="main">
    <s:form action="search" class="form-horizontal">

        <s:include value="/WEB-INF/plugins/jpversioning/apsadmin/jsp/common/searchForm.jsp">
            <s:param name="hasAdvancedFilters" value="true"/>
        </s:include>

        <div class="subsection-light">
            <s:if test="%{null == latestVersions || latestVersions.size == 0 }">
                <div class="alert alert-info">
                    <s:text name="message.jpversioning.noModifiedContents"></s:text>
                </div>
            </s:if>

            <s:else>
                <wpsa:subset source="latestVersions" count="10" objectName="groupContent" advanced="true" offset="5">
                    <s:set var="group" value="#groupContent" />
                    <div class="col-xs-12 no-padding">
                        <table class="table table-striped table-bordered table-hover no-mb">
                            <thead>
                            <tr>
                                <th><s:text name="jpversioning.label.description" /></th>
                                <th class="text-center"><s:text name="jpversioning.label.id" /></th>
                                <th class="text-center"><s:text name="jpversioning.label.contentType" /></th>
                                <th class="text-center"><s:text name="jpversioning.label.username" /></th>
                                <th class="text-center"><s:text name="jpversioning.label.lastVersion" /></th>
                                <th class="text-center"><s:text name="label.state" /></th>
                                <th class="text-center col-sm-1"><s:text name="label.actions"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <s:iterator var="id">
                                <s:set var="contentVersion" value="%{getContentVersion(#id)}" />
                                <tr>
                                    <td><s:property value="#contentVersion.descr" /></td>
                                    <td class="text-center text-nowrap"><code><s:property value="#contentVersion.contentId" /></code></td>
                                    <td><s:property value="%{getSmallContentType(#contentVersion.contentType).descr}" /></td>
                                    <td class="text-center"><s:property value="#contentVersion.username" /></td>
                                    <td class="text-center text-nowrap"><code><s:property value="#contentVersion.version" />&#32;(<s:date name="#contentVersion.versionDate" format="dd/MM/yyyy" />)</code></td>

                                    <s:if test="(#contentVersion.status == 'PUBLIC')">
                                        <s:set var="iconName">check</s:set>
                                        <s:set var="textVariant">success</s:set>
                                        <s:set var="isOnlineStatus" value="%{getText('label.yes')}" />
                                    </s:if>
                                    <s:if test="(#contentVersion.status != 'PUBLIC')">
                                        <s:set var="iconName">pause</s:set>
                                        <s:set var="textVariant">warning</s:set>
                                        <s:set var="isOnlineStatus" value="%{getText('label.no')}" />
                                    </s:if>
                                    <td class="text-center">
                                        <span class="icon fa fa-<s:property value="iconName" /> text-<s:property value="textVariant" />" title="<s:property value="isOnlineStatus" />"></span>
                                        <span class="sr-only"><s:property value="isOnlineStatus" /></span>
                                    </td>
                                    <td class="text-center">
                                        <div class="dropdown dropdown-kebab-pf">
                                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight"
                                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                                <span class="fa fa-ellipsis-v"></span>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownKebabRight">
                                                <li>
                                                    <a href="<s:url action="history" ><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="versionId" value="#contentVersion.id" /></s:url>">
                                                        <s:text name="label.view" />
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </td>
                                </tr>
                            </s:iterator>
                            </tbody>
                        </table>
                    </div>
                    <div class="content-view-pf-pagination clearfix">
                        <div class="form-group">
                            <span><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /></span>
                            <div class="mt-5">
                                <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formTable.jsp" />
                            </div>
                        </div>
                    </div>
                </wpsa:subset>
            </s:else>
        </div>
    </s:form>
</div>