<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:form action="intro">
<p class="noscreen">
	<wpsf:hidden name="messageIndex" />
	<wpsf:hidden name="currentFolderName" />
	<wpsf:hidden name="messageIndexes" value="%{messageIndex}" />
</p>

<s:set name="currentMessage" value="message" />
<h3>MESSAGGIO: <s:property value="#currentMessage.subject" /></h3>

<s:include value="inc/actions.jsp" />

<!-- INIZIO BLOCCO OPZIONI  -->

<s:set name="iconMsgReply"><wp:resourceURL />plugins/jpwebmail/static/img/mail-reply-sender.png</s:set>
<s:set name="iconMsgReplyText">RISPONDI</s:set>
<s:set name="iconMsgReplyAll"><wp:resourceURL />plugins/jpwebmail/static/img/mail-reply-all.png</s:set>
<s:set name="iconMsgReplyAllText">RISPONDI A TUTTI</s:set>
<s:set name="iconMsgForward"><wp:resourceURL />plugins/jpwebmail/static/img/mail-forward.png</s:set>
<s:set name="iconMsgForwardText">INOLTRA</s:set>
<s:set name="iconMsgGoNext"><wp:resourceURL />plugins/jpwebmail/static/img/go-next.png</s:set>
<s:set name="iconMsgGoNextText">SUCCESSIVA</s:set>
<s:set name="iconMsgGoPrevious"><wp:resourceURL />plugins/jpwebmail/static/img/go-previous.png</s:set>
<s:set name="iconMsgGoPreviousText">PRECEDENTE</s:set>

<p>
<a href="<s:url action="reply"><s:param name="messageIndex" value="messageIndex" /><s:param name="currentFolderName" value="currentFolderName" /></s:url>" 
	title="<s:property value="%{#iconMsgReplyText}" />"><img alt="<s:property value="%{#iconMsgReplyText}" />" src="<s:property value="%{#iconMsgReply}" />" /></a>
<span class="noscreen">| </span>
<a href="<s:url action="replyAll"><s:param name="messageIndex" value="messageIndex" /><s:param name="currentFolderName" value="currentFolderName" /></s:url>" 
	title="<s:property value="%{#iconMsgReplyAllText}" />"><img alt="<s:property value="%{#iconMsgReplyAllText}" />" src="<s:property value="%{#iconMsgReplyAll}" />" /></a>
<span class="noscreen">| </span>
<a href="<s:url action="forward"><s:param name="messageIndex" value="messageIndex" /><s:param name="currentFolderName" value="currentFolderName" /></s:url>" 
	title="<s:property value="iconMsgForwardText" />"><img alt="<s:property value="%{#iconMsgForwardText}" />" src="<s:property value="%{#iconMsgForward}" />" /></a>
<span class="noscreen">| </span>
<s:if test="messageIndex > 0">
<a href="<s:url action="openMessage"><s:param name="messageIndex" value="messageIndex-1" /><s:param name="currentFolderName" value="currentFolderName" /></s:url>" 
	title="<s:property value="iconMsgGoPreviousText" />"><img alt="<s:property value="%{#iconMsgGoPreviousText}" />" src="<s:property value="%{#iconMsgGoPrevious}" />" /></a>	
</s:if>
<span class="noscreen">| </span>
<s:if test="%{messageIndex < (currentFolderMessages.size()-1)}">
<a href="<s:url action="openMessage"><s:param name="messageIndex" value="messageIndex+1" /><s:param name="currentFolderName" value="currentFolderName" /></s:url>" 
	title="<s:property value="iconMsgGoNextText" />"><img alt="<s:property value="%{#iconMsgGoNextText}" />" src="<s:property value="%{#iconMsgGoNext}" />" /></a>
</s:if>
</p>

<!-- FINE BLOCCO OPZIONI  -->

<h4 class="accordion_toggler">DETTAGLI</h4>
<div class="accordion_element">
<dl>
	<dt>OGGETTO:</dt>
		<dd><s:property value="#currentMessage.subject" /></dd>
	<dt>DA:</dt>
		<dd><s:property value="#currentMessage.from" /></dd>
	<dt>A:</dt>
		<dd><s:property value="%{getTo(#currentMessage)}" /></dd>
	<dt>CC:</dt>
		<dd><s:property value="%{getCc(#currentMessage)}" /></dd>
	<s:if test="%{getBcc(#currentMessage)!=''}">
	<dt>CCN:</dt>
		<dd><s:property value="%{getBcc(#currentMessage)}" /></dd>
	</s:if>
	<dt>DATA INVIO:</dt>
		<dd><s:date name="#currentMessage.sentDate" format="dd MMMM yyyy HH:mm" /></dd>
	<dt>DATA RICEZIONE:</dt>
		<dd><s:date name="#currentMessage.receivedDate" format="dd MMMM yyyy HH:mm" /></dd>
</dl>
</div>

<h4>TESTO</h4>
<pre>
<s:property value="%{getContent(message)}"/>
</pre>

<s:set name="attachmentInfos" value="%{getAttachmentInfos(#currentMessage)}"></s:set>
<s:if test="#attachmentInfos.size() > 0">
	<h3>ALLEGATI</h3>
	<ol>
	<s:iterator value="#attachmentInfos" id="attachment">
		<li><a href="<s:url action="openAttachment"><s:param name="messageIndex" value="messageIndex" /><s:param name="currentFolderName" value="currentFolderName" /><s:param name="attachmentNumber" value="#attachment.number" /><s:param name="subPartAttachmentNumber" value="#attachment.subPartNumber" /></s:url>" ><s:property value="#attachment.fileName" /> (<s:property value="#attachment.size" /> bytes)</a></li>
	</s:iterator>
	</ol>
</s:if>

</s:form>