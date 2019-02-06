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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import com.agiletec.plugins.jacms.aps.system.services.resource.cache.IResourceManagerCacheWrapper;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class ResourceManagerTest {

    @Mock
    private IResourceManagerCacheWrapper cacheWrapper;

    @Mock
    private IResourceDAO resourceDAO;

    @InjectMocks
    private ResourceManager resourceManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        AttachResource mockAttachResource = Mockito.mock(AttachResource.class);
        when(mockAttachResource.getType()).thenReturn("Attach");
        when(mockAttachResource.getResourcePrototype()).thenReturn(mockAttachResource);
        ImageResource mockImageResource = Mockito.mock(ImageResource.class);
        when(mockImageResource.getResourcePrototype()).thenReturn(mockImageResource);
        when(mockImageResource.getType()).thenReturn("Image");
        Map<String, ResourceInterface> types = new HashMap<>();
        types.put("Image", mockImageResource);
        types.put("Attach", mockAttachResource);
        this.resourceManager.setResourceTypes(types);
    }

    @Test
    public void status_should_be_ready_on_init() {
        when(cacheWrapper.getStatus()).thenReturn(IResourceManager.STATUS_READY);
        int status = this.resourceManager.getStatus();
        assertThat(status, is(IResourceManager.STATUS_READY));
    }

    public void createResourceType() {
        ResourceInterface type = this.resourceManager.createResourceType("Image");
        Assert.assertNotNull(type);
        Assert.assertEquals("Image", type.getType());
    }

}
