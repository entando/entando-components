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

import com.agiletec.plugins.jpwebmail.apsadmin.webmail.IWebMailBaseAction;

/**
 * Basic interface class for Action for the management of operations on messages in a mailbox.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IInboxMessagesAction extends IWebMailBaseAction {
	
	/**
	 * Go through the request of shifting the selected messages.
	 * @return The code of the result of.
	 */
	public String moveMessages();

	/**
	 * Go through the request of cancellation the selected messages.
	 * If the current folder is the folder "Trash", the operation is the actual cancellation of the messages from the mailbox; 
	 * Otherwise, the operation is moving in the "Trash".
	 * @return The code of the result of.
	 */
	public String deleteMessages();
	
	/**
	 * Go through the request of selection all messages in the user interface.
	 * @return The code of the result of.
	 */
	public String selectAllMessages();
	
	/**
	 * Go through the request of deselection all selected messages in the user interface.
	 * @return The code of the result of.
	 */
	public String deselectAllMessages();
	
}
