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
package com.agiletec.plugins.jpwebmail.aps.tags.util;

public class WebMailIntroInfo {
	
	public int getMessageCount() {
		return _messageCount;
	}
	public void setMessageCount(int messageCount) {
		this._messageCount = messageCount;
	}
	
	public int getNewMessageCount() {
		return _newMessageCount;
	}
	public void setNewMessageCount(int newMessageCount) {
		this._newMessageCount = newMessageCount;
	}
	
	public int getUnreadMessageCount() {
		return _unreadMessageCount;
	}
	public void setUnreadMessageCount(int unreadMessageCount) {
		this._unreadMessageCount = unreadMessageCount;
	}
	
	public boolean isExistMailbox() {
		return _existMailbox;
	}
	public void setExistMailbox(boolean existMailbox) {
		this._existMailbox = existMailbox;
	}
	
	private int _messageCount;
	private int _newMessageCount;
	private int _unreadMessageCount;
	private boolean _existMailbox;
	
}