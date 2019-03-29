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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.util.HeadInfoContainer;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class ContentViewerHelperTest {

    @Mock
    private IContentModelManager contentModelManager;

    @Mock
    private IContentManager contentManager;

    @Mock
    private IContentDispenser contentDispenser;

    @Mock
    private IContentAuthorizationHelper contentAuthorizationHelper;

    @Mock
    private RequestContext reqCtx;

    @InjectMocks
    private ContentViewerHelper contentViewerHelper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Lang currentLang = new Lang();
        currentLang.setCode("en");
        currentLang.setDescr("English");
        IPage currentPage = Mockito.mock(IPage.class);
        when(currentPage.isUseExtraTitles()).thenReturn(true);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE)).thenReturn(currentPage);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG)).thenReturn(currentLang);

        ContentRenderizationInfo renderizationInfo = Mockito.mock(ContentRenderizationInfo.class);
        when(this.contentDispenser.getRenderizationInfo(Mockito.anyString(),
                Mockito.anyLong(), Mockito.anyString(), Mockito.any(RequestContext.class), Mockito.anyBoolean())).thenReturn(renderizationInfo);
        when(renderizationInfo.getCachedRenderedContent()).thenReturn("Cached Rendered Content");
        when(renderizationInfo.getRenderedContent()).thenReturn("Final Rendered Content");
    }

    @Test
    public void testGetRenderedContent() throws Exception {
        HeadInfoContainer hic = Mockito.mock(HeadInfoContainer.class);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER)).thenReturn(hic);
        ContentModel model = Mockito.mock(ContentModel.class);
        when(this.contentModelManager.getContentModel(Mockito.anyLong())).thenReturn(model);
        when(model.getStylesheet()).thenReturn("..css style..");
        when(model.getContentShape()).thenReturn("Body of content model");
        String renderedContent = this.contentViewerHelper.getRenderedContent("ART123", "11", reqCtx);
        Assert.assertNotNull(renderedContent);
        Assert.assertEquals("Final Rendered Content", renderedContent);
        Mockito.verify(hic, Mockito.times(1)).addInfo("CSS", "..css style..");
        Mockito.verify(contentDispenser, Mockito.times(1)).getRenderizationInfo("ART123", 11, "en", reqCtx, true);
        Mockito.verify(contentDispenser, Mockito.times(1)).resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.any(RequestContext.class));
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
        Mockito.verifyZeroInteractions(contentManager);
    }

    @Test
    public void testGetRenderedContentWithListModel() throws Exception {
        HeadInfoContainer hic = Mockito.mock(HeadInfoContainer.class);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER)).thenReturn(hic);
        ContentModel model = Mockito.mock(ContentModel.class);
        when(this.contentModelManager.getContentModel(Mockito.anyLong())).thenReturn(model);
        when(this.contentManager.getListModel(Mockito.anyString())).thenReturn("34");
        when(model.getStylesheet()).thenReturn("..other css style..");
        when(model.getContentShape()).thenReturn("Body of content model");
        String renderedContent = this.contentViewerHelper.getRenderedContent("ART123", "list", reqCtx);
        Assert.assertNotNull(renderedContent);
        Assert.assertEquals("Final Rendered Content", renderedContent);
        Mockito.verify(hic, Mockito.times(1)).addInfo("CSS", "..other css style..");
        Mockito.verify(contentDispenser, Mockito.times(1)).getRenderizationInfo("ART123", 34, "en", reqCtx, true);
        Mockito.verify(contentDispenser, Mockito.times(1)).resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.any(RequestContext.class));
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
        Mockito.verify(contentManager, Mockito.times(1)).getListModel("ART123");
    }

    @Test(expected = ApsSystemException.class)
    public void testGetRenderedContentWithError() throws Exception {
        HeadInfoContainer hic = Mockito.mock(HeadInfoContainer.class);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER)).thenReturn(hic);
        Mockito.doThrow(RuntimeException.class).when(this.contentDispenser).resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.any(RequestContext.class));
        try {
            this.contentViewerHelper.getRenderedContent("NEW123", "10", reqCtx);
            Assert.fail();
        } catch (Exception e) {
            Mockito.verify(hic, Mockito.times(0)).addInfo(Mockito.anyString(), Mockito.anyString());
            Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            Mockito.verify(contentDispenser, Mockito.times(1)).getRenderizationInfo("NEW123", 10, "en", reqCtx, true);
            Mockito.verify(contentDispenser, Mockito.times(1)).resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.any(RequestContext.class));
            Mockito.verify(reqCtx, Mockito.times(0)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
            Mockito.verifyZeroInteractions(contentManager);
            throw e;
        }
    }

    @Test
    public void getRenderizationInfo() throws Exception {
        ContentModel model = Mockito.mock(ContentModel.class);
        when(this.contentModelManager.getContentModel(Mockito.anyLong())).thenReturn(model);
        when(this.contentManager.getDefaultModel(Mockito.anyString())).thenReturn("68");
        when(model.getStylesheet()).thenReturn(null);
        when(model.getContentShape()).thenReturn("Body of content model");
        when(this.contentDispenser.getRenderizationInfo(Mockito.anyString(),
                Mockito.anyLong(), Mockito.anyString(), Mockito.any(RequestContext.class), Mockito.anyBoolean())).thenReturn(null);
        String renderedContent = this.contentViewerHelper.getRenderedContent("ART124", "default", reqCtx);
        Assert.assertNotNull(renderedContent);
        Assert.assertTrue(StringUtils.isBlank(renderedContent));
        Mockito.verify(contentDispenser, Mockito.times(1)).getRenderizationInfo("ART124", 68, "en", reqCtx, true);
        Mockito.verify(contentDispenser, Mockito.times(0)).resolveLinks(Mockito.any(ContentRenderizationInfo.class), Mockito.any(RequestContext.class));
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        Mockito.verify(reqCtx, Mockito.times(1)).getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
        Mockito.verify(reqCtx, Mockito.times(0)).getExtraParam(SystemConstants.EXTRAPAR_HEAD_INFO_CONTAINER);
        Mockito.verify(contentManager, Mockito.times(1)).getDefaultModel("ART124");
    }

    @Test
    public void getAuthorizationInfo() throws Exception {
        PublicContentAuthorizationInfo pcaiMock = Mockito.mock(PublicContentAuthorizationInfo.class);
        when(this.contentAuthorizationHelper.getAuthorizationInfo("EVN100", true)).thenReturn(pcaiMock);
        Widget currentWidget = new Widget();
        ApsProperties properties = new ApsProperties();
        properties.setProperty("contentId", "EVN100");
        currentWidget.setConfig(properties);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET)).thenReturn(currentWidget);
        PublicContentAuthorizationInfo pcai = contentViewerHelper.getAuthorizationInfo(null, reqCtx);
        Assert.assertNotNull(pcai);
        Mockito.verify(reqCtx, Mockito.times(0)).getRequest();
    }

    @Test
    public void getNullAuthorizationInfo() throws Exception {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getParameter(SystemConstants.K_CONTENT_ID_PARAM)).thenReturn(null);
        when(this.reqCtx.getRequest()).thenReturn(mockRequest);
        Widget currentWidget = new Widget();
        ApsProperties properties = new ApsProperties();
        properties.setProperty("wrongParam", "xyz");
        currentWidget.setConfig(properties);
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET)).thenReturn(currentWidget);
        PublicContentAuthorizationInfo pcai = contentViewerHelper.getAuthorizationInfo(null, reqCtx);
        Assert.assertNull(pcai);
        Mockito.verify(reqCtx, Mockito.times(1)).getRequest();
        Mockito.verify(contentAuthorizationHelper, Mockito.times(0)).getAuthorizationInfo(Mockito.anyString(), Mockito.anyBoolean());
    }

    @Test(expected = ApsSystemException.class)
    public void getAuthorizationInfoWithError() throws Exception {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getParameter(SystemConstants.K_CONTENT_ID_PARAM)).thenReturn("ART123");
        when(this.reqCtx.getRequest()).thenReturn(mockRequest);
        Widget currentWidget = new Widget();
        currentWidget.setConfig(new ApsProperties());
        when(this.reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET)).thenReturn(currentWidget);
        Mockito.doThrow(RuntimeException.class).when(this.contentAuthorizationHelper).getAuthorizationInfo("ART123", true);
        try {
            contentViewerHelper.getAuthorizationInfo(null, reqCtx);
            Assert.fail();
        } catch (Exception e) {
            Mockito.verify(reqCtx, Mockito.times(1)).getRequest();
            throw e;
        }
    }

}
