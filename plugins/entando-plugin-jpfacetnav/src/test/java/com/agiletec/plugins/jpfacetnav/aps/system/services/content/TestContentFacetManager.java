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
package com.agiletec.plugins.jpfacetnav.aps.system.services.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpfacetnav.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpfacetnav.aps.system.JpFacetNavSystemConstants;

/**
 * @author E.Santoboni
 */
public class TestContentFacetManager extends ApsPluginBaseTestCase {

	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

	public void testLoadContentsId() throws Throwable {
		List<String> categoryCodes = new ArrayList<String>();
		categoryCodes.add("cat1");
		List<String> result = this._contentFacetManager.loadContentsId(null, categoryCodes, null);
		assertNotNull(result);
		String[] expectedIds_1 = {"ART180"};
		assertEquals(expectedIds_1.length, result.size());
		this.verifyOrder(result, expectedIds_1);

		categoryCodes.add("cat2");
		result = this._contentFacetManager.loadContentsId(null, categoryCodes, null);
		String[] expectedIds_2 = {};
		assertEquals(expectedIds_2.length, result.size());
		this.verifyOrder(result, expectedIds_2);
	}

	public void testGetOccurences() throws Throwable {
		List<String> categoryCodes = new ArrayList<String>();
		categoryCodes.add("cat1");
		Map<String, Integer> occurrences = this._contentFacetManager.getOccurrences(null, categoryCodes, null);
		assertNotNull(occurrences);
		assertEquals(1, occurrences.size());
		assertTrue(occurrences.keySet().contains("cat1"));

		categoryCodes.add("cat2");
		occurrences = this._contentFacetManager.getOccurrences(null, categoryCodes, null);
		assertNotNull(occurrences);
		assertEquals(0, occurrences.size());
	}

	private void verifyOrder(List<String> result, String[] expectedIds) {
		for (int i=0; i<result.size(); i++) {
			String item = result.get(i);
			assertEquals(expectedIds[i], item);
		}
	}

    private void init() throws Exception {
    	try {
    		this._contentFacetManager = (IContentFacetManager) this.getService(JpFacetNavSystemConstants.CONTENT_FACET_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IContentFacetManager _contentFacetManager = null;

}
