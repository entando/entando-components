<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:if test="#group.size > #group.max">
	
	<s:if test="%{1 == #group.currItem}">
		<s:set id="goPrevious" name="goPrevious"><wp:i18n key="jpaddressbook_PREV" /></s:set>
	</s:if>
	<s:else>
		<s:set id="goPrevious" name="goPrevious"><wp:i18n key="jpaddressbook_PREV" /></s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set id="goNext" name="goNext"><wp:i18n key="jpaddressbook_PREV" /></s:set>
	</s:if>
	<s:else>    
		<s:set id="goNext" name="goNext"><wp:i18n key="jpaddressbook_NEXT" /></s:set>
	</s:else>

	<p class="paginazione">
		<wpsf:submit useTabindexAutoIncrement="true" name="%{'pagerItem_' + #group.prevItem}" value="%{#goPrevious}" disabled="%{1 == #group.currItem}" />	
		<s:subset source="#group.items" ><s:iterator id="item"><wpsf:submit useTabindexAutoIncrement="true" name="%{'pagerItem_' + #item}" value="%{#item}" disabled="%{#item == #group.currItem}" /></s:iterator></s:subset>
		<wpsf:submit useTabindexAutoIncrement="true" name="%{'pagerItem_' + #group.nextItem}" value="%{#goNext}" disabled="%{#group.maxItem == #group.currItem}" />
	</p>
	
</s:if>