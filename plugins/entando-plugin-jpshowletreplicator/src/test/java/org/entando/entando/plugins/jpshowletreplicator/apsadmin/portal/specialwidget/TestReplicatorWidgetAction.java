/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
