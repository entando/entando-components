/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.content;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;

public class ContentMappingIntegrationTest extends BaseTestCase {
    
    private IContentManager contentManager;
    private ISeoMappingManager seoMappingManager;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testCreateFriendlyCode() throws Exception {
        String contentId1 = null;
        Content content1 = null;
        String contentId2 = null;
        Content content2 = null;
        String contentId3 = null;
        Content content3 = null;
        try {
            content1 = this.contentManager.loadContent("EVN25", true);
            content1.setId(null);
            content1.setMainGroup(Group.FREE_GROUP_NAME);
            
            this.contentManager.insertOnLineContent(content1);
            synchronized (this) {
                this.wait(500);
            }
            super.waitNotifyingThread();
            contentId1 = content1.getId();
            String friendlyCode = this.seoMappingManager.getContentReference(contentId1, null);
            assertEquals("teatro_delle_meraviglie", friendlyCode);
            
            content2 = this.contentManager.loadContent("EVN25", true);
            content2.setId(null);
            content2.setMainGroup(Group.FREE_GROUP_NAME);
            this.contentManager.insertOnLineContent(content2);
            synchronized (this) {
                this.wait(500);
            }
            super.waitNotifyingThread();
            contentId2 = content2.getId();
            String friendlyCode2 = this.seoMappingManager.getContentReference(contentId2, null);
            assertEquals("teatro_delle_meraviglie_it_1", friendlyCode2);
            
            content3 = this.contentManager.loadContent("EVN25", true);
            content3.setId(null);
            content3.setMainGroup(Group.FREE_GROUP_NAME);
            this.contentManager.insertOnLineContent(content3);
            synchronized (this) {
                this.wait(500);
            }
            super.waitNotifyingThread();
            contentId3 = content3.getId();
            String friendlyCode3 = this.seoMappingManager.getContentReference(contentId3, null);
            assertEquals("teatro_delle_meraviglie_it_2", friendlyCode3);
            
            this.contentManager.removeOnLineContent(content1);
            synchronized (this) {
                this.wait(500);
            }
            friendlyCode = this.seoMappingManager.getContentReference(contentId1, null);
            assertNull(friendlyCode);
            
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != contentId1) {
                this.contentManager.removeOnLineContent(content1);
                this.contentManager.deleteContent(content1);
            }
            if (null != contentId2) {
                this.contentManager.removeOnLineContent(content2);
                this.contentManager.deleteContent(content2);
            }
            if (null != contentId3) {
                this.contentManager.removeOnLineContent(content3);
                this.contentManager.deleteContent(content3);
            }
        }
    }
    
    private void init() throws Exception {
        try {
            this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
            this.seoMappingManager = (ISeoMappingManager) this.getService(JpseoSystemConstants.SEO_MAPPING_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
}
