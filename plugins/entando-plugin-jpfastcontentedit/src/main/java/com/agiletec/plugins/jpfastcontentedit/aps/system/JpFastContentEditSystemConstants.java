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
package com.agiletec.plugins.jpfastcontentedit.aps.system;

/**
 * @author E.Santoboni
 */
public interface JpFastContentEditSystemConstants {
	
	public static final String FRONT_CONTENT_ACTION_HELPER = "jpfastcontenteditContentActionHelper";
	
	/**
	 * Nome del parametro (di request e di sessione) rappresentante il codice della pagina 
	 * di provenienza e di destinazione finale una volta terminate le operazione di edit contenuto. 
	 */
	public static final String FINAL_DEST_PAGE_PARAM_NAME = "jpfastcontentedit_finalDestPage";
	
	public static final String TYPE_CODE_SHOWLET_PARAM_NAME = "typeCode";
	
	public static final String AUTHOR_ATTRIBUTE_SHOWLET_PARAM_NAME = "authorAttribute";
	
	public static final String CONTENT_DISABLING_CODE = "onFastContentEdit";
	
	public final static String ACTION_PATH_FOR_CONTENT_EDIT = "/ExtStr2/do/jpfastcontentedit/Content/entryAction.action";
	
}