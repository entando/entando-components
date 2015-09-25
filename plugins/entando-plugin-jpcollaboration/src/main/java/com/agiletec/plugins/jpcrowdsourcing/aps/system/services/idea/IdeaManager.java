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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.parse.CrowdSourcingConfigDOM;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;

public class IdeaManager extends AbstractService implements IIdeaManager, CategoryUtilizer {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaManager.class);

	@Override
	public void init() throws Exception {
		this.checkCategoryRoot();
		this.loadConfiguration();
		_logger.debug("{} ready", this.getClass().getName() + ": initialized");
	}

	/**
	 * Load the XML configuration
	 * @throws com.agiletec.aps.system.exception.ApsSystemException
	 */
	private void loadConfiguration() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpCrowdSourcingSystemConstants.CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpCrowdSourcingSystemConstants.CONFIG_ITEM);
			}
			CrowdSourcingConfigDOM configDOM = new CrowdSourcingConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("error in load Config", t);
			throw new ApsSystemException("Errore in fase di inizializzazione", t);
		}
	}

	@Override
	public void updateConfig(CrowdSourcingConfig config) {
		try {
			String xml = new CrowdSourcingConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpCrowdSourcingSystemConstants.CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error on update configuration", t);
			throw new RuntimeException("Error on update configuration" , t);
		}
	}

	private void checkCategoryRoot() {
		Category categoryRoot = this.getCategoryManager().getCategory(this.getCategoryRoot());
		if (null != categoryRoot) return;
		try {
			categoryRoot = new Category();
			categoryRoot.setCode(this.getCategoryRoot());
			Category root = this.getCategoryManager().getRoot();
			ApsProperties titles = new ApsProperties();
			Set<Object> langCodes = root.getTitles().keySet();
			Iterator<Object> iter = langCodes.iterator();
			while (iter.hasNext()) {
				Object langCode = (Object) iter.next();
				titles.put(langCode, "Crowd Sourcing Root");
			}
			categoryRoot.setTitles(titles);
			categoryRoot.setParent(root);
			categoryRoot.setParentCode(root.getCode());
			this.getCategoryManager().addCategory(categoryRoot);
			_logger.debug("Crowd Sourcing category root Created ");
		} catch (Throwable t) {
			_logger.error("Error on adding Crowd Sourcing category root", t);
			throw new RuntimeException("Error on adding Crowd Sourcing category root" , t);
		}
	}

	@Override
	public void addIdea(IIdea idea) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			((Idea)idea).setId(String.valueOf(key));
			((Idea)idea).setPubDate(new Date());
			if (this.getConfig().isModerateEntries()) {
				((Idea)idea).setStatus(IIdea.STATUS_TO_APPROVE);
			} else {
				((Idea)idea).setStatus(IIdea.STATUS_APPROVED);
			}
			this.getIdeaDAO().insertIdea(idea);
		} catch (Throwable t) {
			_logger.error("Error in adding new Idea", t);
			throw new ApsSystemException("Error in adding new Idea", t);
		}
	}

	@Override
	public void deleteIdea(String id) throws ApsSystemException {
		try {
			this.getIdeaDAO().removeIdea(id);
		} catch (Throwable t) {
			_logger.error("Error deleting Idea with id {}", id, t);
			throw new ApsSystemException("Error deleting Idea with id " + id, t);
		}
	}

	@Override
	public IIdea getIdea(String id) throws ApsSystemException {
		IIdea idea = null;
		try {
			idea = this.getIdeaDAO().loadIdea(id);
		} catch (Throwable t) {
			_logger.error("Error loading Idea {}", id, t);
			throw new ApsSystemException("Error loading Idea " + id, t);
		}
		return idea;
	}

	@Override
	public List<String> searchIdeas(String instanceCode, Integer status, String text, String category, Integer order) throws ApsSystemException {
		List<String> list = new ArrayList<String>();
		try {
			list = this.getIdeaDAO().searchIdea(instanceCode, status, text, category, order);
		} catch (Throwable t) {
			_logger.error("Error in searchIdeas", t);
			throw new ApsSystemException("Error in searchIdeas", t);
		}
		return list;
	}

	@Override
	public Map<String, Integer> getIdeaTags(Integer status) throws ApsSystemException {
		Map<String, Integer> list = new HashMap<String, Integer>();
		try {
			list = this.getIdeaDAO().loadIdeaTags(status);
		} catch (Throwable t) {
			_logger.error("Error in getIdeaTags by status {}", status, t);
			throw new ApsSystemException("Error in getIdeaTags con status " + status, t);
		}
		return list;
	}


	@Override
	public void updateIdea(IIdea idea) throws ApsSystemException {
		try {
			this.getIdeaDAO().updateIdea(idea);
		} catch (Throwable t) {
			_logger.error("Error on update Idea with id {}", idea.getId(), t);
			throw new ApsSystemException("Error on update Idea with id " + idea.getId(), t);
		}
	}

	@Override
	public StatisticInfoBean getActiveIdeaStatistics(String instanceCode)	throws ApsSystemException {
		StatisticInfoBean bean = null;
		try {
			List<String> instances = new ArrayList<String>();
			instances.add(instanceCode);
			bean = this.getIdeaDAO().loadActiveIdeaStatistics(instances);
		} catch (Throwable t) {
			_logger.error("Error on loading ActiveIdeaStatistics", t);
			throw new ApsSystemException("Error on loading ActiveIdeaStatistics", t);
		}
		return bean;
	}

	@Override
	public StatisticInfoBean getActiveIdeaStatistics(Collection<String> groupCodes)	throws ApsSystemException {
		StatisticInfoBean bean = null;
		try {
			List<String> instances = new ArrayList<String>();
			if (null != groupCodes && !groupCodes.isEmpty()) {
				instances = this.getIdeaInstanceManager().getIdeaInstances(groupCodes, null);
			}
			bean = this.getIdeaDAO().loadActiveIdeaStatistics(instances);
		} catch (Throwable t) {
			_logger.error("Error on loading ActiveIdeaStatistics", t);
			throw new ApsSystemException("Error on loading ActiveIdeaStatistics", t);
		}
		return bean;
	}

	@Override
	public List<IIdea> getCategoryUtilizers(String categoryCode) throws ApsSystemException {
		List<IIdea> list = null;
		try {
			List<String> ideasId = this.getIdeaDAO().getCategoryUtilizers(categoryCode);
			if (null != ideasId && !ideasId.isEmpty()) {
				list = new ArrayList<IIdea>();
				Iterator<String> it = ideasId.iterator();
				while (it.hasNext()) {
					String id = it.next();
					IIdea idea = this.getIdea(id);
					list.add(idea);
				}
			}
		} catch (Throwable t) {
			throw new ApsSystemException("Error while loading referenced ideas : category " + categoryCode, t);
		}
		return list;
	}



	public void setCategoryRoot(String categoryRoot) {
		this._categoryRoot = categoryRoot;
	}
	public String getCategoryRoot() {
		if (null == this._categoryRoot) {
			return DEFAULT_CATEGORY_ROOT;
		}
		return _categoryRoot;
	}

	public void setConfig(CrowdSourcingConfig config) {
		this._config = config;
	}
	public CrowdSourcingConfig getConfig() {
		return _config;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setIdeaDAO(IIdeaDAO ideaDAO) {
		this._ideaDAO = ideaDAO;
	}
	protected IIdeaDAO getIdeaDAO() {
		return _ideaDAO;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private String _categoryRoot;
	private IIdeaDAO _ideaDAO;
	private ICategoryManager _categoryManager;
	private ConfigInterface _configManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private CrowdSourcingConfig _config;
	public static final String DEFAULT_CATEGORY_ROOT = "jpcollaboration_categoryRoot";

}
