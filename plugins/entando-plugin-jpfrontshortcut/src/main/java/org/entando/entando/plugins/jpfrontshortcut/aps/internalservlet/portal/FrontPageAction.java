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
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.apsadmin.portal.PageAction;

/**
 * @author E.Santoboni
 */
public class FrontPageAction extends PageAction {

    @Override
    public String save() {
        String result = super.save();
        try {
            this.waitNotifyingThread();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "save");
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

	public String getRedirectUrl() {
		if (this.getStrutsAction() != ApsAdminSystemConstants.EDIT) {
			String langCode = this.getLangCode();
			Lang lang = (null != langCode) ? this.getLangManager().getLang(langCode) : this.getLangManager().getDefaultLang();
			if (null == lang) {
				lang = this.getLangManager().getDefaultLang();
			}
			String pageCode = this.getPageCode();
			IPage page = this.getPage(pageCode);
			return this.getUrlManager().createUrl(page, lang, null);
		}
		return "";
	}

	@Override
	public String delete() {
		String result = super.delete();
		try {
            this.waitNotifyingThread();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "delete");
            return FAILURE;
        }
		return result;
	}

    public String getLangCode() {
        return _langCode;
    }
    public void setLangCode(String langCode) {
        this._langCode = langCode;
    }

	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}

	private String _langCode;
	private IURLManager _urlManager;

}
