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