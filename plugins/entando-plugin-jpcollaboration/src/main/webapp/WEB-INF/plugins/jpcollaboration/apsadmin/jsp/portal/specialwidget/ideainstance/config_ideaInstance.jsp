<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/aps-core" prefix="wp"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="breadcrumb.integrations" />
    </li>
    <li>
        <s:text name="breadcrumb.integrations.components" />
    </li>
    <li>
        <s:text name="title.pageDesigner" />
    </li>
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />"
            title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="jpcrowdsourcing.admin.menu" />
    </li>
</ol>
<h1 class="page-title-container">
    <s:text name="jpblog.config" />
    <span class="pull-right">
        <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
            data-content="<s:text name="jpblog.config.help" />" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div id="messages">
    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
</div>
<div id="main" role="main">
    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
    <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="pageCode"></s:param>
    </s:action>
    <s:form action="changeInstance" namespace="/do/collaboration/Page/SpecialWidget/IdeaInstanceViewer">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
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
                <h2 class="h5 margin-small-vertical">
                    <label class="sr-only"><s:text name="name.widget" /></label>
                    <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                    <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                </h2>
                <s:if test="showlet.config['instanceCode'] == null">
                    <fieldset class="col-xs-12 no-padding">
                    <legend><s:text name="jpcrowdsourcing.ideaInstance.ideaList"/></legend>
                        <div class="form-group">
                            <label class="text-right col-sm-2 control-label" for="instanceCode">
                                <s:text name="label.type" />
                            </label>
                            <div class="col-sm-10 input-group">
                                <wpsf:select name="instanceCode" id="instanceCode" list="ideaInstances" listKey="code"
                                    listValue="code" cssClass="form-control" />
                            </div>
                        </div>
                    </fieldset>
                </s:if>
                <s:else>
                    <div class="col-xs-12 form-group">
                        <label class="col-sm-2 control-label" for="instanceCode">
                            <s:text name="jpcrowdsourcing.label.instance" />
                        </label>
                        <div class="col-sm-10 input-group">
                            <wpsf:select name="instanceCode" id="instanceCode" list="ideaInstances" listKey="code"
                                listValue="code" disabled="true" value="%{getShowlet().getConfig().get('instanceCode')}"
                                cssClass="form-control" />
                            <div class="input-group-btn">
                                <wpsf:submit type="button" cssClass="btn btn-primary" action="changeInstance">
                                    <s:text name="%{getText('label.change')}" />
                                </wpsf:submit>
                            </div>
                        </div>
                    </div>
                </s:else>
            </div>
        </div>
        <s:if test="showlet.config['instanceCode'] == null">
            <div class="form-group pull-right">
                <wpsf:submit type="button" cssClass="btn btn-primary" action="configInstance">
                    <s:text name="%{getText('label.save')}" />
                </wpsf:submit>
            </div>
        </s:if>
    </s:form>
</div>
