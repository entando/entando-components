/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs.common.DocResourceDataBean;
import org.entando.entando.plugins.jpsharedocs.aps.system.services.sharedocs.ISharedocsManager;
import org.entando.entando.plugins.jpsharedocs.aps.system.services.sharedocs.SharedocsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.opensymphony.xwork2.ActionSupport;

public class DocumentActionHelper implements IDocumentActionHelper {
	
	private static Logger _logger = LoggerFactory.getLogger(DocumentActionHelper.class);
	

	@Override
	public String getContentType(HttpServletRequest request) {
		String typeCode = null;
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		if (reqCtx!=null) {
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (widget!=null) {
				ApsProperties config = widget.getConfig();
				if (null!=config) {
					String widgetTypeCode = config.getProperty("typeCode");
					if (widgetTypeCode!=null && widgetTypeCode.trim().length()>0) {
						typeCode = widgetTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}
	
	@Override
	public String getUserCompleteName(UserDetails currentUser) {
		String completeName = null;
		Object profile = currentUser.getProfile();
		if (profile!=null && profile instanceof IUserProfile) {
			IUserProfile userProfile = (IUserProfile) profile;
			Object value = userProfile.getValue("NomeVisualizzato");
			if (value != null) {
				completeName = value.toString();
			}
		}
		if (completeName == null || completeName.length() == 0) {
			completeName = currentUser.getUsername();
		}
		return completeName;
	}
	
	@Override
	public boolean checkAllowedOnContent(Content content, UserDetails currentUser, HttpServletRequest request, ActionSupport action) {
		boolean allowed = false;
		String typeCode = this.getContentType(request);
		
		if (content != null 
				&& content.getTypeCode().equals(typeCode)) {
			_logger.info("Checking permission of the current user against '{}'", content.getMainGroup());
			/*
			IApsAuthority[] auths = currentUser.getAuthorities();
			for (int i=0; i < auths.length; i++) {
				_logger.debug("   current user: '{}'", auths[i].getAuthority());
			}
			*/
			IAuthorizationManager authManager = this.getAuthorizationManager();
			_logger.info("isAuthOnGroup: '{}'", authManager.isAuthOnGroup(currentUser, content.getMainGroup()));
			
			allowed = authManager.isAuthOnGroup(currentUser, content.getMainGroup()) 
					|| authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME);
		}
		if (!allowed) {
			_logger.info("User has no permission to operate on contents!");
			action.addActionError(action.getText("Errors.contentEdit.notAllowed"));
		}
		return allowed;
	}
	
	@Override
	public List<Group> getAllowedGroups(UserDetails currentUser) {
		IAuthorizationManager authManager = this.getAuthorizationManager();
		List<Group> groups = new ArrayList<Group>();
		if (authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME)) {
			Collection<Group> collGroup = this.getGroupManager().getGroups();
			groups.addAll(collGroup);
		} else {
			List<Group> groupsOfUser = authManager.getUserGroups(currentUser);
			groups.addAll(groupsOfUser);
			if (!authManager.isAuthOnGroup(currentUser, Group.FREE_GROUP_NAME)) {
				groups.add(this.getGroupManager().getGroup(Group.FREE_GROUP_NAME));
			}
		}
		BeanComparator comparator = new BeanComparator("descr");
		Collections.sort(groups, comparator);
    	return groups;
	}
	
	@Override
	public boolean checkCategories(Collection<String> categories, ActionSupport action) {
		boolean checkOk = true;
		if (categories!=null && categories.size()>0) {
			ICategoryManager categoryManager = this.getCategoryManager();
			for (String categoryCode : categories) {
				if (categoryManager.getCategory(categoryCode) == null) {
					action.addActionError(action.getText("Errors.categories.wrongCategory", new String[] { categoryCode }));
					checkOk = false;
				}
			}
//		} else {// Commentato per non essere troppo restrittivi
//			action.addActionError(action.getText("Errors.categories.emptyList"));
//			checkOk = false;
		}
		return checkOk;
	}
	
	public boolean checkGroups(Content content, UserDetails currentUser, ActionSupport action) {
		// TODO Andrà a sostituire l'attuale controllo sui gruppi
		boolean checkOk = true;
		IGroupManager groupManager = this.getGroupManager();
		
		if (!this.isEdit(content)) {
			String mainGroup = content.getMainGroup();
			Group group = this.getGroupManager().getGroup(mainGroup);
			if (group == null || (!group.getName().equals(Group.FREE_GROUP_NAME) && !this.getAuthorizationManager().isAuth(currentUser, group))) {
				String[] args = new String[] { (group!=null ? group.getDescr() : mainGroup) };
				action.addFieldError("content.mainGroup", action.getText("Errors.mainGroup.wrong", args));
			}
		}
		
		for (String groupName : content.getGroups()) {
			Group group = groupManager.getGroup(groupName);
			if (group == null) {
				String[] args = new String[] { groupName };
				action.addActionError(action.getText("Errors.groups.wrongGroup", args));
				checkOk = false;
			}
		}
		return checkOk;
	}
	
	@Override
	public boolean checkGroups(Collection<String> groups, UserDetails currentUser, ActionSupport action) {
		boolean checkOk = true;
		IGroupManager groupManager = this.getGroupManager();
		for (String groupName : groups) {
			Group group = groupManager.getGroup(groupName);
			if (group == null) {
				String[] args = new String[] { groupName };
				action.addActionError(action.getText("Errors.groups.wrongGroup", args));
				checkOk = false;
			}
		}
		return checkOk;
	}
	
	@Override
	public boolean checkUploadedDocument(File document, String fileName, ActionSupport action) {
		ResourceInterface resourcePrototype = this.getResourceManager().createResourceType("Attach");
		boolean checkOk = this.checkRightFileType(fileName, resourcePrototype, action);
		if (checkOk) {
			checkOk = this.checkFileName(fileName, action);
		}
		return checkOk;
	}
	
	@Override
	public String saveTemporaryFile(File document, String fileName) throws ApsSystemException {
		try {
			this.prepareTmpFolder();
			File tmpFile = this.getTmpFile(fileName);
    		InputStream stream = new BufferedInputStream(new FileInputStream(document));
    		FileOutputStream outStream = new FileOutputStream(tmpFile);
    		while (stream.available() > 0) {
    			outStream.write(stream.read());
    		}
    		outStream.close();
    		stream.close();
    		return tmpFile.getName();
    	} catch (Throwable t) {
    		throw new ApsSystemException("Error saving file", t);
    	}
	}
	
	@Override
	public File getTemporaryFile(String fileName) {
		File document = null;
		if (fileName != null) {
			fileName = fileName.trim();
			if (fileName.length()>0 && fileName.charAt(0) != '.' && !fileName.contains("\\") && !fileName.contains("/")) { // verifica la presenza di caratteri "pericolosi"
				String filePath = this.getTempFolderPath() + File.separator + fileName;
				File tmpFile = new File(filePath);
				if (tmpFile.exists() && tmpFile.isFile()) {
					document = tmpFile;
				}
			}
		}
		return document;
	}
	
	private File getTmpFile(String fileName) {
		StringBuffer buffer = new StringBuffer(this.getTempFolderPath());
		buffer.append(File.separator);
		buffer.append(DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss"));
		buffer.append("_");
		buffer.append(fileName);
		
		String tmpFileName = buffer.toString();
		File tmpFile = new File(tmpFileName);
		int index = 0;
		while (tmpFile.exists()) {
			tmpFile = new File(tmpFileName + index);
			index++;
		}
		return tmpFile;
	}
	
	private void prepareTmpFolder() {
		File folder = new File(this.getTempFolderPath());
		if (!folder.exists()) {
			folder.mkdirs();
		} else {
			// Clean old files
			String prefix = (DateConverter.getFormattedDate(new Date(new Date().getTime() - 3600000), "yyyyMMddHHmmss")); // Più vecchio di un'ora
			for (File file : folder.listFiles()) {
				if (file.isFile() && file.getName().compareTo(prefix)<0) {
					file.delete();
				}
			}
		}
	}
	
	@Override
	public ResourceDataBean createResource(File document, String fileName, String description, 
			String mainGroup, String mimeType) throws ApsSystemException {
		DocResourceDataBean resourceBean = new DocResourceDataBean();
		
		List<Category> categories = new ArrayList<Category>();
		// TODO Verificare come modificare
//		Category documenti = this.getCategoryManager().getCategory("documenti");
//		categories.add(documenti);
		resourceBean.setCategories(categories);
		
		// descrizione priva di apici, lunga massimo 100
		description = description.replaceAll("[\"']", " ");
		description = description.length()>100 ? description.substring(0, 100) : description;
		resourceBean.setDescr(description.trim());
		resourceBean.setFile(document);
		resourceBean.setFileName(this.getUniqueFileName(fileName, mainGroup));
		resourceBean.setMainGroup(mainGroup);
		resourceBean.setMimeType(mimeType);
		resourceBean.setResourceType("Attach");
		
		return resourceBean;
	}
	
	private String getUniqueFileName(String fileName, String mainGroup) throws ApsSystemException {
		ResourceInterface resourcePrototype = this.getResourceManager().createResourceType("Attach");
		resourcePrototype.setMainGroup(mainGroup);
    	String baseName = fileName.substring(0, fileName.lastIndexOf("."));
    	String extension = fileName.substring(fileName.lastIndexOf('.')).trim();
    	int index = 1;
		while (resourcePrototype.exists(fileName)) {
			StringBuffer buffer = new StringBuffer(baseName);
        	buffer.append("_");
        	buffer.append(index++);
        	buffer.append(extension);
    		fileName = buffer.toString();
		}
    	return fileName;
	}
	
	private boolean checkRightFileType(String fileName, ResourceInterface resourcePrototype, ActionSupport action) {
		if (!this.isRightType(fileName, resourcePrototype)) {
			action.addFieldError("document", action.getText("Errors.document.wrongFormat"));
			return false;
		}
		return true;
	}
	
	private boolean checkFileName(String fileName, ActionSupport action) {
		if (fileName.charAt(0) == '.' || !fileName.matches("[a-zA-Z_\\.0-9]+")) {
			String[] args = {fileName};
			action.addFieldError("document", action.getText("Errors.document.wrongFileNameFormat", args));
			return false;
		}
		return true;
	}
	
	private boolean isRightType(String fileName, ResourceInterface resourcePrototype) {
		boolean isRight = false;
		if (fileName != null && fileName.length() > 0) {
			String docType = fileName.substring(fileName.lastIndexOf('.')+1).trim();
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
	
	private boolean isEdit(Content content) {
		String contentId = content.getId();
		return contentId != null && contentId.length() > 0;
	}
	
	
	protected String getTempFolderPath() {
		SharedocsConfig config = this.getSharedocsManager().getConfiguration();
		
		return config.getTmpFolderPath();
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	/**
	 * Restituisce il manager dei gruppi.
	 * @return Il manager dei gruppi.
	 */
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	/**
	 * Setta il manager dei gruppi.
	 * @param groupManager Il manager dei gruppi.
	 */
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

	public ISharedocsManager getSharedocsManager() {
		return _sharedocsManager;
	}

	public void setSharedocsManager(ISharedocsManager sharedocsManager) {
		this._sharedocsManager = sharedocsManager;
	}
	
	private ICategoryManager _categoryManager;
	private IGroupManager _groupManager;
	private IResourceManager _resourceManager;
	private IAuthorizationManager _authorizationManager;
	
	private ISharedocsManager _sharedocsManager;
}