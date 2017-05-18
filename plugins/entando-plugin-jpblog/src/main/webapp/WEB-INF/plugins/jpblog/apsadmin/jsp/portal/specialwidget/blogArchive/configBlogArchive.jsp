<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li class="page-title-container"><s:text name="title.pageDesigner" /></li>
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li><s:text name="jpblog.admin.menu" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpblog.config" />
    <span class="pull-right">
        <a tabindex="0"
           role="button"
           data-toggle="popover"
           data-trigger="focus"
           data-html="true"
           title=""
           data-content="TO be inserted"
           data-placement="left"
           data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>

<br>

<div id="main">

    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="configListViewer" namespace="/do/jpblog/Page/SpecialWidget/BlogArchiveViewer">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
            </p>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <legend>
                        <span class="control-label label-group-name">
                            <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                            <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                        </span>
                    </legend>

                    <s:if test="contentTypes.isEmpty()">
                        <div class="alert alert-info">
                            <s:text name="jpblog.error.noContentTypeAvailable" />
                        </div>
                    </s:if>
                    <s:elseif test="showlet.config['contentType'] == null">
                        <%-- SELEZIONE DEL TIPO DI CONTENUTO --%>
                        <div class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="contentType">
                                    <s:text name="label.type" />
                                </label>
                                <div class="col-sm-10 input-group">
                                    <wpsf:select name="contentType"
                                                 id="contentType"
                                                 list="contentTypes"
                                                 listKey="code"
                                                 listValue="descr"
                                                 cssClass="form-control" />
                                </div>
                            </div>
                        </div>
                    </s:elseif>
                    <s:else>
                        <div class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="contentType">
                                    <s:text name="label.type" />
                                </label>
                                <div class="col-sm-10 input-group">
                                    <wpsf:select name="contentType"
                                                 id="contentType"
                                                 list="contentTypes"
                                                 listKey="code"
                                                 listValue="descr"
                                                 disabled="true"
                                                 value="%{getShowlet().getConfig().get('contentType')}"
                                                 cssClass="form-control" />
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" action="changeContentType" cssClass="btn btn-primary">
                                            <s:text name="%{getText('label.change')}"/>
                                        </wpsf:submit>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </s:else>
                </div>
            </div>
            <s:if test="showlet.config['contentType'] == null && !contentTypes.isEmpty()">
                <div class="form-horizontal">
                    <div class="form-group pull-right">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit action="configListViewer" type="button" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.save')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </s:if>
        </s:form>
    </div>
</div>