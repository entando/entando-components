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
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedEvent;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedObserver;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Manager of page models definition for my portal.
 * @author E.Santoboni
 */
@Aspect
public class MyPortalPageModelManager extends AbstractService implements IMyPortalPageModelManager, PageModelChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(MyPortalPageModelManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadPageModelConfigurations();
		_logger.debug("{} ready. initialized {} page model configurations", this.getClass().getName() ,this._configuration.size());
	}
	
	@Override
	public void updateFromPageModelChanged(PageModelChangedEvent event) {
		String pageModelCode = (null != event && null != event.getPageModel()) ? event.getPageModel().getCode() : null;
		if (null != pageModelCode && null != this.getPageModelConfig(pageModelCode)) {
			try {
				this.refresh();
			} catch (Throwable ex) {
				_logger.error("Error on service refresh", ex);
			}
		}
	}
	
	@Override
	public Map<Integer, MyPortalFrameConfig> getPageModelConfig(String code) {
		return this._configuration.get(code);
	}
	
	@Override
	public void updateModelConfig(String code, Map<Integer, MyPortalFrameConfig> configuration) throws ApsSystemException {
		try {
			this.getPageModelDAO().updateModelConfig(code, configuration);
			this._configuration.put(code, configuration);
		} catch (Throwable t) {
			_logger.error("Error updating page model configuration", t);
			throw new ApsSystemException("Error updating page model configuration", t);
		}
	}
	
	@Before("execution(* com.agiletec.aps.system.services.pagemodel.IPageModelManager.deletePageModel(..)) && args(code)")
    public void deleteConfiguration(String code) {
        try {
			this.getPageModelDAO().deleteModelConfiguration(code);
			this._configuration.remove(code);
		} catch (Throwable t) {
			_logger.error("Error deleting page model configuration", t);
			throw new RuntimeException("Error deleting page model configuration", t);
		}
    }
	
	private void loadPageModelConfigurations() throws ApsSystemException {
		try {
			this._configuration = this.getPageModelDAO().loadModelConfigs();
		} catch (Throwable t) {
			_logger.error("Error loading page model configurations", t);
			throw new ApsSystemException("Error loading page model configurations", t);
		}
	}
	
	protected IPageModelDAO getPageModelDAO() {
		return _pageModelDao;
	}
	public void setPageModelDAO(IPageModelDAO pageModelDAO) {
		this._pageModelDao = pageModelDAO;
	}
	
	private Map<String, Map<Integer, MyPortalFrameConfig>> _configuration;
	
	private IPageModelDAO _pageModelDao;
	
}
