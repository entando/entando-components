<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<s:set name="iconMsgNew"><wp:resourceURL />plugins/jpwebmail/static/img/mail-message-new.png</s:set>
<s:set name="iconMsgNewText">Nuovo</s:set>
<s:set name="iconMsgDelete"><wp:resourceURL />plugins/jpwebmail/static/img/mail-mark-junk.png</s:set>
<s:set name="iconMsgDeleteText">Cancella</s:set>
<s:set name="iconMsgMove"><wp:resourceURL />plugins/jpwebmail/static/img/mail-message-move.png</s:set>
<s:set name="iconMsgMoveText">Sposta</s:set>

<p class="actions">
	<wpsf:submit useTabindexAutoIncrement="true" action="newMessage" type="image" src="%{#iconMsgNew}" value="%{iconMsgNewText}" title="%{iconMsgNewText}" />
	<wpsf:submit useTabindexAutoIncrement="true" action="deleteMessages" type="image" src="%{#iconMsgDelete}" value="%{iconMsgDeleteText}" title="%{iconMsgDeleteText}" />
	<span class="noscreen"> | </span><label for="folderDest">Sposta: </label>
	<s:select list="mainFolders" name="folderDest" id="folderDest" listKey="fullName" listValue="name" />
	<wpsf:submit useTabindexAutoIncrement="true" action="moveMessages" type="image" src="%{#iconMsgMove}" value="%{iconMsgMoveText}" title="%{iconMsgMoveText}" />
</p>