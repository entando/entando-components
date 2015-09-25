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