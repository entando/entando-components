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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * Event that is notified when an ApsAggregatorItem changes;
 */
public class AggregatorItemsChangedEvent extends ApsEvent {

	private static final Logger _logger = LoggerFactory.getLogger(AggregatorItemsChangedEvent.class);
	
	public void notify(IManager srv) {
		try {
			((AggregatorItemsChangedObserver) srv).updateTasks(this);
		} catch (Throwable t) {
			_logger.error("error in notify", t);
		}
	}

	public Class getObserverInterface() {
		return AggregatorItemsChangedObserver.class;
	}

	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	public void setItemCode(int itemCode) {
		this._itemCode = itemCode;
	}
	public int getItemCode() {
		return _itemCode;
	}

	private int _itemCode;
	private int _operationCode;


	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;
}
