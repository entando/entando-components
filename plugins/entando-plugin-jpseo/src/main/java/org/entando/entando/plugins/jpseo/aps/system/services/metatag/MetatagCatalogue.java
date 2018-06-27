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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class MetatagCatalogue implements IMetatagCatalogue {

	private static final Logger logger = LoggerFactory.getLogger(MetatagCatalogue.class);
    
    private Map<String, Metatag> catalogue = null;
    
    private void loadCatalogue() {
        try {
			InputStream is = this.getClass().getResourceAsStream("metatag_catalogue.xml");
			String xmlConfig = FileTextReader.getText(is);
			MetatagDOM metatagDom = new MetatagDOM(xmlConfig);
			this.catalogue = metatagDom.getMetatags();
		} catch (ApsSystemException | IOException e) {
			logger.error("Error loading langs from iso definition", e);
			throw new RuntimeException("Error loading langs from iso definition", e);
		}
    }

    @Override
    public Map<String, Metatag> getCatalogue() {
        if (null == this.catalogue) {
            this.loadCatalogue();
        }
        return catalogue;
    }

    public void setCatalogue(Map<String, Metatag> catalogue) {
        this.catalogue = catalogue;
    }
    
}
