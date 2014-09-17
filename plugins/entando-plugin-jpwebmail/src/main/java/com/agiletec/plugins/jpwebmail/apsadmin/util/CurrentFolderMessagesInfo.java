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
package com.agiletec.plugins.jpwebmail.apsadmin.util;

import java.util.List;

import javax.mail.Message;

/**
 * @author E.Santoboni
 */
public class CurrentFolderMessagesInfo {
	
	public CurrentFolderMessagesInfo(String folderName, List<Message> messages) {
		this.setFolderName(folderName);
		this.setMessages(messages);
	}
	
	public String getFolderName() {
		return _folderName;
	}
	protected void setFolderName(String folderName) {
		this._folderName = folderName;
	}
	public List<Message> getMessages() {
		return _messages;
	}
	protected void setMessages(List<Message> messages) {
		this._messages = messages;
	}
	
	private String _folderName;
	private List<Message> _messages;
	
	/*
	 * Nome mediante il quale viene inserito in sessione l'oggetto
	 */
	public static final String CURRENT_FOLDER_MESSAGES = "webmailCurrentFolderMessagesInfo";
	
}
