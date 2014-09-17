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
package com.agiletec.plugins.jpnewsletter.apsadmin.config;

/**
 * @author E.Santoboni
 */
public interface INewsletterConfigAction {
	
	/**
	 * Esegue l'azione di edit della configurazione del servizio di Newsletter.
	 * @return Il codice del risultato dell'azione.
	 */
	public String edit();
	
	public String entryConfig();
	
	public String addCategoryMapping();
	
	public String removeCategoryMapping();
	
	public String addContentType();
	
	public String removeContentType();
	
	/**
	 * Esegue l'azione di salvataggio della configurazione del servizio di Newsletter.
	 * @return Il codice del risultato dell'azione.
	 */
	public String save();
	
	
	
}
