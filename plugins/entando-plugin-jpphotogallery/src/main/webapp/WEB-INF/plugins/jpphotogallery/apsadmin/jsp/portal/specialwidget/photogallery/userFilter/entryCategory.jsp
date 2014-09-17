<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>

<div id="main">
<h2><s:text name="title.configPage" /></h2>

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<div class="subsection-light">
<h3><s:text name="title.configPage.youAreDoing" /></h3>

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
<h3 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" /></h3>
<h4 class="margin-bit-bottom"><s:text name="title.userFilterCategoryConfigure" /></h4>

<s:form namespace="/do/jpphotogallery/Page/SpecialWidget/Photogallery">
<p class="noscreen">
	<wpsf:hidden name="pageCode" />
	<wpsf:hidden name="frame" />
	<wpsf:hidden name="widgetTypeCode" />
</p>

	<s:if test="hasFieldErrors()">
<div class="message message_error">
<h5><s:text name="message.title.FieldErrors" /></h5>
	<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
		<li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
	</ul>
</div>
	</s:if>

<p class="noscreen">
	<wpsf:hidden name="contentType" />
	<wpsf:hidden name="categories" value="%{#parameters['categories']}" />
	<wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}" />
	<wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}" />
	<wpsf:hidden name="filters" />
	<wpsf:hidden name="modelIdMaster" />
	<wpsf:hidden name="modelIdPreview" />
	<wpsf:hidden name="pageLink" value="%{#parameters['pageLink']}" />
	<s:iterator id="lang" value="langs">
	<wpsf:hidden name="%{'linkDescr_' + #lang.code}" value="%{#parameters['linkDescr_' + #lang.code]}" />
	<wpsf:hidden name="%{'title_' + #lang.code}" value="%{#parameters['title_' + #lang.code]}" />
	</s:iterator>
	<wpsf:hidden name="userFilterKey" value="category" />
</p>

	<p>
		<label for="userFilterCategoryCode" class="basic-mint-label"><s:text name="label.userFilterCategory" />:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="userFilterCategoryCode" id="userFilterCategoryCode" list="categories" listKey="code" listValue="getShortFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.all')}" cssClass="text" />
	</p>

<p class="centerText">
	<wpsf:submit useTabindexAutoIncrement="true" action="addUserFilter" value="%{getText('label.save')}" cssClass="button" />
</p>

</s:form>

</div>
</div>