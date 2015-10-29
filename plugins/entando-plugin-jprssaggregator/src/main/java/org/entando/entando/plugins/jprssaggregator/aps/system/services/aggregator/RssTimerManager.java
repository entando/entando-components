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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedEvent;
import org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Service hendles the notifications for the AggregatorItemsChangedEvent.
 */
public class RssTimerManager extends AbstractService implements AggregatorItemsChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(RssTimerManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadMap();
		this.startThreads();
		_logger.debug("{}: ready. Active tasks: {}", this.getClass().getName(), this.getTimerTaskMap().size());
	}	

	/**
	 * Load the entries stored in the database and creates a new {@link RssTimerTask} for eache one
	 * @throws ApsSystemException
	 */
	private void loadMap() throws ApsSystemException {
		this.setTimerTaskMap(new HashMap<String, RssTimerTask>());
		List<ApsAggregatorItem> items = this.getAggregatorManager().getItems();
		Iterator<ApsAggregatorItem> it = items.iterator();
		while (it.hasNext()) {
			ApsAggregatorItem item = it.next();
			String key = new Integer(item.getCode()).toString();
			RssTimerTask timerTask = new RssTimerTask(item, this.getAggregatorManager());
			this.getTimerTaskMap().put(key, timerTask);
		}
	}

	@Override
	public void updateTasks(AggregatorItemsChangedEvent event) {
		try {	
			if (event.getOperationCode() == AggregatorItemsChangedEvent.INSERT_OPERATION_CODE) {
				int itemCode = event.getItemCode();
				ApsAggregatorItem item = this.getAggregatorManager().getItem(itemCode);
				RssTimerTask timerTask = new RssTimerTask(item, this.getAggregatorManager());
				this.getTimerTaskMap().put(new Integer(itemCode).toString(), timerTask);
				_logger.trace("jprssaggregator Created new thread: {}", itemCode);
				this.startThread(new Integer(event.getItemCode()).toString());
			} else if (event.getOperationCode() == AggregatorItemsChangedEvent.REMOVE_OPERATION_CODE) {
				String taskKey = new Integer(event.getItemCode()).toString();
				RssTimerTask task = this.getTimerTaskMap().get(taskKey);
				task.cancel();
				this.getTimerTaskMap().remove(taskKey);
				_logger.trace("jprssaggregator Removed thread: {}", taskKey);
			} else if (event.getOperationCode() == AggregatorItemsChangedEvent.UPDATE_OPERATION_CODE) {
				String taskKey = new Integer(event.getItemCode()).toString();
				RssTimerTask task = this.getTimerTaskMap().get(taskKey);
				if (null != task) {
					task.cancel();
				}
				this.getTimerTaskMap().remove(taskKey);
				ApsAggregatorItem item = this.getAggregatorManager().getItem(event.getItemCode());
				RssTimerTask timerTask = new RssTimerTask(item, this.getAggregatorManager());
				this.getTimerTaskMap().put(new Integer(item.getCode()).toString(), timerTask);
				_logger.trace("jprssaggregator Updating thread: {}", taskKey);
				this.startThread(taskKey);
			}
		} catch (Throwable t) {
			_logger.error("error in updateTasks", t);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		try {
			this.getTimer().cancel();
			this.getTimer().purge();
			this.setTimer(null);
			Iterator<RssTimerTask> it = this.getTimerTaskMap().values().iterator();
			while (it.hasNext()) {
				RssTimerTask task = it.next();
				task.cancel();
			}
		} catch (Throwable t) {
			_logger.error("error in destroy", t);
		}
	}
	
	/**
	 * Starts all the tasks
	 */
	public void startThreads()  {
		try {
			this.resetTimer();
			Iterator<String> it = this.getTimerTaskMap().keySet().iterator();
			while (it.hasNext()) {
				String taskId = it.next();
				this.startThread(taskId);
			}
		} catch (Throwable t) {
			_logger.error("error in startThreads", t);
			throw new RuntimeException(t);
		}
	}

	/**
	 * Starts a task by the code 
	 * @param taskId the code of the task
	 */
	private void startThread(String taskId)  {
		_logger.trace("jprssaggregator Starting thread: {}", taskId);
		try {
			RssTimerTask task =	(RssTimerTask) this.getTimerTaskMap().get(taskId);
			ApsAggregatorItem item = task.getItem();
			long delay = new Long(item.getDelay()).longValue();
			this.getTimer().schedule(task, 0, delay * 1000);
		} catch (Throwable t) {
			_logger.error("error in startThread '{}'", taskId, t);
			throw new RuntimeException(t);
		}
	}

	/**
	 * Resets all the tasks
	 */
	private void resetTimer() {
		if (null != getTimer()) {
			this.getTimer().cancel();
			this.getTimer().purge();
			this.setTimer(null);
		}
		this.setTimer(new Timer());
	}

	public void setAggregatorManager(IAggregatorManager aggregatorManager) {
		this._aggregatorManager = aggregatorManager;
	}
	protected IAggregatorManager getAggregatorManager() {
		return _aggregatorManager;
	}

	public void setTimer(Timer timer) {
		this._timer = timer;
	}
	public Timer getTimer() {
		return _timer;
	}

	public void setTimerTaskMap(Map<String, RssTimerTask> timerTaskMap) {
		this._timerTaskMap = timerTaskMap;
	}
	public Map<String, RssTimerTask> getTimerTaskMap() {
		return _timerTaskMap;
	}

	private Map<String, RssTimerTask> _timerTaskMap;
	private IAggregatorManager _aggregatorManager;
	private Timer _timer;

}
