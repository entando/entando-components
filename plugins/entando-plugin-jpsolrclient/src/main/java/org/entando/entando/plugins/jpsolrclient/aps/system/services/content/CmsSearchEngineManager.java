/*
 * Copyright 2013-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsolrclient.aps.system.services.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsolrclient.aps.system.SolrConnectorSystemConstants;
import org.entando.entando.plugins.jpsolrclient.aps.system.services.config.SolrConfigDOM;
import org.entando.entando.plugins.jpsolrclient.aps.system.services.config.model.SolrConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.LastReloadInfo;

/**
 * Servizio detentore delle operazioni di indicizzazione 
 * di oggetti ricercabili tramite motore di ricerca.
 * @author W.Ambu - M.Diana - E.Santoboni
 */
public class CmsSearchEngineManager extends AbstractService 
		implements ICmsSearchEngineManager, PublicContentChangedObserver, EntityTypesChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(CmsSearchEngineManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void refresh() throws Throwable {
		this.release();
		this._lastReloadInfo = null;
		this.init();
	}
	
	@Override
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		if (this.getStatus() == STATUS_RELOADING_INDEXES_IN_PROGRESS) {
			this._publicContentChangedEventQueue.add(0, event);
		} else {
			this.manageEvent(event);
		}
	}
    
	private void manageEvent(PublicContentChangedEvent event) {
		Content content = event.getContent();
		try {
			switch (event.getOperationCode()) {
			case PublicContentChangedEvent.INSERT_OPERATION_CODE:
				this.addEntityToIndex(content);
				break;
			case PublicContentChangedEvent.REMOVE_OPERATION_CODE:
				this.deleteIndexedEntity(content.getId());
				break;
			case PublicContentChangedEvent.UPDATE_OPERATION_CODE:
				this.updateIndexedEntity(content);
				break;
			}
		} catch (Throwable t) {
			_logger.error("Errore in notificazione evento", t);
		}
	}
	
	protected void sellOfQueueEvents() {
		int size = this._publicContentChangedEventQueue.size();
		if (size>0) {
			for (int i=0; i<size; i++) {
				PublicContentChangedEvent event = (PublicContentChangedEvent) this._publicContentChangedEventQueue.get(0);
				this.manageEvent(event);
				this._publicContentChangedEventQueue.remove(0);
			}
		}
	}
	
	@Override
	public Thread startReloadContentsReferences() throws ApsSystemException {
		IndexLoaderThread loaderThread = null;
    	if (this.getStatus() == STATUS_READY || this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
    		try {
				this.setStatus(STATUS_RELOADING_INDEXES_IN_PROGRESS);
				Searcher searcher = new Searcher(this.getSolrPath());
				List<String> contentsId = searcher.extractAllContentsId();
    			Indexer indexer = new Indexer(this.getSolrPath(), this.getLangManager());
    			loaderThread = new IndexLoaderThread(this, this.getContentManager(), indexer, contentsId);
    			String threadName = RELOAD_THREAD_NAME_PREFIX + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
    			loaderThread.setName(threadName);
    			loaderThread.start();
                _logger.debug("Index reloader started");
    		} catch (Throwable t) {
    			throw new ApsSystemException("Error reloading indexes", t);
    		}
    	} else {
    		_logger.debug("Index reloader stopped : status {}", this.getStatus());
    	}
    	return loaderThread;
	}
	
	@Override
	public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
		if (((IManager) this.getContentManager()).getName().equals(event.getEntityManagerName())) {
			if (this.getStatus() == STATUS_NEED_TO_RELOAD_INDEXES) {
				return;
			}
			boolean needToReload = false;
			if (event.getOperationCode() == EntityTypesChangingEvent.INSERT_OPERATION_CODE) {
				return;
			} else if (event.getOperationCode() == EntityTypesChangingEvent.REMOVE_OPERATION_CODE) {
				needToReload = true;
			} else if (event.getOperationCode() == EntityTypesChangingEvent.UPDATE_OPERATION_CODE) {
				needToReload = this.verifyReloadingNeeded(event.getOldEntityType(), event.getNewEntityType());
			}
			if (needToReload == true) {
				this.setStatus(STATUS_NEED_TO_RELOAD_INDEXES);
			}
		}
	}
	
	protected boolean verifyReloadingNeeded(IApsEntity oldEntityType, IApsEntity newEntityType) {
		List<AttributeInterface> attributes = oldEntityType.getAttributeList();
		for (int i = 0; i < attributes.size(); i++) {
			AttributeInterface oldAttribute = attributes.get(i);
			AttributeInterface newAttribute = (AttributeInterface) newEntityType.getAttribute(oldAttribute.getName());
			boolean isOldAttributeIndexeable = (oldAttribute.getIndexingType() != null && oldAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
			boolean isNewAttributeIndexeable = (newAttribute != null && newAttribute.getIndexingType() != null && newAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
			if ((isOldAttributeIndexeable && !isNewAttributeIndexeable) || (!isOldAttributeIndexeable && isNewAttributeIndexeable)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void addEntityToIndex(IApsEntity entity) throws ApsSystemException {
		try {
			Indexer indexer = new Indexer(this.getSolrPath(), this.getLangManager());
            indexer.addContent(entity);
        } catch (Throwable t) {
        	_logger.error("Error adding new content", t);
            throw new ApsSystemException("Error adding new content", t);
        }
	}
	
	@Override
	public void deleteIndexedEntity(String entityId) throws ApsSystemException {
		try {
			Indexer indexer = new Indexer(this.getSolrPath(), this.getLangManager());
            indexer.delete(entityId);
        } catch (Throwable t) {
        	_logger.error("error deeting content {} from index", entityId, t);
            throw new ApsSystemException("Error deleting new content", t);
        }
	}
	
	protected void notifyEndingIndexLoading(LastReloadInfo info) {
		try {
			if (info.getResult() == LastReloadInfo.ID_SUCCESS_RESULT) {
				this._lastReloadInfo = info;
			}
		} catch (Throwable t) {
			_logger.error("Error creating LastReloadInfo", t);
		} finally {
			if (this.getStatus() != STATUS_NEED_TO_RELOAD_INDEXES) {
				this.setStatus(STATUS_READY);
			}
		}
	}
	
	@Override
	public LastReloadInfo getLastReloadInfo() {
		return this._lastReloadInfo;
	}
	
	@Override
	public List<String> searchId(String sectionCode, String langCode, String word, Collection<String> allowedGroups) throws ApsSystemException {
		return this.searchEntityId(langCode, word, allowedGroups);
	}
	
	@Override
	public List<String> searchEntityId(String langCode, String word,
			Collection<String> allowedGroups) throws ApsSystemException {
		List<String> contentsId = new ArrayList<String>();
    	try {
			Searcher searcher = new Searcher(this.getSolrPath());
    		contentsId = searcher.searchContentsId(langCode, word, allowedGroups, this.getMaxResultSize());
    	} catch (ApsSystemException e) {
    		_logger.error("Error searching contents id", e);
    		throw e;
    	}
    	return contentsId;
	}
	
	@Override
	public int getStatus() {
		return this._status;
	}
	protected void setStatus(int status) {
		this._status = status;
	}
	
	@Override
	public void updateIndexedEntity(IApsEntity entity) throws ApsSystemException {
		try {
            this.deleteIndexedEntity(entity.getId());
            this.addEntityToIndex(entity);
        } catch (ApsSystemException e) {
        	_logger.error("Error updating content indexes", e);
            throw e;
        }
	}
	
	public void writeConfiguration(SolrConfig solrConfig) throws ApsSystemException {
		try {
			String marshalInfo = SolrConfigDOM.marshalConfig(solrConfig);
			this.getConfigManager().updateConfigItem(SolrConnectorSystemConstants.JPSOLRCLIENT_SYSTEM_CONFIG_NAME, marshalInfo);
		} catch (ApsSystemException e) {
			_logger.error("Error updating configuration", e);
            throw e;
        }
	}
	
	public SolrConfig readConfiguration() throws ApsSystemException {
		SolrConfig solrConfig = new SolrConfig();
		try {
			String configItem = this.getConfigManager().getConfigItem(SolrConnectorSystemConstants.JPSOLRCLIENT_SYSTEM_CONFIG_NAME);
			if(StringUtils.isNotEmpty(configItem)) {
				solrConfig = SolrConfigDOM.unmarshalConfig(configItem);
			}
		} catch (ApsSystemException e) {
			_logger.error("Error in readConfiguration", e);
            throw e;
        }
		return solrConfig;
	}
	
	protected String getSolrPath() throws ApsSystemException {
		SolrConfig solrConfiguration = this.readConfiguration();
		String serverUrl = "";
		if(null != solrConfiguration){
			serverUrl = solrConfiguration.getProviderUrl();
		}
		if (StringUtils.isBlank(serverUrl)) {
            serverUrl = SolrConnectorSystemConstants.SERVER_URL_DEFAULT_VALUE;
        }
        return serverUrl;
	}
	
	protected int getMaxResultSize() throws ApsSystemException {
		SolrConfig solrConfiguration = this.readConfiguration();
		String maxResultSizeString = "";
		if(null != solrConfiguration){
			maxResultSizeString = solrConfiguration.getMaxResultSize();
		}
		int maxResultSize = -1;
		try {
			maxResultSize = Integer.parseInt(maxResultSizeString);
		} catch (Exception e) {}
			if (maxResultSize < 1) {
				maxResultSize = SolrConnectorSystemConstants.MAX_RESULT_SIZE_DEFAULT_VALUE;
        }
        return maxResultSize;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
	
    private int _status;
    private LastReloadInfo _lastReloadInfo;
    private List<ApsEvent> _publicContentChangedEventQueue = new ArrayList<ApsEvent>();
	
	private ILangManager _langManager;
    private IContentManager _contentManager;
	private ConfigInterface _configManager;
	
	public static final String RELOAD_THREAD_NAME_PREFIX = "RELOAD_INDEX_";
    
	public static final String CONTENT_ID_FIELD_NAME = "id";
    public static final String CONTENT_GROUP_FIELD_NAME = "group";
	
}
