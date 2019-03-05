/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentServiceUtilizer;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

/**
 * @author E.Santoboni
 */
public class ContentServiceTest {

    @Mock
    private ILangManager langManager;
    @Mock
    private ContentManager contentManager;
    @Mock
    private IContentModelManager contentModelManager;
    @Mock
    private IAuthorizationManager authorizationManager;
    @Mock
    private IContentAuthorizationHelper contentAuthorizationHelper;
    @Mock
    private IContentDispenser contentDispenser;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private ICmsSearchEngineManager searchEngineManager;
    @InjectMocks
    private ContentService contentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ContentServiceUtilizer serviceUtilizer = Mockito.mock(ContentServiceUtilizer.class);
        Map<String, ContentServiceUtilizer> map = new HashMap<>();
        map.put("service", serviceUtilizer);
        when(this.applicationContext.getBeansOfType(ContentServiceUtilizer.class)).thenReturn(map);
    }

    @Test
    public void getGroupUtilizer() throws Exception {
        List<String> contentsId = Arrays.asList("ART1", "ART2");
        when(((GroupUtilizer) this.contentManager).getGroupUtilizers(Mockito.anyString())).thenReturn(contentsId);
        when(this.contentManager.loadContent(Mockito.anyString(), Mockito.eq(true))).thenReturn(Mockito.any(Content.class));
        List<ContentDto> dtos = this.contentService.getGroupUtilizer("groupName");
        Assert.assertEquals(2, dtos.size());
        Mockito.verify(((GroupUtilizer<String>) this.contentManager), Mockito.times(1)).getGroupUtilizers(Mockito.anyString());
        Mockito.verify(this.contentManager, Mockito.times(2)).loadContent(Mockito.anyString(), Mockito.eq(true));
    }

    @Test(expected = RestServerError.class)
    public void getGroupUtilizerWithError() throws Exception {
        when(((GroupUtilizer) this.contentManager).getGroupUtilizers(Mockito.anyString())).thenThrow(ApsSystemException.class);
        try {
            List<ContentDto> dtos = this.contentService.getGroupUtilizer("groupName");
            Assert.fail();
        } finally {
            Mockito.verify(((GroupUtilizer) this.contentManager), Mockito.times(1)).getGroupUtilizers(Mockito.anyString());
            Mockito.verify(this.contentManager, Mockito.times(0)).loadContent(Mockito.anyString(), Mockito.eq(true));
        }
    }

    @Test
    public void getCategoryUtilizer() throws Exception {
        List<String> contentsId = Arrays.asList("ART11", "ART22", "ART33");
        when(((CategoryUtilizer) this.contentManager).getCategoryUtilizers(Mockito.anyString())).thenReturn(contentsId);
        when(this.contentManager.loadContent(Mockito.anyString(), Mockito.eq(true))).thenReturn(Mockito.any(Content.class));
        List<ContentDto> dtos = this.contentService.getCategoryUtilizer("categoryCode");
        Assert.assertEquals(3, dtos.size());
        Mockito.verify(((CategoryUtilizer) this.contentManager), Mockito.times(1)).getCategoryUtilizers(Mockito.anyString());
        Mockito.verify(this.contentManager, Mockito.times(3)).loadContent(Mockito.anyString(), Mockito.eq(true));
    }

    @Test(expected = RestServerError.class)
    public void getCategoryUtilizerWithError() throws Exception {
        when(((CategoryUtilizer) this.contentManager).getCategoryUtilizers(Mockito.anyString())).thenThrow(ApsSystemException.class);
        try {
            List<ContentDto> dtos = this.contentService.getCategoryUtilizer("categoryCode");
            Assert.fail();
        } finally {
            Mockito.verify(((CategoryUtilizer) this.contentManager), Mockito.times(1)).getCategoryUtilizers(Mockito.anyString());
            Mockito.verify(this.contentManager, Mockito.times(0)).loadContent(Mockito.anyString(), Mockito.eq(true));
        }
    }

    @Test
    public void getPageUtilizer() throws Exception {
        List<String> contentsId = Arrays.asList("ART1111", "ART2222", "ART333", "ART444", "ART5");
        when(((PageUtilizer) this.contentManager).getPageUtilizers(Mockito.anyString())).thenReturn(contentsId);
        when(this.contentManager.loadContent(Mockito.anyString(), Mockito.eq(true))).thenReturn(Mockito.any(Content.class));
        List<ContentDto> dtos = this.contentService.getPageUtilizer("pageCode");
        Assert.assertEquals(5, dtos.size());
        Mockito.verify(((PageUtilizer) this.contentManager), Mockito.times(1)).getPageUtilizers(Mockito.anyString());
        Mockito.verify(this.contentManager, Mockito.times(5)).loadContent(Mockito.anyString(), Mockito.eq(true));
    }

    @Test(expected = RestServerError.class)
    public void getPageUtilizerWithError() throws Exception {
        when(((PageUtilizer) this.contentManager).getPageUtilizers(Mockito.anyString())).thenThrow(ApsSystemException.class);
        try {
            List<ContentDto> dtos = this.contentService.getPageUtilizer("pageCode");
            Assert.fail();
        } finally {
            Mockito.verify(((PageUtilizer) this.contentManager), Mockito.times(1)).getPageUtilizers(Mockito.anyString());
            Mockito.verify(this.contentManager, Mockito.times(0)).loadContent(Mockito.anyString(), Mockito.eq(true));
        }
    }

    @Test
    public void getContentUtilizer() throws Exception {
        List<String> contentsId = Arrays.asList("ART1111", "ART2222", "ART333", "ART444", "ART5", "ART6");
        when(((ContentUtilizer) this.contentManager).getContentUtilizers(Mockito.anyString())).thenReturn(contentsId);
        when(this.contentManager.loadContent(Mockito.anyString(), Mockito.eq(true))).thenReturn(Mockito.any(Content.class));
        List<ContentDto> dtos = this.contentService.getContentUtilizer("NEW456");
        Assert.assertEquals(6, dtos.size());
        Mockito.verify(((ContentUtilizer) this.contentManager), Mockito.times(1)).getContentUtilizers(Mockito.anyString());
        Mockito.verify(this.contentManager, Mockito.times(6)).loadContent(Mockito.anyString(), Mockito.eq(true));
    }

    @Test(expected = RestServerError.class)
    public void getContentUtilizerWithError() throws Exception {
        when(((ContentUtilizer) this.contentManager).getContentUtilizers(Mockito.anyString())).thenThrow(ApsSystemException.class);
        try {
            List<ContentDto> dtos = this.contentService.getContentUtilizer("NEW456");
            Assert.fail();
        } finally {
            Mockito.verify(((ContentUtilizer) this.contentManager), Mockito.times(1)).getContentUtilizers(Mockito.anyString());
            Mockito.verify(this.contentManager, Mockito.times(0)).loadContent(Mockito.anyString(), Mockito.eq(true));
        }
    }

    @Test
    public void getContentsWithHtml() throws Exception {
        RestContentListRequest requestList = this.createContentsRequest();
        requestList.setStatus(IContentService.STATUS_ONLINE);
        requestList.setPageSize(2);
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.langManager.getDefaultLang()).thenReturn(Mockito.mock(Lang.class));
        when(this.authorizationManager.getUserGroups(user)).thenReturn(new ArrayList<>());
        List<String> contentsId = Arrays.asList("ART1", "ART2", "ART3", "ART4", "ART5", "ART6");
        when((this.contentManager).loadPublicContentsId(Mockito.nullable(String[].class), Mockito.anyBoolean(),
                Mockito.nullable(EntitySearchFilter[].class), Mockito.any(List.class))).thenReturn(contentsId);
        when((this.contentDispenser).getRenderizationInfo(Mockito.nullable(String.class), Mockito.anyLong(),
                Mockito.nullable(String.class), Mockito.nullable(RequestContext.class), Mockito.anyBoolean())).thenReturn(Mockito.mock(ContentRenderizationInfo.class));
        this.createMockContent("ART");
        this.createMockContentModel("ART");
        PagedMetadata<ContentDto> metadata = this.contentService.getContents(requestList, user);
        Assert.assertEquals(2, metadata.getBody().size());
        Mockito.verify(this.contentManager, Mockito.times(2)).loadContent(Mockito.anyString(), Mockito.eq(true));
        Mockito.verify(this.contentModelManager, Mockito.times(2)).getContentModel(10);
        Mockito.verify(this.contentDispenser, Mockito.times(2))
                .resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.nullable(RequestContext.class));
        Mockito.verifyZeroInteractions(this.searchEngineManager);
    }

    @Test
    public void getContentsWithoutHtml() throws Exception {
        RestContentListRequest requestList = this.createContentsRequest();
        requestList.setStatus(IContentService.STATUS_ONLINE);
        requestList.setModel(null);
        requestList.setPageSize(5);
        requestList.setText("text");
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.langManager.getDefaultLang()).thenReturn(Mockito.mock(Lang.class));
        when(this.authorizationManager.getUserGroups(user)).thenReturn(new ArrayList<>());
        List<String> contentsId = new ArrayList<>(Arrays.asList("ART1", "ART2", "ART3", "ART4", "ART5", "ART6"));
        when((this.contentManager).loadPublicContentsId(Mockito.nullable(String[].class), Mockito.anyBoolean(),
                Mockito.nullable(EntitySearchFilter[].class), Mockito.any(List.class))).thenReturn(contentsId);
        when(this.searchEngineManager.searchEntityId(Mockito.nullable(String.class),
                Mockito.eq("text"), Mockito.any())).thenReturn(Arrays.asList("ART7", "ART6", "ART8", "ART12", "ART2", "ART5"));
        this.createMockContent("ART");
        PagedMetadata<ContentDto> metadata = this.contentService.getContents(requestList, user);
        Assert.assertEquals(3, metadata.getBody().size());
        Mockito.verify(this.contentManager, Mockito.times(3)).loadContent(Mockito.anyString(), Mockito.eq(true));
        Mockito.verifyZeroInteractions(this.contentDispenser);
        Mockito.verifyZeroInteractions(this.contentModelManager);
    }

    @Test(expected = ValidationGenericException.class)
    public void getContentsWithModelError_1() throws Exception {
        RestContentListRequest requestList = this.createContentsRequest();
        requestList.setStatus(IContentService.STATUS_ONLINE);
        requestList.setModel("34");
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.langManager.getDefaultLang()).thenReturn(Mockito.mock(Lang.class));
        when(this.authorizationManager.getUserGroups(user)).thenReturn(new ArrayList<>());
        List<String> contentsId = Arrays.asList("ART1", "ART2", "ART3", "ART4", "ART5", "ART6");
        when((this.contentManager).loadPublicContentsId(Mockito.nullable(String[].class), Mockito.anyBoolean(),
                Mockito.nullable(EntitySearchFilter[].class), Mockito.any(List.class))).thenReturn(contentsId);
        this.createMockContent("ART");
        this.createMockContentModel("ART");
        when(this.contentModelManager.getContentModel(34)).thenReturn(null);
        try {
            PagedMetadata<ContentDto> metadata = this.contentService.getContents(requestList, user);
            Assert.fail();
        } finally {
            Mockito.verify(this.contentManager, Mockito.times(1)).loadContent(Mockito.anyString(), Mockito.eq(true));
            Mockito.verify(this.contentModelManager, Mockito.times(1)).getContentModel(34);
            Mockito.verifyZeroInteractions(this.searchEngineManager);
            Mockito.verifyZeroInteractions(this.contentDispenser);
        }
    }

    @Test(expected = ValidationGenericException.class)
    public void getContentsWithModelError_2() throws Exception {
        RestContentListRequest requestList = this.createContentsRequest();
        requestList.setStatus(IContentService.STATUS_ONLINE);
        requestList.setModel("list");
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.langManager.getDefaultLang()).thenReturn(Mockito.mock(Lang.class));
        when(this.authorizationManager.getUserGroups(user)).thenReturn(new ArrayList<>());
        List<String> contentsId = Arrays.asList("ART1", "ART2", "ART3", "ART4", "ART5", "ART6");
        when((this.contentManager).loadPublicContentsId(Mockito.nullable(String[].class), Mockito.anyBoolean(),
                Mockito.nullable(EntitySearchFilter[].class), Mockito.any(List.class))).thenReturn(contentsId);
        this.createMockContent("ART");
        this.createMockContentModel("NEW");
        try {
            PagedMetadata<ContentDto> metadata = this.contentService.getContents(requestList, user);
            Assert.fail();
        } finally {
            Mockito.verify(this.contentManager, Mockito.times(1)).loadContent(Mockito.anyString(), Mockito.eq(true));
            Mockito.verify(this.contentModelManager, Mockito.times(1)).getContentModel(10);
            Mockito.verifyZeroInteractions(this.searchEngineManager);
            Mockito.verifyZeroInteractions(this.contentDispenser);
        }
    }

    protected RestContentListRequest createContentsRequest() {
        RestContentListRequest requestList = new RestContentListRequest();
        Filter[] filters = new Filter[]{new Filter("attribute", "test", "eq")};
        requestList.setFilters(filters);
        requestList.setModel("list");
        requestList.setResolveLink(true);
        requestList.setStatus(IContentService.STATUS_DRAFT);
        return requestList;
    }

    protected void createMockContent(String typeCode) throws Exception {
        Content mockContent = Mockito.mock(Content.class);
        when(mockContent.getListModel()).thenReturn("10");
        when(mockContent.getDefaultModel()).thenReturn("20");
        when(mockContent.getTypeCode()).thenReturn(typeCode);
        when(this.contentManager.loadContent(Mockito.anyString(), Mockito.eq(true))).thenReturn(mockContent);
    }

    protected void createMockContentModel(String typeCode) throws Exception {
        ContentModel mockContentModel = Mockito.mock(ContentModel.class);
        when(mockContentModel.getContentType()).thenReturn(typeCode);
        when(mockContentModel.getContentShape()).thenReturn("Content model");
        when(this.contentModelManager.getContentModel(Mockito.anyLong())).thenReturn(mockContentModel);
    }

    @Test
    public void getContent() throws Exception {
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.langManager.getDefaultLang()).thenReturn(Mockito.mock(Lang.class));
        when(this.authorizationManager.getUserGroups(user)).thenReturn(new ArrayList<>());
        PublicContentAuthorizationInfo pcai = Mockito.mock(PublicContentAuthorizationInfo.class);
        when(pcai.isUserAllowed(ArgumentMatchers.<String>anyList())).thenReturn(true);
        when(this.contentAuthorizationHelper.getAuthorizationInfo(Mockito.anyString())).thenReturn(pcai);
        when((this.contentDispenser).getRenderizationInfo(Mockito.nullable(String.class), Mockito.anyLong(),
                Mockito.nullable(String.class), Mockito.nullable(RequestContext.class), Mockito.anyBoolean())).thenReturn(Mockito.mock(ContentRenderizationInfo.class));
        this.createMockContent("ART");
        this.createMockContentModel("ART");
        ContentDto dto = this.contentService.getContent("ART11", "list", IContentService.STATUS_ONLINE, null, false, user);
        Assert.assertNotNull(dto);
        Mockito.verify(this.contentManager, Mockito.times(1)).loadContent(Mockito.anyString(), Mockito.eq(true));
        Mockito.verify(this.contentModelManager, Mockito.times(1)).getContentModel(10);
        Mockito.verify(this.contentDispenser, Mockito.times(0))
                .resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.nullable(RequestContext.class));
        Mockito.verifyZeroInteractions(this.searchEngineManager);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getContentWithError_1() throws Exception {
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.langManager.getDefaultLang()).thenReturn(Mockito.mock(Lang.class));
        when(this.authorizationManager.getUserGroups(user)).thenReturn(new ArrayList<>());
        PublicContentAuthorizationInfo pcai = Mockito.mock(PublicContentAuthorizationInfo.class);
        when(pcai.isUserAllowed(ArgumentMatchers.<String>anyList())).thenReturn(true);
        when(this.contentAuthorizationHelper.getAuthorizationInfo(Mockito.anyString())).thenReturn(pcai);
        when(this.contentManager.loadContent(Mockito.anyString(), Mockito.eq(true))).thenReturn(null);
        try {
            ContentDto dto = this.contentService.getContent("ART11", "list", IContentService.STATUS_ONLINE, null, false, user);
            Assert.fail();
        } finally {
            Mockito.verify(this.contentManager, Mockito.times(1)).loadContent(Mockito.anyString(), Mockito.eq(true));
            Mockito.verifyZeroInteractions(this.contentModelManager);
            Mockito.verifyZeroInteractions(this.contentDispenser);
            Mockito.verifyZeroInteractions(this.searchEngineManager);
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getContentWithError_2() throws Exception {
        UserDetails user = Mockito.mock(UserDetails.class);
        when(this.contentAuthorizationHelper.getAuthorizationInfo(Mockito.anyString())).thenReturn(null);
        try {
            ContentDto dto = this.contentService.getContent("ART11", "list", IContentService.STATUS_ONLINE, null, false, user);
            Assert.fail();
        } finally {
            Mockito.verifyZeroInteractions(this.contentModelManager);
            Mockito.verifyZeroInteractions(this.contentDispenser);
            Mockito.verifyZeroInteractions(this.contentManager);
            Mockito.verifyZeroInteractions(this.searchEngineManager);
            Mockito.verifyZeroInteractions(this.langManager);
        }
    }

}
