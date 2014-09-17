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
