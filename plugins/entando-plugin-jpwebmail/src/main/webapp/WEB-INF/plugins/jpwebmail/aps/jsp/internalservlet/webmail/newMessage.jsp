<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpwebmail/static/css/webmail.css" /> 
<div class="webmail">
<form action="<wp:action path="/ExtStr2/do/jpwebmail/Portal/WebMail/send.action" />" method="post" enctype="multipart/form-data">
<p class="noscreen">
	<wpsf:hidden name="messageIndex" />
	<wpsf:hidden name="currentFolderName" />
</p>

<div class="colonna1">
	<s:include value="inc/inc_folderInfo.jsp" />
</div>

<div class="colonna2">
<h3><wp:i18n key="jpwebmail_NEW_MSG" /></h3>
<s:if test="hasFieldErrors()">
<div class="message message_error">
<h3><wp:i18n key="ERROR" /></h3>
<ul>
<s:iterator value="fieldErrors">
	<s:iterator value="value">
	<li><s:property escape="false"/></li>
	</s:iterator>
</s:iterator>
</ul>
</div>
</s:if>

<dl>
	<dt><wp:i18n key="jpwebmail_MSG_FROM" />:</dt>
		<dd><s:property value="message.from" /></dd>
	<dt><wp:i18n key="jpwebmail_NOTE" /></dt>
		<dd><wp:i18n key="jpwebmail_CONTACTS_NOTE" /></dd>
	<dt><label for="recipientsTo"><wp:i18n key="jpwebmail_MSG_TO" />:</label></dt>
		<dd><wpsf:textarea useTabindexAutoIncrement="true" name="recipientsTo" id="recipientsTo" value="%{getTo(message)}" cols="50"/>
		<s:if test="%{existAddressBook()}">
			<wpsa:actionParam action="addressBook" var="enterAddressBook" >
				<wpsa:actionSubParam name="actualRecipient" value="1" />
			</wpsa:actionParam>
			<br />
			<s:set name="iconTo"><wp:resourceURL />plugins/jpwebmail/static/img/contact-new.png</s:set>
			<s:set name="iconToText"><wp:i18n key="jpwebmail_MSG_TO_LABEL" /></s:set>	
			<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconTo}" action="%{#enterAddressBook}" value="%{#iconToText}" title="%{#iconToText}" />
		</s:if>
		</dd>
	<dt><label for="recipientsCc"><wp:i18n key="jpwebmail_MSG_CC" />:</label></dt>
		<dd><wpsf:textarea useTabindexAutoIncrement="true" name="recipientsCc" id="recipientsCc" value="%{getCc(message)}" cols="50"/>
		<s:if test="%{existAddressBook()}">
			<wpsa:actionParam action="addressBook" var="enterAddressBook" >
				<wpsa:actionSubParam name="actualRecipient" value="2" />
			</wpsa:actionParam>
			<br />
			<s:set name="iconTo"><wp:resourceURL />plugins/jpwebmail/static/img/contact-new.png</s:set>
			<s:set name="iconToText"><wp:i18n key="jpwebmail_MSG_CC_LABEL" /></s:set>	
			<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconTo}" action="%{#enterAddressBook}" value="%{#iconToText}" title="%{#iconToText}" />
		</s:if>
		</dd>
	<dt><label for="recipientsBcc"><wp:i18n key="jpwebmail_MSG_BCC" />:</label></dt>		
		<dd><wpsf:textarea useTabindexAutoIncrement="true" name="recipientsBcc" id="recipientsBcc" value="%{getBcc(message)}" cols="50"/>
		<s:if test="%{existAddressBook()}">
			<wpsa:actionParam action="addressBook" var="enterAddressBook" >
				<wpsa:actionSubParam name="actualRecipient" value="3" />
			</wpsa:actionParam>
			<br />
			<s:set name="iconTo"><wp:resourceURL />plugins/jpwebmail/static/img/contact-new.png</s:set>
			<s:set name="iconToText"><wp:i18n key="jpwebmail_MSG_BCC_LABEL" /></s:set>	
			<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconTo}" action="%{#enterAddressBook}" value="%{#iconToText}" title="%{#iconToText}" />
		</s:if>
		</dd>
	<dt><label for="subject"><wp:i18n key="jpwebmail_MSG_SUBJECT" />:</label></dt>
		<dd><wpsf:textfield useTabindexAutoIncrement="true" name="subject" id="subject" value="%{message.subject}" cssClass="LongText" /></dd>
	<dt><label for="content"><wp:i18n key="jpwebmail_MSG_BODY" />:</label></dt>
		<dd><wpsf:textarea useTabindexAutoIncrement="true" name="content" id="content" value="%{getContent(message)}" rows="20" cols="50"></wpsf:textarea></dd>
</dl>

<s:set name="attachmentInfos" value="%{getAttachmentInfos(message)}"></s:set>
<s:if test="#attachmentInfos.size() > 0">
	<h3><wp:i18n key="jpwebmail_MSG_ATTACHMENTS" /></h3>
	<ol class="alignInput">
	<s:iterator value="#attachmentInfos" id="attachment">
		<wpsa:actionParam action="removeAttachment" var="removeAttachmentActionName" >
			<wpsa:actionSubParam name="attachmentNumber" value="%{#attachment.number}" />
		</wpsa:actionParam>
		<li>
		<s:property value="#attachment.fileName" /> -	
		<s:set name="iconAttachRemove"><wp:resourceURL />plugins/jpwebmail/static/img/edit-delete.png</s:set>
		<s:set name="iconAttachRemoveText"><wp:i18n key="jpwebmail_ATTACH_REMOVE" /></s:set>	
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#removeAttachmentActionName}" type="image" src="%{#iconAttachRemove}" value="%{#iconAttachRemoveText}" title="%{#iconAttachRemoveText}: %{#attachment.fileName}" />
		</li>
	</s:iterator>
	</ol>
</s:if>

<p class="actions">
	<label for="upload"><wp:i18n key="jpwebmail_MSG_ATTACH_NEW" />:</label><br />
	<input type="file" name="upload" value="" id="upload" tabindex="<wpsa:counter />" />
	<s:set name="iconMsgNewAttach"><wp:resourceURL />plugins/jpwebmail/static/img/mail-attachment.png</s:set>
	<s:set name="iconMsgNewAttachText"><wp:i18n key="jpwebmail_MSG_ATTACH_THIS" /></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconMsgNewAttach}" value="%{#iconMsgNewAttachText}" title="%{#iconMsgNewAttachText}" action="addAttachment" />
</p>

<p class="centerText">
	<s:set name="iconMsgSend"><wp:resourceURL />plugins/jpwebmail/static/img/mail-send-receive.png</s:set>
	<s:set name="iconMsgSendText"><wp:i18n key="jpwebmail_MSG_SEND" /></s:set>	
	<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconMsgSend}" action="send" value="%{#iconMsgSendText}" title="%{#iconMsgSendText}" />
</p>

</div>
</form>
</div>