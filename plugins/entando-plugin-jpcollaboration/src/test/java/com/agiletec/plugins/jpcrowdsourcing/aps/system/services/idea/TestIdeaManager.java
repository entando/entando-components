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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea;

import java.util.Iterator;
import java.util.List;

import com.agiletec.plugins.jpcrowdsourcing.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.JpcrowdsourcingTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.CrowdSourcingConfig;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IdeaManager;

public class TestIdeaManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	/*
	Settare nel file WEB-INF/plugins/jpcrowdsourcing/aps/conf/managers/jpcrowdsourcingManagersConfig.xml
	la property categoryRoot nel bean jpcrowdsourcingIdeaManager
	public void testGetCategoryRoot_WithXmlConfig() throws Throwable {
		String catCode = this._ideaManager.getCategoryRoot();
		assertNotNull(catCode);
		assertEquals("home", catCode);
		Category cat = this._categoryManager.getCategory(catCode);
		assertEquals("Home", cat.getTitle());
	}
	 */
	
	/*
	 * Assicurarsi che nel file WEB-INF/plugins/jpcrowdsourcing/aps/conf/managers/jpcrowdsourcingManagersConfig.xml
	 * 	la property categoryRoot nel bean jpcrowdsourcingIdeaManager NON sia presente
	 */
	public void testGetCategoryRoot() throws Throwable {
		String catCode = this._ideaManager.getCategoryRoot();
		assertNotNull(catCode);
		assertEquals(IdeaManager.DEFAULT_CATEGORY_ROOT, catCode);
		Category cat = this._categoryManager.getCategory(catCode);
		assertNotNull(cat);
		assertEquals("Crowd Sourcing Root", cat.getTitle());
	}
	
	public void testGetConfig() {
		CrowdSourcingConfig config = this._ideaManager.getConfig();
		assertNotNull(config);
		assertTrue(config.isModerateEntries());
	}
	
	public void testLoadIdea() throws Throwable {
		Idea idea = (Idea) this._ideaManager.getIdea("1");
		assertNotNull(idea);
		///1374235;"1";"idea di prova";"facciamo una prova?";"2011-03-08 10:11:12.13";"s.puddu";2;0;0
		assertEquals("idea di prova", idea.getTitle());
		assertEquals("facciamo una prova?", idea.getDescr());
		assertEquals("2011-03-08 10:11:12", DateConverter.getFormattedDate(idea.getPubDate(), "yyyy-MM-dd HH:mm:ss"));
		assertEquals("s.puddu", idea.getUsername());
		assertEquals(3, idea.getVotePositive());
		assertEquals(5, idea.getVoteNegative());
		List<String> tags = idea.getTags();
		assertNotNull(tags);
		assertEquals(1, tags.size());
		assertTrue(tags.get(0).equals("home"));
		assertEquals(3, idea.getComments().get(IIdea.STATUS_TO_APPROVE).size());
	}
	
	public void testAddDeleteSimple() throws Throwable {
		String title = "test_title";
		String descr = "test_descr";
		String username = "test_bot";
		Idea testIdea = this._testHelper.getIdea(INSTANCE, title, descr, username);
		this._ideaManager.addIdea(testIdea);
		
		Idea idea = (Idea) this._ideaManager.getIdea(testIdea.getId());
		assertNotNull(idea);
		assertEquals(title, idea.getTitle());
		assertEquals(descr, idea.getDescr());
		assertEquals(testIdea.getPubDate(), idea.getPubDate());
		assertEquals(username, idea.getUsername());
		assertEquals(0, idea.getVotePositive());
		assertEquals(0, idea.getVoteNegative());
		assertTrue(idea.getComments().isEmpty());
		assertTrue(idea.getTags().isEmpty());
		assertEquals(IIdea.STATUS_TO_APPROVE, idea.getStatus()); //depends on CrowdSourcingConfig settings
		
		this._ideaManager.deleteIdea(idea.getId());
		assertNull(this._ideaManager.getIdea(idea.getId()));
	}

	public void testAddUpdateDelete() throws Throwable {
		String title = "test_title";
		String descr = "test_descr";
		String username = "test_bot";
		Idea testIdea = this._testHelper.getIdea(INSTANCE, title, descr, username);
		this._ideaManager.addIdea(testIdea);
		
		
		Idea insertedIdea = (Idea) this._ideaManager.getIdea(testIdea.getId());
		insertedIdea.getTags().add("general_cat1");
		insertedIdea.getTags().add("general_cat2");
		insertedIdea.setTitle(insertedIdea.getTitle() + "_mod");
		insertedIdea.setDescr(insertedIdea.getDescr() + "_mod");
		insertedIdea.setStatus(IIdea.STATUS_APPROVED);
		insertedIdea.setVoteNegative(12);
		insertedIdea.setVotePositive(13);
		
		////title=?, descr=?,  status=?, votepositive=?, votenegative=?
		this._ideaManager.updateIdea(insertedIdea);
		Idea idea = (Idea) this._ideaManager.getIdea(insertedIdea.getId());
		assertNotNull(idea);
		assertEquals(insertedIdea.getTitle(), idea.getTitle());
		assertEquals(insertedIdea.getDescr(), idea.getDescr());
		assertEquals(insertedIdea.getPubDate(), idea.getPubDate());
		assertEquals(username, idea.getUsername());
		assertEquals(13, idea.getVotePositive());
		assertEquals(12, idea.getVoteNegative());
		assertTrue(idea.getComments().isEmpty());
		assertEquals(IIdea.STATUS_APPROVED, idea.getStatus()); 
		
		List<String> tags = idea.getTags();
		assertNotNull(tags);
		assertEquals(2, tags.size());
		assertTrue(tags.get(0).equals("general_cat1"));
		assertTrue(tags.get(1).equals("general_cat2"));

		assertEquals(1, ((CategoryUtilizer)this._ideaManager).getCategoryUtilizers("general_cat1").size());
		
		this._ideaManager.deleteIdea(insertedIdea.getId());
		assertNull(this._ideaManager.getIdea(insertedIdea.getId()));
	}
	
	public void testSearch() throws Throwable {
		List<String> list = this._ideaManager.searchIdeas(INSTANCE, null, null, null, null);
		assertEquals(1, list.size());
	}

	public void testSearch_2() throws Throwable {
		String title = "test_title";
		String descr = "test_descr";
		String username = "test_bot";
		Idea testIdea = this._testHelper.getIdea(INSTANCE, title, descr, username);
		this._ideaManager.addIdea(testIdea);
		
		Idea insertedIdea = (Idea) this._ideaManager.getIdea(testIdea.getId());
		insertedIdea.getTags().add("general_cat1");
		insertedIdea.getTags().add("general_cat2");
		insertedIdea.setTitle(insertedIdea.getTitle() + "_mod");
		insertedIdea.setDescr(insertedIdea.getDescr() + "_mod");
		insertedIdea.setStatus(IIdea.STATUS_APPROVED);
		insertedIdea.setVoteNegative(12);
		insertedIdea.setVotePositive(13);
		this._ideaManager.updateIdea(insertedIdea);
		
		List<String> list = this._ideaManager.searchIdeas(INSTANCE, IIdea.STATUS_APPROVED, null, null, null);
		assertEquals(1, list.size());
		assertEquals(insertedIdea.getId(), list.get(0));

		list = this._ideaManager.searchIdeas(INSTANCE, IIdea.STATUS_APPROVED, "_titl", null, null);
		assertEquals(1, list.size());
		assertEquals(insertedIdea.getId(), list.get(0));

		list = this._ideaManager.searchIdeas(INSTANCE, IIdea.STATUS_TO_APPROVE, "_titl", null, null);
		assertEquals(0, list.size());

		list = this._ideaManager.searchIdeas(INSTANCE, IIdea.STATUS_TO_APPROVE, null, null, null);
		assertEquals(1, list.size());
		assertEquals("1", list.get(0));
		
		this._ideaManager.deleteIdea(insertedIdea.getId());
		assertNull(this._ideaManager.getIdea(insertedIdea.getId()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		List<String> list = this._ideaManager.searchIdeas(INSTANCE, null, "test_", null, null);
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
		this._categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
		this._testHelper = new JpcrowdsourcingTestHelper();
	}
	
	private IIdeaManager _ideaManager;
	private ICategoryManager _categoryManager;
	private JpcrowdsourcingTestHelper _testHelper;
	private String INSTANCE = "default";
}
