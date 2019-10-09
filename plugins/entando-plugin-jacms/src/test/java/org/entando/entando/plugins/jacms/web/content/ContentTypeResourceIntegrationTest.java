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

import static org.hamcrest.CoreMatchers.is;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ContentTypeResourceIntegrationTest extends AbstractControllerIntegrationTest {

    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private String accessToken;

    @Autowired
    private IContentManager contentManager;

    @Before
    public void setupTest() {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        accessToken = mockOAuthInterceptor(user);
    }

    @Test
    public void testGetReturnsList() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/contentTypes")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.metaData.pageSize").value("100"))
                .andReturn();

        mockMvc.perform(
                get("/plugins/cms/contentTypes")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateUnauthorizedContentType() throws Exception {
        String typeCode = "TX0";
        Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        Content content = new Content();
        content.setTypeCode(typeCode);
        content.setDescription("My content type " + typeCode);
        content.setDefaultModel("My Model");
        content.setListModel("Model list");
        ContentTypeDtoRequest contentTypeRequest = new ContentTypeDtoRequest(content);
        contentTypeRequest.setName("Content request");
        mockMvc.perform(
                post("/plugins/cms/contentTypes")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(contentTypeRequest))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
        Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
    }

    @Test
    public void testCreateContentType() throws Exception {
        String typeCode = "TX1";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
            Content content = new Content();
            content.setTypeCode(typeCode);
            content.setDescription("My content type " + typeCode);
            content.setDefaultModel("My Model");
            content.setListModel("Model list");
            ContentTypeDtoRequest contentTypeRequest = new ContentTypeDtoRequest(content);
            contentTypeRequest.setName("Content request");
            mockMvc.perform(
                    post("/plugins/cms/contentTypes")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonMapper.writeValueAsString(contentTypeRequest))
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.payload.code").value(typeCode))
                    .andReturn();
            Assert.assertNotNull(this.contentManager.getEntityPrototype(typeCode));
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testUpdateContentType() throws Exception {
        String typeCode = "TX2";
        try {
            ContentTypeDto createdContentType = createContentType(typeCode);
            createdContentType.setName("MyContentType");
            mockMvc.perform(put("/plugins/cms/contentTypes")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonMapper.writeValueAsString(createdContentType))
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    //                .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code").value(createdContentType.getCode()))
                    .andExpect(jsonPath("$.payload.name").value("MyContentType"))
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testDeleteContentType() throws Exception {
        String typeCode = "TX3";
        try {
            this.createContentType(typeCode);
            mockMvc.perform(
                    delete("/plugins/cms/contentTypes/{code}", typeCode)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andReturn();
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        } catch (Exception e) {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
            throw e;
        }
    }

    @Test
    public void testGetContentType() throws Exception {
        this.executeGetContentType("ART", status().isOk());
        this.executeGetContentType("XXX", status().isNotFound());
    }

    private void executeGetContentType(String typeCode, ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(
                get("/plugins/cms/contentTypes/{code}", typeCode)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(expectedResult)
                .andReturn();
        mockMvc.perform(
                get("/plugins/cms/contentTypes/{code}", typeCode)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(expectedResult)
                .andReturn();
    }

    @Test
    public void testCreateAndGetContentType() throws Exception {
        String typeCode = "TX4";
        mockMvc.perform(
                get("/plugins/cms/contentTypes/{code}", typeCode)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andReturn();
        try {
            ContentTypeDto createdContentTypeDto = this.createContentType(typeCode);
            MvcResult mvcResult = mockMvc.perform(
                    get("/plugins/cms/contentTypes/{code}", createdContentTypeDto.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            ContentTypeDto contentTypeDto = stringToContentTypeDto(mvcResult);
            assertThat(contentTypeDto).isEqualToComparingFieldByField(createdContentTypeDto);
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testGetAllAttributes() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/contentTypeAttributes")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                //                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath(
                        "$.payload",
                        containsInAnyOrder(
                                "Attach", "Boolean", "CheckBox", "Composite", "Date", "Enumerator",
                                "EnumeratorMap", "Hypertext", "Image", "Link", "List", "Longtext",
                                "Monolist", "Monotext", "Number", "Text", "ThreeState", "Timestamp")))
                .andReturn();
    }

    @Test
    public void testGetAttribute() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/contentTypeAttributes/{code}", "Text")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                //                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.payload.code").value("Text"))
                .andReturn();
    }

    @Test
    public void testCreateContentTypeAttribute() throws Exception {
        String typeCode = "TX5";
        try {
            ContentTypeDto contentType = this.createContentType(typeCode);
            EntityTypeAttributeFullDto attribute = new EntityTypeAttributeFullDto();
            attribute.setCode("MyAttribute");
            attribute.setType("Text");
            attribute.setName("My test attribute");
            mockMvc.perform(
                    post("/plugins/cms/contentTypes/{code}/attributes", contentType.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonMapper.writeValueAsString(attribute))
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    //                .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.metaData.contentTypeCode").value(contentType.getCode()))
                    .andExpect(jsonPath("$.payload.code").value(attribute.getCode()))
                    .andExpect(jsonPath("$.payload.type").value(attribute.getType()))
                    .andExpect(jsonPath("$.payload.name").value(attribute.getName()))
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testGetAttributeFromContentType() throws Exception {
        String typeCode = "TX5";
        try {
            EntityTypeAttributeFullDto contentTypeAttribute = this.createContentTypeAttribute(typeCode);
            mockMvc.perform(
                    get("/plugins/cms/contentTypes/{contentCode}/attributes/{code}",
                            typeCode, contentTypeAttribute.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    //                .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.payload.code").value("MyAttribute"))
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testUpdateAttributeFromContentType() throws Exception {
        String typeCode = "TX6";
        try {
            EntityTypeAttributeFullDto contentTypeAttribute = createContentTypeAttribute(typeCode);
            contentTypeAttribute.setName("My New Name");
            mockMvc.perform(
                    put("/plugins/cms/contentTypes/{contentCode}/attributes/{code}",
                            typeCode, contentTypeAttribute.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonMapper.writeValueAsString(contentTypeAttribute))
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    //                .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.payload.name").value("My New Name"))
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testDeleteContentTypeAttribute() throws Exception {
        String typeCode = "TX7";
        EntityTypeAttributeFullDto attribute = createContentTypeAttribute(typeCode);
        try {
            mockMvc.perform(
                    get("/plugins/cms/contentTypes/{contentCode}/attributes/{code}",
                            typeCode, attribute.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andReturn();
            mockMvc.perform(
                    delete("/plugins/cms/contentTypes/{contentCode}/attributes/{code}",
                            typeCode, attribute.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    //                .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
            mockMvc.perform(
                    get("/plugins/cms/contentTypes/{contentCode}/attributes/{code}",
                            typeCode, attribute.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound())
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testRefreshContentType() throws Exception {
        String typeCode = "TX8";
        try {
            ContentTypeDto contentType = this.createContentType(typeCode);
            mockMvc.perform(
                    post("/plugins/cms/contentTypes/refresh/{contentTypeCode}", contentType.getCode())
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void testReloadReferences() throws Exception {
        ContentTypeRefreshRequest bodyRequest = new ContentTypeRefreshRequest();
        mockMvc.perform(
                post("/plugins/cms/contentTypesStatus")
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonMapper.writeValueAsString(bodyRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testExtractStatus() throws Exception {
        mockMvc.perform(
                get("/plugins/cms/contentTypesStatus")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void moveAttributeUp() throws Exception {
        String typeCode = "TX9";
        try {
            List<EntityTypeAttributeFullDto> attributes = createContentTypeAttributes(typeCode);
            String code = attributes.get(1).getCode();
            mockMvc.perform(
                    put("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}/moveUp", typeCode, code)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.attributeCode").value(code))
                    .andExpect(jsonPath("$.payload.movement").value("UP"))
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    @Test
    public void moveAttributeDown() throws Exception {
        String typeCode = "TXA";
        try {
            List<EntityTypeAttributeFullDto> attributes = createContentTypeAttributes(typeCode);
            String code = attributes.get(0).getCode();
            mockMvc.perform(
                    put("/plugins/cms/contentTypes/{contentTypeCode}/attributes/{attributeCode}/moveDown", typeCode, code)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.attributeCode").value(code))
                    .andExpect(jsonPath("$.payload.movement").value("DOWN"))
                    .andReturn();
        } finally {
            if (null != this.contentManager.getEntityPrototype(typeCode)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(typeCode);
            }
            Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        }
    }

    private ContentTypeDto createContentType(String typeCode) throws Exception {
        Assert.assertNull(this.contentManager.getEntityPrototype(typeCode));
        Content content = new Content();
        content.setTypeCode(typeCode);
        content.setDescription("My content type " + typeCode);
        content.setDefaultModel("My Model");
        content.setListModel("Model list");
        ContentTypeDtoRequest contentTypeRequest = new ContentTypeDtoRequest(content);
        contentTypeRequest.setName("Content request");
        MvcResult mvcResult = mockMvc.perform(
                post("/plugins/cms/contentTypes")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(contentTypeRequest))
                .accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        Assert.assertNotNull(this.contentManager.getEntityPrototype(typeCode));
        return stringToContentTypeDto(mvcResult);
    }

    private EntityTypeAttributeFullDto createContentTypeAttribute(String typeCode) throws Exception {
        ContentTypeDto contentType = createContentType(typeCode);
        return postForAttribute(contentType, "MyAttribute");
    }

    private List<EntityTypeAttributeFullDto> createContentTypeAttributes(String typeCode) throws Exception {
        ContentTypeDto contentType = createContentType(typeCode);
        return ImmutableList.of(
                postForAttribute(contentType, "MyAttribute1"),
                postForAttribute(contentType, "MyAttribute2")
        );
    }

    private EntityTypeAttributeFullDto postForAttribute(ContentTypeDto contentType, String code) throws Exception {
        EntityTypeAttributeFullDto attribute = new EntityTypeAttributeFullDto();
        attribute.setCode(code);
        attribute.setType("Text");
        MvcResult mvcResult = mockMvc.perform(
                post("/plugins/cms/contentTypes/{contentCode}/attributes", contentType.getCode())
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonMapper.writeValueAsString(attribute))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        return stringToEntityTypeDto(mvcResult);
    }

    private ContentTypeDto stringToContentTypeDto(MvcResult mvcResult) throws IOException {
        RestResponse<Map<String, String>, Map> restResponse
                = jsonMapper.readerFor(RestResponse.class)
                .readValue(mvcResult.getResponse().getContentAsString());
        Map<String, String> payload = restResponse.getPayload();
        return new ContentTypeDto()
                .name(payload.get("name"))
                .code(payload.get("code"));
    }

    private EntityTypeAttributeFullDto stringToEntityTypeDto(MvcResult mvcResult) throws IOException {
        RestResponse<Map<String, String>, Map> restResponse
                = jsonMapper.readerFor(RestResponse.class)
                .readValue(mvcResult.getResponse().getContentAsString());
        Map<String, String> payload = restResponse.getPayload();
        EntityTypeAttributeFullDto result = new EntityTypeAttributeFullDto();
        result.setCode(payload.get("code"));
        result.setType(payload.get("type"));
        return result;
    }

    private void deleteContentType(String code) throws Exception {
        mockMvc.perform(
                delete("/plugins/cms/contentTypes/{code}", code)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)))
                .andReturn();
    }

}
