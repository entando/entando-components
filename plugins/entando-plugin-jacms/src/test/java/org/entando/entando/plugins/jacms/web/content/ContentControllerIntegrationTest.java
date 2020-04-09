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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageTestUtil;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
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
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.IContentService;
import org.entando.entando.plugins.jacms.web.content.validator.BatchContentStatusRequest;
import org.entando.entando.plugins.jacms.web.content.validator.ContentStatusRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ContentControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IContentManager contentManager;

    @Autowired
    private ICmsSearchEngineManager searchEngineManager;

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();

    public static final String PLACEHOLDER_STRING = "resourceIdPlaceHolder";
    
    @Test
    public void testGetContentWithModel() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        ResultActions result = this.performGetContent("ART180", "1", true, null, true, user);
        String result1 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1);
        result.andExpect(status().isOk());
        String html1 = JsonPath.read(result1, "$.payload.html");
        Assert.assertTrue(!StringUtils.isBlank(html1));
        
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        result = this.performGetContent("ART180", "11", true, null, true, user);
        String result2 = result.andReturn().getResponse().getContentAsString();
        String html2 = JsonPath.read(result2, "$.payload.html");
        Assert.assertTrue(!StringUtils.isBlank(html2));
        System.out.println(result2);

        result.andExpect(status().isOk());
        result = this.performGetContent("ART180", "default", true, null, true, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result1_copy = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1_copy);
        result.andExpect(status().isOk());
        String htmlCopy = JsonPath.read(result1_copy, "$.payload.html");
        Assert.assertTrue(!StringUtils.isBlank(htmlCopy));
        Assert.assertEquals(html1, htmlCopy);
        
        result = this.performGetContent("ART180", "list", true, null, true, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result2_copy = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2);
        String html2Copy = JsonPath.read(result2_copy, "$.payload.html");
        Assert.assertTrue(!StringUtils.isBlank(html2Copy));
        result.andExpect(status().isOk());
        Assert.assertTrue(!StringUtils.isBlank(html2Copy));
        Assert.assertEquals(html2, html2Copy);

        result = this.performGetContent("ART180", "list", true, "en", true, user);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.payload.html", Matchers.anything()));
        String result2_copy_en = result.andReturn().getResponse().getContentAsString();
        System.out.println(result2_copy_en);
        String html2Copy_en = JsonPath.read(result2_copy_en, "$.payload.html");
        result.andExpect(status().isOk());
        Assert.assertNotEquals(html2Copy_en, html2Copy);
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
            result2.andExpect(jsonPath("$.payload[0].mainGroup", is("free")));
            result2.andExpect(jsonPath("$.payload[0].restriction", is("OPEN")));
            result2.andExpect(jsonPath("$.errors.size()", is(0)));
            result2.andExpect(jsonPath("$.metaData.size()", is(0)));

            String bodyResult = result2.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);
            Map<String, ResourceInterface> attResources = (Map<String,ResourceInterface>) newContent.getAttributeList().get(10).getValue();
            Map<String, ResourceInterface> imgResources = (Map<String,ResourceInterface>) newContent.getAttributeList().get(11).getValue();

            Assert.assertEquals(attResources.get("en").getId(), "6");
            Assert.assertEquals(attResources.get("it").getId(), "7");

            Assert.assertEquals(imgResources.get("en").getId(), "44");
            Assert.assertEquals(imgResources.get("it").getId(), "22");

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
            result4.andExpect(jsonPath("$.payload[0].mainGroup", is("free")));
            result4.andExpect(jsonPath("$.payload[0].restriction", is("OPEN")));
            newContent = this.contentManager.loadContent(newContentId, false);
            date = (Date) newContent.getAttribute("Date").getValue();
            Assert.assertEquals("2018-03-21", DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
            booleanValue = (Boolean) newContent.getAttribute("Boolean").getValue();
            Assert.assertFalse(booleanValue);
            threeState = (Boolean) newContent.getAttribute("ThreeState").getValue();
            Assert.assertNotNull(threeState);
            Assert.assertTrue(threeState);

            attResources = (Map<String,ResourceInterface>) newContent.getAttributeList().get(10).getValue();
            imgResources = (Map<String,ResourceInterface>) newContent.getAttributeList().get(11).getValue();

            Assert.assertEquals(attResources.get("en").getId(), "7");
            Assert.assertEquals(attResources.get("it").getId(), "6");

            Assert.assertEquals(imgResources.get("en").getId(), "22");
            Assert.assertEquals(imgResources.get("it").getId(), "44");

            ListAttribute list = (ListAttribute) newContent.getAttribute("multilist");
            Assert.assertEquals(4, list.getAttributeList("en").size());

            ResultActions result5 = this
                    .executeContentPut("1_PUT_maingroup.json", newContentId, accessToken, status().isOk());
            result5.andExpect(jsonPath("$.payload[0].mainGroup", is("group1")))
                    .andExpect(jsonPath("$.payload[0].restriction", is("RESTRICTED")));

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
    public void testAddContentWithLinkAttribute() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LNK"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_links.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LNK"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_links.json", accessToken, status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.errors.size()", is(0)))
                .andExpect(jsonPath("$.metaData.size()", is(0)))

                .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(SymbolicLink.URL_TYPE)))
                .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", is("https://myurl.com")))
                .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!U;https://myurl.com!#")))
                .andExpect(jsonPath("$.payload[0].attributes[0].values.it", is("My URL Link")))

                .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                .andExpect(jsonPath("$.payload[0].attributes[1].value.destType", is(SymbolicLink.PAGE_TYPE)))
                .andExpect(jsonPath("$.payload[0].attributes[1].value.pageDest", is("pagina_11")))
                .andExpect(jsonPath("$.payload[0].attributes[1].value.symbolicDestination", is("#!P;pagina_11!#")))
                .andExpect(jsonPath("$.payload[0].attributes[1].values.it", is("My Page Link")))

                .andExpect(jsonPath("$.payload[0].attributes[2].code", is("link3")))
                .andExpect(jsonPath("$.payload[0].attributes[2].value.destType", is(SymbolicLink.CONTENT_TYPE)))
                .andExpect(jsonPath("$.payload[0].attributes[2].value.contentDest", is("ART1")))
                .andExpect(jsonPath("$.payload[0].attributes[2].value.symbolicDestination", is("#!C;ART1!#")))
                .andExpect(jsonPath("$.payload[0].attributes[2].values.it", is("My Content Link")))

                .andExpect(jsonPath("$.payload[0].attributes[3].code", is("link4")))
                .andExpect(jsonPath("$.payload[0].attributes[3].value.destType", is(SymbolicLink.CONTENT_ON_PAGE_TYPE)))
                .andExpect(jsonPath("$.payload[0].attributes[3].value.pageDest", is("pagina_11")))
                .andExpect(jsonPath("$.payload[0].attributes[3].value.contentDest", is("ART1")))
                .andExpect(jsonPath("$.payload[0].attributes[3].value.symbolicDestination", is("#!O;ART1;pagina_11!#")))
                .andExpect(jsonPath("$.payload[0].attributes[3].values.it", is("My Page with Content Link")))

                .andExpect(jsonPath("$.payload[0].attributes[4].code", is("link5")))
                .andExpect(jsonPath("$.payload[0].attributes[4].value.destType", is(SymbolicLink.RESOURCE_TYPE)))
                .andExpect(jsonPath("$.payload[0].attributes[4].value.resourceDest", is("44")))
                .andExpect(jsonPath("$.payload[0].attributes[4].value.symbolicDestination", is("#!R;44!#")))
                .andExpect(jsonPath("$.payload[0].attributes[4].values.it", is("My Resource Link")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LNK")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LNK");
            }
        }
    }

    @Test
    public void testAddUpdateContentWithLinkAttribute() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_link.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_link.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(SymbolicLink.PAGE_TYPE)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", is("pagina_11")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!P;pagina_11!#")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it", is("pagina_11")));

            this.executeContentPut("1_PUT_invalid_with_link.json", newContentId, accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The destination page must be published")));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
        }
    }

    @Test
    public void testAddUpdateContentWithLinkAttributeThenRemoveIt() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_link.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_link.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(SymbolicLink.PAGE_TYPE)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", is("pagina_11")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!P;pagina_11!#")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it", is("pagina_11")));

            this.executeContentPut("1_PUT_valid_without_link.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
        }
    }

    @Test
    public void testAddUpdateContentWithLinksAttributeThenRemoveIt() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LNK"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_links.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LNK"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_some_links.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", is("https://myurl.com")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value.pageDest", is("pagina_11")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].code", is("link3")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[3].code", is("link4")))
                    .andExpect(jsonPath("$.payload[0].attributes[3].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[4].code", is("link5")))
                    .andExpect(jsonPath("$.payload[0].attributes[4].value", Matchers.isEmptyOrNullString()));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_some_more_links.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[2].code", is("link3")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].value.contentDest", is("ART1")))
                    .andExpect(jsonPath("$.payload[0].attributes[3].code", is("link4")))
                    .andExpect(jsonPath("$.payload[0].attributes[3].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[4].code", is("link5")))
                    .andExpect(jsonPath("$.payload[0].attributes[4].value.resourceDest", is("44")));

            this.executeContentPut("1_PUT_valid_without_links.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[2].code", is("link3")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[3].code", is("link4")))
                    .andExpect(jsonPath("$.payload[0].attributes[3].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[4].code", is("link5")))
                    .andExpect(jsonPath("$.payload[0].attributes[4].value", Matchers.isEmptyOrNullString()));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LNK")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LNK");
            }
        }
    }

    @Test
    public void testAddContentWithLinksAndRemoveMandatoryLink() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LNK"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_mandatory_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LNK"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_some_links.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", is("https://myurl.com")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value.pageDest", is("pagina_11")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_mandatory_link.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value.pageDest", is("pagina_11")));

            this.executeContentPut("1_PUT_invalid_with_mandatory_link.json", newContentId, accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link2' Mandatory: null")));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LNK")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LNK");
            }
        }
    }

    @Test
    public void testAddUpdateContentWithBooleanAttributeThenEditIt() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("BOL"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_boolean.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("BOL"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_boolean_null.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("bool1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(false)));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_boolean_true.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("bool1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(true)));

            this.executeContentPut("1_PUT_valid_with_boolean_null.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("bool1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(false)));

            this.executeContentPut("1_PUT_valid_with_boolean_true.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("bool1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(true)));

            this.executeContentPut("1_PUT_valid_with_boolean_false.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("bool1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(false)));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("BOL")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("BOL");
            }
        }
    }

    @Test
    public void testAddUpdateContentWithCheckboxAttributeThenEditIt() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CHE"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_checkbox.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CHE"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_checkbox_null.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("check1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(false)));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_checkbox_true.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("check1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(true)));

            this.executeContentPut("1_PUT_valid_with_checkbox_null.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("check1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(false)));

            this.executeContentPut("1_PUT_valid_with_checkbox_true.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("check1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(true)));

            this.executeContentPut("1_PUT_valid_with_checkbox_false.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("check1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", is(false)));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("CHE")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CHE");
            }
        }
    }

    @Test
    public void testAddContentWithImageAttributeWithAllFields() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("IMG"));

            this.executeContentTypePost("1_POST_type_with_image.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("IMG"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_image_all_fields.json", accessToken, status().isOk(), resourceId)
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("img1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.name", is("text en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.metadata.legend", is("legend en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.metadata.alt", is("alt en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.name", is("text it1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.metadata.legend", is("legend it1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.metadata.alt", is("alt it1")))

                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("img2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.name", is("text en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.metadata.legend", is("legend en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.metadata.alt", is("alt en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.name", is("text it2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.metadata.legend", is("legend it2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.metadata.alt", is("alt it2")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "image", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("IMG")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("IMG");
            }
        }
    }

    @Test
    public void testAddContentWithImageAttributeWithoutMetadata() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("IMG"));

            this.executeContentTypePost("1_POST_type_with_image.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("IMG"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_image_no_metadata.json", accessToken, status().isOk(), resourceId)
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("img1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.name", is("text en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.name", is("text it1")))

                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("img2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.name", is("text en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.name", is("text it2")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "image", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("IMG")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("IMG");
            }
        }
    }

    @Test
    public void testAddContentWithImageAttributeWithoutName() throws Exception {
        String resourceId = null;
        String newContentId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("IMG"));

            this.executeContentTypePost("1_POST_type_with_image.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("IMG"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_image_no_name.json", accessToken, status().isOk(), resourceId)
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("img1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.metadata.legend", is("legend en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.metadata.alt", is("alt en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.metadata.legend", is("legend it1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.metadata.alt", is("alt it1")))

                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("img2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.metadata.legend", is("legend en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.metadata.alt", is("alt en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.metadata.legend", is("legend it2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.metadata.alt", is("alt it2")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "image", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("IMG")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("IMG");
            }
        }
    }

    @Test
    public void testAddContentWithAttachAttribute() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("ATT"));

            this.executeContentTypePost("1_POST_type_with_attach.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("ATT"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "file", "free", "application/pdf");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_attach.json", accessToken, status().isOk(), resourceId)
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("att1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.name", is("name att en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.name", is("name att it1")))

                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("att2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.name", is("name att en2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.name", is("name att it2")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "file", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("ATT")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("ATT");
            }
        }
    }

    @Test
    public void testAddContentWithAttachAndImageAttribute() throws Exception {
        String newContentId1 = null;
        String newContentId2 = null;
        String imageResourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("IAT"));

            this.executeContentTypePost("1_POST_type_with_image_and_attach.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("IAT"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            imageResourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this
                    .executeContentPost("1_POST_valid_with_attach_and_image.json", accessToken, status().isOk(), imageResourceId)
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("img1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.name", is("text img en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.metadata.legend", is("legend img en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.en.metadata.alt", is("alt img en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.name", is("text img it1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.metadata.legend", is("legend img it1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].values.it.metadata.alt", is("alt img it1")))

                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("att1")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.en.name", is("text att en1")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it.name", is("text att it1")))

                    .andExpect(jsonPath("$.payload[1].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[1].attributes[0].code", is("img1")))
                    .andExpect(jsonPath("$.payload[1].attributes[0].values.en.name", is("text img en2")))
                    .andExpect(jsonPath("$.payload[1].attributes[0].values.it.name", is("text img it2")))
                    .andExpect(jsonPath("$.payload[1].attributes[0].values.it.metadata.legend", is("legend img it2")))
                    .andExpect(jsonPath("$.payload[1].attributes[0].values.it.metadata.alt", is("alt img it2")))

                    .andExpect(jsonPath("$.payload[1].attributes[1].code", is("att1")))
                    .andExpect(jsonPath("$.payload[1].attributes[1].values.en.name", is("text att en2")))
                    .andExpect(jsonPath("$.payload[1].attributes[1].values.it.name", is("text att it2")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();

            newContentId1 = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent1 = this.contentManager.loadContent(newContentId1, false);
            Assert.assertNotNull(newContent1);

            newContentId2 = JsonPath.read(bodyResult, "$.payload[1].id");
            Content newContent2 = this.contentManager.loadContent(newContentId2, false);
            Assert.assertNotNull(newContent2);


        } finally {
            if (null != imageResourceId) {
                performDeleteResource(accessToken, "image", imageResourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId1) {
                Content newContent = this.contentManager.loadContent(newContentId1, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != newContentId2) {
                Content newContent = this.contentManager.loadContent(newContentId2, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("IAT")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("IAT");
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithListAttributeImage() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LST"));

            this.executeContentTypePost("1_POST_type_with_list_image.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LST"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_list_image.json", accessToken, status().isOk(), resourceId);
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("ListOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en.size()", is(2)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].code", is("ListOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.type", is("image")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.description", is("image_test.jpeg")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].code", is("ListOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].values.it.type", is("image")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].values.it.description", is("image_test.jpeg")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_list_image.json", newContentId, accessToken, status().isOk(), resourceId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("ListOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en.size()", is(1)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].code", is("ListOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.type", is("image")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.description", is("image_test.jpeg")));

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "image", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LST")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LST");
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithListAttributeFile() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LTF"));

            this.executeContentTypePost("1_POST_type_with_list_file.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LTF"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "file", "free", "application/pdf");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_list_file.json", accessToken, status().isOk(), resourceId);
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("ListOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en.size()", is(2)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].code", is("ListOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.type", is("file")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.description", is("file_test.jpeg")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].code", is("ListOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].values.it.type", is("file")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].values.it.description", is("file_test.jpeg")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_list_file.json", newContentId, accessToken, status().isOk(), resourceId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("ListOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en.size()", is(1)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].code", is("ListOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.type", is("file")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].values.it.description", is("file_test.jpeg")));

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "file", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LTF")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LTF");
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithListAttributeBoolean() throws Exception {
        String newContentId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LSB"));

            this.executeContentTypePost("1_POST_type_with_list_bool.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LSB"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_list_bool.json", accessToken, status().isOk());
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en.size()", is(4)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].value", is(false)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].value", is(true)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[2].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[2].value", is(true)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[3].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[3].value", is(false)));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_list_bool.json", newContentId, accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en.size()", is(5)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[0].value", is(false)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[1].value", is(true)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[2].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[2].value", is(false)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[3].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[3].value", is(true)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[4].code", is("ListOfBools")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].listelements.en[4].value", is(false)));

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LSB")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LSB");
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithMonolistAttributeImage() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("MON"));

            this.executeContentTypePost("1_POST_type_with_monolist_image.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("MON"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_monolist_image.json", accessToken, status().isOk(), resourceId);
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is("MON")))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type MON")))
                    .andExpect(jsonPath("$.payload[0].description", is("monolistofimages")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("MonolistOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].elements.size()", is(1)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].elements[0].code", is("MonolistOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].elements[0].values.it.type", is("image")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_monolist_image.json", newContentId, accessToken, status().isOk(), resourceId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is("MON")))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type MON")))
                    .andExpect(jsonPath("$.payload[0].description", is("ContentWithMonolistOfImage2")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("MonolistOfImag")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].elements.size()", is(1)));

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "image", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("MON")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("MON");
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithMonolistAttributeFile() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("MOF"));

            this.executeContentTypePost("1_POST_type_with_monolist_file.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("MOF"));

            ResultActions resourceResult = this.performCreateResource(accessToken, "file", "free", "application/pdf");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_monolist_file.json", accessToken, status().isOk(), resourceId);
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is("MOF")))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type MOF")))
                    .andExpect(jsonPath("$.payload[0].description", is("monolistoffile")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("MonolistOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].elements.size()", is(1)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].elements[0].code", is("MonolistOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].elements[0].values.it.type", is("file")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_monolist_file.json", newContentId, accessToken, status().isOk(), resourceId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is("MOF")))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type MOF")))
                    .andExpect(jsonPath("$.payload[0].description", is("ContentWithMonolistOfFile2")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("MonolistOfFile")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].elements.size()", is(1)));

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "file", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("MOF")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("MOF");
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithCompositeAttributeImage() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        String contentType = "COI";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype(contentType));

            this.executeContentTypePost("1_POST_type_with_composite_image.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype(contentType));

            ResultActions resourceResult = this.performCreateResource(accessToken, "image", "free", "application/jpeg");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_composite_image.json", accessToken, status().isOk(), resourceId);
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is(contentType)))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type COI")))
                    .andExpect(jsonPath("$.payload[0].description", is("compositeofimages")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("composite")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements.size()", is(1)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].code", is("image-compo")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].values.it.type", is("image")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_composite_image.json", newContentId, accessToken, status().isOk(), resourceId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is(contentType)))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type COI")))
                    .andExpect(jsonPath("$.payload[0].description", is("compositeofimages2")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].code", is("image-compo")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].values.it.type", is("image")));

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "image", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype(contentType)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(contentType);
            }
        }
    }

    @Test
    public void testAddAndUpdateContentWithCompositeAttributeFile() throws Exception {
        String newContentId = null;
        String resourceId = null;
        String accessToken = this.createAccessToken();
        String contentType = "COF";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype(contentType));

            this.executeContentTypePost("1_POST_type_with_composite_file.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype(contentType));

            ResultActions resourceResult = this.performCreateResource(accessToken, "file", "free", "application/pdf");

            resourceId = JsonPath.read(resourceResult.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result = this.executeContentPost("1_POST_valid_with_composite_file.json", accessToken, status().isOk(), resourceId);
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is(contentType)))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type COF")))
                    .andExpect(jsonPath("$.payload[0].description", is("compositeoffiles")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("composite")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements.size()", is(1)))

                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].code", is("file-compo")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].values.it.type", is("file")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);

            Assert.assertNotNull(newContent);

            this.executeContentPut("1_PUT_valid_with_composite_file.json", newContentId, accessToken, status().isOk(), resourceId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].typeCode", is(contentType)))
                    .andExpect(jsonPath("$.payload[0].typeDescription", is("Content Type COF")))
                    .andExpect(jsonPath("$.payload[0].description", is("compositeoffiles2")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].code", is("file-compo")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].values.it.type", is("file")));

        } finally {
            if (null != resourceId) {
                performDeleteResource(accessToken, "file", resourceId)
                        .andExpect(status().isOk());
            }
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype(contentType)) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype(contentType);
            }
        }
    }

    @Test
    public void testAddContentInvalidResourceGroup() throws Exception {
        try {
            Assert.assertNull(contentManager.getEntityPrototype("TST"));
            String accessToken = createAccessToken();

            executeContentTypePost("1_POST_type_valid.json", accessToken, status().isCreated());
            Assert.assertNotNull(contentManager.getEntityPrototype("TST"));

            executeContentPost("1_POST_invalid_resource.json", accessToken, status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors.size()", is(1)));
        } finally {
            if (null != contentManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) contentManager).removeEntityPrototype("TST");
            }
        }
    }

    @Test
    public void testAddContentResourceNotFound() throws Exception {
        try {
            Assert.assertNull(contentManager.getEntityPrototype("TST"));
            String accessToken = createAccessToken();

            executeContentTypePost("1_POST_type_valid.json", accessToken, status().isCreated());
            Assert.assertNotNull(contentManager.getEntityPrototype("TST"));

            executeContentPost("1_POST_resource_not_found.json", accessToken, status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.errors.size()", is(1)));
        } finally {
            if (null != contentManager.getEntityPrototype("TST")) {
                ((IEntityTypesConfigurer) contentManager).removeEntityPrototype("TST");
            }
        }
    }

    @Test
    public void testAddUpdateContentCategories() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("TST"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_valid.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("TST"));

            ResultActions result = executeContentPost("1_POST_valid.json", accessToken, status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                .andExpect(jsonPath("$.payload[0].firstEditor", is("jack_bauer")))
                .andExpect(jsonPath("$.payload[0].lastEditor", is("jack_bauer")))
                .andExpect(jsonPath("$.payload[0].version", is("0.1")))
                .andExpect(jsonPath("$.payload[0].attributes.size()", is(13)))
                .andExpect(jsonPath("$.errors.size()", is(0)))
                .andExpect(jsonPath("$.metaData.size()", is(0)));

            newContentId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload[0].id");

            result = executeContentPut("1_PUT_categories.json", newContentId, accessToken, status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.errors.size()", is(0)))
                .andExpect(jsonPath("$.metaData.size()", is(0)))
                .andExpect(jsonPath("$.payload[0].id", is(newContentId)))
                .andExpect(jsonPath("$.payload[0].attributes.size()", is(13)))
                .andExpect(jsonPath("$.payload[0].firstEditor", is("jack_bauer")))
                .andExpect(jsonPath("$.payload[0].lastEditor", is("jack_bauer")))
                .andExpect(jsonPath("$.payload[0].description", is("New Content for test")))
                .andExpect(jsonPath("$.payload[0].mainGroup", is("free")))
                .andExpect(jsonPath("$.payload[0].categories.size()", is(2)))
                .andExpect(jsonPath("$.payload[0].categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload[0].categories[1]", is("resCat2")))
                .andExpect(jsonPath("$.payload[0].version", is("0.2")));
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

    @Test
    public void testUpdateContents() throws Exception {
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

            BatchContentStatusRequest batchContentStatusRequest = new BatchContentStatusRequest();
            batchContentStatusRequest.setStatus("published");
            batchContentStatusRequest.getCodes().add(newContentId);

            result = mockMvc
                    .perform(put("/plugins/cms/contents/status")
                            .content(mapper.writeValueAsString(batchContentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());
            newPublicContent = this.contentManager.loadContent(newContentId, true);
            Assert.assertNotNull(newPublicContent);

            batchContentStatusRequest.setStatus("draft");
            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId)
                            .content(mapper.writeValueAsString(batchContentStatusRequest))
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

    @Test
    public void testUpdateContentsBatch() throws Exception {
        String newContentId1 = null;
        String newContentId2 = null;
        String newContentId3 = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("TST"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_valid.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("TST"));

            BatchContentStatusRequest batchContentStatusRequest = new BatchContentStatusRequest();
            batchContentStatusRequest.setStatus("published");

            ResultActions result = this.executeContentPost("1_POST_valid.json", accessToken, status().isOk());
            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId1 = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId1, false);
            Assert.assertNotNull(newContent);
            Content newPublicContent = this.contentManager.loadContent(newContentId1, true);
            Assert.assertNull(newPublicContent);

            batchContentStatusRequest.getCodes().add(newContentId1);

            result = this.executeContentPost("1_POST_valid.json", accessToken, status().isOk());
            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId2 = JsonPath.read(bodyResult, "$.payload[0].id");

            batchContentStatusRequest.getCodes().add(newContentId2);

            result = this.executeContentPost("1_POST_valid.json", accessToken, status().isOk());
            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId3 = JsonPath.read(bodyResult, "$.payload[0].id");

            batchContentStatusRequest.getCodes().add(newContentId3);

            batchContentStatusRequest.getCodes().stream().forEach(code -> {
                try {
                    Assert.assertNotNull(this.contentManager.loadContent(code, false));
                    Assert.assertNull(this.contentManager.loadContent(code, true));
                } catch (Exception e) {
                    Assert.fail();
                }
            });

            result = mockMvc
                    .perform(put("/plugins/cms/contents/status")
                            .content(mapper.writeValueAsString(batchContentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            batchContentStatusRequest.getCodes().stream().forEach(code -> {
                try {
                    Assert.assertNotNull(this.contentManager.loadContent(code, false));
                    Assert.assertNotNull(this.contentManager.loadContent(code, true));
                } catch (Exception e) {
                    Assert.fail();
                }
            });

            batchContentStatusRequest.setStatus("draft");

            result = mockMvc
                    .perform(put("/plugins/cms/contents/status")
                            .content(mapper.writeValueAsString(batchContentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            batchContentStatusRequest.getCodes().stream().forEach(code -> {
                try {
                    Assert.assertNotNull(this.contentManager.loadContent(code, false));
                    Assert.assertNull(this.contentManager.loadContent(code, true));
                } catch (Exception e) {
                    Assert.fail();
                }
            });

        } finally {
            if (null != newContentId1) {
                Content newContent = this.contentManager.loadContent(newContentId1, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != newContentId2) {
                Content newContent = this.contentManager.loadContent(newContentId2, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != newContentId3) {
                Content newContent = this.contentManager.loadContent(newContentId3, false);
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
        return executeContentPost(fileName, accessToken, expected, null);
    }

    private ResultActions executeContentPost(String fileName, String accessToken, ResultMatcher expected, String... replacements) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        if (replacements != null) {
            String placeholderString = PLACEHOLDER_STRING;
            for (int i = replacements.length-1; i >= 0; i--) {
                StringBuilder sb = new StringBuilder(PLACEHOLDER_STRING);
                if (i > 0) {
                    sb.append(i+1);
                }
                jsonPostValid = jsonPostValid.replace(sb.toString(), replacements[i]);
            }
        }
        ResultActions result = mockMvc
                .perform(post("/plugins/cms/contents")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print());
        result.andExpect(expected);
        return result;
    }

    private ResultActions executeContentPut(String fileName, String valueToReplace, String accessToken, ResultMatcher expected) throws Exception {
        return executeContentPut(fileName, valueToReplace, accessToken, expected, null);
    }

    private ResultActions executeContentPut(String fileName, String valueToReplace, String accessToken, ResultMatcher expected, String resourceId) throws Exception {
        InputStream isJsonPostValid = this.getClass().getResourceAsStream(fileName);
        String jsonPutValid = FileTextReader.getText(isJsonPostValid);
        jsonPutValid = jsonPutValid.replace("**MARKER**", valueToReplace);
        if (resourceId != null) {
            jsonPutValid = jsonPutValid.replace("resourceIdPlaceHolder", resourceId);
        }
        ResultActions result = mockMvc
                .perform(put("/plugins/cms/contents")
                        .content(jsonPutValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print()).andExpect(expected);
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

    private ResultActions performCreateResource(String accessToken, String type, String group, String mimeType) throws Exception {
        String path = String.format("/plugins/cms/assets", type);
        String contents = "content";

        MockMultipartFile file;
        if ("image".equals(type)) {
            file = new MockMultipartFile("file", "image_test.jpeg", mimeType, contents.getBytes());
        } else {
            file = new MockMultipartFile("file", "file_test.jpeg", mimeType, contents.getBytes());
        }

        MockHttpServletRequestBuilder request = multipart(path)
                .file(file)
                .param("group", group)
                .header("Authorization", "Bearer " + accessToken);

        if (type != null) {
            request.param("type", type);
        }

        return mockMvc.perform(request);
    }

    private ResultActions performDeleteResource(String accessToken, String type, String resourceId) throws Exception {
        String path = String.format("/plugins/cms/assets/%s", resourceId);
        return mockMvc.perform(
                delete(path)
                        .header("Authorization", "Bearer " + accessToken));
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
        result.andDo(print());
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
        Content masterContent = this.contentManager.loadContent("EVN193", true);
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
    public void testLoadOrderedPublicEvents_5() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_DESCR_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(9)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN25")))
                .andExpect(jsonPath("$.payload[1].id", is("EVN21")))
                .andExpect(jsonPath("$.payload[2].id", is("EVN20")))
                .andExpect(jsonPath("$.payload[3].id", is("EVN194")))
                .andExpect(jsonPath("$.payload[4].id", is("EVN193")))
                .andExpect(jsonPath("$.payload[5].id", is("EVN192")))
                .andExpect(jsonPath("$.payload[6].id", is("EVN191")))
                .andExpect(jsonPath("$.payload[7].id", is("EVN23")))
                .andExpect(jsonPath("$.payload[8].id", is("EVN24")));
    }

    @Test
    public void testLoadOrderedPublicEvents_6() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(9)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN25")));
    }

    @Test
    public void testLoadOrderedPublicEvents_7() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_FIRST_EDITOR_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)));
    }

    @Test
    public void testLoadOrderedPublicEvents_8() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(9)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN20")))
                .andExpect(jsonPath("$.payload[1].id", is("EVN192")))
                .andExpect(jsonPath("$.payload[2].id", is("EVN23")))
                .andExpect(jsonPath("$.payload[3].id", is("EVN24")))
                .andExpect(jsonPath("$.payload[4].id", is("EVN21")))
                .andExpect(jsonPath("$.payload[5].id", is("EVN25")))
                .andExpect(jsonPath("$.payload[6].id", is("EVN191")))
                .andExpect(jsonPath("$.payload[7].id", is("EVN194")))
                .andExpect(jsonPath("$.payload[8].id", is("EVN193")));
    }

    @Test
    public void testLoadOrderedPublicEvents_9() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", "status")
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(9)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN193")))
                .andExpect(jsonPath("$.payload[1].id", is("EVN194")))
                .andExpect(jsonPath("$.payload[2].id", is("EVN191")))
                .andExpect(jsonPath("$.payload[3].id", is("EVN25")))
                .andExpect(jsonPath("$.payload[4].id", is("EVN21")))
                .andExpect(jsonPath("$.payload[5].id", is("EVN24")))
                .andExpect(jsonPath("$.payload[6].id", is("EVN23")))
                .andExpect(jsonPath("$.payload[7].id", is("EVN192")))
                .andExpect(jsonPath("$.payload[8].id", is("EVN20")));
    }

    @Test
    public void testLoadOrderedPublicEvents_10() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", "lastmodified")
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(9)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN21")))
                .andExpect(jsonPath("$.payload[1].id", is("EVN20")))
                .andExpect(jsonPath("$.payload[2].id", is("EVN25")))
                .andExpect(jsonPath("$.payload[3].id", is("EVN24")))
                .andExpect(jsonPath("$.payload[4].id", is("EVN23")))
                .andExpect(jsonPath("$.payload[5].id", is("EVN192")))
                .andExpect(jsonPath("$.payload[6].id", is("EVN191")))
                .andExpect(jsonPath("$.payload[7].id", is("EVN194")))
                .andExpect(jsonPath("$.payload[8].id", is("EVN193")));
    }

    @Test
    public void testLoadOrderedPublicEvents_11() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(9)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN21")))
                .andExpect(jsonPath("$.payload[1].id", is("EVN25")))
                .andExpect(jsonPath("$.payload[2].id", is("EVN24")))
                .andExpect(jsonPath("$.payload[3].id", is("EVN23")))
                .andExpect(jsonPath("$.payload[4].id", is("EVN20")))
                .andExpect(jsonPath("$.payload[5].id", is("EVN194")))
                .andExpect(jsonPath("$.payload[6].id", is("EVN193")))
                .andExpect(jsonPath("$.payload[7].id", is("EVN192")))
                .andExpect(jsonPath("$.payload[8].id", is("EVN191")));
    }

    public void testFilteredContent_1() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", "description")
                        .param("filters[0].value", "Sagra")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN21")));
    }

    @Test
    public void testFilteredContent_2() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("sort", IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("filters[0].attribute", "id")
                        .param("filters[0].value", "EVN194")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("EVN194")));
    }

    @Test
    public void testFilteredContent_3() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", "firsteditor")
                        .param("filters[0].value", "admin")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));
    }

    @Test
    public void testFilteredContent_4() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "firsteditor")
                        .param("filters[1].value", "editor1")
                        .param("filters[1].operator", "eq")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));
    }

    @Test
    public void testFilteredContent_5() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "firsteditor")
                        .param("filters[1].value", "editor2")
                        .param("filters[1].operator", "eq")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilteredContent_6() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "firsteditor")
                        .param("filters[1].value", "editor2")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilteredContent_7() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "firsteditor")
                        .param("filters[1].value", "editor1")
                        .param("filters[1].operator", "not")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilteredContent_8() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "mainGroup")
                        .param("filters[1].value", "free")
                        .param("filters[1].operator", "eq")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(8)));
    }

    @Test
    public void testFilteredContent_9() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "mainGroup")
                        .param("filters[1].value", "free")
                        .param("filters[1].operator", "not")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilteredContent_10() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("categories[0]", "general_cat1")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));
    }

    @Test
    public void testFilteredContent_11() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("categories[0]", "general_cat1")
                        .param("categories[1]", "general_cat2")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilteredContent_12() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("categories[0]", "general_cat2")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilteredContent_13() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("categories[0]", "general_cat3")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));
    }
    
    @Test
    public void testFilteredContent_14() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "group")
                        .param("filters[1].value", "group1")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)));
    }
    
    @Test
    public void testFilteredContent_15() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "group")
                        .param("filters[1].value", "free")
                        .param("filters[2].attribute", "group")
                        .param("filters[2].value", "group1")
                        .param("filters[3].attribute", "group")
                        .param("filters[3].value", "group2")
                        .param("filters[4].attribute", "group")
                        .param("filters[4].value", "group3")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
    }
    
    @Test
    public void testFilteredContent_16() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "restriction")
                        .param("filters[1].value", "OPEN")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));
    }

    @Test
    public void testFilteredContent_17() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "tempRole", Permission.BACKOFFICE).build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/plugins/cms/contents")
                        .param("status", IContentService.STATUS_ONLINE)
                        .param("filters[0].attribute", IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)
                        .param("filters[0].operator", "eq")
                        .param("filters[0].value", "EVN")
                        .param("filters[1].attribute", "restriction")
                        .param("filters[1].value", "RESTRICTED")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));
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
        List<String> expectedFreeContentsId = Arrays.asList("ART111", "ART120", "ART122", "EVN25");
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

    @Test
    public void testAddContentWithLinkNumberAndCompositeBool() throws Exception {
        String newContentId = null;
        try {

            String accessToken = this.createAccessToken();
            Assert.assertNull(this.contentManager.getEntityPrototype("AL1"));

            this.executeContentTypePost("1_POST_type_with_link_number_composite_bool.json", accessToken,
                    status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("AL1"));

            ResultActions result = this
                    .executeContentPost("1_POST_valid_with_link_number_composite_bool.json", accessToken,
                            status().isOk());
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].firstEditor", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload[0].lastEditor", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload[0].mainGroup", is("free")))
                    .andExpect(jsonPath("$.payload[0].restriction", is("OPEN")))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].restriction", is("OPEN")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("composite")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].code", is("bool-compo")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].compositeelements[0].value", is(true)))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].values.it", is("a")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].code", is("number")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].value", is("1")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("AL1")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("AL1");
            }
        }
    }

    @Test
    public void testAddContentWithAllAttributes() throws Exception {
        String newContentId = null;
        try {

            String accessToken = this.createAccessToken();
            Assert.assertNull(this.contentManager.getEntityPrototype("AL2"));

            this.executeContentTypePost("1_POST_type_with_all_attributes.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("AL2"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_all_attributes.json", accessToken, status().isOk());
            result.andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].firstEditor", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload[0].lastEditor", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload[0].mainGroup", is("free")))
                    .andExpect(jsonPath("$.payload[0].restriction", is("OPEN")))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].restriction", is("OPEN")))

                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("attach")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("bool")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value", is(true)))
                    .andExpect(jsonPath("$.payload[0].attributes[2].code", is("checkbox")))
                    .andExpect(jsonPath("$.payload[0].attributes[2].value", is(true)))
                    .andExpect(jsonPath("$.payload[0].attributes[3].code", is("composite")))
                    .andExpect(jsonPath("$.payload[0].attributes[3].compositeelements[0].code", is("bool-compo")))
                    .andExpect(jsonPath("$.payload[0].attributes[3].compositeelements[0].value", is(true)))
                    .andExpect(jsonPath("$.payload[0].attributes[4].code", is("date")))
                    .andExpect(jsonPath("$.payload[0].attributes[4].value", is("2020-01-29 00:00:00")))
                    .andExpect(jsonPath("$.payload[0].attributes[5].code", is("enumerator")))
                    .andExpect(jsonPath("$.payload[0].attributes[5].value", is("lable1")))
                    .andExpect(jsonPath("$.payload[0].attributes[6].code", is("enumap")))
                    .andExpect(jsonPath("$.payload[0].attributes[6].value", is("key1")))
                    .andExpect(jsonPath("$.payload[0].attributes[7].code", is("hypertext")))
                    .andExpect(jsonPath("$.payload[0].attributes[7].values.en", is("<p>adasdf</p>")))
                    .andExpect(jsonPath("$.payload[0].attributes[8].code", is("image")))
                    .andExpect(jsonPath("$.payload[0].attributes[9].code", is("link")))
                    .andExpect(jsonPath("$.payload[0].attributes[9].values.it", is("a")))
                    .andExpect(jsonPath("$.payload[0].attributes[10].code", is("longtext")))
                    .andExpect(jsonPath("$.payload[0].attributes[10].values.en", is("sdfadsfasf")))
                    .andExpect(jsonPath("$.payload[0].attributes[11].code", is("monolist")))
                    .andExpect(jsonPath("$.payload[0].attributes[11].elements[0].code", is("monolist")))
                    .andExpect(jsonPath("$.payload[0].attributes[11].elements[0].values.it", is("a")))
                    .andExpect(jsonPath("$.payload[0].attributes[12].code", is("monotext")))
                    .andExpect(jsonPath("$.payload[0].attributes[12].value", is("dfadfa")))
                    .andExpect(jsonPath("$.payload[0].attributes[13].code", is("number")))
                    .andExpect(jsonPath("$.payload[0].attributes[13].value", is("1")))
                    .andExpect(jsonPath("$.payload[0].attributes[14].code", is("text")))
                    .andExpect(jsonPath("$.payload[0].attributes[14].values.it", is("dfsdfasd")))
                    .andExpect(jsonPath("$.payload[0].attributes[15].code", is("threestate")))
                    .andExpect(jsonPath("$.payload[0].attributes[15].value", is(false)))
                    .andExpect(jsonPath("$.payload[0].attributes[16].code", is("list")))
                    .andExpect(jsonPath("$.payload[0].attributes[16].listelements.en[0].code", is("list")))
                    .andExpect(jsonPath("$.payload[0].attributes[16].listelements.en[0].value", is("1")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");

        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("AL2")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("AL2");
            }
        }
    }

    public void testGetPageOfflineNoWidgetErrorMessage() throws Exception {
        String pageCode = "page_error_test";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            Page mockPage = createPage(pageCode, false);
            this.pageManager.addPage(mockPage);

            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, CoreMatchers.is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, CoreMatchers.is(not(nullValue())));

            this.executeContentPost("1_POST_invalid_with_link.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The destination page must be published")));
        } finally {
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testGetPageOfflineWithWidgetErrorMessage() throws Exception {
        String pageCode = "page_error_test";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            Page mockPage = createPage(pageCode, true);
            this.pageManager.addPage(mockPage);

            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, CoreMatchers.is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, CoreMatchers.is(not(nullValue())));

            this.executeContentPost("1_POST_invalid_with_link.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The destination page must be published")));
        } finally {
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testGetPageOnlineNoWidgetErrorMessage() throws Exception {
        String pageCode = "page_error_test";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            Page mockPage = createPage(pageCode, false);
            this.pageManager.addPage(mockPage);

            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, CoreMatchers.is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, CoreMatchers.is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();

            String putPageOnlinePayload = "{\"status\": \"published\"}";
            ResultActions result = mockMvc.perform(
                    put("/pages/{pageCode}/status", pageCode)
                            .sessionAttr("user", user)
                            .content(putPageOnlinePayload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            this.executeContentPost("1_POST_invalid_with_link.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The destination page must have a widget set")));
        } finally {
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testGetPageOnlineWrongGroupErrorMessage() throws Exception {
        String pageCode = "page_error_test";
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            Page mockPage = createPage(pageCode, true, "wrongGroup");
            this.pageManager.addPage(mockPage);

            IPage onlinePage = this.pageManager.getOnlinePage(pageCode);
            assertThat(onlinePage, CoreMatchers.is(nullValue()));
            IPage draftPage = this.pageManager.getDraftPage(pageCode);
            assertThat(draftPage, CoreMatchers.is(not(nullValue())));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();

            String putPageOnlinePayload = "{\"status\": \"published\"}";
            ResultActions result = mockMvc.perform(
                    put("/pages/{pageCode}/status", pageCode)
                            .sessionAttr("user", user)
                            .content(putPageOnlinePayload)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            this.executeContentPost("1_POST_invalid_with_link.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The destination page must belong to the group(s): free")));
        } finally {
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
            this.pageManager.deletePage(pageCode);
        }
    }

    @Test
    public void testAddContentWithEmptyLinkAttribute() throws Exception {
        String newContentId = null;
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_empty_link.json", accessToken, status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.resourceDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(0)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!!#")));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId, false);
            Assert.assertNotNull(newContent);
            this.contentManager.deleteContent(newContent);

            result = this.executeContentPost("1_POST_valid_with_empty_link2.json", accessToken, status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.resourceDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(0)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!!#")));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            newContent = this.contentManager.loadContent(newContentId, false);
            Assert.assertNotNull(newContent);
            this.contentManager.deleteContent(newContent);

            result = this.executeContentPost("1_POST_valid_with_empty_link3.json", accessToken, status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.resourceDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(0)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!!#")));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            newContent = this.contentManager.loadContent(newContentId, false);
            Assert.assertNotNull(newContent);
            this.contentManager.deleteContent(newContent);

            result = this.executeContentPost("1_POST_valid_with_empty_link4.json", accessToken, status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.resourceDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(0)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!!#")));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            newContent = this.contentManager.loadContent(newContentId, false);
            Assert.assertNotNull(newContent);
            this.contentManager.deleteContent(newContent);

            result = this.executeContentPost("1_POST_valid_with_empty_link5.json", accessToken, status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))

                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.urlDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.resourceDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.destType", is(0)))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.pageDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.symbolicDestination", is("#!!#")));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");
            newContent = this.contentManager.loadContent(newContentId, false);
            Assert.assertNotNull(newContent);
            this.contentManager.deleteContent(newContent);


        } finally {
            if (null != newContentId) {
                Content newContent = this.contentManager.loadContent(newContentId, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
        }
    }

    @Test
    public void testInvalidLinkMessage() throws Exception {
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            this.executeContentPost("1_POST_invalid_with_link2.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The Link attribute is invalid or incomplete")));

            this.executeContentPost("1_POST_invalid_with_link3.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The Link attribute is invalid or incomplete")));

            this.executeContentPost("1_POST_invalid_with_link4.json", accessToken, status().isBadRequest())
                    .andExpect(jsonPath("$.payload.size()", is(0)))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.errors[0].code", is("4")))
                    .andExpect(jsonPath("$.errors[0].message", is("Attribute 'link1' Invalid: The Link attribute is invalid or incomplete")));
        } finally {
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
        }
    }

    @Test
    public void testContentWithReference() throws Exception {
        String newContentId1 = null;
        String newContentId2 = null;
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("CML"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_link.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("CML"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_link.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId1 = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId1, false);

            Assert.assertNotNull(newContent);

            ContentStatusRequest contentStatusRequest = new ContentStatusRequest();
            contentStatusRequest.setStatus("published");
            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId1)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = this.executeContentPost("1_POST_valid_with_link_to_content.json", accessToken, status().isOk(), newContentId1)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", is(newContentId1)));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId2 = JsonPath.read(bodyResult, "$.payload[0].id");

            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId2)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(get("/plugins/cms/contents/{code}", newContentId1)
                            .sessionAttr("user", user)
                            .header("Authorization", "Bearer " + accessToken));
            result
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.id", is(newContentId1)))
                    .andExpect(jsonPath("$.payload.references.CmsPageManagerWrapper", is(false)))
                    .andExpect(jsonPath("$.payload.references.jacmsContentManager", is(true)));

            contentStatusRequest.setStatus("draft");
            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId1)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.errors[0].code", is("2")))
                    .andExpect(jsonPath("$.errors[0].message", is("Content '" + newContentId1 + "' cannot be unpublished because it is referenced")));

        } finally {
            if (null != newContentId2) {
                Content newContent = this.contentManager.loadContent(newContentId2, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != newContentId1) {
                Content newContent = this.contentManager.loadContent(newContentId1, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("CML")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("CML");
            }
        }
    }

    @Test
    public void testContentWithReferenceBatch() throws Exception {
        String newContentId1 = null;
        String newContentId2 = null;
        String newContentId3 = null;

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR)
                .build();
        try {
            Assert.assertNull(this.contentManager.getEntityPrototype("LNK"));
            String accessToken = this.createAccessToken();

            this.executeContentTypePost("1_POST_type_with_links.json", accessToken, status().isCreated());
            Assert.assertNotNull(this.contentManager.getEntityPrototype("LNK"));

            ResultActions result = this.executeContentPost("1_POST_valid_with_empty_links.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId1 = JsonPath.read(bodyResult, "$.payload[0].id");
            Content newContent = this.contentManager.loadContent(newContentId1, false);

            Assert.assertNotNull(newContent);

            ContentStatusRequest contentStatusRequest = new ContentStatusRequest();
            contentStatusRequest.setStatus("published");
            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId1)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = this.executeContentPost("1_POST_valid_with_empty_links.json", accessToken, status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value", Matchers.isEmptyOrNullString()));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId2 = JsonPath.read(bodyResult, "$.payload[0].id");
            newContent = this.contentManager.loadContent(newContentId2, false);

            Assert.assertNotNull(newContent);

            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId2)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = this.executeContentPost("1_POST_valid_with_link_to_contents.json", accessToken, status().isOk(), newContentId1, newContentId2)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.errors.size()", is(0)))
                    .andExpect(jsonPath("$.metaData.size()", is(0)))
                    .andExpect(jsonPath("$.payload[0].id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload[0].attributes[0].code", is("link1")))
                    .andExpect(jsonPath("$.payload[0].attributes[0].value.contentDest", is(newContentId1)))
                    .andExpect(jsonPath("$.payload[0].attributes[1].code", is("link2")))
                    .andExpect(jsonPath("$.payload[0].attributes[1].value.contentDest", is(newContentId2)));

            bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId3 = JsonPath.read(bodyResult, "$.payload[0].id");

            result = mockMvc
                    .perform(put("/plugins/cms/contents/{code}/status", newContentId3)
                            .content(mapper.writeValueAsString(contentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            result = mockMvc
                    .perform(get("/plugins/cms/contents/{code}", newContentId1)
                            .sessionAttr("user", user)
                            .header("Authorization", "Bearer " + accessToken));
            result
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.id", is(newContentId1)))
                    .andExpect(jsonPath("$.payload.references.CmsPageManagerWrapper", is(false)))
                    .andExpect(jsonPath("$.payload.references.jacmsContentManager", is(true)));

            result = mockMvc
                    .perform(get("/plugins/cms/contents/{code}", newContentId2)
                            .sessionAttr("user", user)
                            .header("Authorization", "Bearer " + accessToken));
            result
                    .andDo(print())
                    .andExpect(jsonPath("$.payload.id", is(newContentId2)))
                    .andExpect(jsonPath("$.payload.references.CmsPageManagerWrapper", is(false)))
                    .andExpect(jsonPath("$.payload.references.jacmsContentManager", is(true)));

            BatchContentStatusRequest batchContentStatusRequest = new BatchContentStatusRequest();
            batchContentStatusRequest.setStatus("draft");
            batchContentStatusRequest.getCodes().add(newContentId1);
            batchContentStatusRequest.getCodes().add(newContentId2);

            result = mockMvc
                    .perform(put("/plugins/cms/contents/status")
                            .content(mapper.writeValueAsString(batchContentStatusRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken));
            result.andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.size()", is(2)))
                    .andExpect(jsonPath("$.errors[0].code", is("2")))
                    .andExpect(jsonPath("$.errors[0].message", is("Content '" + newContentId1 + "' cannot be unpublished because it is referenced")))
                    .andExpect(jsonPath("$.errors[1].code", is("2")))
                    .andExpect(jsonPath("$.errors[1].message", is("Content '" + newContentId2 + "' cannot be unpublished because it is referenced")));

        } finally {
            if (null != newContentId3) {
                Content newContent = this.contentManager.loadContent(newContentId3, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != newContentId2) {
                Content newContent = this.contentManager.loadContent(newContentId2, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != newContentId1) {
                Content newContent = this.contentManager.loadContent(newContentId1, false);
                if (null != newContent) {
                    this.contentManager.deleteContent(newContent);
                }
            }
            if (null != this.contentManager.getEntityPrototype("LNK")) {
                ((IEntityTypesConfigurer) this.contentManager).removeEntityPrototype("LNK");
            }
        }
    }

    protected Page createPage(String pageCode, boolean addWidget) {
        return createPage(pageCode, addWidget, "free");
    }

    protected Page createPage(String pageCode, boolean addWidget, String groupName) {
        IPage parentPage = pageManager.getDraftPage("service");
        PageModel pageModel = parentPage.getMetadata().getModel();
        PageMetadata metadata = PageTestUtil
                .createPageMetadata(pageModel, true, pageCode + "_title", null, null, false, null, null);
        ApsProperties config = PageTestUtil.createProperties("temp", "tempValue", "contentId", "ART11");
        Widget[] widgets = null;
        if (addWidget) {
            widgets = new Widget[pageModel.getFrames().length];
            Widget widgetToAdd = PageTestUtil.createWidget("content_viewer", config, this.widgetTypeManager);
            widgets[0] = widgetToAdd;
        }
        Page pageToAdd = PageTestUtil.createPage(pageCode, parentPage.getCode(), groupName, metadata, widgets);
        return pageToAdd;
    }
}
