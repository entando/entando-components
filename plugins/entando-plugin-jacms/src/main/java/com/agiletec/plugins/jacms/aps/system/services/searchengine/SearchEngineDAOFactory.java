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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Classe factory degli elementi ad uso del SearchEngine.
 *
 * @author E.Santoboni
 */
public class SearchEngineDAOFactory implements ISearchEngineDAOFactory {

	private static final Logger _logger = LoggerFactory.getLogger(SearchEngineDAOFactory.class);

	@Override
	public void init() throws Exception {
		this._subDirectory = this.getConfigManager().getConfigItem(JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR);
		if (_subDirectory == null) {
			throw new ApsSystemException("Item configurazione assente: " + JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR);
		}
	}

	@Override
	public IIndexerDAO getIndexer() throws ApsSystemException {
		return this.getIndexer(this._subDirectory);
	}

	@Override
	public ISearcherDAO getSearcher() throws ApsSystemException {
		return this.getSearcher(this._subDirectory);
	}

	@Override
	public IIndexerDAO getIndexer(String subDir) throws ApsSystemException {
		IIndexerDAO indexerDao = null;
		try {
			Class indexerClass = Class.forName(this.getIndexerClassName());
			indexerDao = (IIndexerDAO) indexerClass.newInstance();
			indexerDao.setLangManager(this.getLangManager());
			indexerDao.init(this.getDirectory(subDir));
		} catch (Throwable t) {
			_logger.error("Error getting indexer", t);
			throw new ApsSystemException("Error creating new indexer", t);
		}
		return indexerDao;
	}

	@Override
	public ISearcherDAO getSearcher(String subDir) throws ApsSystemException {
		ISearcherDAO searcherDao = null;
		try {
			Class searcherClass = Class.forName(this.getSearcherClassName());
			searcherDao = (ISearcherDAO) searcherClass.newInstance();
			searcherDao.init(this.getDirectory(subDir));
		} catch (Throwable t) {
			_logger.error("Error creating new searcher", t);
			throw new ApsSystemException("Error creating new searcher", t);
		}
		return searcherDao;
	}

	@Override
	public void updateSubDir(String newSubDirectory) throws ApsSystemException {
		this.getConfigManager().updateConfigItem(JacmsSystemConstants.CONFIG_ITEM_CONTENT_INDEX_SUB_DIR, newSubDirectory);
		String oldDir = _subDirectory;
		this._subDirectory = newSubDirectory;
		this.deleteSubDirectory(oldDir);
	}

	private File getDirectory(String subDirectory) throws ApsSystemException {
		String dirName = this.getIndexDiskRootFolder();
		if (!dirName.endsWith("/")) {
			dirName += "/";
		}
		dirName += "cmscontents/" + subDirectory;
		_logger.debug("Index Directory: {}", dirName);
		File dir = new File(dirName);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
			_logger.debug("Index Directory created");
		}
		if (!dir.canRead() || !dir.canWrite()) {
			throw new ApsSystemException(dirName + " does not have r/w rights");
		}
		return dir;
	}

	@Override
	public void deleteSubDirectory(String subDirectory) {
		String dirName = this.getIndexDiskRootFolder();
		if (!dirName.endsWith("/") || !dirName.endsWith(File.separator)) {
			dirName += File.separator;
		}
		dirName += subDirectory;
		File dir = new File(dirName);
		if (dir.exists() || dir.isDirectory()) {
			String[] filesName = dir.list();
			for (int i = 0; i < filesName.length; i++) {
				File fileToDelete = new File(dirName + File.separator + filesName[i]);
				boolean fileDeleted = fileToDelete.delete();

				if(!fileDeleted) {
					_logger.warn("Failed to delete file {} ",fileToDelete.getAbsolutePath());
				}

			}
			boolean deleted = dir.delete();
			if(!deleted) {
				_logger.warn("Failed to delete file {}", deleted);
			}else {
				_logger.debug("Deleted subfolder {}", subDirectory);
			}
		}
	}

	public String getIndexerClassName() {
		return _indexerClassName;
	}

	public void setIndexerClassName(String indexerClassName) {
		this._indexerClassName = indexerClassName;
	}

	public String getSearcherClassName() {
		return _searcherClassName;
	}

	public void setSearcherClassName(String searcherClassName) {
		this._searcherClassName = searcherClassName;
	}

	protected String getIndexDiskRootFolder() {
		return _indexDiskRootFolder;
	}

	public void setIndexDiskRootFolder(String indexDiskRootFolder) {
		this._indexDiskRootFolder = indexDiskRootFolder;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configService) {
		this._configManager = configService;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	private String _indexerClassName;
	private String _searcherClassName;

	private String _indexDiskRootFolder;
	private String _subDirectory;

	private ConfigInterface _configManager;
	private ILangManager _langManager;

}
