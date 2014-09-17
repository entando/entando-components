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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services;

/**
 * Interface for 'jpwebdynamicform' plugin system constants.
 * @author E.Mezzano
 */
public interface JpwebdynamicformSystemConstants {

	/**
	 * The name of the configuration item containing the Message Notifier Configuration.
	 */
	public static final String MESSAGE_NOTIFIER_CONFIG_ITEM = "jpwebdynamicform_messageNotifierConfig";

	/**
	 * Web Dynamic Form Messages manager bean name
	 */
	public static final String MESSAGE_MANAGER = "jpwebdynamicformMessageManager";

	/**
	 * Base Entity Renderer bean name.
	 */
	public static final String BASE_ENTITY_RENDERER = "jpwebdynamicformBaseMessageRenderer";
	
	/**
	 * Type code parameter in widget.
	 */
	public static final String TYPECODE_WIDGET_PARAM = "typeCode";
	
	@Deprecated
	public static final String TYPECODE_SHOWLET_PARAM = TYPECODE_WIDGET_PARAM;
	
	public static final String FORM_PROTECTION_TYPE_WIDGET_PARAM = "formProtectionType";
	
	public static final String RECAPTCHA_PUBLICKEY_PARAM_NAME = "jpwebdynamicform_recaptcha_publickey";
	public static final String RECAPTCHA_PRIVATEKEY_PARAM_NAME = "jpwebdynamicform_recaptcha_privatekey";
	
	public static final String FORM_PROTECTION_TYPE_NONE = "NONE";
	public static final String FORM_PROTECTION_TYPE_HONEYPOT ="HONEYPOT";
	//public static final String FORM_PROTECTION_TYPE_CAPTCHA = "CAPTCHA";
	//public static final String FORM_PROTECTION_TYPE_CAPTCHA_AFTER = "CAPTCHA_AFTER";
	public static final String FORM_PROTECTION_TYPE_RECAPTCHA = "RECAPTCHA";
	public static final String FORM_PROTECTION_TYPE_RECAPTCHA_AFTER = "RECAPTCHA_AFTER";
	public static final String[] FORM_PROTECTION_TYPES = {FORM_PROTECTION_TYPE_NONE, 
		FORM_PROTECTION_TYPE_HONEYPOT, /*FORM_PROTECTION_TYPE_CAPTCHA, FORM_PROTECTION_TYPE_CAPTCHA_AFTER, */
		FORM_PROTECTION_TYPE_RECAPTCHA, FORM_PROTECTION_TYPE_RECAPTCHA_AFTER};
	
}