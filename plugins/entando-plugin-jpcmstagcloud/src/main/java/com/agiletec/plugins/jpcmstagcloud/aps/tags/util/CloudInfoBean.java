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