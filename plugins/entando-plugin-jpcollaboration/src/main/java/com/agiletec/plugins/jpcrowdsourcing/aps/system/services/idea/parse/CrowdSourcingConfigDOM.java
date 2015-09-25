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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.parse;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IdeaCommentManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.CrowdSourcingConfig;

/*
<crowdSourcingConfig>
	<idea>
		<moderateEntries descr="Determina se la pubblicazione deve essere moderata">true</moderateEntries>
		<moderateComments descr="Determina se i commenti devono essere moderati">true</moderateComments>
	</idea>
</crowdSourcingConfig>
 */

public class CrowdSourcingConfigDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaCommentManager.class);

	/**
	 * Extracts the configuration from an xml.
	 */
	public CrowdSourcingConfig extractConfig(String xml) throws ApsSystemException {
		CrowdSourcingConfig config = new CrowdSourcingConfig();
		Element root = this.getRootElement(xml);
		this.extractIdeaConfig(root, config);
		return config;
	}

	/**
	 * Create an xml containing the configuration.
	 */
	public String createConfigXml(CrowdSourcingConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}


	private void extractIdeaConfig(Element root, CrowdSourcingConfig config) {
		Element ideaEl = root.getChild(ELEMENT_IDEA);
		if (ideaEl != null) {
			Element moderateEntriesEl = ideaEl.getChild(ELEMENT_MODERATE_ENTRIES);
			Element moderateCommentsEl = ideaEl.getChild(ELEMENT_MODERATE_COMMENTS);
			if (null != moderateEntriesEl) {
				config.setModerateEntries(moderateEntriesEl.getValue().equalsIgnoreCase("true"));
			}
			if (null != moderateCommentsEl) {
				config.setModerateComments(moderateCommentsEl.getValue().equalsIgnoreCase("true"));
			}
		}
	}

	private Element createConfigElement(CrowdSourcingConfig config) {
		Element configElem = new Element(ROOT);
		Element ideaElem = this.createIdeaElement(config);
		configElem.addContent(ideaElem);
		return configElem;
	}

	private Element createIdeaElement(CrowdSourcingConfig config) {
		Element ideaEl = new Element(ELEMENT_IDEA);
		
		Element moderateEntriesEl = new Element(ELEMENT_MODERATE_ENTRIES);
		moderateEntriesEl.setAttribute("descr", "Determina se la pubblicazione deve essere moderata");
		moderateEntriesEl.setText(new Boolean(config.isModerateEntries()).toString());

		Element moderateCommentsEl = new Element(ELEMENT_MODERATE_COMMENTS);
		moderateCommentsEl.setAttribute("descr", "Determina se i commenti devono essere modeati");
		moderateCommentsEl.setText(new Boolean(config.isModerateComments()).toString());
		
		ideaEl.addContent(moderateEntriesEl);
		ideaEl.addContent(moderateCommentsEl);
		return ideaEl;
	}


	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			_logger.error("Error parsing xml: {}", xmlText, t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}

	private final String ROOT = "crowdSourcingConfig";

	private final String ELEMENT_IDEA = "idea";
	private final String ELEMENT_MODERATE_ENTRIES = "moderateEntries";
	private final String ELEMENT_MODERATE_COMMENTS = "moderateComments";

}
