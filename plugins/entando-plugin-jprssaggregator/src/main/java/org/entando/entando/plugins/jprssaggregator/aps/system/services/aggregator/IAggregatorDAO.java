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

/**
 * Base Inteface for the Data Access Object that handles the ApsAggregatorItems
 */
public interface IAggregatorDAO {
	
	/**
	 * Add a new item in the database
	 * @param item the {@link ApsAggregatorItem} to add
	 */
	public void addItem(ApsAggregatorItem item);
	
	/**
	 * Retrieve the {@link ApsAggregatorItem} according with the id provided
	 * @param code the id of the object
	 * @return a ApsAggregatorItem
	 */
	public ApsAggregatorItem getItem(int code);
	
	/**
	 * Delete an item
	 * @param code the code of the item to delete
	 */
	public void deleteItem(int code);
	
	/**
	 * Gets the list of all the items stored in the database
	 * @return a list containing all the ApsAggregatorItems stored in the database
	 */
	public List<ApsAggregatorItem> getItems();
	
	/**
	 * Updates an object in the database.
	 * @param item the object to update.
	 */
	public void update(ApsAggregatorItem item);
	
}