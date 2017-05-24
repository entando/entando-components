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
                   data-content="<s:text name="jpversioning.content.finding.help"/>" data-placement="left" data-original-title="">
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

        <div class="searchPanel form-group">
            <label for="descr" class="sr-only">
                <s:text name="label.search.by"/>&#32;<s:text name="label.description"/>
            </label>

            <div class="well col-md-offset-3 col-md-6 form-horizontal">
                <p class="search-label col-sm-12"><s:text name="label.search.label"/></p>
                <div class="form-group">
                    <label for="contentType" class="control-label col-sm-3"><s:text name="jpversioning.label.description"/></label>
                    <div class="col-sm-9 has-clear">
                        <wpsf:textfield id="descr"
                                        name="descr"
                                        cssClass="form-control"
                                        title="%{getText('label.search.by')+' '+getText('label.description')}"
                                        placeholder="%{getText('label.search.label')}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="contentType" class="control-label col-sm-3" ><s:text name="label.type" /></label>
                    <div class="col-sm-9" >
                        <wpsf:select name="contentType"
                                     id="contentType"
                                     list="contentTypes"
                                     listKey="code"
                                     listValue="descr"
                                     headerKey=""
                                     headerValue="%{getText('label.all')}"
                                     cssClass="form-control" >
                        </wpsf:select>
                    </div>
                </div>
                <%--<div class="panel-group" id="accordion-markup" >--%>
                    <%--<div class="panel panel-default">--%>
                        <%--<div class="panel-heading" style="padding:0 0 10px;">--%>
                            <%--<p class="panel-title active" style="text-align: end">--%>
                                <%--<a data-toggle="collapse" data-parent="#accordion-markup" href="#collapseOne">--%>
                                    <%--<s:text name="label.search.advanced" />--%>
                                <%--</a>--%>
                            <%--</p>--%>
                        <%--</div>--%>
                        <%--<div id="collapseOne" class="panel-collapse collapse">--%>
                            <%--<div class="panel-body">--%>
                                    <%--&lt;%&ndash; Type &ndash;%&gt;--%>
                                <%----%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <div class="col-sm-12">
                    <div class="form-group">
                        <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                            <span class="sr-only"><s:text name="label.search" /></span>
                            <s:text name="label.search" />
                        </wpsf:submit>
                    </div>
                </div>
            </div>
        </div>

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
                                <th class="text-nowrap"><s:text name="jpversioning.label.description" /></th>
                                <th class="text-center text-nowrap"><s:text name="jpversioning.label.id" /></th>
                                <th class="text-center text-nowrap"><s:text name="jpversioning.label.contentType" /></th>
                                <th class="text-center text-nowrap"><s:text name="jpversioning.label.username" /></th>
                                <th class="text-center text-nowrap"><s:text name="jpversioning.label.lastVersion" /></th>
                                <th class="text-center text-nowrap"><s:text name="label.state" /></th>
                                <th class="text-center text-nowrap"><s:text name="label.actions"/></th>
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