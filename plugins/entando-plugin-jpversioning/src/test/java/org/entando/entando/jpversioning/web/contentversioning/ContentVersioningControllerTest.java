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
package org.entando.entando.jpversioning.web.contentversioning;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.entando.entando.plugins.jpversioning.services.contentsversioning.ContentVersioningService;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.ContentVersioningController;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.model.ContentVersionDTO;
import org.entando.entando.plugins.jpversioning.web.contentsversioning.validator.ContentVersioningValidator;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ContentVersioningControllerTest extends AbstractControllerTest {

    private static final String CONTENT_ID = "TST";

    @Mock
    private ContentVersioningValidator validator;

    @Mock
    private ContentVersioningService service;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private ContentVersioningController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void testGetExistingContentVersioning() throws Exception {
        PagedMetadata<ContentVersionDTO> pagedMetadata = getContentVersionDTOPagedMetadata();
        UserDetails user = this.createUser(true);
        when(this.httpSession.getAttribute("user")).thenReturn(user);
        when(this.validator.contentVersioningExist(CONTENT_ID)).thenReturn(true);
        when(this.service.getListContentVersions(Mockito.eq(CONTENT_ID), Mockito.any(RestListRequest.class)))
                .thenReturn(pagedMetadata);
        ResultActions result = getListContentVersioning(CONTENT_ID, user);
        result.andExpect(status().isOk());
    }

    @Test
    public void testNotAuthorizedGetExistingContentVersioning() throws Exception {
        PagedMetadata<ContentVersionDTO> pagedMetadata = getContentVersionDTOPagedMetadata();
        UserDetails user = this.createUser(false);
        when(this.httpSession.getAttribute("user")).thenReturn(user);
        when(this.validator.contentVersioningExist(CONTENT_ID)).thenReturn(true);
        when(this.service.getListContentVersions(Mockito.eq(CONTENT_ID), Mockito.any(RestListRequest.class)))
                .thenReturn(pagedMetadata);
        ResultActions result = getListContentVersioning(CONTENT_ID, user);
        result.andExpect(status().isForbidden());
    }

    @Test
    public void testGetExistingContentVersioningNotExist() throws Exception {
        PagedMetadata<ContentVersionDTO> pagedMetadata = getContentVersionDTOPagedMetadata();
        UserDetails user = this.createUser(true);
        when(this.httpSession.getAttribute("user")).thenReturn(user);
        when(this.validator.contentVersioningExist(CONTENT_ID)).thenReturn(false);
        ResultActions result = getListContentVersioning(CONTENT_ID, user);
        result.andExpect(status().isNotFound());
    }

    private PagedMetadata<ContentVersionDTO> getContentVersionDTOPagedMetadata() {
        PagedMetadata<ContentVersionDTO> pagedMetadata = new PagedMetadata<>();
        List<ContentVersionDTO> allResults = new ArrayList<>();
        allResults.add(new ContentVersionDTO());
        pagedMetadata.setBody(allResults);
        return pagedMetadata;
    }

    private ResultActions getListContentVersioning(String contentId, UserDetails user) throws Exception {
        String accessToken = mockOAuthInterceptor(user);
        String path = "/plugins/versioning/contents/{contentId}";
        return mockMvc.perform(
                get(path, contentId)
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
    }

    private UserDetails createUser(boolean adminAuth) throws Exception {
        UserDetails user = (adminAuth) ? (new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.ADMINS_GROUP_NAME, "roletest", Permission.CONTENT_EDITOR)
                .build())
                : (new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                        .withAuthorization(Group.FREE_GROUP_NAME, "roletest", Permission.MANAGE_PAGES)
                        .build());
        return user;
    }
}
