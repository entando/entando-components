<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/searchRecipients.action" />" method="post" >
	<p class="noscreen">
	<wpsf:hidden name="actualRecipient" />
	<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
		<wpsf:hidden name="selectedReceivers" value="%{#selectedReceiver}" id="selectedReceivers-%{#rowstatus.index}"/>
	</s:iterator>
	</p>

<s:if test="actualRecipient == 1 ">
	<h3><wp:i18n key="jpwebmail_MSG_TO_LABEL" /></h3>
</s:if>
<s:elseif test="actualRecipient == 2 ">
	<h3><wp:i18n key="jpwebmail_MSG_CC_LABEL" /></h3>
</s:elseif>
<s:elseif test="actualRecipient == 3 ">
	<h3><wp:i18n key="jpwebmail_MSG_BCC_LABEL" /></h3>
</s:elseif>
<s:else>
	<h3><wp:i18n key="jpwebmail_ERROR_RECIPIENT" /></h3>
</s:else>

<p><wp:i18n key="jpwebmail_ADDRESS_BOOK_INTRO" /></p>
	<p class="actions">
		<label for="searchLabel"><wp:i18n key="jpwebmail_SEARCH" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="searchLabel" cssClass="text" />
		<s:set name="iconSearch"><wp:resourceURL />plugins/jpwebmail/static/img/system-search.png</s:set>
		<s:set name="addressSearch"><wp:i18n key="jpwebmail_SEARCH" /></s:set>	
		<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconSearch}" value="%{#addressSearch}" title="%{#addressSearch}" />
	</p>
</form>

<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/searchRecipients.action" />" method="post" enctype="multipart/form-data">
<p class="noscreen">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="actualRecipient" />
	<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
		<wpsf:hidden name="selectedReceivers" value="%{#selectedReceiver}" id="selectedReceivers-%{#rowstatus.index}"/>
	</s:iterator>
</p>

<c:choose>
<c:when test="${not empty searchedReceivers}">
	<wpsa:subset source="searchedReceivers" count="10" objectName="contacts" advanced="true" offset="5">
	<s:set name="group" value="#contacts" />
	
	<div class="pager"><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" /></div>
	
	<p class="centerText"><wp:i18n key="jpwebmail_ADDRESS_LIST" /></p>
		<ul>
			<s:iterator id="contact" status="counter">
				<li>
				<input type="checkbox" name="selectedReceivers"	id="selectedReceivers_<s:property value="#counter.index"/>" value="<s:property value="#contact.username"/>" /><label for="selectedReceivers_<s:property value="#counter.index"/>"><s:property value="#contact.fullName" />&#32;&lt;<s:property value="#contact.emailAddress"/>&gt;</label>
				</li>
			</s:iterator>
		</ul>
		<div class="pager"><s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" /></div>
			<s:set name="iconAddressAdd"><wp:resourceURL />plugins/jpwebmail/static/img/list-add.png</s:set>
			<s:set name="iconAddressAddText"><wp:i18n key="jpwebmail_ADDRESS_ADD" /></s:set>	
			
			<p><wpsf:submit useTabindexAutoIncrement="true" action="addRecipients" type="image" src="%{#iconAddressAdd}" value="%{#iconAddressAddText}" title="%{#iconAddressAddText}" /></p>
	</wpsa:subset>
</c:when>
<c:otherwise>
	<p><wp:i18n key="jpwebmail_ADDRESS_NO_RESULT" /></p>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${not empty selectedReceivers}">
	<table class="generic" summary="<wp:i18n key="jpwebmail_CONTACT_LIST_SUMMARY" />">
	<caption><wp:i18n key="jpwebmail_CONTACT_LIST_CAPTION" /></caption>
		<tr>
		<th><wp:i18n key="jpwebmail_USER" /></th>
		<th><wp:i18n key="jpwebmail_ADDRESS" /></th>
		<th><abbr title="<wp:i18n key="jpwebmail_OPERATIONS" />">-</abbr></th>
		</tr>
		
		<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
		<tr>
		<s:set name="currentUser" value="%{getContact(#selectedReceiver)}"/>
		<td><s:property value="#currentUser.fullName" /></td>
		<td><s:property value="#currentUser.emailAddress"/></td>
		<td class="centerText">
			<wpsa:actionParam action="removeRecipient" var="removeRecipientActionName" >
				<wpsa:actionSubParam name="actualReceiver" value="%{#currentUser.username}" />
			</wpsa:actionParam>
			<s:set name="iconAddressRemove"><wp:resourceURL />plugins/jpwebmail/static/img/edit-delete.png</s:set>
			<s:set name="iconAddressRemoveText"><wp:i18n key="jpwebmail_ADDRESS_REMOVE" /></s:set>	
			<wpsf:submit useTabindexAutoIncrement="true" action="%{#removeRecipientActionName}" type="image" src="%{#iconAddressRemove}" value="%{#iconAddressRemoveText}" title="%{#iconAddressRemoveText}: %{#currentUser.fullName} " />
		</td>
		</tr>
		</s:iterator>
	</table>
</c:when>
</c:choose>
</form>

<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/searchRecipients.action" />" method="post" >
<p class="noscreen">
	<wpsf:hidden name="text" />
	<wpsf:hidden name="actualRecipient" />
	<s:iterator value="selectedReceivers" id="selectedReceiver" status="rowstatus">
		<wpsf:hidden name="selectedReceivers" value="%{#selectedReceiver}" id="selectedReceivers-%{#rowstatus.index}"/>
	</s:iterator>
</p>
<p class="centerText">
	<s:set name="cancelLabel"><wp:i18n key="jpwebmail_CANCEL" /></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="cancel" value="%{cancelLabel}" title="%{cancelLabel}" cssClass="buttonBorded" />
	<s:set name="confirmLabel"><wp:i18n key="jpwebmail_CONFIRM" /></s:set>	
	<wpsf:submit useTabindexAutoIncrement="true" action="joinRecipients" value="%{#confirmLabel}" title="%{#confirmLabel}" cssClass="buttonBorded" />
</p>
</form>