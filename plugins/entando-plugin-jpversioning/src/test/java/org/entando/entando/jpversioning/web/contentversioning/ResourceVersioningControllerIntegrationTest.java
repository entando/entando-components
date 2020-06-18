package org.entando.entando.jpversioning.web.contentversioning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.VersioningManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.entando.entando.plugins.jacms.web.resource.request.CreateResourceRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class ResourceVersioningControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private TrashedResourceManager trashedResourceManager;

    @Test
    public void testListDeletedAttachments() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";
        String resourceId = null;
        String versionResourceId = null;

        try {

            Map<String,String> params = new HashMap<>();
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

            performDeleteResource(user, resourceTypeCode, resourceId)
                    .andExpect(status().isOk());

            result = listTrashedResources(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)));

            bodyResult = result.andReturn().getResponse().getContentAsString();

            versionResourceId = JsonPath.read(bodyResult, "$.payload[0].id");

        } finally {
            if (versionResourceId != null) {
                trashedResourceManager.removeFromTrash(versionResourceId);
            }
        }
    }

    @Test
    public void testListDeletedAttachmentsPagination() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";
        String versionResourceId1 = null;
        String versionResourceId2 = null;
        String versionResourceId3 = null;
        String versionResourceId4 = null;
        String versionResourceId5 = null;
        String versionResourceId6 = null;

        try {

            Map<String,String> params = new HashMap<>();
            params.put("resourceTypeCode", resourceTypeCode);

            listTrashedResources(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(0)));

            versionResourceId1 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId2 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId3 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId4 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId5 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId6 = createAndDeleteResource(user, resourceTypeCode, params);

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

        } finally {
            if (versionResourceId6 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId6);
            }
            if (versionResourceId5 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId5);
            }
            if (versionResourceId4 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId4);
            }
            if (versionResourceId3 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId3);
            }
            if (versionResourceId2 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId2);
            }
            if (versionResourceId1 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId1);
            }
        }
    }

    @Test
    public void testListDeletedAttachmentsFiltering() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String resourceTypeCode = "Attach";
        String versionResourceId1 = null;
        String versionResourceId2 = null;
        String versionResourceId3 = null;

        try {

            Map<String,String> params = new HashMap<>();
            params.put("resourceTypeCode", resourceTypeCode);

            listTrashedResources(user, params)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(0)));

            versionResourceId1 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId2 = createAndDeleteResource(user, resourceTypeCode, params);
            versionResourceId3 = createAndDeleteResource(user, resourceTypeCode, params);

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

        } finally {
            if (versionResourceId3 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId3);
            }
            if (versionResourceId2 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId2);
            }
            if (versionResourceId1 != null) {
                trashedResourceManager.removeFromTrash(versionResourceId1);
            }
        }
    }

    private String createAndDeleteResource(UserDetails user, String resourceTypeCode, Map<String, String> params) throws Exception {
        ResultActions result = performCreateResource(user, "file", "free", "application/pdf");
        String bodyResult = result.andReturn().getResponse().getContentAsString();
        String resourceId = JsonPath.read(bodyResult, "$.payload.id");

        performDeleteResource(user, resourceTypeCode, resourceId)
                .andExpect(status().isOk());

        result = listTrashedResources(user, params)
                .andExpect(status().isOk());
        bodyResult = result.andReturn().getResponse().getContentAsString();

        return JsonPath.read(bodyResult, "$.payload[0].id");
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

}
