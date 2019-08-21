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
package org.entando.entando.plugins.jacms.web.contentsettings;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsCropRatioRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsEditorRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsMetadataRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContentSettingsControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetContentSettingsUnauthorized() throws Exception {
        ResultActions result = this.performGetContentSettings(null);

        String result1 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1);
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetContentSettings() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        ResultActions result = this.performGetContentSettings(user);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.metadata.size()", is(4)))
            .andExpect(jsonPath("$.payload.metadata.legend", Matchers.anything()))
            .andExpect(jsonPath("$.payload.metadata.alt", Matchers.anything()))
            .andExpect(jsonPath("$.payload.metadata.title", Matchers.anything()))
            .andExpect(jsonPath("$.payload.metadata.description", Matchers.anything()))

            .andExpect(jsonPath("$.payload.cropRatios.size()", is(0)))

            .andExpect(jsonPath("$.payload.referencesStatus", Matchers.anything()))
            .andExpect(jsonPath("$.payload.indexesStatus", Matchers.anything()))
            .andExpect(jsonPath("$.payload.indexesLastReload", Matchers.nullValue()))

            .andExpect(jsonPath("$.payload.editor.key", Matchers.equalTo("fckeditor")))
            .andExpect(jsonPath("$.payload.editor.label", Matchers.equalTo("CKEditor")));
    }

    @Test
    public void testCreateAndRemoveMetadata() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        ResultActions result = this.performCreateMetadata(user, "newKey", "newMappingValue1,newMappingValue2");

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(5)))
            .andExpect(jsonPath("$.payload.legend", Matchers.anything()))
            .andExpect(jsonPath("$.payload.alt", Matchers.anything()))
            .andExpect(jsonPath("$.payload.title", Matchers.anything()))
            .andExpect(jsonPath("$.payload.description", Matchers.anything()))
            .andExpect(jsonPath("$.payload.newKey.size()", is(2)))
            .andExpect(jsonPath("$.payload.newKey[0]", Matchers.equalTo("newMappingValue1")))
            .andExpect(jsonPath("$.payload.newKey[1]", Matchers.equalTo("newMappingValue2")));


        result = this.performRemoveMetadata(user, "newKey");

        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(4)));

    }

    @Test
    public void testCreateAndRemoveCropRatios() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        ResultActions result = this.performCreateCropRatio(user, "4:3");

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("4:3")));

        result = this.performCreateCropRatio(user, "16:9");
        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)))
                .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("4:3")))
                .andExpect(jsonPath("$.payload[1]", Matchers.equalTo("16:9")));


        result = this.performRemoveCropRatio(user, "4:3");
        result = this.performRemoveCropRatio(user, "16:9");

        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

    }

    @Test
    public void testChangeEditor() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        ResultActions result = this.performSetEditor(user, "none");

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.key", Matchers.equalTo("none")))
                .andExpect(jsonPath("$.payload.label", Matchers.equalTo("None")));

        result = this.performSetEditor(user, "fckeditor");
        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.key", Matchers.equalTo("fckeditor")))
                .andExpect(jsonPath("$.payload.label", Matchers.equalTo("CKEditor")));

    }

    @Test
    public void testReloadIndexes() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        ResultActions result = this.performReloadIndexes(user);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk());

        result = this.performGetContentSettings(user);
        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.indexesStatus", Matchers.equalTo(1)));
    }

    @Test
    public void testReloadReferences() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        ResultActions result = this.performReloadReferences(user);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk());

        result = this.performGetContentSettings(user);
        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.referencesStatus", Matchers.equalTo(1)));
    }

    private String createAccessToken() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        return mockOAuthInterceptor(user);
    }

    private ResultActions performGetContentSettings(UserDetails user) throws Exception {
        String path = "/plugins/cms/contentSettings";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                get(path)
                    .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performCreateMetadata(UserDetails user, String key, String mapping) throws Exception {
        String path = "/plugins/cms/contentSettings/metadata";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        ContentSettingsMetadataRequest request = new ContentSettingsMetadataRequest();
        request.setKey(key);
        request.setMapping(mapping);

        return mockMvc.perform(
                post(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performRemoveMetadata(UserDetails user, String key) throws Exception {
        String path = "/plugins/cms/contentSettings/metadata";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        ContentSettingsMetadataRequest request = new ContentSettingsMetadataRequest();
        request.setKey(key);

        return mockMvc.perform(
                delete(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performCreateCropRatio(UserDetails user, String ratio) throws Exception {
        String path = "/plugins/cms/contentSettings/cropRatios/";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        ContentSettingsCropRatioRequest request = new ContentSettingsCropRatioRequest();
        request.setRatio(ratio);

        return mockMvc.perform(
                post(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performRemoveCropRatio(UserDetails user, String ratio) throws Exception {
        String path = "/plugins/cms/contentSettings/cropRatios";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        ContentSettingsCropRatioRequest request = new ContentSettingsCropRatioRequest();
        request.setRatio(ratio);

        return mockMvc.perform(
                delete(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performSetEditor(UserDetails user, String editor) throws Exception {
        String path = "/plugins/cms/contentSettings/editor";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        ContentSettingsEditorRequest request = new ContentSettingsEditorRequest();
        request.setKey(editor);

        return mockMvc.perform(
                put(path)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performReloadIndexes(UserDetails user) throws Exception {
        String path = "/plugins/cms/contentSettings/reloadIndexes";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        return mockMvc.perform(
                post(path)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performReloadReferences(UserDetails user) throws Exception {
        String path = "/plugins/cms/contentSettings/reloadReferences";
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        return mockMvc.perform(
                post(path)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8));
    }

}
