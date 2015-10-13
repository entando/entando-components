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
package org.entando.entando.plugins.jprss.apsadmin.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.util.FilterUtils;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.IContentListFilterAction;
import org.entando.entando.plugins.jprss.aps.system.services.rss.Channel;
import org.entando.entando.plugins.jprss.aps.system.services.rss.IRssManager;
import org.entando.entando.plugins.jprss.aps.system.services.rss.RssContentMapping;
import com.opensymphony.xwork2.Action;

/**
 * This action handles backend operations for the channel
 * @author S.Puddu
 */
public class RssAction extends BaseAction {
	
	@Override
	public void validate() {
		super.validate();
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			String filters = this.getFilters();
			List<Properties> properties = FilterUtils.getFiltersProperties(filters);
			this.setFiltersProperties(properties);
		}
	}

	public List<Channel> getChannels() {
		List<Channel> channels = new ArrayList<Channel>();
		try {
			channels = this.getRssManager().getChannels(Channel.STATUS_ALL);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getChannels");
			throw new RuntimeException("Errore recupero lista channels", t);
		}
		return channels;
	}

	public String newChannel() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "newChannel");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			Channel channel = this.buildChannel();
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getRssManager().addChannel(channel);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getRssManager().updateChannel(channel);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			if (0 == this.getId()) {
				this.addActionError(this.getText("jprss.message.rss.invalidId"));
				return INPUT;
			}
			this.getRssManager().deleteChannel(this.getId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			if (0 == this.getId()) {
				this.addActionError(this.getText("jprss.message.rss.invalidId"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
			Channel channel = this.getRssManager().getChannel(this.getId());
			this.setActive(channel.isActive());
			this.setCategory(channel.getCategory());
			this.setContentType(channel.getContentType());
			this.setDescription(channel.getDescription());
			this.setFeedType(channel.getFeedType());
			this.setFilters(channel.getFilters());
			this.setTitle(channel.getTitle());
			this.setMaxContentsSize(channel.getMaxContentsSize());
			this.setFiltersProperties(this.buildFilterProperties());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			if (0 == this.getId()) {
				this.addActionError(this.getText("jprss.message.rss.invalidId"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
			Channel channel = this.getRssManager().getChannel(this.getId());
			this.setActive(channel.isActive());
			this.setCategory(channel.getCategory());
			this.setContentType(channel.getContentType());
			this.setDescription(channel.getDescription());
			this.setFeedType(channel.getFeedType());
			this.setFilters(channel.getFilters());
			this.setTitle(channel.getTitle());
			this.setMaxContentsSize(channel.getMaxContentsSize());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "trash");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<Category> getAvailableCategories() {
		return this.getCategoryManager().getCategoriesList();
	}
	
	public SmallContentType getSmallContentType(String code) {
		Map<String, SmallContentType> smallContentTypes = this.getContentManager().getSmallContentTypesMap();
		return smallContentTypes.get(code);
	}
	
	public List<IApsEntity> getContentTypes() {
		List<IApsEntity> entityPrototypes = null;
		try {
			Map<String, IApsEntity> modelMap = this.getContentManager().getEntityPrototypes();
			entityPrototypes = new ArrayList<IApsEntity>(modelMap.values());
			BeanComparator comparator = new BeanComparator("typeDescr");
			Collections.sort(entityPrototypes, comparator);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentTypes");
			throw new RuntimeException("Error extracting content types", t);
		}
		return entityPrototypes;
	}
	
	public List<SelectItem> getAllowedFilterTypes() throws ApsSystemException {
		List<SelectItem> types = new ArrayList<SelectItem>();
		try {
			types.add(new SelectItem(IContentListFilterAction.METADATA_KEY_PREFIX + IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, this.getText("label.creationDate")));
			types.add(new SelectItem(IContentListFilterAction.METADATA_KEY_PREFIX + IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, this.getText("label.lastModifyDate")));
			//String contentType = this.getShowlet().getConfig().getProperty(IContentListWidgetHelper.WIDGET_PARAM_CONTENT_TYPE);
			Content prototype = this.getContentManager().createContentType(this.getContentType());
			List<AttributeInterface> contentAttributes = prototype.getAttributeList();
			for (int i=0; i<contentAttributes.size(); i++) {
				AttributeInterface attribute = contentAttributes.get(i);
				if (attribute.isSearchable()) {
					types.add(new SelectItem(attribute.getName(), this.getText("label.attribute", new String[]{attribute.getName()})));
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedFilterTypes");
			throw new ApsSystemException("Error extracting allowed filter types", t);
		}
		return types;
	}
	
	public Map<String, String> getAvailableFeedTypes() {
		return this.getRssManager().getAvailableFeedTypes();
	}
	
	public Map<String, String> getAvailableContentTypes() {
		return this.getRssManager().getAvailableContentTypes();
	}
	
	public RssContentMapping getContentMapping(String typeCode) {
		return this.getRssManager().getContentMapping(typeCode);
	}
	
	public String selectContentType() {
		try {
			if (null != this.getContentType() &&  this.getContentType().trim().length() > 0) {
				// nothing to do
			} else {
				String[] args = {"contentType"};
				this.addFieldError("contentType", this.getText("requiredstring", args));
				return Action.INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "selectContentType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private Channel buildChannel() {
		Channel channel = new Channel();
		channel.setActive(this.isActive());
		channel.setCategory(this.getCategory());
		channel.setContentType(this.getContentType());
		channel.setDescription(this.getDescription());
		channel.setFeedType(this.getFeedType());
		channel.setTitle(this.getTitle());
		channel.setFilters(this.getFilters());
		if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
			channel.setId(this.getId());
		}
		if (this.getMaxContentsSize() > 0) {
			channel.setMaxContentsSize(this.getMaxContentsSize());
		}
		return channel;
	}

	public String addFilter() {
		try {
			List<Properties> properties = this.buildFilterProperties();
			Properties newFilter = this.getNewFilter();
			if (null != newFilter) {
				properties.add(newFilter);
			}
			String newShowletParam = FilterUtils.getShowletParam(properties);
			this.setFilters(newShowletParam);
			this.setFiltersProperties(properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addFilter");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private List<Properties> buildFilterProperties() {
		String filters = this.getFilters();
		if (null == filters || filters.trim().length() == 0) {
			filters = null;
		}
		List<Properties> properties = FilterUtils.getFiltersProperties(filters);
		return properties;
	}

	public String removeFilter() {
		try {
			String filters = this.getFilters();
			List<Properties> properties = FilterUtils.getFiltersProperties(filters);
			int filterIndex = this.getFilterIndex();
			properties.remove(filterIndex);
			this.setFiltersProperties(properties);
			String newShowletParam = FilterUtils.getShowletParam(properties);
			this.setFilters(newShowletParam);
			this.setFiltersProperties(properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFilter");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String moveFilter() {
		try {
			//ESTRAI "filters" campo testo
			String filters = this.getFilters();
			
			//Estrai lista properties da testo
			List<Properties> properties = FilterUtils.getFiltersProperties(filters);
			
			//FAI LO SPOSTAMENTO.
			int filterIndex = this.getFilterIndex();
			Properties element = properties.get(filterIndex);
			if (getMovement().equalsIgnoreCase(MOVEMENT_UP_CODE)){
				if (filterIndex > 0) {
					properties.remove(filterIndex);
					properties.add(filterIndex -1, element);
				}
			} else if (getMovement().equalsIgnoreCase(MOVEMENT_DOWN_CODE)) {
				if (filterIndex < properties.size() -1) {
					properties.remove(filterIndex);
					properties.add(filterIndex + 1, element);
				}
			}
			//Setta Properties
			this.setFiltersProperties(properties);
			
			//crea nuovo "filters" String
			String newShowletParam = FilterUtils.getShowletParam(properties);
			this.setFilters(newShowletParam);
			
			//SETTA property FILTERS nella showlet
			this.setFiltersProperties(properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "moveFilter");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}

	public String getFeedType() {
		return _feedType;
	}
	public void setFeedType(String feedType) {
		this._feedType = feedType;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}
	
	public void setFiltersProperties(List<Properties> filtersProperties) {
		this._filtersProperties = filtersProperties;
	}
	public List<Properties> getFiltersProperties() {
		return _filtersProperties;
	}

	public void setFilterIndex(int filterIndex) {
		this._filterIndex = filterIndex;
	}
	public int getFilterIndex() {
		return _filterIndex;
	}

	public void setMovement(String movement) {
		this._movement = movement;
	}
	public String getMovement() {
		return _movement;
	}
	
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	public String getContentType() {
		return _contentType;
	}

	public void setCategory(String category) {
		this._category = category;
	}
	public String getCategory() {
		return _category;
	}
	
	public int getMaxContentsSize() {
		return _maxContentsSize;
	}
	public void setMaxContentsSize(int maxContentsSize) {
		this._maxContentsSize = maxContentsSize;
	}
	
	public void setFilters(String filters) {
		this._filters = filters;
	}
	public String getFilters() {
		return _filters;
	}

	public void setNewFilter(Properties newFilter) {
		this._newFilter = newFilter;
	}
	public Properties getNewFilter() {
		return _newFilter;
	}
	
	protected IRssManager getRssManager() {
		return _rssManager;
	}
	public void setRssManager(IRssManager rssManager) {
		this._rssManager = rssManager;
	}
	
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private int _id;
	private String _title;
	private String _description;
	private boolean _active;
	private String _feedType;
	private int _filterIndex;
	private String _contentType;
	private String _category;
	private int _maxContentsSize = -1;
	
	private String _filters;
	private String _movement;
	
	private Properties _newFilter;
	
	private List<Properties> _filtersProperties;
	
	private int _strutsAction;
	
	private IRssManager _rssManager;
	private ICategoryManager _categoryManager;
	private IContentManager _contentManager;
	
	public static final String MOVEMENT_UP_CODE ="UP";
	public static final String MOVEMENT_DOWN_CODE ="DOWN";
	
}