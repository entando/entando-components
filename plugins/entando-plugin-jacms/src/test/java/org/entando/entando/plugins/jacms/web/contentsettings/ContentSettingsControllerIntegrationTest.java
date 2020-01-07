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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsCropRatioRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.ContentSettingsEditorRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.CreateContentSettingsMetadataRequest;
import org.entando.entando.plugins.jacms.web.contentsettings.model.EditContentSettingsMetadataRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class ContentSettingsControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetContentSettingsUnauthorized() throws Exception {
        performGetContentSettings(null)
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetContentSettings() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performGetContentSettings(user)
            .andDo(print())
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
    public void testCreateEditAndRemoveMetadata() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateMetadata(user, "newkey", "newmappingvalue1,newmappingvalue2")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(5)))
            .andExpect(jsonPath("$.payload.legend", Matchers.anything()))
            .andExpect(jsonPath("$.payload.alt", Matchers.anything()))
            .andExpect(jsonPath("$.payload.title", Matchers.anything()))
            .andExpect(jsonPath("$.payload.description", Matchers.anything()))
            .andExpect(jsonPath("$.payload.newkey.size()", is(2)))
            .andExpect(jsonPath("$.payload.newkey[0]", Matchers.equalTo("newmappingvalue1")))
            .andExpect(jsonPath("$.payload.newkey[1]", Matchers.equalTo("newmappingvalue2")));

        performEditMetadata(user, "newkey", "newmappingvalue2")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(5)))
                .andExpect(jsonPath("$.payload.legend", Matchers.anything()))
                .andExpect(jsonPath("$.payload.alt", Matchers.anything()))
                .andExpect(jsonPath("$.payload.title", Matchers.anything()))
                .andExpect(jsonPath("$.payload.description", Matchers.anything()))
                .andExpect(jsonPath("$.payload.newkey.size()", is(1)))
                .andExpect(jsonPath("$.payload.newkey[0]", Matchers.equalTo("newmappingvalue2")));


        performRemoveMetadata(user, "newkey")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(4)));

    }

    @Test
    public void testCreateDuplicateMetadata() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateMetadata(user, "newkey", "newmappingvalue1,newmappingvalue2")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(5)))
                .andExpect(jsonPath("$.payload.newkey.size()", is(2)))
                .andExpect(jsonPath("$.payload.newkey[0]", Matchers.equalTo("newmappingvalue1")))
                .andExpect(jsonPath("$.payload.newkey[1]", Matchers.equalTo("newmappingvalue2")));

        performCreateMetadata(user, "newkey", "newmappingvalue3")
                .andDo(print())
                .andExpect(status().isConflict());

        performRemoveMetadata(user, "newkey")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));
    }

    @Test
    public void testEditNonExistentMetadata() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performEditMetadata(user, "invalid", "newmappingvalue3")
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRemoveNonExistentMetadata() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performRemoveMetadata(user, "invalid")
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateInvalidMetadata() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateMetadata(user, "new-key", "newmappingvalue")
                .andDo(print())
                .andExpect(status().isBadRequest());

        performCreateMetadata(user, "{newkey}", "newmappingvalue")
                .andDo(print())
                .andExpect(status().isBadRequest());

        performCreateMetadata(user, "NEWKEY!", "newmappingvalue")
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEditAndRemoveCropRatios() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateCropRatio(user, "4:3")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("4:3")));

        performEditCropRatio(user, "4:3", "8:6")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("8:6")));

        performCreateCropRatio(user, "16:9")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(2)))
            .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("8:6")))
            .andExpect(jsonPath("$.payload[1]", Matchers.equalTo("16:9")));

        performRemoveCropRatio(user, "4:3")
            .andDo(print())
            .andExpect(status().isNotFound());

        performRemoveCropRatio(user, "8:6")
                .andDo(print())
                .andExpect(status().isOk());

        performRemoveCropRatio(user, "16:9")
            .andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    public void testCreateDuplicateCropRatios() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateCropRatio(user, "4:3")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("4:3")));

        performCreateCropRatio(user, "4:3")
            .andDo(print())
            .andExpect(status().isConflict());

        performRemoveCropRatio(user, "4:3")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(0)));
    }

    @Test
    public void testCreateCropRatiosInvalidFormat() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateCropRatio(user, "alfa,3")
                .andDo(print())
                .andExpect(status().isBadRequest());

        performCreateCropRatio(user, "alfa:3")
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditCropRatiosInvalidFormat() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performCreateCropRatio(user, "4:3")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0]", Matchers.equalTo("4:3")));

        performEditCropRatio(user, "4:3", "alfa:6")
                .andDo(print())
                .andExpect(status().isBadRequest());

        performEditCropRatio(user, "4:3", "4-6")
                .andDo(print())
                .andExpect(status().isBadRequest());

        performRemoveCropRatio(user, "4:3")
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testChangeEditor() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performSetEditor(user, "none")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.key", Matchers.equalTo("none")))
            .andExpect(jsonPath("$.payload.label", Matchers.equalTo("None")));

        performSetEditor(user, "fckeditor")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.key", Matchers.equalTo("fckeditor")))
            .andExpect(jsonPath("$.payload.label", Matchers.equalTo("CKEditor")));
    }

    @Test
    public void testReloadIndexes() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performReloadIndexes(user)
            .andDo(print())
            .andExpect(status().isOk());

        performGetContentSettings(user)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.indexesStatus", Matchers.equalTo(1)));
    }

    @Test
    public void testReloadReferences() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.SUPERUSER)
                .build();

        performReloadReferences(user)
            .andDo(print())
            .andExpect(status().isOk());

        performGetContentSettings(user)
            .andDo(print())
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

        CreateContentSettingsMetadataRequest request = new CreateContentSettingsMetadataRequest();
        request.setKey(key);
        request.setMapping(mapping);

        return mockMvc.perform(
                post(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performEditMetadata(UserDetails user, String key, String mapping) throws Exception {
        String path = "/plugins/cms/contentSettings/metadata/" + key;
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        EditContentSettingsMetadataRequest request = new EditContentSettingsMetadataRequest();
        request.setMapping(mapping);

        return mockMvc.perform(
                put(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performRemoveMetadata(UserDetails user, String key) throws Exception {
        String path = "/plugins/cms/contentSettings/metadata/" + key;
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        return mockMvc.perform(
                delete(path)
                    .header("Authorization", "Bearer " + accessToken)
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

    private ResultActions performEditCropRatio(UserDetails user, String ratio, String newRatio) throws Exception {
        String path = "/plugins/cms/contentSettings/cropRatios/" + ratio;
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        ContentSettingsCropRatioRequest request = new ContentSettingsCropRatioRequest();
        request.setRatio(newRatio);

        return mockMvc.perform(
                put(path)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions performRemoveCropRatio(UserDetails user, String ratio) throws Exception {
        String path = "/plugins/cms/contentSettings/cropRatios/" + ratio;
        if (null == user) {
            return mockMvc.perform(get(path));
        }
        String accessToken = mockOAuthInterceptor(user);

        return mockMvc.perform(
                delete(path)
                    .header("Authorization", "Bearer " + accessToken)
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
