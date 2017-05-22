/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model;

import java.io.Serializable;

public class ContentState implements Comparable<ContentState>, Serializable {

	private static final long serialVersionUID = -1124975021255290835L;

	private String _contentid;
	private String _type;
	private String _desciption;
	private String _action;
	private String _result;

	public String getAction() {
		return _action;
	}

	public void setAction(String action) {
		this._action = action;
	}

	public String getResult() {
		return _result;
	}

	public void setResult(String result) {
		this._result = result;
	}

	public String getContentid() {
		return _contentid;
	}

	public void setContentid(String contentid) {
		this._contentid = contentid;
	}

	public String getType() {
		return _type;
	}

	public void setTypeid(String type) {
		this._type = type;
	}

	public String getDesc() {
		return _desciption;
	}

	public void setDesc(String desciption) {
		this._desciption = desciption;
	}

	public ContentState(String contentid, String type, String desciption, String action, String result) {
		this._contentid = contentid;
		this._type = type;
		this._desciption = desciption;
		this._action = action;
		this._result = result;
	}

	@Override
	public int compareTo(ContentState o) {
		return this._contentid.compareTo(o._contentid);
	}

}
