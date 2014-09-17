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