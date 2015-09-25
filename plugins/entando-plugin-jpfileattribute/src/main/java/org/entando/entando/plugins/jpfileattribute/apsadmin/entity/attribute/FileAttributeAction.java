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
package org.entando.entando.plugins.jpfileattribute.apsadmin.entity.attribute;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.entando.entando.plugins.jpfileattribute.aps.system.entity.model.FileAttribute;
import org.entando.entando.plugins.jpfileattribute.aps.system.file.AttachedFile;
import org.entando.entando.plugins.jpfileattribute.aps.system.file.IFilePersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public abstract class FileAttributeAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FileAttributeAction.class);
	
	public String loadFile() {
		try {
			this.updateEntity();
			FileAttributeActionHelper.initSessionParams(this, this.getRequest());
		} catch (Throwable t) {
			_logger.error("error in findResource", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeFile() {
		try {
			this.updateEntity();
			FileAttributeActionHelper.initSessionParams(this, this.getRequest());
			FileAttributeActionHelper.removeResource(this.getEntity(), this.getRequest());
		} catch (Throwable t) {
			_logger.error("error in removeFile", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			AttachedFile file = new AttachedFile();
			file.setFilename(this.getUploadFileName());
			file.setContentType(this.getUploadContentType());
			file.setEntityClass(this.getEntity().getClass().getName());
			file.setApproved(false);
			file.setUsername(this.getCurrentUser().getUsername());
			file.setDate(new Date());
			byte[] bytes = IOUtils.toByteArray(this.getInputStream());
			file.setBase64(bytes);
			this.getPersistenceManager().addFile(file);
			this.buildEntryEntityAnchorDest();
			FileAttributeActionHelper.joinResource(this.getEntity(), file, this.getRequest());
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void buildEntryEntityAnchorDest() {
		//nothing to do
	}
	
	protected abstract void updateEntity() throws ApsException;
	
	protected abstract IApsEntity getEntity();
	
	public String viewFile() {
		try {
			String attributeName = (null != this.getParentAttributeName()) ? this.getParentAttributeName() : this.getAttributeName();
			AttributeInterface attribute = (AttributeInterface) this.getEntity().getAttribute(attributeName);
			if(attribute instanceof AbstractListAttribute){
				attribute = ((AbstractListAttribute) attribute).getAttributes().get(this.getElementIndex());
			} 
			FileAttribute fileAttribute = this.extractResourceAttribute(attribute);
			if (null != fileAttribute) {
				AttachedFile attachedFile = fileAttribute.getAttachedFile();
				UserDetails currentuser = this.getCurrentUser();
				if (null != attachedFile) {
					if (attachedFile.getUsername().equals(currentuser.getUsername()) 
							|| super.getAuthorizationManager().isAuthOnPermission(currentuser, Permission.SUPERUSER)) {
						this.setUploadContentType(attachedFile.getContentType());
						this.setUploadInputStream(new ByteArrayInputStream(attachedFile.getBase64()));
						this.setUploadFileName(attachedFile.getFilename());
					}
				}
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
	
	/**
	 * Associa la risorsa all'attributo del contenuto o all'elemento dell'attributo lista
	 * o all'elemento dell'attributo Composito (sia semplice che in lista).
	 */
	private FileAttribute extractResourceAttribute(AttributeInterface attribute) {
		if (attribute instanceof CompositeAttribute) {
			AttributeInterface includedAttribute = ((CompositeAttribute) attribute).getAttribute(this.getAttributeName());
			return this.extractResourceAttribute(includedAttribute);
		} else if (attribute instanceof FileAttribute) {
			return (FileAttribute) attribute;
		} else if (attribute instanceof MonoListAttribute) {
			AttributeInterface attributeElement = ((MonoListAttribute) attribute).getAttribute(this.getElementIndex());
			return this.extractResourceAttribute(attributeElement);
		} else return null;
	}
	
    public void setUpload(File file) {
       this._file = file;
    }
    public File getUpload() {
		return this._file;
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
	
	public int getFileSize() {
		return (int) this._file.length()/1000;
	}
	
	public File getFile() {
		return _file;
	}
	
	public InputStream getInputStream() throws Throwable {
		if (null == this.getFile()) return null;
		return new FileInputStream(this.getFile());
	}
	
	public String getAttributeName() {
		return _attributeName;
	}
	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}
	
	public String getParentAttributeName() {
		return _parentAttributeName;
	}
	public void setParentAttributeName(String parentAttributeName) {
		this._parentAttributeName = parentAttributeName;
	}
	
	public int getElementIndex() {
		return _elementIndex;
	}
	public void setElementIndex(int elementIndex) {
		this._elementIndex = elementIndex;
	}
	
	protected IFilePersistenceManager getPersistenceManager() {
		return _persistenceManager;
	}
	public void setPersistenceManager(IFilePersistenceManager persistenceManager) {
		this._persistenceManager = persistenceManager;
	}
	
	private File _file;
    private String _uploadContentType;
    private String _uploadFileName;
	
	private InputStream _uploadInputStream;
    
	private String _attributeName;
	private String _parentAttributeName;
	private int _elementIndex = -1;
	
	private IFilePersistenceManager _persistenceManager;
	
}
