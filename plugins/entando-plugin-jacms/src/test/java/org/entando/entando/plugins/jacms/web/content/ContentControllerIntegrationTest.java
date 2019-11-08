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
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import org.entando.entando.plugins.jacms.web.content.validator.ContentStatusRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContentControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IContentManager contentManager;

    @Autowired
    private ICmsSearchEngineManager searchEngineManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetContentWithModel() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        ResultActions result = this.performGetContent("ART180", "1", true, null, true, user);
        String result1 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1);
        result.andExpect(status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        result = this.performGetContent("ART180", "11", true, null, true, user);
        String result2 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2);

        result.andExpect(status().isOk());
        result = this.performGetContent("ART180", "default", true, null, true, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result1_copy = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1_copy);
        result.andExpect(status().isOk());
        Assert.assertEquals(result1, result1_copy);
        result = this.performGetContent("ART180", "list", true, null, true, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result2_copy = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2);
        result.andExpect(status().isOk());
        Assert.assertEquals(result2, result2_copy);

        result = this.performGetContent("ART180", "list", true, "en", true, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result2_copy_en = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2_copy_en);
        result.andExpect(status().isOk());
        Assert.assertNotEquals(result2_copy_en, result2_copy);
    }

    @Test
    public void testGetInvalidContent() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        ResultActions result = this.performGetContent("ART985", null, true, null, true, user);
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
            result2.andDo(print());
            result2.andExpect(jsonPath("$.payload.size()", is(1)));
            result2.andExpect(jsonPath("$.payload[0].id", Matchers.anything()));
            result2.andExpect(jsonPath("$.payload[0].firstEditor", is("jack_bauer")));
            result2.andExpect(jsonPath("$.payload[0].lastEditor", is("jack_bauer")));
            result2.andExpect(jsonPath("$.errors.size()", is(0)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));
            String bodyResult = result2.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);
            Date date = (Date) newContent.getAttribute("Date").getValue();
            Assert.assertEquals("2017-09-21", DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
            Boolean booleanValue = (Boolean) newContent.getAttribute("Boolean").getValue();
            Assert.assertTrue(booleanValue);
            Boolean threeState = (Boolean) newContent.getAttribute("ThreeState").getValue();
            Assert.assertNull(threeState);

            ResultActions result3 = this.executeContentPut("1_PUT_valid.json", "invalid", accessToken, status().isNotFound());
            result3.andExpect(jsonPath("$.payload.size()", is(0)));
            result3.andExpect(jsonPath("$.errors.size()", is(1)));
            result3.andExpect(jsonPath("$.errors[0].code", is("1")));
            result3.andExpect(jsonPath("$.metaData.size()", is(0)));

            ResultActions result4 = this.executeContentPut("1_PUT_valid.json", newContentId, accessToken, status().isOk());
            result4.andExpect(jsonPath("$.payload.size()", is(1)));
            result4.andExpect(jsonPath("$.errors.size()", is(0)));
            result4.andExpect(jsonPath("$.metaData.size()", is(0)));
            result4.andExpect(jsonPath("$.payload[0].id", is(newContentId)));
            result4.andExpect(jsonPath("$.payload[0].attributes[0].code", is("Title")));
            result4.andExpect(jsonPath("$.payload[0].attributes[0].value", is("My title")));
            result4.andExpect(jsonPath("$.payload[0].attributes[0].values", Matchers.anything()));
            result4.andExpect(jsonPath("$.payload[0].attributes[0].elements.size()", is(0)));
            result4.andExpect(jsonPath("$.payload[0].attributes[0].compositeelements.size()", is(0)));
            result4.andExpect(jsonPath("$.payload[0].attributes[0].listelements", Matchers.anything()));
            result4.andExpect(jsonPath("$.payload[0].firstEditor", is("jack_bauer")));
            result4.andExpect(jsonPath("$.payload[0].lastEditor", is("jack_bauer")));
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

    @Test
    public void testAddDeleteContent() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("TST"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_valid.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("TST"));

            ResultActions result = this.executeContentPost("1_POST_valid.json", accessToken, status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(1)));
            result.andExpect(jsonPath("$.payload[0].id", Matchers.anything()));
            result.andExpect(jsonPath("$.errors.size()", is(0)));
            result.andExpect(jsonPath("$.metaData.size()", is(0)));
            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);
            Assert.assertNotNull(newContent);
            Content newPublicContent = this.contentManager.loadContent(newContentId, true);
            Assert.assertNull(newPublicContent);

            ContentStatusRequest contentStatusRequest = new ContentStatusRequest();
            contentStatusRequest.setStatus("published");
            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            newPublicContent = this.contentManager.loadContent(newContentId, true);
            Assert.assertNotNull(newPublicContent);

            contentStatusRequest.setStatus("draft");
            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            newPublicContent = this.contentManager.loadContent(newContentId, true);
            Assert.assertNull(newPublicContent);

            result = mockMvc
                    .perform(delete("/plugins/cms/contents")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapper.writeValueAsString(new String[] { newContentId }))
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(1)));
            result.andExpect(jsonPath("$.payload[0]", is(newContentId)));
            Assert.assertNull(this.contentManager.loadContent(newContentId, false));
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
            boolean online, String langCode, boolean resolveLink, UserDetails user) throws Exception {
        String path = "/plugins/cms/contents/{code}";
        if (null != modelId) {
            path += "/model/" + modelId;
        }
        path += "?status=" + ((online) ? IContentService.STATUS_ONLINE : IContentService.STATUS_DRAFT);
        if (null != langCode) {
            path += "&lang=" + langCode;
        }
        path += "&resolveLink=" + (resolveLink ? "true" : "false");
        if (null == user) {
            return mockMvc.perform(get(path, code));
        }
        String accessToken = mockOAuthInterceptor(user);
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

    private ResultActions executeContentPut(String fileName, String valueToReplace, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPutValid = FileTextReader.getText(isJsonPostValid);
        jsonPutValid = jsonPutValid.replace("**MARKER**", valueToReplace);
        ResultActions result = mockMvc
                .perform(put("/plugins/cms/contents")
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
    public void testGetContentsPaginated() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents?page=1&pageSize=2")
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filter[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filter[0].operator", "eq")
                        .param("filter[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)))
                .andExpect(jsonPath("$.metaData.page", is(1)))
                .andExpect(jsonPath("$.metaData.pageSize", is(2)))
                .andExpect(jsonPath("$.metaData.totalItems", is(11)))
                .andExpect(jsonPath("$.metaData.lastPage", is(6)));
    }

    @Test
    public void testGetContentsByGuestUser() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filter[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filter[0].operator", "eq")
                        .param("filter[0].value", "EVN"));
        result.andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(Matchers.greaterThan(0))));
    }

    @Test
    public void testGetReturnsList() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        result.andExpect(jsonPath("$.metaData.pageSize").value("100"));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8));
        String bodyResult2 = result.andReturn().getResponse().getContentAsString();
        int payloadSize2 = JsonPath.read(bodyResult2, "$.payload.size()");
        Assert.assertEquals(payloadSize2, payloadSize);
    }

    @Test
    public void testLoadPublicEvents_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("pageSize", "20")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        List<String> expectedFreeContentsId = Arrays.asList("EVN194", "EVN193",
                "EVN24", "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191");
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedFreeContentsId.size(), payloadSize);
        for (int i = 0; i < expectedFreeContentsId.size(); i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertTrue(expectedFreeContentsId.contains(extractedId));
        }

        user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization("coach", "tempRole", Permission.BACKOFFICE).build();
        accessToken = mockOAuthInterceptor(user);
        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("pageSize", "20")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        List<String> newExpectedFreeContentsId = new ArrayList<>(expectedFreeContentsId);
        newExpectedFreeContentsId.add("EVN103");
        newExpectedFreeContentsId.add("EVN41");
        Assert.assertEquals(newExpectedFreeContentsId.size(), payloadSize);
        for (int i = 0; i < payloadSize; i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertTrue(newExpectedFreeContentsId.contains(extractedId));
        }
    }

    @Test
    public void testLoadPublicEvents_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_DESCR_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].entityAttr", "DataInizio")
                        .param("filters[0].operator", "gt")
                        .param("filters[0].type", "date")
                        .param("filters[0].value", "1997-06-10 01:00:00")
                        .param("filters[1].entityAttr", "DataInizio")
                        .param("filters[1].operator", "lt")
                        .param("filters[1].type", "date")
                        .param("filters[1].value", "2020-09-19 01:00:00")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        String[] expected = {"EVN25", "EVN21", "EVN20", "EVN41", "EVN193",
            "EVN192", "EVN103", "EVN23", "EVN24"};
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expected.length, payloadSize);
        for (int i = 0; i < expected.length; i++) {
            String expectedId = expected[i];
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedId, extractedId);
        }
    }

    @Test
    public void testLoadPublicEvents_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization("coach", "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.CONTENT_DESCR_FILTER_KEY)
                        .param("filters[0].operator", "like")
                        .param("filters[0].value", "Even")
                        .param("filters[0].order", FieldSearchFilter.DESC_ORDER)
                        .param("filters[1].entityAttr", "DataInizio")
                        .param("filters[1].operator", "gt")
                        .param("filters[1].type", "date")
                        .param("filters[1].value", "1997-06-10 01:00:00")
                        .param("filters[2].entityAttr", "DataInizio")
                        .param("filters[2].operator", "lt")
                        .param("filters[2].type", "date")
                        .param("filters[2].value", "2020-09-19 01:00:00")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        String[] expected = {"EVN193", "EVN192"};
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expected.length, payloadSize);
        for (int i = 0; i < expected.length; i++) {
            String expectedId = expected[i];
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedId, extractedId);
        }
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
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
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

    @Test
    public void testLoadOrderedPublicEvents_2() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN"));
        result.andExpect(status().isOk());
        String[] expectedFreeOrderedContentsId_1 = {"EVN191", "EVN192",
            "EVN193", "EVN194", "EVN20", "EVN23", "EVN24", "EVN25", "EVN21"};
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(expectedFreeOrderedContentsId_1.length)));
        for (int i = 0; i < expectedFreeOrderedContentsId_1.length; i++) {
            String expectedId = expectedFreeOrderedContentsId_1[expectedFreeOrderedContentsId_1.length - i - 1];
            result.andExpect(jsonPath("$.payload[" + i + "].id", is(expectedId)));
        }

        Thread thread = this.searchEngineManager.startReloadContentsReferences();
        thread.join();

        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("langCode", "it")
                        .param("text", "Titolo Evento 4")
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN"));
        result.andExpect(status().isOk());
        String[] expectedFreeOrderedContentsId_2 = {"EVN191", "EVN192", "EVN193", "EVN194", "EVN24"};
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(expectedFreeOrderedContentsId_2.length)));
        for (int i = 0; i < expectedFreeOrderedContentsId_2.length; i++) {
            String expectedId = expectedFreeOrderedContentsId_2[i];
            result.andExpect(jsonPath("$.payload[" + i + "].id", is(expectedId)));
        }
    }

    @Test
    public void testLoadOrderedPublicEvents_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].entityAttr", "DataInizio")
                        .param("filters[0].order", "DESC")
                        .param("filters[1].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[1].operator", "eq")
                        .param("filters[1].value", "EVN")
                        .param("pageSize", "5")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        String[] expectedFreeOrderedContentsId_1 = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN25"};
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedFreeOrderedContentsId_1.length, payloadSize);
        for (int i = 0; i < expectedFreeOrderedContentsId_1.length; i++) {
            String expectedId = expectedFreeOrderedContentsId_1[i];
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedId, extractedId);
        }

        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("direction", FieldSearchFilter.DESC_ORDER) //ignored
                        .param("filters[0].entityAttr", "DataInizio")
                        .param("filters[0].order", FieldSearchFilter.ASC_ORDER)
                        .param("filters[1].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[1].operator", "eq")
                        .param("filters[1].value", "EVN")
                        .param("pageSize", "6")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        String[] expectedFreeOrderedContentsId_2 = {"EVN191", "EVN192", "EVN21", "EVN20", "EVN25", "EVN23"};
        int payloadSize_2 = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedFreeOrderedContentsId_2.length, payloadSize_2);
        for (int i = 0; i < expectedFreeOrderedContentsId_2.length; i++) {
            String expectedId = expectedFreeOrderedContentsId_2[i];
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedId, extractedId);
        }
    }

    @Test
    public void testLoadOrderedPublicEvents_4() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        Content masterContent = this.contentManager.loadContent("EVN193", true, false);
        masterContent.setId(null);
        masterContent.setDescription("Cloned content for test");
        DateAttribute dateAttribute = (DateAttribute) masterContent.getAttribute("DataInizio");
        dateAttribute.setDate(DateConverter.parseDate("17/06/2019", "dd/MM/yyyy"));
        try {
            this.contentManager.saveContent(masterContent);
            this.contentManager.insertOnLineContent(masterContent);
            ResultActions result = mockMvc
                    .perform(get("/plugins/cms/contents")
                            .param("status", IContentService.STATUS_ONLINE)
                            .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                            .param("filters[0].operator", "eq")
                            .param("filters[0].value", "EVN")
                            .param("filters[1].entityAttr", "DataInizio")
                            .param("filters[1].order", FieldSearchFilter.DESC_ORDER)
                            .sessionAttr("user", user)
                            .header("Authorization", "Bearer " + accessToken));
            String bodyResult = result.andReturn().getResponse().getContentAsString();
            result.andExpect(status().isOk());
            String[] expectedFreeOrderedContentsId = {"EVN194", masterContent.getId(),
                "EVN193", "EVN24", "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
            int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
            Assert.assertEquals(expectedFreeOrderedContentsId.length, payloadSize);
            for (int i = 0; i < expectedFreeOrderedContentsId.length; i++) {
                String expectedId = expectedFreeOrderedContentsId[i];
                String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
                Assert.assertEquals(expectedId, extractedId);
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null != masterContent.getId() && !"EVN193".equals(masterContent.getId())) {
                this.contentManager.removeOnLineContent(masterContent);
                this.contentManager.deleteContent(masterContent);
            }
        }
    }

    @Test
    public void testLoadPublicContentsWithHtml() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "ART"));
        result.andExpect(status().isOk());
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        List<String> expectedFreeContentsId = Arrays.asList("ART1", "ART180", "ART187", "ART121");
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(expectedFreeContentsId.size())));
        for (int i = 0; i < expectedFreeContentsId.size(); i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedFreeContentsId.get(i), extractedId);
            Assert.assertNull(JsonPath.read(bodyResult, "$.payload[" + i + "].html"));
        }

        Map<String, String> extractedHtml = new HashMap<>();
        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("model", "list")
                        .param("lang", "it")
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "ART"));
        bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(expectedFreeContentsId.size())));
        for (int i = 0; i < expectedFreeContentsId.size(); i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedFreeContentsId.get(i), extractedId);
            String html = JsonPath.read(bodyResult, "$.payload[" + i + "].html");
            Assert.assertNotNull(html);
            extractedHtml.put(extractedId, html);
        }
        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("model", "list")
                        .param("lang", "en")
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "ART"));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(expectedFreeContentsId.size())));
        bodyResult = result.andReturn().getResponse().getContentAsString();
        for (int i = 0; i < expectedFreeContentsId.size(); i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedFreeContentsId.get(i), extractedId);
            String html = JsonPath.read(bodyResult, "$.payload[" + i + "].html");
            Assert.assertNotNull(html);
            Assert.assertNotNull(extractedHtml.get(extractedId));
            Assert.assertFalse(html.equals(extractedHtml.get(extractedId)));
        }
    }

    @Test
    public void testLoadPublicContentsForCategory_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("categories[0]", "evento")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        List<String> expectedFreeContentsId = Arrays.asList("EVN192", "EVN193");
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedFreeContentsId.size(), payloadSize);
        for (int i = 0; i < expectedFreeContentsId.size(); i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertTrue(expectedFreeContentsId.contains(extractedId));
        }

        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("categories[0]", "evento")
                        .param("filters[0].entityAttr", "DataInizio")
                        .param("filters[0].operator", "lt")
                        .param("filters[0].type", "date")
                        .param("filters[0].value", "2005-02-13 01:00:00")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        int newPayloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(1, newPayloadSize);
        String extractedId = JsonPath.read(bodyResult, "$.payload[0].id");
        Assert.assertEquals("EVN192", extractedId);
    }

    @Test
    public void testLoadPublicEventsForCategory_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("categories[0]", "general_cat3")
                        .param("categories[1]", "general_cat2")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(1, payloadSize);
        String singleId = JsonPath.read(bodyResult, "$.payload[0].id");
        Assert.assertEquals("ART120", singleId);

        result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("categories[0]", "general_cat3")
                        .param("categories[1]", "general_cat2")
                        .param("orClauseCategoryFilter", "true")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        List<String> expectedFreeContentsId = Arrays.asList("ART111", "ART120", "ART122");
        int newPayloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedFreeContentsId.size(), newPayloadSize);
        for (int i = 0; i < expectedFreeContentsId.size(); i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertTrue(expectedFreeContentsId.contains(extractedId));
        }
    }

    @Test
    public void testLoadWorkContentsByAttribute() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_DRAFT)
                        .param("filters[0].attribute", IContentManager.ENTITY_ID_FILTER_KEY)
                        .param("filters[0].order", EntitySearchFilter.ASC_ORDER)
                        .param("filters[1].entityAttr", "Numero")
                        .param("filters[1].type", "number")
                        .param("filters[1].order", FieldSearchFilter.ASC_ORDER)
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        String[] expectedContentsId = {"ART120", "ART121"};
        int payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        Assert.assertEquals(expectedContentsId.length, payloadSize);
        for (int i = 0; i < expectedContentsId.length; i++) {
            String extractedId = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            Assert.assertEquals(expectedContentsId[i], extractedId);
        }
    }

}
