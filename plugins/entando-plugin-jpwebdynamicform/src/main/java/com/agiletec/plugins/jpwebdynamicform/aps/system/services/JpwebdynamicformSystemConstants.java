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