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
package org.entando.entando.plugins.jpsolrclient.aps.system.services.config;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpsolrclient.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpsolrclient.aps.system.services.config.model.SolrConfig;

/**
 * @author S.Loru
 */
public class TestSolrConfigDOM extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testWriter() throws ApsSystemException {
		String marshalConfig = SolrConfigDOM.marshalConfig(this.getSolrConfig());
		assertEquals(this.getSolrConfigXmlString(), marshalConfig);
	}
	
	public void testReader() throws ApsSystemException {
		SolrConfig unmarshalConfig = SolrConfigDOM.unmarshalConfig(this.getSolrConfigXmlString());
		assertEquals(this.getSolrConfig(), unmarshalConfig);
	}
	
	private void init() throws Exception {
		SolrConfig solrConfig = new SolrConfig();
		solrConfig.setProviderUrl("test");
		solrConfig.setMaxResultSize("250");
		this.setSolrConfig(solrConfig);
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
				+ "<SolrConfig>\n"
				+ "    <providerUrl>test</providerUrl>\n"
				+ "    <maxResultSize>250</maxResultSize>\n"
				+ "</SolrConfig>\n";
		this.setSolrConfigXmlString(xml);
	}

	public String getSolrConfigXmlString() {
		return _solrConfigXmlString;
	}

	public void setSolrConfigXmlString(String solrConfigXmlString) {
		this._solrConfigXmlString = solrConfigXmlString;
	}

	public SolrConfig getSolrConfig() {
		return _solrConfig;
	}

	public void setSolrConfig(SolrConfig solrConfig) {
		this._solrConfig = solrConfig;
	}
	
	private String _solrConfigXmlString;
	private SolrConfig _solrConfig;
}
