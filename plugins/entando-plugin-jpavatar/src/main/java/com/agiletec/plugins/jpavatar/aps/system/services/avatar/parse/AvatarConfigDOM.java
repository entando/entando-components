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

package com.agiletec.plugins.jpavatar.aps.system.services.avatar.parse;

import java.io.StringReader;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.AvatarConfig;


/*
<jpavatar>
	<style>{AvatarConfig style constant}</style>
</jpavatar>
 */

public class AvatarConfigDOM {

	/**
	 * Extract the service configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The service configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public AvatarConfig extractConfig(String xml) throws ApsSystemException {
		AvatarConfig config = new AvatarConfig();
		Element root = this.getRootElement(xml);
		Element styleElem = root.getChild(STYLE_ELEM);
		String style = null;
		if (styleElem != null) {
			style = styleElem.getText();
		}
		
		if (StringUtils.isBlank(style) || !ArrayUtils.contains(AvatarConfig.STYLES, style)) {
			style = AvatarConfig.STYLE_LOCAL;
		}
		
		config.setStyle(style);
		return config;
	}
	
	/**
	 * Extract the configuration from the xml element and save it into the AvatarConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	private Element createConfigElement(AvatarConfig config) {
		Element configElem = new Element(ROOT);
		Element styleElement = new Element(STYLE_ELEM);
		styleElement.setText(config.getStyle());
		configElem.addContent(styleElement);
		return configElem;
	}

	/**
	 * Create an xml containing the jpavatar configuration.
	 * @param config The jpavatar configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(AvatarConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
	
	private final String ROOT = "jpavatar";
	private final String STYLE_ELEM = "style";
	

}
