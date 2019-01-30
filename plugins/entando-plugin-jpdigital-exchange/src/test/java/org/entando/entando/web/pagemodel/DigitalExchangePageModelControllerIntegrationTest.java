/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.PageModelManager;
import com.agiletec.aps.system.services.user.User;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DigitalExchangePageModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String USERNAME = "jack_bauer";
    private static final String PASSWORD = "0x24";
    private static final String PAGE_MODEL_CODE = "testPM";

    private String accessToken;

    @Autowired
    private PageModelManager pageModelManager;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        setupAuthenticationDetails();
        deletePageModelsFromPreviousTests();
    }

    private void setupAuthenticationDetails() {
        User user = new OAuth2TestUtils.UserBuilder(USERNAME, PASSWORD)
                .grantedToRoleAdmin()
                .build();

        accessToken = mockOAuthInterceptor(user);
    }

    private void deletePageModelsFromPreviousTests() throws ApsSystemException {
        pageModelManager.deletePageModel(PAGE_MODEL_CODE);
    }


    @Test public void
    get_all_page_models_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }
}
