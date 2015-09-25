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
package org.entando.entando.plugins.jpsolrclient.aps.system.services.content;

import com.agiletec.aps.system.SystemConstants;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.entando.entando.plugins.jpsolrclient.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpsolrclient.aps.system.SolrConnectorSystemConstants;

/**
 * @author E.Santoboni
 */
public class TestSearcher extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
    	super.setUp();
        this.init();
    }
	
    public void testSearchContentsId_1() throws Throwable {
		try {
			this.deleteAllContent();
			Searcher searcher = new Searcher(this.getSolrPath());
			List<String> ids = searcher.extractAllContentsId();
			assertTrue(ids.isEmpty());
			
    		Content content = this.createContent();
			
			Indexer indexer = new Indexer(this.getSolrPath(), this._langManager);
			indexer.addContent(content);
			ids = searcher.extractAllContentsId();
			assertFalse(ids.isEmpty());
			assertTrue(ids.contains(content.getId()));
			
			indexer.delete(content.getId());
			ids = searcher.extractAllContentsId();
			assertTrue(ids.isEmpty());
			
        } catch (Throwable t) {
			throw t;
		}
    }
	
	private void deleteAllContent() throws Throwable {
		try {
			Searcher searcher = new Searcher(this.getSolrPath());
			List<String> ids = searcher.extractAllContentsId();
			if (null != ids && !ids.isEmpty()) {
				for (int i = 0; i < ids.size(); i++) {
					String id = ids.get(i);
					Indexer indexer = new Indexer(this.getSolrPath(), this._langManager);
					indexer.delete(id);
				}
			}
        } catch (Throwable t) {
			throw t;
		}
	}
    
	protected String getSolrPath() {
		String serverUrl = this._configManager.getParam(SolrConnectorSystemConstants.SERVER_URL_PARAM_NAME);
		if (null != serverUrl && serverUrl.trim().length() == 0) {
			serverUrl = null;
		}
		if (null == serverUrl) {
            serverUrl = SolrConnectorSystemConstants.SERVER_URL_DEFAULT_VALUE;
        }
        return serverUrl;
	}
	
    private Content createContent() {
        Content content = new Content();
        content.setId("100");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("secondaryGroup");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("San Pietroburgo è una città meravigliosa W3C-WAI", "it");
        text.setText("St. Petersburg is a wonderful city", "en");
        content.addAttribute(text);
        return content;
    }
    
    private void init() throws Exception {
        try {
        	this._langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
        } catch (Exception e) {
			throw e;
        }
    }
    
    private ILangManager _langManager = null;
	private ConfigInterface _configManager;

}