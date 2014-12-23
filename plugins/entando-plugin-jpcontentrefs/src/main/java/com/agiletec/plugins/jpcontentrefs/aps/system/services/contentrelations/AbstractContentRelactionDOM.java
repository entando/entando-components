/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe JDOM per la lettura e scrittura dell'xml di relazione 
 * dei tipi contenuto con elementi associabili.
 * Esempio per categorie:
<contenttypes>
	<contenttype typecode="MEN">
		<category code="cat1"/>
		<category code="cat2"/>
	</contenttype>
	<contenttype typecode="EVN">
		<category code="cat1"/>
	</contenttype>
</contenttypes>
 * @version 1.0
 * @author E.Santoboni
 */
public abstract class AbstractContentRelactionDOM {
	
	public AbstractContentRelactionDOM() throws ApsSystemException {}
	
	public AbstractContentRelactionDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Errore nel parsing: " + t.getMessage());
			throw new ApsSystemException("Errore nel parsing: " + t.getMessage(), t);
		}
	}
	
	public Map<String, List<String>> getContentTypes() {
		Map<String, List<String>> contentTypesMap = new HashMap<String, List<String>>();
		List contentTypeElements = this.getDocument().getRootElement().getChildren("contenttype");
		if (null != contentTypeElements && contentTypeElements.size() > 0) {
			Iterator contentTypeElementsIter = contentTypeElements.iterator();
			while (contentTypeElementsIter.hasNext()) {
				Element contentTypeElement = (Element) contentTypeElementsIter.next();
				this.addReferenciesForType(contentTypesMap, contentTypeElement);
			}
		}
		return contentTypesMap;
	}
	
	private void addReferenciesForType(Map<String, List<String>> contentTypesMap, Element contentTypeElement) {
		String contentTypeCode = contentTypeElement.getAttributeValue("typecode");
		List<String> referencesForType = new ArrayList<String>();
		List referenceElements = contentTypeElement.getChildren(this.getReferenceElementName());
		if (null != referenceElements && referenceElements.size() > 0) {
			Iterator referenceElementsIter = referenceElements.iterator();
			while (referenceElementsIter.hasNext()) {
				Element categoryElement = (Element) referenceElementsIter.next();
				String code = categoryElement.getAttributeValue("code");
				referencesForType.add(code);
			}
		}
		contentTypesMap.put(contentTypeCode, referencesForType);
	}
	
	public void setContent(Map<String, List<String>> contentTypeCategories) {
		this.initDocument();
		Element elementRoot = new Element("contenttypes");
		this.getDocument().setRootElement(elementRoot);
		Iterator<String> contentTypeCodesIter = contentTypeCategories.keySet().iterator();
		while (contentTypeCodesIter.hasNext()) {
			String contentTypeCode = (String) contentTypeCodesIter.next();
			if (null != contentTypeCategories.get(contentTypeCode) && 
					!((List<String>)contentTypeCategories.get(contentTypeCode)).isEmpty()) {
				Element contentElement = new Element("contenttype");
				contentElement.setAttribute("typecode", contentTypeCode);
				List<String> codes = (List<String>)contentTypeCategories.get(contentTypeCode);
				for (int i=0; i<codes.size(); i++) {
					String code = codes.get(i);
					Element refElement = new Element(this.getReferenceElementName());
					refElement.setAttribute("code", code);
					contentElement.addContent(refElement);
				}
				elementRoot.addContent(contentElement);
			}
		}
	}
	
	protected abstract String getReferenceElementName();

	protected Document getDocument() {
		return this._doc;
	}
	
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		out.setFormat(format);
		String xml = out.outputString(_doc);
		return xml;
	}
	
	public void initDocument() {
		this._doc = new Document();
	}
	
	private Document _doc;
	
}
