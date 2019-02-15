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
package org.entando.entando.plugins.jacms.aps.system.services.page;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import java.util.List;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentServiceUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author E.Santoboni
 */
public class CmsPageServiceWrapper implements ContentServiceUtilizer<PageDto> {

    private static final Logger logger = LoggerFactory.getLogger(CmsPageServiceWrapper.class);

    @Autowired
    private IDtoBuilder<IPage, PageDto> dtoBuilder;

    @Autowired
    @Qualifier(JacmsSystemConstants.PAGE_MANAGER_WRAPPER)
    private ContentUtilizer pageManagerWrapper;

    @Override
    public String getManagerName() {
        return this.getPageManagerWrapper().getName();
    }

    @Override
    public List<PageDto> getContentUtilizer(String contentId) {
        try {
            List<IPage> pages = this.getPageManagerWrapper().getContentUtilizers(contentId);
            return this.getDtoBuilder().convert(pages);
        } catch (ApsSystemException ex) {
            logger.error("Error loading page references for content {}", contentId, ex);
            throw new RestServerError("Error loading page references for content", ex);
        }
    }

    protected IDtoBuilder<IPage, PageDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IPage, PageDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected ContentUtilizer getPageManagerWrapper() {
        return pageManagerWrapper;
    }

    public void setPageManagerWrapper(ContentUtilizer pageManagerWrapper) {
        this.pageManagerWrapper = pageManagerWrapper;
    }

}
