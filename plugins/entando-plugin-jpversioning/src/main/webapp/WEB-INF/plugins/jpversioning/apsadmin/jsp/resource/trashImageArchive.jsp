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
                   data-content="<s:text name="jpversioning.admin.help"/>" data-placement="left" data-original-title="">
                    <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url namespace="/do/jpversioning/Content/Versioning" action="list" />">
                        <s:text name="jpversioning.menu.contentList"/>
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Image</s:param></s:url>" >
                        <s:text name="jpversioning.menu.images" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Attach</s:param></s:url>" >
                        <s:text name="jpversioning.menu.attaches" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>

<br/>

<div id="main">
    <div class="container-fluid subsection-light">
        <s:form action="search" cssClass="form-horizontal">
            <p class="sr-only">
                <wpsf:hidden name="resourceTypeCode" />
            </p>

            <div class="searchPanel form-group">
                <label for="descr" class="sr-only">
                    <s:text name="label.search.by"/>&#32;<s:text name="label.description"/>
                </label>

                <div class="well col-md-offset-3 col-md-6">
                    <p class="search-label col-sm-12"><s:text name="label.search.label"/></p>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><s:text name="jpversioning.label.description" /></label>
                        <div class="col-sm-10 has-clear">
                            <wpsf:textfield id="text" name="text" cssClass="form-control input-lg" title="%{getText('label.search.by')+' '+getText('label.description')}" placeholder="%{getText('jpversioning.label.description')}"/>
                        </div>
                    </div>
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
        </s:form>

        <div class="list-group list-view-pf list-view-pf-view">
            <wpsa:subset source="trashedResources" count="10" objectName="groupResource" advanced="true" offset="5">
            <s:set var="group" value="#groupResource" />
            <s:iterator var="resourceid">
                <s:set var="resource" value="%{getTrashedResource(#resourceid)}" />
                <div class="list-group-item">
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

                    <div class="list-view-pf-left col-o" style="width: 130px">
                        <img src="<c:out value="${url1}" escapeXml="false"/>" class="img-responsive center-block"/>
                    </div>

                    <div class="list-view-pf-body">
                        <div class="list-view-pf-description">
                            <div class="list-group-item-heading" style="font-size: 16px">
                                <s:property value="#resource.descr" />
                            </div>
                        </div>
                    </div>

                    <div class="list-view-pf-actions">
                        <div class="dropdown pull-right dropdown-kebab-pf">
                            <button class="btn btn-link dropdown-toggle" type="button" id="dropdownKebabRight"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                <span class="fa fa-ellipsis-v"></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-right"
                                aria-labelledby="dropdownKebabRight">
                                <li>
                                    <a href="<s:url action="restore" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>"
                                       title="<s:text name="jpversioning.label.restore" />">
                                        <s:text name="jpversioning.label.restore" />
                                    </a>
                                </li>
                                <li>
                                    <a href="<s:url action="trash" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>">
                                        <s:text name="label.delete"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
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
    </div>
</div>