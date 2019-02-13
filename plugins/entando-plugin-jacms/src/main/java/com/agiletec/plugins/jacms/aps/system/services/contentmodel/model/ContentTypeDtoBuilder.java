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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.aps.system.services.entity.model.AbstractEntityTypeDtoBuilder;

import java.util.List;

public class ContentTypeDtoBuilder extends AbstractEntityTypeDtoBuilder<Content, ContentTypeDto> {

    public ContentTypeDtoBuilder(List<AttributeRole> roles) {
        super(roles);
    }

    @Override
    protected ContentTypeDto toDto(Content src) {
        return new ContentTypeDto(src, this.getRoles());
    }

}
