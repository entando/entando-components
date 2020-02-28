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

import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;

public interface INewsletterManagerCacheWrapper {

    public static final String NEWSLETTER_MANAGER_CACHE_NAME = "Entando_INewsletterManager";

    public static final String NEWSLETTER_STATUS_CACHE_NAME = "INewsletterManager_status";

    public static final String NEWSLETTER_CONFIG_CACHE_NAME = "INewsletterManager_config";

    public NewsletterConfig getConfig();

    public void updateConfig(NewsletterConfig config);

    public Integer getStatus();

    public void updateStatus(Integer status);

}
