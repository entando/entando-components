/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.folder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Flags.Flag;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.apsadmin.util.CurrentFolderMessagesInfo;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.AbstractWebmailBaseAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe per le operazioni di visualizzazione delle cartelle.
 * @author E.Santoboni
 */
public class WebmailAction extends AbstractWebmailBaseAction implements IWebMailAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(WebmailAction.class);
	
	@Override
	public String execute() throws Exception {
		this.getRequest().getSession().removeAttribute(CurrentFolderMessagesInfo.CURRENT_FOLDER_MESSAGES);
		return super.execute();
	}
	
	@Override
	public String moveIntoFolder() {
		return SUCCESS;
	}
	
	public List<Message> getMessages() throws Throwable {
		List<Message> messageList = null;
		try {
			Folder folder = this.getCurrentFolder();
			folder.open(Folder.READ_ONLY);
			CurrentFolderMessagesInfo folderInfos = (CurrentFolderMessagesInfo) this.getRequest().getSession().getAttribute(CurrentFolderMessagesInfo.CURRENT_FOLDER_MESSAGES);
			//if (this.hasToReloadMessages(folder, folderInfos)) {
			Message messages[] = folder.getMessages();
			FetchProfile profile = new FetchProfile();
			profile.add(FetchProfile.Item.ENVELOPE);
			folder.fetch(messages, profile);
			this.setOpenedFolder(folder);
			messageList = Arrays.asList(messages);
			this.orderMessages(messageList);
			folderInfos = new CurrentFolderMessagesInfo(this.getCurrentFolderName(), messageList);
			this.getRequest().getSession().setAttribute(CurrentFolderMessagesInfo.CURRENT_FOLDER_MESSAGES, folderInfos);
			//} else {
			//	messageList = folderInfos.getMessages();
			//}
		} catch (Throwable t) {
			_logger.error("Error extracting messages", t);
			throw t;
		}
		return messageList;
	}
	/*
	protected boolean hasToReloadMessages(Folder folder, CurrentFolderMessagesInfo folderInfos) throws Throwable {
		return (folderInfos == null //inizio navigazione
				|| folder.hasNewMessages() //vi sono nuovi messaggi nella cartella di destinazione
				|| !folderInfos.getFolderName().equals(this.getCurrentFolderName()) //cambio cartella
				|| folderInfos.getMessages() == null || folderInfos.getMessages().isEmpty() //non vi sono messaggi precedentemente
				|| folderInfos.getMessages().size() != folder.getMessageCount()); //cambiato size
	}
	*/
	protected void orderMessages(List<Message> messages) {
		//TODO PER ORA ORDINA PER DATA DISCENDENTE... POI STRUTTURARE OPZIONI
		BeanComparator comparator = new BeanComparator("receivedDate");
		Collections.sort(messages, comparator);
		Collections.reverse(messages);
	}
	
	@Override
	public String changeFolder() {
		return SUCCESS;
	}
	
	public boolean isDraft(Message message) {
		return this.isFlag(message, Flag.DRAFT);
	}
	public boolean isAnswered(Message message) {
		return this.isFlag(message, Flag.ANSWERED);
	}
	public boolean isDeleted(Message message) {
		return this.isFlag(message, Flag.DELETED);
	}
	public boolean isFlagged(Message message) {
		return this.isFlag(message, Flag.FLAGGED);
	}
	public boolean isRecent(Message message) {
		return this.isFlag(message, Flag.RECENT);
	}
	public boolean isSeen(Message message) {
		return this.isFlag(message, Flag.SEEN);
	}
	protected boolean isFlag(Message message, Flag flag) {
		try {
			Folder folder = message.getFolder();
			super.checkFolder(folder);
			return message.isSet(flag);
		} catch (Throwable t) {
			_logger.error("Error checking flag '" + flag + "' on message ", t);
		}
		return false;
	}
	
	public boolean isSentFolder() {
		String sentFolderName = this.getWebMailManager().getSentFolderName();
		return sentFolderName.equals(this.getCurrentFolderName());
	}
	
	public Address[] getTo(Message message) throws Throwable {
		return message.getRecipients(Message.RecipientType.TO);
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	private IWebMailManager _webMailManager;
	
}