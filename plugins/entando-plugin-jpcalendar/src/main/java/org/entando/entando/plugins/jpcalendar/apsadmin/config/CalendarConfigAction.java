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
package org.entando.entando.plugins.jpcalendar.apsadmin.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.CalendarConfig;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.ICalendarManager;

/**
 * @author E.Santoboni
 */
public class CalendarConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CalendarConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			String contentTypeCode = this.getContentType();
			if (null == this.getContentManager().getSmallContentTypesMap().get(contentTypeCode)) {
				String[] args = {this.getContentType()};
				this.addFieldError("contentType", this.getText("error.calendarConfig.invalidContentType", args));
			} else {
				Content prototype = this.getContentManager().createContentType(contentTypeCode);
				if (null == this.getStartDateAttributeName() || null == prototype.getAttribute(this.getStartDateAttributeName())) {
					String[] args = {this.getStartDateAttributeName()};
					this.addFieldError("startDateAttributeName", this.getText("error.calendarConfig.invalidStartDateAttribute", args));
				}
				if (null == this.getEndDateAttributeName() || null == prototype.getAttribute(this.getEndDateAttributeName())) {
					String[] args = {this.getEndDateAttributeName()};
					this.addFieldError("endDateAttributeName", this.getText("error.calendarConfig.invalidEndDateAttribute", args));
				}
				if (null != this.getStartDateAttributeName() && null != this.getEndDateAttributeName() 
						&& this.getEndDateAttributeName().equals(this.getStartDateAttributeName())) {
					this.addFieldError("endDateAttributeName", this.getText("error.calendarConfig.endDateEqualsStartDate"));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error validating config", t);
		}
	}
	
	public String edit() {
		try {
			CalendarConfig config = this.getCalendarManager().getConfig();
			if (null != config && null != this.getContentManager().getSmallContentTypesMap().get(config.getContentTypeCode())) {
				this.setContentType(config.getContentTypeCode());
				this.setStartDateAttributeName(config.getStartAttributeName());
				this.setEndDateAttributeName(config.getEndAttributeName());
			}
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String configContentType() {
		this.setStartDateAttributeName(null);
		this.setEndDateAttributeName(null);
		return SUCCESS;
	}
	
	public String changeContentType() {
		this.setContentType(null);
		this.setStartDateAttributeName(null);
		this.setEndDateAttributeName(null);
		return SUCCESS;
	}
	
	public String save() {
		try {
			CalendarConfig config = new CalendarConfig();
			config.setContentTypeCode(this.getContentType());
			config.setStartAttributeName(this.getStartDateAttributeName());
			config.setEndAttributeName(this.getEndDateAttributeName());
			this.getCalendarManager().updateConfig(config);
			this.addActionMessage(this.getText("message.calendarConfig.updated"));
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<SelectItem> getAllowedDateAttributes() throws ApsSystemException {
		List<SelectItem> attributes = new ArrayList<SelectItem>();
		try {
			Content prototype = this.getContentManager().createContentType(this.getContentType());
			if (null == prototype) {
				return attributes;
			}
			List<AttributeInterface> contentAttributes = prototype.getAttributeList();
			for (int i=0; i<contentAttributes.size(); i++) {
				AttributeInterface attribute = contentAttributes.get(i);
				if ((attribute instanceof DateAttribute) && attribute.isSearcheable()) {
					attributes.add(new SelectItem(attribute.getName(), this.getText("label.attribute", new String[]{attribute.getName()})));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting allowed filter types", t);
			throw new ApsSystemException("Error extracting allowed filter types", t);
		}
		return attributes;
	}
	
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getStartDateAttributeName() {
		return _startDateAttributeName;
	}
	public void setStartDateAttributeName(String startDateAttributeName) {
		this._startDateAttributeName = startDateAttributeName;
	}
	
	public String getEndDateAttributeName() {
		return _endDateAttributeName;
	}
	public void setEndDateAttributeName(String endDateAttributeName) {
		this._endDateAttributeName = endDateAttributeName;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	public ICalendarManager getCalendarManager() {
		return calendarManager;
	}

	public void setCalendarManager(ICalendarManager calendarManager) {
		this.calendarManager = calendarManager;
	}
	
	private String _contentType;
	private String _startDateAttributeName;
	private String _endDateAttributeName;
	
	private IContentManager _contentManager;
	private ICalendarManager calendarManager;
	
}