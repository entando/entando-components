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

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.jdom.Element;

/**
 * DOM class delegated to parse the xml defining the Message Types configuration.
 * This class is used as utility for MessageManager.
 * @see EntityTypeDOM for more information.
 * @author M.Minnai
 */
public class FormTypeDOM extends EntityTypeDOM {
	
	@Override
	protected IApsEntity createEntityType(Element element, Class entityClass) throws ApsSystemException {
		Message message = (Message) super.createEntityType(element, entityClass);
		Integer version = null;
		Boolean repeatable = Boolean.TRUE;
		Boolean ignoreVersionEdit = Boolean.TRUE;
		Boolean ignoreVersionDisplay = Boolean.TRUE;
		String versionString = this.extractXmlAttribute(element, "version", false);
		String repeatableString = this.extractXmlAttribute(element, "repeatable", false);
		String ignoreVersionEditString = this.extractXmlAttribute(element, "ignoreVersionEdit", false);
		String ignoreVersionDisplayString = this.extractXmlAttribute(element, "ignoreVersionDisplay", false);
		try {
			version = Integer.parseInt(versionString);
			if (null != version) {
				message.setVersionType(version);
			}
		} catch (Throwable t) {
			//Nothing to catch
		}
		try {
			repeatable = Boolean.parseBoolean(repeatableString);
			if (null != repeatable) {
				message.setRepeatable(repeatable);
			}
			ignoreVersionEdit = Boolean.parseBoolean(ignoreVersionEditString);
			if (null != ignoreVersionEdit) {
				message.setIgnoreVersionEdit(ignoreVersionEdit);
			}
			ignoreVersionDisplay = Boolean.parseBoolean(ignoreVersionDisplayString);
			if (null != ignoreVersionDisplay) {
				message.setIgnoreVersionDisplay(ignoreVersionDisplay);
			}
		} catch (Throwable t) {
			//Nothing to catch
		}
		return message;
	}
	
	@Override
	protected Element createRootTypeElement(IApsEntity currentEntityType) {
		Element typeElement = super.createRootTypeElement(currentEntityType);
		Message message = (Message) currentEntityType;
		typeElement.setAttribute("version", message.getVersionType().toString());
		typeElement.setAttribute("repeatable", Boolean.toString(message.isRepeatable()));
		typeElement.setAttribute("ignoreVersionEdit", Boolean.toString(message.isIgnoreVersionEdit()));
		typeElement.setAttribute("ignoreVersionDisplay", Boolean.toString(message.isIgnoreVersionDisplay()));
		return typeElement;
	}
	
	@Override
	protected String getEntityTypeRootElementName() {
		return "formtype";
	}
	
	@Override
	protected String getEntityTypesRootElementName() {
		return "formtypes";
	}

	
}