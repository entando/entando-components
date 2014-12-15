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
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ComponentDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(ComponentDOM.class);
	
	public ComponentDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	public List<AvailableArtifact> getArtifacts() {
		List<AvailableArtifact> artifacts = new ArrayList<AvailableArtifact>();
		Integer index = 0;
		List<Element> elements = this._doc.getRootElement().getChildren();
		for (int i=0; i<elements.size(); i++) {
			Element containerElement = elements.get(i);
			this.loadArtifacts(artifacts, index, containerElement, null, null);
		}
		return artifacts;
	}
	
	protected void loadArtifacts(List<AvailableArtifact> artifacts, 
			Integer index, Element containerElement, String section, AvailableArtifact.Type type) {
		if (null == containerElement) {
			return;
		}
		String label = containerElement.getChildText("label");
		if (null == label) {
			return;
		}
		if (null == type) {
			String typeString = containerElement.getChildText("type");
			if (null != typeString) {
				type = Enum.valueOf(AvailableArtifact.Type.class, typeString.toUpperCase());
			}
		}
		String currentSection = (null != section) ? section + " - " + label : label;
		List<Element> children = containerElement.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Element element = children.get(i);
			String name = element.getName();
			if (name.equals("artifact")) {
				/*
		<artifact>
			<description>Activiti BPM Connector</description>
			<groupId>org.entando.entando.plugins</groupId>
			<artifactId>entando-plugin-jpactiviti</artifactId>
		</artifact>
				 */
				String description = element.getChildText("description");
				String groupId = element.getChildText("groupId");
				String artifactId = element.getChildText("artifactId");
				AvailableArtifact artifact = new AvailableArtifact();
				artifact.setLabel(currentSection);
				artifact.setArtifactId(artifactId);
				artifact.setDescription(description);
				artifact.setGroupId(groupId);
				artifact.setId(index);
				artifact.setType(type);
				index = index + 1;
				artifacts.add(artifact);
			} else {
				this.loadArtifacts(artifacts, index, element, currentSection, type);
			}
		}
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			this._doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error while parsing xml : {}", xmlText, t);
			throw new ApsSystemException("Error detected while parsing the XML", t);
		}
	}
	
	private Document _doc;
	
}
