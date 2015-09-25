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
			index = this.loadArtifacts(artifacts, index, containerElement, null, null);
		}
		return artifacts;
	}
	
	protected Integer loadArtifacts(List<AvailableArtifact> artifacts, 
			Integer index, Element containerElement, String section, AvailableArtifact.Type type) {
		if (null == containerElement) {
			return index;
		}
		String label = containerElement.getChildText("label");
		if (null == label) {
			return index;
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
				index = this.loadArtifacts(artifacts, index, element, currentSection, type);
			}
		}
		return index;
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
