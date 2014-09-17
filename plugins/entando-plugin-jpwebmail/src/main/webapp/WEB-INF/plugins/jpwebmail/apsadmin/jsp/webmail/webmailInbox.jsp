<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="webmail" uri="/webmail-core" %>

<s:form action="moveIntoFolder" >
<p class="noscreen">
	<wpsf:hidden name="currentFolderName" />
</p>

<s:if test="hasActionErrors()">
	<div class="message message_error">	
	<h3><s:text name="message.title.ActionErrors" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="true" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>

<wpsa:subset source="messages" count="10" objectName="groupMessages" advanced="true" offset="5">
<s:set name="group" value="#groupMessages" />

<h1>MESSAGGI</h1>
<div class="paginazione">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<table class="generic" >
<caption>CAPTION</caption>
<tr>
	<th><abbr title="NUMERO">N.</abbr></th>
	<th><abbr title="STATO">S.</abbr></th>
	<th><s:if test="%{isSentFolder()}">DESTINATARI</s:if><s:else>DA</s:else></th>
	<th>SUBJECT</th>
	<th>DATA</th>	
</tr>
<s:iterator id="message" status="count">
<wpsa:set name="messagePosition" value="#groupMessages.begin + #count.index" />
<tr>
	<td><input type="checkbox" <s:if test="selectedAllMessages">checked="checked"</s:if> name="messageIndexes" id="<s:property value="%{'message_' + #count.index}" />" value="<s:property value="#messagePosition" />" class="checkbox" /><label for="<s:property value="%{'message_' + #count.index}" />"><s:property value="#messagePosition + 1" /></label></td>
	<td class="centerText messageStatus">
		<s:if test="!isSeen(#message)">
			<img src="<wp:resourceURL />plugins/jpwebmail/static/img/mail-message-unread.png" alt="NON LETTO" title="NON LETTO" />
		</s:if>
		<s:else>
			<img src="<wp:resourceURL />plugins/jpwebmail/static/img/mail-message-read.png" alt="LETTO" title="LETTO" />		
		</s:else>
	</td>
	<td>
	<s:if test="%{isSentFolder()}">
		<s:set name="recipientsTo" value="getTo(#message)"></s:set>
		<s:if test="#recipientsTo == null || #recipientsTo.length == 0">NESSUN DESTINATARIO</s:if>
		<s:else>
			<s:if test="#recipientsTo.length > 1">
			MOLTI DESTINATARI
			</s:if>
			<s:else>
			<wpsa:set name="getFromAddress" value="#recipientsTo[0]"></wpsa:set>
			<s:include value="inc/recipient.jsp" />
			</s:else>
		</s:else>
	</s:if>
	<s:else>
		<s:set name="getFromAddress" value="#message.from[0]"></s:set>
		<s:include value="inc/recipient.jsp" />
	</s:else>
	</td>
	<td>
		<s:set name="messageSubject" value="#message.subject"></s:set>
		<a href="<s:url action="openMessage"><s:param name="messageIndex" value="#messagePosition" /><s:param name="currentFolderName" value="currentFolderName" /></s:url>"><s:if test="%{#messageSubject == null || ''.equals(#messageSubject)}">NESSUN OGGETTO</s:if><s:else><s:property value="#messageSubject" /></s:else></a>
	</td>
	<td class="centerText">
		<s:bean name="java.util.Date" id="now"></s:bean>
		<wpsa:set name="today"><s:date name="now" format="yyyy-MM-dd" /></wpsa:set>
		<wpsa:set name="messageDate"><s:date name="#message.receivedDate" format="yyyy-MM-dd" /></wpsa:set>
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

<p>
SELEZIONA: <a href="<s:url action="selectAllMessages"><s:param name="selectedAllMessages" value="true" /><s:param name="currentFolderName" value="currentFolderName" /><s:param name="%{'pagerItem_' + #group.currItem}" value="%{'pagerItem_' + #group.currItem}" /></s:url>">TUTTI</a>, 
<a href="<s:url action="deselectAllMessages"><s:param name="selectedAllMessages" value="false" /><s:param name="currentFolderName" value="currentFolderName" /><s:param name="%{'pagerItem_' + #group.currItem}" value="%{'pagerItem_' + #group.currItem}" /></s:url>">NESSUNO</a>, 
</p>

<s:include value="inc/actions.jsp" />

<div class="paginazione">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>
</wpsa:subset>

</s:form>