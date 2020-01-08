<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

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

<wpsa:set name="showletConfig" value="showlet.config" />

<!-- Info Details  -->
<div class="button-bar mt-20">
    <s:action namespace="/do/Page" name="printPageDetails"
              executeResult="true" ignoreContextParams="true">
        <s:param name="selectedNode" value="currentPage.code"></s:param>
    </s:action>
</div>

<s:form action="saveConfig" cssClass="form-horizontal">

    <div class="panel panel-default mt-20">
        <div class="panel-heading">
            <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
        </div>
        <div class="panel-body">
            <h2 class="h5 margin-small-vertical">
                <label class="sr-only"><s:text name="name.widget" /></label>
                <span class="icon fa fa-puzzle-piece" title="<s:text name="name.widget" />"></span>&#32;
                <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
            </h2>

            <s:if test="hasActionErrors()">
                <div class="alert alert-danger alert-dismissable fade in">
                    <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                    <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
                    <ul class="margin-base-top">
                        <s:iterator value="actionErrors">
                            <li><s:property escapeHtml="false" /></li>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <s:if test="hasFieldErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                    <h3 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h3>
                    <ul class="margin-base-vertical">
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li><s:property escapeHtml="false" /></li>
                                </s:iterator>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>

            <s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
            <p class="sr-only">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}"/>
                <wpsf:hidden name="frameIdParam" value="%{#showletConfig.get('frameIdParam')}"/>
                <s:if test="#pageTreeStyleVar == 'request'">
                    <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
                </s:if>
            </p>

            <s:if test="%{#showletConfig.get('frameIdParam') == null}">
                <div class="panel-body">
                    <s:if test="#pageTreeStyleVar == 'request'">
                        <script type="text/javascript">
                            jQuery(function () {
                                $('.table-treegrid').treegrid(null, false);
                                $(".treeRow ").on("click", function () {
                                    $(".treeRow").removeClass("active");
                                    $(this).addClass("active").find('.subTreeToggler').prop("checked", true);
                                });
                            });
                        </script>
                    </s:if>
                    <div class="table-responsive">
                        <table id="pageTree" class="table table-bordered table-hover table-treegrid no-mb">
                            <thead>
                                <tr>
                                    <th>
                                        <s:text name="label.pageTree" />
                                        <s:if test="#pageTreeStyleVar == 'classic'">
                                            <button type="button" class="btn-no-button expand-button" id="expandAll">
                                                <span class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;
                                                <s:text name="label.expandAll" />
                                            </button>
                                            <button type="button" class="btn-no-button" id="collapseAll">
                                                <span class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></span>&#32;
                                                <s:text name="label.collapseAll" />
                                            </button>
                                        </s:if>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <s:set var="inputFieldName" value="'pageCodeParam'" />
                                <s:set var="selectedTreeNode" value="%{#showletConfig.get('pageCodeParam')}" />
                                <s:set var="liClassName" value="'page'" />
                                <s:set var="treeItemIconName" value="'fa-folder'" />
                                <wpsa:groupsByPermission permission="managePages" var="groupsByPermission" />
                                <s:if test="#pageTreeStyleVar == 'classic'">
                                    <s:set var="currentRoot" value="allowedTreeRootNode" />
                                    <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
                                </s:if>
                                <s:elseif test="#pageTreeStyleVar == 'request'">
                                    <s:set var="treeNodeActionMarkerCode" value="treeNodeActionMarkerCode" />
                                    <s:set var="targetNode" value="%{parentPageCode}" />
                                    <s:set var="treeNodesToOpen" value="treeNodesToOpen" />
                                    <s:set var="currentRoot" value="showableTree"/>
                                <style>
                                    .table-treegrid span.collapse-icon, .table-treegrid span.expand-icon {
                                        cursor: pointer;
                                        display: none;
                                    }
                                </style>
                                <s:include value="/WEB-INF/plugins/jpwidgetutils/apsadmin/jsp/portal/specialwidget/replicator/treeBuilder-request-page.jsp" />
                            </s:elseif>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit action="browseFrames" type="button" cssClass="btn btn-primary btn-block">
                                <s:text name="label.continue" />
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </s:if>
            <s:else>
                <div class="panel-body">
                    <fieldset class="margin-large-top">
                        <legend><s:text name="title.configuration" /></legend>
                        <s:set var="targetPage" value="%{getPage(#showletConfig.get('pageCodeParam'))}" />
                        <input type="hidden" name="pageCodeParam" value="<s:property value="%{#targetPage.code}" />" />
                        <div class="input-group">
                            <strong><s:text name="label.selectedPage" />:</strong><br />
                            <s:iterator value="langs" status="rowStatus">
                                <s:if test="#rowStatus.index != 0">, </s:if><span class="monospace">(<abbr title="<s:property value="descr" />"><s:property value="code" /></abbr>)</span>&#32;<s:property value="#targetPage.getTitles()[code]" />
                            </s:iterator>
                            &#32;
                            <span class="input-group-btn">
                                <wpsf:submit action="reset" value="%{getText('label.reconfigure')}" cssClass="btn btn-info" />
                            </span>
                        </div>
                        <div class="input-group">
                            <strong><s:text name="label.selectedFrame" />:</strong><br />
                            <s:set var="targetFrame"  value="%{#showletConfig.get('frameIdParam')}" />
                            <s:set var="targetShowlet" value="#targetPage.getShowlets()[(#targetFrame - 0)]" />
                            <s:property value="#targetFrame"/> &ndash;
                            <s:property value="#targetPage.getModel().getFrames()[(#targetFrame - 0)]"/> &ndash;
                            <s:if test="%{#targetShowlet != null}">
                                <s:property value="getTitle(#targetShowlet.type.code, #targetShowlet.type.titles)"/>
                            </s:if>
                            <s:else>
                                <s:text name="note.emptyFrame"></s:text>
                            </s:else>
                            &#32;
                            <span class="input-group-btn">
                                <wpsf:submit action="browseFrames" value="%{getText('label.browseFrames')}" cssClass="btn btn-info" />
                            </span>
                        </div>
                    </fieldset>
                </div>
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                        <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                            <span class="icon fa fa-floppy-o"></span>&#32;
                            <s:text name="label.save" />
                        </wpsf:submit>
                    </div>
                </div>
            </s:else>
        </div>
    </div>

</s:form>

