<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<wp:headInfo type="JS" info="../../plugins/jpwebmail/static/js/mootools-1.2-core.js" />
<wp:headInfo type="JS" info="../../plugins/jpwebmail/static/js/mootools-1.2-more.js" />
<wp:headInfo type="CSS" info="../../plugins/jpwebmail/static/css/webmail.css" />

<c:set var="js_accordion">

//per gli accordion
window.addEvent('domready', function(){
	var myAccordion = new Accordion($$('.accordion_toggler'), $$('.accordion_element'), {
	    show: -1,
	    duration: 'short',
	    alwaysHide: true
	});
	
	var myAnchor = new Element('img', {
	    'src': '<wp:resourceURL/>plugins/jpwebmail/static/img/media-eject.png',
	    'class': 'myClass',
	    'style': 'vertical-align: middle',
	    'alt': '<wp:i18n key="jpwebmail_OPEN" /> ',
	    'title': '<wp:i18n key="jpwebmail_OPEN" /> '	    
	});

	$$('.accordion_toggler').each(function(cToggler) {
		cToggler.appendText(' ');
		var anchorVar = myAnchor.clone();
		anchorVar.injectBottom(cToggler);
	});
	
});

</c:set>
<wp:headInfo type="JS_RAW" var="js_accordion" />

<div class="webmail">
<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/intro" />" method="post">
<p class="noscreen">
	<wpsf:hidden name="messageIndex" />
	<wpsf:hidden name="currentFolderName" />
	<wpsf:hidden name="messageIndexes" value="%{messageIndex}" />
</p>

<div class="colonna1">
	<s:include value="inc/inc_folderInfo.jsp" />
</div>

<div class="colonna2">
<s:set name="currentMessage" value="message" />
<h3><wp:i18n key="jpwebmail_MSG" />: <s:property value="#currentMessage.subject" /></h3>

<s:include value="inc/inc_actions.jsp" />

<!-- INIZIO BLOCCO OPZIONI  -->

<s:set name="iconMsgReply"><wp:resourceURL />plugins/jpwebmail/static/img/mail-reply-sender.png</s:set>
<s:set name="iconMsgReplyText"><wp:i18n key="jpwebmail_MSG_REPLY" /></s:set>
<s:set name="iconMsgReplyAll"><wp:resourceURL />plugins/jpwebmail/static/img/mail-reply-all.png</s:set>
<s:set name="iconMsgReplyAllText"><wp:i18n key="jpwebmail_MSG_REPLY_ALL" /></s:set>
<s:set name="iconMsgForward"><wp:resourceURL />plugins/jpwebmail/static/img/mail-forward.png</s:set>
<s:set name="iconMsgForwardText"><wp:i18n key="jpwebmail_MSG_FORWARD" /></s:set>
<s:set name="iconMsgGoNext"><wp:resourceURL />plugins/jpwebmail/static/img/go-next.png</s:set>
<s:set name="iconMsgGoNextText"><wp:i18n key="jpwebmail_MSG_GO_NEXT" /></s:set>
<s:set name="iconMsgGoPrevious"><wp:resourceURL />plugins/jpwebmail/static/img/go-previous.png</s:set>
<s:set name="iconMsgGoPreviousText"><wp:i18n key="jpwebmail_MSG_GO_PREVIOUS" /></s:set>

<p class="actionLink">
<c:set var="messageReplyPath">/ExtStr2/do/jpwebmail/Portal/WebMail/reply.action&amp;messageIndex=<s:property value="messageIndex" />&amp;currentFolderName=<s:property value="currentFolderName" /></c:set>
<a href="<wp:action path="${messageReplyPath}" />" title="<s:property value="%{#iconMsgReplyText}" />"><img alt="<s:property value="%{#iconMsgReplyText}" />" src="<s:property value="%{#iconMsgReply}" />" /></a>
<span class="noscreen">| </span>
<c:set var="messageReplyAllPath">/ExtStr2/do/jpwebmail/Portal/WebMail/replyAll.action&amp;messageIndex=<s:property value="messageIndex" />&amp;currentFolderName=<s:property value="currentFolderName" /></c:set>
<a href="<wp:action path="${messageReplyAllPath}" />" title="<s:property value="%{#iconMsgReplyAllText}" />"><img alt="<s:property value="%{#iconMsgReplyAllText}" />" src="<s:property value="%{#iconMsgReplyAll}" />" /></a>
<span class="noscreen">| </span>
<c:set var="messageForwardPath">/ExtStr2/do/jpwebmail/Portal/WebMail/forward.action&amp;messageIndex=<s:property value="messageIndex" />&amp;currentFolderName=<s:property value="currentFolderName" /></c:set>
<a href="<wp:action path="${messageForwardPath}" />" title="<s:property value="iconMsgForwardText" />"><img alt="<s:property value="%{#iconMsgForwardText}" />" src="<s:property value="%{#iconMsgForward}" />" /></a>
<span class="noscreen">| </span>
<s:if test="messageIndex > 0">
	<c:set var="openPrevMessagePath">/ExtStr2/do/jpwebmail/Portal/WebMail/openMessage.action&amp;messageIndex=<s:property value="messageIndex-1" />&amp;currentFolderName=<s:property value="currentFolderName" /></c:set>
	<a href="<wp:action path="${openPrevMessagePath}" />" title="<s:property value="iconMsgGoPreviousText" />"><img alt="<s:property value="%{#iconMsgGoPreviousText}" />" src="<s:property value="%{#iconMsgGoPrevious}" />" /></a>	
</s:if>
<span class="noscreen">| </span>
<s:if test="%{messageIndex < (currentFolderMessages.size()-1)}">
	<c:set var="openNextMessagePath">/ExtStr2/do/jpwebmail/Portal/WebMail/openMessage.action&amp;messageIndex=<s:property value="messageIndex+1" />&amp;currentFolderName=<s:property value="currentFolderName" /></c:set>
	<a href="<wp:action path="${openNextMessagePath}" />" title="<s:property value="iconMsgGoNextText" />"><img alt="<s:property value="%{#iconMsgGoNextText}" />" src="<s:property value="%{#iconMsgGoNext}" />" /></a>
</s:if>
</p>

<!-- FINE BLOCCO OPZIONI  -->

<h4 class="accordion_toggler"><wp:i18n key="jpwebmail_MSG_DETAILS" /></h4>
<div class="accordion_element">
<dl>
	<dt><wp:i18n key="jpwebmail_MSG_SUBJECT" />:</dt>
		<dd><s:property value="#currentMessage.subject" /></dd>
	<dt><wp:i18n key="jpwebmail_MSG_FROM" />:</dt>
		<dd><s:property value="#currentMessage.from" /></dd>
	<dt><wp:i18n key="jpwebmail_MSG_TO" />:</dt>
		<dd><s:property value="%{getTo(#currentMessage)}" /></dd>
	<dt><wp:i18n key="jpwebmail_MSG_CC" />:</dt>
		<dd><s:property value="%{getCc(#currentMessage)}" /></dd>
	<s:if test="%{getBcc(#currentMessage)!=''}">
	<dt><wp:i18n key="jpwebmail_MSG_BCC" />:</dt>
		<dd><s:property value="%{getBcc(#currentMessage)}" /></dd>
	</s:if>
	<dt><wp:i18n key="jpwebmail_MSG_SEND_DATE" />:</dt>
		<dd><s:date name="#currentMessage.sentDate" format="dd MMMM yyyy HH:mm" /></dd>
	<dt><wp:i18n key="jpwebmail_MSG_RECEIVE_DATE" />:</dt>
		<dd><s:date name="#currentMessage.receivedDate" format="dd MMMM yyyy HH:mm" /></dd>
</dl>
</div>

<h4><wp:i18n key="jpwebmail_MSG_BODY" /></h4>
<pre>
<s:property value="%{getContent(message)}"/>
</pre>

<s:set name="attachmentInfos" value="%{getAttachmentInfos(#currentMessage)}"></s:set>
<s:if test="#attachmentInfos.size() > 0">
	<h3><wp:i18n key="jpwebmail_MSG_ATTACHMENTS" /></h3>
	<ol>
	<s:iterator value="#attachmentInfos" id="attachment">
		<c:set var="openAttachmentMessagePath"><wp:info key="systemParam" paramName="applicationBaseURL" />do/jpwebmail/Portal/WebMail/openAttachment.action?messageIndex=<s:property value="messageIndex" />&currentFolderName=<s:property value="currentFolderName" />&attachmentNumber=<s:property value="#attachment.number" />&subPartAttachmentNumber=<s:property value="#attachment.subPartNumber" /></c:set>	
		<li><a href="<c:out value="${openAttachmentMessagePath}" />" ><s:property value="#attachment.fileName" /> (<s:property value="#attachment.size" /> bytes)</a></li>
	</s:iterator>
	</ol>
</s:if>

</div>
</form>
</div>