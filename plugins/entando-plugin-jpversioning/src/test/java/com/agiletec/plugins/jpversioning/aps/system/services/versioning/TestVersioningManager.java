/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.aps.system.services.group.Group;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpversioning.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpversioning.util.JpversioningTestHelper;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpversioning.aps.system.JpversioningSystemConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * @author G.Cocco
 */
public class TestVersioningManager extends ApsPluginBaseTestCase {

    private ConfigInterface configManager;
    private IContentManager contentManager;
    private IVersioningManager versioningManager;
    private JpversioningTestHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this.helper.initContentVersions();
    }

    @Override
    protected void tearDown() throws Exception {
        this.helper.cleanContentVersions();
        super.tearDown();
    }

    public void testGetVersions() throws Throwable {
        List<Long> versions = this.versioningManager.getVersions("CNG12");
        assertNull(versions);

        versions = this.versioningManager.getVersions("ART1");
        this.checkVersionIds(new long[]{1, 2, 3}, versions);
    }

    public void testGetLastVersions() throws Throwable {
        List<Long> versions = this.versioningManager.getLastVersions("CNG", null);
        assertTrue(versions.isEmpty());

        versions = this.versioningManager.getLastVersions("ART", null);
        this.checkVersionIds(new long[]{3}, versions);
    }

    public void testGetVersion() throws Throwable {
        ContentVersion contentVersion = this.versioningManager.getVersion(10000);
        assertNull(contentVersion);
        long id = 1;
        contentVersion = this.versioningManager.getVersion(id);
        assertEquals(id, contentVersion.getId());
        assertEquals("ART1", contentVersion.getContentId());
        assertEquals("ART", contentVersion.getContentType());
        assertEquals("Articolo", contentVersion.getDescr());
        assertEquals(Content.STATUS_DRAFT, contentVersion.getStatus());
        assertNotNull(contentVersion.getXml());
        assertEquals("13/02/2005", DateConverter.getFormattedDate(contentVersion.getVersionDate(), "dd/MM/yyyy"));
        assertEquals("0.0", contentVersion.getVersion());
        assertEquals(0, contentVersion.getOnlineVersion());
        assertTrue(contentVersion.isApproved());
        assertEquals("admin", contentVersion.getUsername());
    }

    public void testGetLastVersion() throws Throwable {
        ContentVersion contentVersion = this.versioningManager.getLastVersion("CNG12");
        assertNull(contentVersion);
        contentVersion = this.versioningManager.getLastVersion("ART1");
        assertEquals(3, contentVersion.getId());
        assertEquals("ART1", contentVersion.getContentId());
        assertEquals("ART", contentVersion.getContentType());
        assertEquals("Articolo 3", contentVersion.getDescr());
        assertEquals(Content.STATUS_READY, contentVersion.getStatus());
        assertNotNull(contentVersion.getXml());
        assertEquals("15/02/2005", DateConverter.getFormattedDate(contentVersion.getVersionDate(), "dd/MM/yyyy"));
        assertEquals("1.0", contentVersion.getVersion());
        assertEquals(1, contentVersion.getOnlineVersion());
        assertTrue(contentVersion.isApproved());
        assertEquals("mainEditor", contentVersion.getUsername());
    }

    public void testSaveGetDeleteVersion() throws Throwable {
        ((VersioningManager) this.versioningManager).saveContentVersion("ART102");
        ContentVersion contentVersion = this.versioningManager.getLastVersion("ART102");
        assertEquals(4, contentVersion.getId());
        assertEquals("ART102", contentVersion.getContentId());
        assertEquals("ART", contentVersion.getContentType());
        assertEquals("Contenuto 2 Customers", contentVersion.getDescr());
        assertEquals(Content.STATUS_DRAFT, contentVersion.getStatus());
        assertNotNull(contentVersion.getXml());
        assertEquals("15/12/2007", DateConverter.getFormattedDate(contentVersion.getVersionDate(), "dd/MM/yyyy"));
        assertEquals("1.0", contentVersion.getVersion());
        assertEquals(1, contentVersion.getOnlineVersion());
        assertTrue(contentVersion.isApproved());
        assertEquals("admin", contentVersion.getUsername());

        this.versioningManager.deleteVersion(contentVersion.getId());
        assertNull(this.versioningManager.getLastVersion("ART102"));
    }

    public void deleteWorkVersions() throws Throwable {
        List<Long> versions = this.versioningManager.getVersions("ART1");
        this.checkVersionIds(new long[]{1, 2, 3}, versions);
        this.versioningManager.deleteWorkVersions("ART1", 0);
        versions = this.versioningManager.getVersions("ART1");
        this.checkVersionIds(new long[]{1, 3}, versions);
    }

    private void checkVersionIds(long[] expected, List<Long> received) {
        assertEquals(expected.length, received.size());
        for (long current : expected) {
            if (!received.contains(new Long(current))) {
                fail("Expected " + current + " - Not found");
            }
        }
    }

    public void testContentVersionToIgnore_1() throws Exception {
        this.testContentVersionToIgnore(false, true);
        this.testContentVersionToIgnore(true, true);
    }

    public void testContentVersionToIgnore_2() throws Exception {
        this.testContentVersionToIgnore(false, false);
        this.testContentVersionToIgnore(true, false);
    }

    protected void testContentVersionToIgnore(boolean includeVersion, boolean isType) throws Exception {
        String newContentId = null;
        List<Long> versions = null;
        try {
            Content content = this.contentManager.loadContent("ALL4", false);
            content.setId(null);
            this.contentManager.saveContent(content);
            newContentId = content.getId();
            if (!includeVersion) {
                if (isType) {
                    this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE, "EVN,ART," + content.getTypeCode());
                } else {
                    this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE, "EVN103,ART1," + newContentId + ",EVN191");
                }
            } else if (isType) {
                this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE, "EVN,ART");
            } else {
                this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE, "EVN103,ART1,EVN191");
            }
            content = this.contentManager.loadContent(newContentId, false);
            versions = this.versioningManager.getVersions(newContentId);
            assertTrue(null == versions || versions.isEmpty());
            content.addGroup(Group.FREE_GROUP_NAME);
            content.setDescription(content.getDescription() + " modified");
            this.contentManager.saveContent(content);
            versions = this.versioningManager.getVersions(newContentId);
            if (includeVersion) {
                assertEquals(1, versions.size());
            } else {
                assertTrue(null == versions || versions.isEmpty());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != versions) {
                for (Long version : versions) {
                    this.versioningManager.deleteVersion(version);
                }
            }
            Content contentToDelete = this.contentManager.loadContent(newContentId, false);
            if (null != contentToDelete) {
                this.contentManager.deleteContent(contentToDelete);
            }
            if (isType) {
                this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENT_TYPES_TO_IGNORE, "");
            } else {
                this.updateConfigItem(JpversioningSystemConstants.CONFIG_PARAM_CONTENTS_TO_IGNORE, "");
            }
        }
    }

    private void updateConfigItem(String paramKey, String paramValue) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(paramKey, paramValue);
        String xmlParams = this.configManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, params, true);
        this.configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
    }

    private void init() throws Exception {
        this.versioningManager = (IVersioningManager) this.getService(JpversioningSystemConstants.VERSIONING_MANAGER);
        this.configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
        this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
        DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
        this.helper = new JpversioningTestHelper(dataSource, this.getApplicationContext());
    }

}
