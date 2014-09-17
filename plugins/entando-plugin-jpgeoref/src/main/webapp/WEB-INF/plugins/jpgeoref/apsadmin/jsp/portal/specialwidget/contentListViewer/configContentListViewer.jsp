<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
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

        <s:form action="saveListViewerConfig" namespace="/do/jpgeoref/Page/SpecialWidget/ListViewer">
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
                    <%--<s:set var="contentTypes" value="contentTypes" />--%>
                    
                    <s:if test="#contentTypesVar.isEmpty()">
                        <div class="alert alert-info">
                            <s:text name="jpgeoref.error.noContentTypeAvailable" />
                        </div>
                    </s:if>
                    <s:elseif test="showlet.config['contentType'] == null">
                        <%-- SELEZIONE DEL TIPO DI CONTENUTO --%>
                        <fieldset class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label for="contentType"><s:text name="label.type"/></label>
                                <div class="input-group">
                                    <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="form-control" />
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" action="configListViewer" cssClass="btn btn-default" >
                                            <s:text name="%{getText('label.continue')}"/>
                                            <span class="icon fa fa-play-circle-o"></span>
                                        </wpsf:submit>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                    </s:elseif>
                    <s:else>
                        <fieldset class="col-xs-12">
                            <legend><s:text name="title.contentInfo" /></legend>
                            <div class="form-group">
                                <label for="contentType"><s:text name="label.type"/></label>
                                <div class="input-group">
                                    <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="form-control" />
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" action="changeContentType" cssClass="btn btn-info">
                                            <s:text name="%{getText('label.change')}"/>
                                        </wpsf:submit>	
                                    </div>
                                </div>
                            </div>
                            <p class="noscreen">
                                <wpsf:hidden name="contentType" value="%{getShowlet().getConfig().get('contentType')}" />
                                <wpsf:hidden name="categories" value="%{getShowlet().getConfig().get('categories')}" />
                                <s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
                                    <input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
                                </s:iterator>
                            </p>

                            <div class="form-group">
                                <label for="category"><s:text name="label.categories" /></label>
                                <div class="input-group">
                                    <wpsf:select name="categoryCode" id="category" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="form-control" />
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" action="addCategory" cssClass="btn btn-info">
                                            <span class="icon fa fa-plus"></span>&#32;
                                            <s:text name="%{getText('label.join')}"/>
                                        </wpsf:submit>
                                    </div>
                                </div>
                            </div>

                            <s:if test="null != categoryCodes && categoryCodes.size() > 0">
                                <div class="clearfix margin-small-bottom">
                                    <s:iterator value="categoryCodes" var="categoryCodeVar">
                                        <s:set name="showletCategory" value="%{getCategory(#categoryCodeVar)}"></s:set>

                                            <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
                                                <span class="icon fa fa-tag"></span>&#32;
                                                <abbr title="<s:property value="#showletCategory.getFullTitle(currentLang.code)"/>">
                                                <s:property value="#showletCategory.getShortFullTitle(currentLang.code)" />
                                            </abbr>&#32;
                                            <wpsa:actionParam action="removeCategory" var="actionName" >
                                                <wpsa:actionSubParam name="categoryCode" value="%{#categoryCodeVar}" />
                                            </wpsa:actionParam>
                                            <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ': ' + #showletCategory.getFullTitle(currentLang.code)}" cssClass="btn btn-default btn-xs badge">
                                                <span class="icon fa fa-times"></span>
                                                <span class="sr-only">x</span>
                                            </wpsf:submit>
                                        </span>                                    
                                    </s:iterator>
                                </div>
                                <s:if test="categoryCodes.size() > 1">
                                    <div class="form-group">
                                        <div class="btn-group" data-toggle="buttons">
                                        <label for="orClauseCategoryFilter" class="btn btn-default">
                                        <wpsf:checkbox name="orClauseCategoryFilter" 
                                                       value="%{getShowlet().getConfig().get('orClauseCategoryFilter')}" id="orClauseCategoryFilter" cssClass="radiocheck" />
                                            <s:text name="label.orClauseCategoryFilter" />
                                        </label>
                                        </div>
                                    </div>
                                </s:if>
                            </s:if>
                            <s:else>
                                <div class="alert alert-info"><s:text name="note.categories.none" /></div>		
                            </s:else>
                        </fieldset>

                        <fieldset class="col-xs-12"> 
                            <legend data-toggle="collapse" data-target="#filterOptions">
                                <s:text name="title.filterOptions"/>
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="filterOptions">

                                <div class="form-group">
                                    <label for="filterKey"><s:text name="label.filter" /></label>
                                    <div class="input-group">
                                        <wpsf:select name="filterKey" id="filterKey" list="allowedFilterTypes" listKey="key" listValue="value" cssClass="form-control" />
                                        <div class="input-group-btn">

                                            <wpsf:submit type="button" action="setFilterType" cssClass="btn btn-info">
                                                <span class="icon fa fa-filter"></span>
                                                <s:text name="%{getText('label.add')}"/>
                                            </wpsf:submit>
                                        </div>
                                    </div>
                                </div>

                                <p class="noscreen">
                                    <wpsf:hidden name="filters" value="%{getShowlet().getConfig().get('filters')}" />
                                </p>

                                <s:if test="null != filtersProperties && filtersProperties.size()>0" >
                                    <table class="table table-bordered">
                                        <tr>
                                            <th class="text-center"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
                                            <th class="text-right"><abbr title="<s:text name="label.number" />">N</abbr></th>
                                            <th><s:text name="name.filterDescription" /></th>
                                            <th><s:text name="label.order" /></th>
                                        </tr>
                                        <s:iterator value="filtersProperties" id="filter" status="rowstatus">
                                            <tr>
                                                <td class="text-center">
                                                    <div class="btn-group btn-group-xs">

                                                        <wpsa:actionParam action="moveFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="UP" />
                                                        </wpsa:actionParam>
                                                        <s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>		
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-desc"></span>
                                                        </wpsf:submit>
                                                        <wpsa:actionParam action="moveFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="DOWN" />
                                                        </wpsa:actionParam>
                                                        <s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-asc"></span>
                                                        </wpsf:submit>
                                                        <wpsa:actionParam action="removeFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                        </wpsa:actionParam>
                                                    </div>
                                                    <div class="btn-group btn-group-xs">

                                                        <s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
                                                            <span class="icon fa fa-times-circle-o"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                </td>	
                                                <td class="text-right"><s:property value="#rowstatus.index+1"/></td>
                                                <td>
                                                    <s:text name="label.filterBy" /><strong>
                                                        <s:if test="#filter['key'] == 'created'">
                                                            <s:text name="label.creationDate" />
                                                        </s:if>
                                                        <s:elseif test="#filter['key'] == 'modified'">
                                                            <s:text name="label.lastModifyDate" />			
                                                        </s:elseif>
                                                        <s:else>
                                                            <s:property value="#filter['key']" />
                                                        </s:else>
                                                    </strong><s:if test="(#filter['start'] != null) || (#filter['end'] != null) || (#filter['value'] != null)">,
                                                        <s:if test="#filter['start'] != null">
                                                            <s:text name="label.filterFrom" /><strong>
                                                                <s:if test="#filter['start'] == 'today'">
                                                                    <s:text name="label.today" />
                                                                </s:if>
                                                                <s:else>
                                                                    <s:property value="#filter['start']" />
                                                                </s:else>
                                                            </strong>
                                                            <s:if test="#filter['startDateDelay'] != null" >
                                                                <s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['startDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />&nbsp;
                                                            </s:if>		
                                                        </s:if>
                                                        <s:if test="#filter['end'] != null">
                                                            <s:text name="label.filterTo" /><strong>
                                                                <s:if test="#filter['end'] == 'today'">
                                                                    <s:text name="label.today" />
                                                                </s:if>
                                                                <s:else>
                                                                    <s:property value="#filter['end']" />
                                                                </s:else>
                                                            </strong>
                                                            <s:if test="#filter['endDateDelay'] != null" >
                                                                <s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['endDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
                                                            </s:if>
                                                        </s:if>
                                                        <s:if test="#filter['value'] != null">
                                                            <s:text name="label.filterValue" />:<strong> <s:property value="#filter['value']" /></strong> 
                                                            <s:if test="#filter['likeOption'] == 'true'">
                                                                <em>(<s:text name="label.filterValue.isLike" />)</em> 
                                                            </s:if>
                                                        </s:if>
                                                        <s:if test="#filter['valueDateDelay'] != null" >
                                                            <s:text name="label.filterValueDateDelay" />:<strong> <s:property value="#filter['valueDateDelay']" /></strong>&nbsp;<s:text name="label.filterDateDelay.gg" />
                                                        </s:if>
                                                    </s:if>
                                                    <s:if test="#filter['nullValue'] != null" >
                                                        &nbsp;<s:text name="label.filterNoValue" />
                                                    </s:if>
                                                </td>
                                                <td>
                                                    <s:if test="#filter['order'] == 'ASC'"><s:text name="label.order.ascendant" /></s:if>
                                                    <s:if test="#filter['order'] == 'DESC'"><s:text name="label.order.descendant" /></s:if>
                                                    </td>
                                                </tr>
                                        </s:iterator>
                                    </table>
                                </s:if>
                                <s:else>
                                    <div class="alert alert-info"><s:text name="note.filters.none" /></div>		
                                </s:else>
                            </div>
                        </fieldset>

                        <%-- TITLES --%>
                        <fieldset class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#extraOption">
                                <s:text name="title.extraOption" />
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="extraOption">
                                <p><s:text name="note.extraOption.intro" /></p>
                                <s:iterator id="lang" value="langs">
                                    <div class="form-group">
                                        <label for="title_<s:property value="#lang.code" />" >
                                            <code class="label label-info"><s:property value="#lang.code" /></code>
                                            <s:text name="label.title" />
                                        </label>
                                        <wpsf:textfield name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" cssClass="form-control" />
                                    </div>
                                </s:iterator>

                                <div class="form-group">
                                    <label for="pageLink" ><s:text name="label.link.page" /></label>
                                    <wpsf:select list="pages" name="pageLink" id="pageLink" listKey="code" listValue="getShortFullTitle(currentLang.code)" 
                                                 value="%{showlet.config.get('pageLink')}" headerKey="" headerValue="- %{getText('label.select')} -" cssClass="form-control"/>
                                </div>

                                <s:iterator var="lang" value="langs">
                                    <div class="form-group">
                                        <label for="linkDescr_<s:property value="#lang.code" />" >
                                            <code class="label label-info"><s:property value="#lang.code" /></code>
                                            <s:text name="label.link.descr"/>
                                        </label>
                                        <wpsf:textfield name="linkDescr_%{#lang.code}" id="linkDescr_%{#lang.code}" value="%{showlet.config.get('linkDescr_' + #lang.code)}" cssClass="form-control" />
                                    </div>
                                </s:iterator>

                            </div>
                        </fieldset>

                        <%-- USER FILTERS - START BLOCK --%>
                        <fieldset class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#filtersSearch">
                                <s:text name="title.filters.search" />&#32;
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="filtersSearch">
                                <div class="form-group">
                                    <label for="userFilterKey"><s:text name="label.filter" /></label>
                                    <div class="input-group">
                                        <wpsf:select name="userFilterKey" id="userFilterKey" list="allowedUserFilterTypes" listKey="key" listValue="value" cssClass="form-control" />
                                        <div class="input-group-btn">
                                            <wpsf:submit type="button" action="addUserFilter" cssClass="btn btn-info">
                                                <span class="icon fa fa-plus"></span>
                                                <s:text name="%{getText('label.add')}"/>
                                            </wpsf:submit>
                                        </div>
                                    </div>
                                </div>

                                <p class="noscreen">
                                    <wpsf:hidden name="userFilters" value="%{getShowlet().getConfig().get('userFilters')}" />
                                </p>

                                <s:if test="null != userFiltersProperties && userFiltersProperties.size() > 0" >
                                    <table class="table table-bordered">
                                        <tr>
                                            <th class="text-center"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
                                            <th class="text-right"><abbr title="<s:text name="label.number" />">N</abbr></th>
                                            <th><s:text name="name.filterDescription" /></th>
                                        </tr>
                                        <s:iterator value="userFiltersProperties" id="userFilter" status="rowstatus">
                                            <tr>
                                                <td class="text-center">
                                                    <div class="btn-group btn-group-xs">
                                                        <wpsa:actionParam action="moveUserFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="UP" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-desc"></span>
                                                        </wpsf:submit>
                                                        <wpsa:actionParam action="moveUserFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            <wpsa:actionSubParam name="movement" value="DOWN" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
                                                            <span class="icon fa fa-sort-asc"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                    <div class="btn-group btn-group-xs">
                                                        <wpsa:actionParam action="removeUserFilter" var="actionName" >
                                                            <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                        </wpsa:actionParam>
                                                        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
                                                            <span class="icon fa fa-times-circle-o"></span>
                                                        </wpsf:submit>
                                                    </div>
                                                </td>	
                                                <td class="text-right"><s:property value="#rowstatus.index+1"/></td>
                                                <td>
                                                    <s:text name="label.filterBy" />
                                                    <strong>
                                                        <s:if test="#userFilter['attributeFilter'] == 'false'">
                                                            <s:if test="#userFilter['key'] == 'fulltext'">
                                                                <s:text name="label.fulltext" />			
                                                            </s:if>
                                                            <s:elseif test="#userFilter['key'] == 'category'">
                                                                <s:text name="label.category" />
                                                                <s:if test="null != #userFilter['categoryCode']">
                                                                    <s:set name="userFilterCategoryRoot" value="%{getCategory(#userFilter['categoryCode'])}"></s:set>
                                                                    (<s:property value="#userFilterCategoryRoot.getFullTitle(currentLang.code)"/>)
                                                                </s:if>
                                                            </s:elseif>
                                                        </s:if>
                                                        <s:elseif test="#userFilter['attributeFilter'] == 'true'">
                                                            <s:property value="#userFilter['key']" />
                                                        </s:elseif>
                                                    </strong>
                                                </td>
                                            </tr>
                                        </s:iterator>
                                    </table>
                                </s:if>
                                <s:else>
                                    <div class="alert alert-info"><s:text name="note.filters.none" /></div>		
                                </s:else>
                            </div>

                        </fieldset>
                        <%-- USER FILTERS - END BLOCK --%>

                        <fieldset class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#publishingOptions">
                                <s:text name="title.publishingOptions" />&#32;
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="publishingOptions">

                                <div class="form-group">
                                    <label for="modelId"><s:text name="jpgeoref.lisViewer.contentModelForCloud" /></label>
                                    <wpsf:select name="modelId" id="modelId" value="%{getShowlet().getConfig().get('modelId')}" 
                                                 list="%{getModelsForContentType(showlet.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="form-control" />
                                </div>

                                <div class="form-group">
                                    <label for="maxElements"><s:text name="label.maxElements" /></label>
                                    <wpsf:select name="maxElements" id="maxElements" value="%{getShowlet().getConfig().get('maxElements')}" 
                                                 headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="form-control" />
                                </div>

                            </div>
                        </fieldset>
            </s:else>
                    </div>
                </div>
            <s:if test="showlet.config['contentType'] != null && !#contentTypesVar.isEmpty()">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit action="saveListViewerConfig" type="button" cssClass="btn btn-primary btn-block">
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