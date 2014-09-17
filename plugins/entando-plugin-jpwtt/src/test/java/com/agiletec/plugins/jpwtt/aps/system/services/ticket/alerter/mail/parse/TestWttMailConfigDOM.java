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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.parse;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpwtt.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model.MailTemplate;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.model.WttMailConfig;

public class TestWttMailConfigDOM extends ApsPluginBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testReadConfig() throws Throwable {
		String xml = this._configManager.getConfigItem("jpwttMailConfig");
		WttMailConfig config = new WttMailConfigDOM().extractConfig(xml);

		assertEquals(true, config.isUniqueMail());
		assertEquals("CODE1", config.getSenderCode());
		assertEquals("email", config.getMailAttrName());
		assertEquals("Wtt Alert", config.getSubject());

		Map<Integer, MailTemplate> templates = config.getTemplates();
		assertEquals(6, templates.size());
		MailTemplate mailTemplate = templates.get(new Integer(5));
		Map<String, String> bodies = mailTemplate.getTemplateBodies();
		assertEquals("Op3: Testo della mail admin", bodies.get(MailTemplate.TEMPLATE_TYPE_ADMIN));
		assertEquals("Op3: Testo della mail user", bodies.get(MailTemplate.TEMPLATE_TYPE_USER));
		assertEquals("Op3: Testo della mail allOperators", bodies.get(MailTemplate.TEMPLATE_TYPE_ALLOPERATORS));

		List<String> adminAddresses = config.getCommonAdminAddresses();
		assertEquals(2, adminAddresses.size());
		List<String> operatorAddresses = config.getCommonOperatorAddresses();
		assertEquals(1, operatorAddresses.size());

		Map<Integer, List<String>> intervTypesAdminAddresses = config.getIntervTypesAdminAddresses();
		assertEquals(1, intervTypesAdminAddresses.size());
		adminAddresses = intervTypesAdminAddresses.get(1);
		assertEquals(2, adminAddresses.size());

		Map<Integer, List<String>> intervTypesOperatorAddresses = config.getIntervTypesOperatorAddresses();
		assertEquals(1, intervTypesOperatorAddresses.size());
		operatorAddresses = intervTypesOperatorAddresses.get(1);
		assertEquals(3, operatorAddresses.size());
	}

	private void init() throws Exception {
		try {
			this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}

	private ConfigInterface _configManager;

}