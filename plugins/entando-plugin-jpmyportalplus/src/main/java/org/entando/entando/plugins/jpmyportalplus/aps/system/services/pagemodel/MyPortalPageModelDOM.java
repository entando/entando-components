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
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsPropertiesDOM;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class MyPortalPageModelDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(MyPortalPageModelDOM.class);
	
	public MyPortalPageModelDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
		this.buildFrames();
	}
	
	public MyPortalPageModelDOM(Map<Integer, MyPortalFrameConfig> configuration) throws ApsSystemException {
		this._modelConfig = configuration;
		this._doc = new Document();
		Element root = new Element("frames");
		this._doc.setRootElement(root);
		if (null != configuration && !configuration.isEmpty()) {
			List<Integer> positions = new ArrayList<Integer>();
			positions.addAll(configuration.keySet());
			Collections.sort(positions);
			for (int i = 0; i < positions.size(); i++) {
				Integer position = positions.get(i);
				MyPortalFrameConfig config = configuration.get(position);
				if (null != config) {
					Element frameElement = new Element(TAB_FRAME);
					frameElement.setAttribute(ATTRIBUTE_POS, String.valueOf(position.intValue()));
					String locked = config.isLocked() ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
					frameElement.setAttribute("locked", locked);
					String column = (null != config.getColumn()) ? String.valueOf(config.getColumn().intValue()) : null;
					if (null != column) {
						frameElement.setAttribute("column", column);
					}
					root.addContent(frameElement);
				}
			}
		}
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error detected parsing the XML: {}", xmlText, t);
			throw new ApsSystemException("Error detected parsing the XML", t);
		}
	}
	
	private void buildFrames() throws ApsSystemException {
		List<Element> frameElements = _doc.getRootElement().getChildren(TAB_FRAME);
		if (null != frameElements && frameElements.size() > 0) {
			for (int i = 0; i < frameElements.size(); i++) {
				Element element = frameElements.get(i);
				this.buildFrame(element);
			}
		}
	}
	
	private void buildFrame(Element frameElement) throws ApsSystemException {
		int pos = Integer.parseInt(frameElement.getAttributeValue(ATTRIBUTE_POS));
		MyPortalFrameConfig frameConfig = new MyPortalFrameConfig();
		String fixed = frameElement.getAttributeValue("locked");
		if (null == fixed || fixed.equals(Boolean.TRUE.toString())) {
			frameConfig.setLocked(true);
		}
		String column = frameElement.getAttributeValue("column");
		if (null != column) {
			try {
				frameConfig.setColumn(Integer.parseInt(column));
			} catch (Throwable t) {
				//nothing to do
			}
		}
		this.getModelConfig().put(pos, frameConfig);
	}
	
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(this._doc);
	}
	
	private Document _doc;
	private final String TAB_FRAME = "frame";
	private final String ATTRIBUTE_POS = "pos";
	
	public Map<Integer, MyPortalFrameConfig> getModelConfig() {
		return _modelConfig;
	}
	
	private Map<Integer, MyPortalFrameConfig> _modelConfig = new HashMap<Integer, MyPortalFrameConfig>();

}