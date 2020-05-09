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
import com.agiletec.plugins.jpfacetnav.apsadmin.portal.specialwidget.FacetNavTreeWidgetAction;
import com.opensymphony.xwork2.Action;
import java.util.HashMap;
import java.util.Map;

public class TestFacetNavTreeWidgetAction extends ApsAdminPluginBaseTestCase {

	public void testInitConfig() throws Throwable {
		String result = this.executeConfigFacetNavTree("admin", "homepage", "1", "jpfacetnav_tree");
		assertEquals(Action.SUCCESS, result);
		FacetNavTreeWidgetAction action = (FacetNavTreeWidgetAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		assertEquals(0, widget.getConfig().size());
		List<SmallContentType> contentTypes = action.getContentTypes();
        contentTypes.stream().forEach(ct -> System.out.println(ct.getCode() + " - " + ct.getDescr()));
		assertNotNull(contentTypes);
		assertEquals(4, contentTypes.size());
	}
    
    public void testAddRemoveContentType() throws Throwable {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("pageCode", "homepage");
		parameters.put("frame", "1");
		parameters.put("widgetTypeCode", "jpfacetnav_tree");
        
        parameters.put("contentTypeCode", "EVN");
		String result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "joinContentType", parameters);
		assertEquals(Action.SUCCESS, result);
		FacetNavTreeWidgetAction action = (FacetNavTreeWidgetAction) this.getAction();
		List<SmallContentType> contentTypes = action.getContentTypes();
		assertNotNull(contentTypes);
		assertEquals(4, contentTypes.size());
        assertEquals("EVN", action.getContentTypesFilter());
        
        parameters.put("contentTypesFilter", "RAH,ART");
        parameters.put("contentTypeCode", "ALL");
		result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "joinContentType", parameters);
		assertEquals(Action.SUCCESS, result);
		action = (FacetNavTreeWidgetAction) this.getAction();
        assertEquals("RAH,ART,ALL", action.getContentTypesFilter());
        
        parameters.put("contentTypeCode", "EVN");
        parameters.put("contentTypesFilter", "RAH,EVN,ART,ALL");
        result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "removeContentType", parameters);
		assertEquals(Action.SUCCESS, result);
		action = (FacetNavTreeWidgetAction) this.getAction();
        assertEquals("RAH,ART,ALL", action.getContentTypesFilter());
	}
    
    public void testAddRemoveFacets() throws Throwable {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("pageCode", "homepage");
		parameters.put("frame", "1");
		parameters.put("widgetTypeCode", "jpfacetnav_tree");
        parameters.put("contentTypesFilter", "RAH,EVN,ALL");
        
        parameters.put("facetCode", "cat1");
		String result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "joinFacet", parameters);
		assertEquals(Action.SUCCESS, result);
		FacetNavTreeWidgetAction action = (FacetNavTreeWidgetAction) this.getAction();
		List<SmallContentType> contentTypes = action.getContentTypes();
		assertNotNull(contentTypes);
		assertEquals(4, contentTypes.size());
        assertEquals("cat1", action.getFacetRootNodes());
        assertEquals("RAH,EVN,ALL", action.getContentTypesFilter());
        
        parameters.put("facetCode", "general_cat2");
		parameters.put("facetRootNodes", "evento,general,cat1");
        parameters.put("contentTypesFilter", "RAH,ART,EVN");
		result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "joinFacet", parameters);
		assertEquals(Action.SUCCESS, result);
		action = (FacetNavTreeWidgetAction) this.getAction();
        assertEquals("RAH,ART,EVN", action.getContentTypesFilter());
        assertEquals("evento,general,cat1,general_cat2", action.getFacetRootNodes());
        
        parameters.put("facetCode", "xxxx");
		parameters.put("facetRootNodes", "evento,general,cat1");
		parameters.put("contentTypesFilter", "RAH,ART,EVN");
		result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "joinFacet", parameters);
		assertEquals(Action.SUCCESS, result);
		action = (FacetNavTreeWidgetAction) this.getAction();
        assertEquals("RAH,ART,EVN", action.getContentTypesFilter());
        assertEquals("evento,general,cat1", action.getFacetRootNodes());
        
        parameters.put("facetCode", "general");
		parameters.put("facetRootNodes", "evento,general,cat1");
		parameters.put("contentTypesFilter", "RAH,ART,EVN");
		result = this.executeAction("admin", "/do/jpfacetnav/Page/SpecialWidget/FacetNavTree", "removeFacet", parameters);
		assertEquals(Action.SUCCESS, result);
		action = (FacetNavTreeWidgetAction) this.getAction();
        assertEquals("RAH,ART,EVN", action.getContentTypesFilter());
        assertEquals("evento,cat1", action.getFacetRootNodes());
	}
    
	private String executeConfigFacetNavTree(String username, String pageCode, String frame, String widgetTypeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Page/SpecialWidget", "facetNavTreeConfig");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		if (null != widgetTypeCode && widgetTypeCode.trim().length()>0) {
			this.addParameter("widgetTypeCode", widgetTypeCode);
		}
		return this.executeAction();
	}
    
	private String executeAction(String username, String namespace, String actionName, Map<String, String> parameters) throws Throwable {
		this.setUserOnSession(username);
		this.initAction(namespace, actionName);
		this.addParameters(parameters);
		return this.executeAction();
	}
    
}