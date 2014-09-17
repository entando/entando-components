<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset>
	<legend><s:text name="title.categoriesManagement"/></legend>
<p>
<wpsf:select useTabindexAutoIncrement="true" name="categoryCode" id="categoryCode" list="%{getCategories(content.typeCode)}" 
	listKey="code" listValue="getFullTitle(currentLang.code)" headerKey="" headerValue="%{getText('label.select')}" 
	value="%{getShowlet().getConfig().get('category')}" cssClass="text" />
<wpsf:submit useTabindexAutoIncrement="true" action="joinPrivateCategory" value="%{getText('label.join')}" cssClass="button" />
</p>

<s:set var="contentCategories" value="content.categories" />

<s:if test="#contentCategories != null && #contentCategories.size() > 0">

<table class="generic" summary="<s:text name="note.contentCategories.summary"/>: <s:property value="content.descr" />">
<caption><span><s:text name="title.contentCategories.list"/></span></caption>
<tr>	
	<th><s:text name="label.category"/></th>
	<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
</tr>
<s:iterator value="#contentCategories" id="contentCategory">
<tr>
	<td><s:property value="#contentCategory.getFullTitle(currentLang.code)"/></td>
	<td class="icon">
		<wpsa:actionParam action="removePrivateCategory" var="actionName" >
			<wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
		</wpsa:actionParam>
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
		<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ' ' + #contentCategory.defaultFullTitle}" />
	</td>
</tr>
</s:iterator>
</table>
</s:if>
</fieldset>