package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.*;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.*;

public class BpmBpmCaseInstanceSelectorWidgetActionIntegrationTest extends ApsAdminBaseTestCase {

    private IPageManager pageManager;
    private Page temporaryPage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        this.pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        this.temporaryPage = createTemporaryPage("tmp_page");
    }

    @Override
    protected void tearDown() throws Exception {
        deleteTemporaryPage();
        super.tearDown();
    }

    public void testSaveWidget() throws Throwable {
        this.setUserOnSession("admin");

        // Select Knowledge Source
        this.initAction("/do/bpm/Page/SpecialWidget/BpmCaseInstanceSelectorViewer", "chooseKnowledgeSourceForm"); // jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-case-instance-selector");
        this.addParameter("frame", "0");
        this.addParameter("knowledgeSourcePath", "1");
        assertEquals(Action.SUCCESS, executeActionUsingFakeKieServer());

        // Change Deployment Unit
        this.initAction("/do/bpm/Page/SpecialWidget/BpmCaseInstanceSelectorViewer", "changeForm"); // jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-case-instance-selector");
        this.addParameter("frame", "0");
        this.addParameter("knowledgeSourcePath", "1");
        this.addParameter("processPath", "container1");
        assertEquals(Action.SUCCESS, executeActionUsingFakeKieServer());
        String frontEndCaseData = getAction().getFrontEndCaseData();
        assertNotNull(frontEndCaseData);

        // Save the configuration
        this.initAction("/do/bpm/Page/SpecialWidget/BpmCaseInstanceSelectorViewer", "save"); // jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-case-instance-selector");
        this.addParameter("frame", "0");
        this.addParameter("knowledgeSourcePath", "1");
        this.addParameter("processPath", "container1");
        this.addParameter("channel", "1");
        this.addParameter("frontEndCaseData", frontEndCaseData);
        assertEquals("configure", executeActionUsingFakeKieServer());

        // Re-open the saved configuration
        this.initAction("/do/Page/SpecialWidget", "jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig"); // jpkiebpmBpmCaseInstanceSelectorWidgetViewerConfig
        this.addParameter("pageCode", temporaryPage.getCode());
        this.addParameter("widgetTypeCode", "bpm-case-instance-selector");
        this.addParameter("frame", "0");
        this.addParameter("knowledgeSourcePath", "1");
        assertEquals(Action.SUCCESS, executeActionUsingFakeKieServer());
        assertNotNull(getAction().getFrontEndCaseData());
    }

    private String executeActionUsingFakeKieServer() throws Throwable {
        IKieFormManager formManager = getAction().getFormManager();
        IKieFormManager formManagerSpy = SpyKieFormManagerUtil.getSpiedKieFormManager(formManager);
        getAction().setFormManager(formManagerSpy);
        return this.executeAction();
    }

    @Override
    protected BpmBpmCaseInstanceSelectorWidgetAction getAction() {
        return (BpmBpmCaseInstanceSelectorWidgetAction) super.getAction();
    }

    private Page createTemporaryPage(String pageCode) throws Exception {
        IPage root = this.pageManager.getDraftRoot();

        Page page = new Page();
        page.setCode(pageCode);
        page.setTitle("en", pageCode);
        page.setTitle("it", pageCode);
        page.setParent(root);
        page.setParentCode(root.getCode());
        page.setGroup(root.getGroup());

        PageMetadata pageMetadata = new PageMetadata();
        pageMetadata.setMimeType("text/html");
        pageMetadata.setModel(root.getModel());
        pageMetadata.setTitles(page.getTitles());
        pageMetadata.setGroup(page.getGroup());
        page.setMetadata(pageMetadata);

        this.pageManager.addPage(page);
        return page;
    }

    private void deleteTemporaryPage() throws Exception {
        if (temporaryPage != null) {
            this.pageManager.deletePage(temporaryPage.getCode());
        }
    }
}
