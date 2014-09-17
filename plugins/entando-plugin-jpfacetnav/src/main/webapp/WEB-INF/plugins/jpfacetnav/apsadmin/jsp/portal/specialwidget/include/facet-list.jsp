<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:set var="facetTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<h3 class="h3 margin-small-bottom">
  <span class="margin-large-top icon fa fa-tags"></span>&#32;<s:text name="jpfacetnav.title.facets"/>
</h3>
<hr class="margin-none" />
<p class="sr-only">
  <wpsf:hidden name="facetRootNodes" />
  <s:if test="#facetTreeStyleVar == 'request'">
    <s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
  </s:if>
</p>
<div class="well margin-base-vertical">
  <%-- Tree --%>
    <ul id="pageTree" class="fa-ul list-unstyled">
      <s:set name="inputFieldName" value="'facetCode'" />
      <s:set name="selectedTreeNode" value="%{facetCode}" />
      <s:set name="selectedNode" value="%{facetCode}" />
      <s:set name="liClassName" value="'page'" />
      <s:set var="treeItemIconName" value="'fa-folder'" />
      <s:if test="#facetTreeStyleVar == 'classic'">
        <s:set name="currentRoot" value="facetRoot" />
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
      </s:if>
      <s:elseif test="#facetTreeStyleVar == 'request'">
        <s:set name="currentRoot" value="showableTree" />
        <s:set var="treeNodeExtraParamName" value="'activeTab'" />
        <s:set var="treeNodeExtraParamValue" value="1" />
        <s:set name="openTreeActionName" value="'openCloseFacetTreeNode'" />
        <s:set name="closeTreeActionName" value="'openCloseFacetTreeNode'" />
        <s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-links.jsp" />
      </s:elseif>
    </ul>
  <%-- Fieldset button --%>
  <fieldset data-toggle="tree-toolbar"><legend><s:text name="label.actions" /></legend>
    <div class="btn-toolbar" data-toggle="tree-toolbar-actions">
        <%-- Join button --%>
          <div class="btn-group btn-group-sm margin-small-top margin-small-bottom">
            <wpsf:submit action="joinFacet" type="button" title="%{getText('label.join')}" cssClass="btn btn-info" data-toggle="tooltip">
              <span class="icon fa fa-plus">&#32;
              </span>
            </wpsf:submit>
          </div>
    </div>
  </fieldset>
</div>
<%-- Area results' labels--%>
  <s:if test="%{facetRootCodes.size()>0}">
    <s:iterator value="facetRootCodes" id="currentFacetCode" status="rowstatus">
      <wpsa:set name="currentFacet" value="%{getFacet(#currentFacetCode)}" />
      <span class="label label-default label-sm pull-left padding-small-top padding-small-bottom margin-small-right margin-small-bottom">
        <span class="icon fa fa-tag"></span>&#32;
        <wpsa:set name="currentFacet" value="%{getFacet(#currentFacetCode)}" />
        <abbr title="<s:property value="#currentFacet.defaultFullTitle"/>">
           <s:property value="#currentFacet.defaultFullTitle"/>
        </abbr>&#32;
        <wpsa:actionParam action="removeFacet" var="actionName" >
          <wpsa:actionSubParam name="facetCode" value="%{#currentFacetCode}" />
        </wpsa:actionParam>
        <wpsf:submit type="button" action="%{#actionName}" title="%{getText('label.remove') + ' ' + #currentFacetCode.defaultFullTitle}" cssClass="btn btn-default btn-xs badge">
           <span class="icon fa fa-times"></span>
            <span class="sr-only">x</span>
        </wpsf:submit>
      </span>
    </s:iterator>
  </s:if>

