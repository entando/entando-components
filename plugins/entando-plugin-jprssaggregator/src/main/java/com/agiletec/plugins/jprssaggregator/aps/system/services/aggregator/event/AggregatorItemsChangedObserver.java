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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.event;

import com.agiletec.aps.system.common.notify.ObserverService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;

/**
 * Interface for the services that must handle event notifications about 
 * the {@link ApsAggregatorItem} 
 */
public interface AggregatorItemsChangedObserver extends ObserverService {

	/**
	 * Operation performed by the service that must respond
	 * to the AggregatorItemsChangedEvent notification
	 * @param event the event notified
	 * @throws ApsSystemException if an error occurs
	 */
	public void updateTasks(AggregatorItemsChangedEvent event) throws ApsSystemException;

}
