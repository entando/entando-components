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