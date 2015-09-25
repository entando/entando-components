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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.model;

import java.util.Date;

import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import java.util.List;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.parse.FormDOM;

/**
 * @author E.Santoboni
 */
public class Message extends ApsEntity {
	
	@Override
	public IApsEntity getEntityPrototype() {
		Message prototype = (Message) super.getEntityPrototype();
		prototype.setVersionType(this.getVersionType());
		prototype.setRepeatable(this.isRepeatable());
		prototype.setIgnoreVersionEdit(this.isIgnoreVersionEdit());
		prototype.setIgnoreVersionDisplay(this.isIgnoreVersionDisplay());
		return prototype;
	}
	
	public void toggleAttribute(String currentStep) {
		List<AttributeInterface> attributes = this.getAttributeList();
		for (int i = 0; i < attributes.size(); i++) {
			AttributeInterface attribute = attributes.get(i);
			attribute.disable(JpwebformSystemConstants.DISABLING_CODE_ONSTEP_PREFIX + currentStep);
		}
	}
	
	@Override
	protected IApsEntityDOM getBuildJDOM() {
		FormDOM dom = (FormDOM) super.getBuildJDOM();
		dom.setLastCompletedStep(this.getLastCompletedStep());
		dom.setVersionType(this.getVersionType());
		return dom;
	}
	
	public Integer getVersionType() {
		if (null == this._versionType || _versionType.intValue() < 0) {
			return 0;
		}
		return _versionType;
	}
	public void setVersionType(Integer versionType) {
		this._versionType = versionType;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public Date getCreationDate() {
		return _creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	public String getLastCompletedStep() {
		return _lastCompletedStep;
	}
	public void setLastCompletedStep(String lastCompletedStep) {
		this._lastCompletedStep = lastCompletedStep;
	}
	
	public boolean isCompleted() {
		return _completed;
	}
	public void setCompleted(boolean completed) {
		this._completed = completed;
	}

	public Date getSendDate() {
		return _sendDate;
	}

	public void setSendDate(Date sendDate) {
		this._sendDate = sendDate;
	}

	public boolean isRepeatable() {
		return _repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this._repeatable = repeatable;
	}
	
	public boolean isIgnoreVersionEdit() {
		return _ignoreVersionEdit;
	}
	
	public void setIgnoreVersionEdit(boolean ignoreVersionEdit) {
		this._ignoreVersionEdit = ignoreVersionEdit;
	}

	public boolean isIgnoreVersionDisplay() {
		return _ignoreVersionDisplay;
	}

	public void setIgnoreVersionDisplay(boolean ignoreVersionDisplay) {
		this._ignoreVersionDisplay = ignoreVersionDisplay;
	}

	private Integer _versionType;
	private String _username;
	private Date _creationDate;
	private String _langCode;
	private Date _sendDate;
	private String _lastCompletedStep;
	private boolean _completed;
	private boolean _repeatable;
	private boolean _ignoreVersionEdit;
	private boolean _ignoreVersionDisplay;
}