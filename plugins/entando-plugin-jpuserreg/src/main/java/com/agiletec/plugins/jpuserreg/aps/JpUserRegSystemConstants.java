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
package com.agiletec.plugins.jpuserreg.aps;

public interface JpUserRegSystemConstants {
	
	/**
	 * Name of the service for management of user/profile registration.
	 */
	public static final String USER_REG_MANAGER = "jpuserregUserRegManager";
	
	/**
	 * Name of the sysconfig parameter containing the Plugin configuration
	 */
	public static final String USER_REG_CONFIG_ITEM = "jpuserreg_Config";

	/**
	 * Type code parameter in showlet.
	 */
	public static final String TYPECODE_SHOWLET_PARAM = "typeCode";
	
	/**
	 * The name of the attribute that contains the lang code.
	 */
	public static final String ATTRIBUTE_ROLE_LANGUAGE = "jpuserreg:language";
	
	/**
	 * The name of the disabling code in user registration.
	 */
	public static final String ATTRIBUTE_DISABLING_CODE_ON_REGISTRATION = "jpuserreg:onEdit";
	
}