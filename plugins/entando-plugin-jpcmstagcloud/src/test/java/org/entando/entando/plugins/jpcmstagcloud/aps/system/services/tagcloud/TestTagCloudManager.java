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
package org.entando.entando.plugins.jpcmstagcloud.aps.system.services.tagcloud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.sql.DataSource;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcmstagcloud.aps.system.JpcmstagcloudSystemConstants;
import com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud.ITagCloudManager;
import org.entando.entando.plugins.jpcmstagcloud.aps.ApsPluginBaseTestCase;

/**
 * @author E.Santoboni
 */
public class TestTagCloudManager extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.addFakeContents();
	}

	@Override
	protected void tearDown() throws Exception {
		this.removeFakeContents();
		super.tearDown();
	}

	public void testLoadPublicTaggedContentsId() throws Throwable {
		UserDetails user = this.getUser("admin");
		List<String> contentIds = this._tagCloudManager.loadPublicTaggedContentsId("cat1", user);
		compareIds(new String[]{"ART180"}, contentIds);

		contentIds = this._tagCloudManager.loadPublicTaggedContentsId("evento", user);
		compareIds(new String[]{"EVN192", "EVN193", "RAH101b", "ART120b"}, contentIds);

		user = this.getUser("editorCoach");
		contentIds = this._tagCloudManager.loadPublicTaggedContentsId("evento", user);
		compareIds(new String[]{"EVN192", "EVN193", "RAH101b"}, contentIds);
	}

	public void testGetCloudInfos() throws Throwable {
		UserDetails user = this.getUser("admin");
		Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put("cat1", new Integer(1));
		expected.put("evento", new Integer(4));
		expected.put("general", new Integer(5));

		Map<ITreeNode, Integer> cloudInfos = this._tagCloudManager.getCloudInfos(user);
		this.compareCloudInfos(expected, cloudInfos);

		user = this.getUser("editorCoach");
		expected.put("cat1", new Integer(1));
		expected.put("evento", new Integer(3));
		expected.put("general", new Integer(3));
		cloudInfos = this._tagCloudManager.getCloudInfos(user);
		this.compareCloudInfos(expected, cloudInfos);

		user = this._userManager.getGuestUser();
		expected.put("cat1", new Integer(1));
		expected.put("evento", new Integer(2));
		expected.remove("general");
		cloudInfos = this._tagCloudManager.getCloudInfos(user);
		this.compareCloudInfos(expected, cloudInfos);
	}

	private void compareIds(String[] expected, List<String> received) {
		assertEquals(expected.length, received.size());
		for (int i = 0; i < expected.length; i++) {
			String id = expected[i];
			if (!received.contains(id)) {
				fail("Expected " + id + " - not found in " + received.toString());
			}
		}
	}

	private void compareCloudInfos(Map<String, Integer> expected, Map<ITreeNode, Integer> received) {
		Iterator<ITreeNode> iter = received.keySet().iterator();
		while (iter.hasNext()) {
			ITreeNode node = iter.next();
			String key = node.getCode();
			Integer expectedSize = expected.get(key);
			if (expectedSize != null) {
				assertEquals(expectedSize.intValue(), received.get(node).intValue());
			}
		}
	}

	private void addFakeContents() throws ApsSystemException {
		Content masterContent = this._contentManager.loadContent("EVN193", true);

		Content content1 = this._contentManager.loadContent("RAH101", true);
		content1.setId("RAH101b");
		for (Category category : masterContent.getCategories()) {
			content1.addCategory(category);
		}
		this._contentDao.addEntity(content1);
		this._contentDao.insertOnLineContent(content1);

		Content content2 = this._contentManager.loadContent("ART120", true);
		content2.setId("ART120b");
		for (int i = 0; i < masterContent.getCategories().size(); i++) {
			Category category = masterContent.getCategories().get(i);
			content2.addCategory(category);
		}
		this._contentDao.addEntity(content2);
		this._contentDao.insertOnLineContent(content2);
	}

	private void removeFakeContents() {
		this._contentDao.deleteEntity("RAH101b");
		this._contentDao.deleteEntity("ART120b");
	}

	private void init() throws Exception {
		try {
			this._tagCloudManager = (ITagCloudManager) this.getService(JpcmstagcloudSystemConstants.TAG_CLOUD_MANAGER);
			this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
			this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			ContentDAO contentDao = new ContentDAO();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			contentDao.setDataSource(dataSource);
			ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			contentDao.setLangManager(langManager);
			this._contentDao = contentDao;

			ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
			configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, TEST_CONFIG);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	private ITagCloudManager _tagCloudManager;
	private IContentDAO _contentDao;
	private IContentManager _contentManager;
	private IUserManager _userManager;
	private String TEST_CONFIG = "<Params>"
			+ "<Param name=\"urlStyle\">classic</Param>"
			+ "<Param name=\"hypertextEditor\">fckeditor</Param>"
			+ "<Param name=\"treeStyle_page\">classic</Param>"
			+ "<Param name=\"treeStyle_category\">classic</Param>"
			+ "<Param name=\"startLangFromBrowser\">false</Param>"
			+ "<SpecialPages>"
			+ "<Param name=\"notFoundPageCode\">notfound</Param>"
			+ "<Param name=\"homePageCode\">homepage</Param>"
			+ "<Param name=\"errorPageCode\">errorpage</Param>"
			+ "<Param name=\"loginPageCode\">login</Param>"
			+ "</SpecialPages>"
			+ "<ExtendendPrivacyModule>"
			+ "<Param name=\"extendedPrivacyModuleEnabled\">false</Param>"
			+ "<Param name=\"maxMonthsSinceLastAccess\">6</Param>"
			+ "<Param name=\"maxMonthsSinceLastPasswordChange\">3</Param>        "
			+ "</ExtendendPrivacyModule>"
			+ "<ExtraParams>"
			+ "<Param name=\"jpcmstagcloud_delayDays\" >20000</Param>"
			+ "<Param name=\"jpcmstagcloud_categoryRoot\" >home</Param>"
			+ "</ExtraParams>"
			+ "</Params>";
}