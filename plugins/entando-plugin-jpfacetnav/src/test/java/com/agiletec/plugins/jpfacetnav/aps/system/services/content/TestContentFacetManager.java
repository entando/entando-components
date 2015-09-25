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
