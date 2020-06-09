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
package org.entando.entando.jpversioning.services.contentversioning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.entando.entando.plugins.jpversioning.services.contentsversioning.ContentVersioningService;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.model.ContentVersionDTO;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ContentVersioningServiceTest extends TestCase {

    private static final String CONTENT_ID = "TST";
    private static final Long CONTENT_VERSION_ID_1 =1L;
    private static final Long CONTENT_VERSION_ID_2 =2L;

    @Mock
    private VersioningManager manager;

    @InjectMocks
    private ContentVersioningService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetListContentVersions() throws ApsSystemException{
        RestListRequest requestList = new RestListRequest();
        List<Long> mockedVersions = new ArrayList();
        mockedVersions.add(CONTENT_VERSION_ID_1);
        mockedVersions.add(CONTENT_VERSION_ID_2);
        when(manager.getVersions(CONTENT_ID)).thenReturn(mockedVersions);
        when(manager.getVersion(CONTENT_VERSION_ID_1)).thenReturn(getMockedVersion1());
        when(manager.getVersion(2L)).thenReturn(getMockedVersion2());
        final PagedMetadata<ContentVersionDTO> listContentVersions = service
                .getListContentVersions(CONTENT_ID, requestList);
        assertThat(listContentVersions.getBody().size()).isEqualTo(mockedVersions.size());
        assertThat(listContentVersions.getBody().get(0).getId()).isEqualTo(CONTENT_VERSION_ID_1);
        assertThat(listContentVersions.getBody().get(1).getId()).isEqualTo(CONTENT_VERSION_ID_2);
    }

    private ContentVersion getMockedVersion1() {
        final ContentVersion contentVersion = new ContentVersion();
        contentVersion.setId(CONTENT_VERSION_ID_1);
        return contentVersion;
    }
    private ContentVersion getMockedVersion2() {
        final ContentVersion contentVersion = new ContentVersion();
        contentVersion.setId(CONTENT_VERSION_ID_2);
        return contentVersion;
    }
}
