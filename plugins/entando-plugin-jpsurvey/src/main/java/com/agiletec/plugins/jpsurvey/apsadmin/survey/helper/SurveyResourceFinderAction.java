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
package com.agiletec.plugins.jpsurvey.apsadmin.survey.helper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.group.Group;
//import com.agiletec.apsadmin.resource.AbstractResourceAction;
//import com.agiletec.apsadmin.resource.IResourceFinderAction;
import com.agiletec.plugins.jacms.apsadmin.resource.AbstractResourceAction;
import com.agiletec.plugins.jacms.apsadmin.resource.IResourceFinderAction;


/**
 * We force the search of resources of type "Image" only which, in addiction, must belong to the free group.
 * @author M.E. Minnai
 */
public class SurveyResourceFinderAction extends AbstractResourceAction implements IResourceFinderAction {

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
