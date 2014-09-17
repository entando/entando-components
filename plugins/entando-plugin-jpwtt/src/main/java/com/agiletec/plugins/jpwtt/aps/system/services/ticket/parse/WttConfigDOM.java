/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.parse;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.WttConfig;

/*
<wttConf>
	<interventionTypes>
		<interventionType id="1" descr="Hardware" />
		<interventionType id="2" descr="Software" />
	</interventionTypes>
	<priorities>
		<priority id="1" descr="High" />
		<priority id="2" descr="Medium" />
		<priority id="3" descr="Low" />
	</priorities>
</wttConf>
*/

/**
 * Classe dom delegata alla lettura della configurazione del servizio di web trouble ticketing.
 * @version 1.0
 * @author E.Mezzano
 */
public class WttConfigDOM {
	
	/**
	 * Extract the wtt configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The wtt configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public WttConfig extractConfig(String xml) throws ApsSystemException {
		WttConfig config = new WttConfig();
		Element root = this.getRootElement(xml);
		this.extractConfig(root, config);
		return config;
	}
	
	/**
	 * Create an xml containing the wtt configuration.
	 * @param config The wtt configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(WttConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}
	
	private Element createConfigElement(WttConfig config) {
		// TODO Implementare createConfigElement
		return null;
	}
	
	private WttConfig extractConfig(Element root, WttConfig config) {
		Map<Integer, InterventionType> intervTypes = new HashMap<Integer, InterventionType>();
		Element intervTypesElem = root.getChild("interventionTypes");
		if (intervTypesElem!=null) {
			List<Element> intervTypeElements = intervTypesElem.getChildren("interventionType");
			for (Element intervTypeElem : intervTypeElements) {
				InterventionType interventionType = this.extractInterventionType(intervTypeElem);
				intervTypes.put(interventionType.getId(), interventionType);
			}
			config.setInterventionTypes(intervTypes);
		}
		
		Map<Integer, String> priorities = new HashMap<Integer, String>();
		Element prioritiesElem = root.getChild("priorities");
		if (prioritiesElem!=null) {
			List<Element> priorityElements = prioritiesElem.getChildren("priority");
			for (Element priorityElem : priorityElements) {
				Integer id = new Integer(priorityElem.getAttributeValue("id"));
				String descr = priorityElem.getAttributeValue("descr");
				priorities.put(id, descr);
			}
			config.setPriorities(priorities);
		}
		
		return config;
	}
	
	private InterventionType extractInterventionType(Element intervTypeElem) {
		InterventionType interventionType = new InterventionType();
		Integer id = new Integer(intervTypeElem.getAttributeValue("id"));
		String descr = intervTypeElem.getAttributeValue("descr");
		interventionType.setId(id);
		interventionType.setDescr(descr);
		return interventionType;
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
	
}