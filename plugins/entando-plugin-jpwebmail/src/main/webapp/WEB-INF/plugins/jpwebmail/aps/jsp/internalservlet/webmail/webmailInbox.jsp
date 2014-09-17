<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="webmail" uri="/webmail-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpwebmail/static/css/webmail.css" />
<div class="webmail">
<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/moveIntoFolder.action" />" method="post">
<p class="noscreen">
	<wpsf:hidden name="currentFolderName" />
</p>

<div class="colonna1">
	<s:include value="inc/inc_folderInfo.jsp" />
</div>

<div class="colonna2">

<s:if test="hasActionErrors()">
<div class="message message_error">
<h3><wp:i18n key="ERROR" /></h3>
	<ul>
	<s:iterator value="actionErrors">
		<li><s:property escape="true"/></li>
	</s:iterator>
	</ul>
</div>
</s:if>

<wpsa:subset source="messages" count="10" objectName="groupMessages" advanced="true" offset="5">
<s:set name="group" value="#groupMessages" />

<h3><wp:i18n key="jpwebmail_MESSAGES" /></h3>

<s:if test="%{messages.size() > 0}">
<div class="paginazione">
	<s:include value="inc/pagerInfo.jsp" />
	<s:include value="inc/pager_formBlock.jsp" />
</div>

<%--
	<s:include value="inc/inc_actions.jsp" />
--%>

<div class="centerText">
<table class="generic" summary="<wp:i18n key="jpwebmail_MSG_LIST_SUMMARY" />">
<caption><wp:i18n key="jpwebmail_MSG_LIST_CAPTION" /></caption>
<tr>
	<th><abbr title="<wp:i18n key="jpwebmail_MSG_NUMBER" />"><wp:i18n key="jpwebmail_MSG_NUMBER_ABBR" /></abbr></th>
	<th><abbr title="<wp:i18n key="jpwebmail_MSG_STATUS" />"><wp:i18n key="jpwebmail_MSG_STATUS_ABBR" /></abbr></th>
	<th><s:if test="%{isSentFolder()}"><wp:i18n key="jpwebmail_RECIPIENTS" /></s:if><s:else><wp:i18n key="jpwebmail_MSG_FROM" /></s:else></th>
	<th><wp:i18n key="jpwebmail_MSG_SUBJECT" /></th>
	<th><wp:i18n key="jpwebmail_MSG_DATE" /></th>
</tr>
<s:iterator id="message" status="count">
<s:set name="messagePosition" value="#groupMessages.begin + #count.index" />
<tr>
	<td class="doubleItems"><input type="checkbox" <s:if test="selectedAllMessages">checked="checked"</s:if> name="messageIndexes" id="<s:property value="%{'message_' + #count.index}" />" value="<s:property value="#messagePosition" />" class="checkbox" /><label for="<s:property value="%{'message_' + #count.index}" />"><s:property value="#messagePosition + 1" /></label></td>
	<td class="centerText messageStatus">
		<s:if test="!isSeen(#message)">
			<img src="<wp:resourceURL />plugins/jpwebmail/static/img/mail-message-unread.png" alt="<wp:i18n key="jpwebmail_MSG_UNREAD" />" title="<wp:i18n key="jpwebmail_MSG_UNREAD" />" />
		</s:if>
		<s:else>
			<img src="<wp:resourceURL />plugins/jpwebmail/static/img/mail-message-read.png" alt="<wp:i18n key="jpwebmail_MSG_READ" />" title="<wp:i18n key="jpwebmail_MSG_READ" />" />		
		</s:else>
	</td>
	<td>
	<s:if test="%{isSentFolder()}">
		<s:set name="recipientsTo" value="getTo(#message)"></s:set>
		<s:if test="#recipientsTo == null || #recipientsTo.length == 0"><wp:i18n key="jpwebmail_NO_RECIPIENT" /></s:if>
		<s:else>
			<s:if test="#recipientsTo.length > 1">
			<wp:i18n key="jpwebmail_MORE_RECIPIENTS" />
			</s:if>
			<s:else>
			<wpsa:set name="getFromAddress" value="#recipientsTo[0]"></wpsa:set>
			<s:include value="inc/inc_recipient.jsp" />
			</s:else>
		</s:else>
	</s:if>
	<s:else>
		<s:set name="getFromAddress" value="#message.from[0]"></s:set>
		<s:include value="inc/inc_recipient.jsp" />
	</s:else>
	</td>
	<td>
		<s:set name="messageSubject" value="#message.subject"></s:set>
		<a href="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/openMessage"><wp:parameter name="messageIndex"><s:property value="#messagePosition" /></wp:parameter><wp:parameter name="currentFolderName"><s:property value="currentFolderName" /></wp:parameter></wp:action>"><s:if test="%{#messageSubject == null || ''.equals(#messageSubject)}"><wp:i18n key="jpwebmail_MSG_SUBJECT_EMPTY" /></s:if><s:else><s:property value="#messageSubject" /></s:else></a>
	</td>
	<td class="centerText">
		<s:bean name="java.util.Date" id="now"></s:bean>
		<s:set name="today"><s:date name="now" format="yyyy-MM-dd" /></s:set>
		<s:set name="messageDate"><s:date name="#message.receivedDate" format="yyyy-MM-dd" /></s:set>
		<s:if test="%{#messageDate == #today}">
			<s:date name="#message.receivedDate" format="HH:mm" />
		</s:if>
		<s:else>
			<s:date name="#message.receivedDate" format="dd/MM/yyyy HH:mm" />		
		</s:else>
	</td>
</tr>
</s:iterator>
</table>

</div>

<p class="noStamp">
<c:set var="currentPagerItemVar"><s:property value="%{'pagerItem_' + #group.currItem}" /></c:set>
<wp:i18n key="jpwebmail_MSG_SELECT" />: <a href="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/selectAllMessages" >
													<wp:parameter name="selectedAllMessages">true</wp:parameter>
													<wp:parameter name="currentFolderName"><s:property value="currentFolderName" /></wp:parameter>
													<wp:parameter name="${currentPagerItemVar}"><s:property value="%{'pagerItem_' + #group.currItem}" /></wp:parameter>
												</wp:action>"><wp:i18n key="jpwebmail_ALL_MSG" /></a>, 
<a href="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/deselectAllMessages" >
			<wp:parameter name="selectedAllMessages">false</wp:parameter>
			<wp:parameter name="currentFolderName"><s:property value="currentFolderName" /></wp:parameter>
			<wp:parameter name="${currentPagerItemVar}"><s:property value="%{'pagerItem_' + #group.currItem}" /></wp:parameter>
		</wp:action>"><wp:i18n key="jpwebmail_NONE_MSG" /></a>
</p>

<s:include value="inc/inc_actions.jsp" />

<div class="paginazione">
	<s:include value="inc/pager_formBlock.jsp" />
</div>
</s:if>
<s:else>
<c:set var="startFolderPath">/ExtStr2/do/jpwebmail/Portal/WebMail/intro.action</c:set>
<p><wp:i18n key="jpwebmail_TABLE_EMPTY" />&#32;<a href="<wp:action path="${startFolderPath}" />" title="<wp:i18n key="jpwebmail_RETURN_START_FOLDER" />" ><wp:i18n key="jpwebmail_REFRESH_NOW" /></a></p>

<s:include value="inc/inc_actions.jsp" />

</s:else>
</wpsa:subset>

</div>
</form>
</div>