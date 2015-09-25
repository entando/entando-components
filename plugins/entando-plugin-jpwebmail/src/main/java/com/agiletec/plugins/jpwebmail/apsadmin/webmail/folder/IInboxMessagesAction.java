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
