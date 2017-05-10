<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpversioning.admin.menu"/></li>
    <li class="page-title-container"><s:text name="title.jpversioning.resources.images" /></li>
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
                <li>
                    <a href="<s:url namespace="/do/jpversioning/Content/Versioning" action="list" />"><s:text name="jpversioning.menu.contentList"/></a>
                </li>
                <li class="active">
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
    <div class="subsection-light">
        <s:form action="search" cssClass="form-horizontal">
            <p class="sr-only">
                <wpsf:hidden name="resourceTypeCode" />
            </p>

            <s:include value="/WEB-INF/plugins/jpversioning/apsadmin/jsp/common/searchForm.jsp">
                <s:param name="hasAdvancedFilters" value="false"/>
            </s:include>

            <wpsa:subset source="trashedResources" count="10" objectName="groupResource" advanced="true" offset="5">
                <s:set var="group" value="#groupResource" />

                <div class="row">
                    <s:iterator var="resourceid">
                        <s:set var="resource" value="%{getTrashedResource(#resourceid)}" />
                        <div class="col-sm-4 col-md-3">
                            <div class="panel panel-default text-center">
                                <s:set var="url0" >
                                    <s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
                                        <s:param name="resourceId" value="#resourceid"/>
                                        <s:param name="size" value="0"/>
                                        <s:param name="langCode" value=""/>
                                    </s:url>
                                </s:set>

                                <s:set var="url1" >
                                    <s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
                                        <s:param name="resourceId" value="#resourceid"/>
                                        <s:param name="size" value="1"/>
                                        <s:param name="langCode" value=""/>
                                    </s:url>
                                </s:set>

                                <div class="panel-heading text-right padding-small-vertical padding-small-right">
                                    <a href="<s:url action="remove" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" class="icon fa fa-times-circle text-warning">
                                        <span class="sr-only">Delete</span>
                                    </a>
                                </div>

                                <div>
                                        <%-- Dimension forced for img thumbnail --%>
                                    <img src="<c:out value="${url1}" escapeXml="false"/>" alt=" " style="height:90px;max-width:130px" class="margin-small-top" />
                                </div>

                                <div class="btn-group margin-small-vertical">
                                    <a href="<s:url action="restore" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="jpversioning.label.restore" />"
                                       class="btn btn-default"
                                       title="<s:text name="jpversioning.label.restore" />: <s:property value="#resource.descr" />">
                                        <span class="icon fa fa-reply"></span>&#32;
                                        <s:text name="jpversioning.label.restore" />
                                    </a>

                                    <button type="button" class="btn btn-info" data-toggle="popover">
                                        <span class="icon fa fa-info"></span>
                                        <span class="sr-only"><s:text name="label.info" /></span>
                                    </button>
                                </div>

                                <s:set var="fileInfo">
                                    <s:property value="#resource.descr" />
                                </s:set>

                                <script>
                                    $("[data-toggle=popover]").popover({
                                        html: true,
                                        placement: "top",
                                        content: '<s:property value="fileInfo" escapeHtml="false" />'
                                    });
                                </script>

                            </div>
                        </div>

                    </s:iterator>
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
        </s:form>
    </div>
</div>