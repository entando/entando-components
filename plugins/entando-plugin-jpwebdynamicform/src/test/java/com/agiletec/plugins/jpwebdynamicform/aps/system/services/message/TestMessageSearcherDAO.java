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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpwebdynamicform.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwebdynamicform.util.JpwebdynamicformTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageRecordVO;

public class TestMessageSearcherDAO extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testSearchRecords() throws Throwable {
		Date currentDate = new Date();
		Message message1 = this._helper.createMessage("TEST", "admin", "it", currentDate, "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageDao.addEntity(message1);
		
		List<ApsEntityRecord> records = this._messageSearcherDao.searchRecords(null);
		assertEquals(1, records.size());
		MessageRecordVO record = (MessageRecordVO) records.get(0);
		assertEquals(message1.getId(), record.getId());
		assertEquals(message1.getTypeCode(), record.getTypeCode());
		assertEquals(message1.getUsername(), record.getUsername());
		assertEquals(DateConverter.getFormattedDate(currentDate, "dd/MM/yyyy"), 
				DateConverter.getFormattedDate(record.getCreationDate(), "dd/MM/yyyy"));
		Date start = new Date(currentDate.getTime()-86400001);
		Date end = new Date(currentDate.getTime()+86400001);
		EntitySearchFilter filter = new EntitySearchFilter(IMessageManager.CREATION_DATE_FILTER_KEY, false, start, end);
		records = this._messageSearcherDao.searchRecords(new EntitySearchFilter[] { filter });
		assertEquals(1, records.size());
		
		end = new Date(currentDate.getTime()-86400001);
		filter = new EntitySearchFilter(IMessageManager.CREATION_DATE_FILTER_KEY, false, start, end);
		records = this._messageSearcherDao.searchRecords(new EntitySearchFilter[] { filter });
		assertEquals(0, records.size());
		
		filter = new EntitySearchFilter(IMessageManager.CREATION_DATE_FILTER_KEY, false, null, end);
		records = this._messageSearcherDao.searchRecords(new EntitySearchFilter[] { filter });
		assertEquals(0, records.size());
		
		filter = new EntitySearchFilter(IMessageManager.CREATION_DATE_FILTER_KEY, false, start, null);
		records = this._messageSearcherDao.searchRecords(new EntitySearchFilter[] { filter });
		assertEquals(1, records.size());
	}
	
	public void testSearchId() throws Throwable {
		Date currentDate = new Date();
		Message message1 = this._helper.createMessage("TEST1", "admin", "it", currentDate, "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageDao.addEntity(message1);
		
		Message message2 = this._helper.createMessage("TEST2", "mainEditor", "it", currentDate, "MyCompany", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageDao.addEntity(message2);
		
		List<String> messageIds = this._messageSearcherDao.searchId(null);
		assertEquals(2, messageIds.size());
		assertTrue(messageIds.contains("TEST1"));
		assertTrue(messageIds.contains("TEST2"));
		
		EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "PER", true);
		messageIds = this._messageSearcherDao.searchId(new EntitySearchFilter[] { filter });
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains("TEST1"));
		
		filter = new EntitySearchFilter(IMessageManager.USERNAME_FILTER_KEY, false, "mainEditor", true);
		messageIds = this._messageSearcherDao.searchId(new EntitySearchFilter[] { filter });
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains("TEST2"));
		
		messageIds = this._messageSearcherDao.searchId("COM", new EntitySearchFilter[] {});
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains("TEST2"));
	}
	
	public void testSearchIdAnswered() throws Throwable {
		Date currentDate = new Date();
		Message message1 = this._helper.createMessage("TEST1", "admin", "it", currentDate, "MyName", "MySurname", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageDao.addEntity(message1);
		String message1Id = message1.getId();
		Answer answer1 = this._helper.createAnswer(null, message1Id, "admin", new Date(), "text1");
		this._helper.addAnswer(answer1);
		
		Message message2 = this._helper.createMessage("TEST2", "mainEditor", "it", currentDate, "MyCompany", "MyAddress", JpwebdynamicformTestHelper.EMAIL, "MyNotes");
		this._messageDao.addEntity(message2);
		String message2Id = message2.getId();
		
		List<String> messageIds = this._messageSearcherDao.searchId(null, true);
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains(message1Id));
		
		messageIds = this._messageSearcherDao.searchId(null, false);
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains(message2Id));
		
		EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "PER", true);
		messageIds = this._messageSearcherDao.searchId(new EntitySearchFilter[] { filter }, true);
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains(message1Id));
		
		filter = new EntitySearchFilter(IMessageManager.USERNAME_FILTER_KEY, false, "mainEditor", true);
		messageIds = this._messageSearcherDao.searchId(new EntitySearchFilter[] { filter }, true);
		assertEquals(0, messageIds.size());
		
		filter = new EntitySearchFilter(IMessageManager.USERNAME_FILTER_KEY, false, "admin", true);
		messageIds = this._messageSearcherDao.searchId(new EntitySearchFilter[] { filter }, true);
		assertEquals(1, messageIds.size());
		assertTrue(messageIds.contains(message1Id));
	}
	
    private void init() throws Exception {
		try {
			MessageDAO messageDAO = new MessageDAO();
			messageDAO.setDataSource((DataSource) this.getApplicationContext().getBean("servDataSource"));
			messageDAO.setLangManager((ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER));
			this._messageDao = messageDAO;
			MessageSearcherDAO searcherDAO = new MessageSearcherDAO();
			searcherDAO.setDataSource((DataSource) this.getApplicationContext().getBean("servDataSource"));
			this._messageSearcherDao = searcherDAO;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private IMessageDAO _messageDao;
    private IMessageSearcherDAO _messageSearcherDao;
    
}