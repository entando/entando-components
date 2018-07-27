/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpseo.aps.system.services.metatag;

import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class MetatagDOM {

	private static final Logger logger = LoggerFactory.getLogger(MetatagDOM.class);
	
	private Document doc;
	
	public MetatagDOM(String xmlText) {
		this.decodeDOM(xmlText);
	}
    
	// <meta attributeName="name" key="og:email" defaultValue="me@example.com" group="OpenGraph Meta Tags" />
	public Map<String, Metatag> getMetatags() {
        Map<String, Metatag> map = new HashMap<>();
		List<Element> metaElements = this.doc.getRootElement().getChildren();
		for (Element metaElement : metaElements) {
			Metatag metatag = new Metatag();
			String key = metaElement.getAttributeValue("key");
			metatag.setKey(key);
            metatag.setAttributeKey(metaElement.getAttributeValue("attributeKey"));
            metatag.setDefaultValue(metaElement.getAttributeValue("defaultValue"));
            metatag.setGroup(metaElement.getAttributeValue("group"));
			metatag.setDescription(metaElement.getText());
			map.put(key, metatag);
		}
		return map;
	}
	
	private void decodeDOM(String xmlText) {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			this.doc = builder.build(reader);
		} catch (Throwable t) {
			logger.error("Error while parsing xml : {}", xmlText, t);
			throw new RuntimeException("Error detected while parsing the XML", t);
		}
	}

}
