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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.*;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;

import java.util.*;
import java.util.stream.Collectors;

public class BpmFormWidgetActionIntegrationTest extends ApsAdminBaseTestCase {

    private IKieFormOverrideManager kieFormOverrideManager;
    private IBpmWidgetInfoManager bpmWidgetInfoManager;
    private IPageManager pageManager;
    private Page temporaryPage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        this.kieFormOverrideManager = (IKieFormOverrideManager) this.getService(IKieFormOverrideManager.BEAN_ID);
        assertNotNull(this.kieFormOverrideManager);
        this.bpmWidgetInfoManager = (IBpmWidgetInfoManager) this.getService("jpkiebpmBpmWidgetInfoManager");
        assertNotNull(this.bpmWidgetInfoManager);
        this.pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        assertNotNull(this.pageManager);
        this.temporaryPage = this.createTemporaryPage("tmp_draft");
    }

    @Override
    protected void tearDown() throws Exception {
        deleteTemporaryPage();
        super.tearDown();
    }

    @Override
    protected BpmFormWidgetAction getAction() {
        return (BpmFormWidgetAction) super.getAction();
    }

    public void testCreateOverridesAndRemoveWidget() throws Throwable {
        this.setUserOnSession("admin");

        // Open the widgetConfiguration
        this.initAction("/do/Page/SpecialWidget", "jpkiebpmBpmFormWidgetViewerConfig");
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-datatype-form");
        this.addParameter("frame", "0");
        assertEquals(Action.SUCCESS, this.executeAction());
        assertEquals(2, getAction().getKnowledgeSource().size());

        // Select Knowledge Source
        thenPrepareAction("chooseKnowledgeSourceForm");
        this.addParameter("knowledgeSourcePath", "1");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        assertEquals("1", getAction().getKnowledgeSourcePath());
        assertNotNull(getAction().getKnowledgeSourcePath());
        assertEquals(1, getAction().getProcess().size());
        assertNull(getAction().getProcessPath());

        // Select Process
        thenPrepareAction("chooseForm");
        this.addParameter("processPath", "process1@container1@1");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        assertEquals("process1@container1@1", getAction().getProcessPath());
        assertTrue(getAction().getOverrides().isEmpty());
        assertEquals(2, getAction().getFields().size());

        // Add override
        thenPrepareAction("addFormOverride");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        assertEquals(1, getAction().getOverrides().size());
        assertEquals("employee", getAction().getFields().get(0).getName());
        assertEquals("reason", getAction().getFields().get(1).getName());

        // Edit override fields and add another override 
        thenPrepareAction("addFormOverride");
        this.addParameter("overrides[0].active", "false");
        this.addParameter("overrides[0].field", "employee");
        this.addParameter("overrides[0].defaultValue", "default1");
        this.addParameter("overrides[0].placeHolderValue", "placeholder1");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        assertEquals(2, getAction().getOverrides().size());
        KieFormOverrideInEditing ovr = getAction().getOverrides().get(0);
        assertEquals("employee", ovr.getField());
        assertEquals("default1", ovr.getDefaultValue());
        assertEquals("placeholder1", ovr.getPlaceHolderValue());
        assertFalse(ovr.isActive());

        // Set second override fields and save widget configuration
        thenPrepareAction("save");
        this.addParameter("overrides[1].active", "true");
        this.addParameter("overrides[1].field", "reason");
        this.addParameter("overrides[1].defaultValue", "default2");
        this.addParameter("overrides[1].placeHolderValue", "placeholder2");
        assertEquals("configure", this.executeActionWithMockedKieServer());
        this.waitNotifyingThread();

        // Reopen the widget settings
        this.initAction("/do/Page/SpecialWidget", "jpkiebpmBpmFormWidgetViewerConfig");
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-datatype-form");
        this.addParameter("frame", "0");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        int widgetInfoId = getWidgetInfoId();
        assertTrue(widgetInfoId > 0);
        List<KieFormOverride> onlineOverrides = this.kieFormOverrideManager.getFormOverrides(widgetInfoId, true);
        List<KieFormOverride> draftOverrides = this.kieFormOverrideManager.getFormOverrides(widgetInfoId, false);
        assertTrue(onlineOverrides.isEmpty());
        assertEquals(2, draftOverrides.size());
        assertEquals(2, draftOverrides.get(0).getOverrides().getList().size());
        assertEquals(2, getAction().getKnowledgeSource().size());
        assertEquals("1", getAction().getKnowledgeSourcePath());
        assertEquals("process1@container1@1", getAction().getProcessPath());
        assertEquals(2, getAction().getOverrides().size());
        KieFormOverrideInEditing ovr1 = getAction().getOverrides().get(0);
        assertEquals(1, (int) ovr1.getId());
        assertEquals("employee", ovr1.getField());
        assertEquals("default1", ovr1.getDefaultValue());
        KieFormOverrideInEditing ovr2 = getAction().getOverrides().get(1);
        assertEquals(2, (int) ovr2.getId());
        assertEquals("reason", ovr2.getField());
        assertEquals("default2", ovr2.getDefaultValue());

        // Delete the first override and edit the second override
        thenPrepareAction("deleteFormOverride");
        this.addParameter("overrideToDeleteIndex", "0");
        this.addParameter("overrides[1].id", "2");
        this.addParameter("overrides[1].active", "false");
        this.addParameter("overrides[1].field", "reason");
        this.addParameter("overrides[1].defaultValue", "default2-MOD");
        this.addParameter("overrides[1].placeHolderValue", "placeholder2");
        this.addParameter("knowledgeSourcePath", "1");
        this.addParameter("processPath", "process1@container1@1");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        assertEquals(1, getAction().getOverrides().size());
        KieFormOverrideInEditing modOvr = getAction().getOverrides().get(0);
        assertEquals(2, (int) modOvr.getId());
        assertEquals("reason", modOvr.getField());
        assertEquals("default2-MOD", modOvr.getDefaultValue());
        assertFalse(modOvr.isActive());

        // Edit the override and save
        // (here we can't reuse old parameters, because overrides list has changed)
        this.initAction("/do/bpm/Page/SpecialWidget/BpmFormViewer", "save");
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-datatype-form");
        this.addParameter("frame", "0");
        this.addParameter("knowledgeSourcePath", "1");
        this.addParameter("processPath", "process1@container1@1");
        this.addParameter("overrides[0].id", "2");
        this.addParameter("overrides[0].active", "false");
        this.addParameter("overrides[0].field", "reason");
        this.addParameter("overrides[0].defaultValue", "default2-MOD");
        this.addParameter("overrides[0].placeHolderValue", "placeholder2-MOD");
        assertEquals("configure", this.executeActionWithMockedKieServer());
        this.waitNotifyingThread();

        // Publish the page
        this.pageManager.setPageOnline(temporaryPage.getCode());
        this.waitNotifyingThread();
        // check online overrides
        onlineOverrides = this.kieFormOverrideManager.getFormOverrides(widgetInfoId, true);
        assertEquals(1, onlineOverrides.size());
        KieFormOverride onlineOverride = onlineOverrides.get(0);
        assertEquals(2, onlineOverride.getOverrides().getList().size());
        assertTrue(onlineOverride.isOnline());
        // check draft overrides
        draftOverrides = this.kieFormOverrideManager.getFormOverrides(widgetInfoId, false);
        assertEquals(1, draftOverrides.size());
        KieFormOverride draftOverride = draftOverrides.get(0);
        assertEquals(2, draftOverride.getOverrides().getList().size());
        assertFalse(draftOverride.isOnline());
        assertNotSame(onlineOverride.getId(), draftOverride.getId());

        // Reopen widget settings and check modifications
        this.initAction("/do/Page/SpecialWidget", "jpkiebpmBpmFormWidgetViewerConfig");
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-datatype-form");
        this.addParameter("frame", "0");
        assertEquals(Action.SUCCESS, this.executeActionWithMockedKieServer());
        assertEquals(widgetInfoId, getWidgetInfoId());
        assertEquals(1, getAction().getOverrides().size());
        ovr = getAction().getOverrides().get(0);
        assertEquals(2, (int) ovr.getId());
        assertEquals("reason", ovr.getField());
        assertEquals("default2-MOD", ovr.getDefaultValue());
        assertEquals("placeholder2-MOD", ovr.getPlaceHolderValue());
        assertFalse(ovr.isActive());
        BpmWidgetInfo widgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(widgetInfoId);
        assertNotNull(widgetInfo.getInformationOnline());
        assertNotNull(widgetInfo.getInformationDraft());

        // Save the draft widget modifying the active flag
        this.initAction("/do/bpm/Page/SpecialWidget/BpmFormViewer", "save");
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-datatype-form");
        this.addParameter("frame", "0");
        this.addParameter("knowledgeSourcePath", "1");
        this.addParameter("processPath", "process1@container1@1");
        this.addParameter("overrides[0].id", "2");
        this.addParameter("overrides[0].active", "true"); // this is modified
        this.addParameter("overrides[0].field", "reason");
        this.addParameter("overrides[0].defaultValue", "default2-MOD");
        this.addParameter("overrides[0].placeHolderValue", "placeholder2-MOD");
        assertEquals("configure", this.executeActionWithMockedKieServer());
        this.waitNotifyingThread();
        onlineOverrides = this.kieFormOverrideManager.getFormOverrides(widgetInfoId, true);
        assertEquals(1, onlineOverrides.size());
        onlineOverride = onlineOverrides.get(0);
        assertFalse(onlineOverride.isActive());
        draftOverrides = this.kieFormOverrideManager.getFormOverrides(widgetInfoId, false);
        assertEquals(1, draftOverrides.size());
        draftOverride = draftOverrides.get(0);
        assertTrue(draftOverride.isActive());
        widgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(widgetInfoId);
        assertNotNull(widgetInfo.getInformationOnline());
        assertNotNull(widgetInfo.getInformationDraft());

        // Restore the published widget
        this.initAction("/do/rs/Page", "restoreOnlineConfig");
        this.addParameter("pageCode", temporaryPage.getCode());
        assertEquals(Action.SUCCESS, this.executeAction());
        onlineOverrides = kieFormOverrideManager.getFormOverrides(widgetInfoId, true);
        draftOverrides = kieFormOverrideManager.getFormOverrides(widgetInfoId, false);
        assertEquals(1, onlineOverrides.size());
        assertEquals(1, draftOverrides.size());
        assertNotNull(widgetInfo.getInformationOnline());
        assertNotNull(widgetInfo.getInformationDraft());

        // Delete the widget (this must delete the related draft overrides)
        assertNotNull(bpmWidgetInfoManager.getBpmWidgetInfo(widgetInfoId));
        assertEquals(1, kieFormOverrideManager.getFormOverrides(widgetInfoId, true).size());
        this.initAction("/do/rs/Page", "deleteWidget");
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("frame", "0");
        assertEquals(Action.SUCCESS, this.executeAction());
        this.waitNotifyingThread();
        widgetInfo = bpmWidgetInfoManager.getBpmWidgetInfo(widgetInfoId);
        assertNotNull(widgetInfo);
        assertNull(widgetInfo.getInformationDraft());
        assertTrue(kieFormOverrideManager.getFormOverrides(widgetInfoId, false).isEmpty());
        assertEquals(1, kieFormOverrideManager.getFormOverrides(widgetInfoId, true).size());

        // Publish the page (this must delete the related online overrides)
        this.pageManager.setPageOnline(temporaryPage.getCode());
        this.waitNotifyingThread();
        assertTrue(kieFormOverrideManager.getFormOverrides(widgetInfoId, true).isEmpty());
    }

    private int getWidgetInfoId() {
        return Integer.parseInt(getAction().getWidget().getConfig().getProperty(KieBpmSystemConstants.WIDGET_PARAM_INFO_ID));
    }

    /**
     * Initialize a new action keeping the parameters of the previous action.
     */
    private void thenPrepareAction(String actionName) throws Throwable {
        Map<String, String> previousParams = super.getActionContext().getParameters().values()
                .stream().collect(Collectors.toMap(p -> p.getName(), p -> p.getValue()));
        this.initAction("/do/bpm/Page/SpecialWidget/BpmFormViewer", actionName);
        previousParams.forEach((key, value) -> this.addParameter(key, value));
    }

    private String executeActionWithMockedKieServer() throws Throwable {
        IKieFormManager formManager = getAction().getFormManager();
        IKieFormManager formManagerSpy = SpyKieFormManagerUtil.getSpiedKieFormManager(formManager);
        getAction().setFormManager(formManagerSpy);
        return this.executeAction();
    }

    private Page createTemporaryPage(String pageCode) throws Exception {
        IPage root = this.pageManager.getDraftRoot();

        Page page = new Page();
        page.setCode(pageCode);
        page.setTitle("en", pageCode);
        page.setTitle("it", pageCode);
        page.setParentCode(root.getCode());
        page.setGroup(root.getGroup());

        PageMetadata pageMetadata = new PageMetadata();
        pageMetadata.setMimeType("text/html");
        pageMetadata.setModel(root.getModel());
        pageMetadata.setTitles(page.getTitles().clone());
        pageMetadata.setGroup(page.getGroup());
        page.setMetadata(pageMetadata);
       
        page.setWidgets(new Widget[root.getModel().getFrames().length]);
        this.pageManager.addPage(page);
        return page;
    }

    private void deleteTemporaryPage() throws Exception {
        if (temporaryPage != null) {
            this.pageManager.deletePage(temporaryPage.getCode());
        }
    }
}
