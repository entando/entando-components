<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpcontentrefs.title.contentTypesManagement" /></h1>
<div id="main">

<h2><s:text name="jpcontentrefs.title.categories" /></h2>

<p><s:text name="jpcontentrefs.note.youAreConfigure" />: <em class="important"><s:property value="%{getContentType(contentTypeCode).getDescr()}" /></em></p>

<s:form method="get" cssClass="action-form">
<p class="noscreen">
	<wpsf:hidden name="contentTypeCode" />
	<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"/></s:iterator>
</p>

<fieldset class="margin-more-top"><legend><s:text name="jpcontentrefs.title.categoryTree" /></legend>

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>
<ul id="categoryTree">
	<s:set name="inputFieldName" value="'selectedNode'" />
	<s:set name="selectedTreeNode" value="selectedNode" />
	<s:set name="liClassName" value="'category'" />
	<s:if test="%{#categoryTreeStyleVar == 'classic'}">
	<s:set name="currentRoot" value="allowedTreeRootNode" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
	</s:if>
	<s:elseif test="%{#categoryTreeStyleVar == 'request'}">
	<s:set name="openTreeActionName" value="'openCloseFacetTree'" />
	<s:set name="closeTreeActionName" value="'openCloseFacetTree'" />
	<s:set name="currentRoot" value="showableTree" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
	</s:elseif>
</ul>

<div id="actions-container">
	<p class="buttons">
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/join-to-content.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="join" type="image" src="%{#iconImagePath}" value="%{getText('label.join')}" title="%{getText('label.join')}" />		
	</p>
</div>
</fieldset>
<s:set var="joinedCategoryVar" value="%{getCategories(contentTypeCode)}"/>

<s:if test="#joinedCategoryVar != null && #joinedCategoryVar.size() > 0">
<table class="generic" summary="<s:text name="jpcontentrefs.note.categories.summary" />">
<caption><span><s:text name="jpcontentrefs.title.categoriesAssociated" /></span></caption>
<tr>
	<th><s:text name="jpcontentrefs.label.description" /></th>
	<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
</tr>

<s:iterator id="joinedCategory" value="#joinedCategoryVar" >
<tr>
	<td><s:property value="%{#joinedCategory.getFullTitle(currentLang.code)}" /></td>
 	<td class="icon">
 	<wpsa:actionParam action="remove" var="actionName" >
		<wpsa:actionSubParam name="selectedNode" value="%{#joinedCategory.code}" />
	</wpsa:actionParam>
	<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</wpsa:set>
	<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}: %{#joinedCategory.getFullTitle(currentLang.code)}" title="%{getText('label.remove')}: %{#joinedCategory.getFullTitle(currentLang.code)}" />
 	</td>
</tr>
</s:iterator>
</table>
</s:if>
<s:else><p><s:text name="jpcontentrefs.note.categories.empty" /></p></s:else>

</s:form>
</div>