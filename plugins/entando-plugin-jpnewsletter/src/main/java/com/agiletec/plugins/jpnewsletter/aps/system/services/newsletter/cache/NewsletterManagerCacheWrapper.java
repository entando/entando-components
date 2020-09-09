/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;

import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;

public class NewsletterManagerCacheWrapper extends AbstractCacheWrapper implements INewsletterManagerCacheWrapper {

    @Override
    public void release() {
        this.getCache().evict(NEWSLETTER_CONFIG_CACHE_NAME);
    }

    @Override
    protected String getCacheName() {
        return NEWSLETTER_MANAGER_CACHE_NAME;
    }

    @Override
    public NewsletterConfig getConfig() {
        return super.get(NEWSLETTER_CONFIG_CACHE_NAME, NewsletterConfig.class);
    }

    @Override
    public void updateConfig(NewsletterConfig config) {
        super.getCache().put(NEWSLETTER_CONFIG_CACHE_NAME, config);
    }

    @Override
    public Integer getStatus() {
        Integer status = super.get(NEWSLETTER_STATUS_CACHE_NAME, Integer.class);
        if (null == status) {
            return 0;
        }
        return status;
    }

    @Override
    public void updateStatus(Integer status) {
        super.getCache().put(NEWSLETTER_STATUS_CACHE_NAME, status);
    }
    
}
