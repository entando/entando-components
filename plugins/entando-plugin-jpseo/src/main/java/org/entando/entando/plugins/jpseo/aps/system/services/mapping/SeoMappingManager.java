/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

import java.util.Iterator;
import java.util.List;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.cache.ISeoMappingCacheWrapper;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.event.SeoChangedEvent;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;
import org.entando.entando.plugins.jpseo.aps.util.FriendlyCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni, E.Mezzano
 */
public class SeoMappingManager extends AbstractService implements ISeoMappingManager, PageChangedObserver, PublicContentChangedObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoMappingManager.class);
	
	private ISeoMappingDAO seoMappingDAO;
	private ILangManager langManager;
	private IPageManager pageManager;
    private ISeoMappingCacheWrapper cacheWrapper;

	@Override
	public void init() throws Exception {
		this.getCacheWrapper().initCache(this.getSeoMappingDAO());
		_logger.debug("{} ready. initialized",this.getClass().getName());
	}
	
	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		IPage page = event.getPage();
		try {
			if (event.getSource().equals(ApsEvent.LOCAL_EVENT)) {
				if (null == this.getPageManager().getOnlinePage(page.getCode())) {
					this.deleteMapping(page.getCode(), null);
				} else {
					this.updatePageMapping(page);
				}
			} else {
				System.out.println("jpseo: ignoring EXTERNAL event");
			}
			SeoChangedEvent sevent = new SeoChangedEvent();
			event.setOperationCode(SeoChangedEvent.PAGE_CHANGED_EVENT);
			this.notifyEvent(sevent);
		} catch (Throwable t) {
			_logger.error("Error updating mapping from page changed", t);
		}
	}
	
	private void updatePageMapping(IPage page) {
		if (!(page.getMetadata() instanceof SeoPageMetadata)) {
            return;
        }
		SeoPageMetadata seoMetadata = (SeoPageMetadata) page.getMetadata();
		String friendlyCode = seoMetadata.getFriendlyCode();
		if (null == friendlyCode || friendlyCode.trim().length() == 0) return;
		FriendlyCodeVO vo = new FriendlyCodeVO(seoMetadata.getFriendlyCode(), page.getCode());
		this.updateMapping(vo);
	}
	
	@Override
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		if (null == event.getContent()) return;
		Content content = event.getContent();
		try {
			if (event.getSource().equals(ApsEvent.LOCAL_EVENT)) {
				if (event.getOperationCode() != PublicContentChangedEvent.REMOVE_OPERATION_CODE) {
					this.updateContentMapping(content);
				} else {
					this.deleteMapping(null, content.getId());
				}
			} else {
				System.out.println("jpseo: ignoring EXTERNAL event");
			}
			SeoChangedEvent sevent = new SeoChangedEvent();
			event.setOperationCode(SeoChangedEvent.CONTENT_CHANGED_EVENT);
			this.notifyEvent(sevent);
		} catch (Throwable t) {
			_logger.error("Error updating mapping from public content changed", t);
		}
	}
	
	private void updateContentMapping(Content content) throws ApsSystemException {
		try {
			AttributeInterface attribute = content.getAttributeByRole(JpseoSystemConstants.ATTRIBUTE_ROLE_FRIENDLY_CODE);
			if (null == attribute || !(attribute instanceof ITextAttribute)) {
				attribute = content.getAttributeByRole(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
			}
			if (null != attribute && attribute instanceof ITextAttribute) {
				ContentFriendlyCode contentFriendlyCode = this.prepareContentFriendlyCode(content.getId(), (ITextAttribute) attribute);
				this.getSeoMappingDAO().updateMapping(contentFriendlyCode);
				this.getCacheWrapper().initCache(this.getSeoMappingDAO());
			}
		} catch (Throwable t) {
			_logger.error("Error updating content mapping", t);
			throw new ApsSystemException("Error updating content mapping", t);
		}
	}
	
	private ContentFriendlyCode prepareContentFriendlyCode(String contentId, ITextAttribute attribute) {
		ContentFriendlyCode contentFriendlyCode = new ContentFriendlyCode();
		contentFriendlyCode.setContentId(contentId);
		String defaultLang = this.getLangManager().getDefaultLang().getCode();
		if (((AttributeInterface) attribute).isMultilingual()) {
			String defaultFriendlyCode = FriendlyCodeGenerator.generateFriendlyCode(attribute.getTextForLang(defaultLang));
			contentFriendlyCode.addFriendlyCode(defaultLang, defaultFriendlyCode);
			Iterator<Lang> langs = this.getLangManager().getLangs().iterator();
			while (langs.hasNext()) {
				Lang currentLang = langs.next();
				if (!currentLang.isDefault()) {
					String langCode = currentLang.getCode();
					String friendlyCode = FriendlyCodeGenerator.generateFriendlyCode(attribute.getTextForLang(langCode));
					if (friendlyCode!=null && !friendlyCode.equals(defaultFriendlyCode)) {
						contentFriendlyCode.addFriendlyCode(langCode, friendlyCode);
					}
				}
			}
		} else {
			String friendlyCode = FriendlyCodeGenerator.generateFriendlyCode(attribute.getText());
			contentFriendlyCode.addFriendlyCode(defaultLang, friendlyCode);
		}
		return contentFriendlyCode;
	}
	
	private void updateMapping(FriendlyCodeVO vo) {
		try {
			this.getSeoMappingDAO().updateMapping(vo);
			this.getCacheWrapper().initCache(this.getSeoMappingDAO());
		} catch (Throwable t) {
			_logger.error("Error updating mapping", t);
		}
	}
	
	private void deleteMapping(String pageCode, String contentId) {
		try {
			this.getSeoMappingDAO().deleteMapping(pageCode, contentId);
		} catch (Throwable t) {
			_logger.error("Error deleting mapping", t);
		}
	}
	
	@Override
	public List<String> searchFriendlyCode(FieldSearchFilter[] filters) throws ApsSystemException {
		List<String> codes = null;
		try {
			codes = this.getSeoMappingDAO().searchFriendlyCode(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Friendly Codes", t);
			throw new ApsSystemException("Error searching Friendly Codes", t);
		}
		return codes;
	}
	
	@Override
	public FriendlyCodeVO getReference(String friendlyCode) {
		return this.getCacheWrapper().getMappingByFriendlyCode(friendlyCode);//this.getMapping().get(friendlyCode);
	}
	
	@Override
	public String getPageReference(String pageCode) {
		FriendlyCodeVO friendlyCode = this.getCacheWrapper().getMappingByPageCode(pageCode);//this.getPageFriendlyCodes().get(pageCode);
		if (friendlyCode!=null) {
			return friendlyCode.getPageCode();
		}
		return null;
	}
	
	@Override
	public String getContentReference(String contentId, String langCode) {
		String friendlyCode = null;
		ContentFriendlyCode content = this.getCacheWrapper().getMappingByContentId(contentId);//this.getContentFriendlyCodes().get(contentId);
		if (content != null) {
			friendlyCode = content.getFriendlyCode(langCode);
			if (friendlyCode == null) {
				friendlyCode = content.getFriendlyCode(this.getLangManager().getDefaultLang().getCode());
			}
		}
		return friendlyCode;
	}
	
	protected ISeoMappingDAO getSeoMappingDAO() {
		return seoMappingDAO;
	}
	public void setSeoMappingDAO(ISeoMappingDAO seoMappingDAO) {
		this.seoMappingDAO = seoMappingDAO;
	}
	
	protected ILangManager getLangManager() {
		return langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this.langManager = langManager;
	}
	
	protected IPageManager getPageManager() {
		return pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this.pageManager = pageManager;
	}

    protected ISeoMappingCacheWrapper getCacheWrapper() {
        return cacheWrapper;
    }
    public void setCacheWrapper(ISeoMappingCacheWrapper cacheWrapper) {
        this.cacheWrapper = cacheWrapper;
    }
	
}
