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
package org.entando.entando.plugins.jpwebform.aps.system.services.form.parse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * 
 * 
<formtypes>
<formtype code="XXXX">
	<step order="" type="" builtGui="true" ognlExpression="">
		<attribute name="aaa" view="true" />
		<attribute name="bbbb" view="true" />
		<attribute name="bbbb" view="false" />
		....
		....
	</step>
</formtype>
...
...
</formtypes>
 * 
 * 
 * @author E.Santoboni
 */
public class StepConfigsDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(StepConfigsDOM.class);

	public Map<String, StepsConfig> extractConfig(String xml) throws ApsSystemException {
		Map<String, StepsConfig> config = new HashMap<String, StepsConfig>();
		try {
			Element root = this.getRootElement(xml);
			List<Element> messageTypeElements = root.getChildren(FORMTYPE_ELEM);
			for (int i = 0; i < messageTypeElements.size(); i++) {
				Element messageTypeElem = messageTypeElements.get(i);
				StepsConfig messageTypeConfig = this.extractConfig(messageTypeElem);
				config.put(messageTypeConfig.getFormTypeCode(), messageTypeConfig);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting config", t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return config;
	}
	
	public StepsConfig extractStepConfig(String xml) throws ApsSystemException {
		StepsConfig config = null;
		try {
			Element root = this.getRootElement(xml);
			config = this.extractConfig(root);
		} catch (Throwable t) {
			_logger.error("Error extracting step config", t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return config;
	}
	
	private StepsConfig extractConfig(Element messageTypeElem) throws ApsSystemException {
		StepsConfig config = new StepsConfig();
		try {
			String messageTypeCode = messageTypeElem.getAttributeValue(FORMTYPE_TYPECODE_ATTR);
			config.setFormTypeCode(messageTypeCode);
			
			String confirmGui = messageTypeElem.getAttributeValue(FORMTYPE_CONFIRM_GUI_ATTR);
			config.setConfirmGui(Boolean.parseBoolean(confirmGui));
			String builtConfirmGui = messageTypeElem.getAttributeValue(FORMTYPE_BUILT_CONFIRM_GUI_ATTR);
			config.setBuiltConfirmGui(Boolean.parseBoolean(builtConfirmGui));
			String builtEndPointGui = messageTypeElem.getAttributeValue(FORMTYPE_BUILT_END_POINT_GUI_ATTR);
			config.setBuiltEndPointGui(Boolean.parseBoolean(builtEndPointGui));
			
			List<Step> steps = new ArrayList<Step>();
			List<Element> stepsElements = messageTypeElem.getChildren(STEP_ELEM);
			for (int i = 0; i < stepsElements.size(); i++) {
				Element stepElem = stepsElements.get(i);
				Step step = this.extractStepConfig(stepElem);
				steps.add(step);
			}
			config.setSteps(steps);
		} catch (Throwable t) {
			_logger.error("Error extracting steps config", t);
			throw new ApsSystemException("Error extracting steps config", t);
		}
		return config;
	}
	
	private Step extractStepConfig(Element stepElem) throws ApsSystemException {
		Step step = new Step();
		try {
			//String orderString = stepElem.getAttributeValue(STEP_ORDER_ATTR);
			//step.setOrder(Integer.parseInt(orderString));
			
			//String type = stepElem.getAttributeValue(STEP_TYPE_ATTR);
			//step.setType(Enum.valueOf(Step.StepType.class, type.toUpperCase()));
			
			step.setCode(stepElem.getAttributeValue(STEP_CODE_ATTR));
			step.setOgnlExpression(stepElem.getAttributeValue(STEP_OGNL_EXPRESSION_ATTR));
			
			String builtGuiValue = stepElem.getAttributeValue(STEP_BUILT_GUI);
			Boolean builtGui = (null != builtGuiValue) ? Boolean.parseBoolean(builtGuiValue) : false;
			step.setBuiltGui(builtGui);
			
			List<Element> attributeElements = stepElem.getChildren(ATTRIBUTE_ELEM);
			for (int i = 0; i < attributeElements.size(); i++) {
				Element attributeElem = attributeElements.get(i);
				String name = attributeElem.getAttributeValue(ATTRIBUTE_NAME_ATTR);
				String viewString = attributeElem.getAttributeValue(ATTRIBUTE_VIEW_ATTR);
				boolean view = Boolean.parseBoolean(viewString);
				step.addAttributeConfig(name, view);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting step config", t);
			throw new ApsSystemException("Error extracting step config", t);
		}
		return step;
	}
	
	public String createConfigXml(Map<String, StepsConfig> config) throws ApsSystemException {
		String xml = null;
		try {
			Element root = new Element(ROOT);
			Collection<StepsConfig> stepsConfigs = config.values();
			if (null != stepsConfigs) {
				Iterator<StepsConfig> iter = stepsConfigs.iterator();
				while (iter.hasNext()) {
					StepsConfig stepsConfig = iter.next();
					Element stepsConfigElements = this.createConfigElement(stepsConfig);
					root.addContent(stepsConfigElements);
				}
			}
			XMLOutputter out = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setIndent("\t");
			out.setFormat(format);
			Document doc = new Document(root);
			xml = out.outputString(doc);
		} catch (Throwable t) {
			_logger.error("Error creating xml config", t);
			throw new ApsSystemException("Error creating xml config", t);
		}
		return xml;
	}
	
	public String createConfigXml(StepsConfig stepsConfig) throws ApsSystemException {
		String xml = null;
		try {
			Element root =  this.createConfigElement(stepsConfig);
			XMLOutputter out = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setIndent("\t");
			out.setFormat(format);
			Document doc = new Document(root);
			xml = out.outputString(doc);
		} catch (Throwable t) {
			_logger.error("Error creating xml config", t);
			throw new ApsSystemException("Error creating xml config", t);
		}
		return xml;
	}
	
	private Element createConfigElement(StepsConfig stepsConfig) throws ApsSystemException {
		Element element = new Element(FORMTYPE_ELEM);
		try {
			element.setAttribute(FORMTYPE_TYPECODE_ATTR, stepsConfig.getFormTypeCode());
			element.setAttribute(FORMTYPE_CONFIRM_GUI_ATTR, String.valueOf(stepsConfig.isConfirmGui()));
			element.setAttribute(FORMTYPE_BUILT_CONFIRM_GUI_ATTR, String.valueOf(stepsConfig.isBuiltConfirmGui()));
			element.setAttribute(FORMTYPE_BUILT_END_POINT_GUI_ATTR, String.valueOf(stepsConfig.isBuiltEndPointGui()));
			List<Step> steps = stepsConfig.getSteps();
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				Element stepElement = this.createStepElement(step);
				element.addContent(stepElement);
			}
		} catch (Throwable t) {
			_logger.error("Error creating config element", t);
			throw new ApsSystemException("Error creating config element", t);
		}
		return element;
	}
	
	private Element createStepElement(Step step) throws ApsSystemException {
		Element element = new Element(STEP_ELEM);
		try {
			//element.setAttribute(STEP_ORDER_ATTR, String.valueOf(step.getOrder()));
			//element.setAttribute(STEP_TYPE_ATTR, step.getType().toString());
			element.setAttribute(STEP_CODE_ATTR, step.getCode());
			element.setAttribute(STEP_BUILT_GUI, String.valueOf(step.isBuiltGui()));
			if(StringUtils.isNotBlank(step.getOgnlExpression())){
				element.setAttribute(STEP_OGNL_EXPRESSION_ATTR, step.getOgnlExpression());
			}
			List<String> attributes = step.getAttributeOrder();
			for (int i = 0; i < attributes.size(); i++) {
				String name = attributes.get(i);
				Step.AttributeConfig attributeConfig = step.getAttributeConfigs().get(name);
				Element attributeConfigElement = new Element(ATTRIBUTE_ELEM);
				attributeConfigElement.setAttribute(ATTRIBUTE_NAME_ATTR, attributeConfig.getName());
				attributeConfigElement.setAttribute(ATTRIBUTE_VIEW_ATTR, String.valueOf(attributeConfig.isView()));
				element.addContent(attributeConfigElement);
			}
		} catch (Throwable t) {
			_logger.error("Error creating steb element", t);
			throw new ApsSystemException("Error creating step element", t);
		}
		return element;
	}
	
	protected Element getRootElement(String xmlText) throws ApsSystemException {
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
	
	private final String ROOT = "formtypes";
	
	private final String FORMTYPE_ELEM = "formtype";
	private final String FORMTYPE_TYPECODE_ATTR = "code";
	private final String FORMTYPE_CONFIRM_GUI_ATTR = "confirmGui";
	private final String FORMTYPE_BUILT_CONFIRM_GUI_ATTR = "builtConfirmGui";
	private final String FORMTYPE_BUILT_END_POINT_GUI_ATTR = "builtEndPointGui";
	private final String STEP_ELEM = "step";
	//private final String STEP_ORDER_ATTR = "order";
	//private final String STEP_TYPE_ATTR = "type";
	private final String STEP_CODE_ATTR = "code";
	private final String STEP_BUILT_GUI = "builtGui";
	private final String STEP_OGNL_EXPRESSION_ATTR = "ognlExpression";
	private final String ATTRIBUTE_ELEM = "attribute";
	private final String ATTRIBUTE_NAME_ATTR = "name";
	private final String ATTRIBUTE_VIEW_ATTR = "view";
	
}