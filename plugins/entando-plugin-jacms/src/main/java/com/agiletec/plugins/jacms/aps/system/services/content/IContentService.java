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
package com.agiletec.plugins.jacms.aps.system.services.content;

import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public interface IContentService {

    public ContentDto getContent(String code);

    public ContentDto addContent(ContentDto request, BindingResult bindingResult);

    public ContentDto updateContent(ContentDto request, BindingResult bindingResult);

    public void deleteContent(String code);

    public ContentDto updateContentStatus(String code, String status);

    public PagedMetadata<?> getContentReferences(String code, String manager, RestListRequest requestList);

}
