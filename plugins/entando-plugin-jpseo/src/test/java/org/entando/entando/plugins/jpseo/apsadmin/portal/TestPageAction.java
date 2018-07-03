/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.apsadmin.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpseo.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.apsadmin.portal.PageAction;
import com.opensymphony.xwork2.Action;
import org.entando.entando.plugins.jpseo.aps.system.services.page.PageMetatag;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;

public class TestPageAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testEditPage_1() throws Throwable {
		String selectedPageCode = "pagina_1";
		String result = this.executeActionOnPage(selectedPageCode, "admin", "edit", null);
		assertEquals(Action.SUCCESS, result);
		IPage page = this._pageManager.getDraftPage(selectedPageCode);
		PageAction action = (PageAction) this.getAction();
		assertEquals(action.getStrutsAction(), ApsAdminSystemConstants.EDIT);
		assertEquals(page.getCode(), action.getPageCode());
		assertEquals(page.getParentCode(), action.getParentPageCode());
		assertEquals(page.getModel().getCode(), action.getModel());
		assertEquals(page.getGroup(), action.getGroup());
		assertEquals(page.isShowable(), action.isShowable());
		assertEquals("Pagina 1", action.getTitles().getProperty("it"));
		assertEquals("Page 1", action.getTitles().getProperty("en"));
	}

	public void testEditPage_2() throws Throwable {
		String selectedPageCode = "seo_page_1";
		String result = this.executeActionOnPage(selectedPageCode, "admin", "edit", null);
		assertEquals(Action.SUCCESS, result);
		IPage page = this._pageManager.getDraftPage(selectedPageCode);
		PageAction action = (PageAction) this.getAction();
		assertEquals(action.getStrutsAction(), ApsAdminSystemConstants.EDIT);
		assertEquals(page.getCode(), action.getPageCode());
		assertEquals(page.getParentCode(), action.getParentPageCode());
		assertEquals(page.getModel().getCode(), action.getModel());
		assertEquals(page.getGroup(), action.getGroup());
		assertEquals(page.isShowable(), action.isShowable());
		assertEquals("Seo Page 1", action.getTitles().getProperty("en"));
		assertEquals("Pagina Seo 1", action.getTitles().getProperty("it"));
        
        Map<String, Map<String, PageMetatag>> metas = (Map<String, Map<String, PageMetatag>>) this.getRequest().getAttribute(PageActionAspect.PARAM_METATAGS);
        assertNotNull(metas);
        assertEquals(3, metas.size());
        Map<String, PageMetatag> engMetas = metas.get("en");
        assertNotNull(engMetas);
        assertEquals(6, engMetas.size());
        assertNull(engMetas.get("key2").getValue());
        assertEquals("VALUE_5 EN", engMetas.get("key5").getValue());
        
        String descriptionIt = (String) this.getRequest().getAttribute(PageActionAspect.PARAM_DESCRIPTION_PREFIX + "it");
        assertEquals("Descrizione IT SeoPage 1", descriptionIt);
        Boolean useDefaultDescrIt = (Boolean) this.getRequest().getAttribute(PageActionAspect.PARAM_DESCRIPTION_USE_DEFAULT_PREFIX + "it");
        assertFalse(useDefaultDescrIt);
	}

	public void testJoinGroupPageForAdminUser() throws Throwable {
		String extraGroup = Group.ADMINS_GROUP_NAME;
		String selectedPageCode = "pagina_1";
		Map<String, String> params = new HashMap<>();
		params.put("extraGroupNameToAdd", extraGroup);

		//add extra group
		String result = this.executeActionOnPage(selectedPageCode, "admin", "joinExtraGroup", params);
		assertEquals(Action.SUCCESS, result);
		PageAction action = (PageAction) this.getAction();
		boolean hasExtraGroupAdministrators = action.getExtraGroups().contains(extraGroup);
		assertTrue(hasExtraGroupAdministrators);

		//remove extra group
        params.put("extraGroupNameToRemove", extraGroup);
		result = this.executeActionOnPage(selectedPageCode, "admin", "removeExtraGroup", params);
		assertEquals(Action.SUCCESS, result);
		action = (PageAction) this.getAction();
		hasExtraGroupAdministrators = action.getExtraGroups().contains(extraGroup);
		assertFalse(hasExtraGroupAdministrators);
	}

	private String executeActionOnPage(String selectedPageCode, String username, String actionName, Map<String, String> params) throws Throwable {
		if (StringUtils.isNotBlank(username)) {
		}
		this.setUserOnSession(username);
		this.initAction("/do/Page", actionName);
		this.addParameter("selectedNode", selectedPageCode);
		if (null != params && !params.isEmpty()) {
			this.addParameters(params);
		}
		String result = this.executeAction();
		return result;
	}

	public void testValidateSavePage() throws Throwable {
		String pageCode = "pagina_test";
		String longPageCode = "very_long_page_code__very_long_page_code";
		assertNull(this._pageManager.getDraftPage(pageCode));
		assertNull(this._pageManager.getDraftPage(longPageCode));
		try {
			IPage root = this._pageManager.getOnlineRoot();
			Map<String, String> params = new HashMap<>();
			params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			params.put("parentPageCode", root.getCode());
			String result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(4, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("model"));
			assertTrue(fieldErrors.containsKey("group"));
			assertTrue(fieldErrors.containsKey("langit"));
			assertTrue(fieldErrors.containsKey("langen"));

			params.put("langit", "Pagina Test");
			params.put("model", "home");
			result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("group"));
			assertTrue(fieldErrors.containsKey("langen"));

			assertNotNull(this._pageManager.getOnlinePage("pagina_1"));
			params.put("langen", "Test Page");
			params.put("group", Group.FREE_GROUP_NAME);
			params.put("pageCode", "pagina_1");//page already present
			result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("pageCode"));

			params.put("pageCode", longPageCode);
			result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("pageCode"));
		} catch (Throwable t) {
			this._pageManager.deletePage(pageCode);
			this._pageManager.deletePage(longPageCode);
			throw t;
		}
	}

	public void testSavePage_1() throws Throwable {
		String pageCode = "seo_test_1";
		assertNull(this._pageManager.getDraftPage(pageCode));
		try {
			Map<String, String> params = this.createParamForTest(pageCode);
			params.put(PageActionAspect.PARAM_FRIENDLY_CODE, "friendly_code_test_1");
            params.put("description_lang_en", "Seo Description Lang EN");
			params.put("description_lang_it", "Descrizione SEO per LINGUA IT");
            params.put("description_useDefaultLang_en", "true");
			params.put("description_useDefaultLang_it", "false");
			String result = this.executeSave(params, "admin");
			assertEquals(Action.SUCCESS, result);
			IPage addedPage = this._pageManager.getDraftPage(pageCode);
			assertNotNull(addedPage);
			assertEquals("Pagina Test 1", addedPage.getTitles().getProperty("it"));
			assertTrue(addedPage.getMetadata() instanceof SeoPageMetadata);
			SeoPageMetadata addedSeoPage = (SeoPageMetadata) addedPage.getMetadata();
			assertEquals("friendly_code_test_1", addedSeoPage.getFriendlyCode());
            
            ApsProperties titles = addedSeoPage.getDescriptions();
            assertNotNull(titles);
            assertEquals(2, titles.size());
            assertEquals("Descrizione SEO per LINGUA IT", ((PageMetatag) titles.get("it")).getValue());
            assertEquals("Seo Description Lang EN", ((PageMetatag) titles.get("en")).getValue());
            assertFalse(((PageMetatag) titles.get("it")).isUseDefaultLangValue());
            assertTrue(((PageMetatag) titles.get("en")).isUseDefaultLangValue());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageManager.deletePage(pageCode);
		}
	}

	public void testSavePage_2() throws Throwable {
		String pageCode = "seo_test_2";
		assertNull(this._pageManager.getDraftPage(pageCode));
		try {
			Map<String, String> params = this.createParamForTest(pageCode);
			params.put(PageActionAspect.PARAM_FRIENDLY_CODE, "friendly_code_test_2");
            
            params.put("pageMetataKey_it_0", "metaKey_0");
            params.put("pageMetataAttribute_it_0", "name");
            params.put("pageMetataValue_it_0", "meta value IT 0");
            
            params.put("pageMetataKey_en_0", "metaKey_0");
            
            params.put("pageMetataKey_it_1", "metaKey_1");
            params.put("pageMetataAttribute_it_1", "name");
            params.put("pageMetataValue_it_1", "meta value IT 1");
            
            params.put("pageMetataKey_en_1", "metaKey_1");
			params.put("pageMetataAttribute_en_1", "property");
            params.put("pageMetataValue_en_1", "meta value EN 1");
            
			String result = this.executeSave(params, "admin");
			assertEquals(Action.SUCCESS, result);
			IPage addedPage = this._pageManager.getDraftPage(pageCode);
			assertNotNull(addedPage);
			assertEquals("Test Page 1", addedPage.getTitles().getProperty("en"));
			assertTrue(addedPage.getMetadata() instanceof SeoPageMetadata);
			SeoPageMetadata addedSeoPage = (SeoPageMetadata) addedPage.getMetadata();
			assertEquals("friendly_code_test_2", addedSeoPage.getFriendlyCode());
            Map<String, Map<String, PageMetatag>> extraParams = addedSeoPage.getComplexParameters();
            assertEquals(2, extraParams.size());
            assertEquals(2, extraParams.get("it").size());
            assertEquals(2, extraParams.get("en").size());
            PageMetatag metaIt0 = extraParams.get("it").get("metaKey_0");
            assertNotNull(metaIt0);
            assertEquals("meta value IT 0", metaIt0.getValue());
            assertEquals("name", metaIt0.getKeyAttribute());
            assertFalse(metaIt0.isUseDefaultLangValue());
            
            PageMetatag metaEn1 = extraParams.get("en").get("metaKey_1");
            assertNotNull(metaEn1);
            assertEquals("meta value EN 1", metaEn1.getValue());
            assertEquals("property", metaEn1.getKeyAttribute());
            assertFalse(metaEn1.isUseDefaultLangValue());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageManager.deletePage(pageCode);
		}
	}
    
    private Map<String, String> createParamForTest(String pageCode) {
        IPage root = this._pageManager.getDraftRoot();
        Map<String, String> params = new HashMap<>();
        params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        params.put("parentPageCode", root.getCode());
        params.put("langit", "Pagina Test 1");
        params.put("langen", "Test Page 1");
        params.put("model", "home");
        params.put("group", Group.FREE_GROUP_NAME);
        params.put("pageCode", pageCode);
        return params;
    }
    
	private String executeSave(Map<String, String> params, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Page", "save");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

	private void init() throws Exception {
		try {
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IPageManager _pageManager = null;

}
