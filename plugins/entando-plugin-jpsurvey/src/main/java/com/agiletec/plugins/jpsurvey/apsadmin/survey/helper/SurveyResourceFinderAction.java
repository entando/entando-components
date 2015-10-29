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
package com.agiletec.plugins.jpsurvey.apsadmin.survey.helper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.apsadmin.resource.AbstractResourceAction;

/**
 * We force the search of resources of type "Image" only which, in addiction, must belong to the free group.
 * @author M.E. Minnai
 */
public class SurveyResourceFinderAction extends AbstractResourceAction {
	
	public List<String> getResources() throws Throwable {
		List<String> resources = null;
		List<String> readOnlyFixedList = Arrays.asList(Group.FREE_GROUP_NAME);
		try {				
			resources = this.getResourceManager().searchResourcesId("Image", 
					this.getText(), this.getCategoryCode(), readOnlyFixedList);			
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getResources");
			throw t;
		}
		return resources;
	}
	
	public String getIconFile(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
		if (null != extension && extension.length() > 0) {
			Iterator<String> iter = this.getResourceTypesIconFiles().keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				List<String> extensions = Arrays.asList(key.split(","));
				if (extensions.contains(extension)) {
					return this.getResourceTypesIconFiles().get(key);
				}
			}
		}
		return this.getDefaultResourceIcon();
	}
	
	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}
	
	public String getCategoryCode() {
		if (this._categoryCode != null && this.getCategoryManager().getRoot().getCode().equals(this._categoryCode)) {
			this._categoryCode = null;
		}
		return _categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}
	
	protected Map<String, String> getResourceTypesIconFiles() {
		return _resourceTypesIconFiles;
	}
	public void setResourceTypesIconFiles(Map<String, String> resourceTypesIconFiles) {
		this._resourceTypesIconFiles = resourceTypesIconFiles;
	}
	
	protected String getDefaultResourceIcon() {
		return _defaultResourceIcon;
	}
	public void setDefaultResourceIcon(String defaultResourceIcon) {
		this._defaultResourceIcon = defaultResourceIcon;
	}
	
	private String _text;
	private String _categoryCode;
	
	private Map<String, String> _resourceTypesIconFiles;
	private String _defaultResourceIcon;
	
}
