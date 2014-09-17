<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
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
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>

        <s:form namespace="/do/jpgeoref/Page/SpecialWidget/ListViewer" cssClass="form-horizontal">

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

            <p class="noscreen">
                <wpsf:hidden name="pageCode" />
                <wpsf:hidden name="frame" />
                <wpsf:hidden name="widgetTypeCode" />
                <wpsf:hidden name="contentType" />
                <wpsf:hidden name="categories" value="%{#parameters['categories']}" />
                <wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}" />
                <wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}" />
                <wpsf:hidden name="filters" />
                <wpsf:hidden name="modelId" />
                <wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
                <s:iterator id="lang" value="langs">
                    <wpsf:hidden name="%{'linkDescr_' + #lang.code}" value="%{#parameters['linkDescr_' + #lang.code]}" />
                    <wpsf:hidden name="%{'title_' + #lang.code}" value="%{#parameters['title_' + #lang.code]}" />
                </s:iterator>
                <wpsf:hidden name="userFilterKey" value="category" />
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
                    <p>
                        <span class="icon fa fa-filter" title="Filter by"></span>&#32;
                        <s:text name="title.userFilterCategoryConfigure" />
                    </p>
                    <div class="form-group">
                        <div class="col-xs-12">
                            <label for="userFilterCategoryCode"><s:text name="label.userFilterCategory" /></label>
                            <wpsf:select name="userFilterCategoryCode" id="userFilterCategoryCode" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <wpsf:submit action="addUserFilter" type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="%{getText('label.save')}" />
                    </wpsf:submit>
                </div>
            </div>
        </s:form>
    </div>
</div>