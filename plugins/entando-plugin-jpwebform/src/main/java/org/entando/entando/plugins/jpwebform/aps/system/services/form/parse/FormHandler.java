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

import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.EntityHandler;

/**
 * @author E.Santoboni
 */
public class FormHandler extends EntityHandler {

	private static final Logger _logger =  LoggerFactory.getLogger(FormHandler.class);

	@Override
	protected void startEntityElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if (qName.equals("versionType")) {
				this.startVersionType(attributes, qName);
			} else if (qName.equals("lastCompletedStep")) {
				this.startLastCompletedStep(attributes, qName);
			}
		} catch (SAXException e) {
			_logger.error("error in startElement", e);
			throw e;
		} catch (Throwable t) {
			_logger.error("error in startElement", t);
			throw new SAXException(t.getMessage());
		}
	}

	@Override
	protected void endEntityElement(String uri, String localName, String qName) throws SAXException {
		try {
			if (qName.equals("versionType")) {
				this.endVersionType();
			} else if (qName.equals("lastCompletedStep")) {
				this.endLastCompletedStep();
			}
		} catch (Throwable t) {
			_logger.error("error in endEntityElement" ,t);
			throw new SAXException(t.getMessage());
		}
	}

	private void startLastCompletedStep(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}

	private void startVersionType(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}


	private void endLastCompletedStep() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			((Message) this.getCurrentEntity()).setLastCompletedStep(textBuffer.toString());
		}
	}

	private void endVersionType() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			Integer version = null;
			try {
				version = Integer.parseInt(textBuffer.toString());
			} catch (Exception e) {}
			((Message) this.getCurrentEntity()).setVersionType(version);
		}
	}

}