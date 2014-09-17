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
package com.agiletec.plugins.jpcontentrefs.aps.system.services.category;

import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jpcontentrefs.aps.system.services.contentrelations.IContentRefManager;

/**
 * Interfaccia base per i Manager gestori delle associazioni categorie/TipoContenuto. 
 * @author E.Santoboni
 */
public interface IContentCategoryRefManager extends IContentRefManager {
	
	/**
	 * Restituisce la lista (ordinata per codice) delle categorie disponibili
	 * per il tipo di contenuto specificato.
	 * @return La lista (ordinata per codice) delle categorie disponibili 
	 * per il tipo di contenuto specificato.
	 */
	public List<Category> getCategories(String contentType);
	
}
