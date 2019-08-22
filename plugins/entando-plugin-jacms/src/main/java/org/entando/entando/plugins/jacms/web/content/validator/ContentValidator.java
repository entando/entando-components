/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.web.content.validator;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * @author E.Santoboni
 */
@Component
public class ContentValidator extends EntityValidator {

    @Autowired
    private IContentManager contentManager;

    public boolean existContent(String code, String status) {
        boolean online = (IContentService.STATUS_ONLINE.equalsIgnoreCase(status));
        try {
            return (null != this.contentManager.loadContent(code, online));
        } catch (Exception e) {
            logger.error("Error extracting content", e);
            throw new RestServerError("error extracting content", e);
        }
    }

    @Override
    protected IEntityManager getEntityManager() {
        return this.contentManager;
    }

}
