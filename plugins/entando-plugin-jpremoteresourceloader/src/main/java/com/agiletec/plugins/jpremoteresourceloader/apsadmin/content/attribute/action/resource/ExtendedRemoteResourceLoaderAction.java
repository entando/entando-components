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
package com.agiletec.plugins.jpremoteresourceloader.apsadmin.content.attribute.action.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ExtendedResourceAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ResourceAttributeActionHelper;
import com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource.IRemoteResourceLoaderAction;
import com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource.helper.RemoteResourceLoaderActionHelper;

public class ExtendedRemoteResourceLoaderAction extends ExtendedResourceAction implements IRemoteResourceLoaderAction, ResourceDataBean {

	@Override
	public void validate() {
		if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
			String resourceType = this.getResourceType();
			ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(resourceType);
			this.clearFieldErrors();
			this.loadExternalResource();
			if (this.hasFieldErrors()) return;
			this.checkRightFileType(resourcePrototype);
			if (this.hasFieldErrors()) return;
			//this.checkFileName();
			//if (this.hasFieldErrors()) return;
			this.checkDuplicateFile(resourcePrototype);
		}
	}

	/*
	protected void checkRightFileType(ResourceInterface resourcePrototype) {
		if (!this.isRightType(resourcePrototype)) {
			this.addFieldError("upload", this.getText("Resource.file.wrongFormat"));
		}
	}
	private void checkFileName() {
		String fileName = this.getFileName();
		if (!fileName.matches("[a-zA-Z_\\.0-9]+")) {
			String[] args = {fileName};
			this.addFieldError("upload", this.getText("Resource.file.wrongFileNameFormat", args));
		}
	}
	protected void checkDuplicateFile(ResourceInterface resourcePrototype) {
		resourcePrototype.setMainGroup(this.getMainGroup());
		String baseDiskFolder = resourcePrototype.getDiskFolder();
		String formFileName = this._filename;
		String fileName = null;
		if (resourcePrototype.isMultiInstance()) {
			fileName = ((AbstractMultiInstanceResource) resourcePrototype).getInstanceFileName(formFileName, 0, null);
		} else {
			fileName = ((AbstractMonoInstanceResource) resourcePrototype).getInstanceFileName(formFileName);
		}
		if ((new File(baseDiskFolder + fileName)).exists()) {
			String[] args = {formFileName};
			this.addFieldError("upload", this.getText("Resource.file.alreadyPresent", args));
		}
	}

	private boolean isRightType(ResourceInterface resourcePrototype) {
		boolean isRight = false;
		if (this._filename.length() > 0) {
			String fileName = _filename;
			String docType = fileName.substring(fileName.lastIndexOf(".")+1).trim();
			String[] types = resourcePrototype.getAllowedFileTypes();
			isRight = this.isValidType(docType, types);
		} else {
			isRight = true;
		}
		return isRight;
	}

	private boolean isValidType(String docType, String[] rightTypes) {
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
	*/

	public void loadExternalResource() {
		try {
			if (this.getUrl().startsWith("file://")) {
				this.addFieldError("url", "ResourceURL.protocolNotAllowed");
				return;
			}
			URLConnection conn = RemoteResourceLoaderActionHelper.getRemoteResourceConnection(this.getUrl(), this.getDefaultStorageDirPath());
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			String filename = null;
			String urlFileName = RemoteResourceLoaderActionHelper.extractFilenameFromUrl(this.getUrl()) ;

			if (null != urlFileName && urlFileName.length() > 0) {
				if (null == this.getAlternateName() || this.getAlternateName().trim().length() == 0) {
					filename = urlFileName;
				} else {
					//String[] fullFileName = urlFileName.split("\\.");
					String docType = urlFileName.substring(urlFileName.lastIndexOf(".")+1).trim();
					filename = new StringBuffer(this.getAlternateName()).append(".").append(docType).toString();
				}
				File file = RemoteResourceLoaderActionHelper.createFileFromInputStream(inputStream, filename);
				this.setUpload(file);
				this.setFileName(filename);
			}

			if (null == this.getFileName() || null == this.getUpload()) {
				this.addFieldError("url", this.getText("ResourceURL.file.error"));
			}
		} catch (Throwable t) {
			this.addFieldError("url", this.getText("ResourceURL.file.error"));
			ApsSystemUtils.logThrowable(t, this, "loadExternalResource");
		}
	}

	@Override
	public String newResource() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
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
			ApsSystemUtils.logThrowable(t, this, "joinRemoveCategory");
			return FAILURE;
		}
		return SUCCESS;
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	public List<Group> getAllowedGroups() {
		return this.getResourceActionHelper().getAllowedGroups(this.getCurrentUser());
	}

	@Override
	public String save() {
		try {
			this.getResourceManager().addResource(this);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String saveOnEditContent() {
		try {
			ResourceInterface resource = this.getResourceManager().addResource(this);
			//this.setResourceLangCode(this.getCurrentAttributeLang());
			ResourceAttributeActionHelper.joinResource(resource, this.getRequest());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String getCurrentAttributeLang() {
		HttpSession session = this.getRequest().getSession();
		return (String) session.getAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM);
	}

	public boolean isOnEditContent() {
		return true;
	}

	@Override
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

	@Override
	public int getFileSize() {
		return (int) this.getUpload().length()/1000;
	}

	@Override
	public InputStream getInputStream() throws Throwable {
		return new FileInputStream(this.getUpload());
	}

	@Override
	public String getResourceType() {
		return this.getResourceTypeCode();
	}

	public void setUrl(String url) {
		this._url = url;
	}
	public String getUrl() {
		return _url;
	}

	public void setFileName(String filename) {
		this._filename = filename;
	}
	public String getFilename() {
		return _filename;
	}
	@Override
	public String getFileName() {
		return this.getFilename();
	}

	public void setAlternateName(String alternateName) {
		this._alternateName = alternateName;
	}
	public String getAlternateName() {
		return _alternateName;
	}

	public void setDefaultStorageDirPath(String defaultStorageDirPath) {
		this._defaultStorageDirPath = defaultStorageDirPath;
	}
	public String getDefaultStorageDirPath() {
		return _defaultStorageDirPath;
	}

	private String _url;
	private String _filename;
	private String _alternateName;

	private String _defaultStorageDirPath;

}