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

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.apache.commons.lang3.ArrayUtils;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.userprofile.MockUser;
import org.entando.entando.plugins.jacms.aps.system.services.assertionhelper.ContentTypeAssertionHelper;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import org.entando.entando.plugins.jacms.aps.system.services.mockhelper.ContentMockHelper;
import org.entando.entando.plugins.jacms.aps.system.services.mockhelper.ContentTypeMockHelper;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.entando.entando.web.common.assembler.PagedMetadataMapper;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentTypeServiceTest {

    private RestListRequest restListRequest;

    @Mock
    private HttpSession httpSession;
    @Mock
    private ContentService contentService;
    @Mock
    private PagedMetadataMapper pagedMetadataMapper;

    @InjectMocks
    private ContentTypeService contentTypeService;

    @Before
    public void setUp() throws Exception {

        when(this.httpSession.getAttribute(anyString())).thenReturn(new MockUser());
        this.restListRequest = ContentTypeMockHelper.mockRestListRequest();

        Field f = this.contentTypeService.getClass().getDeclaredField("httpSession");
        f.setAccessible(true);
        f.set(this.contentTypeService, this.httpSession);

        Field fp = this.contentTypeService.getClass().getDeclaredField("pagedMetadataMapper");
        fp.setAccessible(true);
        fp.set(this.contentTypeService, this.pagedMetadataMapper);

    }


    @Test
    public void getContentTypeUsageDetailsTest() {

        int relatedContents = 3;

        PagedMetadata<ContentDto> contentDtoPagedMetadata = ContentMockHelper.mockPagedContentDto(this.restListRequest, relatedContents);
        when(this.contentService.getContents(any(RestContentListRequest.class), any(UserDetails.class))).thenReturn(contentDtoPagedMetadata);
        when(this.pagedMetadataMapper.getPagedResult(any(RestListRequest.class), any(List.class), anyString(), anyInt()))
                .thenReturn(ContentMockHelper.mockPagedContentComponentUsageEntity(this.restListRequest, relatedContents));

        PagedMetadata<ComponentUsageEntity> pageUsageDetails = this.contentTypeService.getComponentUsageDetails(ContentTypeMockHelper.CONTENT_TYPE_CODE,  this.restListRequest);

        ContentTypeAssertionHelper.assertUsageDetails(pageUsageDetails, relatedContents, relatedContents, this.restListRequest.getPage());
    }


    @Test
    public void getContentTypeUsageDetailsWithPagination() {

        int pageSize = 2;
        this.restListRequest.setPageSize(pageSize);

        // creates paged data
        List<Integer> pageList = Arrays.asList(1, 2);
        ContentDto[][] utilizers = {
                { ContentMockHelper.mockContentDto(0), ContentMockHelper.mockContentDto(1) },
                { ContentMockHelper.mockContentDto(2) }
        };
        ContentDto[] allUtilizers = ArrayUtils.addAll(utilizers[0], utilizers[1]);

        int totalItems = utilizers[0].length + utilizers[1].length;

        // does assertions
        IntStream.range(0, pageList.size())
                .forEach(i -> {

                    restListRequest.setPage(pageList.get(i));

                    PagedMetadata<ContentDto> contentDtoPagedMetadata = ContentMockHelper.mockPagedContentDto(restListRequest, totalItems);
                    contentDtoPagedMetadata.setTotalItems(totalItems);
                    when(this.contentService.getContents(any(), any())).thenReturn(contentDtoPagedMetadata);
                    mockPagedMetadata(allUtilizers, pageList.get(i), 2, pageSize, totalItems);

                    PagedMetadata<ComponentUsageEntity> usageDetails = contentTypeService.getComponentUsageDetails(restListRequest.getFilters()[0].getValue(), restListRequest);

                    ContentTypeAssertionHelper.assertUsageDetails(usageDetails,
                            utilizers[i],
                            totalItems,
                            pageList.get(i));
                });
    }


    /**
     * init mock for a multipaged request
     */
    private void mockPagedMetadata(ContentDto[] utilizers, int currPage, int lastPage, int pageSize, int totalSize) {

        try {
            PageSearchRequest pageSearchRequest = new PageSearchRequest(PageMockHelper.PAGE_CODE);
            pageSearchRequest.setPageSize(pageSize);

            List<ComponentUsageEntity> componentUsageEntityList = Arrays.stream(utilizers)
                    .map(child -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_CONTENT, child.getId(), IContentService.STATUS_ONLINE))
                    .collect(Collectors.toList());

            PagedMetadata pagedMetadata = new PagedMetadata(pageSearchRequest, componentUsageEntityList, totalSize);
            pagedMetadata.setPageSize(pageSize);
            pagedMetadata.setPage(currPage);
            pagedMetadata.imposeLimits();
            when(pagedMetadataMapper.getPagedResult(any(), any(), anyString(), anyInt())).thenReturn(pagedMetadata);

        } catch (Exception e) {
            Assert.fail("Mock Exception");
        }
    }


    @Test
    public void getContentStatusWithContentStatusPublicWillReturnPublished() throws Exception {

        ContentDto contentDto = new ContentDto();
        contentDto.setStatus(Content.STATUS_PUBLIC);
        assertEquals("Published", getContentStatusMethod().invoke(contentTypeService, contentDto));
    }

    @Test
    public void getContentStatusWithContentStatusReadyAndOnlineWillReturnPublicNotEqualToReady() throws Exception {

        ContentDto contentDto = new ContentDto();
        contentDto.setStatus(Content.STATUS_READY);
        contentDto.setOnLine(true);
        assertEquals(Content.STATUS_PUBLIC + " not equals to " + Content.STATUS_READY, getContentStatusMethod().invoke(contentTypeService, contentDto));
    }

    @Test
    public void getContentStatusWithContentStatusReadyAndNOTOnlineWillReturnReady() throws Exception {

        ContentDto contentDto = new ContentDto();
        contentDto.setStatus(Content.STATUS_READY);
        assertEquals(Content.STATUS_READY, getContentStatusMethod().invoke(contentTypeService, contentDto));
    }

    @Test
    public void getContentStatusWithContentStatusDraftAndOnlineWillReturnPublicNotEqualToDraft() throws Exception {

        ContentDto contentDto = new ContentDto();
        contentDto.setStatus(Content.STATUS_DRAFT);
        contentDto.setOnLine(true);
        assertEquals(Content.STATUS_PUBLIC + " not equals to " + Content.STATUS_DRAFT, getContentStatusMethod().invoke(contentTypeService, contentDto));
    }

    @Test
    public void getContentStatusWithContentStatusDraftAndNOTOnlineWillReturnUnpublished() throws Exception {

        ContentDto contentDto = new ContentDto();
        contentDto.setStatus(Content.STATUS_DRAFT);
        assertEquals("Unpublished", getContentStatusMethod().invoke(contentTypeService, contentDto));
    }



    private Method getContentStatusMethod() {

        Method getContentStatusMethod = ReflectionUtils.findMethod(contentTypeService.getClass(), "getContentStatus", ContentDto.class);
        getContentStatusMethod.setAccessible(true);
        return getContentStatusMethod;
    }
}
