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
package com.agiletec.plugins.jpmyportalplus.aps.system;

/**
 * @author E.Santoboni
 */
public interface JpmyportalplusSystemConstants {
	
	public static final String MYPORTALPLUS_CONFIG_ITEM = "jpmyportalplus_config";
	
	public static final String MYPORTAL_CONFIG_MANAGER = "jpmyportalplusMyPortalConfigManager";
	
	public static final String MYPORTAL_MODEL_CONFIG_MANAGER = "jpmyportalplusMyPortalPageModelManager";
	
	public static final String PAGE_USER_CONFIG_MANAGER = "jpmyportalplusPageUserConfigManager";
	
	/**
	 * Nome parametro di sessione: configurazione personalizzata dell'utente corrente
	 */
	public static final String SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG = "jpmyportalplus_currentCustomUserPageConfig";
	
	public static final String SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG = "jpmyportalplus_currentCustomPageConfig";
	
	public static final String SESSIONPARAM_CURRENT_LANG = "jpmyportalplus_currentLang";
	
	/**
	 * Nome parametro extra per requestContext: lista dei frames liberamente configurabili
	 */
	public static final String EXTRAPAR_CURRENT_PAGE_MODEL_SWAPPABLE_FRAMES = "pageModelSwappableFrames";
	
	/**
	 * Nome del parametro di configurazione XML con il quale sono indicate le showlet liberamente configurabili
	 */
	public static final String XML_PARAM_CUSTOMIZABLE_SHOWLETS = "allowed";
	

	
}