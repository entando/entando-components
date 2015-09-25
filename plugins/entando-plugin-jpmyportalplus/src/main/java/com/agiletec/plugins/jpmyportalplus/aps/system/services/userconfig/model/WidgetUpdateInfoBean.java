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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model;

import com.agiletec.aps.system.services.page.Widget;



/**
 * @author E.Santoboni
 */
public class WidgetUpdateInfoBean {

	public WidgetUpdateInfoBean(int framePos, Widget showlet, int status) {
		this.setFramePos(framePos);
		this.setShowlet(showlet);
		this.setStatus(status);
	}

	public int getFramePos() {
		return _framePos;
	}
	protected void setFramePos(int framePos) {
		this._framePos = framePos;
	}

	public Widget getShowlet() {
		return _showlet;
	}
	protected void setShowlet(Widget showlet) {
		this._showlet = showlet;
	}

	public int getStatus() {
		return _status;
	}
	protected void setStatus(int status) {
		this._status = status;
	}

	private int _framePos;
	private Widget _showlet;
	private int _status;

}