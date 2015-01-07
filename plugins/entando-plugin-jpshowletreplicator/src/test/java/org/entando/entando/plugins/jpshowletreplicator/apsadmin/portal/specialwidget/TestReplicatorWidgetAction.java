/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpshowletreplicator.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import org.entando.entando.plugins.jpshowletreplicator.apsadmin.ApsAdminPluginBaseTestCase;

/**
 * @author E.Santoboni
 */
public class TestReplicatorWidgetAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testValidate_1() throws Throwable {
		String pageCode = "pagina_2";
		int frame = 0;
		IPage page = this._pageManager.getPage(pageCode);
		Widget widget = page.getWidgets()[frame];
		assertNull(widget);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jpshowletreplicator/Page/SpecialWidget/Replicator", "saveConfig");
			this.addParameter("pageCode", pageCode);
			this.addParameter("frame", String.valueOf(frame));
			this.addParameter("widgetTypeCode", "jpshowletreplicator");
			this.addParameter("pageCodeParam", "pagina_1");
			this.addParameter("frameIdParam", "");
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			ActionSupport action = super.getAction();
			assertTrue(action.hasFieldErrors());
		} catch (Throwable t) {
			page = this._pageManager.getPage(pageCode);
			page.getWidgets()[frame] = null;
			this._pageManager.updatePage(page);
			throw t;
		}
	}
	
	public void testSave() throws Throwable {
		String pageCode = "pagina_2";
		int frame = 0;
		IPage page = this._pageManager.getPage(pageCode);
		Widget widget = page.getWidgets()[frame];
		assertNull(widget);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jpshowletreplicator/Page/SpecialWidget/Replicator", "saveConfig");
			this.addParameter("pageCode", pageCode);
			this.addParameter("frame", String.valueOf(frame));
			this.addParameter("widgetTypeCode", "jpshowletreplicator");
			this.addParameter("pageCodeParam", "pagina_1");
			this.addParameter("frameIdParam", "2");
			String result = this.executeAction();
			assertEquals("configure", result);
			page = this._pageManager.getPage(pageCode);
			widget = page.getWidgets()[frame];
			assertNotNull(widget);
			assertEquals("jpshowletreplicator", widget.getType().getCode());
			assertEquals(2, widget.getConfig().size());
			assertEquals("pagina_1", widget.getConfig().getProperty("pageCodeParam"));
			assertEquals("2", widget.getConfig().getProperty("frameIdParam"));
		} catch (Throwable t) {
			throw t;
		} finally {
			page = this._pageManager.getPage(pageCode);
			page.getWidgets()[frame] = null;
			this._pageManager.updatePage(page);
		}
	}

	private void init() throws Exception {
    	try {
    		_pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IPageManager _pageManager = null;

}
