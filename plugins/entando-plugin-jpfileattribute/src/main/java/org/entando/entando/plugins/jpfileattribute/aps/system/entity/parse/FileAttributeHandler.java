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
package org.entando.entando.plugins.jpfileattribute.aps.system.entity.parse;

import org.entando.entando.plugins.jpfileattribute.aps.system.entity.model.FileAttribute;
import org.entando.entando.plugins.jpfileattribute.aps.system.file.AttachedFile;
import org.entando.entando.plugins.jpfileattribute.aps.system.file.IFilePersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.attribute.AbstractAttributeHandler;

/**
 * Handler class that interprets the XML defining a Text that DOES NOT support multiple languages
 * ('Monotext Attribute')
 * @author E.Santoboni
 */
public class FileAttributeHandler extends AbstractAttributeHandler {

	private static final Logger _logger =  LoggerFactory.getLogger(FileAttributeHandler.class);

	@Override
	public Object getAttributeHandlerPrototype() {
		FileAttributeHandler handler = (FileAttributeHandler) super.getAttributeHandlerPrototype();
		handler.setFilePersistenceManager(this.getFilePersistenceManager());
		return handler;
	}
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("file")) {
			startFile(attributes, qName);
		} else if (qName.equals("id")) {
			startId(attributes, qName);
		} else if (qName.equals("filename")) {
			startFilename(attributes, qName);
		} //else if (qName.equals("base64")) {
		//	startBase64(attributes, qName);
		//}
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("file")) {
			endFile();
		} else if (qName.equals("id")) {
			endId(textBuffer);
		} else if (qName.equals("filename")) {
			endFilename(textBuffer);
		}// else if (qName.equals("base64")){
		//	endBase64(textBuffer);
		//}
	}
	
	private void startFile(Attributes attributes, String qName) {
		//nothing to do
	}
	
	private void startId(Attributes attributes, String qName) {
		//nothing to do
	}
	
	private void startFilename(Attributes attributes, String qName) {
		//nothing to do
	}
	/*
	private void startBase64(Attributes attributes, String qName) {
		//nothing to do
	}
	*/
	private void endId(StringBuffer textBuffer) {
		if (null != textBuffer) {
			try {
				this._id = Integer.parseInt(textBuffer.toString());
			} catch (Exception e) {}
		}
	}
	/*
	private void endBase64(StringBuffer textBuffer) {
		if (null != textBuffer) {
			this._base64 = textBuffer.toString().getBytes();
		}
	}
	*/
	private void endFilename(StringBuffer textBuffer) {
		//if (null != textBuffer) {
		//	this._filename = textBuffer.toString();
		//}
	}
	
	private void endFile() {
		if (null != this._id) {
			FileAttribute attribute = (FileAttribute) this.getCurrentAttr();
			try {
				AttachedFile file = this.getFilePersistenceManager().loadFile(this._id);
				attribute.setAttachedFile(file);
			} catch (Throwable t) {
				_logger.error("error in endFile", t);
				//throw new SAXException("Error extracting Attached File", new Exception(t));
			}
		}
	}
	
	protected IFilePersistenceManager getFilePersistenceManager() {
		return _filePersistenceManager;
	}
	public void setFilePersistenceManager(IFilePersistenceManager filePersistenceManager) {
		this._filePersistenceManager = filePersistenceManager;
	}
	
	private Integer _id;
	//private String _filename;
	//private byte[] _base64;
	
	private IFilePersistenceManager _filePersistenceManager;
	
}
