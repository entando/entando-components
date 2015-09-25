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
package com.agiletec.plugins.jpfacetnav.aps.system.services.content;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
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
