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
package org.entando.entando.plugins.jpwebform.aps.system.services.form.parse;

import com.agiletec.aps.system.common.entity.parse.ApsEntityDOM;
import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public class FormDOM extends ApsEntityDOM {
	
	public void setVersionType(Integer versionType) {
		this.setAttribute(TAG_VERSION_TYPE, String.valueOf(versionType));
	}
	public void setLastCompletedStep(String lastCompletedStep) {
		this.setAttribute(TAG_LAST_COMPLETED_STEP, lastCompletedStep);
	}
	
	private void setAttribute(String name, String value) {
		if (null == value) return;
		if (this._root.getChild(name) == null) {
			Element tag = new Element(name);
			this._root.addContent(tag);
		}
		this._root.getChild(name).setText(value);
	}
	
	private final static String TAG_VERSION_TYPE = "versionType";
	private final static String TAG_LAST_COMPLETED_STEP = "lastCompletedStep";
	
}