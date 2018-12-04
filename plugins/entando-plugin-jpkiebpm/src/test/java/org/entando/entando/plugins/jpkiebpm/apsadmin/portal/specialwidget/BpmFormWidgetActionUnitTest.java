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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import java.util.Arrays;
import java.util.HashMap;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;

public class BpmFormWidgetActionUnitTest {

    @Mock
    private IKieFormManager formManager;

    @InjectMocks
    private BpmFormWidgetAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initFormManagerMocks();
    }

    @Test
    public void testChangeKnowledgeSource() {
        action.changeKnowledgeSourceForm();
        assertNull(action.getKnowledgeSourcePath());
        assertNull(action.getProcessPath());
        assertNull(action.getOverrides());
    }

    @Test
    public void testChooseKnowledgeSource() {
        action.setKnowledgeSourcePath("default");
        action.chooseKnowledgeSourceForm();
        assertNotNull(action.getKnowledgeSourcePath());
        assertEquals(1, action.getProcess().size());
    }

    @Test
    public void testChangeProcess() {
        action.setKnowledgeSourcePath("default");
        action.changeForm();
        assertNotNull(action.getKnowledgeSourcePath());
        assertNull(action.getProcessPath());
        assertNull(action.getOverrides());
    }

    @Test
    public void testChooseProcess() {
        action.setKnowledgeSourcePath("default");
        action.setProcessPath("process");
        action.chooseForm();
        assertNotNull(action.getKnowledgeSourcePath());
        assertNotNull(action.getProcessPath());
        assertNotNull(action.getOverrides());
    }

    private void initFormManagerMocks() throws Exception {
        KieBpmConfig defaultConfig = new KieBpmConfig();
        HashMap<String, KieBpmConfig> config = new HashMap<>();
        config.put("default", defaultConfig);

        KieProcess process = new KieProcess();

        when(formManager.getKieServerConfigurations()).thenReturn(config);
        when(formManager.getProcessDefinitionsList(any())).thenReturn(Arrays.asList(process));
        when(formManager.getKieServerStatus()).thenReturn(new JSONArray());
    }
}
