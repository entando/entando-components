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
package com.agiletec.plugins.jpcrowdsourcing.aps.internalservlet.idea;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.util.TokenHelper;

import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.apsadmin.ApsAdminPluginBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class TestIdeaFrontAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testIntro() throws Throwable {
		String result = this.executeIntro("admin");
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testIntroGuest() throws Throwable {
		String result = this.executeIntro(null);
		assertEquals("userNotAllowed", result);
	}
	
	public void testSaveWithErrors() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("idea.title", "");
		params.put("idea.descr", "");
		params.put("idea.username", "admin");
		params.put("idea.instanceCode", INSTANCE);
		
		String result = this.executeSave("admin", params);
		assertEquals(Action.INPUT, result);
		
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("idea.title"));
		assertTrue(fieldErrors.containsKey("idea.descr"));
	}

	public void testSaveGuest() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("idea.title", "test_title_" + this.getClass().getSimpleName());
		params.put("idea.descr", "test_descr_" + this.getClass().getSimpleName());
		//params.put("idea.username", "");
		
		String result = this.executeSave(null, params);
		assertEquals("userNotAllowed", result);
	}
	
	public void testSaveOk() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("idea.title", "test_title_" + this.getClass().getSimpleName());
		params.put("idea.descr", "test_descr_" + this.getClass().getSimpleName());
		params.put("idea.username", "admin");
		params.put("tags", "tags");
		params.put("idea.instanceCode", "default");
		
		List<String> ideaInstances =  this._ideaInstanceManager.getIdeaInstances();
		
		String result = this.executeSave("admin", params);
		assertEquals(Action.SUCCESS, result);
		
		List<String> ideaList = this._ideaManager.searchIdeas(INSTANCE,null, this.getClass().getSimpleName(), null, null);
		assertEquals(1, ideaList.size());
	}
	
	private String executeSave(String username, Map<String, String> params) throws Throwable {
		if (null != username) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "save");
		this.addParameters(params);
		this.setToken();
		return this.executeAction();
	}
	
	private String executeIntro(String username) throws Throwable {
		if (null != username) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "intro");
		return this.executeAction();
	}
	
	protected void setToken() {
		String token = TokenHelper.generateGUID();
		ActionContext.getContext().setSession(new HashMap<String, Object>());
		String tokenName = "test_tokenName";
		String tokenSessionAttributeName = TokenHelper.buildTokenSessionAttributeName(tokenName);
		ActionContext.getContext().getSession().put(tokenSessionAttributeName, token);
		this.addParameter(TokenHelper.TOKEN_NAME_FIELD, new String[]{tokenName});
		this.addParameter(tokenName, new String[]{token});
	}
	
	@Override
	protected void tearDown() throws Exception {
		List<String> list = this._ideaManager.searchIdeas(INSTANCE,null, "test_", null, null);
		if (null != list) {
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				String ideaId = it.next();
				this._ideaManager.deleteIdea(ideaId);
				System.out.println("Eliminata idea di test " + ideaId);
			}
		}
		super.tearDown();
	}

	private void init() {
		this._ideaManager = (IIdeaManager) this.getService(JpCrowdSourcingSystemConstants.IDEA_MANAGER);
		this._ideaInstanceManager = (IIdeaInstanceManager) this.getService(JpCrowdSourcingSystemConstants.IDEA_INSTANCE_MANAGER);
	}

	private String NS = "/do/collaboration/FrontEnd/Idea/NewIdea";
	private IIdeaManager _ideaManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private String INSTANCE = "default";
	
}

