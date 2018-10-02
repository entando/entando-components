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

import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class ContentAdminActionTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private ILangManager langManager;
    @Mock
    private IAuthorizationManager authorizationManager;
    @InjectMocks
    private ContentAdminAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validate_1() {
        List<String> metadataKeys = new ArrayList<>();
        metadataKeys.add("metadata_1");
        metadataKeys.add("metadata_2");
        this.action.setMetadataKeys(metadataKeys);
        this.action.setMetadataKey("metadata_3");
        when(request.getParameter("resourceMetadata_mapping_metadata_1")).thenReturn("value1,value2,value3,value4");
        when(request.getParameter("resourceMetadata_mapping_metadata_2")).thenReturn("Value_a");
        this.action.validate();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(2, mapping.size());
        Assert.assertEquals(4, mapping.get("metadata_1").size());
        Assert.assertEquals(1, mapping.get("metadata_2").size());
        Assert.assertNull(mapping.get("metadata_3"));
    }

    @Test
    public void validate_2() {
        List<String> metadataKeys = new ArrayList<>();
        metadataKeys.add("metadata_A");
        metadataKeys.add("metadata_B");
        this.action.setMetadataKeys(metadataKeys);
        this.action.setMetadataKey("metadata_B");
        when(request.getParameter("resourceMetadata_mapping_metadata_A")).thenReturn("value1,value2");
        when(request.getParameter("resourceMetadata_mapping_metadata_B")).thenReturn("Value_a");
        this.action.validate();
        Map<String, List<String>> mapping = this.action.getMapping();
        Assert.assertEquals(2, mapping.size());
        Assert.assertEquals(2, mapping.get("metadata_A").size());
        Assert.assertEquals(1, mapping.get("metadata_B").size());
        Assert.assertTrue(action.getFieldErrors().size() > 0);
        Assert.assertEquals(1, action.getFieldErrors().get("metadataKey").size());
    }

}
