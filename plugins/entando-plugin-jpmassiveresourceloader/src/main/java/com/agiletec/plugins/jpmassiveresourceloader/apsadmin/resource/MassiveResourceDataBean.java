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
package com.agiletec.plugins.jpmassiveresourceloader.apsadmin.resource;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;

/**
 * @author E.Santoboni
 */
public class MassiveResourceDataBean implements ResourceDataBean {
	
	@Override
	public String getResourceId() {
		return _resourceId;
	}
	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}
	
	@Override
	public String getResourceType() {
		return _resourceType;
	}
	public void setResourceType(String resourceType) {
		this._resourceType = resourceType;
	}
	
	@Override
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	
	@Override
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	@Override
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	@Override
	public String getFileName() {
		return _fileName;
	}
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}
	
	@Override
	public InputStream getInputStream() {
		return _inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	
	@Override
	public int getFileSize() {
		return _fileSize;
	}
	public void setFileSize(int fileSize) {
		this._fileSize = fileSize;
	}
	
	@Override
	public List<Category> getCategories() {
		return _categories;
	}
	public void setCategories(List<Category> categories) {
		this._categories = categories;
	}
	
	@Override
	public File getFile() {
		return _file;
	}
	public void setFile(File file) {
		this._file = file;
	}
	
	private String _resourceId;
	
	private String _resourceType;
	private String _mimeType;
	private String _descr;
	private String _mainGroup;
	private String _fileName;
	private InputStream _inputStream;
	private int _fileSize;
	private List<Category> _categories;
	
	private File _file;
	
}