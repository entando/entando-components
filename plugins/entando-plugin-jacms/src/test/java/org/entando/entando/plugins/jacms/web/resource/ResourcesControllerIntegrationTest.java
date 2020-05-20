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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.entando.entando.plugins.jacms.web.resource.request.CreateResourceRequest;
import org.entando.entando.plugins.jacms.web.resource.request.UpdateResourceRequest;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

public class ResourcesControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private ICategoryManager categoryManager;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testListImagesUnauthorized() throws Exception {
        performGetResources(null, "image", null)
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testListImagesWithoutFilter() throws Exception {
        UserDetails user = createAccessToken();

        performGetResources(user, "image", null)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(3)))
            .andExpect(jsonPath("$.payload[0].versions.size()", is(4)))
            .andExpect(jsonPath("$.payload[1].versions.size()", is(4)))
            .andExpect(jsonPath("$.payload[2].versions.size()", is(4)));
    }

    @Test
    public void testListFilesWithoutFilter() throws Exception {
        UserDetails user = createAccessToken();

        performGetResources(user, "file", null)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(3)));
    }

    @Test
    public void testListAllTypesWithoutFilter() throws Exception {
        UserDetails user = createAccessToken();

        performGetResources(user, null, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(6)));
    }

    @Test
    public void testFilterImagesByPage() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("page", "2");
        params.put("pageSize", "2");

        performGetResources(user, "image", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0].versions.size()", is(4)));
    }

    @Test
    public void testFilterFilesByPage() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("page", "2");
        params.put("pageSize", "2");

        performGetResources(user, "file", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)));
    }

    @Test
    public void testFilterImagesByGroup() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "group");
        params.put("filters[0].value", "customers");

        performGetResources(user, "image", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0].versions.size()", is(4)))
            .andExpect(jsonPath("$.payload.[0].id", is("82")));
    }

    @Test
    public void testFilterFilesByGroup() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "group");
        params.put("filters[0].value", "customers");

        performGetResources(user, "file", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload.[0].id", is("8")));
    }

    @Test
    public void testSortByGroupAsc() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("sort", "group");
        params.put("direction", FieldSearchFilter.ASC_ORDER);

        performGetResources(user, "file", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)))
                .andExpect(jsonPath("$.payload.[0].id", is("8")));
    }

    @Test
    public void testSortByGroupDesc() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("sort", "group");
        params.put("direction", FieldSearchFilter.DESC_ORDER);

        performGetResources(user, "file", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(3)))
                .andExpect(jsonPath("$.payload.[2].id", is("8")));
    }

    @Test
    public void testFilterImagesByCategories() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat1");
        params.put("filters[1].attribute", "categories");
        params.put("filters[1].value", "resCat3");

        performGetResources(user, "image", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0].versions.size()", is(4)));
    }

    @Test
    public void testFilterFilesByCategories() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat2");

        performGetResources(user, "file", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(1)))
            .andExpect(jsonPath("$.payload[0].id", is("8")));
    }

    @Test
    public void testFilterImagesByCategoriesShouldFindNone() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat2");

        performGetResources(user, "image", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(0)));
    }

    @Test
    public void testFilterFilesByCategoriesShouldFindNone() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "categories");
        params.put("filters[0].value", "resCat1");

        performGetResources(user, "file", params)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(0)));
    }

    @Test
    public void testFilterResourcesByGroupAndCategories() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "group");
        params.put("filters[0].value", "free");
        params.put("filters[1].attribute", "categories");
        params.put("filters[1].value", "resCat1");
        params.put("filters[2].attribute", "categories");
        params.put("filters[2].value", "resCat3");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload.[0].id", is("44")))
                .andExpect(jsonPath("$.payload.[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByOwner() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();
        params.put("filters[0].attribute", "owner");
        params.put("filters[0].value", "rocky");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("22")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByCreatedAt() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();

        params.put("filters[0].attribute", "createdAt");
        params.put("filters[0].value", "2011-01-01 01:00:00");
        params.put("filters[0].operator", "gt");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("82")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByCreatedAt2() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();

        params.put("filters[0].attribute", "createdAt");
        params.put("filters[0].value", "2009-01-01-00.00.00");
        params.put("filters[0].operator", "lt");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("22")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByCreatedAt3() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();

        params.put("filters[0].attribute", "createdAt");
        params.put("filters[0].value", "2009-01-01 01:00:00");
        params.put("filters[0].operator", "gt");

        params.put("filters[1].attribute", "createdAt");
        params.put("filters[1].value", "2011-01-01 01:00:00");
        params.put("filters[1].operator", "lt");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("44")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByUpdatedAt() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();

        params.put("filters[0].attribute", "updatedAt");
        params.put("filters[0].value", "2015-01-01 01:00:00");
        params.put("filters[0].operator", "gt");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(1)))
                .andExpect(jsonPath("$.payload[0].id", is("22")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByUpdatedAt2() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();

        params.put("filters[0].attribute", "updatedAt");
        params.put("filters[0].value", "2015-11-25 19:19:00");
        params.put("filters[0].operator", "lt");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)))
                .andExpect(jsonPath("$.payload[0].id", is("82")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testFilterResourceByUpdatedAt3() throws Exception {
        UserDetails user = createAccessToken();
        Map<String,String> params = new HashMap<>();

        params.put("filters[0].attribute", "updatedAt");
        params.put("filters[0].value", "2013-01-01 01:00:00");
        params.put("filters[0].operator", "gt");

        params.put("filters[1].attribute", "updatedAt");
        params.put("filters[1].value", "2017-01-01 01:00:00");
        params.put("filters[1].operator", "lt");

        performGetResources(user, "image", params)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(2)))
                .andExpect(jsonPath("$.payload[0].id", is("82")))
                .andExpect(jsonPath("$.payload[0].type", is("image")));
    }

    @Test
    public void testCreateEditDeleteImageResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList()), "application/jpeg")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResources(user, "image", null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            performEditResource(user, "image", createdId, "new image description", categories, true)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(createdId)))
                .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                .andExpect(jsonPath("$.payload.description", is("new image description")))
                .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")));
                //.andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));
        } finally {
            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                    .andDo(print())
                    .andExpect(status().isOk());

                performGetResources(user, "image", null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateEditDeleteImageResourceWithoutCategory() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = performCreateResource(user, "image", "free", null, "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(0)))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performEditResource(user, "image", createdId, "new image description", null, true)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", is(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(0)))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("new image description")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")));
                    //.andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));
        } finally {
            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateEditDeleteFileResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList()), "application/pdf")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResources(user, "file", null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            performEditResource(user, "file", createdId, "new file description", categories, true)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(createdId)))
                .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("new file description")))
                .andExpect(jsonPath("$.payload.size", is("2 Kb")));
                //.andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));
        } finally {
            if (createdId != null) {
                performDeleteResource(user, "file", createdId)
                    .andDo(print())
                    .andExpect(status().isOk());

                performGetResources(user, "file", null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateEditWithoutFileDeleteImageResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList()), "application/jpeg")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResources(user, "image", null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            performEditResource(user, "image", createdId, "new image description", categories, false)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(createdId)))
                .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("new image description")))
                .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")));
                //.andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));
        } finally {
            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                    .andDo(print())
                    .andExpect(status().isOk());

                performGetResources(user, "image", null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateEditWithoutFileDeleteFileResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {
            ResultActions result = performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList()), "application/pdf")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResources(user, "file", null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.size()", is(4)));

            List<String> categories = Arrays.stream(new String[]{"resCat1"}).collect(Collectors.toList());

            performEditResource(user, "file", createdId, "new file description", categories, false)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(createdId)))
                .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                .andExpect(jsonPath("$.payload.group", is("free")))
                .andExpect(jsonPath("$.payload.description", is("new file description")))
                .andExpect(jsonPath("$.payload.size", is("2 Kb")));
                //.andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));
        } finally {
            if (createdId != null) {
                performDeleteResource(user, "file", createdId)
                    .andDo(print())
                    .andExpect(status().isOk());

                performGetResources(user, "file", null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateImageResourceWithInvalidMimeType() throws Exception {
        UserDetails user = createAccessToken();
        performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf")
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.size()", is(1)))
            .andExpect(jsonPath("$.errors[0].code", is("4")))
            .andExpect(jsonPath("$.errors[0].message", is("File type not allowed")));

        performGetResources(user, "image", null)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.payload.size()", is(3)));
    }

    @Test
    public void testCreateFileResourceWithInvalidMimeType() throws Exception {
        UserDetails user = createAccessToken();
        performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg")
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.size()", is(1)))
            .andExpect(jsonPath("$.errors[0].code", is("4")))
            .andExpect(jsonPath("$.errors[0].message", is("File type not allowed")));

        performGetResources(user, "file", null)
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testCreateCloneDeleteFileResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;
        String clonedId = null;

        try {
            ResultActions result = performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList()), "application/pdf")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result2 = performCloneResource(user, createdId)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", not(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            clonedId = JsonPath.read(result2.andReturn().getResponse().getContentAsString(), "$.payload.id");

        } finally {

            if (clonedId != null) {
                performDeleteResource(user, "file", clonedId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "file", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(4)));
            }

            if (createdId != null) {
                performDeleteResource(user, "file", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "file", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateCloneDeleteImageResource() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;
        String clonedId = null;

        try {
            ResultActions result = performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            ResultActions result2 = performCloneResource(user, createdId)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", not(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            clonedId = JsonPath.read(result2.andReturn().getResponse().getContentAsString(), "$.payload.id");

        } finally {

            if (clonedId != null) {
                performDeleteResource(user, "image", clonedId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(4)));
            }

            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testEditResourceNotFound() throws Exception {
        UserDetails user = createAccessToken();

        performEditResource(user, "file", "999999", "new file description", null, false)
            .andDo(print())
            .andExpect(status().isNotFound());

        performEditResource(user, "image", "999999", "new file description", null, false)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteResourceNotFound() throws Exception {
        UserDetails user = createAccessToken();

        performDeleteResource(user, "file", "99999")
                .andDo(print())
                .andExpect(status().isNotFound());

        performDeleteResource(user, "image", "99999")
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCloneResourceNotFound() throws Exception {
        UserDetails user = createAccessToken();
        performCloneResource(user, "999999")
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterResourcesByCategory() throws Exception {
        String createdId1 = null;
        String createdId2 = null;
        String createdId3 = null;
        String createdId4 = null;
        String createdId5 = null;
        String createdId6 = null;
        String resourceCatCode1 = "resourceCatCode1";
        String resourceCatCode2 = "resourceCatCode2";
        String resourceCatCode3 = "resourceCatCode3";

        UserDetails user = createAccessToken();
        try {

            List<Category> categories = categoryManager.getCategoriesList();

            Category resourceCat1 = new Category();
            resourceCat1.setCode(resourceCatCode1);
            resourceCat1.setParentCode("cat1");
            resourceCat1.setTitle("en", resourceCatCode1);
            categoryManager.addCategory(resourceCat1);

            Category resourceCat2 = new Category();
            resourceCat2.setCode(resourceCatCode2);
            resourceCat2.setParentCode("cat1");
            resourceCat2.setTitle("en", resourceCatCode2);
            categoryManager.addCategory(resourceCat2);

            Category resourceCat3 = new Category();
            resourceCat3.setCode(resourceCatCode3);
            resourceCat3.setParentCode("cat1");
            resourceCat3.setTitle("en", resourceCatCode3);
            categoryManager.addCategory(resourceCat3);

            ResultActions result = performCreateResource(user, "image", "free",
                    Arrays.stream(new String[]{resourceCatCode1}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is(resourceCatCode1)));

            createdId1 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            result = performCreateResource(user, "image", "free",
                    Arrays.stream(new String[]{resourceCatCode2}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is(resourceCatCode2)));

            createdId2 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            result = performCreateResource(user, "image", "free",
                    Arrays.stream(new String[]{resourceCatCode3}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(1)))
                    .andExpect(jsonPath("$.payload.categories[0]", is(resourceCatCode3)));

            createdId3 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            result = performCreateResource(user, "image", "free",
                    Arrays.stream(new String[]{resourceCatCode1, resourceCatCode2}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is(resourceCatCode1)))
                    .andExpect(jsonPath("$.payload.categories[1]", is(resourceCatCode2)));

            createdId4 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            result = performCreateResource(user, "image", "free",
                    Arrays.stream(new String[]{resourceCatCode2, resourceCatCode3}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is(resourceCatCode2)))
                    .andExpect(jsonPath("$.payload.categories[1]", is(resourceCatCode3)));

            createdId5 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            result = performCreateResource(user, "image", "free",
                    Arrays.stream(new String[]{resourceCatCode1, resourceCatCode2, resourceCatCode3}).collect(Collectors.toList()), "application/jpeg")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(3)))
                    .andExpect(jsonPath("$.payload.categories[0]", is(resourceCatCode1)))
                    .andExpect(jsonPath("$.payload.categories[1]", is(resourceCatCode2)))
                    .andExpect(jsonPath("$.payload.categories[2]", is(resourceCatCode3)));

            createdId6 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            Map<String,String> params = new HashMap<>();
            params.put("filters[0].attribute", "categories");
            params.put("filters[0].value", resourceCatCode1);
            params.put("page", "1");
            params.put("pageSize", "10");

            performGetResources(user, "image", params)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)));

            params = new HashMap<>();
            params.put("filters[0].attribute", "categories");
            params.put("filters[0].value", resourceCatCode2);
            params.put("page", "1");
            params.put("pageSize", "10");

            performGetResources(user, "image", params)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(4)));

            params = new HashMap<>();
            params.put("filters[0].attribute", "categories");
            params.put("filters[0].value", resourceCatCode3);
            params.put("page", "1");
            params.put("pageSize", "10");

            performGetResources(user, "image", params)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(3)));

        } finally {

            if (createdId1 != null) {
                performDeleteResource(user, "image", createdId1)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(8)));
            }

            if (createdId2 != null) {
                performDeleteResource(user, "image", createdId2)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(7)));
            }

            if (createdId3 != null) {
                performDeleteResource(user, "image", createdId3)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(6)));
            }

            if (createdId4 != null) {
                performDeleteResource(user, "image", createdId4)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(5)));
            }

            if (createdId5 != null) {
                performDeleteResource(user, "image", createdId5)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(4)));
            }

            if (createdId6 != null) {
                performDeleteResource(user, "image", createdId6)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }

            categoryManager.deleteCategory(resourceCatCode1);
            categoryManager.deleteCategory(resourceCatCode2);
            categoryManager.deleteCategory(resourceCatCode3);
        }
    }

    @Test
    public void testCreateDeleteImageResourceWithPath() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {

            String type = "image";
            String group = "free";
            String folderPath = "abc";
            List<String> categories = Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList());
            String mimeType = "application/jpeg";

            ResultActions result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

        } finally {

            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateEditDeleteImageResourceWithPath() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;

        try {

            String type = "image";
            String group = "free";
            String folderPath = "abc";
            List<String> categories = Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList());
            String mimeType = "application/jpeg";

            ResultActions result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is("abc")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performEditResource(user, type, createdId, "new file description", categories, true, "abcd")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("new file description")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is("abcd")))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

        } finally {

            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testCreateCloneDeleteImageResourceWithPath() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;
        String clonedId = null;

        try {

            String type = "image";
            String group = "free";
            String folderPath = "folderPath123";
            List<String> categories = Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList());
            String mimeType = "application/jpeg";

            ResultActions result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            result = performCloneResource(user, createdId)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", not(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            clonedId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResourcesFolder(user, folderPath)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(2)))
                    .andExpect(jsonPath("$.metaData.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(0)));

            performGetResourcesFolder(user, null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(6)))
                    .andExpect(jsonPath("$.metaData.folderPath", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("folderPath123")));

        } finally {

            if (clonedId != null) {
                performDeleteResource(user, "image", clonedId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(4)));
            }

            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    @Test
    public void testSubfoldersOnListAssetsFolderPath() throws Exception {
        UserDetails user = createAccessToken();
        String createdId = null;
        String createdId2 = null;
        String createdId3 = null;
        String createdId4 = null;
        String createdId5 = null;

        try {

            String type = "image";
            String group = "free";
            String folderPath = null;
            List<String> categories = Arrays.stream(new String[]{"resCat1", "resCat2"}).collect(Collectors.toList());
            String mimeType = "application/jpeg";

            ResultActions result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResourcesFolder(user, folderPath)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(7)))
                    .andExpect(jsonPath("$.metaData.folderPath", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(0)));

            folderPath = "abc";

            result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", not(createdId)))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("image_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.versions.size()", is(4)))
                    .andExpect(jsonPath("$.payload.versions[0].size", is("2 Kb")))
                    .andExpect(jsonPath("$.payload.versions[0].path", startsWith("/Entando/resources/cms/images/image_test")));

            createdId2 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResourcesFolder(user, folderPath)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(0)));

            performGetResourcesFolder(user, null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(7)))
                    .andExpect(jsonPath("$.metaData.folderPath", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc")));

            type = "file";
            folderPath = "abc/def";
            mimeType = "application/pdf";

            result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            createdId3 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResourcesFolder(user, folderPath)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(0)));

            performGetResourcesFolder(user, null)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(7)))
                    .andExpect(jsonPath("$.metaData.folderPath", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc")));

            performGetResourcesFolder(user, "/abc")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is("abc")))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc/def")));

            folderPath = "abc/ghi";

            result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            createdId4 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResourcesFolder(user, folderPath)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(0)));

            performGetResourcesFolder(user, " ")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(7)))
                    .andExpect(jsonPath("$.metaData.folderPath", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc")));

            performGetResourcesFolder(user, "abc///")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is("abc")))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(2)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc/def")))
                    .andExpect(jsonPath("$.metaData.subfolders[1]", is("abc/ghi")));

            folderPath = "abc/def/ghi";

            result = performCreateResource(user, type, group, categories, folderPath, mimeType)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.id", Matchers.anything()))
                    .andExpect(jsonPath("$.payload.categories.size()", is(2)))
                    .andExpect(jsonPath("$.payload.categories[0]", is("resCat1")))
                    .andExpect(jsonPath("$.payload.categories[1]", is("resCat2")))
                    .andExpect(jsonPath("$.payload.group", is("free")))
                    .andExpect(jsonPath("$.payload.description", is("file_test.jpeg")))
                    .andExpect(jsonPath("$.payload.owner", is("jack_bauer")))
                    .andExpect(jsonPath("$.payload.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.payload.path", startsWith("/Entando/resources/cms/documents/file_test")));

            createdId5 = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.payload.id");

            performGetResourcesFolder(user, folderPath)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is(folderPath)))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(0)));

            performGetResourcesFolder(user, "/")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(7)))
                    .andExpect(jsonPath("$.metaData.folderPath", isEmptyOrNullString()))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc")));

            performGetResourcesFolder(user, "abc")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is("abc")))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(2)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc/def")))
                    .andExpect(jsonPath("$.metaData.subfolders[1]", is("abc/ghi")));

            performGetResourcesFolder(user, "abc/////def")
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.folderPath", is("abc/def")))
                    .andExpect(jsonPath("$.metaData.subfolders.size()", is(1)))
                    .andExpect(jsonPath("$.metaData.subfolders[0]", is("abc/def/ghi")));

        } finally {

            if (createdId5 != null) {
                performDeleteResource(user, "file", createdId5)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "file", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(5)));
            }

            if (createdId4 != null) {
                performDeleteResource(user, "file", createdId4)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "file", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(4)));
            }

            if (createdId3 != null) {
                performDeleteResource(user, "file", createdId3)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "file", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }

            if (createdId2 != null) {
                performDeleteResource(user, "image", createdId2)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(4)));
            }

            if (createdId != null) {
                performDeleteResource(user, "image", createdId)
                        .andDo(print())
                        .andExpect(status().isOk());

                performGetResources(user, "image", null)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.payload.size()", is(3)));
            }
        }
    }

    /* Auxiliary methods */

    private UserDetails createAccessToken() throws Exception {
        return new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withAuthorization(Group.FREE_GROUP_NAME, "editor", Permission.CONTENT_EDITOR).grantedToRoleAdmin()
                .build();
    }

    private ResultActions performGetResources(UserDetails user, String type, Map<String,String> params) throws Exception {
        String path = "/plugins/cms/assets";

        if (type != null) {
            path += "?type=" + type;
        }

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

    private ResultActions performGetResourcesFolder(UserDetails user, String folderPath) throws Exception {
        String path = "/plugins/cms/assets/folder";

        if (folderPath != null) {
            path += "?folderPath=" + folderPath;
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

    private ResultActions performCreateResource(UserDetails user, String type, String group, List<String> categories, String folderPath, String mimeType) throws Exception {
        String urlPath = String.format("/plugins/cms/assets", type);

        CreateResourceRequest resourceRequest = new CreateResourceRequest();
        resourceRequest.setType(type);
        resourceRequest.setCategories(categories);
        resourceRequest.setGroup(group);
        resourceRequest.setFolderPath(folderPath);

        if (null == user) {
            return mockMvc.perform(get(urlPath));
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
        if ("image".equals(type)) {
            file = new MockMultipartFile("file", "image_test.jpeg", mimeType, contents.getBytes());
        } else {
            file = new MockMultipartFile("file", "file_test.jpeg", mimeType, contents.getBytes());
        }

        MockHttpServletRequestBuilder request = multipart(urlPath)
                .file(file)
                .param("metadata", MAPPER.writeValueAsString(resourceRequest))
                .header("Authorization", "Bearer " + accessToken);

        return mockMvc.perform(request);
    }

    private ResultActions performEditResource(UserDetails user, String type, String resourceId, String description,
            List<String> categories, boolean useFile) throws Exception {
        return performEditResource(user, type, resourceId, description, categories, useFile, null);
    }

    private ResultActions performEditResource(UserDetails user, String type, String resourceId, String description,
            List<String> categories, boolean useFile, String folderPath) throws Exception {
        String path = String.format("/plugins/cms/assets/%s", resourceId, type);

        UpdateResourceRequest resourceRequest = new UpdateResourceRequest();
        resourceRequest.setDescription(description);
        resourceRequest.setCategories(categories);
        resourceRequest.setFolderPath(folderPath);

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
        request.param("metadata", MAPPER.writeValueAsString(resourceRequest));

        if (user != null) {
            request.header("Authorization", "Bearer " + mockOAuthInterceptor(user));
        }

        if (useFile) {
            request.file(file);
        }

        return mockMvc.perform(request);
    }

    private ResultActions performCreateResource(UserDetails user, String type, String group, List<String> categories, String mimeType) throws Exception {
        return performCreateResource(user, type, group, categories, null, mimeType);
    }

    private ResultActions performCloneResource(UserDetails user, String resourceId) throws Exception {

        String path = String.format("/plugins/cms/assets/%s/clone", resourceId);

        if (null == user) {
            return mockMvc.perform(post(path));
        }

        return mockMvc.perform(post(path)
                .header("Authorization", "Bearer " + mockOAuthInterceptor(user)));
    }

}
