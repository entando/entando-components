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
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.parse;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model.VCardContactField;

/**
 * @author A.Cocco
 */
public class VCardDOM implements IVCardDOM {

	/**
	 * Build vcard-mapping document
	 * @param vcards
	 */
	@Override
	public void buildVcardMappingDOM(List<VCardContactField> vcards) {
		this.setDoc(new Document());
		this.setRoot(new Element(VCARD_FIELDS_ELEMENT));
		Element fieldElement = null;
		if(vcards != null){
			for (int i = 0; i < vcards.size(); i++) {
				VCardContactField vcard = vcards.get(i);
				fieldElement = new Element(VCARD_FIELD_ELEMENT);
				this.getRoot().addContent(fieldElement);

				this.setCode(vcard.getCode(), fieldElement);
				this.setDescription(vcard.getDescription(), fieldElement);
				this.setEnabled(vcard.isEnabled(), fieldElement);
				this.setProfileAttribute(vcard.getProfileAttribute(), fieldElement);
			}
		}
		this.getDoc().setRootElement(this.getRoot());
	}

	/**
	 * Returns a VCard list
	 * @return vcard list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VCardContactField> parseVcardMapping() {
		Element element = null;
		List<VCardContactField> cards = new ArrayList<VCardContactField>();
		Element root = this.getDoc().getRootElement();
		List<Element> fields = root.getChildren();
		VCardContactField vcard = null;
		for (int i = 0; i < fields.size(); i++) {
			element = fields.get(i);
			vcard = new VCardContactField();
			if(element.getAttribute(ATTRIBUTE_CODE_FIELD) != null) vcard.setCode(element.getAttribute(ATTRIBUTE_CODE_FIELD).getValue());
			if(element.getAttribute(ATTRIBUTE_DESCRIPTION_FIELD) != null) vcard.setDescription(element.getAttribute(ATTRIBUTE_DESCRIPTION_FIELD).getValue());
			if(element.getAttribute(ATTRIBUTE_ENABLED_FIELD) != null) vcard.setEnabled(Boolean.valueOf(element.getAttribute(ATTRIBUTE_ENABLED_FIELD).getValue()));
			if(element.getAttribute(ATTRIBUTE_PROFILE_FIELD) != null) vcard.setProfileAttribute(element.getAttribute(ATTRIBUTE_PROFILE_FIELD).getValue());
			cards.add(vcard);
		}
		return  cards;
	}
	
	/**
	 * Read vcard mapping
	 * @param vcard mapping
	 */
	@Override
	public void readVcardMapping(String vcardMapping){
		SAXBuilder builder = new SAXBuilder();
		try {
			this.setDoc(builder.build(new ByteArrayInputStream(vcardMapping.getBytes())));
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().trace("Method readVcardMapping failed.");
		} 
	}
	
	/**
	 * Returns vcard mapping in XML format
	 * @return vcard mapping in XML format
	 */	
	@Override
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		String xml = out.outputString(_doc);
		return xml;
	}

	/**
	 * Sets the vcard code
	 * @param code
	 * @param element
	 */
	private void setCode(String code, Element element){
		if(code != null) element.setAttribute(ATTRIBUTE_CODE_FIELD, code.trim());
		else element.setAttribute(ATTRIBUTE_CODE_FIELD, "");
	}

	/**
	 * Sets the vcard description
	 * @param description
	 * @param element
	 */
	private void setDescription(String description, Element element){
		if(description != null) element.setAttribute(ATTRIBUTE_DESCRIPTION_FIELD, description.trim());
		else element.setAttribute(ATTRIBUTE_DESCRIPTION_FIELD, "");
	}

	/**
	 * Sets vcard enabling
	 * @param enabled
	 * @param element
	 */
	private void setEnabled(boolean enabled, Element element){
		element.setAttribute(ATTRIBUTE_ENABLED_FIELD, String.valueOf(enabled));
	}

	/**
	 * Sets vcard profile attribute
	 * @param profileAttribute
	 * @param element
	 */
	private void setProfileAttribute(String profileAttribute, Element element){
		if(profileAttribute != null && !"".equals(profileAttribute.trim())) element.setAttribute(ATTRIBUTE_PROFILE_FIELD, profileAttribute.trim());
		else element.setAttribute(ATTRIBUTE_PROFILE_FIELD, "");
	}

	/**
	 * Sets the doc
	 * @param doc the doc to set
	 */
	public void setDoc(Document doc) {
		this._doc = doc;
	}
	/**
	 * Sets the root element
	 * @param root the root to set
	 */
	public void setRoot(Element root) {
		this._root = root;
	}
	/**
	 * Returns the doc
	 * @return the doc
	 */
	public Document getDoc() {
		return _doc;
	}
	/**
	 * Returns the root
	 * @return the root
	 */
	public Element getRoot() {
		return _root;
	}

	protected Document _doc;
	protected Element _root;

	private final static String VCARD_FIELDS_ELEMENT   = "vcardfields";
	private final static String VCARD_FIELD_ELEMENT   = "vcardfield";

	private final static String ATTRIBUTE_CODE_FIELD = "code";
	private final static String ATTRIBUTE_DESCRIPTION_FIELD = "description";
	private final static String ATTRIBUTE_ENABLED_FIELD = "enabled";
	private final static String ATTRIBUTE_PROFILE_FIELD = "profileAttribute";
	
}
