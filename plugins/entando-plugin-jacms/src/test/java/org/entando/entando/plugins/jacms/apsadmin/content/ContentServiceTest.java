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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import java.util.Arrays;
import java.util.List;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
import org.entando.entando.plugins.jacms.web.content.validator.RestContentListRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    @InjectMocks
    private ContentService contentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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

    public void getContents() throws Exception {
        RestContentListRequest requestList = new RestContentListRequest();

    }

}
