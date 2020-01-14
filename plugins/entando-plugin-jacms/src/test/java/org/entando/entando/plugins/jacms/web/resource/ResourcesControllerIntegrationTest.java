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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourcesControllerIntegrationTest extends AbstractControllerIntegrationTest {

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
            ResultActions result = performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg")
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
            ResultActions result = performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf")
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
            ResultActions result = performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg")
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
            ResultActions result = performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf")
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
            ResultActions result = performCreateResource(user, "file", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/pdf")
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
            ResultActions result = performCreateResource(user, "image", "free", Arrays.stream(new String[]{"resCat1, resCat2"}).collect(Collectors.toList()), "application/jpeg")
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

    /* Auxiliary methods */

    private UserDetails createAccessToken() throws Exception {
        return new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
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
        String path = String.format("/plugins/cms/assets", type);

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

        if (categories != null) {
            request.param("categories", String.join(",", categories));
        }

        return mockMvc.perform(request);
    }

    private ResultActions performEditResource(UserDetails user, String type, String resourceId, String description,
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

        if (categories != null) {
            request.param("categories", String.join(",", categories));
        }

        if (description != null) {
            request.param("description", description);
        }

        return mockMvc.perform(request);
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
