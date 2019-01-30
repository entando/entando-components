/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDto;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class DigitalExchangePageModelService extends PageModelService {

    @Autowired
    public DigitalExchangePageModelService(IPageModelManager pageModelManager, IDtoBuilder<PageModel, PageModelDto> dtoBuilder) {
        super(pageModelManager, dtoBuilder);
    }

    @Override
    public PagedMetadata<PageModelDto> getPageModels(RestListRequest restListReq, Map<String, String> requestParams) {

        PagedMetadata<PageModelDto> pageModels = super.getPageModels(restListReq, requestParams);

        List<PageModelDto> dePageModels = new ArrayList<>();

        for (PageModelDto pageModelDto : pageModels.getBody()) {
            DigitalExchangePageModelDto dePageModel = new DigitalExchangePageModelDto(pageModelDto);
            dePageModel.setDigitalExchange(null); // Local page model
            dePageModels.add(dePageModel);
        }
        // TODO: query the DE instances for retrieving installable pageModels

        pageModels.setBody(dePageModels);
        return pageModels;
    }
}
