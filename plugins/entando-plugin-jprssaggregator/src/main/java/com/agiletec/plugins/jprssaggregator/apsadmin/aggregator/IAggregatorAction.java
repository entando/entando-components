/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jprssaggregator.apsadmin.aggregator;

import java.util.List;

import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;

/**
 * Interface for the action that manages the 
 * ApsAggregatorItems in the backend
 */
public interface IAggregatorAction {
	
	/**
	 * Returns the list of all the AggregatoItems stored in the database
	 * The list can be empty, but never null.
	 * @return the list of the AggregatoItems stored in the database
	 */
	public List<ApsAggregatorItem> getAggregatorItems();
	
	/**
	 * Action to insert a new ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String newItem();
	
	/**
	 * Save an ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String save();
	
	/**
	 * Prepare the action to edit an existing ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String edit();
	
	/**
	 * Delete an existing ApsAggregatorItem
	 * @return success if no error occurs
	 */
	public String delete();
	
	/**
	 * Refresh the Item
	 * @return success if no error occurs
	 */
	public String syncronize();
	
	/**
	 * Get the list of the contentTypes defined in the mapping.
	 * @return the list of all the contentTypes suitable for the rss conversion
	 */
	public List<SmallContentType> getContentTypes();
	
}