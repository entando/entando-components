<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<!-- Admin console Breadcrumbs -->
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="title.pageDesigner" /></li>
    <li>
        <s:url action="configure" namespace="/do/Page" var="configureURL">
            <s:param name="pageCode"><s:property value="currentPage.code"/></s:param>
        </s:url>
        <s:set var="configureTitle">
            <s:text name="note.goToSomewhere" />: <s:text name="title.configPage" />
        </s:set>
        <a href="${configureURL}" title="${configureTitle}"><s:text name="title.configPage" /></a>
    </li>
    <li class="page-title-container"><s:text name="name.widget" /></li>
</ol>

<!-- Page Title -->
<s:set var="dataContent" value="%{'help block'}" />
<s:set var="dataOriginalTitle" value="%{'Section Help'}"/>
<h1 class="page-title-container">
    <s:text name="name.widget" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" data-content="<s:text name="name.widget.help" />" data-placement="left" data-original-title="">
            <span class="fa fa-question-circle-o" aria-hidden="true"></span>
        </a>
    </span>
</h1>

<hr>
<div id="main" role="main">

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                <span class="pficon pficon-close"></span>
            </button>
            <span class="pficon pficon-error-circle-o"></span>
            <h3 class="h4 margin-none">
                <s:text name="message.title.FieldErrors" />
            </h3>
            <ul>
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li>
                            <s:property escapeHtml="false" />
                        </li>
                    </s:iterator>
                </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>
    <s:form action="saveConfig" namespace="/do/jpfacetnav/Page/SpecialWidget/FacetNavTree"
        cssClass="form-horizontal action-form" id="facetnav-actionform">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <div class="panel panel-default">
            <div class="panel-heading">
                <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
            </div>
            <div class="panel-body">
                <h2 class="h5 margin-small-vertical">
                    <label class="sr-only">
                        <s:text name="name.widget" />
                    </label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>
                    &#32;
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                </h2>
                <p class="sr-only">
                    <wpsf:hidden name="pageCode" />
                    <wpsf:hidden name="frame" />
                    <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
                </p>
                <div class="alert alert-info">
                  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">
                  </button>
                  <span class="pficon pficon-info"></span>
                  <s:text name="jpfacetnav.note.facetNavTree.intro" />
                </div>

                <wpsa:include
                    value="/WEB-INF/plugins/jpfacetnav/apsadmin/jsp/portal/specialwidget/include/content-type-list.jsp" />
                <wpsa:include
                    value="/WEB-INF/plugins/jpfacetnav/apsadmin/jsp/portal/specialwidget/include/facet-list.jsp" />
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 mb-20">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>