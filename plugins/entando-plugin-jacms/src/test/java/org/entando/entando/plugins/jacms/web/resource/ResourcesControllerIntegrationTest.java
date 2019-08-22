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
package org.entando.entando.plugins.jacms.web.resource;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourcesControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testListImagesUnauthorized() throws Exception {
        ResultActions result = this.performGetResources(null, "image", null);

        String result1 = result.andReturn().getResponse().getContentAsString();
        System.out.println(result1);
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void testListImagesWithoutFilter() throws Exception {
        UserDetails user = createAccessToken();

        ResultActions result = this.performGetResources(user, "image", null);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(3)))
            .andExpect(jsonPath("$.payload[0].versions.size()", is(4)))
            .andExpect(jsonPath("$.payload[1].versions.size()", is(4)))
            .andExpect(jsonPath("$.payload[2].versions.size()", is(4)))
            ;
    }

    @Test
    public void testListFilesWithoutFilter() throws Exception {
        UserDetails user = createAccessToken();

        ResultActions result = this.performGetResources(user, "file", null);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)))
        ;
    }

    @Test
    public void testFilterImagesByPage() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("page", "2");
        params.put("pageSize", "2");

        ResultActions result = this.performGetResources(user, "image", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].versions.size()", is(4)))
        ;
    }

    @Test
    public void testFilterFilesByPage() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("page", "2");
        params.put("pageSize", "2");

        ResultActions result = this.performGetResources(user, "file", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
        ;
    }

    @Test
    public void testFilterImagesByGroup() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "group");
        params.put("filters[0].value", "customers");

        ResultActions result = this.performGetResources(user, "image", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].versions.size()", is(4)))
                .andExpect(jsonPath("$.payload.[0].id", is("82")))
        ;
    }

    @Test
    public void testFilterFilesByGroup() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "group");
        params.put("filters[0].value", "customers");

        ResultActions result = this.performGetResources(user, "file", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload.[0].id", is("8")))
        ;
    }

    @Test
    public void testFilterImagesByCategories() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat1");

        ResultActions result = this.performGetResources(user, "image", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0].versions.size()", is(4)))
        ;
    }

    @Test
    public void testFilterFilesByCategories() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat2");

        ResultActions result = this.performGetResources(user, "file", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("8")))
        ;
    }

    @Test
    public void testFilterImagesByCategoriesShouldFindNone() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat2");

        ResultActions result = this.performGetResources(user, "image", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(0)))
        ;
    }

    @Test
    public void testFilterFilesByCategoriesShouldFindNone() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat1");

        ResultActions result = this.performGetResources(user, "file", params);

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(0)))
        ;
    }

    @Test
    public void testCreateEditDeleteImageResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = this.performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg");

            String content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            createdId = JsonPath.read(content, "$.payload.id");

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")))
            ;

            result = this.performGetResources(user, "image", null);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(4)))
            ;

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            result = this.performEditResource(user, "image", createdId, "new image description", "customers", categories, true);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", is(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.group", is("customers")))
                    .andExpect(jsonPath("$.payload.description", is("new image description")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    //.andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")))
            ;
        } finally {
            if (createdId != null) {
                ResultActions result = this.performDeleteResource(user, "image", createdId);

                String content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                ;

                result = this.performGetResources(user, "image", null);

                content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)))
                ;
            }
        }
    }

    @Test
    public void testCreateEditDeleteFileResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = this.performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf");

            String content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);
            createdId = JsonPath.read(content, "$.payload.id");


            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")))
            ;

            result = this.performGetResources(user, "file", null);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(4)))
            ;

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            result = this.performEditResource(user, "file", createdId, "new file description", "customers", categories, true);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", is(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.group", is("customers")))
                    .andExpect(jsonPath("$.payload.description", is("new file description")))
                    .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                    //.andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")))
            ;
        } finally {
            if (createdId != null) {
                ResultActions result = this.performDeleteResource(user, "file", createdId);

                String content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                ;

                result = this.performGetResources(user, "file", null);

                content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)))
                ;
            }
        }
    }

    @Test
    public void testCreateEditWithoutFileDeleteImageResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = this.performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg");

            String content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            createdId = JsonPath.read(content, "$.payload.id");


            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")))
            ;

            result = this.performGetResources(user, "image", null);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(4)))
            ;

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            result = this.performEditResource(user, "image", createdId, "new image description", "customers", categories, false);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", is(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.group", is("customers")))
                    .andExpect(jsonPath("$.payload.description", is("new image description")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    //.andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")))
            ;
        } finally {
            if (createdId != null) {
                ResultActions result = this.performDeleteResource(user, "image", createdId);

                String content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                ;

                result = this.performGetResources(user, "image", null);

                content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)))
                ;
            }
        }
    }

    @Test
    public void testCreateEditWithoutFileDeleteFileResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = this.performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf");

            String content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            createdId = JsonPath.read(content, "$.payload.id");


            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")))
            ;

            result = this.performGetResources(user, "file", null);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(4)))
            ;

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            result = this.performEditResource(user, "file", createdId, "new file description", "customers", categories, false);

            content = result.andReturn().getResponse().getContentAsString();
            System.out.println(content);

            result
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", is(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.group", is("customers")))
                    .andExpect(jsonPath("$.payload.description", is("new file description")))
                    .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                    //.andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")))
            ;
        } finally {
            if (createdId != null) {
                ResultActions result = this.performDeleteResource(user, "file", createdId);

                String content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                ;

                result = this.performGetResources(user, "file", null);

                content = result.andReturn().getResponse().getContentAsString();
                System.out.println(content);

                result
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)))
                ;
            }
        }
    }

    @Test
    public void testCreateImageResourceWithInvalidMimeType() throws Exception {
        UserDetails user = createAccessToken();
        ResultActions result = this.performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf");

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);


        result.andExpect(status().is5xxServerError());

        result = this.performGetResources(user, "image", null);

        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(3)))
        ;
    }

    @Test
    public void testCreateFileResourceWithInvalidMimeType() throws Exception {
        UserDetails user = createAccessToken();
        ResultActions result = this.performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg");

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);


        result.andExpect(status().is5xxServerError());

        result = this.performGetResources(user, "file", null);

        content = result.andReturn().getResponse().getContentAsString();
        System.out.println(content);

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)))
        ;
    }

    /* Auxiliary methods */

    private UserDetails createAccessToken() throws Exception {
        return new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
    }

    private ResultActions performGetResources(UserDetails user, String type, Map<String,String> params) throws Exception {
        String path = "/plugins/cms/assets?type=" + type;
        for (String key : Optional.ofNullable(params).orElse(new HashMap<>()).keySet()) {
            path += String.format("&%s=%s", key, params.get(key));
        }

        if (null == user) {
            return mockMvc.perform(get(path));
        }

        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                get(path)
                    .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performDeleteResource(UserDetails user, String type, String resourceId) throws Exception {
        String path = String.format("/plugins/cms/assets/%s", resourceId);

        if (null == user) {
            return mockMvc.perform(get(path));
        }

        String accessToken = mockOAuthInterceptor(user);
        return mockMvc.perform(
                delete(path)
                    .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performCreateResource(UserDetails user, String type, String group, List<String> categories, String mimeType) throws Exception {
        String path = String.format("/plugins/cms/assets?type=%s", type);

        if (null == user) {
            return mockMvc.perform(get(path));
        }

        String accessToken = mockOAuthInterceptor(user);
        String contents = "some text very big so it has more than 1Kb size asdklasdhadsjakhdsjadjasdhjhjasd some garbage to make it bigger!!!" +
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
        if (type.equals("image")) {
            file = new MockMultipartFile("file", "image_test.jpeg", mimeType, contents.getBytes());
        } else {
            file = new MockMultipartFile("file", "file_test.jpeg", mimeType, contents.getBytes());
        }

        return mockMvc.perform(
                multipart(path)
                    .file(file)
                    .param("group", group)
                    .param("categories", String.join(",", categories))
                    .header("Authorization", "Bearer " + accessToken));
    }

    private ResultActions performEditResource(UserDetails user, String type, String resourceId, String description, String group,
                              List<String> categories, boolean useFile) throws Exception {
        String path = String.format("/plugins/cms/assets/%s", resourceId, type);

        MockMultipartFile file;
        String contents = "some text very big so it has more than 1Kb size asdklasdhadsjakhdsjadjasdhjhjasd some garbage to make it bigger!!!" +
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
        if (type.equals("image")) {
            file = new MockMultipartFile("file", "image_test.jpeg", "application/jpeg", contents.getBytes());
        } else {
            file = new MockMultipartFile("file", "file_test.jpeg", "application/pdf", contents.getBytes());
        }

        MockMultipartHttpServletRequestBuilder request = multipart(path);

        if (user != null) {
            request.header("Authorization", "Bearer " + mockOAuthInterceptor(user));
        }

        if (useFile) {
            request.file(file);
        }

        request
            .param("group", group)
            .param("categories", String.join(",", categories != null ? categories : new ArrayList<>()))
            .param("description", description);

        return mockMvc.perform(request);
    }

}
