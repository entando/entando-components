<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:form action="send" enctype="multipart/form-data">
<p class="noscreen">
	<wpsf:hidden name="messageIndex" />
	<wpsf:hidden name="currentFolderName" />
</p>

<h3><wp:i18n key="WEBMAIL_MSG_NEW" /></h3>
<s:if test="hasFieldErrors()">
<ul>
<s:iterator value="fieldErrors">
	<s:iterator value="value">
	<li><s:property escape="false"/></li>
	</s:iterator>
</s:iterator>
</ul>
</s:if>

<dl>
	<dt>DA:</dt>
		<dd><s:property value="message.from" /></dd>
	<dt>NOTA</dt>
		<dd>Puoi aggiungere gli indirizzi dei destinatari scrivendoli a mano separati da una virgola (es. mariorossi@japsportal.org, giuseppeverdi@japsportal.org)</dd>
	<dt><label for="recipientsTo">A:</label></dt>
		<dd><wpsf:textfield useTabindexAutoIncrement="true" name="recipientsTo" id="recipientsTo" value="%{getTo(message)}" cssClass="text" />
		<s:if test="%{existAddressBook()}">
			<wpsa:actionParam action="addressBook" var="enterAddressBook" >
				<wpsa:actionSubParam name="actualRecipient" value="1" />
			</wpsa:actionParam>
			<s:set name="iconTo"><wp:resourceURL />plugins/jpwebmail/static/img/contact-new.png</s:set>
			<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconTo}" action="%{#enterAddressBook}" value="A" title="A" />
		</s:if>
		</dd>
	<dt><label for="recipientsCc">CC:</label></dt>
		<dd><wpsf:textfield useTabindexAutoIncrement="true" name="recipientsCc" id="recipientsCc" value="%{getCc(message)}" cssClass="text" />
		<s:if test="%{existAddressBook()}">
			<wpsa:actionParam action="addressBook" var="enterAddressBook" >
				<wpsa:actionSubParam name="actualRecipient" value="2" />
			</wpsa:actionParam>
			<s:set name="iconTo"><wp:resourceURL />plugins/jpwebmail/static/img/contact-new.png</s:set>
			<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconTo}" action="%{#enterAddressBook}" value="CC" title="CC" />
		</s:if>
		</dd>
	<dt><label for="recipientsBcc">CCN:</label></dt>		
		<dd><wpsf:textfield useTabindexAutoIncrement="true" name="recipientsBcc" id="recipientsBcc" value="%{getBcc(message)}" cssClass="text" />
		<s:if test="%{existAddressBook()}">
			<wpsa:actionParam action="addressBook" var="enterAddressBook" >
				<wpsa:actionSubParam name="actualRecipient" value="3" />
			</wpsa:actionParam>
			<s:set name="iconTo"><wp:resourceURL />plugins/jpwebmail/static/img/contact-new.png</s:set>
			<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconTo}" action="%{#enterAddressBook}" value="CCN" title="CCN" />
		</s:if>
		</dd>
	<dt><label for="subject">OGGETTO:</label></dt>
		<dd><wpsf:textfield useTabindexAutoIncrement="true" name="subject" id="subject" value="%{message.subject}" cssClass="text" /></dd>
	<dt><label for="content">TESTO:</label></dt>
		<dd><s:textarea name="content" id="content" value="%{getContent(message)}" rows="20" cols="50"></s:textarea></dd>
</dl>

<s:set name="attachmentInfos" value="%{getAttachmentInfos(message)}"></s:set>
<s:if test="#attachmentInfos.size() > 0">
	<h3>ALLEGATI</h3>
	<ol>
	<s:iterator value="#attachmentInfos" id="attachment">
		<wpsa:actionParam action="removeAttachment" var="removeAttachmentActionName" >
			<wpsa:actionSubParam name="attachmentNumber" value="%{#attachment.number}" />
		</wpsa:actionParam>
		<li><s:property value="#attachment.fileName" /> - <wpsf:submit useTabindexAutoIncrement="true" action="%{#removeAttachmentActionName}" value="RIMUOVI" title="RIMUOVI" /></li>
	</s:iterator>
	</ol>
</s:if>

<p class="actions">
	<label for="upload"><wp:i18n key="WEBMAIL_MSG_ATTACH_NEW" />:</label><br />
	<input type="file" name="upload" value="" id="upload" tabindex="<wpsa:counter />" />
	<s:set name="iconMsgNewAttach"><wp:resourceURL />plugins/jpwebmail/static/img/mail-attachment.png</s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="addAttachment" type="image" src="%{#iconMsgNewAttach}" value="ALLEGA" title="ALLEGA" />
</p>

<p class="centerText">
	<s:set name="iconMsgSend"><wp:resourceURL />plugins/jpwebmail/static/img/mail-send-receive.png</s:set>
	<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconMsgSend}" action="send" value="INVIA" title="INVIA" />
</p>

</s:form>