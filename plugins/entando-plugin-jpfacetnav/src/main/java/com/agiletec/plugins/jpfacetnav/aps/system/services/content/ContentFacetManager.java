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
package com.agiletec.plugins.jpfacetnav.aps.system.services.content;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * 
 * @author E.Santoboni
 */
public class ContentFacetManager extends AbstractService implements IContentFacetManager {

	private static final Logger _logger = LoggerFactory.getLogger(ContentFacetManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getName());
	}
	
	@Override
	public List<String> loadContentsId(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes) throws ApsSystemException {
		List<String> items = null;
		try {
			items = this.getContentFacetSearcherDAO().loadContentsId(contentTypeCodes, facetNodeCodes, groupCodes);
		} catch (Throwable t) {
			_logger.error("Error loading contents id", t);
			throw new ApsSystemException("Error loading contents id", t);
		}
		return items;
	}
	
	@Override
	public Map<String, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes) throws ApsSystemException {
		Map<String, Integer> occurrence = null;
		try {
			occurrence = this.getContentFacetSearcherDAO().getOccurrences(contentTypeCodes, facetNodeCodes, groupCodes);
		} catch (Throwable t) {
			_logger.error("Error loading occurrences", t);
			throw new ApsSystemException("Error loading occurrences", t);
		}
		return occurrence;
	}
	
	protected IContentFacetSearcherDAO getContentFacetSearcherDAO() {
		return this._contentSearcherDao;
	}
	public void setContentFacetSearcherDAO(IContentFacetSearcherDAO contentSearcherDao) {
		this._contentSearcherDao = contentSearcherDao;
	}
	
	private IContentFacetSearcherDAO _contentSearcherDao;
	
}
