<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<div id="form-container" class="widget_form jpfrontshortcut-frameconfig-navigatorConfig">
<h2 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h2>

<h3><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h3>

<s:set var="showletType" value="%{getShowletType(widgetTypeCode)}"></s:set>
<h3 class="margin-more-top"><s:text name="name.widget" />:&#32;<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" /></h3>
<h4 class="margin-bit-bottom"><s:text name="title.userFilterCategoryConfigure" /></h4>

<s:form namespace="/do/jpfrontshortcut/Page/SpecialWidget/ListViewer" id="formform" theme="simple">
<p class="noscreen">
	<wpsf:hidden name="pageCode" />
	<wpsf:hidden name="frame" />
	<wpsf:hidden name="widgetTypeCode" />
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

<p class="noscreen">
	<wpsf:hidden name="contentType" />
	<wpsf:hidden name="categories" value="%{#parameters['categories']}" />
	<wpsf:hidden name="orClauseCategoryFilter" value="%{#parameters['orClauseCategoryFilter']}" />
	<wpsf:hidden name="userFilters" value="%{#parameters['userFilters']}" />
	<wpsf:hidden name="filters" />
	<wpsf:hidden name="modelId" />
	<wpsf:hidden name="maxElemForItem" />
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
	<s:url var="addUserFilterUrlVar" action="addUserFilter" />
	<sj:submit targets="form-container" href="%{#addUserFilterUrlVar}" 
		value="%{getText('label.save')}" indicator="indicator" button="true" cssClass="button" />
</p>
</s:form>

</div>