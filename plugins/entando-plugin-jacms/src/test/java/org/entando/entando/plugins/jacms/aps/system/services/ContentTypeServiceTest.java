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
package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.assertionhelper.PageAssertionHelper;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.page.PageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.userprofile.MockUser;
import org.entando.entando.plugins.jacms.aps.system.services.assertionhelper.ContentTypeAssertionHelper;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.plugins.jacms.aps.system.services.mockhelper.ContentMockHelper;
import org.entando.entando.plugins.jacms.aps.system.services.mockhelper.ContentTypeMockHelper;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentTypeServiceTest {

    private RestListRequest restListRequest;

    @Mock
    private HttpSession httpSession;

    @Mock
    private ContentService contentService;

    @InjectMocks
    private ContentTypeService contentTypeService;

    @Before
    public void setUp() throws Exception {

        when(this.httpSession.getAttribute(anyString())).thenReturn(new MockUser());
        this.restListRequest = ContentTypeMockHelper.mockRestListRequest();

        Field f = this.contentTypeService.getClass().getDeclaredField("httpSession");
        f.setAccessible(true);
        f.set(this.contentTypeService, this.httpSession);
    }


    @Test
    public void getContentTypeUsageDetailsTest() {

        int relatedContents = 3;

        PagedMetadata<ContentDto> contentDtoPagedMetadata = ContentMockHelper.mockPagedContentDto(this.restListRequest, relatedContents);

        when(this.contentService.getContents(any(), any())).thenReturn(contentDtoPagedMetadata);

        PagedMetadata<ComponentUsageEntity> pageUsageDetails = this.contentTypeService.getComponentUsageDetails(ContentTypeMockHelper.CONTENT_TYPE_CODE,  this.restListRequest);

        ContentTypeAssertionHelper.assertUsageDetails(pageUsageDetails, relatedContents, relatedContents, this.restListRequest.getPage());
    }

    @Test
    public void getContentTypeUsageDetailsWithPagination() {

        this.restListRequest.setPageSize(2);

        // creates paged data
        List<Integer> pageList = Arrays.asList(1, 2);
        ContentDto[][] utilizers = {
                { ContentMockHelper.mockContentDto(0), ContentMockHelper.mockContentDto(1) },
                { ContentMockHelper.mockContentDto(2) }
        };

        int totalItems = utilizers[0].length + utilizers[1].length;

        // does assertions
        IntStream.range(0, pageList.size())
                .forEach(i -> {

                    restListRequest.setPage(pageList.get(i));

                    PagedMetadata<ContentDto> contentDtoPagedMetadata = ContentMockHelper.mockPagedContentDto(restListRequest, totalItems);
                    contentDtoPagedMetadata.setTotalItems(totalItems);
                    when(this.contentService.getContents(any(), any())).thenReturn(contentDtoPagedMetadata);

                    PagedMetadata<ComponentUsageEntity> usageDetails = contentTypeService.getComponentUsageDetails(restListRequest.getFilters()[0].getValue(), restListRequest);

                    ContentTypeAssertionHelper.assertUsageDetails(usageDetails,
                            utilizers[i],
                            totalItems,
                            pageList.get(i));
                });
    }

}
