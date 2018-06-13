/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;


public class SeoChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((SeoChangedObserver) srv).updateFromSeoChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return SeoChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}

	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	private int _operationCode;
	
	public final static int PAGE_CHANGED_EVENT = 1;
	public final static int CONTENT_CHANGED_EVENT = 2;
}
