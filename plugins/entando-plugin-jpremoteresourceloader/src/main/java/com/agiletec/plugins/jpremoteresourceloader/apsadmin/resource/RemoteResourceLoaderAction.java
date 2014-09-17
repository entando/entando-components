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
package com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceAction;
import com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource.helper.RemoteResourceLoaderActionHelper;

public class RemoteResourceLoaderAction extends ResourceAction implements IRemoteResourceLoaderAction, ResourceDataBean {
	
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

	public void loadExternalResource() {
		try {
			if (this.getUrl().startsWith("file://")) {
				this.addFieldError("url", "ResourceURL.protocolNotAllowed");
				return;
			}
			InputStream inputStream = null;
			try {
				URLConnection conn = RemoteResourceLoaderActionHelper.getRemoteResourceConnection(this.getUrl(), this.getDefaultStorageDirPath());
				conn.connect();	
				inputStream = conn.getInputStream();
			} catch (ConnectException ce) {
				this.addFieldError("url", this.getText("jpremoteresourceloader.ResourceURL.file.ConnectionProblem"));
				ApsSystemUtils.logThrowable(ce, this, "loadExternalResource");
				return;
			} catch (IOException ioe) {
				this.addFieldError("url", this.getText("jpremoteresourceloader.ResourceURL.file.ConnectionProblem"));
				ApsSystemUtils.logThrowable(ioe, this, "loadExternalResource");
				return;
			}      
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

	//	public String saveOnEditContent() {
	//		try {
	//			ResourceInterface resource = this.getResourceManager().addResource(this);
	//			this.setResourceLangCode(this.getCurrentAttributeLang());
	//			ResourceAttributeActionHelper.joinResource(resource, this.getRequest());
	//		} catch (Throwable t) {
	//			ApsSystemUtils.logThrowable(t, this, "save");
	//			return FAILURE;
	//		}
	//		return SUCCESS;
	//	}
	//
	//	public String getCurrentAttributeLang() {
	//		HttpSession session = this.getRequest().getSession();
	//		return (String) session.getAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM);
	//	}
	//
	//	public boolean isOnEditContent() {
	//		return true;
	//	}

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
	/*
	protected void checkRightFileType(ResourceInterface resourcePrototype) {
		if (!this.isRightType(resourcePrototype)) {
			this.addFieldError("upload", this.getText("Resource.file.wrongFormat"));
		}
	}
*/
	/*
	private void checkFileName() {
		String fileName = this.getFileName();
		if (!fileName.matches("[a-zA-Z_\\.0-9]+")) {
			String[] args = {fileName};
			this.addFieldError("upload", this.getText("Resource.file.wrongFileNameFormat", args));
		}
	}
	@Override
	protected void checkDuplicateFile(ResourceInterface resourcePrototype) {
		String formFileName = this._filename;
		try {
			resourcePrototype.setMainGroup(this.getMainGroup());
			if (resourcePrototype.exists(formFileName)) {
				String[] args = {formFileName};
				this.addFieldError("upload", this.getText("Resource.file.alreadyPresent", args));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkDuplicateFile", 
					"Error while check duplicate file - master file name '" + formFileName + "'");
		}
	}
	 */
/*
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

	@Override
	public int getFileSize() {
		return (int) this.getUpload().length()/1000;
	}

	@Override
	public InputStream getInputStream() throws Throwable {
		return new FileInputStream(this.getUpload());
	}

	//	@Override
	//	public String getMimeType() {
	//		return super.getcon
	//	}

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

	//	public void setUpload(File upload) {
	//		this._upload = upload;
	//	}
	//	public File getUpload() {
	//		return _upload;
	//	}

	//	public void setCategoryCodes(List<String> categoryCodes) {
	//		this._categoryCodes = categoryCodes;
	//	}
	//	public List<String> getCategoryCodes() {
	//		return _categoryCodes;
	//	}

	//	public void setDescr(String descr) {
	//		this._descr = descr;
	//	}
	//	public String getDescr() {
	//		return _descr;
	//	}

	//	public void setMainGroup(String mainGroup) {
	//		this._mainGroup = mainGroup;
	//	}
	//	public String getMainGroup() {
	//		return _mainGroup;
	//	}

	//	public void setContentType(String contentType) {
	//		this._contentType = contentType;
	//	}
	//	public String getContentType() {
	//		return _contentType;
	//	}

	//	public void setCategoryCode(String categoryCode) {
	//		this._categoryCode = categoryCode;
	//	}
	//	public String getCategoryCode() {
	//		return _categoryCode;
	//	}

	//	public String getResourceLangCode() {
	//		return _resourceLangCode;
	//	}
	//	public void setResourceLangCode(String resourceLangCode) {
	//		this._resourceLangCode = resourceLangCode;
	//	}

	public void setAlternateName(String alternateName) {
		this._alternateName = alternateName;
	}
	public String getAlternateName() {
		return _alternateName;
	}

	//private String _resourceLangCode;

	public void setDefaultStorageDirPath(String defaultStorageDirPath) {
		this._defaultStorageDirPath = defaultStorageDirPath;
	}
	public String getDefaultStorageDirPath() {
		return _defaultStorageDirPath;
	}

	private String _url;
	private String _filename;
	//private File _upload;
	//private List<String> _categoryCodes = new ArrayList<String>();
	//private String _descr;
	//private String _mainGroup;
	//private String _contentType;
	private String _alternateName;

	//private String _categoryCode;

	private String _defaultStorageDirPath;

}