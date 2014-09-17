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
package com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.attribute.action.list;

/**
 * @author E.Santoboni
 */
public class ListAttributeAction extends com.agiletec.plugins.jacms.apsadmin.content.attribute.action.list.ListAttributeAction {
	
	public String getLangTabAnchorDest() {
		return this.getListLangCode() + "_tab";
	}
	
}
