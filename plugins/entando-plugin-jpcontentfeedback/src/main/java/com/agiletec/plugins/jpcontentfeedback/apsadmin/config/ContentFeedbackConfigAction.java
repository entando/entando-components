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
