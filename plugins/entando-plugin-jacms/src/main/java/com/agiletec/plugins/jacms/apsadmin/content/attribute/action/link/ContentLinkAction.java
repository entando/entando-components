/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Classe action delegata alla gestione dei link a contenuto nelle 
 * operazioni sugli attributi tipo Link.
 * @author E.Santoboni
 */
public class ContentLinkAction extends ContentFinderAction {

	private static final Logger logger = LoggerFactory.getLogger(ContentLinkAction.class);
	
	private String contentOnSessionMarker;
	
	private String _contentId;
	private boolean contentOnPageType;
	
	private String entryContentAnchorDest;
	
	private ILinkAttributeActionHelper linkAttributeHelper;

	private String linkAttributeRel;

	private String linkAttributeTarget;

	private String linkAttributeHRefLang;

	@Override
	public String execute()  {
		String result= super.execute();
		if (result.equals(SUCCESS)) {
			Map<String, String> properties = (Map<String, String>) this.getRequest().getSession().getAttribute(ILinkAttributeActionHelper.LINK_PROPERTIES_MAP_SESSION_PARAM);
			if (null != properties) {
				this.linkAttributeRel = properties.get("rel");
				this.linkAttributeTarget = properties.get("target");
				this.linkAttributeHRefLang = properties.get("hreflang");
			}
		}
		return result;
	}
    
    @Override
    public SearcherDaoPaginatedResult<String> getPaginatedContentsId(Integer limit) {
        SearcherDaoPaginatedResult<String> result = null;
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
            EntitySearchFilter[] filters = this.createFilters();
            if (null != limit) {
                filters = ArrayUtils.add(filters, this.getPagerFilter(limit));
            }
			result = this.getContentManager().getPaginatedPublicContentsId(null, false, filters, allowedGroups);
		} catch (Exception e) {
			logger.error("error loading paginated contents", e);
			throw new RuntimeException("error loading paginated contents", e);
		}
		return result;
    }

    @Deprecated
	@Override
	public List<String> getContents() {
		List<String> result = null;
		try {
			List<String> allowedGroups = this.getContentGroupCodes();
			result = this.getContentManager().loadPublicContentsId(null, this.createFilters(), allowedGroups);
		} catch (Throwable t) {
			logger.error("error loading contents", t);
			throw new RuntimeException("error loading contents", t);
		}
		return result;
	}
	
	public String joinContentLink() {
		ContentRecordVO contentVo = this.getContentVo(this.getContentId());
		if (null == contentVo || !contentVo.isOnLine()) {
			logger.error("Content '{}' does not exists or is not public", this.getContentId());
			return FAILURE;
		}
		if (this.isContentOnPageType()) {
			//Fa il forward alla scelta pagina di destinazione
			return "configContentOnPageLink";
		} else {
			String[] destinations = {null, this.getContentId(), null};
			this.buildEntryContentAnchorDest();
			this.getLinkAttributeHelper().joinLink(destinations, SymbolicLink.CONTENT_TYPE, createPropertiesMap() ,this.getRequest());
			return SUCCESS;
		}
	}
	
	private void buildEntryContentAnchorDest() {
		HttpSession session = this.getRequest().getSession();
		String anchorDest = this.getLinkAttributeHelper().buildEntryContentAnchorDest(session);
		this.setEntryContentAnchorDest(anchorDest);
	}
	
	/**
	 * Sovrascrittura del metodo della {@link ContentFinderAction}.
	 * Il metodo fà in modo di ricercare i contenuti che hanno, come gruppo proprietario o gruppo abilitato alla visualizzazione, 
	 * o il gruppo "free" o il gruppo proprietario del contenuto in redazione.
	 */
	@Override
	protected List<String> getContentGroupCodes() {
		List<String> allowedGroups = new ArrayList<>();
		allowedGroups.add(Group.FREE_GROUP_NAME);
		Content currentContent = this.getContent();
		allowedGroups.add(currentContent.getMainGroup());
		return allowedGroups;
	}
	
	public SymbolicLink getSymbolicLink() {
		return (SymbolicLink) this.getRequest().getSession().getAttribute(ILinkAttributeActionHelper.SYMBOLIC_LINK_SESSION_PARAM);
	}
	
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}

	private Map<String,String> createPropertiesMap(){
		Map<String,String> properties = new HashMap<>();
		if (StringUtils.isNotBlank(getLinkAttributeRel())) {
			properties.put("rel", getLinkAttributeRel());
		}
		if (StringUtils.isNotBlank(getLinkAttributeTarget())) {
			properties.put("target", getLinkAttributeTarget());
		}
		if (StringUtils.isNotBlank(getLinkAttributeHRefLang())) {
			properties.put("hrefLang",getLinkAttributeHRefLang());
		}
		return properties;
	}

	public String getLinkAttributeRel() {
		return linkAttributeRel;
	}
	public void setLinkAttributeRel(String linkAttributeRel) {
		this.linkAttributeRel = linkAttributeRel;
	}

	public String getLinkAttributeTarget() {
		return linkAttributeTarget;
	}
	public void setLinkAttributeTarget(String linkAttributeTarget) {
		this.linkAttributeTarget = linkAttributeTarget;
	}

	public String getLinkAttributeHRefLang() {
		return linkAttributeHRefLang;
	}
	public void setLinkAttributeHRefLang(String linkAttributeHRefLang) {
		this.linkAttributeHRefLang = linkAttributeHRefLang;
	}
    
	public String getContentOnSessionMarker() {
		return contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this.contentOnSessionMarker = contentOnSessionMarker;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public boolean isContentOnPageType() {
		return contentOnPageType;
	}
	public void setContentOnPageType(boolean contentOnPageType) {
		this.contentOnPageType = contentOnPageType;
	}
	
	public String getEntryContentAnchorDest() {
		if (null == this.entryContentAnchorDest) {
			this.buildEntryContentAnchorDest();
		}
		return entryContentAnchorDest;
	}
	protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
		this.entryContentAnchorDest = entryContentAnchorDest;
	}
	
	/**
	 * Restituisce la classe helper della gestione degli attributi di tipo Link.
	 * @return La classe helper degli attributi di tipo Link.
	 */
	protected ILinkAttributeActionHelper getLinkAttributeHelper() {
		return linkAttributeHelper;
	}
	/**
	 * Setta la classe helper della gestione degli attributi di tipo Link.
	 * @param linkAttributeHelper La classe helper degli attributi di tipo Link.
	 */
	public void setLinkAttributeHelper(ILinkAttributeActionHelper linkAttributeHelper) {
		this.linkAttributeHelper = linkAttributeHelper;
	}

}
