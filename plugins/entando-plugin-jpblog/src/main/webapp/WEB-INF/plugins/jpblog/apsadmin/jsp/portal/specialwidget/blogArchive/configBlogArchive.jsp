<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="viewTree" namespace="/do/Page" />" 
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>&#32;/&#32;
        <s:text name="title.configPage" />
    </span>
</h1>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="configListViewer" namespace="/do/jpblog/Page/SpecialWidget/BlogArchiveViewer">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
            </p>

            <s:if test="hasFieldErrors()">
                <div class="alert alert-danger alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                    <h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>
                    <ul class="margin-base-vertical">
                        <s:iterator value="fieldErrors">
                            <s:iterator value="value">
                                <li><s:property escape="false" /></li>
                                </s:iterator>
                            </s:iterator>
                    </ul>
                </div>
            </s:if>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <h2 class="h5 margin-small-vertical">
                        <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                        <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                    </h2>
                    <s:if test="contentTypes.isEmpty()">
                        <div class="alert alert-info">
                            <s:text name="jpblog.error.noContentTypeAvailable" />
                        </div>
                    </s:if>
                    <s:elseif test="showlet.config['contentType'] == null">
                        <%-- SELEZIONE DEL TIPO DI CONTENUTO --%>
                        <fieldset class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label for="contentType"><s:text name="label.type"/></label>
                                <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="form-control" />
                            </div>
                        </fieldset>
                    </s:elseif>
                    <s:else>

                        <fieldset class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label for="contentType"><s:text name="label.type"/>:</label>
                                <div class="input-group">
                                    <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="form-control" />
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" action="changeContentType" cssClass="btn btn-default">
                                            <s:text name="%{getText('label.change')}"/>
                                        </wpsf:submit>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                    </s:else>
                </div>
            </div>
            <s:if test="showlet.config['contentType'] == null && !contentTypes.isEmpty()">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit action="configListViewer" type="button" cssClass="btn btn-primary btn-block">
                                <span class="icon fa fa-floppy-o"></span>&#32;
                                <s:text name="%{getText('label.save')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </s:if>                    
        </s:form>
    </div>
</div>