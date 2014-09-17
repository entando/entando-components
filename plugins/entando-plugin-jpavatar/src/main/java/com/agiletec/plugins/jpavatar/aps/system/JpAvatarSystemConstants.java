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
package com.agiletec.plugins.jpavatar.aps.system;

public interface JpAvatarSystemConstants {
	
	/**
	 * Name of the bean that manages the avatars
	 */
	public static final String AVATAR_MANAGER = "jpavatarAvatarManager";
	
	/**
	 * Filename that must be returned when no avatar is found. This file must exists and placed in the "avatarDiskFolder" directory
	 */
	public static final String DEFAULT_AVATAR_NAME = "avatar-default.png";
	
	/**
	 * Config item stored into sysconfig table, *port db
	 */
	public static final String CONFIG_ITEM = "jpavatar_config";
	
}
