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
package com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i Manager gestori delle associazioni elemento con TipoContenuto. 
 * @version 1.0
 * @author E.Santoboni
 */
public interface IContentRefManager {
	
	public void addRelation(String elementCode, String contentType) throws ApsSystemException;
	
	public void removeRelation(String elementCode, String contentType) throws ApsSystemException;
	
}
