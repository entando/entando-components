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
package com.agiletec.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.opensymphony.xwork2.TextProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class ContentAdminActionTest {

    private static final String CONFIG_PARAMETER
            = "<Params><Param name=\"param_1\">value_1</Param><Param name=\"param_2\">value_2</Param></Params>";

    private static final String CONFIG_MAPPING
            = "<Params><Param name=\"param_1\">value_1</Param><Param name=\"param_2\">value_2</Param></Params>";

    private static final String ASPECT_RATIO_PARAMS
            = "<Params><ExtraParams><Param name=\"aspect_ratio\">16:9;2:3</Param></ExtraParams></Params>";

    @Mock
    private HttpServletRequest request;
    @Mock
    private ILangManager langManager;
    @Mock
    private ConfigInterface configManager;
    @Mock
    private IResourceManager resourceManager;
    @Mock
    private TextProvider textProvider;
    @Mock
    private ICmsSearchEngineManager searchEngineManager;
    @Mock
    private IContentManager contentManager;

    @InjectMocks
    private ContentAdminAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(textProvider.getText(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(ArgumentMatchers.anyString());
        List<String> metadataKeys = new ArrayList<>();
        metadataKeys.add("metadata_A");
        metadataKeys.add("metadata_B");
        this.action.setMetadataKeys(metadataKeys);
        when(request.getParameter("resourceMetadata_mapping_metadata_A")).thenReturn("value1,value2,value3");
        when(request.getParameter("resourceMetadata_mapping_metadata_B")).thenReturn("Value_a");
    }

    @Test
    public void validate_1() {
        this.action.setMetadataKey("metadata_C");
        this.action.validate();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(2, mapping.size());
        Assert.assertEquals(3, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
        Assert.assertNull(mapping.get("metadata_C"));
    }

    @Test
    public void validate_2() {
        this.action.setMetadataKey("metadata_B");
        this.action.validate();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(2, mapping.size());
        Assert.assertEquals(3, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
        Assert.assertTrue(action.getFieldErrors().size() > 0);
        Assert.assertEquals(1, action.getFieldErrors().get("metadataKey").size());
    }

    @Test
    public void addMetadata_1() {
        this.action.setMetadataKey("metadata_A");
        this.action.addMetadata();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(2, mapping.size());
        Assert.assertEquals(3, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
        Assert.assertEquals(0, action.getFieldErrors().size());
    }

    @Test
    public void addMetadata_2() {
        this.action.setMetadataKey("metadata_C");
        this.action.addMetadata();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(3, mapping.size());
        Assert.assertEquals(3, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
        Assert.assertEquals(0, mapping.get("metadata_C").size());
        Assert.assertEquals(0, action.getFieldErrors().size());
    }

    @Test
    public void addMetadata_3() {
        this.action.setMetadataKey("metadata_C");
        this.action.setMetadataMapping("Value_X,ValueY,Value_Z,Value_K,Value_J");
        this.action.addMetadata();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(3, mapping.size());
        Assert.assertEquals(3, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
        Assert.assertEquals(5, mapping.get("metadata_C").size());
        Assert.assertEquals(0, action.getFieldErrors().size());
    }

    @Test
    public void removeMetadata_1() {
        when(request.getParameter("resourceMetadata_mapping_metadata_A")).thenReturn("value1,value2,value3,newValue");
        this.action.setMetadataKey("metadata_C");
        this.action.removeMetadata();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(2, mapping.size());
        Assert.assertEquals(4, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
    }

    @Test
    public void removeMetadata_2() {
        this.action.setMetadataKey("metadata_A");
        this.action.removeMetadata();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(1, mapping.size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
    }

    @Test
    public void configSystemParams_1() {
        when(configManager.getConfigItem(Mockito.anyString())).thenThrow(RuntimeException.class);
        String result = action.configSystemParams();
        Assert.assertEquals(BaseAction.FAILURE, result);
        Mockito.verify(resourceManager, Mockito.times(0)).getMetadataMapping();
    }

    @Test
    public void configSystemParams_2() {
        when(configManager.getConfigItem(Mockito.anyString())).thenReturn(CONFIG_PARAMETER);
        when(resourceManager.getMetadataMapping()).thenThrow(RuntimeException.class);
        String result = action.configSystemParams();
        Assert.assertEquals(BaseAction.FAILURE, result);
    }

    @Test
    public void configSystemParams_3() {
        when(configManager.getConfigItem(Mockito.anyString())).thenReturn(CONFIG_PARAMETER);
        Map<String, List<String>> mapping = new HashMap<>();
        mapping.put("param_1", new ArrayList<>());
        when(resourceManager.getMetadataMapping()).thenReturn(mapping);
        String result = action.configSystemParams();
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Map<String, List<String>> extractedMapping = this.action.getMapping();
        Assert.assertEquals(1, extractedMapping.size());
        Assert.assertEquals(0, extractedMapping.get("param_1").size());
    }

    @Test
    public void updateSystemParams_1() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        Enumeration mockedEnumerator = Mockito.mock(Enumeration.class);
        when(mockedEnumerator.hasMoreElements()).thenReturn(false);
        when(request.getParameterNames()).thenReturn(mockedEnumerator);
        Mockito.doThrow(ApsSystemException.class).when(configManager).updateConfigItem(Mockito.anyString(), Mockito.anyString());
        String result = action.updateSystemParams();
        Assert.assertEquals(BaseAction.FAILURE, result);
        Mockito.verify(resourceManager, Mockito.times(0)).updateMetadataMapping(Mockito.any(Map.class));
    }

    @Test
    public void updateSystemParams_2() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        Enumeration mockedEnumerator = Mockito.mock(Enumeration.class);
        when(mockedEnumerator.hasMoreElements()).thenReturn(false);
        when(request.getParameterNames()).thenReturn(mockedEnumerator);
        //Mockito.doThrow(ApsSystemException.class).when(configManager).updateConfigItem(Mockito.anyString(), Mockito.anyString());
        String result = action.updateSystemParams();
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Mockito.verify(resourceManager, Mockito.times(1)).updateMetadataMapping(Mockito.any(Map.class));
        Mockito.verify(configManager, Mockito.times(1)).updateConfigItem(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void reloadContentsIndex_1() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        when(searchEngineManager.startReloadContentsReferences()).thenThrow(ApsSystemException.class);
        String result = action.reloadContentsIndex();
        Assert.assertEquals(BaseAction.FAILURE, result);
    }

    @Test
    public void reloadContentsIndex_2() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        String result = action.reloadContentsIndex();
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Mockito.verify(searchEngineManager, Mockito.times(1)).startReloadContentsReferences();
    }

    @Test
    public void reloadContentsReference_1() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        when(contentManager.reloadEntitiesReferences(null)).thenThrow(RuntimeException.class);
        String result = action.reloadContentsReference();
        Assert.assertEquals(BaseAction.FAILURE, result);
    }

    @Test
    public void reloadContentsReference_2() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        String result = action.reloadContentsReference();
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Mockito.verify(contentManager, Mockito.times(1)).reloadEntitiesReferences(null);
    }

    @Test
    public void testValidAspectRatioParams() throws Exception {
        List<String> ratio = Arrays.asList("16:9", "4:3");
        action.setRatio(ratio);
        action.validate();
        Assert.assertFalse(action.hasErrors());
    }

    @Test
    public void testInvalidAspectRatioParams() throws Exception {
        List<String> ratio = Arrays.asList("invalid");
        action.setRatio(ratio);
        action.validate();
        Assert.assertTrue(action.hasErrors());
    }

    @Test
    public void testAspectRatioUpdate() throws Exception {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(CONFIG_PARAMETER);
        Enumeration mockedEnumerator = Mockito.mock(Enumeration.class);
        when(mockedEnumerator.hasMoreElements()).thenReturn(false);
        when(request.getParameterNames()).thenReturn(mockedEnumerator);
        List<String> ratio = Arrays.asList("16:9", "4:3");
        action.setRatio(ratio);
        String result = action.updateSystemParams();
        Assert.assertEquals(BaseAction.SUCCESS, result);
        Assert.assertEquals("16:9;4:3", action.getAspectRatio());
    }

    @Test
    public void testAspectRatioFromParams() throws Throwable {
        when(configManager.getConfigItem(ArgumentMatchers.anyString())).thenReturn(ASPECT_RATIO_PARAMS);
        action.initLocalMap();
        Assert.assertEquals("16:9;2:3", action.getAspectRatio());
    }
}
