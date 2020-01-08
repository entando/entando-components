<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"%>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<fieldset class="col-xs-12 no-padding">
    <legend>
        <span class="margin-large-top icon fa fa-tags"></span>
        &#32;
        <s:text name="jpfacetnav.title.facets" />
    </legend>
    <p class="sr-only">
        <wpsf:hidden name="facetRootNodes" />
        <s:if test="#facetTreeStyleVar == 'request'">
            <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden>
            </s:iterator>
        </s:if>
    </p>
    <div class="form-group">
        <label class="col-sm-2 control-label">
            <s:text name="label.categories" />
        </label>
        <div class="col-sm-10">
            <s:set var="categoryTreeStyleVar">
                <wp:info key="systemParam" paramName="treeStyle_category" />
            </s:set>
            <s:if test="#categoryTreeStyleVar == 'request'">
                <p class="sr-only">
                    <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar">
                        <wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}" />
                    </s:iterator>
                </p>
            </s:if>
            <!--  category Tree -->
            <div class="table-responsive">
                <table id="categoryTree" class="table table-bordered table-hover table-treegrid">
                    <thead>
                        <tr>
                            <th>
                                <s:text name="label.category.tree" />
                                <s:if test="#categoryTreeStyleVar == 'classic'">
                                    <button type="button" class="btn-no-button expand-button" id="expandAll">
                                        <span class="fa fa-plus-square-o treeInteractionButtons" aria-hidden="true"></span>
                                        &#32;
                                        <s:text name="label.expandAll" />
                                    </button>
                                    <button type="button" class="btn-no-button" id="collapseAll">
                                        <span class="fa fa-minus-square-o treeInteractionButtons" aria-hidden="true"></span>
                                        &#32;
                                        <s:text name="label.collapseAll" />
                                    </button>
                                </s:if>
                            </th>
                            <th class="text-center w4perc">
                                <s:text name="label.category.join" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:set var="inputFieldName" value="'facetCode'" />
                        <s:set var="selectedTreeNode" value="%{facetCode}" />
                        <s:set var="selectedNode" value="%{facetCode}" />
                        <s:set var="liClassName" value="'page'" />
                        <s:set var="treeItemIconName" value="'fa-folder'" />
                        <s:set var="actionName" value="'joinFacet'" />
                        <s:if test="#categoryTreeStyleVar == 'classic'">
                            <s:set var="currentRoot" value="allowedTreeRootNode" />
                            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilderCategoriesJoin.jsp" />
                        </s:if>
                        <s:elseif test="#categoryTreeStyleVar == 'request'">
                            <s:set var="currentRoot" value="showableTree" />
                            <s:set var="treeNodeExtraParamName" value="'activeTab'" />
                            <s:set var="treeNodeExtraParamValue" value="1" />
                            <s:set var="openTreeActionName" value="'openCloseFacetTreeNode'" />
                            <s:set var="closeTreeActionName" value="'openCloseFacetTreeNode'" />
                            <s:include
                                value="/WEB-INF/plugins/jacms/apsadmin/jsp/common/treeBuilder-request-categories.jsp" />
                        </s:elseif>
                    </tbody>
                </table>
            </div>
            <!--  Facet Nav -->
            <s:set var="contentCategories" value="content.categories" />
            <s:if test="%{null != facetRootCodes && facetRootCodes.size() > 0}">
                <h4><s:text name="jpfacetnav.title.categories"/></h4>
                <ul class="list-inline">
                    <s:iterator value="facetRootCodes" var="currentFacetCode" status="rowstatus">
                        <wpsa:set name="currentFacet" value="%{getFacet(#currentFacetCode)}" />
                        <li>
                            <span class="label label-tag">
                                <span class="icon fa fa-tag"></span>
                                &#32;
                                <abbr title="<s:property value="%{getDefaultFullTitle(#currentFacet)}"/>">
                                    <s:property value="%{getDefaultFullTitle(#currentFacet)}" />
                                </abbr>
                                &#32; &#32;
                                <wpsa:actionParam action="removeFacet" var="actionName">
                                    <wpsa:actionSubParam name="facetCode" value="%{#currentFacetCode}" />
                                </wpsa:actionParam>
                                <wpsf:submit type="button" action="%{#actionName}" 
                                    title="%{getText('label.remove') + ' ' + getDefaultFullTitle(#currentFacetCode)}" cssClass="btn btn-link">
                                    <span class="pficon pficon-close white"></span>
                                    <span class="sr-only">x</span>
                                </wpsf:submit>
                            </span>
                        </li>
                    </s:iterator>
                </ul>
            </s:if>
        </div>
    </div>
</fieldset>
