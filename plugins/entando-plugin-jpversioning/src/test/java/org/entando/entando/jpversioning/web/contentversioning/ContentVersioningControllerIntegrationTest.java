package org.entando.entando.jpversioning.web.contentversioning;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import com.jayway.jsonpath.JsonPath;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class ContentVersioningControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private VersioningManager versioningManager;

    @Autowired
    private IContentManager contentManager;

    public static final String PLACEHOLDER_STRING = "resourceIdPlaceHolder";

    @Test
    public void testListContentVersion() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String contentTypeCode = "CT1";
        String newContentId = null;

        try {
            String accessToken = mockOAuthInterceptor(user);

            Assert.assertNull(contentManager.getEntityPrototype(contentTypeCode));
            postContentType("json/1_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());
            Assert.assertNotNull(contentManager.getEntityPrototype(contentTypeCode));

            ResultActions result = postContent("json/1_POST_content_with_boolean_attribute.json", accessToken,
                    status().isOk());
            result.andExpect(jsonPath("$.payload.size()", is(1)));

            String bodyResult = result.andReturn().getResponse().getContentAsString();
            newContentId = JsonPath.read(bodyResult, "$.payload[0].id");

            listContentVersions(user, newContentId)
                    .andExpect(jsonPath("$.payload.size()", is(0)));

            versioningManager.saveContentVersion(newContentId);
            listContentVersions(user, newContentId)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.payload[0].id", notNullValue()))
                    .andExpect(jsonPath("$.payload[0].contentType", is(contentTypeCode)))
                    .andExpect(jsonPath("$.payload[0].description", is("content-bool")))
                    .andExpect(jsonPath("$.payload[0].version", is("0.1")))
                    .andExpect(jsonPath("$.payload[0].username", is("jack_bauer")));

        } finally {
            deleteContent(user, newContentId);
            deleteContentType(contentTypeCode);
        }
    }

    @Test
    public void testListLatestVersions() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String contentTypeCode = "CT1";
        String newContentId1 = null;
        String newContentId2 = null;
        String newContentId3 = null;

        try {
            String accessToken = mockOAuthInterceptor(user);

            postContentType("json/1_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());

            newContentId1 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            saveContentVersion(user, newContentId1);

            newContentId2 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            saveContentVersion(user, newContentId2);

            newContentId3 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            saveContentVersion(user, newContentId3);

            listLatestVersions(user)
                    .andExpect(status().isOk());

        } finally {
            deleteContent(user, newContentId3);
            deleteContent(user, newContentId2);
            deleteContent(user, newContentId1);
            deleteContentType(contentTypeCode);
        }
    }

    @Test
    public void testListLatestVersionsWithMultipleVersion() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String contentTypeCode = "CT1";
        String newContentId1 = null;
        String newContentId2 = null;

        try {
            String accessToken = mockOAuthInterceptor(user);

            postContentType("json/1_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());

            newContentId1 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);

            Content newContent1 = contentManager.loadContent(newContentId1, false);
            contentManager.saveContent(newContent1);
            contentManager.saveContent(newContent1);

            newContentId2 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);

            Content newContent2 = contentManager.loadContent(newContentId2, false);
            contentManager.saveContent(newContent2);
            contentManager.saveContent(newContent2);

            listLatestVersions(user)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.payload[0].contentType", is(contentTypeCode)))
                    .andExpect(jsonPath("$.payload[0].description", is("content-bool")))
                    .andExpect(jsonPath("$.payload[0].version", is("0.2")))
                    .andExpect(jsonPath("$.payload[1].contentType", is(contentTypeCode)))
                    .andExpect(jsonPath("$.payload[1].description", is("content-bool")))
                    .andExpect(jsonPath("$.payload[1].version", is("0.2")));

        } finally {
            deleteContent(user, newContentId2);
            deleteContent(user, newContentId1);
            deleteContentType(contentTypeCode);
        }
    }

    @Test
    public void testPaginationListContentVersion() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String contentTypeCode = "CT1";
        String newContentId = null;

        try {
            String accessToken = mockOAuthInterceptor(user);

            postContentType("json/1_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());

            newContentId = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);

            listContentVersions(user, newContentId)
                    .andExpect(jsonPath("$.payload.size()", is(0)));

            Content newContent = contentManager.loadContent(newContentId, false);

            contentManager.saveContent(newContent);
            contentManager.saveContent(newContent);
            contentManager.saveContent(newContent);
            contentManager.saveContent(newContent);
            contentManager.saveContent(newContent);

            listContentVersions(user, newContentId)
                    .andExpect(jsonPath("$.payload.size()", is(5)))
                    .andExpect(jsonPath("$.metaData.page", is(1)))
                    .andExpect(jsonPath("$.metaData.pageSize", is(100)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(5)));


            Map<String,String> params = new HashMap<>();
            params.put("pageSize", "4");
            params.put("page", "1");

            listContentVersions(user, newContentId, params)
                    .andExpect(jsonPath("$.payload.size()", is(4)))
                    .andExpect(jsonPath("$.metaData.page", is(1)))
                    .andExpect(jsonPath("$.metaData.pageSize", is(4)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(5)));

            params.clear();
            params.put("pageSize", "4");
            params.put("page", "2");

            listContentVersions(user, newContentId, params)
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.page", is(2)))
                    .andExpect(jsonPath("$.metaData.pageSize", is(4)))
                    .andExpect(jsonPath("$.metaData.totalItems", is(5)));

        } finally {
            deleteContent(user, newContentId);
            deleteContentType(contentTypeCode);
        }
    }

    @Test
    public void testPaginationListLatestVersions() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String contentTypeCode = "CT1";
        String newContentId1 = null;
        String newContentId2 = null;
        String newContentId3 = null;
        String newContentId4 = null;
        String newContentId5 = null;

        try {
            String accessToken = mockOAuthInterceptor(user);

            postContentType("json/1_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());

            newContentId1 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId1);
            newContentId2 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId2);
            newContentId3 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId3);
            newContentId4 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId4);
            newContentId5 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId5);

            Map<String,String> params = new HashMap<>();
            params.put("pageSize", "2");
            params.put("page", "2");
            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.metaData.page", is(2)))
                    .andExpect(jsonPath("$.metaData.pageSize", is(2)));

            params.clear();
            params.put("pageSize", "4");
            params.put("page", "1");
            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(4)))
                    .andExpect(jsonPath("$.metaData.page", is(1)))
                    .andExpect(jsonPath("$.metaData.pageSize", is(4)));

        } finally {
            deleteContent(user, newContentId5);
            deleteContent(user, newContentId4);
            deleteContent(user, newContentId3);
            deleteContent(user, newContentId2);
            deleteContent(user, newContentId1);
            deleteContentType(contentTypeCode);
        }
    }

    @Test
    public void testFilterListLatestVersions() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String contentTypeCode1 = "CT1";
        String contentTypeCode2 = "CT2";
        String newContentId1 = null;
        String newContentId2 = null;
        String newContentId3 = null;
        String newContentId4 = null;
        String newContentId5 = null;

        try {
            String accessToken = mockOAuthInterceptor(user);

            postContentType("json/1_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());
            postContentType("json/2_POST_content_type_with_boolean_attribute.json", accessToken,
                    status().isCreated());

            newContentId1 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId1);

            newContentId2 = saveContent("json/1_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId2);

            newContentId3 = saveContent("json/2_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId3);

            newContentId4 = saveContent("json/3_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId4);

            newContentId5 = saveContent("json/4_POST_content_with_boolean_attribute.json", accessToken);
            versioningManager.saveContentVersion(newContentId5);

            Map<String,String> params = new HashMap<>();
            params.put("filters[0].attribute", "wrongName");
            params.put("filters[0].value", contentTypeCode1);

            listLatestVersions(user, params)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.payload", empty()))
                    .andExpect(jsonPath("$.metaData", empty()))
                    .andExpect(jsonPath("$.errors.size()", is(1)))
                    .andExpect(jsonPath("$.errors[0].code", is("101")))
                    .andExpect(jsonPath("$.errors[0].message", is("the filtering attribute 'wrongName' does not exist")));

            params.clear();
            params.put("filters[0].attribute", "contentType");
            params.put("filters[0].value", contentTypeCode1);

            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)))
                    .andExpect(jsonPath("$.payload[0].contentType", is(contentTypeCode1)))
                    .andExpect(jsonPath("$.payload[1].contentType", is(contentTypeCode1)))
                    .andExpect(jsonPath("$.payload[2].contentType", is(contentTypeCode1)))
                    .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.filters[0].attribute", is("contentType")))
                    .andExpect(jsonPath("$.metaData.filters[0].value", is(contentTypeCode1)));

            params.clear();
            params.put("filters[0].attribute", "contentType");
            params.put("filters[0].value", contentTypeCode2);

            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.payload[0].contentType", is(contentTypeCode2)))
                    .andExpect(jsonPath("$.payload[1].contentType", is(contentTypeCode2)))
                    .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.filters[0].attribute", is("contentType")))
                    .andExpect(jsonPath("$.metaData.filters[0].value", is(contentTypeCode2)));

            params.clear();
            params.put("filters[0].attribute", "description");
            params.put("filters[0].value", "content-bool");

            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)))
                    .andExpect(jsonPath("$.payload[0].description", is("content-bool")))
                    .andExpect(jsonPath("$.payload[1].description", is("content-bool")))
                    .andExpect(jsonPath("$.payload[2].description", is("content-bool")))
                    .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.filters[0].attribute", is("description")))
                    .andExpect(jsonPath("$.metaData.filters[0].value", is("content-bool")));

            params.clear();
            params.put("filters[0].attribute", "description");
            params.put("filters[0].value", "another description");

            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.payload[0].description", is("another description")))
                    .andExpect(jsonPath("$.payload[1].description", is("another description")))
                    .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.filters[0].attribute", is("description")))
                    .andExpect(jsonPath("$.metaData.filters[0].value", is("another description")));

            params.clear();
            params.put("filters[0].attribute", "contentType");
            params.put("filters[0].value", contentTypeCode1);
            params.put("filters[1].attribute", "description");
            params.put("filters[1].value", "content-bool");

            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.payload[0].contentType", is(contentTypeCode1)))
                    .andExpect(jsonPath("$.payload[0].description", is("content-bool")))
                    .andExpect(jsonPath("$.payload[1].contentType", is(contentTypeCode1)))
                    .andExpect(jsonPath("$.payload[1].description", is("content-bool")))
                    .andExpect(jsonPath("$.metaData.filters.size()", is(2)))
                    .andExpect(jsonPath("$.metaData.filters[0].attribute", is("contentType")))
                    .andExpect(jsonPath("$.metaData.filters[0].value", is(contentTypeCode1)))
                    .andExpect(jsonPath("$.metaData.filters[1].attribute", is("description")))
                    .andExpect(jsonPath("$.metaData.filters[1].value", is("content-bool")));

            params.clear();
            params.put("filters[0].attribute", "contentType");
            params.put("filters[0].value", contentTypeCode2);
            params.put("filters[1].attribute", "description");
            params.put("filters[1].value", "another description");

            listLatestVersions(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.payload[0].contentType", is(contentTypeCode2)))
                    .andExpect(jsonPath("$.payload[0].description", is("another description")))
                    .andExpect(jsonPath("$.metaData.filters.size()", is(2)))
                    .andExpect(jsonPath("$.metaData.filters[0].attribute", is("contentType")))
                    .andExpect(jsonPath("$.metaData.filters[0].value", is(contentTypeCode2)))
                    .andExpect(jsonPath("$.metaData.filters[1].attribute", is("description")))
                    .andExpect(jsonPath("$.metaData.filters[1].value", is("another description")));

        } finally {
            deleteContent(user, newContentId5);
            deleteContent(user, newContentId4);
            deleteContent(user, newContentId3);
            deleteContent(user, newContentId2);
            deleteContent(user, newContentId1);
            deleteContentType(contentTypeCode1);
            deleteContentType(contentTypeCode2);
        }
    }

    private void deleteContentType(String contentTypeCode) throws ApsSystemException {
        if (null != contentManager.getEntityPrototype(contentTypeCode)) {
            ((IEntityTypesConfigurer) contentManager).removeEntityPrototype(contentTypeCode);
        }
    }

    private void deleteContent(UserDetails user, String contentId) throws Exception {
        removeAllVersions(user, contentId);
        if (null != contentId) {
            Content content = contentManager.loadContent(contentId, false);
            if (null != content) {
                contentManager.deleteContent(content);
            }
        }
        removeAllVersions(user, contentId);
    }

    private void removeAllVersions(UserDetails user, String contentId) throws Exception {
        ResultActions result = listContentVersions(user, contentId);
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        Integer size = JsonPath.read(bodyResult, "$.payload.size()");
        for (int i = 0; i < size; i++) {
            Integer id = JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            versioningManager.deleteVersion(Long.valueOf(id));
        }
    }

    private String saveContent(String filename, String accessToken) throws Exception {
        ResultActions result = postContent(filename, accessToken, status().isOk());
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        return JsonPath.read(bodyResult, "$.payload[0].id");
    }

    private Integer saveContentVersion(UserDetails user, String contentId) throws Exception {
        versioningManager.saveContentVersion(contentId);
        ResultActions result = listContentVersions(user, contentId);
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        return JsonPath.read(bodyResult, "$.payload[0].id");
    }

    private ResultActions listContentVersions(UserDetails user, String contentId) throws Exception {
        return listContentVersions(user, contentId, null);
    }

    private ResultActions listContentVersions(UserDetails user, String contentId, Map<String,String> params) throws Exception {
        String accessToken = mockOAuthInterceptor(user);

        final MockHttpServletRequestBuilder requestBuilder = get("/plugins/versioning/contents/{contentId}", contentId)
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + accessToken);

        for (String key : Optional.ofNullable(params).orElse(new HashMap<>()).keySet()) {
            requestBuilder.param(key, params.get(key));
        }

        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    private ResultActions listLatestVersions(UserDetails user) throws Exception {
        return listLatestVersions(user, null);
    }

    private ResultActions listLatestVersions(UserDetails user, Map<String,String> params) throws Exception {
        String accessToken = mockOAuthInterceptor(user);

        final MockHttpServletRequestBuilder requestBuilder = get("/plugins/versioning/contents/")
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + accessToken);

        for (String key : Optional.ofNullable(params).orElse(new HashMap<>()).keySet()) {
            requestBuilder.param(key, params.get(key));
        }

        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    private ResultActions postContentType(String fileName, String accessToken, ResultMatcher expected) throws Exception {
        InputStream isJsonPostValid = getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        ResultActions result = mockMvc
                .perform(post("/plugins/cms/contentTypes")
                        .content(jsonPostValid)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(expected);
        return result;
    }

    private ResultActions postContent(String fileName, String accessToken, ResultMatcher expected, String... replacements) throws Exception {
        InputStream isJsonPostValid = getClass().getResourceAsStream(fileName);
        String jsonPostValid = FileTextReader.getText(isJsonPostValid);
        if (replacements != null) {
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
}
