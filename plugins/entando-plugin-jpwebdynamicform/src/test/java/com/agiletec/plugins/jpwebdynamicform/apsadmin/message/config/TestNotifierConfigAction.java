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
package com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwebdynamicform.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageModel;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageTypeNotifierConfig;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config.INotifierConfigAction;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config.NotifierConfigAction;
import com.opensymphony.xwork2.Action;

public class TestNotifierConfigAction extends ApsAdminPluginBaseTestCase {

	public void testList() throws Throwable {
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		List<SmallMessageType> messageTypes = ((INotifierConfigAction) this.getAction()).getMessageTypes();
		assertEquals(2, messageTypes.size());
	}

	public void testEdit() throws Throwable {
		String result = this.executeEdit("admin", "PER");
		assertEquals(Action.SUCCESS, result);
		NotifierConfigAction action = (NotifierConfigAction) this.getAction();
		assertEquals("PER", action.getTypeCode());
		assertTrue(action.getStore().booleanValue());
		assertEquals("eMail", action.getMailAttrName());
		assertTrue(action.getNotifiable().booleanValue());
		assertEquals("CODE1", action.getSenderCode());
		assertEquals(2, action.getRecipientsTo().size());
		assertEquals(1, action.getRecipientsCc().size());
		assertEquals(1, action.getRecipientsBcc().size());
		assertEquals("Corpo della mail PER", action.getBodyModel());
		assertEquals("Oggetto della mail PER", action.getSubjectModel());

		result = this.executeEdit("admin", "COM");
		assertEquals(Action.SUCCESS, result);
		action = (NotifierConfigAction) this.getAction();
		assertEquals("COM", action.getTypeCode());
		assertFalse(action.getStore().booleanValue());
		assertEquals("eMail", action.getMailAttrName());
		assertTrue(action.getNotifiable().booleanValue());
		assertEquals("CODE2", action.getSenderCode());
		assertEquals(3, action.getRecipientsTo().size());
		assertEquals(2, action.getRecipientsCc().size());
		assertEquals(1, action.getRecipientsBcc().size());
		assertEquals("Corpo della mail COM", action.getBodyModel());
		assertEquals("Oggetto della mail COM", action.getSubjectModel());
	}

	public void testAddRemoveAddressFailure() throws Throwable {
		String[] recipientsTo = new String[] { "address1@dominioinesistente.it", "address2@dominioinesistente.it" };

		String result = this.executeAddAddress("admin", "PER", recipientsTo, null, null, INotifierConfigAction.RECIPIENT_TO, "address");
		assertEquals(Action.INPUT, result);
		List<String> addressErrors = this.getAction().getFieldErrors().get("address");
		assertEquals(1, addressErrors.size());
		assertEquals(this.getAction().getText("Errors.address.notValid"), addressErrors.get(0));
		this.checkStrings(recipientsTo, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeAddAddress("admin", "PER", recipientsTo, null, null, INotifierConfigAction.RECIPIENT_TO, "");
		assertEquals(Action.INPUT, result);
		addressErrors = this.getAction().getFieldErrors().get("address");
		assertEquals(1, addressErrors.size());
		assertEquals(this.getAction().getText("Errors.address.required"), addressErrors.get(0));
		this.checkStrings(recipientsTo, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeRemoveAddress("admin", "PER", recipientsTo, null, null, INotifierConfigAction.RECIPIENT_TO, "address");
		assertEquals(Action.SUCCESS, result);
		addressErrors = this.getAction().getFieldErrors().get("address");
		assertNull(addressErrors);
		this.checkStrings(recipientsTo, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeRemoveAddress("admin", "PER", recipientsTo, null, null, INotifierConfigAction.RECIPIENT_TO, "");
		assertEquals(Action.SUCCESS, result);
		addressErrors = this.getAction().getFieldErrors().get("address");
		assertNull(addressErrors);
		this.checkStrings(recipientsTo, ((NotifierConfigAction) this.getAction()).getRecipientsTo());
	}

	public void testAddAddress() throws Throwable {
		String result = this.executeAddAddress("admin", "PER", new String[] { "a1@aaa.it", "a2@aaa.it" },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a3@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { "a1@aaa.it", "a2@aaa.it", "a3@aaa.it" }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeAddAddress("admin", "PER", new String[] { "a1@aaa.it", "a2@aaa.it" },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a2@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { "a1@aaa.it", "a2@aaa.it" }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeAddAddress("admin", "PER", new String[] { },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a1@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { "a1@aaa.it" }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());
	}

	public void testRemoveAddress() throws Throwable {
		String result = this.executeRemoveAddress("admin", "PER", new String[] { "a1@aaa.it", "a2@aaa.it" },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a3@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { "a1@aaa.it", "a2@aaa.it" }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeRemoveAddress("admin", "PER", new String[] { "a1@aaa.it", "a2@aaa.it" },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a2@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { "a1@aaa.it" }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeRemoveAddress("admin", "PER", new String[] { "a1@aaa.it" },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a1@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());

		result = this.executeRemoveAddress("admin", "PER", new String[] { },
				null, null, INotifierConfigAction.RECIPIENT_TO, "a1@aaa.it");
		assertEquals(Action.SUCCESS, result);
		this.checkStrings(new String[] { }, ((NotifierConfigAction) this.getAction()).getRecipientsTo());
	}

	public void testSaveFailure() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeCode", "PER");
		params.put("mailAttrName", "AttributoInesistente");
		params.put("notifiable", "false");
		params.put("senderCode", "senderCodeFarlocco");

		String result = this.executeSave("admin", params, null, null, null);
		assertEquals(Action.INPUT, result);
		String mailAttrNameMsg = this.getAction().getText("Errors.mailAttrName.notValid");
		String senderCodeMsg = this.getAction().getText("Errors.senderCode.notValid");
		String subjectModelMsg = this.getAction().getText("Errors.subjectModel.required");
		assertEquals(0, this.getAction().getActionErrors().size());
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(3, fieldErrors.size());
		this.checkStrings(new String[] { mailAttrNameMsg }, fieldErrors.get("mailAttrName"));
		this.checkStrings(new String[] { senderCodeMsg }, fieldErrors.get("senderCode"));
		this.checkStrings(new String[] { subjectModelMsg }, fieldErrors.get("subjectModel"));

		params.put("notifiable", "true");
		result = this.executeSave("admin", params, new String[] { "mailNonValida" }, new String[] { "a@aaa.it" }, null);
		assertEquals(Action.INPUT, result);
		assertEquals(0, this.getAction().getActionErrors().size());
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(5, fieldErrors.size());
		this.checkStrings(new String[] { mailAttrNameMsg }, fieldErrors.get("mailAttrName"));
		this.checkStrings(new String[] { senderCodeMsg }, fieldErrors.get("senderCode"));
		String bodyModelMsg = this.getAction().getText("Errors.bodyModel.required");
		this.checkStrings(new String[] { bodyModelMsg }, fieldErrors.get("bodyModel"));
		this.checkStrings(new String[] { subjectModelMsg }, fieldErrors.get("subjectModel"));
		String recipientsToMsg = this.getAction().getText("Errors.recipient.address.notValid",
				new String[] { this.getAction().getText("recipientsTo"), "mailNonValida" });
		this.checkStrings(new String[] { recipientsToMsg }, fieldErrors.get("recipientsTo"));

		params.put("notifiable", "true");
		result = this.executeSave("admin", params, null, null, null);
		assertEquals(Action.INPUT, result);
		String recipientsEmptyMsg = this.getAction().getText("Errors.recipients.emptyList");
		this.checkStrings(new String[] { recipientsEmptyMsg }, this.getAction().getActionErrors());
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(4, fieldErrors.size());
	}

	public void testSave() throws Throwable {
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		String originaryConfig = configManager.getConfigItem(JpwebdynamicformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM);
		MessageTypeNotifierConfig orginaryComConfig = this._messageManager.getNotifierConfig("COM");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("typeCode", "PER");
			params.put("store", "true");
			params.put("mailAttrName", "eMail");
			params.put("notifiable", "true");
			params.put("senderCode", "CODE1");
			params.put("bodyModel", "Mail Body");
			params.put("subjectModel", "Mail Subject");
			String[] recipientsTo = new String[] { "indirizzo1@inesistente.it" };
			String[] recipientsCc = new String[] { "indirizzo2@inesistente.it", "indirizzo3@inesistente.it" };
			String[] recipientsBcc = new String[] { "indirizzo1@inesistente.it", "indirizzo2@inesistente.it" };
			String result = this.executeSave("admin", params, recipientsTo, recipientsCc, recipientsBcc);
			assertEquals(Action.SUCCESS, result);
			String newConfig = configManager.getConfigItem(JpwebdynamicformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM);
			assertFalse(newConfig.equals(originaryConfig));
			MessageTypeNotifierConfig newComConfig = this._messageManager.getNotifierConfig("COM");
			this.checkConfigs(orginaryComConfig, newComConfig);

			MessageTypeNotifierConfig newPerConfig = this._messageManager.getNotifierConfig("PER");
			assertEquals("PER", newPerConfig.getTypeCode());
			assertTrue(newPerConfig.isStore());
			assertEquals("eMail", newPerConfig.getMailAttrName());
			assertTrue(newPerConfig.isNotifiable());
			assertEquals("CODE1", newPerConfig.getSenderCode());
			MessageModel model = newPerConfig.getMessageModel();
			assertEquals("Mail Body", model.getBodyModel());
			assertEquals("Mail Subject", model.getSubjectModel());
			this.checkStrings(recipientsTo, newPerConfig.getRecipientsTo());
			this.checkStrings(recipientsCc, newPerConfig.getRecipientsCc());
			this.checkStrings(recipientsBcc, newPerConfig.getRecipientsBcc());
		} catch (Throwable t) {
			throw t;
		} finally {
			configManager.updateConfigItem(JpwebdynamicformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM, originaryConfig);
		}
	}

	private void checkStrings(String[] expected, Collection<String> received) {
		assertEquals(expected.length, received.size());
		for (String address : expected) {
			if (!received.contains(address)) {
				fail("Expected string " + address + " - Not found");
			}
		}
	}

	private void checkStrings(String[] expected, String[] received) {
		assertEquals(expected.length, received.length);
		for (String address : expected) {
			boolean found = false;
			for (String current : received) {
				if (current.equals(address)) {
					found = true;
					break;
				}
			}
			if (!found) {
				fail("Expected string " + address + " - Not found");
			}
		}
	}

	private void checkConfigs(MessageTypeNotifierConfig expected, MessageTypeNotifierConfig received) {
		assertEquals(expected.getTypeCode(), received.getTypeCode());
		assertEquals(expected.getMailAttrName(), received.getMailAttrName());
		assertEquals(expected.isStore(), received.isStore());
		assertEquals(expected.isNotifiable(), received.isNotifiable());
		assertEquals(expected.getSenderCode(), received.getSenderCode());
		MessageModel expectedModel = expected.getMessageModel();
		MessageModel receivedModel = received.getMessageModel();
		assertEquals(expectedModel.getBodyModel(), receivedModel.getBodyModel());
		assertEquals(expectedModel.getSubjectModel(), receivedModel.getSubjectModel());
		this.checkStrings(expected.getRecipientsTo(), received.getRecipientsTo());
		this.checkStrings(expected.getRecipientsCc(), received.getRecipientsCc());
		this.checkStrings(expected.getRecipientsBcc(), received.getRecipientsBcc());
	}

	private String executeList(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Config", "list");
		String result = this.executeAction();
		return result;
	}

	private String executeEdit(String username, String typeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Config", "edit");
		this.addParameter("typeCode", typeCode);
		String result = this.executeAction();
		return result;
	}

	private String executeAddAddress(String username, String typeCode, String[] recipientsTo, String[] recipientsCc,
			String[] recipientsBcc, int recipientType, String address) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Config", "addAddress");
		this.addParameter("typeCode", typeCode);
		if (recipientsTo!=null) {
			this.addParameter("recipientsTo", recipientsTo);
		}
		if (recipientsCc!=null) {
			this.addParameter("recipientsCc", recipientsCc);
		}
		if (recipientsBcc!=null) {
			this.addParameter("recipientsBcc", recipientsBcc);
		}
		this.addParameter("recipientType", String.valueOf(recipientType));
		this.addParameter("address", address);
		String result = this.executeAction();
		return result;
	}

	private String executeRemoveAddress(String username, String typeCode, String[] recipientsTo, String[] recipientsCc,
			String[] recipientsBcc, int recipientType, String address) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Config", "removeAddress");
		this.addParameter("typeCode", typeCode);
		if (recipientsTo!=null) {
			this.addParameter("recipientsTo", recipientsTo);
		}
		if (recipientsCc!=null) {
			this.addParameter("recipientsCc", recipientsCc);
		}
		if (recipientsBcc!=null) {
			this.addParameter("recipientsBcc", recipientsBcc);
		}
		this.addParameter("recipientType", String.valueOf(recipientType));
		this.addParameter("address", address);
		String result = this.executeAction();
		return result;
	}

	private String executeSave(String username, Map<String, Object> params,
			String[] recipientsTo, String[] recipientsCc, String[] recipientsBcc) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwebdynamicform/Message/Config", "save");
		this.addParameters(params);
		if (recipientsTo!=null) {
			this.addParameter("recipientsTo", recipientsTo);
		}
		if (recipientsCc!=null) {
			this.addParameter("recipientsCc", recipientsCc);
		}
		if (recipientsBcc!=null) {
			this.addParameter("recipientsBcc", recipientsBcc);
		}
		String result = this.executeAction();
		return result;
	}

}