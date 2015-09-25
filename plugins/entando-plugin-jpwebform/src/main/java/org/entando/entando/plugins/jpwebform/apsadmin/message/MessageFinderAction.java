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
package org.entando.entando.plugins.jpwebform.apsadmin.message;

import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;

/**
 * Implementation for the Operator Message list operations.
 * @author E.Mezzano
 */
public class MessageFinderAction extends AbstractApsEntityFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(MessageFinderAction.class);

	@Override
	public String execute() {
		String result = super.execute();
		if (SUCCESS.equals(result)) {
			try {
				this.addMessageFilters();
			} catch (Throwable t) {
				_logger.error("error in execute", t);
				return FAILURE;
			}
		}
		return result;
	}
	
	@Override
	public List<String> getSearchResult() {
		if (null==this._messageIds) {
			try {
				Integer answered = this.getAnswered();
				if (null==answered) {
					this._messageIds = this.getMessageManager().loadMessagesId(this.getFilters());
				} else {
					boolean answeredFlag = answered.intValue()==1;
					this._messageIds = this.getMessageManager().loadMessagesId(this.getFilters(), answeredFlag);
				}
			} catch (Throwable t) {
				_logger.error("Error searching message identifiers", t);
				throw new RuntimeException("Error searching message identifiers", t);
			}
		}
		return this._messageIds;
	}
	
	public IApsEntity getMessage(String entityId) {
		return this.getEntity(entityId);
	}
	
	/**
	 * Returns the answers to the message of given id.
	 * @param id The identifier of the requested message.
	 * @return The answers to the message of given id.
	 */
	public List<Answer> getAnswers(String id) {
		try {
			return this.getMessageManager().getAnswers(id);
		} catch(Throwable t) {
			_logger.error("Error finding answers to message of id {}", id, t);
			throw new RuntimeException("Error finding answers to message of id " + id, t);
		}
	}
	
	/**
	 * Create the filters for the selection an ordination of messages.
	 * @return The filters for the selection an ordination of messages.
	 */
	protected void addMessageFilters() {
		String author = this.getAuthor();
		if (null!=author && author.length()>0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IFormManager.FILTER_KEY_USERNAME, false, author, false);
			this.addFilter(filterToAdd);
		}
		EntitySearchFilter dateFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_CREATION_DATE, false);
		dateFilter.setOrder(EntitySearchFilter.ASC_ORDER);
		Date startDate = this.getFrom();
		Date endDate = this.getTo();
		if (null!=startDate || null!=endDate) {
			if (null!=endDate) {
				endDate = new Date(endDate.getTime()+86400000);
			}
			dateFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_CREATION_DATE, false, startDate, endDate);
		}
		this.addFilter(dateFilter);
	}
	
	@Override
	protected void deleteEntity(String entityId) throws Throwable {
		//method not supported
	}
	
	public Date getFrom() {
		return _from;
	}
	public void setFrom(Date from) {
		this._from = from;
	}
	
	public Date getTo() {
		return _to;
	}
	public void setTo(Date to) {
		this._to = to;
	}
	
	/**
	 * Returns the message author search filter.
	 * @return The message author search filter.
	 */
	public String getAuthor() {
		return _author;
	}
	
	/**
	 * Sets the message author search filter.
	 * @param author The message author search filter.
	 */
	public void setAuthor(String author) {
		this._author = author;
	}
	
	public Integer getAnswered() {
		return _answered;
	}
	public void setAnswered(Integer answered) {
		this._answered = answered;
	}
	
	@Override
	protected IEntityManager getEntityManager() {
		return this._messageManager;
	}
	protected IFormManager getMessageManager() {
		return this._messageManager;
	}
	public void setMessageManager(IFormManager entityManager) {
		this._messageManager = entityManager;
	}
	
	private Date _from;
	private Date _to;
	private String _author;
	private Integer _answered;
	
	private List<String> _messageIds;
	
	private IFormManager _messageManager;
	
}