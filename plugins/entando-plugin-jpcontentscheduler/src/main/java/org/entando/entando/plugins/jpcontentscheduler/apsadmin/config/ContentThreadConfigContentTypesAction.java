/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.apsadmin.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.IContentSchedulerManager;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentTypeElem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class ContentThreadConfigContentTypesAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentThreadConfigContentTypesAction.class);

	//private static final String THREAD_CONFIG_SESSION_PARAM = "threadConfig";
	private static final String THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES = "threadConfigContentTypes";

	private static final String ALL_TYPES = "*";

	/**
	 * entrypoint
	 * 
	 * @return
	 */
	public String viewContentTypes() {
		try {
			this.setConfigItemOnSession();

		} catch (Throwable t) {
			_logger.error("Error in viewUsers", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String entryContentType() {

		return Action.SUCCESS;

	}

	public String addContentType() {
		try {

			List<ContentTypeElem> config = this.getTypes();
			ContentTypeElem elem = this.getContentTypeElem();

			ContentTypeElem existingElem = null;
			for (ContentTypeElem contentTypeElem : config) {
				if (contentTypeElem.getContentType().equalsIgnoreCase(this.getContentType())) {
					existingElem = contentTypeElem;
					break;
				}
			}
			if (null != existingElem) {
				existingElem = elem;
			} else {
				config.add(elem);
			}

			this.setConfigItemOnSession(config);

		} catch (Throwable t) {
			_logger.error("Error in addContentType", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String removeContentType() {
		try {
			List<ContentTypeElem> config = this.getTypes();

			if (StringUtils.isNotBlank(this.getContentType())) {
				ContentTypeElem elem = null;
				for (ContentTypeElem contentTypeElem : config) {
					if (contentTypeElem.getContentType().equalsIgnoreCase(this.getContentType())) {
						elem = contentTypeElem;
						break;
					}
				}
				if (null != elem) {
					config.remove(elem);
				}
			}
			this.setConfigItemOnSession(config);
		} catch (Throwable t) {
			_logger.error("Error in removeContentType", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	public String saveContentTypeItem() {
		try {
			ContentThreadConfig config = this.getContentSchedulerManager().getConfig();
			config.setTypesList(this.getTypes());

			this.getContentSchedulerManager().updateConfig(config);

			this.addActionMessage(this.getText("jpcontentscheduler.saveItem.success"));
		} catch (Throwable t) {
			_logger.error("Error saving ContentTypeItem", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}

	private List<ContentTypeElem> setConfigItemOnSession() {
		List<ContentTypeElem> types = this.getContentSchedulerManager().getConfig().getTypesList();
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES, types);
		return types;
	}

	private void setConfigItemOnSession(List<ContentTypeElem> config) {
		this.getRequest().getSession().setAttribute(THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES, config);
	}

	public List<ContentTypeElem> getTypes() {
		return (List<ContentTypeElem>) this.getRequest().getSession().getAttribute(THREAD_CONFIG_SESSION_PARAM_CONTENT_TYPES);
	}

	public List<SmallEntityType> getContentTypes() {
		List<SmallEntityType> smallContentTypes = this.getContentManager().getSmallEntityTypes();
		return smallContentTypes;
	}

	public void setBaseConfigManager(ConfigInterface baseConfigManager) {
		this._baseConfigManager = baseConfigManager;
	}

	public ConfigInterface getBaseConfigManager() {
		return _baseConfigManager;
	}

	public IContentSchedulerManager getContentSchedulerManager() {
		return _contentSchedulerManager;
	}

	public void setContentSchedulerManager(IContentSchedulerManager contentSchedulerManager) {
		this._contentSchedulerManager = contentSchedulerManager;
	}

	public IContentManager getContentManager() {
		return contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this.contentManager = contentManager;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ContentTypeElem getContentTypeElem() {
		return contentTypeElem;
	}

	public void setContentTypeElem(ContentTypeElem contentTypeElem) {
		this.contentTypeElem = contentTypeElem;
	}

	private ConfigInterface _baseConfigManager;
	private IContentSchedulerManager _contentSchedulerManager;
	private IContentManager contentManager;
	private String contentType;

	private ContentTypeElem contentTypeElem;

}
