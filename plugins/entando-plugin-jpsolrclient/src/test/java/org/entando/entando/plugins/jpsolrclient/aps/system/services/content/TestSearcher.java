/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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