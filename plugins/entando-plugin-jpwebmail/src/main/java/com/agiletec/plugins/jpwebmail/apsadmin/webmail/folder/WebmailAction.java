/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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