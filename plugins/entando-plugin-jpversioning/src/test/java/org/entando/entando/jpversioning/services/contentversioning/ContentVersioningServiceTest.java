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
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.plugins.jacms.aps.system.services.content.ContentService;
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
    private static final Long VERSION_ID_1 =1L;
    private static final Long VERSION_ID_2 =2L;
    private static final String STATUS = "DRAFT";

    @Mock
    private VersioningManager manager;

    @Mock
    private ContentVersion contentVersion;

    @Mock
    private DtoBuilder dtoBuilder;

    @Mock
    private ContentService contentService;

    @Mock
    private ContentManager contentManager;

    @Mock
    private ContentRecordVO currentRecordVo;

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
        mockedVersions.add(VERSION_ID_1);
        mockedVersions.add(VERSION_ID_2);
        when(manager.getVersions(CONTENT_ID)).thenReturn(mockedVersions);
        when(manager.getVersion(VERSION_ID_1)).thenReturn(getMockedVersion1());
        when(manager.getVersion(2L)).thenReturn(getMockedVersion2());
        final PagedMetadata<ContentVersionDTO> listContentVersions = service
                .getListContentVersions(CONTENT_ID, requestList);
        assertThat(listContentVersions.getBody().size()).isEqualTo(mockedVersions.size());
        assertThat(listContentVersions.getBody().get(0).getId()).isEqualTo(VERSION_ID_1);
        assertThat(listContentVersions.getBody().get(1).getId()).isEqualTo(VERSION_ID_2);
    }

    @Test
    public void testGetContent() throws ApsSystemException {
        Content content = new Content();
        ContentDto contentDto = new ContentDto();
        contentDto.setStatus(STATUS);

        when(contentService.getDtoBuilder()).thenReturn(dtoBuilder);
        when(dtoBuilder.convert(content)).thenReturn(contentDto);
        when(manager.getVersion(VERSION_ID_1)).thenReturn(contentVersion);
        when(manager.getContent(contentVersion)).thenReturn(content);

        final ContentDto contentDto1 = service.getContent(VERSION_ID_1);
        assertThat(contentDto1.getStatus()).isEqualTo(STATUS);
    }

    @Test
    public void testRecoverContentVersion() throws ApsSystemException {
        Content content = new Content();
        ContentDto contentDto = new ContentDto();
        String contentXml = null;
        try {
            InputStream xml = getClass().getClassLoader().getResourceAsStream("xml/content_test.xml");
            contentXml = FileTextReader.getText(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentDto.setStatus(STATUS);
        when(manager.getVersion(VERSION_ID_1)).thenReturn(contentVersion);
        when(contentVersion.getXml()).thenReturn(contentXml);
        when(contentVersion.getContentId()).thenReturn(CONTENT_ID);
        when(contentManager.loadContentVO(CONTENT_ID)).thenReturn(currentRecordVo);
        when(currentRecordVo.getVersion()).thenReturn("1.8");
        when(manager.getLastVersion(CONTENT_ID)).thenReturn(contentVersion);
        when(manager.getContent(contentVersion)).thenReturn(content);
        when(contentService.getDtoBuilder()).thenReturn(dtoBuilder);
        when(dtoBuilder.convert(content)).thenReturn(contentDto);
        final ContentDto recoveredContent = service.recover(VERSION_ID_1);
        assertThat(recoveredContent.getStatus()).isEqualTo(STATUS);
    }

    private ContentVersion getMockedVersion1() {
        final ContentVersion contentVersion = new ContentVersion();
        contentVersion.setId(VERSION_ID_1);
        return contentVersion;
    }
    private ContentVersion getMockedVersion2() {
        final ContentVersion contentVersion = new ContentVersion();
        contentVersion.setId(VERSION_ID_2);
        return contentVersion;
    }
}
