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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedEvent;

/**
 * Interface for the sercive that manages the sources.
 */
public interface IAggregatorManager {

	/**
	 * Adds a new aggregator source.
	 * After this operation a new event {@link AggregatorItemsChangedEvent} is raised
	 * @param item the source {@link ApsAggregatorItem} to add.
	 * @throws ApsSystemException if an error occurs
	 */
	public void addItem(ApsAggregatorItem item)	throws ApsSystemException;

	/**
	 * Gets the {@link ApsAggregatorItem} by the code
	 * @param code int Id of the object
	 * @return the requested ApsAggregatorItem 
	 * @throws ApsSystemException if an error occurs
	 */
	public ApsAggregatorItem getItem(int code)	throws ApsSystemException;

	/**
	 * Delete an ApsAggregatorItem by code.
	 * After this operation an {@link AggregatorItemsChangedEvent} is raised
	 * @param code Id of the object
	 * @throws ApsSystemException if an error occurs
	 */
	public void deleteItem(int code) throws ApsSystemException;

	/**
	 * Returns the list of all the AggregatoItems stored in the database
	 * The list can be empty, but never null.
	 * @return Returns the list of all the AggregatoItems stored in the database
	 * @throws ApsSystemException if an error occurs
	 */
	public List<ApsAggregatorItem> getItems()	throws ApsSystemException;

	/**
	 * Update the ApsAggregaroItem
	 * After this operation an {@link AggregatorItemsChangedEvent} is raised
	 * @param item the object to update
	 * @throws ApsSystemException if an error occurs
	 */
	public void update(ApsAggregatorItem item) throws ApsSystemException;
	
	/**
	 * Ping the source and extract a List of contents to insert or update.
	 * @param item the ApsaggregatorItem
	 * @throws ApsSystemException if an error occurs
	 */
	public void updateSource(ApsAggregatorItem item) throws ApsSystemException;

}