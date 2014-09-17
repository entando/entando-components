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
package com.agiletec.plugins.jpversioning.aps.system;

/**
 * @author G.Cocco
 */
public interface JpversioningSystemConstants {
	
	/**
	 * Nome del servizio che gestisce le risorse cestinate.
	 */
	public static final String TRASHED_RESOURCE_MANAGER = "jpversioningTrashedResourceManager";
	
	/**
	 * Nome del servizio che gestisce le risorse cestinate.
	 */
	public static final String VERSIONING_MANAGER = "jpversioningVersioningManager";
	
	public final static String TRASHED_IMAGE_RESOURCE_DIR_NAME = "images";
	
	public final static String TRASHED_ATTACH_RESOURCE_DIR_NAME = "documents";
	
	public final static String CONFIG_PARAM_DELETE_MID_VERSIONS = "jpversioning_deleteMidVersions";
	
	public final static String CONFIG_PARAM_RESOURCE_TRASH_FOLDER = "jpversioning_resourceTrashRootDiskFolder";
	
	public final static String DEFAULT_RESOURCE_TRASH_FOLDER_NAME = "jpversioning/trashedresources";
	
}