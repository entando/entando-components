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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public abstract class AbstractWebmailBaseAction extends BaseAction implements IWebMailBaseAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(AbstractWebmailBaseAction.class);
	
	public Folder[] getMainFolders() throws Throwable {
		Folder folder = this.getStore().getDefaultFolder();
		Folder[] child = folder.list();
		return child;
	}
	
	public Folder[] getCurrentChildrenFolders() {
		try {
			Folder folder = this.getCurrentFolder();
			Folder[] child = folder.list();
			if (child == null || child.length==0) {
				child = folder.getParent().list();
			}
			return child;
		} catch (Throwable t) {
			_logger.error("Error extracting folder's children for " + this.getCurrentFolderName(), t);
			//ApsSystemUtils.logThrowable(t, this, "getCurrentChildrenFolders", 
			//		"Errore in estrazione figli di cartella " + this.getCurrentFolderName());
		}
		return new Folder[0];
	}
	
	public Folder getCurrentFolder() {
		String currentFolder = this.getCurrentFolderName();
		try {
			return this.getStore().getFolder(currentFolder);
		} catch (MessagingException e) {
			_logger.error("Error extracting folder " + currentFolder, e);
			throw new RuntimeException("Errore in estrazione cartella " + currentFolder, e);
		}
	}
	
	@Override
	public void closeFolders() {
		Folder folder = this.getOpenedFolder();
		if (null != folder && folder.isOpen()) {
			try {
				folder.close(false);
			} catch (MessagingException e) {
				_logger.error("Error closing folder " + folder.getName(), e);
				throw new RuntimeException("Errore in chiusura folder " + folder.getName(), e);
			}
		}
	}
	
	protected Store getStore() {
		this.checkStore();
		return this._store;
	}
	@Override
	public void setStore(Store store) {
		this._store = store;
	}
	
	protected void checkStore() {
		if (!this._store.isConnected()) {
			try {
				this._store.connect();
			} catch (Exception e) {
				_logger.error("Error checking store ", e);
			}
		}
	}
	
	protected void checkFolder(Folder folder) {
		if (null == folder) {
			return;
		}
		try {
			if (!folder.isOpen()) {
				this.checkStore();
				folder.open(Folder.READ_ONLY);
			}
		} catch (Exception t) {
			_logger.error("Error checking folder ", t);
		}
	}
	
	public String getCurrentFolderName() {
		if (null == this._currentFolderName) {
			this.setCurrentFolderName(JpwebmailSystemConstants.INBOX_FOLDER);
		}
		return _currentFolderName;
	}
	public void setCurrentFolderName(String currentFolderName) {
		this._currentFolderName = currentFolderName;
	}
	
	protected Folder getOpenedFolder() {
		return _openedFolder;
	}
	protected void setOpenedFolder(Folder openedFolder) {
		this._openedFolder = openedFolder;
	}
	
	private Store _store;
	private String _currentFolderName;
	private Folder _openedFolder;
	
}