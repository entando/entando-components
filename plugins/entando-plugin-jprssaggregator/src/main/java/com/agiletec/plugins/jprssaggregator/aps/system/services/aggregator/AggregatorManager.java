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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedEvent;
import com.agiletec.plugins.jprssaggregator.aps.system.services.converter.IRssConverterManager;

/**
 * Implementation for the manager that handles
 * the operations on the ApsAggregatorItem
 */
public class AggregatorManager extends AbstractService implements IAggregatorManager {

	private static final Logger _logger = LoggerFactory.getLogger(AggregatorManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready ", this.getClass().getName());
	}
	
	@Override
	public void addItem(ApsAggregatorItem item) throws ApsSystemException {
		try {
			int code = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			item.setLastUpdate(this.getNeverUpdatedDate());
			item.setCode(code);
			this.getAggregatorDAO().addItem(item);
			this.updateSource(item);
			notifyItemsChangedEvent(item.getCode(), AggregatorItemsChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("An error occurred adding a new ApsAggregatorItem", t);
			throw new ApsSystemException("An error occurred adding a new ApsAggregatorItem", t);
		}
	}
	
	@Override
	public ApsAggregatorItem getItem(int code) throws ApsSystemException {
		ApsAggregatorItem item = null;
		try {
			item = this.getAggregatorDAO().getItem(code);
		} catch (Throwable t) {
			_logger.error("An error occurred getting the ApsAggregatorItem with code {}", code, t);
			throw new ApsSystemException("An error occurred getting the ApsAggregatorItem with code: " + code, t);
		}
		return item;
	}
	
	@Override
	public void deleteItem(int code) throws ApsSystemException {
		try {
			this.getAggregatorDAO().deleteItem(code);
			notifyItemsChangedEvent(code, AggregatorItemsChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("An error occurred trying to delete the ApsAggregatorItem {}", code, t);
			throw new ApsSystemException ("An error occurred trying to delete the ApsAggregatorItem: " + code, t);
		}
	}
	
	@Override
	public List<ApsAggregatorItem> getItems() throws ApsSystemException {
		List<ApsAggregatorItem> items = new ArrayList<ApsAggregatorItem>();
		try {
			items = this.getAggregatorDAO().getItems();
		} catch (Throwable t) {
			_logger.error("An error occurred loading the list", t);
			throw new ApsSystemException ("An error occurred loading the list", t);
		}
		return items;
	}
	
	@Override
	public void update(ApsAggregatorItem item) throws ApsSystemException {
		try {
			this.getAggregatorDAO().update(item);
			notifyItemsChangedEvent(item.getCode(), AggregatorItemsChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("An error occurred updating the item {}", item.getCode(), t);
			throw new ApsSystemException ("An error occurred updating the item " + item.getCode(), t);
		}
	}
	
	@Override
	public synchronized void updateSource(ApsAggregatorItem item) throws ApsSystemException {
		try {
			List<Content> contents = this.getRssConverterManager().getContents(item);
			Iterator<Content> contentListIterator = contents.iterator();
			while (contentListIterator.hasNext()) {
				Content content = contentListIterator.next();
				content.setLastEditor("rss_aggregator");
				this.getContentManager().saveContent(content);
				if (this.getRssConverterManager().getAggregatorConfig(content.getTypeCode()).isInsertOnline()) {
					this.getContentManager().insertOnLineContent(content);
				}
			}
			item.setLastUpdate(new Date());
			this.update(item);
		} catch (Throwable t) {
			_logger.error("An error occurred in updateSource for the item {}", item.getCode(), t);
			throw new ApsSystemException("An error occurred in updateSource for the item " + item.getCode(), t);
		}
	}
	
	private void notifyItemsChangedEvent(int code, int operationCode) {
		AggregatorItemsChangedEvent event = new AggregatorItemsChangedEvent();
		event.setItemCode(code);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
	
	private Date getNeverUpdatedDate() {
		Date date = DateConverter.parseDate("01/01/1900", ApsAggregatorItem.DATE_FORMAT);
		return date;
	}
	
	protected IAggregatorDAO getAggregatorDAO() {
		return _aggregatorDAO;
	}
	public void setAggregatorDAO(IAggregatorDAO aggregatorDAO) {
		this._aggregatorDAO = aggregatorDAO;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IRssConverterManager getRssConverterManager() {
		return _rssConverterManager;
	}
	public void setRssConverterManager(IRssConverterManager rssConverterManager) {
		this._rssConverterManager = rssConverterManager;
	}

	private IAggregatorDAO _aggregatorDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	private IRssConverterManager _rssConverterManager;
	private IContentManager _contentManager;
}
