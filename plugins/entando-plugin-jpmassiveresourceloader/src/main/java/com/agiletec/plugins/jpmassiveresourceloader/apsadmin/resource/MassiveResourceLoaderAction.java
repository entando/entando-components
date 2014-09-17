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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.ApsFileUploadInterceptor;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.resource.AbstractResourceAction;

/**
 * @author E.Santoboni
 */
public class MassiveResourceLoaderAction extends AbstractResourceAction implements IMassiveResourceLoaderAction {

	private static final Logger _logger = LoggerFactory.getLogger(MassiveResourceLoaderAction.class);
	
	@Override
	public String save() {
		try {
			MassiveResourceDataBean bean = this.prepareDataBean();
			ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceTypeCode());
			File folder = new File(this.getFolder());
			this.addFolder(folder, "/", this.isRecursive(), bean, resourcePrototype);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void addFolder(File folder, String folderName, boolean recursive, MassiveResourceDataBean bean,
			ResourceInterface resourcePrototype) throws Exception {
		File[] files = folder.listFiles();
		if (null == files) return;
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				String newFolderName = folderName+file.getName()+"/";
				if (!recursive) {
					String[] args = {newFolderName};
					this.addActionError(this.getText("jpmassiveresourceloader.Resource.file.directoryNotExplored", args));
				} else {
					this.addFolder(file, newFolderName, recursive, bean, resourcePrototype);
				}
			} else {
				if (this.checkFile(file, folderName, resourcePrototype)) {
					this.completeDataBean(bean, resourcePrototype, file);
					this.getResourceManager().addResource(bean);
					String[] args = {folderName+file.getName()};
					this.addActionMessage(this.getText("jpmassiveresourceloader.Resource.file.added", args));
				}
			}
		}
	}
	
	@Override
	public String joinCategory() {
		return this.joinRemoveCategory(true, this.getCategoryCode());
	}
	
	@Override
	public String removeCategory() {
		return this.joinRemoveCategory(false, this.getCategoryCode());
	}
	
	private String joinRemoveCategory(boolean isJoin, String categoryCode) {
		try {
			Category category = this.getCategory(categoryCode);
			if (category == null) return SUCCESS;
			List<String> categories = this.getCategoryCodes();
			if (isJoin) {
				if (!categories.contains(categoryCode)) {
					categories.add(categoryCode);
				}
			} else {
				categories.remove(categoryCode);
			}
		} catch (Throwable t) {
			_logger.error("error in joinRemoveCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected boolean checkFile(File file, String folderName, ResourceInterface resourcePrototype) {
		String fileName = file.getName();
		String[] args = {folderName + fileName};
		boolean success = true;
		if (!this.isRightType(fileName, resourcePrototype)) {
			this.addActionError(this.getText("jpmassiveresourceloader.Resource.file.wrongFormat", args));
			success = false;
		} else {
			if (!file.getName().matches("[_\\.a-zA-Z0-9]+")) {
				this.addActionError(this.getText("jpmassiveresourceloader.Resource.file.wrongFileNameFormat", args));
				success = false;
			}
			if (this.getMaximumSize()<file.length()) {
				this.addActionError(this.getText("jpmassiveresourceloader.Resource.file.tooBigFileLength", args));
				success = false;
			}
			if (this.isDuplicatedFile(fileName, resourcePrototype)) {
		    	this.addActionError(this.getText("jpmassiveresourceloader.Resource.file.alreadyPresent", args));
				success = false;
			}
		}
		return success;
	}
	
	protected boolean isRightType(String fileName, ResourceInterface resourcePrototype) {
		boolean isRight = false;
		if (fileName.length() > 0) {
			String docType = fileName.substring(fileName.lastIndexOf('.')+1).trim();
			String[] types = resourcePrototype.getAllowedFileTypes();
			isRight = this.isValidType(docType, types);
		} else {
			isRight = true;
		}
		return isRight;
	}
	
	protected boolean isDuplicatedFile(String fileName, ResourceInterface resourcePrototype) {
		boolean isDuplicated = false;
		resourcePrototype.setMainGroup(this.getMainGroup());
    	try {
    		isDuplicated = resourcePrototype.exists(fileName);
		} catch (Throwable t) {
			_logger.error("Error while check duplicate file - master file name '{}'",fileName, t);
		}
    	return isDuplicated;
	}
	
	public List<Group> getAllowedGroups() {
		return this.getResourceActionHelper().getAllowedGroups(this.getCurrentUser());
	}
	
	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}
	
	protected MassiveResourceDataBean prepareDataBean() {
		MassiveResourceDataBean bean = new MassiveResourceDataBean();
		bean.setCategories(this.getCategories());
		bean.setMainGroup(this.getMainGroup());
		bean.setResourceType(this.getResourceTypeCode());
		return bean;
	}
	
	protected void completeDataBean(MassiveResourceDataBean bean, ResourceInterface resourcePrototype, File file) throws FileNotFoundException {
		bean.setDescr(this.prepareDescr(file));
		bean.setFileName(file.getName());
		bean.setFileSize((int) file.length()/1000);
		bean.setInputStream(new FileInputStream(file));
		bean.setMimeType(this._mimeTypes.getContentType(file));
	}
	
	protected String prepareDescr(File file) {
		String descr = this.getDescr() + " " + file.getName();
		descr = descr.length()>50 ? descr.substring(0, 50) : descr;
		return descr;
	}
	
	protected boolean isValidType(String docType, String[] rightTypes) {
		boolean isValid = false;
		if (rightTypes.length > 0) {
			for (int i=0; i<rightTypes.length; i++) {
				if (docType.toLowerCase().equals(rightTypes[i])) {
					isValid = true;
					break;
				}
			}
		} else {
			isValid = true;
		}
		return isValid;
	}
	
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>(this.getCategoryCodes().size());
		Iterator<String> iter = this.getCategoryCodes().iterator();
		while (iter.hasNext()) {
			String categoryCode = iter.next();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) categories.add(category);
		}
		return categories;
	}
	
	public String getFolder() {
		return _folder;
	}
	public void setFolder(String folder) {
		this._folder = folder;
	}
	
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	public String getMainGroup() {
		return this._mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public List<String> getCategoryCodes() {
		return _categoryCodes;
	}
	public void setCategoryCodes(List<String> categoryCodes) {
		this._categoryCodes = categoryCodes;
	}
	
	public boolean isRecursive() {
		return _recursive;
	}
	public void setRecursive(boolean recursive) {
		this._recursive = recursive;
	}
	
	public String getCategoryCode() {
		return _categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}
	
	public long getMaximumSize() {
		if (this._maximumSize<=0) {
			ConfigInterface configManager = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, ServletActionContext.getRequest());
			String maxSizeParam = configManager.getParam(SystemConstants.PAR_FILEUPLOAD_MAXSIZE);
			if (null != maxSizeParam) {
				try {
					this._maximumSize = Long.parseLong(maxSizeParam);
				} catch (Throwable t) {
					_logger.error("Error parsing param 'maxSize' - value '{}' - message ", maxSizeParam , t);
				}
			}
		}
		if (this._maximumSize<=0) {
			this._maximumSize = ApsFileUploadInterceptor.DEFAULT_MAX_SIZE;
		}
		return this._maximumSize;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	private String _folder;
	private String _descr = "";
	private String _mainGroup;
	private List<String> _categoryCodes = new ArrayList<String>();
	private boolean _recursive;
	
	private String _categoryCode;
	private long _maximumSize;
	
	private IGroupManager _groupManager;
	private MimetypesFileTypeMap _mimeTypes = new MimetypesFileTypeMap();
	
}