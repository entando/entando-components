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
package org.entando.entando.plugins.jpwebform.apsadmin.message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.entando.entando.plugins.jpfileattribute.aps.system.file.AttachedFile;
import org.entando.entando.plugins.jpfileattribute.aps.system.file.IFilePersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 *
 * @author S.Loru
 */
public class FileAttributeMessageAction extends BaseAction{

	private static final Logger _logger =  LoggerFactory.getLogger(FileAttributeMessageAction.class);

	public String viewFile() {
		try {
			AttachedFile attachedFile = this.getPersistenceManager().loadFile(this.getId());
			if (null != attachedFile) {
				this.setUploadContentType(attachedFile.getContentType());
				this.setUploadInputStream(new ByteArrayInputStream(attachedFile.getBase64()));
				this.setUploadFileName(attachedFile.getFilename());
			}
			if (null == this.getUploadInputStream()) {
				//TODO message
				return "nullFile";
			}
		} catch (Throwable t) {
			_logger.error("error in viewFile", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}
	
	public String getUploadContentType() {
		return _uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this._uploadContentType = uploadContentType;
	}
	
	public String getUploadFileName() {
		return _uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this._uploadFileName = uploadFileName;
	}
	
	public InputStream getUploadInputStream() {
		return _uploadInputStream;
	}
	public void setUploadInputStream(InputStream uploadInputStream) {
		this._uploadInputStream = uploadInputStream;
	}
	
	protected IFilePersistenceManager getPersistenceManager() {
		return _persistenceManager;
	}
	public void setPersistenceManager(IFilePersistenceManager persistenceManager) {
		this._persistenceManager = persistenceManager;
	}
	
	private Integer _id;
	
    private String _uploadContentType;
    private String _uploadFileName;
	private InputStream _uploadInputStream;
    
	private IFilePersistenceManager _persistenceManager;
	
}
