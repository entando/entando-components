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
package com.agiletec.plugins.jpblog.aps.system.services.blog;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;

public interface IBlogManager {

	/**
	 * Returns just one category code.
	 * Will be the "blog root category code" when specified in config, otherwise
	 * the returned code will be the root of all the categories
	 * @return
	 */
	public List<String> getSpecialCategories();

	/**
	 * Returns occurrences
	 * @param contentTypeCodes
	 * @param facetNodeCodes
	 * @param groupCodes
	 * @return occurrences
	 * @throws ApsSystemException
	 */
	@Deprecated
	public Map<Category, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes) throws ApsSystemException;

	/**
	 * Returns occurrences
	 * @param contentTypeCodes
	 * @param facetNodeCodes
	 * @param groupCodes
	 * @return occurrences
	 * @throws ApsSystemException
	 */
	public Map<Category, Integer> getOccurrences(List<String> contentTypeCodes, List<String> groupCodes) throws ApsSystemException;

	/**
	 * Calcola la data del contenuto più "vecchio" e rende una lista raggruppata per anno-mese
	 * @param contentTypeCodes
	 * @param facetNodeCodes
	 * @param groupCodes
	 * @return
	 * @throws ApsSystemException
	 */
	public List<BlogArchiveInfoBean> getOccurrencesByDate(String contentType, List<String> groupCodes) throws ApsSystemException;

	public IBlogConfig getConfig();

	public void updateConfig(IBlogConfig config) throws ApsSystemException;
}
