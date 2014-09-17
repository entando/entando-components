<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><a href="<s:url action="list" namespace="/do/jacms/Resource"><s:param name="resourceTypeCode"><s:property value="resourceTypeCode" /></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.resourceManagement" />"><s:text name="title.resourceManagement" /></a></h1>
<div id="main">
	<h2 class="margin-more-bottom"><s:text name="jpmassiveresourceloader.title.resourceManagement.massiveResourceNew" />&nbsp;<s:text name="jpmassiveresourceloader.resourceType.%{resourceTypeCode}"></s:text></h2>

	<s:form action="save" method="post" cssClass="action-form">
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property/></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<fieldset>
			<legend><s:text name="label.info" /></legend>
			<p class="noscreen">
				<s:hidden name="resourceTypeCode" />
				<s:hidden name="resourceId" />
				<s:iterator value="categoryCodes" id="categoryCode" status="rowstatus">
					<input type="hidden" name="categoryCodes" value="<s:property value="#categoryCode" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
				</s:iterator>
			</p>

			<p>
				<label for="descr" class="basic-mint-label"><s:text name="label.description" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="descr" id="descr" cssClass="text" />
			</p>

			<p>
				<label for="mainGroup" class="basic-mint-label"><s:text name="label.group" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" name="mainGroup" id="mainGroup" list="allowedGroups" value="mainGroup" listKey="name" listValue="descr" cssClass="text" />
			</p>

			<p>
				<label for="folder" class="basic-mint-label"><s:text name="jpmassiveresourceloader.label.folder" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="folder" id="folder" cssClass="text"/>
			</p>

			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="recursive" id="recursive" cssClass="radiocheck"/>
				<label for="recursive"><s:text name="jpmassiveresourceloader.label.recursive" /></label>
			</p>

		</fieldset>

		<fieldset>
				<legend><s:text name="title.resourceCategoriesManagement"/></legend>
				<ul id="categoryTree">
					<s:set name="inputFieldName" value="'categoryCode'" />
					<s:set name="selectedTreeNode" value="selectedNode" />
					<s:set name="liClassName" value="'category'" />
					<s:if test="#categoryTreeStyleVar == 'classic'">
					<s:set name="currentRoot" value="categoryRoot" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
					</s:if>
					<s:elseif test="#categoryTreeStyleVar == 'request'">
					<s:set name="currentRoot" value="showableTree" />
					<s:set name="openTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
					<s:set name="closeTreeActionName" value="'openCloseCategoryTreeNodeOnEntryResource'" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
					</s:elseif>
				</ul>
				<div id="actions-container">
					<p class="buttons">
						<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/join-to-resource.png</s:set>
						<wpsf:submit useTabindexAutoIncrement="true" action="joinCategory" type="image" src="%{#iconImagePath}" value="%{getText('label.join')}" title="%{getText('label.join')}" />
					</p>
				</div>

			<s:if test="categoryCodes != null && categoryCodes.size() > 0">
				<table class="generic" summary="<s:text name="note.resourceCategories.summary"/>">
					<caption><span><s:text name="title.resourceCategories.list"/></span></caption>
					<tr>
						<th><s:text name="label.category"/></th>
						<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
					</tr>
					<s:iterator value="categoryCodes" id="categoryCode">
					<s:set name="resourceCategory" value="%{getCategory(#categoryCode)}"></s:set>
					<tr>
						<td><s:property value="#resourceCategory.getFullTitle(currentLang.code)"/></td>
						<td class="icon">
							<wpsa:actionParam action="removeCategory" var="actionName" >
								<wpsa:actionSubParam name="categoryCode" value="%{#resourceCategory.code}" />
							</wpsa:actionParam>
							<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
							<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />
						</td>
					</tr>
					</s:iterator>
				</table>
			</s:if>

		</fieldset>

		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>

	</s:form>
</div>