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
package com.agiletec.plugins.jpblog.aps.system.services.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.cache.ICacheManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.event.ContentFeedbackChangedEvent;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.event.ContentFeedbackChangedObserver;

/**
 * @author E.Santoboni
 */
public class CacheReleaser extends AbstractService implements ContentFeedbackChangedObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(CacheReleaser.class);

    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }
    
    public void updateFromContentFeedbackChanged(ContentFeedbackChangedEvent event) {
        if (null != event.getContentId()) {
			this.getCacheManager().flushGroup(JacmsSystemConstants.CONTENT_CACHE_GROUP_PREFIX + event.getContentId());
        }
    }
    
    protected ICacheManager getCacheManager() {
        return _cacheManager;
    }
    public void setCacheManager(ICacheManager cacheManager) {
        this._cacheManager = cacheManager;
    }
    
    private ICacheManager _cacheManager;
    
}
