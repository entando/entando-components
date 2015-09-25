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
package org.entando.entando.plugins.jpmyportalplus.apsadmin.portal.model;

import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.HashMap;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.entando.entando.apsadmin.portal.model.AbstractPageModelAction;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.IMyPortalPageModelManager;
import org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalFrameConfig;

/**
 * @author E.Santoboni
 */
public class PageModelConfigAction extends AbstractPageModelAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageModelConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
	}
	
	public String edit() {
		PageModel pageModel = super.getPageModel(this.getCode());
		if (null == pageModel) {
			this.addActionError(this.getText("error.pageModel.notExist"));
			return "pageModelList";
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			Map<Integer, MyPortalFrameConfig> myPortalConfiguration = new HashMap<Integer, MyPortalFrameConfig>();
			PageModel pageModel = super.getPageModel(this.getCode());
			if (null == pageModel) {
				this.addActionError(this.getText("error.pageModel.notExist"));
				return "pageModelList";
			}
			Frame[] configuration = pageModel.getConfiguration();
			if (null != configuration) {
				for (int i = 0; i < configuration.length; i++) {
					Frame frame = configuration[i];
					String isFreeString = this.getRequest().getParameter("freePosition_" + frame.getPos());
					Integer column = null;
					try {
						column = Integer.parseInt(this.getRequest().getParameter("columnPosition_" + frame.getPos()));
					} catch (Throwable t) {}
					MyPortalFrameConfig config = new MyPortalFrameConfig();
					config.setColumn(column);
					config.setLocked(null == isFreeString);
					myPortalConfiguration.put(frame.getPos(), config);
				}
			}
			this.getMyPortalPageModelManager().updateModelConfig(pageModel.getCode(), myPortalConfiguration);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Map<Integer, MyPortalFrameConfig> getConfiguration(String code) {
		return this.getMyPortalPageModelManager().getPageModelConfig(code);
	}
	
	@Override
	public PageModel getPageModel(String code) {
		return super.getPageModel(code);
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	protected IMyPortalPageModelManager getMyPortalPageModelManager() {
		return _myPortalPageModelManager;
	}
	public void setMyPortalPageModelManager(IMyPortalPageModelManager myPortalPageModelManager) {
		this._myPortalPageModelManager = myPortalPageModelManager;
	}
	
	private String _code;
	
	private IMyPortalPageModelManager _myPortalPageModelManager;
	
}