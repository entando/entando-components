package org.entando.entando.jpversioning.web.resource;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.entando.entando.plugins.jacms.web.resource.request.CreateResourceRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class ResourceVersioningControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private TrashedResourceManager trashedResourceManager;

    @Test
    public void testListDeletedAttachments() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";

        cleanUpAttach(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        ResultActions result = performCreateResource(user, "file", "free", "application/pdf");

        String bodyResult = result.andReturn().getResponse().getContentAsString();
        String resourceId = JsonPath.read(bodyResult, "$.payload.id");

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

    }

    @Test
    public void testListDeletedAttachmentsPagination() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";
        String type = "file";

        cleanUpAttach(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(6)));

        params.put("pageSize", "4");
        params.put("page", "1");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(4)))
                .andExpect(jsonPath("$.metaData.page", is(1)))
                .andExpect(jsonPath("$.metaData.pageSize", is(4)))
                .andExpect(jsonPath("$.metaData.totalItems", is(6)));

        params.clear();
        params.put("resourceTypeCode", resourceTypeCode);
        params.put("pageSize", "4");
        params.put("page", "2");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(2)))
                .andExpect(jsonPath("$.metaData.page", is(2)))
                .andExpect(jsonPath("$.metaData.pageSize", is(4)))
                .andExpect(jsonPath("$.metaData.totalItems", is(6)));

    }

    @Test
    public void testListDeletedAttachmentsFiltering() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";
        String type = "file";

        cleanUpAttach(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)));

        params.put("filters[0].attribute", "description");
        params.put("filters[0].value", "file_test.jpeg");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(3)))
                .andExpect(jsonPath("$.payload[0].description", is("file_test.jpeg")))
                .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                .andExpect(jsonPath("$.metaData.filters[0].attribute", is("description")))
                .andExpect(jsonPath("$.metaData.filters[0].value", is("file_test.jpeg")));

        params.clear();
        params.put("resourceTypeCode", resourceTypeCode);
        params.put("filters[0].attribute", "description");
        params.put("filters[0].value", "wrong description");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(0)))
                .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                .andExpect(jsonPath("$.metaData.filters[0].attribute", is("description")))
                .andExpect(jsonPath("$.metaData.filters[0].value", is("wrong description")));

    }

    @Test
    public void testListDeletedImage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Image";
        String type = "image";

        cleanUpImage(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        createAndDeleteResource(user, resourceTypeCode, type, params);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));

    }

    @Test
    public void testListDeletedImagePagination() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Image";
        String type = "image";

        cleanUpImage(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);
        createAndDeleteResource(user, resourceTypeCode, type, params);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(6)));

        params.put("pageSize", "4");
        params.put("page", "1");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(4)))
                .andExpect(jsonPath("$.metaData.page", is(1)))
                .andExpect(jsonPath("$.metaData.pageSize", is(4)))
                .andExpect(jsonPath("$.metaData.totalItems", is(6)));

        params.clear();
        params.put("resourceTypeCode", resourceTypeCode);
        params.put("pageSize", "4");
        params.put("page", "2");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(2)))
                .andExpect(jsonPath("$.metaData.page", is(2)))
                .andExpect(jsonPath("$.metaData.pageSize", is(4)))
                .andExpect(jsonPath("$.metaData.totalItems", is(6)));

    }

    @Test
    public void testListDeletedImageFiltering() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Image";
        String type = "image";

        cleanUpImage(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        createAndDeleteResource(user, resourceTypeCode, type, params);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));

        params.put("filters[0].attribute", "description");
        params.put("filters[0].value", "image_test.jpeg");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].description", is("image_test.jpeg")))
                .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                .andExpect(jsonPath("$.metaData.filters[0].attribute", is("description")))
                .andExpect(jsonPath("$.metaData.filters[0].value", is("image_test.jpeg")));

        params.clear();
        params.put("resourceTypeCode", resourceTypeCode);
        params.put("filters[0].attribute", "description");
        params.put("filters[0].value", "wrong description");

        listTrashedResources(user, params)
                .andExpect(jsonPath("$.payload.size()", is(0)))
                .andExpect(jsonPath("$.metaData.filters.size()", is(1)))
                .andExpect(jsonPath("$.metaData.filters[0].attribute", is("description")))
                .andExpect(jsonPath("$.metaData.filters[0].value", is("wrong description")));

    }

    @Test
    public void testTrashedAttachmentsRecover() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";

        cleanUpAttach(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        ResultActions result = performCreateResource(user, "file", "free", "application/pdf");

        String bodyResult = result.andReturn().getResponse().getContentAsString();
        String resourceId = JsonPath.read(bodyResult, "$.payload.id");

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        performGetResources(user, "file")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        result = listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performGetResources(user, "file")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)));

        bodyResult = result.andReturn().getResponse().getContentAsString();

        String versionResourceId = JsonPath.read(bodyResult, "$.payload[0].id");

        recoverResource(user, versionResourceId)
                .andExpect(status().isOk());

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        performGetResources(user, "file")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

    }

    @Test
    public void testTrashedImageRecover() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Image";
        String resourceId = null;
        String versionResourceId = null;

        cleanUpImage(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        ResultActions result = performCreateResource(user, "image", "free", "application/jpeg");

        String bodyResult = result.andReturn().getResponse().getContentAsString();
        resourceId = JsonPath.read(bodyResult, "$.payload.id");

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performGetResources(user, "image")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        result = listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));

        performGetResources(user, "image")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        bodyResult = result.andReturn().getResponse().getContentAsString();

        versionResourceId = JsonPath.read(bodyResult, "$.payload[1].id");

        deleteTrashedResource(user, versionResourceId)
                .andExpect(status().isOk());

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performGetResources(user, "image")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

    }

    @Test
    public void testDeleteTrashedAttachment() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";
        String resourceId = null;
        String versionResourceId = null;

        cleanUpAttach(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        ResultActions result = performCreateResource(user, "file", "free", "application/pdf");

        String bodyResult = result.andReturn().getResponse().getContentAsString();
        resourceId = JsonPath.read(bodyResult, "$.payload.id");

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        performGetResources(user, "file")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        result = listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performGetResources(user, "file")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)));

        bodyResult = result.andReturn().getResponse().getContentAsString();

        versionResourceId = JsonPath.read(bodyResult, "$.payload[0].id");

        recoverResource(user, versionResourceId)
                .andExpect(status().isOk());

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        performGetResources(user, "file")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteTrashedImage() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Image";
        String resourceId = null;
        String versionResourceId = null;

        cleanUpImage(user);

        Map<String, String> params = new HashMap<>();
        params.put("resourceTypeCode", resourceTypeCode);

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        ResultActions result = performCreateResource(user, "image", "free", "application/jpeg");

        String bodyResult = result.andReturn().getResponse().getContentAsString();
        resourceId = JsonPath.read(bodyResult, "$.payload.id");

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performGetResources(user, "image")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        result = listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)));

        performGetResources(user, "image")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

        bodyResult = result.andReturn().getResponse().getContentAsString();

        versionResourceId = JsonPath.read(bodyResult, "$.payload[1].id");

        deleteTrashedResource(user, versionResourceId)
                .andExpect(status().isOk());

        listTrashedResources(user, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)));

        performGetResources(user, "image")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)));

    }

    private void cleanUpImage(UserDetails user) throws Exception {
        ResultActions result = performGetResources(user, "image")
                .andExpect(status().isOk());

        String bodyResult = result.andReturn().getResponse().getContentAsString();

        Integer payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        for (int i = 0; i < payloadSize; i++) {
            performDeleteResource(user, "Image", JsonPath.read(bodyResult, "$.payload[" + i + "].id"));
        }

        Map<String,String> params = new HashMap<>();
        params.put("resourceTypeCode", "Image");

        result = listTrashedResources(user, params);

        bodyResult = result.andReturn().getResponse().getContentAsString();
        payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        for (int i = 0; i < payloadSize; i++) {
            trashedResourceManager.removeFromTrash(JsonPath.read(bodyResult, "$.payload[" + i + "].id"));
        }
    }

    private void cleanUpAttach(UserDetails user) throws Exception {
        ResultActions result = performGetResources(user, "file")
                .andExpect(status().isOk());

        String bodyResult = result.andReturn().getResponse().getContentAsString();

        Integer payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        for (int i = 0; i < payloadSize; i++) {
            if (JsonPath.read(bodyResult, "$.payload[" + i + "].description").equals("file_test.jpeg")) {
                performDeleteResource(user, "Attach", JsonPath.read(bodyResult, "$.payload[" + i + "].id"));
            }
        }

        Map<String,String> params = new HashMap<>();
        params.put("resourceTypeCode", "Attach");

        result = listTrashedResources(user, params);

        bodyResult = result.andReturn().getResponse().getContentAsString();
        payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        for (int i = 0; i < payloadSize; i++) {
            trashedResourceManager.removeFromTrash(JsonPath.read(bodyResult, "$.payload[" + i + "].id"));
        }
    }

    private ResultActions recoverResource(UserDetails user, String versionResourceId) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = post(
                "/plugins/versioning/resources/{resourceId}/recover", versionResourceId)
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + mockOAuthInterceptor(user));

        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    private ResultActions deleteTrashedResource(UserDetails user, String versionResourceId) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = delete(
                "/plugins/versioning/resources/{resourceId}", versionResourceId)
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + mockOAuthInterceptor(user));

        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    private String createAndDeleteResource(UserDetails user, String resourceTypeCode, String type, Map<String, String> params) throws Exception {
        String mimeType = type.equals("image") ? "application/jpeg" : "application/pdf";
        ResultActions result = performCreateResource(user, type, "free", mimeType);
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        String resourceId = JsonPath.read(bodyResult, "$.payload.id");

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        result = listTrashedResources(user, params)
                .andExpect(status().isOk());
        bodyResult = result.andReturn().getResponse().getContentAsString();

        Integer payloadSize = JsonPath.read(bodyResult, "$.payload.size()");
        for (int i = 0; i < payloadSize; i++) {
            if (JsonPath.read(bodyResult, "$.payload[" + i + "].description").equals("image_test.jpeg")
                    || JsonPath.read(bodyResult, "$.payload[" + i + "].description").equals("file_test.jpeg")) {
                return JsonPath.read(bodyResult, "$.payload[" + i + "].id");
            }
        }
        return null;
    }

    private ResultActions listTrashedResources(UserDetails user, Map<String,String> params) throws Exception {
        String accessToken = mockOAuthInterceptor(user);

        final MockHttpServletRequestBuilder requestBuilder = get("/plugins/versioning/resources")
                .sessionAttr("user", user)
                .header("Authorization", "Bearer " + accessToken);

        for (String key : Optional.ofNullable(params).orElse(new HashMap<>()).keySet()) {
            requestBuilder.param(key, params.get(key));
        }

        return mockMvc.perform(requestBuilder)
                .andDo(print());
    }

    private ResultActions performDeleteResource(UserDetails user, String type, String resourceId) throws Exception {
        String path = String.format("/plugins/cms/assets/%s", resourceId);

        if (null == user) {
            return mockMvc.perform(get(path));
        }

        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                delete(path)
                        .header("Authorization", "Bearer " + accessToken)).andDo(print());
    }

    private ResultActions performCreateResource(UserDetails user, String type, String group, String mimeType) throws Exception {
        String urlPath = String.format("/plugins/cms/assets", type);

        CreateResourceRequest resourceRequest = new CreateResourceRequest();
        resourceRequest.setType(type);
        resourceRequest.setGroup(group);

        if (null == user) {
            return mockMvc.perform(get(urlPath));
        }

        String accessToken = mockOAuthInterceptor(user);
        String contents = "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x" +
                "a;lsdka;lsdka;lsdka;lsdk;alskd;laskd;aslkd;alsdk;alskda;lskldaskl;sdjodpasu0i9728938701o7i186r890347974209817409823740bgbdf98dw787012378b1789b13281328701b39871029371x";

        MockMultipartFile file;
        if ("image".equals(type)) {
            file = new MockMultipartFile("file", "image_test.jpeg", mimeType, contents.getBytes());
        } else {
            file = new MockMultipartFile("file", "file_test.jpeg", mimeType, contents.getBytes());
        }

        MockHttpServletRequestBuilder request = multipart(urlPath)
                .file(file)
                .param("metadata", MAPPER.writeValueAsString(resourceRequest))
                .header("Authorization", "Bearer " + accessToken);

        return mockMvc.perform(request).andDo(print());
    }

    private ResultActions performGetResources(UserDetails user, String type) throws Exception {
        String path = "/plugins/cms/assets";

        if (type != null) {
            path += "?type=" + type;
        }

        if (null == user) {
            return mockMvc.perform(get(path));
        }

        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                get(path)
                        .header("Authorization", "Bearer " + accessToken)).andDo(print());
    }

}
