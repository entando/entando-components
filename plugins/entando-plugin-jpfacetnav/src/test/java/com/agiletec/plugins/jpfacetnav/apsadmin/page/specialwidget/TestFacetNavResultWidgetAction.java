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
package com.agiletec.plugins.jpfacetnav.apsadmin.page.specialwidget;

import java.util.List;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpfacetnav.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget.FacetNavResultWidgetAction;
import com.opensymphony.xwork2.Action;

public class TestFacetNavResultWidgetAction extends ApsAdminPluginBaseTestCase {

	public void testInitConfig_1() throws Throwable {
		String result = this.executeConfigFacetNavResult("admin", "homepage", "1", "jpfacetnav_results");
		assertEquals(Action.SUCCESS, result);
		FacetNavResultWidgetAction action = (FacetNavResultWidgetAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		assertEquals(0, widget.getConfig().size());
		List<SmallContentType> contentTypes = action.getContentTypes();
		assertNotNull(contentTypes);
		assertEquals(4, contentTypes.size());
	}

	private String executeConfigFacetNavResult(String username, String pageCode, String frame, String widgetTypeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Page/SpecialWidget", "facetNavResultConfig");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		if (null != widgetTypeCode && widgetTypeCode.trim().length()>0) {
			this.addParameter("widgetTypeCode", widgetTypeCode);
		}
		return this.executeAction();
	}

}