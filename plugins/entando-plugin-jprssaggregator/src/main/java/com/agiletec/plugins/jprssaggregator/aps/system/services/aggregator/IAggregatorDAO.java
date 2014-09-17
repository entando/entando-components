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
