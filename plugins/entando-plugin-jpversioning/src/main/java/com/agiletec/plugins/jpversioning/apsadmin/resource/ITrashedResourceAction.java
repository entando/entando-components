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
package com.agiletec.plugins.jpversioning.apsadmin.resource;

import java.util.List;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

public interface ITrashedResourceAction {
	
	/**
	 * Rimuove una risorsa dal cestino in base all'identificativo della risorsa
	 * */
	public String remove();
	
	/**
	 * Ripristina una risorsa dal cestino all'archivio delle risorse
	 * */
	public String restore();
	
	/**
	 * Restituisce una risorsa cestinata in base all'identificativo
	 * */
	public ResourceInterface getTrashedResource(String id) throws Throwable;
	
	/**
	 * Restituice la lista degli identificativi delle risorse in base ai parametri di ricerca
	 * */
	public List<String> getTrashedResources() throws Throwable;
	
}