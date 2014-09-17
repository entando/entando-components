/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmail.util;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;

/**
 * Helper for the jpmail tests. Use its functionalities to arrange the system for jpmail tests.
 * @author E.Mezzano
 */
public class JpmailTestHelper {
	
	/**
	 * Instantiate 
	 * @param configManager The BaseConfigManager.
	 * @param mailManager The IMailManager.
	 */
	public JpmailTestHelper(ConfigInterface configManager, IMailManager mailManager) {
		this._configManager = configManager;
		this._mailManager = mailManager;
		this._config = this._configManager.getConfigItem(JpmailSystemConstants.MAIL_CONFIG_ITEM);
	}
	
	/**
	 * Resets the jpmail system configuration.
	 * Use this method after an update of the configuration.
	 * @throws Throwable 
	 */
	public void resetConfig() throws Exception {
		this._configManager.updateConfigItem(JpmailSystemConstants.MAIL_CONFIG_ITEM, this._config);
		try {
			((AbstractService) this._mailManager).refresh();
		} catch (Throwable t) {
			throw new ApsSystemException("Error in resetConfig", t);
		}
	}
	
	private String _config;
	
	private ConfigInterface _configManager;
	private IMailManager _mailManager;
	
	/**
	 * Mail addresses used to test the IMailManager send methods.
	 * Change this addresses to check correct mail dispatch.
	 */
	public static final String[] MAIL_ADDRESSES = { "testuser@localhost" };
	
}