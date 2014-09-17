<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>

<div id="main">
<h2><s:text name="title.configPage" /></h2>

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<div class="subsection-light">
<h3><s:text name="title.configPage.youAreDoing" /></h3>

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

<h3 class="margin-more-top margin-more-bottom"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>

<s:form action="saveConfig" namespace="/do/jpphotogallery/Page/SpecialWidget/Photogallery">
<p class="noscreen">
	<wpsf:hidden name="pageCode" />
	<wpsf:hidden name="frame" />
	<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
</p>

	<s:if test="hasFieldErrors()">
<div class="message message_error">
<h4><s:text name="message.title.FieldErrors" /></h4>	
	<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
		<li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
	</ul>
</div>
	</s:if>

<s:if test="showlet.config['contentType'] == null">
<%-- SELEZIONE DEL TIPO DI CONTENUTO --%>
<fieldset><legend><s:text name="title.contentInfo" /></legend>
<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="configWidget" value="%{getText('label.continue')}" cssClass="button" />	
</p>
</fieldset>
</s:if>
<s:else>

<fieldset class="margin-bit-bottom"><legend><s:text name="title.contentInfo" /></legend>
<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="changeContentType" value="%{getText('label.change')}" cssClass="button" />	
</p>
<p class="noscreen">
	<wpsf:hidden name="contentType" value="%{getShowlet().getConfig().get('contentType')}" />
	<wpsf:hidden name="categories" value="%{getShowlet().getConfig().get('categories')}" />
	<s:iterator value="categoryCodes" var="categoryCodeVar" status="rowstatus">
	<input type="hidden" name="categoryCodes" value="<s:property value="#categoryCodeVar" />" id="categoryCodes-<s:property value="#rowstatus.index" />"/>
	</s:iterator>
</p>

	<p>
		<label for="category" class="basic-mint-label"><s:text name="label.categories" />:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="categoryCode" id="category" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="text" />
		<wpsf:submit useTabindexAutoIncrement="true" action="addCategory" value="%{getText('label.join')}" cssClass="button" />
	</p>
	
	<s:if test="null != categoryCodes && categoryCodes.size() > 0">
		<table class="generic" summary="<s:text name="note.resourceCategories.summary"/>">
		<caption><span><s:text name="title.resourceCategories.list"/></span></caption>
		<tr>
			<th><s:text name="label.category"/></th>
			<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
		</tr>
		<s:iterator value="categoryCodes" var="categoryCodeVar">
		<s:set name="showletCategory" value="%{getCategory(#categoryCodeVar)}"></s:set>
		<tr>
			<td><s:property value="#showletCategory.getFullTitle(currentLang.code)"/></td>
			<td class="icon">
				<wpsa:actionParam action="removeCategory" var="actionName" >
					<wpsa:actionSubParam name="categoryCode" value="%{#categoryCodeVar}" />
				</wpsa:actionParam>
				<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/22x22/delete.png</wpsa:set>
				<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ': ' + #showletCategory.getFullTitle(currentLang.code)}" />
			</td>
		</tr>
		</s:iterator>
		</table>
		<s:if test="categoryCodes.size() > 1">
			<p>
			<wpsf:checkbox name="orClauseCategoryFilter" 
				value="%{getShowlet().getConfig().get('orClauseCategoryFilter')}" id="orClauseCategoryFilter" cssClass="radiocheck" />
			<label for="orClauseCategoryFilter"><s:text name="label.orClauseCategoryFilter" /></label>
			</p>
		</s:if>
	</s:if>
	<s:else>
		<p><s:text name="note.categories.none" /></p>		
	</s:else>
</fieldset>

<fieldset><legend><s:text name="title.filterOptions" /></legend>
<p>
	<label for="filterKey" class="basic-mint-label"><s:text name="label.filter" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="filterKey" id="filterKey" list="allowedFilterTypes" listKey="key" listValue="value" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="setFilterType" value="%{getText('label.add')}" cssClass="button" />
</p>

<p class="noscreen">
	<wpsf:hidden name="filters" value="%{getShowlet().getConfig().get('filters')}" />
</p>

<s:if test="null != filtersProperties && filtersProperties.size()>0" >
<table class="generic margin-bit-top" summary="<s:text name="note.page.contentListViewer.summary" />">
<caption><span><s:text name="title.filterOptions" /></span></caption>
<tr>
	<th><abbr title="<s:text name="label.number" />">N</abbr></th>
	<th><s:text name="name.filterDescription" /></th>
	<th><s:text name="label.order" /></th>
	<th class="icon" colspan="3"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
</tr>
<s:iterator value="filtersProperties" id="filter" status="rowstatus">
<tr>
	<td class="rightText"><s:property value="#rowstatus.index+1"/></td>
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
	<td class="icon">
		<wpsa:actionParam action="moveFilter" var="actionName" >
			<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			<wpsa:actionSubParam name="movement" value="UP" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>		
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}" />
	</td>
	<td class="icon">	
		<wpsa:actionParam action="moveFilter" var="actionName" >
			<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			<wpsa:actionSubParam name="movement" value="DOWN" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}" />
	</td>
	<td class="icon">	
		<wpsa:actionParam action="removeFilter" var="actionName" >
			<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image"  src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />
	</td>	
</tr>
</s:iterator>
</table>
</s:if>
<s:else>
	<p><s:text name="note.filters.none" /></p>		
</s:else>
</fieldset>

<%-- TITLES --%>
<fieldset><legend class="accordion_toggler"><s:text name="title.extraOption" /></legend>
<div class="accordion_element">
<p><s:text name="note.extraOption.intro" /></p>
	<s:iterator id="lang" value="langs">
		<p>
			<label for="title_<s:property value="#lang.code" />"  class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code" />)</span><s:text name="label.title" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="title_%{#lang.code}" id="title_%{#lang.code}" value="%{showlet.config.get('title_' + #lang.code)}" cssClass="text" />
		</p>
	</s:iterator>

	<p>
		<label for="pageLink"  class="basic-mint-label"><s:text name="label.link.page" />:</label>
		<wpsf:select useTabindexAutoIncrement="true" list="pages" name="pageLink" id="pageLink" listKey="code" listValue="getShortFullTitle(currentLang.code)" 
				value="%{showlet.config.get('pageLink')}" headerKey="" headerValue="- %{getText('label.select')} -" />
	</p>
	
	<s:iterator var="lang" value="langs">
		<p>
			<label for="linkDescr_<s:property value="#lang.code" />"  class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code" />)</span><s:text name="label.link.descr"/>:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="linkDescr_%{#lang.code}" id="linkDescr_%{#lang.code}" value="%{showlet.config.get('linkDescr_' + #lang.code)}" cssClass="text" />
		</p>
	</s:iterator>

</div>
</fieldset>

<%-- USER FILTERS - START BLOCK --%>
<fieldset><legend><s:text name="title.filters.search" /></legend>
	<p>
		<label for="userFilterKey" class="basic-mint-label"><s:text name="label.filter" />:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="userFilterKey" id="userFilterKey" list="allowedUserFilterTypes" listKey="key" listValue="value" cssClass="text" />
		<wpsf:submit useTabindexAutoIncrement="true" action="addUserFilter" value="%{getText('label.add')}" cssClass="button" />
	</p>

	<p class="noscreen">
		<wpsf:hidden name="userFilters" value="%{getShowlet().getConfig().get('userFilters')}" />
	</p>
	
<s:if test="null != userFiltersProperties && userFiltersProperties.size() > 0" >
	<table class="generic margin-bit-top" summary="<s:text name="note.page.contentListViewer.frontendFilters.summary" />">
	<caption><span><s:text name="title.filters.search" /></span></caption>
	<tr>
		<th><abbr title="<s:text name="label.number" />">N</abbr></th>
		<th><s:text name="name.filterDescription" /></th>
		<th class="icon" colspan="3"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th> 
	</tr>
	<s:iterator value="userFiltersProperties" id="userFilter" status="rowstatus">
	<tr>
		<td class="rightText"><s:property value="#rowstatus.index+1"/></td>
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
		<td class="icon">
			<wpsa:actionParam action="moveUserFilter" var="actionName" >
				<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
				<wpsa:actionSubParam name="movement" value="UP" />
			</wpsa:actionParam>
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>		
			<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')}" />
		</td>
		<td class="icon">
			<wpsa:actionParam action="moveUserFilter" var="actionName" >
				<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
				<wpsa:actionSubParam name="movement" value="DOWN" />
			</wpsa:actionParam>
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
			<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')}" />
		</td>
		<td class="icon">
			<wpsa:actionParam action="removeUserFilter" var="actionName" >
				<wpsa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			</wpsa:actionParam>
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
			<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image"  src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />
		</td>	
	</tr>
	</s:iterator>
	</table>
</s:if>
<s:else>
	<p><s:text name="note.filters.none" /></p>		
</s:else>

</fieldset>
<%-- USER FILTERS - END BLOCK --%>

<fieldset><legend><s:text name="title.publishingOptions" /></legend>
<p>
	<label for="modelIdMaster"><s:text name="label.contentModelMaster" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="modelIdMaster" id="modelIdMaster" value="%{getShowlet().getConfig().get('modelIdMaster')}" 
		list="%{getModelsForContentType(showlet.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="text" />
</p>

<p>
	<label for="modelIdPreview"><s:text name="label.contentModelPreview" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="modelIdPreview" id="modelIdPreview" value="%{getShowlet().getConfig().get('modelIdPreview')}" 
		list="%{getModelsForContentType(showlet.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="text" />
</p>

<p>
	<label for="maxElements" class="basic-mint-label"><s:text name="label.maxElements" />:</label>
	<wpsf:select name="maxElements" id="maxElements" value="%{getShowlet().getConfig().get('maxElements')}" 
		headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="text" />
</p>

</fieldset>

<p class="centerText"><wpsf:submit useTabindexAutoIncrement="true" action="saveConfig" value="%{getText('label.save')}" cssClass="button" /></p>

</s:else>

</s:form>

</div>
</div>