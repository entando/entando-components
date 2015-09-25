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
package org.entando.entando.plugins.jpfrontshortcut.aps.internalservlet.portal;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.portal.PageConfigAction;

/**
 * @author E.Santoboni
 */
public class FrontPageConfigAction extends PageConfigAction {

	/**
	 * @deprecated Use {@link #joinWidget()} instead
	 */
	@Override
	public String joinShowlet() {
		return joinWidget();
	}

	@Override
	public String joinWidget() {
		String result = super.joinWidget();
		try {
            this.waitNotifyingThread();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "joinShowlet");
            return FAILURE;
        }
		return result;
	}

	/**
	 * @deprecated Use {@link #trashWidget()} instead
	 */
	@Override
	public String trashShowlet() {
		return trashWidget();
	}

	@Override
	public String trashWidget() {
		try {
			String result = this.checkBaseParams();
			if (null != result) {
				return result;
			}
			IPage page = this.getPage(this.getPageCode());
			this.setShowlet(page.getWidgets()[this.getFrame()]);
		} catch (Exception e) {
			ApsSystemUtils.logThrowable(e, this, "trashShowlet");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * @deprecated Use {@link #deleteWidget()} instead
	 */
	@Override
	public String deleteShowlet() {
		return deleteWidget();
	}

	@Override
	public String deleteWidget() {
		String result = super.deleteWidget();
		try {
            this.waitNotifyingThread();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "deleteWidget");
            return FAILURE;
        }
		return result;
	}

    protected void waitNotifyingThread() throws InterruptedException {
            Thread[] threads = new Thread[20];
        Thread.enumerate(threads);
        for (int i=0; i<threads.length; i++) {
            Thread currentThread = threads[i];
            if (currentThread != null &&
                            currentThread.getName().startsWith(NotifyManager.NOTIFYING_THREAD_NAME)) {
                    currentThread.join();
            }
        }
    }

}