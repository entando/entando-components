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
