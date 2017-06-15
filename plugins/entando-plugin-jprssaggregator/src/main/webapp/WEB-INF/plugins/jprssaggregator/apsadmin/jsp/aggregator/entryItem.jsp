<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li>
        <s:text name="breadcrumb.integrations.components"/>
    </li>
    <li>
        <a href="<s:url namespace="/do/jprssaggregator/Aggregator" action="list" />">
            <s:text name="jprssaggregator.title.rssAggregator.rssManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:if test="strutsAction == 1">
            <s:text name="label.add"/>
        </s:if>
        <s:elseif test="strutsAction == 2">
            <s:text name="label.edit"/>
        </s:elseif>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12">
            <h1>
                <s:if test="strutsAction == 1">
                    <s:text name="label.add"/>
                </s:if>
                <s:elseif test="strutsAction == 2">
                    <s:text name="label.edit"/>
                </s:elseif>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="rssAggregator.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>
<br>


<div id="main">
    <s:form action="save" cssClass="form-horizontal action-form">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />

        <div class="sr-only">
            <wpsf:hidden name="strutsAction"/>
            <wpsf:hidden name="code"/>
            <wpsf:hidden name="lastUpdate"/>
            <wpsf:hidden name="xmlCategories"/>
            <s:if test="strutsAction == 2">
                <wpsf:hidden name="contentType"/>
            </s:if>
            <s:if test="strutsAction == 1"><s:set var="delay" value="86400"/></s:if>
            <s:else><s:set var="delay" value="delay"/></s:else>
            <s:if test="#categoryTreeStyleVar == 'request'">
                <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                    <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/>
                </s:iterator>
            </s:if>
        </div>

        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['contentType']}"/>
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()"/>
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label for="rss_contentType"><s:text name="jprssaggregator.rssAggregator.contentType"/></label>
            </div>
            <div class="col-xs-10">
                <s:if test="strutsAction == 1">
                    <wpsf:select cssClass="form-control" name="contentType" id="rss_contentType" list="contentTypes"
                                 listKey="code" listValue="descr"/>
                </s:if>
                <s:elseif test="strutsAction == 2">
                    <wpsf:textfield cssClass="form-control" name="contentType" id="rss_contentType"
                                    value="%{getSmallContentType(contentType).descr}" disabled="true"/>
                </s:elseif>
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property/>&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['descr']}"/>
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()"/>
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label for="rss_descr"><s:text name="jprssaggregator.rssAggregator.description"/></label>
            </div>
            <div class="col-xs-10">
                <wpsf:textfield id="rss_descr" name="descr" cssClass="form-control"/>
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property/>&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['link']}"/>
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()"/>
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label for="rss_link"><s:text name="jprssaggregator.rssAggregator.url"/></label>
            </div>
            <div class="col-xs-10">
                <wpsf:textfield id="rss_link" name="link" cssClass="form-control"/>
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property/>&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['delay']}"/>
        <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()"/>
        <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}"/>
        <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
            <div class="col-xs-2 control-label">
                <label for="rss_delay"><s:text name="jprssaggregator.rssAggregator.delay"/></label>
            </div>
            <div class="col-xs-10">
                <wpsf:select cssClass="form-control" name="delay" id="rss_delay" list="delays" listKey="key"
                             listValue="value" value="#delay"/>
                <s:if test="#fieldHasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldFieldErrorsVar}"><s:property/>&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>
        <fieldset class="margin-base-vertical" id="category-content-block">

            <div class="form-group<s:property value="controlGroupErrorClassVar" />">
                <div class="col-xs-2 control-label">
                    <label><s:text name="title.categoriesManagement"/></label>
                </div>
                <div class="col-xs-10">
                    <script src="<wp:resourceURL />administration/js/entando-typeahead-tree.js"></script>
                    <s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/category/categoryTree-extra.jsp" />

                    <table id="categoryTree" class="table table-bordered table-hover table-treegrid">
                        <thead>
                            <tr>
                                <th>
                                    <s:text name="label.category.tree"/>
                                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                                        <i class="fa fa-plus-square-o treeInteractionButtons"  aria-hidden="true"></i>&#32;
                                        <s:text name="label.category.expandAll"/>
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <i class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></i>&#32;
                                        <s:text name="label.category.collapseAll"/>
                                    </button>
                                </th>
                                <th class="text-center table-w-5">
                                    <s:text name="label.join" />
                                </th>
                            </tr>
                        </thead>
                        <tbody>

                            <s:set var="inputFieldName" value="'categoryCode'" />
                            <s:set var="selectedTreeNode" value="selectedNode" />
                            <s:set var="selectedPage" value="%{getCategory(selectedTreeNode)}" />
                            <s:set var="currentRoot" value="categoryRoot" />
                            <s:set var="isPosition" value="false" />
                            <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />

                        </tbody>
                    </table>
                    <script>
                        $('.table-treegrid').treegrid();
                    </script>

                    <s:if test="extraGroups.size() != 0">
                        <s:iterator value="extraGroups" var="groupName">
                            <wpsa:actionParam action="removeExtraGroup" var="actionName" >
                                <wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
                            </wpsa:actionParam>

                            <div class="label label-default label-tag label-sm">
                                <s:property value="%{getSystemGroups()[#groupName].getDescr()}"/>&#32;
                                <wpsf:submit type="button" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" cssClass="btn btn-tag">
                                    <span class="icon fa fa-times"></span>
                                    <span class="sr-only">x</span>
                                </wpsf:submit>
                            </div>
                        </s:iterator>
                    </s:if>

                    <s:set var="categoriesVar" value="categories" />
                    <label class="sr-only"><s:text name="title.contentCategories.list" /></label>
                    <s:if test="#categoriesVar != null && #categoriesVar.size() > 0">
                        <s:iterator value="#categoriesVar" var="contentCategory">
                            <s:set var="contentCategory" value="%{getCategory(#contentCategory.key)}"></s:set>
                                <div class="label label-default label-tag label-sm">
                                    <span class="icon fa fa-tag"></span>&#32;
                                    <span title="<s:property value="#contentCategory.getFullTitle(currentLang.code)"/>"><s:property value="#contentCategory.getShortFullTitle(currentLang.code)" /></span>&#32;
                                <wpsa:actionParam action="removeCategory" var="actionName" >
                                    <wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
                                </wpsa:actionParam>
                                <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #contentCategory.defaultFullTitle}" cssClass="btn btn-tag btn-link">
                                    <span class="icon fa fa-times"></span>
                                    <span class="sr-only">x</span>
                                </wpsf:submit>
                            </div>
                        </s:iterator>
                    </s:if>
                </div>
            </div>
        </fieldset>

        <div class="form-group">
            <div class="col-xs-12">
                <wpsf:submit type="button" cssClass="btn btn-primary pull-right">
                    <s:text name="label.save" />
                </wpsf:submit>
            </div>
        </div>
    </s:form>
</div>
