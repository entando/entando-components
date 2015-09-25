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
package org.entando.entando.plugins.jpwebform.apsadmin;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.SelectItem;

/**
 *
 * @author S.Loru
 */
public class AbstractPreviewAction extends AbstractConfigAction{

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractPreviewAction.class);

	public List<SelectItem> getShowingPageSelectItems() {
		List<SelectItem> pageItems = new ArrayList<SelectItem>();
		try {
			IPageManager pageManager = this.getPageManager();
			List<IPage> pages = pageManager.getWidgetUtilizers("jpwebform_form");
			for (int i = 0; i < pages.size(); i++) {
				IPage page = pages.get(i);
				String pageCode = page.getCode();
				pageItems.add(new SelectItem(pageCode, super.getTitle(pageCode, page.getTitles())));
			}
		} catch (Throwable t) {
			_logger.error("Error on extracting showing pages", t);
			throw new RuntimeException("Error on extracting showing pages", t);
		}
		return pageItems;
	}

	public boolean isBuiltEveryStepGui(){
		return this.getFormManager().checkStepGui(this.getEntityTypeCode());
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private IPageManager _pageManager;



}
