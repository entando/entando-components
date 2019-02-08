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
package org.entando.entando.plugins.jacms.web.content;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.plugins.jacms.web.content.validator.ContentValidator;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.validation.BindingResult;

public class ContentControllerTest extends AbstractControllerTest {

    @Mock
    private ContentValidator contentValidator;

    @Mock
    private IContentService contentService;

    @InjectMocks
    private ContentController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void shouldGetExistingContent() throws Exception {
        UserDetails user = this.createUser(true);
        when(this.contentValidator.existContent("ART123", IContentService.STATUS_DRAFT)).thenReturn(true);
        when(this.contentService.getContent(Mockito.eq("ART123"), Mockito.isNull(),
                Mockito.eq("draft"), Mockito.isNull(), Mockito.any(UserDetails.class))).thenReturn(Mockito.mock(ContentDto.class));
        ResultActions result = performGetContent("ART123", null, false, null, user);
        result.andExpect(status().isOk());
    }

    @Test
    public void testUnexistingContent() throws Exception {
        UserDetails user = this.createUser(true);
        when(this.contentValidator.existContent("ART098", IContentService.STATUS_ONLINE)).thenReturn(false);
        ResultActions result = performGetContent("ART098", null, true, null, user);
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testAddContent() throws Exception {
        UserDetails user = this.createUser(true);
        when(this.contentService.addContent(Mockito.any(ContentDto.class), Mockito.any(UserDetails.class), Mockito.any(BindingResult.class)))
                .thenReturn(Mockito.mock(ContentDto.class));
        String mockJson = "{\n"
                + "    \"id\": \"ART123\",\n"
                + "    \"typeCode\": \"ART\",\n"
                + "    \"attributes\": [\n"
                + "         {\"code\": \"code1\", \"value\": \"value1\"},\n"
                + "         {\"code\": \"code2\", \"value\": \"value2\"}\n"
                + "    ]}";
        ResultActions result = this.performPostContent(mockJson, user);
        result.andExpect(status().isOk());
    }

    @Test
    public void testUpdateContent() throws Exception {
        UserDetails user = this.createUser(true);
        when(this.contentService.updateContent(Mockito.any(ContentDto.class), Mockito.any(UserDetails.class), Mockito.any(BindingResult.class)))
                .thenReturn(Mockito.mock(ContentDto.class));
        String mockJson = "{\n"
                + "    \"id\": \"ART123\",\n"
                + "    \"typeCode\": \"ART\",\n"
                + "    \"attributes\": [\n"
                + "         {\"code\": \"code1\", \"value\": \"value1\"},\n"
                + "         {\"code\": \"code2\", \"value\": \"value2\"}\n"
                + "    ]}";
        ResultActions result = this.performPutContent("user", mockJson, user);
        result.andExpect(status().isOk());
    }

    private ResultActions performGetContent(String code, String modelId,
            boolean online, String langCode, UserDetails user) throws Exception {
        String accessToken = mockOAuthInterceptor(user);
        String path = "/plugins/cms/contents/{code}";
        if (null != modelId) {
            path += "/model/" + modelId;
        }
        path += "?status=" + ((online) ? IContentService.STATUS_ONLINE : IContentService.STATUS_DRAFT);
        if (null != langCode) {
            path += "&lang=" + langCode;
        }
        return mockMvc.perform(
                get(path, code)
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performPostContent(String jsonContent, UserDetails user) throws Exception {
        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                post("/plugins/cms/contents")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performPutContent(String code, String jsonContent, UserDetails user) throws Exception {
        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                put("/plugins/cms/contents/{code}", code)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
