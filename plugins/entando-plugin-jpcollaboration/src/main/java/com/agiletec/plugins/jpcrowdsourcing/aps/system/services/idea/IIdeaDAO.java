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

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IIdeaDAO {


	public void insertIdea(IIdea idea);

	public void removeIdea(String id);

	public void removeIdeas(List<String> codes, Connection conn);

	public IIdea loadIdea(String id);

	public void updateIdea(IIdea idea);

	/**
	 *
	 * @param status Ca be null
	 * @param text Searches in title and text
	 * @param category TODO
	 * @param order One of the SORT constants. Can be null
	 * @return
	 */
	public List<String> searchIdea(String instanceCode, Integer status, String text, String category, Integer order);

	/**
	 * Restituisce una mappa ["CategoryCode";"Numero di idee"]
	 * @param status
	 * @return
	 */
	public Map<String, Integer> loadIdeaTags(Integer status);

	public StatisticInfoBean loadActiveIdeaStatistics(Collection<String> instanceCode);


	public List<String> getCategoryUtilizers(String categoryCode);

	public static final int SORT_LATEST = 0;
	public static final int SORT_MOST_RATED = 1;

}
