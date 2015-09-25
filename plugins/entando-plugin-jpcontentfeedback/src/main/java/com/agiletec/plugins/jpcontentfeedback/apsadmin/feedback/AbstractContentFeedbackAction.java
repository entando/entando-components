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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback;

import java.util.HashMap;
import java.util.Map;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;

/**
 * @author D.Cherchi
 */
public abstract class AbstractContentFeedbackAction extends BaseAction {

	public Map<Integer, String>  getAllStatus(){
		Map<Integer, String> allStatus = new HashMap<Integer, String>();
		allStatus.put(Comment.STATUS_NOT_APPROVED , this.getText("Message.status.notApproved"));
		allStatus.put(Comment.STATUS_TO_APPROVE, this.getText("Message.status.toApprove"));
		allStatus.put(Comment.STATUS_APPROVED, this.getText("Message.status.approved"));
		return allStatus;
	}

	public String getNameStatus(int idStatus){
		return getAllStatus().get(idStatus);
	}

	public String getPagName() {
		return _pagName;
	}

	public void setPagName(String pagName) {
		this._pagName = pagName;
	}

	public String getPagValue() {
		return _pagValue;
	}

	public void setPagValue(String pagValue) {
		this._pagValue = pagValue;
	}

	private String _pagName;
	private String _pagValue;
}
