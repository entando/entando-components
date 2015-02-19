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
package org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs.common.IEditDocumentAction;
import org.entando.entando.plugins.jpsharedocs.aps.internalservlet.sharedocs.helper.IDocumentActionHelper;
import org.entando.entando.plugins.jpsharedocs.aps.system.JpSharedocsSystemConstants;
import org.entando.entando.plugins.jpsharedocs.aps.system.services.sharedocs.ISharedocsManager;
import org.entando.entando.plugins.jpsharedocs.aps.system.services.sharedocs.SharedocsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AttachAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

public class EditDocumentAction extends BaseAction implements IEditDocumentAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(EditDocumentAction.class);
	
	@Override
	public void validate() {
		int step = this.getStep();
		String inputActionName = this.validateFields();// STEP_FIELDS
		if (step!=STEP_FIELDS && inputActionName==null) {
			inputActionName = this.validateCategories();// STEP_CATEGORIES
			if (step!=STEP_CATEGORIES && inputActionName==null) {
				inputActionName = this.validateGroups();// STEP_GROUPS
			}
		}
		this.setInputActionName(inputActionName);
	}
	
	private String validateFields() {
		boolean isEdit = this.isEdit();
		if (isEdit) {
			Content content = this.getContent();
			if (content==null || !this.getDocHelper().checkAllowedOnContent(content, this.getCurrentUser(), this.getRequest(), this)) {
				throw new RuntimeException("Error! Access to content " + this.getContentId() + " not allowed!");
			}
		} else {
			String title = this.getTitle();
			if (title == null || title.length()==0) {
				this.addFieldError("title", this.getText("Errors.title.required"));
			}
			String author = this.getAuthor();
			if (author == null || author.length()==0) {
				this.addFieldError("author", this.getText("Errors.author.required"));
			}
		}
		String descr = this.getDescription();
		if (descr == null || descr.length()==0) {
			this.addFieldError("descr", this.getText("Errors.description.required"));
		}
		File document = this.getDocument();
		String docDescr = this.getDocDescription();
		boolean hasDocDescr = docDescr != null && docDescr.length()>0;
		if (!isEdit || !(document == null && (docDescr==null || !hasDocDescr))) {
			if (!hasDocDescr) {
				this.addFieldError("docDescription", this.getText("Errors.docDescription.required"));
			}
			if (document == null) {
				this.addFieldError("document", this.getText("Errors.document.required"));
			} else {
				boolean checkResult = this.getDocHelper().checkUploadedDocument(document, this.getDocumentFileName(), this);
				if (checkResult) {
					this.storeFile();
				}
			}
		}
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			return "compileFields";
		}
		return null;
	}
	
	private String validateCategories() {
		String inputActionName = null;
		IDocumentActionHelper helper = this.getDocHelper();
		helper.checkCategories(this.getCategories(), this);
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			inputActionName = "compileCategories";
		}
		return inputActionName;
	}
	
	private String validateGroups() {
		String inputActionName = null;
		IDocumentActionHelper helper = this.getDocHelper();
		helper.checkGroups(this.getExtraGroups(), this.getCurrentUser(), this);
		
		if (!this.isEdit()) {
			String mainGroup = this.getMainGroup();
			Group group = this.getGroupManager().getGroup(mainGroup);
			if (group == null || (!group.getName().equals(Group.FREE_GROUP_NAME) && !this.getAuthorizationManager().isAuth(this.getCurrentUser(), group))) {
				String[] args = new String[] { (group!=null ? group.getDescr() : mainGroup) };
				this.addFieldError("mainGroup", this.getText("Errors.mainGroup.wrong", args));
			}
		}
		
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			inputActionName = "compileGroups";
		}
		return inputActionName;
	}
	
	/**
	 * Metodo di validazione standard. Restituisce il nome della action di input definita per gli errori verificatisi.
	 * Può essere usato per consentire il ritorno alla compilazione di dati precedenti non corretti.
	 * @return Il nome della action di input definita per gli errori verificatisi.
	 */
	protected String validateDocument() {
		return this.validateFields();
	}
	
	@Override
	public String entry() {
		try {
			if (this.isEdit()) {
				Content content = this.getContent();
				if (!this.getDocHelper().checkAllowedOnContent(content, this.getCurrentUser(), this.getRequest(), this)) {
					return USER_NOT_ALLOWED;
				}
				String langCode = this.getLangManager().getDefaultLang().getCode();

				// jpcheckin plugin integration // start
				if (null != this.getCheckin() 
						&& this.getCheckin().equalsIgnoreCase("true")) {
					_logger.debug("locking the content");
					AttributeInterface checkinAttribute = content.getAttributeByRole(JpSharedocsSystemConstants.ATTRIBUTE_ROLE_CHECK_IN);
					if (null != checkinAttribute) {
						boolean isBoolAttr = checkinAttribute instanceof BooleanAttribute;
						_logger.debug("Modifiying attribute '{}'", checkinAttribute.getName());
						if (isBoolAttr) {
							content.setLastEditor(this.getCurrentUser().getUsername());
							((BooleanAttribute)checkinAttribute).setBooleanValue(new Boolean(true));
							this.getContentManager().insertOnLineContent(content);
						} else {
							_logger.error("The checkin attribute is not a boolean");
						}
					} else {
						_logger.error("The content type is missing the checkin attribute");

					}
				}
				ITextAttribute titleAttr = (ITextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_TITLE);
				this.setTitle(titleAttr.getTextForLang(langCode));

				ITextAttribute authorAttr = (ITextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_AUTHOR);
				this.setAuthor(authorAttr.getTextForLang(langCode));

				ITextAttribute descriptionAttr = (ITextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_DESCRIPTION);
				this.setDescription(descriptionAttr.getTextForLang(langCode));
				
				Collection<Category> categories = content.getCategories();
				Collection<String> categoryCodes = this.getCategories();
				for (Category category : categories) {
					categoryCodes.add(category.getCode());
				}
				this.getExtraGroups().addAll(content.getGroups());

				this.setMainGroup(content.getMainGroup());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "entry");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String releaseDocument() {
		try {
			if (this.isEdit()) {
				Content content = this.getContent();
				if (content==null || !this.getDocHelper().checkAllowedOnContent(content, this.getCurrentUser(), this.getRequest(), this)) {
					return USER_NOT_ALLOWED;
				}
			}
			File document = this.getDocument();
			if (this.getTmpFileName() != null && document != null) {
				document.delete();
				this.setTmpFileName(null);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "releaseDocument");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String saveFields() {
		try {
			if (this.isEdit()) {
				Content content = this.getContent();
				if (content==null || !this.getDocHelper().checkAllowedOnContent(content, this.getCurrentUser(), this.getRequest(), this)) {
					return USER_NOT_ALLOWED;
				}
			}
			this.storeFile();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveFields");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void storeFile() {
		if (this.getTmpFileName() == null && this.getDocument() != null) {
			try {
				String tmpFileName = this.getDocHelper().saveTemporaryFile(this.getDocument(), this.getDocumentFileName());
				this.setTmpFileName(tmpFileName);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "storeFile");
				throw new RuntimeException(t);
			}
		}
	}
	
	@Override
	public String saveContent() {
		try {
			boolean isEdit = this.isEdit();
			File document = this.getDocument();
			ResourceInterface resource = null;
			
			if (document != null) {
				String mainGroup = isEdit ? this.getContent().getMainGroup() : this.getMainGroup();
				ResourceDataBean resourceBean = this.getDocHelper().createResource(document, this.getDocumentFileName(), 
						this.getDocDescription(), mainGroup, this.getDocumentContentType());
				resource = this.getResourceManager().addResource(resourceBean);
				document.delete();
			}
			Content content = this.createContent(resource);
			/*
			// keep track of the author
			Object author = (MonoTextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_USER);
			
			if (null != author
					&& author instanceof MonoTextAttribute) {
				String username = getCurrentUser().getUsername();
				String existing = ((MonoTextAttribute)author).getText();

				if (null == existing
						|| "".equals(existing)) {
					((MonoTextAttribute)author).setText(username);
					_logger.debug("Setting author '{}'", username);
//					System.out.println("Setting author " + username);
				} else {
					content.setLastEditor(username);
					_logger.debug("Setting last editor '{}'", username);
//					System.out.println("Setting last editor " + username);
				}
			} else {
				_logger.error("The attribute '{}' is not a Monotext as it ought to be", JpSharedocsSystemConstants.ATTRIBUTE_USER);
			}
			*/
			if (null != this.getCheckin() 
					&& this.getCheckin().equalsIgnoreCase("false")) {
				
				_logger.debug("Checking the content '{}' out", content.getId());
				
				AttributeInterface checkinAttribute = content.getAttributeByRole(JpSharedocsSystemConstants.ATTRIBUTE_ROLE_CHECK_IN);
				if (null != checkinAttribute) {
					boolean isBoolAttr = checkinAttribute instanceof BooleanAttribute;
					if (isBoolAttr) {
						((BooleanAttribute)checkinAttribute).setBooleanValue(new Boolean(false));
					}
				}
			}
			// jpcheckin plugin integration // end
			
			if (!isEdit) {
				this.getContentManager().saveContent(content);
			}
			this.getContentManager().insertOnLineContent(content);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveContent");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trashContent() {
		return SUCCESS;
	}
	
	/**
	 * This deletes also the documents!
	 * @return
	 */
	public String removeContent() {
		Content content = null;
		SharedocsConfig config = this.getSharedocsManager().getConfiguration();
		boolean deleteOnRemove = config.isDeleteOnRemove();
		boolean deleteResources = config.isDeleteResources() && deleteOnRemove;
		
		try {
			content = this.getContent();
			Object versions = content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_VERSION);
			if (deleteResources
					&& null != versions && versions instanceof MonoListAttribute) {
				List<AttributeInterface> attributes = ((MonoListAttribute)versions).getAttributes();
				
				
				if (null != attributes
						&& attributes.size() > 0) {
					for (AttributeInterface listElement: attributes) {
						if (listElement instanceof CompositeAttribute) {
							AttributeInterface attachAttribute = ((CompositeAttribute)listElement).getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_COMPOSITE_DOCUMENT);
							
							if (null != attachAttribute
									&& attachAttribute instanceof AttachAttribute) {
								ResourceInterface resource = ((AttachAttribute)attachAttribute).getResource();
								
								if (null != resource) {
									_logger.info("Deleting " + resource.getMasterFileName());
									getResourceManager().deleteResource(resource);
								} else {
									_logger.error("Expected resource (interface) not found");
								}
							} else {
								_logger.error("Expected attachment attribute not found");
							}
						} else {
							_logger.error("Expected composite attribute not found");
						}
					}
				}
			} else if (deleteResources) {
				_logger.error("The version attribute is not a monolist or it does not exist");
				throw new RuntimeException("The version attribute is not a monolist or it does not exist");
			} else {
				_logger.debug("Skipping attachmente deletion for content '{}'", content.getId());
			}
			// finally
			getContentManager().removeOnLineContent(content);
			if (deleteOnRemove) {
				_logger.debug("Deleting content '{}' as requested", content.getId());
				getContentManager().deleteContent(content);
			} else {
				_logger.info("Unpublishing content '{}'", content.getId());
			}
			//
			String retUrl = getListPage();
			setRetUrl(retUrl);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeContent");
			return FAILURE;
		}
		return SUCCESS;
	}
	/**
	 * Return the value of the checkin attribute of the current Shared content
	 * @return
	 */
	public boolean isCheckedIn() {
		boolean checkInValue = false;
		
		try {
			Content content = this.getContent();
			if (null != content) {
				AttributeInterface checkinAttribute = content.getAttributeByRole(JpSharedocsSystemConstants.ATTRIBUTE_ROLE_CHECK_IN);
				if (null != checkinAttribute) {
					boolean isBoolAttr = checkinAttribute instanceof BooleanAttribute;
					if (isBoolAttr) {
						checkInValue= ((BooleanAttribute)checkinAttribute).getValue();
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking the checkin attribute", t);
		}
		return checkInValue;
	}
	
	private Content createContent(ResourceInterface resource) {
		IDocumentActionHelper docHelper = this.getDocHelper();
		boolean isEdit = this.isEdit();
		String langCode = this.getLangManager().getDefaultLang().getCode();
		Date now = new Date();
		String username = getCurrentUser().getUsername();
		
		Content content = null;
		if (isEdit) {
			content = this.getContent();
		} else {
			String typeCode = docHelper.getContentType(this.getRequest());
			content = this.getContentManager().createContentType(typeCode);
			String title = this.getTitle();
			String descr = title.length()>100 ? title.substring(0, 100) : title;
			content.setDescr(descr.trim());
			content.setMainGroup(this.getMainGroup());
			content.setStatus(Content.STATUS_READY);

			ITextAttribute titleAttr = (ITextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_TITLE);
			titleAttr.setText(title, langCode);

			ITextAttribute authorAttr = (ITextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_AUTHOR);
			authorAttr.setText(this.getAuthor(), langCode);

			DateAttribute creationDateAttr = (DateAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_CREATION_DATE);
			creationDateAttr.setDate(now);
			
			MonoTextAttribute author = (MonoTextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_USER);
			author.setText(username);
			
		}
		ITextAttribute descriptionAttr = (ITextAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_DESCRIPTION);
		descriptionAttr.setText(this.getDescription(), langCode);
		
		if (resource!=null) {
			MonoListAttribute versioniAttr = (MonoListAttribute) content.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_VERSION);
			CompositeAttribute versioneAttr = (CompositeAttribute) versioniAttr.addAttribute();
			
			AttachAttribute allegatoAttr = (AttachAttribute) versioneAttr.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_COMPOSITE_DOCUMENT);
			allegatoAttr.setResource(resource, langCode);
			allegatoAttr.setText(this.getDocDescription(), langCode);
			
			DateAttribute dataVersioneAttr = (DateAttribute) versioneAttr.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_VERSION_DATE);
			dataVersioneAttr.setDate(now);
			
			ITextAttribute addedFromAttr = (ITextAttribute) versioneAttr.getAttribute(JpSharedocsSystemConstants.ATTRIBUTE_INSERTED_BY);
			String addedFrom = docHelper.getUserCompleteName(this.getCurrentUser());
			addedFromAttr.setText(addedFrom, langCode);
		}
		
		Set<String> groups = content.getGroups();
		groups.clear();
		groups.addAll(this.getExtraGroups());
		
		ICategoryManager categoryManager = this.getCategoryManager();
		List<Category> categories = content.getCategories();
		categories.clear();
		for (String categoryCode : this.getCategories()) {
			categories.add(categoryManager.getCategory(categoryCode));
		}
		
		return content;
	}
	
	@Override
	public String joinCategory() {
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.getCode().equals(category.getParentCode()) 
					&& !this.getCategories().contains(categoryCode)) { 
				this.getCategories().add(categoryCode);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinCategory");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeCategory() {
		try {
			String categoryCode = this.getCategoryCode();
			if (null != categoryCode) {
				this.getCategories().remove(categoryCode);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeCategory");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String joinGroup() {
		try {
			String extraGroupName = this.getExtraGroupName();
			Group group = this.getGroupManager().getGroup(extraGroupName);
			if (null != group) { 
				this.getExtraGroups().add(extraGroupName);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinGroup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeGroup() {
		try {
			String extraGroupName = this.getExtraGroupName();
			if (null != extraGroupName) {
				this.getExtraGroups().remove(extraGroupName);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeGroup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Content getContent() {
		if (this._content == null && this.isEdit()) {
			try {
				this._content = this.getContentManager().loadContent(this.getContentId(), false);
			} catch (ApsSystemException e) {
				throw new RuntimeException("Error! Access to content " + this.getContentId() + " not allowed!");
			}
		}
		return this._content;
	}
	
	
	/**
	 * Return the portal page where the widget list is deployed
	 * @return
	 */
	private String getListPage() {
		String url = null;
		List<IPage> pageCodeList;
		
		try {
			pageCodeList = getPageManager().getWidgetUtilizers(JpSharedocsSystemConstants.WIDGET_LIST);

			if (null != pageCodeList 
					&& !pageCodeList.isEmpty()) {
				IPage page = pageCodeList.get(0);
				url = getUrlManager().createUrl(page, getCurrentLang(), null);
			}
		} catch (ApsSystemException t) {
			_logger.error("Error getting the page with the list widget", t);
		}
		return url;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public boolean isEdit() {
		String contentId = this.getContentId();
		return contentId != null && contentId.length() > 0;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	public String getAuthor() {
		return _author;
	}
	public void setAuthor(String author) {
		this._author = author;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public File getDocument() {
		String tmpFileName = this.getTmpFileName();
		if (this._document == null && tmpFileName != null && tmpFileName.length()>0) {
			this._document = this.getDocHelper().getTemporaryFile(tmpFileName);
		}
		return this._document;
	}
	public void setDocument(File document) {
		this._document = document;
	}
	
	public String getDocDescription() {
		return _docDescription;
	}
	public void setDocDescription(String docDescription) {
		this._docDescription = docDescription;
	}
	
	public String getDocumentContentType() {
		return this._documentContentType;
	}
	public void setDocumentContentType(String documentContentType) {
		this._documentContentType = documentContentType;
	}
	
	public String getDocumentFileName() {
		return this._documentFileName;
	}
	public void setDocumentFileName(String documentFileName) {
		this._documentFileName = documentFileName;
	}
	
	public String getTmpFileName() {
		return this._tmpFileName;
	}
	public void setTmpFileName(String tmpFileName) {
		this._tmpFileName = tmpFileName;
	}
	
	public Category getCategoryRoot() {
		return this.getCategoryManager().getRoot();
//		return this.getCategoryManager().getCategory("documenti");
	}
	
	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}
	
	public Set<String> getCategories() {
		return _categories;
	}
	public void setCategories(Set<String> categories) {
		this._categories = categories;
	}
	
	/**
	 * Restituisce un gruppo in base al nome.
	 * @param groupName Il nome del gruppo da restituire.
	 * @return Il gruppo cercato.
	 */
	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
	}
	
	public List<Group> getAllowedGroups() {
		return this.getDocHelper().getAllowedGroups(this.getCurrentUser());
	}
	
	public List<Group> getGroups() {
    	return this.getGroupManager().getGroups();
	}
	
	public Set<String> getExtraGroups() {
		return _extraGroups;
	}
	public void setExtraGroups(Set<String> extraGroups) {
		this._extraGroups = extraGroups;
	}
	
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public String getInputActionName() {
		return _inputActionName;
	}
	protected void setInputActionName(String inputActionName) {
		this._inputActionName = inputActionName;
	}
	
	public String getCategoryCode() {
		return _categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}
	
	public String getExtraGroupName() {
		return _extraGroupName;
	}
	public void setExtraGroupName(String extraGroupName) {
		this._extraGroupName = extraGroupName;
	}
	
	public int getStep() {
		return _step;
	}
	public void setStep(int step) {
		this._step = step;
	}
	
	/**
	 * Restituisce il manager gestore delle operazioni sui contenuti.
	 * @return Il manager gestore delle operazioni sui contenuti.
	 */
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	/**
	 * Setta il manager gestore delle operazioni sui contenuti.
	 * @param contentManager Il manager gestore delle operazioni sui contenuti.
	 */
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
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
	
	protected IDocumentActionHelper getDocHelper() {
		return _docHelper;
	}
	public void setDocHelper(IDocumentActionHelper docHelper) {
		this._docHelper = docHelper;
	}

	public String getCheckin() {
		return _checkin;
	}

	public void setCheckin(String checkin) {
		this._checkin = checkin;
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public String getRetUrl() {
		return _retUrl;
	}

	public void setRetUrl(String retUrl) {
		this._retUrl = retUrl;
	}

	public IURLManager getUrlManager() {
		return _urlManager;
	}

	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	public ISharedocsManager getSharedocsManager() {
		return _sharedocsManager;
	}

	public void setSharedocsManager(ISharedocsManager sharedocsManager) {
		this._sharedocsManager = sharedocsManager;
	}

	private Content _content;
	private String _contentId;
	
	private String _title;
	private String _author;
	private String _description;
	private String _docDescription;
	
	private String _documentContentType;
	private String _documentFileName;
	private String _tmpFileName;
	private File _document;
	
	private Set<String> _categories = new TreeSet<String>();
	private Set<String> _extraGroups = new TreeSet<String>();
	private String _mainGroup;
	
	private String _inputActionName;
	private String _categoryCode;
	private String _extraGroupName;
	
	private int _step;
	
	private IContentManager _contentManager;
	private ICategoryManager _categoryManager;
	private IGroupManager _groupManager;
	private IResourceManager _resourceManager;
	private IPageManager _pageManager;
	private IURLManager _urlManager;
	private ISharedocsManager _sharedocsManager;
	
	private IDocumentActionHelper _docHelper;
	
	private static final int STEP_FIELDS = 1;
	private static final int STEP_CATEGORIES = 2;
	private static final int STEP_GROUPS = 3;
	
	// jpcontentcheckin integration
	private String _checkin;
	// return to the list page
	private String _retUrl; 
}