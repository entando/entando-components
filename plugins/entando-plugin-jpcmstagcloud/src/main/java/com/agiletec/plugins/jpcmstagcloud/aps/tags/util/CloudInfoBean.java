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
package com.agiletec.plugins.jpcmstagcloud.aps.tags.util;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * @author E.Santoboni
 */
public class CloudInfoBean {
	
	public CloudInfoBean(ITreeNode cloudNode, int occurrence, String classId, Lang lang) {
		this._cloudNode = cloudNode;
		this._occurrence = occurrence;
		this._classId = classId;
		this._lang = lang;
	}
	
	public ITreeNode getCloudNode() {
		return _cloudNode;
	}
	public void setCloudNode(ITreeNode cloudNode) {
		this._cloudNode = cloudNode;
	}
	
	public String getTitle() {
		String title = this.getCloudNode().getTitle(this.getLang().getCode());
		if (null == title || title.trim().length() == 0) {
			return this.getCloudNode().getCode();
		}
		return title;
	}
	
	public int getOccurrence() {
		return _occurrence;
	}
	public void setOccurrence(int occurrence) {
		this._occurrence = occurrence;
	}
	
	public String getClassId() {
		return _classId;
	}
	public void setClassId(String classId) {
		this._classId = classId;
	}
	
	public Lang getLang() {
		return _lang;
	}
	public void setLang(Lang lang) {
		this._lang = lang;
	}
	
	private ITreeNode _cloudNode;
	private int _occurrence;
	private String _classId;
	private Lang _lang;
	
}