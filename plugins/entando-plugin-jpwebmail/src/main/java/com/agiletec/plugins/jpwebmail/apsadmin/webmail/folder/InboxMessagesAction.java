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

import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.apsadmin.util.CurrentFolderMessagesInfo;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.AbstractWebmailBaseAction;

/**
 * Classe per le operazioni sui messaggi visualizzati in una singola cartella.
 * @version 1.0
 * @author E.Santoboni
 */
public class InboxMessagesAction extends AbstractWebmailBaseAction implements IInboxMessagesAction {
	
	@Override
	public String moveMessages() {
		Folder currentFolder = this.getCurrentFolder();
		try {
			currentFolder.open(Folder.READ_WRITE);
			Folder dfolder = this.getStore().getFolder(this.getFolderDest());
			if (null == dfolder || this.getCurrentFolderName().equals(this.getFolderDest())) {
				this.addActionError(this.getText("error.DestFolderNotSelected"));
				return SUCCESS;
			}
			Message[] msgs = this.getSelectedMessages();
			if (msgs.length != 0) {
				currentFolder.copyMessages(msgs, dfolder);
				currentFolder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
			} else {
				this.addActionError(this.getText("error.MsgToMoveNotSelected"));
			}
		} catch (FolderNotFoundException t) {
			ApsSystemUtils.logThrowable(t, this, "moveMessages", "Folder '" + this.getFolderDest() + "' not Found");
			if (this.getWebMailManager().getTrashFolderName().equals(this.getFolderDest())) {
				this.addActionError(this.getText("error.TrashFolderNotFound", new String[]{this.getFolderDest()}));
			} else {
				this.addActionError(this.getText("error.DestFolderNotFound", new String[]{this.getFolderDest()}));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "moveMessages");
			return FAILURE;
		} finally {
			this.closeFolder(currentFolder, true);
		}
		return SUCCESS;
	}
	
	@Override
	public String deleteMessages() {
		String trashFolderName = this.getWebMailManager().getTrashFolderName();
		if (null != trashFolderName) {
			boolean isTrashFolder = trashFolderName.equalsIgnoreCase(this.getCurrentFolderName());
			if (!isTrashFolder) {
				this.setFolderDest(trashFolderName);
				return this.moveMessages();
			}
		}
		Folder currentFolder = this.getCurrentFolder();
		try {
			currentFolder.open(Folder.READ_WRITE);
			Message[] msgs = this.getSelectedMessages();
			//System.out.println("Deleting " + msgs.length + " messages");
			if (msgs.length != 0) {
				currentFolder.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
			} else {
				this.addActionError(this.getText("error.MsgToTrashNotSelected"));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteMessages");
			return FAILURE;
		} finally {
			this.closeFolder(currentFolder, true);
		}
		return SUCCESS;
	}
	
	protected void closeFolder(Folder currentFolder, boolean expunge) {
		try {
			if (null != currentFolder) currentFolder.close(expunge);
		} catch (MessagingException e) {
			throw new RuntimeException("Errore in chiusura folder " + currentFolder.getName());
		}
	}
	
	protected Message[] getSelectedMessages() throws Throwable {
		CurrentFolderMessagesInfo folderInfos = this.getCurrentFolderMessagesInfo();
		List<Message> messages = folderInfos.getMessages();
		List<Integer> selectedIndexes = this.getMessageIndexes();
		if (null == selectedIndexes || selectedIndexes.isEmpty() 
				|| null == messages || messages.isEmpty()) {
			return new Message[0];
		}
		Message[] selected = new Message[0];
		for (int i=0; i<selectedIndexes.size(); i++) {
			int index = selectedIndexes.get(i);
			selected = this.addMessage(selected, messages.get(index));
		}
		return selected;
	}
	
	@Override
	public String selectAllMessages() {
		return SUCCESS;
	}
	
	@Override
	public String deselectAllMessages() {
		return SUCCESS;
	}
	
	protected CurrentFolderMessagesInfo getCurrentFolderMessagesInfo() throws Throwable {
		CurrentFolderMessagesInfo folderInfos = (CurrentFolderMessagesInfo) this.getRequest().getSession().getAttribute(CurrentFolderMessagesInfo.CURRENT_FOLDER_MESSAGES);
		if (folderInfos == null) {
			throw new RuntimeException("There is no information about the current folder " + this.getCurrentFolderName());
		}
		if (!folderInfos.getFolderName().equals(this.getCurrentFolderName())) {
			throw new RuntimeException("Informazioni correnti su cartella " + folderInfos.getFolderName() 
					+ " non compatibili con cartella corrente " + this.getCurrentFolderName());
		}
		return folderInfos;
	}
	
	private Message[] addMessage(Message[] messages, Message message){
		int len = messages.length;
		Message[] newMessages = new Message[len + 1];
		for (int i=0; i < len; i++){
			newMessages[i] = messages[i];
		}
		newMessages[len] = message;
		return newMessages;
	}
	
	protected List<Integer> getMessageIndexes() {
		return _messageIndexes;
	}
	public void setMessageIndexes(List<Integer> messageIndexes) {
		this._messageIndexes = messageIndexes;
	}
	
	public boolean isSelectedAllMessages() {
		return _selectedAllMessages;
	}
	public void setSelectedAllMessages(boolean selectedAllMessages) {
		this._selectedAllMessages = selectedAllMessages;
	}
	
	public String getFolderDest() {
		return _folderDest;
	}
	public void setFolderDest(String folderDest) {
		this._folderDest = folderDest;
	}
	
	public String getCurrentPagerItem() {
		return _currentPagerItem;
	}
	public void setCurrentPagerItem(String currentPagerItem) {
		this._currentPagerItem = currentPagerItem;
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	private List<Integer> _messageIndexes;
	private boolean _selectedAllMessages;
	
	private String _folderDest;
	
	private String _currentPagerItem;
	
	private IWebMailManager _webMailManager;
	
}