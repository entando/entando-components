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
package org.entando.entando.plugins.jpfileattribute.aps.system.entity.model;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.services.lang.Lang;

import org.entando.entando.plugins.jpfileattribute.aps.system.file.AttachedFile;

import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public class FileAttribute extends AbstractAttribute {
	
	@Override
	public boolean isSearchableOptionSupported() {
		return false;
	}
	
	@Override
	public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
		return null;
	}
	
	@Override
	public Element getJDOMElement() {
		Element attributeElement = new Element("attribute");
        attributeElement.setAttribute("name", this.getName());
        attributeElement.setAttribute("attributetype", this.getType());
		AttachedFile file = this.getAttachedFile();
        if (null != file) {
            Element fileElement = new Element("file");
			Element idElement = new Element("id");
			idElement.setText(String.valueOf(file.getId()));
			fileElement.addContent(idElement);
			Element filenameElement = new Element("filename");
			filenameElement.setText(file.getFilename());
			fileElement.addContent(filenameElement);
			attributeElement.addContent(fileElement);
        }
        return attributeElement;
	}
	
	@Override
	public Object getValue() {
		return this.getAttachedFile();
	}
	
	@Override
	public Status getStatus() {
		if (null != this.getValue()) {
            return Status.VALUED;
        }
        return Status.EMPTY;
	}
	
	@Override
	protected AbstractJAXBAttribute getJAXBAttributeInstance() {
		return new JAXBFileAttribute();
	}
	
	@Override
	public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
		JAXBFileAttribute jaxbAttribute = (JAXBFileAttribute) super.createJAXBAttribute(langCode);
		if (null == jaxbAttribute) return null;
		jaxbAttribute.setAttachedFile(this.getAttachedFile());
		return jaxbAttribute;
	}
	
	@Override
	public void valueFrom(AbstractJAXBAttribute jaxbAttribute) {
		super.valueFrom(jaxbAttribute);
		if (null == jaxbAttribute) return;
		AttachedFile attachedFile = ((JAXBFileAttribute) jaxbAttribute).getAttachedFile();
		this.setAttachedFile(attachedFile);
	}
	
	public AttachedFile getAttachedFile() {
		return _attachedFile;
	}
	public void setAttachedFile(AttachedFile attachedFile) {
		this._attachedFile = attachedFile;
	}
	
	private AttachedFile _attachedFile;
	
}