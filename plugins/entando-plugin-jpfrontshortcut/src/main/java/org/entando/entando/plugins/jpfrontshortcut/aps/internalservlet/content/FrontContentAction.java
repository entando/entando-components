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
package org.entando.entando.plugins.jpfrontshortcut.aps.internalservlet.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.entando.entando.plugins.jpfrontshortcut.aps.system.JpFrontShortcutSystemConstants;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.apsadmin.content.ContentAction;

/**
 * @author E.Santoboni
 */
public class FrontContentAction extends ContentAction {
	
	public String editView() {
		try {
			String modelIdString = this.getRequest().getParameter("modelId");
			ContentModel model = this.getContentModel(modelIdString);
			if (null == model) {
				modelIdString = this.getContentManager().getDefaultModel(this.getContentId());
				model = this.getContentModel(modelIdString);
			}
			this.setContentModel(model);
			if (null == model) {
				return this.edit();
			}
			if (!this.getContentId().startsWith(model.getContentType())) {
				throw new ApsSystemException("Invalid model id " + model.getId() + 
						" of type " + model.getContentType() + " for content " + this.getContentId());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "editView");
			return FAILURE;
		}
		return this.edit();
	}
    
	public ContentModel getContentModel(String modelIdString) {
		if (null == modelIdString) {
			return null;
		}
		ContentModel model = null;
		try {
			long modelId = Long.parseLong(modelIdString);
			model = this.getContentModelManager().getContentModel(modelId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentModel", "Error extracting modelId");
		}
		return model;
	}
	
    @Override
    public String edit() {
        String result = super.edit();
		if (!result.equals(SUCCESS)) {
			return result;
		}
		try {
			Content content = this.getContent();
			ContentModel model = this.getContentModel();
			if (null != this.getAttributeName() && null != model) {
				if (!content.getTypeCode().equals(model.getContentType())) {
					ApsSystemUtils.getLogger().error("Invalid model id " + model.getId() + 
						" of type " + model.getContentType() + " for content " + this.getContentId());
					//return SUCCESS;
				}
			}
			if (model != null) {
				this.extractAttributesToEdit(model.getContentShape(), content);
			}
			List<AttributeInterface> attributes = content.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				//jpfrontshortcut_${typeCodeKey}_${attributeNameI18nKey}
				String attributeLabelKey = "jpfrontshortcut_" + content.getTypeCode() + "_" + attribute.getName();
				if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
					this.addLabelGroups(attributeLabelKey, attribute.getName());
				}
				attribute.setDisablingCodes(this.createNewCodes(attribute.getDisablingCodes()));
				if (null != this.getAttributeName() && !this.getAttributeName().contains(attribute.getName())) {
					attribute.disable(JpFrontShortcutSystemConstants.WIDGET_DISABLING_CODE);
				}
			}
			Lang currentLang = super.getCurrentLang();
			this.getRequest().getSession()
					.setAttribute(JpFrontShortcutSystemConstants.CONTENT_LANG_SESSION_PARAM, currentLang);
		} catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return result;
    }
    
	private void extractAttributesToEdit(String text, Content content) {
		if (null == content) {
			ApsSystemUtils.getLogger().error("null content in extractAttributesToEdit");
			return;
		}
		if (null == content.getAttributeMap() || content.getAttributeMap().isEmpty()) {
			ApsSystemUtils.getLogger().error("no attributes for content " + content.getId() + " in extractAttributesToEdit");
			return;
		}
		Set<String> attributes = content.getAttributeMap().keySet();
		Iterator<String> it = attributes.iterator();
		while (it.hasNext()) {
			String currentAttrName = it.next();
			if (text.contains("$content." + currentAttrName)) {
				if (null == this.getAttributeName()) {
					this.setAttributeName(new ArrayList<String>());
				}
				if (!this.getAttributeName().contains(currentAttrName)) {
					this.getAttributeName().add(currentAttrName);
				}
			}
		}
	}
    
	protected void addLabelGroups(String key, String defaultValue) throws ApsSystemException {
		try {
			ApsProperties properties = new ApsProperties();
			Lang defaultLang = super.getLangManager().getDefaultLang();
			properties.put(defaultLang.getCode(), defaultValue);
			this.getI18nManager().addLabelGroup(key, properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addLabelGroups");
			throw new RuntimeException("Error adding label groups - key '" + key + "'", t);
		}
	}
	
	private String[] createNewCodes(String[] oldDisablingCodes) {
		if (null == oldDisablingCodes) {
			String[] newCodes = new String[1];
			newCodes[0] = JpFrontShortcutSystemConstants.WIDGET_DISABLING_CODE;
			return newCodes;
		}
		int len = oldDisablingCodes.length;
		String[] newCodes = new String[len + 1];
		for (int i=0; i < len; i++){
			newCodes[i] = oldDisablingCodes[i];
		}
		newCodes[len] = JpFrontShortcutSystemConstants.WIDGET_DISABLING_CODE;
		return newCodes;
	}
	
    @Override
	public String saveAndApprove() {
		if (null != super.getContent()) {
			this.setContentId(super.getContent().getId());
		}
		String result = super.saveAndApprove();
		try {
			synchronized (this) {
				this.wait(1000);
			}
			this.waitNotifyingThread();
			this.getRequest().getSession()
					.removeAttribute(JpFrontShortcutSystemConstants.CONTENT_LANG_SESSION_PARAM);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveAndApprove");
			return FAILURE;
		}
		return result;
	}
    
    protected void waitNotifyingThread() throws InterruptedException {
		Thread[] threads = new Thread[20];
        Thread.enumerate(threads);
        for (int i=0; i<threads.length; i++) {
            Thread currentThread = threads[i];
            if (currentThread != null && 
                            currentThread.getName().startsWith(NotifyManager.NOTIFYING_THREAD_NAME)) {
                    currentThread.join();
            }
        }
    }
	
	public String getRedirectUrl() {
		return "";
	}
    
    public String executeSubmit() {
        return SUCCESS;
    }

    public List<String> getAttributeName() {
        return _attributeName;
    }
    public void setAttributeName(List<String> attributeName) {
        this._attributeName = attributeName;
    }
    
    public String getLangCode() {
        return _langCode;
    }
    public void setLangCode(String langCode) {
        this._langCode = langCode;
    }
	
	protected ContentModel getContentModel() {
		return _contentModel;
	}
	protected void setContentModel(ContentModel contentModel) {
		this._contentModel = contentModel;
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
    
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	
    private List<String> _attributeName;
    private String _langCode;
	
	private ContentModel _contentModel;
	private IContentModelManager _contentModelManager;
	private II18nManager _i18nManager;
	
}