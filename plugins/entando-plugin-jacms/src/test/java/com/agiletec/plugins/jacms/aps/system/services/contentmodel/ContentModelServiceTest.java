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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.ArrayList;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.plugins.jacms.web.contentmodel.validator.ContentModelValidator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class ContentModelServiceTest {

    @Mock
    private IContentModelManager contentModelManager;

    @InjectMocks
    private ContentModelService contentModelService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetContentModelReferences() {
        // Existing content model
        when(contentModelManager.getContentModel(1)).thenReturn(new ContentModel());
        when(contentModelManager.getContentModelReferences(1l)).thenReturn(new ArrayList<>());
        assertEquals(0, contentModelService.getContentModelReferences(1l).size());

        // Unexisting content model
        when(contentModelManager.getContentModel(2)).thenReturn(null);
        RestRourceNotFoundException exception = null;
        try {
            contentModelService.getContentModelReferences(2l);
        } catch (RestRourceNotFoundException ex) {
            exception = ex;
        }
        assertNotNull(exception);
        assertEquals(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, exception.getErrorCode());
    }
}
