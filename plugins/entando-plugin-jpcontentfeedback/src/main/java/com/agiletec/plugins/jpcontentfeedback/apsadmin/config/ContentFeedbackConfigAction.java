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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.ContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;

public class ContentFeedbackConfigAction extends BaseAction {

    public String edit() {
        try {
            IContentFeedbackConfig config = this.getContentFeedbackManager().getConfig();
            if (null == config) {
                config = new ContentFeedbackConfig();
            }
            this.setConfig((ContentFeedbackConfig) config);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String update() {
        try {
			if (null == this.getConfig()) {
				this.setConfig(new ContentFeedbackConfig());
			}
            this.getContentFeedbackManager().updateConfig(this.getConfig());
            this.addActionMessage(this.getText("jpcontentfeedback.message.config.updated"));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "update");
            return FAILURE;
        }
        return SUCCESS;
    }

    protected IContentFeedbackManager getContentFeedbackManager() {
        return _contentFeedbackManager;
    }
    public void setContentFeedbackManager(IContentFeedbackManager contentFeedbackManager) {
        this._contentFeedbackManager = contentFeedbackManager;
    }

    public ContentFeedbackConfig getConfig() {
        return _config;
    }
    public void setConfig(ContentFeedbackConfig config) {
        this._config = config;
    }

    private ContentFeedbackConfig _config;
    private IContentFeedbackManager _contentFeedbackManager;

}
