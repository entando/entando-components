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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedEvent;
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
	 * Ping the source and returns a List of contents to insert or update.
	 * @param item the ApsaggregatorItem
	 * @return a List of contents. 
	 * @throws ApsSystemException if an error occurs
	 */
	public void updateSource(ApsAggregatorItem item) throws ApsSystemException;

}