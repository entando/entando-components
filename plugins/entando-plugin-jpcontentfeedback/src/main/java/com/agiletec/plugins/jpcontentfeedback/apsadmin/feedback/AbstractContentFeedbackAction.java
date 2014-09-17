/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
