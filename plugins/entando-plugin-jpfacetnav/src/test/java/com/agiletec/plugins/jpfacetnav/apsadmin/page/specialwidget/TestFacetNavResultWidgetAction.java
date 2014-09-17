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