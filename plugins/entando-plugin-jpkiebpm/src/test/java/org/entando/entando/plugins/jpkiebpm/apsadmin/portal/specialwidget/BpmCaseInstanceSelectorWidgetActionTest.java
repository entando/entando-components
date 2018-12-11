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
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpkiebpm.ActionTestHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieContainer;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class BpmCaseInstanceSelectorWidgetActionTest extends BpmSourceAndProcessSelectorTest<BpmBpmCaseInstanceSelectorWidgetAction> {

    @Mock
    private IKieFormManager formManager;

    @Mock
    private IPageManager pageManager;

    @Spy
    @InjectMocks
    private BpmBpmCaseInstanceSelectorWidgetAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ActionTestHelper.initActionMocks(action);
        super.setAction(action, formManager);
    }

    @Override
    protected void mockForInit() throws ApsSystemException {
        Widget widget = new Widget();
        WidgetType widgetType = new WidgetType();
        widgetType.setCode("bpm-case-instance-selector");
        widget.setType(widgetType);

        ApsProperties widgetConfig = new ApsProperties();
        widgetConfig.setProperty("frontEndCaseData",
                "{\"container-id\":\"containerId\",\"knowledge-source-id\":\"unexisting-source\"}");
        widgetConfig.setProperty("channel", "1");
        widget.setConfig(widgetConfig);

        Page page = new Page();
        page.setWidgets(new Widget[]{widget});
        when(pageManager.getDraftPage(any())).thenReturn(page);
    }

    @Override
    protected void initFormManagerMocks() throws Exception {
        super.initFormManagerMocks();

        when(formManager.getContainersList(any(KieBpmConfig.class)))
                .thenAnswer(getPAMInstanceAnswer(new KieContainer()));
    }
}
