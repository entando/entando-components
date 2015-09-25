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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import org.entando.entando.plugins.jpsolrclient.aps.ApsPluginBaseTestCase;

/**
 * @author E.Santoboni
 */
public class TestSearchEngineManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
    	super.setUp();
        this.init();
    }
	
    public void testSearchContentsId_1() throws Throwable {
    	try {
    		Content content = this.createContent();
    		this._searchEngineManager.deleteIndexedEntity(content.getId());
    		this._searchEngineManager.addEntityToIndex(content);
            
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", null);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content.getId()));
			contentsId = this._searchEngineManager.searchEntityId("en", "Petersburg wonderful", null);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content.getId()));
			contentsId = this._searchEngineManager.searchEntityId("en", "meravigliosa", null);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains(content.getId()));
        } catch (Throwable t) {
			throw t;
		}
    }
    
    public void testSearchContentsId_2() throws Throwable {
        try {
        	Thread thread = this._searchEngineManager.startReloadContentsReferences();
        	thread.join();
        	
        	Set<String> allowedGroup = new HashSet<String>();
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "Corpo coach", allowedGroup);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains("ART104"));
        	
			allowedGroup.add("coach");
			contentsId = this._searchEngineManager.searchEntityId("it", "testo coach", allowedGroup);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains("ART104"));//coach content
			
			contentsId = this._searchEngineManager.searchEntityId("it", "Titolo Evento 4", allowedGroup);
        	assertNotNull(contentsId);
			assertTrue(contentsId.contains("EVN194"));//free content
			
			Set<String> allowedGroup2 = new HashSet<String>();
			allowedGroup2.add(Group.ADMINS_GROUP_NAME);
			contentsId = this._searchEngineManager.searchEntityId("it", "testo coach", allowedGroup2);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains("ART104"));//coach content
			
        } catch (Throwable t) {
			throw t;
		}
    }
    
    public void testSearchContentsId_3() throws Throwable {
    	try {
    		Content content = this.createContent();
    		content.setMainGroup(Group.ADMINS_GROUP_NAME);
    		this._searchEngineManager.deleteIndexedEntity(content.getId());
    		this._searchEngineManager.addEntityToIndex(content);
    		
            List<String> allowedGroup = new ArrayList<String>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains(content.getId()));
			
			allowedGroup.add("secondaryGroup");
			
			contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content.getId()));
			
        } catch (Throwable t) {
			throw t;
		}
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
        	this._searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
        } catch (Exception e) {
			throw e;
        }
    }
    
    private ICmsSearchEngineManager _searchEngineManager = null;

}