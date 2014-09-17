<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-navigatorConfig">
<h2 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h2>

<h3><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h3>

<s:form action="saveListViewerConfig" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" id="formform" theme="simple">
<p class="noscreen">
	<wpsf:hidden name="pageCode" />
	<wpsf:hidden name="frame" />
	<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
</p>

	<s:if test="hasFieldErrors()">
		<div class="alert">
		<p><strong><s:text name="message.title.FieldErrors" /></strong></p>
			<ul class="unstyled">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
	</s:if>

<s:if test="showlet.config['contentType'] == null">
	<%-- choose content type --%>
	<fieldset class="form-inline"><legend><s:text name="title.contentInfo" /></legend>
		<div class="control-group">
			<label class="control-label" for="contentType"><s:text name="label.type"/></label>
			<div class="controls">
				<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
				<s:url var="configListViewerUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="configListViewer" />
				<sj:submit targets="form-container" href="%{#configListViewerUrlVar}" value="%{getText('label.continue')}" indicator="indicator" button="true" cssClass="button" />
			</div>
		</div>
	</fieldset>
</s:if>
<s:else>
	<fieldset class="margin-bit-bottom"><legend><s:text name="title.contentInfo" /></legend>
	<p>
		<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="text" />
		<s:url var="changeContentTypeUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="changeContentType" />
		<sj:submit targets="form-container" href="%{#changeContentTypeUrlVar}" value="%{getText('label.change')}" indicator="indicator" button="true" cssClass="button" />
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
			<s:url var="addCategoryUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="addCategory" />
			<sj:submit targets="form-container" href="%{#addCategoryUrlVar}" value="%{getText('label.join')}" indicator="indicator" button="true" cssClass="button" />
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
					<wpfssa:actionParam action="removeCategory" var="removeCategoryActionNameVar" >
						<wpfssa:actionSubParam name="categoryCode" value="%{#categoryCodeVar}" />
					</wpfssa:actionParam>
					<s:url var="removeCategoryActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#removeCategoryActionNameVar}" />
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/22x22/delete.png</s:set>
					<sj:submit type="image" targets="form-container" value="%{getText('label.remove')}" button="true" 
						href="%{#removeCategoryActionVar}" src="%{#iconImagePath}" title="%{getText('label.remove') + ': ' + #showletCategory.getFullTitle(currentLang.code)}" />
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

	<fieldset class="well well-small"><legend><s:text name="title.filterOptions" /></legend>
	<p>
		<label for="filterKey" class="basic-mint-label"><s:text name="label.filter" />:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="filterKey" id="filterKey" list="allowedFilterTypes" listKey="key" listValue="value" cssClass="text" />
		<s:url var="setFilterTypeUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="setFilterType" />
		<sj:submit targets="form-container" href="%{#setFilterTypeUrlVar}" value="%{getText('label.add')}" indicator="indicator" button="true" cssClass="button" />
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
			<wpfssa:actionParam action="moveFilter" var="moveFilterActionNameVar" >
				<wpfssa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
				<wpfssa:actionSubParam name="movement" value="UP" />
			</wpfssa:actionParam>
			<s:url var="moveFilterActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#moveFilterActionNameVar}" />
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/go-up.png</s:set>
			<sj:submit type="image" targets="form-container" value="%{getText('label.moveUp')}" button="true" 
				href="%{#moveFilterActionVar}" src="%{#iconImagePath}" title="%{getText('label.moveUp')}" />
		</td>
		<td class="icon">	
			<wpfssa:actionParam action="moveFilter" var="moveFilterActionNameVar" >
				<wpfssa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
				<wpfssa:actionSubParam name="movement" value="DOWN" />
			</wpfssa:actionParam>
			<s:url var="moveFilterActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#moveFilterActionNameVar}" />
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/go-down.png</s:set>
			<sj:submit type="image" targets="form-container" value="%{getText('label.moveDown')}" button="true" 
				href="%{#moveFilterActionVar}" src="%{#iconImagePath}" title="%{getText('label.moveDown')}" />
		</td>
		<td class="icon">
			<wpfssa:actionParam action="removeFilter" var="removeFilterActionNameVar" >
				<wpfssa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			</wpfssa:actionParam>
			<s:url var="removeFilterActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#removeFilterActionNameVar}" />
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
			<sj:submit type="image" targets="form-container" value="%{getText('label.remove')}" button="true" 
				href="%{#removeFilterActionVar}" src="%{#iconImagePath}" title="%{getText('label.remove')}" />
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
<fieldset class="well well-small"><legend><s:text name="title.filters.search" /></legend>
	<p>
		<label for="userFilterKey" class="basic-mint-label"><s:text name="label.filter" />:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="userFilterKey" id="userFilterKey" list="allowedUserFilterTypes" listKey="key" listValue="value" cssClass="text" />
		<s:url var="addUserFilterUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="addUserFilter" />
		<sj:submit targets="form-container" href="%{#addUserFilterUrlVar}" value="%{getText('label.add')}" indicator="indicator" button="true" cssClass="button" />
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
			<wpfssa:actionParam action="moveUserFilter" var="moveUserFilterActionNameVar" >
				<wpfssa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
				<wpfssa:actionSubParam name="movement" value="UP" />
			</wpfssa:actionParam>
			<s:url var="moveUserFilterActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#moveUserFilterActionNameVar}" />
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/go-up.png</s:set>
			<sj:submit type="image" targets="form-container" value="%{getText('label.moveUp')}" button="true" 
				href="%{#moveUserFilterActionVar}" src="%{#iconImagePath}" title="%{getText('label.moveUp')}" />
		</td>
		<td class="icon">	
			<wpfssa:actionParam action="moveUserFilter" var="moveUserFilterActionNameVar" >
				<wpfssa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
				<wpfssa:actionSubParam name="movement" value="DOWN" />
			</wpfssa:actionParam>
			<s:url var="moveUserFilterActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#moveUserFilterActionNameVar}" />
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/go-down.png</s:set>
			<sj:submit type="image" targets="form-container" value="%{getText('label.moveDown')}" button="true" 
				href="%{#moveUserFilterActionVar}" src="%{#iconImagePath}" title="%{getText('label.moveDown')}" />
		</td>
		<td class="icon">
			<wpfssa:actionParam action="removeUserFilter" var="removeUserFilterActionNameVar" >
				<wpfssa:actionSubParam name="filterIndex" value="%{#rowstatus.index}" />
			</wpfssa:actionParam>
			<s:url var="removeUserFilterActionVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="%{#removeUserFilterActionNameVar}" />
			<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
			<sj:submit type="image" targets="form-container" value="%{getText('label.remove')}" button="true" 
				href="%{#removeUserFilterActionVar}" src="%{#iconImagePath}" title="%{getText('label.remove')}" />
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
	<label for="modelId" class="basic-mint-label"><s:text name="label.contentModel" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="modelId" id="modelId" value="%{getShowlet().getConfig().get('modelId')}" 
		list="%{getModelsForContentType(showlet.config['contentType'])}" headerKey="" headerValue="%{getText('label.default')}" listKey="id" listValue="description" cssClass="text" />
</p>

<p>
	<label for="maxElemForItem" class="basic-mint-label"><s:text name="label.maxElementsForItem" />:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="maxElemForItem" id="maxElemForItem" value="%{getShowlet().getConfig().get('maxElemForItem')}" 
		headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="text" />
</p>

<p>
	<label for="maxElements" class="basic-mint-label"><s:text name="label.maxElements" />:</label>
	<wpsf:select name="maxElements" id="maxElements" value="%{getShowlet().getConfig().get('maxElements')}" 
		headerKey="" headerValue="%{getText('label.all')}" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10,15:15,20:20}" cssClass="text" />
</p>

</fieldset>

<p class="centerText">
	<s:url var="saveListViewerConfigUrlVar" namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" action="saveListViewerConfig" />
	<sj:submit targets="form-container" href="%{#saveListViewerConfigUrlVar}" value="%{getText('label.save')}" indicator="indicator" button="true" cssClass="button" />
</p>

</s:else>

</s:form>

</div>