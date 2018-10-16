/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.metatag;

import com.agiletec.aps.BaseTestCase;
import java.util.Map;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;

/**
 * @author E.Santoboni
 */
public class MetatagCatalogueIntegrationTest extends BaseTestCase {
    
	public void testLoadCatalogue() {
        IMetatagCatalog catalogue = super.getApplicationContext().getBean(JpseoSystemConstants.SEO_METATAG_CATALOG, IMetatagCatalog.class);
        Map<String, Metatag> map = catalogue.getCatalog();
        assertNotNull(map);
        assertTrue(map.size() > 0);
        Metatag ogDescription = map.get("og:description");
        assertNotNull(ogDescription);
        assertEquals("property", ogDescription.getAttributeKey());
    }
    
}
