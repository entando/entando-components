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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.opensymphony.xwork2.Action;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertTrue;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpkiebpm.ActionTestHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfoBuilder;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.IBpmWidgetInfoManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcess;
import org.mockito.Spy;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverrideInEditing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BpmFormWidgetActionTest extends BpmSourceAndProcessSelectorTest<BpmFormWidgetAction> {

    @Mock
    private IKieFormManager formManager;

    @Mock
    private IPageManager pageManager;

    @Mock
    private IBpmWidgetInfoManager bpmWidgetInfoManager;

    @Mock
    private IKieFormOverrideManager formOverrideManager;

    @InjectMocks
    @Spy
    private BpmFormWidgetAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ActionTestHelper.initActionMocks(action);
        super.setAction(action, formManager);
    }

    @Test
    public void testAddOverride() {
        action.setKnowledgeSourcePath("default");
        action.setProcessPath("process");
        assertEquals(Action.SUCCESS, action.addFormOverride());
        assertNotNull(action.getKnowledgeSource());
        assertNotNull(action.getProcess());
        assertNotNull(action.getOverrides());
        assertEquals(1, action.getOverrides().size());
    }

    @Test
    public void testDeleteOverride() {
        action.setKnowledgeSourcePath("default");
        action.setProcessPath("process");
        List<KieFormOverrideInEditing> overrides = new ArrayList<>();
        overrides.add(new KieFormOverrideInEditing());
        action.setOverrides(overrides);
        action.setOverrideToDeleteIndex(0);
        assertEquals(Action.SUCCESS, action.deleteFormOverride());
        assertNotNull(action.getKnowledgeSource());
        assertNotNull(action.getProcess());
        assertNotNull(action.getOverrides());
        assertTrue(action.getOverrides().isEmpty());
    }

    @Override
    protected void verifyAllUnset() {
        super.verifyAllUnset();
        assertNull(action.getOverrides());
    }

    @Override
    protected void mockForInit() throws ApsSystemException {

        Widget widget = new Widget();
        WidgetType widgetType = new WidgetType();
        widgetType.setCode("bpm-start-new-process-form");
        widget.setType(widgetType);

        ApsProperties widgetConfig = new ApsProperties();
        widgetConfig.setProperty(WIDGET_PARAM_INFO_ID, "1");
        widget.setConfig(widgetConfig);

        BpmWidgetInfo widgetInfo = new BpmWidgetInfoBuilder()
                .setConfigDraftProperty(WIDGET_INFO_PROP_PROCESS_ID, "process")
                .setConfigDraftProperty(WIDGET_INFO_PROP_KIE_SOURCE_ID, "unexisting-source")
                .setConfigDraftProperty(WIDGET_INFO_PROP_CONTAINER_ID, "container")
                .build();

        when(bpmWidgetInfoManager.getBpmWidgetInfo(1)).thenReturn(widgetInfo);

        when(formOverrideManager.getFormOverrides(1, false)).thenReturn(new ArrayList<>());

        Page page = new Page();
        page.setWidgets(new Widget[]{widget});
        when(pageManager.getDraftPage(any())).thenReturn(page);
    }

    @Override
    protected void initFormManagerMocks() throws Exception {
        super.initFormManagerMocks();

        when(formManager.getProcessDefinitionsList(any(KieBpmConfig.class)))
                .thenAnswer(getPAMInstanceAnswer(new KieProcess()));
    }
}
