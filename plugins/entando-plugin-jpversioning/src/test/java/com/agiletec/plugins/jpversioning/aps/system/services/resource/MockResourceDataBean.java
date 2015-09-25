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
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;

/**
 * @author E.Santoboni
 */
public class MockResourceDataBean implements ResourceDataBean {
	
	@Override
	public String getResourceId() {
		return _resourceId;
	}
	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}
	
	/**
	 * Restituisce il tipo di risorsa.
	 * @return Il tipo di risorsa.
	 */
	public String getResourceType() {
		return _resourceType;
	}
	
	/**
	 * Setta il tipo di risorsa.
	 * @param type Il tipo di risorsa.
	 */
	public void setResourceType(String type) {
		this._resourceType = type;
	}
	
	/**
	 * Setta la descrizione della risorsa.
	 * @param descr La descrizione della risorsa.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	/**
	 * Restituisce la descrizione della risorsa.
	 * @return La descrizione della risorsa.
	 */
	public String getDescr() {
		return _descr;
	}
	
	/**
	 * Setta il file relativo alla risorsa.
	 * @param file Il file relativo alla risorsa.
	 */
	public void setFile(File file) {
		this._file = file;
	}
	
	public String getMainGroup() {
		return _mainGroup;
	}
	
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public List<Category> getCategories() {
		return _categories;
	}
	
	public void setCategories(List<Category> categories) {
		this._categories = categories;
	}
	
	public int getFileSize() {
		return (int) this._file.length()/1000;
	}
	
	public InputStream getInputStream() throws Throwable {
		return new FileInputStream(this._file);
	}
	
	public String getFileName() {
		String filename = _file.getName().substring(_file.getName().lastIndexOf('/')+1).trim();
		return filename;
	}
	
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimetype) {
		this._mimeType = mimetype;
	}
	
	@Override
	public File getFile() {
		return _file;
	}
	
	private String _resourceId;

	private String _resourceType;
	private String _descr;
	private String _mainGroup;
	private File _file;
	private List<Category> _categories = new ArrayList<Category>();
	private String _mimeType;
	
}
