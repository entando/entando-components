/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.web.page;

import com.agiletec.aps.system.services.page.PagesStatus;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.entity.ContentType;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.PageServiceIntegrationTest;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author paddeo
 */
public class PageControllerIntegrationTest extends AbstractControllerIntegrationTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private IPageService pageService;

    @Test
    public void testPageGet() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/{code}", "pagina_11")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.code", is("pagina_11")));
        result.andExpect(jsonPath("$.payload.references.length()", is(1)));
    }

    @Test
    public void testPageGetReferences() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/pages/{code}/references/{manager}", "pagina_11", "jacmsContentManager")
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.totalItems", is(2)));
    }

    @Test
    public void testPageAddUpdateDelete() throws Exception {
        String pageCode = "page_update_test";
        String widgetCode = "content_viewer";

        PageDto pageToClone = pageService.getPage("pagina_11", IPageService.STATUS_DRAFT);

        PageRequest pageRequest = PageServiceIntegrationTest.createRequestFromDto(pageToClone);
        pageRequest.setCode(pageCode);
        pageRequest.setStatus(IPageService.STATUS_DRAFT);

        WidgetConfigurationRequest widgetRequest = new WidgetConfigurationRequest();
        widgetRequest.setCode(widgetCode);
        widgetRequest.setConfig(new HashMap<>());

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        try {
            mockMvc.perform(post("/pages", pageCode)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MAPPER.writeValueAsString(pageRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.numWidget", is(0)));

            mockMvc.perform(put("/pages/{code}/widgets/{frame}", pageCode, 0)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MAPPER.writeValueAsString(widgetRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());

            mockMvc.perform(put("/pages/{code}", pageCode)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MAPPER.writeValueAsString(pageRequest)))
                    .andDo(print()).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.code", is(pageCode)))
                    .andExpect(jsonPath("$.payload.numWidget", is(1)));
        } finally {
            mockMvc.perform(delete("/pages/{code}", pageCode)
                    .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk());
        }
    }

}
