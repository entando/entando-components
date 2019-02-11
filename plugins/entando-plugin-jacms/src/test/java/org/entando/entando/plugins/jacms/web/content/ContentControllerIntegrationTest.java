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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.jayway.jsonpath.JsonPath;
import java.io.InputStream;
import java.util.Date;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ContentControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IContentManager contentManager;

    @Test
    public void testGetContent_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        ResultActions result = this.performGetContent("ART187", null, true, null, user);
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(header().string("Access-Control-Allow-Origin", "*"));
        result.andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
        result.andExpect(header().string("Access-Control-Allow-Headers", "Content-Type, Authorization"));
        result.andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    public void testGetContent_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        ResultActions result = this.performGetContent("ART187", null, false, null, user);
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result = this.performGetContent("ART187", null, true, null, user);
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.nullValue()));
    }

    @Test
    public void testGetContent_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        ResultActions result = this.performGetContent("ART179", null, false, null, user);
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        result = this.performGetContent("ART179", null, true, null, user);
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload", Matchers.empty()));
    }

    @Test
    public void testGetContentWithModel() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        ResultActions result = this.performGetContent("ART180", "1", true, null, user);
        String result1 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1);
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        result = this.performGetContent("ART180", "11", true, null, user);
        String result2 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2);

        result.andExpect(status().isOk());
        result = this.performGetContent("ART180", "default", true, null, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result1_copy = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1_copy);
        result.andExpect(status().isOk());
        Assert.assertEquals(result1, result1_copy);
        result = this.performGetContent("ART180", "list", true, null, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result2_copy = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2);
        result.andExpect(status().isOk());
        Assert.assertEquals(result2, result2_copy);

        result = this.performGetContent("ART180", "list", true, "en", user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result2_copy_en = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2_copy_en);
        result.andExpect(status().isOk());
        Assert.assertNotEquals(result2_copy_en, result2_copy);
    }

    @Test
    public void testGetInvalidContent() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        ResultActions result = this.performGetContent("ART985", null, true, null, user);
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testAddUpdateContent() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("TST"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_valid.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("TST"));

            ResultActions result = this.executeContentPost("1_POST_invalid.json", accessToken, status().isBadRequest());
            result.andExpect(jsonPath("$.payload.size()", is(0)));
            result.andExpect(jsonPath("$.errors.size()", is(3)));
            result.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result2 = this.executeContentPost("1_POST_valid.json", accessToken, status().isOk());
            result2.andExpect(jsonPath("$.payload.id", Matchers.anything()));
            result2.andExpect(jsonPath("$.errors.size()", is(0)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));
            String bodyResult = result2.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload.id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);
            Date date = (Date) newContent.getAttribute("Date").getValue();
            Assert.assertEquals("2017-09-21", DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
            Boolean booleanValue = (Boolean) newContent.getAttribute("Boolean").getValue();
            Assert.assertTrue(booleanValue);
            Boolean threeState = (Boolean) newContent.getAttribute("ThreeState").getValue();
            Assert.assertNull(threeState);

            ResultActions result3 = this.executeContentPut("1_PUT_valid.json", newContentId, "invalid", accessToken, status().isConflict());
            result3.andExpect(jsonPath("$.payload.size()", is(0)));
            result3.andExpect(jsonPath("$.errors.size()", is(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("2")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeContentPut("1_PUT_valid.json", newContentId, newContentId, accessToken, status().isOk());
            result4.andExpect(jsonPath("$.payload.id", is(newContentId)));
            result4.andExpect(jsonPath("$.errors.size()", is(0)));
            result4.andExpect(jsonPath("$.metaData.size()", is(0)));
            result4.andExpect(jsonPath("$.payload.attributes[0].code", is("Title")));
            result4.andExpect(jsonPath("$.payload.attributes[0].value", is("My title")));
            result4.andExpect(jsonPath("$.payload.attributes[0].values", Matchers.anything()));
            result4.andExpect(jsonPath("$.payload.attributes[0].elements.size()", is(0)));
            result4.andExpect(jsonPath("$.payload.attributes[0].compositeelements.size()", is(0)));
            result4.andExpect(jsonPath("$.payload.attributes[0].listelements", Matchers.anything()));
            newContent = this.contentManager.loadContent(newContentId, false);
            date = (Date) newContent.getAttribute("Date").getValue();
            Assert.assertEquals("2018-03-21", DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
            booleanValue = (Boolean) newContent.getAttribute("Boolean").getValue();
            Assert.assertFalse(booleanValue);
            threeState = (Boolean) newContent.getAttribute("ThreeState").getValue();
            Assert.assertNotNull(threeState);
            Assert.assertTrue(threeState);

            ListAttribute list = (ListAttribute) newContent.getAttribute("multilist");
            Assert.assertEquals(4, list.getAttributeList("en").size());
        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("TST");
            }
        }
    }

    private String createAccessToken() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        return mockOAuthInterceptor(user);
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

    private ResultActions executeContentPost(String fileName, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/plugins/cms/contents")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeContentPut(String fileName, String valueToReplace, String idInPath, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPutValid = FileTextReader.getText(isJsonPostValid);
        jsonPutValid = jsonPutValid.replace("**MARKER**", valueToReplace);
        ResultActions result = mockMvc
                .perform(put("/plugins/cms/contents/{code}", new Object[]{idInPath})
                        .content(jsonPutValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeContentTypePost(String fileName, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/plugins/cms/contentTypes")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    @Test
    public void testGetContents() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filter[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filter[0].operator", "eq")
                        .param("filter[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(Matchers.greaterThan(0))));
    }

    @Test
    public void testLoadOrderedPublicEvents_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_DESCR_FILTER_KEY)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("filter[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filter[0].operator", "eq")
                        .param("filter[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        System.out.println(bodyResult);
        result.andExpect(status().isOk());
        String[] expectedFreeContentsId = {"EVN24", "EVN23",
            "EVN191", "EVN192", "EVN193", "EVN194", "EVN20", "EVN21", "EVN25"};
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedFreeContentsId.length, payloadSize);
        for (int i = 0; i < expectedFreeContentsId.length; i++) {
            String expectedId = expectedFreeContentsId[i];
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedId, extractedId);
        }
    }

}
