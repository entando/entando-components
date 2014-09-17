<%@ taglib prefix="s" uri="/struts-tags" %>
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

<p class="noscreen"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>

<div id="main">

    <s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="currentPage.code"></s:param></s:action>

        <s:form action="saveConfigParameters" namespace="/do/jpfastcontentedit/Page/SpecialWidget">
            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
                <s:if test="showlet.config['typeCode'] != null">
                    <wpsf:hidden name="typeCode" value="%{getShowlet().getConfig().get('typeCode')}" />
                </s:if>
            </p>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
                </div>
                <div class="panel-body">
                    <h2 class="h5 margin-small-vertical">
                        <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                        <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                    </h2>
                    <s:if test="showlet.config['typeCode'] == null">
                        <fieldset class="col-xs-12">
                            <legend><s:text name="label.info" /></legend>
                            <div class="form-group">
                                <label for="contentType"><s:text name="label.type"/></label>
                                <div class="input-group">
                                    <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="form-control" />
                                    <span class="input-group-btn">

                                        <wpsf:submit type="button" action="configContentType" cssClass="btn btn-info">
                                            <span class="icon fa fa-play-circle-o"></span>&#32;
                                            <s:text name="%{getText('label.continue')}"/>
                                        </wpsf:submit>
                                    </span>
                                </div>
                            </div>
                        </fieldset>
                    </s:if>
                    <s:else>
                        <fieldset class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label for="contentType"><s:text name="label.type"/></label>
                                <div class="input-group">
                                    <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" cssClass="form-control" value="%{getShowlet().getConfig().get('typeCode')}"/>
                                    <span class="input-group-btn">
                                        <wpsf:submit type="button" action="changeContentType" cssClass="btn btn-info">
                                            <s:text name="%{getText('label.change')}"/>
                                        </wpsf:submit>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group"> 
                                <label for="contentTypeAuthor"><s:text name="jpfastcontentedit.label.authorAttribute"/></label>
                                <wpsf:select id="contentTypeAuthor" list="selectableAttributes" headerKey="" headerValue="%{getText('label.none')}" 
                                             name="authorAttribute" value="%{getShowlet().getConfig().get('authorAttribute')}" listKey="name" listValue="name" 
                                             cssClass="form-control"/>
                            </div>
                        </fieldset>
                    </s:else>
                </div>
            </div>
            <s:if test="showlet.config['typeCode'] != null">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit type="button" cssClass="btn btn-primary btn-block">
                                <span class="icon fa fa-floppy-o"></span>&#32;
                                <s:text name="%{getText('label.save')}"/>
                            </wpsf:submit>
                        </div>
                    </div>
                </div>

            </s:if>
        </s:form>
    </div>
</div>