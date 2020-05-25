<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li>
        <s:text name="title.pageDesigner" />
    </li>
    <li>
        <a href="<s:url action="viewTree" namespace="/do/Page" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />">
            <s:text name="title.pageManagement" />
        </a>
    </li>
    <li class="page-title-container"><s:text name="jpblog.admin.menu" /></li>
</ol>

<h1 class="page-title-container">
    <s:text name="jpblog.config" />
    <span class="pull-right">
        <a tabindex="0" role="button"  data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="jpblog.config.list.help" />" data-placement="left" data-original-title="">
            <i class="fa fa-question-circle-o" aria-hidden="true"></i>
        </a>
    </span>
</h1>

<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>
<div>
    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
    <s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

    <div class="subsection-light">

        <s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>

        <s:form action="saveListViewerConfig" namespace="/do/jpblog/Page/SpecialWidget/BlogListViewer">
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
                    <legend>
                        <span class="control-label label-group-name">
                            <span class="icon fa fa-puzzle-piece" title="Widget"></span>
                            <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" />
                        </span>
                    </legend>
                    <s:if test="showlet.config['contentType'] == null">
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
                                    <div class="input-group-btn">
                                        <wpsf:submit type="button" action="configListViewer" cssClass="btn btn-primary">
                                            <s:text name="%{getText('label.continue')}"/>
                                        </wpsf:submit>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </s:if>
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
                            <p class="noscreen">
                                <wpsf:hidden name="contentType" value="%{getShowlet().getConfig().get('contentType')}" />
                                <wpsf:hidden name="categories" value="%{getShowlet().getConfig().get('categories')}" />
                                <s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
                                    <input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
                                </s:iterator>
                            </p>
                        </div>

                        <div class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#filterOptions">
                                <s:text name="title.filterOptions"/>
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="filterOptions">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label" for="filterKey">
                                        <s:text name="label.filter" />
                                    </label>
                                    <div class="col-sm-10 input-group">
                                        <wpsf:select name="filterKey"
                                                     id="filterKey"
                                                     list="allowedFilterTypes"
                                                     listKey="key"
                                                     listValue="value"
                                                     cssClass="form-control" />
                                        <div class="input-group-btn">
                                            <%-- blogSetFilterType setta in sessione parametri priam di chiamare setFilterType --%>
                                            <wpsf:submit type="button" action="blogSetFilterType" cssClass="btn btn-primary">
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
                                        <thead>
                                            <tr>
                                                <th class="text-right"><abbr title="<s:text name="label.number" />">N</abbr></th>
                                                <th><s:text name="name.filterDescription" /></th>
                                                <th><s:text name="label.order" /></th>
                                                <th class="text-center" style="width: 50px"><s:text name="label.actions"/></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <s:iterator value="filtersProperties" var="filter" status="rowstatus">
                                                <tr>
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
                                                        <td class="text-center">
                                                            <div class="btn-group btn-group-xs">
                                                            <wpsa:actionParam action="moveFilter" var="actionName" >
                                                                <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                                <wpsa:actionSubParam name="movement" value="UP" />
                                                            </wpsa:actionParam>
                                                            <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveUp')}" cssClass="btn btn-default">
                                                                <span class="icon fa fa-sort-desc"></span>
                                                            </wpsf:submit>
                                                            <wpsa:actionParam action="moveFilter" var="actionName" >
                                                                <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                                <wpsa:actionSubParam name="movement" value="DOWN" />
                                                            </wpsa:actionParam>
                                                            <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.moveDown')}" cssClass="btn btn-default">
                                                                <span class="icon fa fa-sort-asc"></span>
                                                            </wpsf:submit>
                                                        </div>
                                                        <div class="btn-group btn-group-xs">
                                                            <wpsa:actionParam action="removeFilter" var="actionName" >
                                                                <wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
                                                            </wpsa:actionParam>
                                                            <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove')}" cssClass="btn btn-warning">
                                                                <span class="icon fa fa-times-circle-o"></span>
                                                            </wpsf:submit>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </s:iterator>
                                        </tbody>
                                    </table>
                                </s:if>
                                <s:else>
                                    <div class="alert alert-info"><s:text name="note.filters.none" /></div>
                                </s:else>
                            </div>
                        </div>

                        <%-- TITLES --%>
                        <div class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#extraOption">
                                <s:text name="title.extraOption"/>
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="extraOption">
                                <p><s:text name="note.extraOption.intro" /></p>
                                <s:iterator var="lang" value="langs">
                                    <div class="form-group">
                                        <label for="title_<s:property value="#lang.code" />">
                                            <code class="label label-info"><s:property value="#lang.code" /></code>
                                            <s:text name="label.title" />
                                        </label>
                                        <wpsf:textfield name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" cssClass="form-control" />
                                    </div>
                                </s:iterator>

                                <div class="form-group">
                                    <label class="col-sm-2 control-label" for="pageLink">
                                        <s:text name="label.link.page" />
                                    </label>
                                    <div class="col-sm-10 input-group">
                                        <select name="pageLink" id="pageLink" class="form-control">
                                            <option value=""><s:text name="label.select" /></option>
                                            <s:iterator value="pages" var="pageVar">
                                                <option <s:if test="%{widget.config.get('pageLink') == #pageVar.code}">selected="selected"</s:if> 
                                                    value="<s:property value="#pageVar.code"/>"><s:property value="%{getShortFullTitle(#pageVar, currentLang.code)}"/></option>
                                            </s:iterator>
                                        </select>
                                    </div>
                                </div>

                                <s:iterator var="lang" value="langs">
                                    <div class="form-group">
                                        <label for="linkDescr_<s:property value="#lang.code" />">
                                            <code class="label label-info"><s:property value="#lang.code" /></code>
                                            <s:text name="label.link.descr"/>
                                        </label>
                                        <wpsf:textfield name="linkDescr_%{#lang.code}" id="linkDescr_%{#lang.code}" value="%{showlet.config.get('linkDescr_' + #lang.code)}" cssClass="form-control" />
                                    </div>
                                </s:iterator>
                            </div>
                        </div>

                        <%-- USER FILTERS - START BLOCK --%>
                        <div class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#filtersSearch">
                                <s:text name="title.filters.search"/>
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="filtersSearch">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label" for="userFilterKey">
                                        <s:text name="label.filter" />
                                    </label>
                                    <div class="col-sm-10 input-group">
                                        <wpsf:select name="userFilterKey"
                                                     id="userFilterKey"
                                                     list="allowedUserFilterTypes"
                                                     listKey="key"
                                                     listValue="value"
                                                     cssClass="form-control" />
                                        <div class="input-group-btn">
                                            <wpsf:submit type="button" action="addUserFilter" cssClass="btn btn-primary">
                                                <s:text name="%{getText('label.add')}"></s:text>
                                            </wpsf:submit>
                                        </div>
                                    </div>
                                </div>

                                <p class="noscreen">
                                    <wpsf:hidden name="userFilters" value="%{getShowlet().getConfig().get('userFilters')}" />
                                </p>

                                <s:if test="null != userFiltersProperties && userFiltersProperties.size() > 0" >
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th class="text-right"><abbr title="<s:text name="label.number" />">N</abbr></th>
                                                <th><s:text name="name.filterDescription" /></th>
                                                <th class="text-center" style="width: 50px"><s:text name="label.actions"/></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <s:iterator value="userFiltersProperties" var="userFilter" status="rowstatus">
                                                <tr>
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
                                                                        <s:set var="userFilterCategoryRoot" value="%{getCategory(#userFilter['categoryCode'])}"></s:set>
                                                                        (<s:property value="%{getFullTitle(#userFilterCategoryRoot, currentLang.code)}"/>)
                                                                    </s:if>
                                                                </s:elseif>
                                                            </s:if>
                                                            <s:elseif test="#userFilter['attributeFilter'] == 'true'">
                                                                <s:property value="#userFilter['key']" />
                                                            </s:elseif>
                                                        </strong>
                                                    </td>
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
                                                </tr>
                                            </s:iterator>
                                        </tbody>
                                    </table>
                                </s:if>
                                <s:else>
                                    <div class="alert alert-info"><s:text name="note.filters.none" /></div>
                                </s:else>
                            </div>
                        </div>
                        <%-- USER FILTERS - END BLOCK --%>
                        <div class="col-xs-12">
                            <legend data-toggle="collapse" data-target="#publishingOptions">
                                <s:text name="title.publishingOptions"/>
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="publishingOptions">
                                <p class="noscreen">
                                    <wpsf:hidden name="maxElemForItem" value="1" />
                                </p>

                                <div class="form-group">
                                    <label class="col-sm-2 control-label" for="maxElements">
                                        <s:text name="label.maxElements" />
                                    </label>
                                    <div class="col-sm-10 input-group">
                                        <wpsf:select name="maxElements"  id="maxElements" value="%{getShowlet().getConfig().get('maxElements')}"
                                                     headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}"
                                                     cssClass="form-control" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="margin-more-top">
                            <legend data-toggle="collapse" data-target="#jpcontentfeedback">
                                <s:text name="jpcontentfeedback.title.configuration"/>
                                <span class="icon fa fa-chevron-down"></span>
                            </legend>
                            <div class="collapse" id="jpcontentfeedback">
                                <div class="btn-group" data-toggle="buttons">
                                    <label class="btn btn-default" for="jpcontentfeedback_usedComment">
                                        <wpsf:checkbox name="usedComment" id="jpcontentfeedback_usedComment"  value="%{getShowlet().getConfig().get('usedComment')}" cssClass="radiocheck"/>
                                        <s:text name="jpcontentfeedback.label.commentsOnContent" />
                                    </label>
                                    <label class="btn btn-default" for="jpcontentfeedback_anonymousComment">
                                        <wpsf:checkbox name="anonymousComment" id="jpcontentfeedback_anonymousComment"  value="%{getShowlet().getConfig().get('anonymousComment')}" cssClass="radiocheck"/>
                                        <s:text name="jpcontentfeedback.label.anonymousComments" />
                                    </label>
                                    <label class="btn btn-default" for="jpcontentfeedback_commentsModeration">
                                        <wpsf:checkbox name="commentValidation" id="jpcontentfeedback_commentsModeration" value="%{getShowlet().getConfig().get('commentValidation')}" cssClass="radiocheck" />
                                        <s:text name="jpcontentfeedback.label.commentsModeration" />
                                    </label>
                                    <label class="btn btn-default" for="jpcontentfeedback_usedContentRating">
                                        <wpsf:checkbox name="usedContentRating" id="jpcontentfeedback_usedContentRating"  value="%{getShowlet().getConfig().get('usedContentRating')}" cssClass="radiocheck" />
                                        <s:text name="jpcontentfeedback.label.contentsRating" />
                                    </label>
                                    <label class="btn btn-default" for="jpcontentfeedback_usedCommentWithRating">
                                        <wpsf:checkbox name="usedCommentWithRating" id="jpcontentfeedback_usedCommentWithRating"  value="%{getShowlet().getConfig().get('usedCommentWithRating')}" cssClass="radiocheck" />
                                        <s:text name="jpcontentfeedback.label.commentsRating" />
                                    </label>
                                </div>
                            </div>
                        </div>
                    </s:else>
                </div>
            </div>
            <s:if test="showlet.config['contentType'] != null">
                <div class="form-horizontal">
                    <div class="form-group pull-right">
                        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                            <wpsf:submit action="saveListViewerConfig" type="button" cssClass="btn btn-primary">
                                <s:text name="%{getText('label.save')}" />
                            </wpsf:submit>
                        </div>
                    </div>
                </div>
            </s:if>
        </s:form>
    </div>
</div>
