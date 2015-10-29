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
package org.entando.entando.plugins.jprssaggregator.apsadmin.aggregator;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.IAggregatorManager;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.converter.IRssConverterManager;
import com.rometools.rome.feed.synd.SyndEntry;

/**
 * This class executes the operations for the backend administration
 * for the {@link ApsAggregatorItem} objects
 */
public class AggregatorAction extends AbstractTreeAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkLink();
	}
	
	private void checkLink() {
		try {
			//if (isValidUrl(this.getLink())) {
			List<SyndEntry> entries = this.getRssConverterManager().getRssEntries("rss_2.0", this.getLink());
			if (null == entries || entries.isEmpty()) {
				this.addFieldError("link", this.getText("jprssaggregator.error.link.nofeeds"));
			}
			this.checkDuplicateLink();
			//} else {
			//	this.addFieldError("link", this.getText("jprssaggregator.error.link.invalid"));
			//}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "error in checkLink");
		}
	}

	private void checkDuplicateLink() {
		try {
			List<ApsAggregatorItem> items = this.getAggregatorItems();
			if (null != items && items.size() > 0) {
				for (int i = 0; i < items.size(); i++) {
					ApsAggregatorItem aggregatorItem = items.get(i);
					boolean sameItem = this.getCode() == aggregatorItem.getCode();
					if (this.getLink().equals(aggregatorItem.getLink()) && !sameItem) {
						String arg[] = new String[]{aggregatorItem.getDescr()};
						this.addFieldError("link", this.getText("jprssaggregator.error.link.duplicated", arg));
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "error in checkDuplicateLink");
		} 
	}
	/*
	private static boolean isValidUrl(String link) {
		boolean valid = false;
		try {
			URL url = new URL(link);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "Entando jprssaggregaror");
			if (null != connection.getContent()) return true;
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("unable to parse url", t);
		}
		return valid;
	}
	*/
	/**
	 * Returns the list of all the AggregatoItems stored in the database
	 * The list can be empty, but never null.
	 * @return the list of the AggregatoItems stored in the database
	 */
	public List<ApsAggregatorItem> getAggregatorItems() {
		List<ApsAggregatorItem> items = null;
		try {
			items = this.getAggregatorManager().getItems();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAggregatorItems");
		}
		return items;
	}
	
	/**
	 * Action to insert a new ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String newItem() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "newItem");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Save an ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String save() {
		try {
			int strutsAction = this.getStrutsAction();
			ApsAggregatorItem item = this.createItem();
			if (strutsAction == ApsAdminSystemConstants.ADD) {
				this.getAggregatorManager().addItem(item);
			} else if (strutsAction == ApsAdminSystemConstants.EDIT) {
				this.getAggregatorManager().update(item);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Prepare the action to edit an existing ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String edit() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
			String check = this.populateForm();
			if (null != check) {
				return check;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Delete an existing ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String delete() {
		try {
			ApsAggregatorItem item = this.getAggregatorManager().getItem(this.getCode());
			if (null == item) {
				this.addActionError(this.getText("jprssaggregator.error.itemNull"));
				return INPUT;
			}
			this.getAggregatorManager().deleteItem(item.getCode());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteItem");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Get the list of the contentTypes defined in the mapping.
	 * @return the list of all the contentTypes suitable for the rss conversion
	 */
	public List<SmallContentType> getContentTypes() {
		List<String> tempContentTypes = new ArrayList<String>();
		Set<String> mappings = this.getRssConverterManager().getMappings();
		tempContentTypes.addAll(mappings);
		List<SmallContentType> contentTypes = new ArrayList<SmallContentType>();
		for (int i = 0; i < tempContentTypes.size(); i++) {
			String code = tempContentTypes.get(i);
			SmallContentType smallContentType = this.getSmallContentType(code);
			contentTypes.add(smallContentType);
		}
		return contentTypes;
	}
	
	/**
	 * Refresh the Item
	 * @return success if no error occurs
	 */
	public String syncronize() {
		try {
			ApsAggregatorItem item = this.getAggregatorManager().getItem(this.getCode());
			this.getAggregatorManager().updateSource(item);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "syncronize");
			return FAILURE;
		}
		return SUCCESS;
	}

	private ApsAggregatorItem createItem() throws Throwable {
		ApsAggregatorItem item = new ApsAggregatorItem();
		if (getStrutsAction() == ApsAdminSystemConstants.EDIT) {
			item.setCode(this.getCode());
			item.setLastUpdate(new Date(this.getLastUpdate().getTime()));
		}
		System.out.println(this.getContentType());
		item.setContentType(this.getContentType());
		item.setDelay(this.getDelay());
		item.setLink(this.getLink());
		item.setDescr(this.getDescr());
		item.setCategories(this.getCategories());
		return item;
	}

	protected String populateForm() throws ApsSystemException, IOException {
		int code = this.getCode();
		ApsAggregatorItem item = this.getAggregatorManager().getItem(code);
		if (null == item) {
			this.addActionError(this.getText("jprssaggregator.error.itemNotFound"));
			return "itemList";
		}
		if (null != item.getCategories()) {
			this.setXmlCategories(item.getCategories().toXml());
		}
		this.setCode(item.getCode());
		this.setContentType(item.getContentType());
		this.setDescr(item.getDescr());
		this.setDelay(item.getDelay());
		this.setLastUpdate(new Timestamp(item.getLastUpdate().getTime()));
		this.setLink(item.getLink());
		return null;
	}
	
	public Map<Integer, String> getDelays() {
		Map delays = new TreeMap<Integer, String>();
		Iterator it = this._delays.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String value = (String)pairs.getValue();
			String[] splitted = value.split("_");
			delays.put(pairs.getKey(), splitted[0] + " " + this.getText(splitted[1]));
		}
		return delays;
	}
	
	public Category getCategoryRoot() {
		return this.getCategoryManager().getRoot();
	}
	
	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}
	
	public String joinCategory() {
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.getCode().equals(category.getParentCode())) { 
				ApsProperties currentCats = this.getCategories();
				currentCats.put(category.getCode(), category.getFullTitle());
				this.setXmlCategories(currentCats.toXml());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinCategory");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String removeCategory() {
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				ApsProperties currentCats = this.getCategories();
				currentCats.remove(categoryCode);
				this.setXmlCategories(currentCats.toXml());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeCategory");
			return FAILURE;
		}
		return SUCCESS;
	}

	public ApsProperties getCategories() throws Throwable {
		ApsProperties properties = new ApsProperties();
		if (null != this.getXmlCategories() && this.getXmlCategories().trim().length() > 0) {
			properties.loadFromXml(this.getXmlCategories());
		}
		this.purgeCategories(properties);
		return properties;
	}
	
	/**
	 * Rimuove eventuali categorie non più esistenti
	 * @param properties
	 */
	private void purgeCategories(ApsProperties properties) {
		for (Enumeration e = properties.keys() ; e.hasMoreElements() ;) {
			String categoryCode = (String) e.nextElement();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null == category) {
				properties.remove(categoryCode);
			}
		}
	}

	public SmallContentType getSmallContentType(String code) {
		SmallContentType smallContentType = null;
		Map<String, SmallContentType> smallContentTypes = this.getContentManager().getSmallContentTypesMap();
		if (smallContentTypes.containsKey(code)) {
			smallContentType = (SmallContentType) smallContentTypes.get(code);
		}
		return smallContentType;
	}
	
	public void setDelays(Map<Integer, String> delays) {
		this._delays = delays;
	}

	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}
	public String getCategoryCode() {
		return _categoryCode;
	}

	public void setXmlCategories(String xmlCategories) {
		this._xmlCategories = xmlCategories;
	}
	public String getXmlCategories() {
		return _xmlCategories;
	}

	public int getCode() {
		return _code;
	}
	public void setCode(int code) {
		this._code = code;
	}

	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	public String getContentType() {
		return _contentType;
	}

	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}

	public long getDelay() {
		return _delay;
	}
	public void setDelay(long delay) {
		this._delay = delay;
	}

	public String getLink() {
		return _link;
	}
	public void setLink(String link) {
		this._link = link;
	}

	public Timestamp getLastUpdate() {
		return _lastUpdate;
	}
	public void setLastUpdate(Timestamp lastUpdate) {
		this._lastUpdate = lastUpdate;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}
	
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	
	public void setAggregatorManager(IAggregatorManager aggregatorManager) {
		this._aggregatorManager = aggregatorManager;
	}
	protected IAggregatorManager getAggregatorManager() {
		return _aggregatorManager;
	}
	
	public void setRssConverterManager(IRssConverterManager rssConverterManager) {
		this._rssConverterManager = rssConverterManager;
	}
	protected IRssConverterManager getRssConverterManager() {
		return _rssConverterManager;
	}
	
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	protected IContentManager getContentManager() {
		return _contentManager;
	}

	private int _code;
	private String _contentType;
	private String _descr;
	private long _delay;
	private String _link;
	private Timestamp _lastUpdate;
	private String _categoryCode;
	private String _xmlCategories;

	private int _strutsAction;
	private Map<Integer, String> _delays;

	private IAggregatorManager _aggregatorManager;
	private IRssConverterManager _rssConverterManager;
	private ICategoryManager _categoryManager;
	private IContentManager _contentManager;
	
}