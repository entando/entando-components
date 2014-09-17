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