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
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.io.InputStream;

/**
 * Interfaccia Servizio gestore delle risorse cestinate.
 * 
 * @author G.Cocco
 */
public interface ITrashedResourceManager {
	
	/**
	 * Effettua la ricerca delle risorse nell'archivio delle risorse in funzione dei parametri di ricerca
	 * 
	 * @param resourceTypeCode l'identificativo del codice di tipo risorsa
	 * @param text il testo da cercare all'interno della definizione della risorsa
	 * @param allowedGroups la lista dei gruppi
	 * */
	List<String> searchTrashedResourceIds(String resourceTypeCode, String text,
			List<String> allowedGroups) throws ApsSystemException;
	
	/**
	 * Carica una risorsa cestinata dal cestino
	 * 
	 * @param id l'identificativo della risorsa
	 * */
	ResourceInterface loadTrashedResource(String id) throws ApsSystemException;
	
	/**
	 * Ripristina una risorsa dal cestino all'archivio delle risorse correnti
	 * 
	 * @param resourceId l'identificativo della risorsa
	 * @param basePath il percorso RealPath di esecuzione per il calcolo dei percorsi sul filesystem
	 * */
	void restoreResource(String resourceId) throws ApsSystemException;
	
	/**
	 * Rimuove una risorsa dal cestino
	 * 
	 * @param resourceId l'identificativo della risorsa
	 * @param basePath il percorso RealPath di esecuzione per il calcolo dei percorsi sul filesystem
	 * */
	void removeFromTrash(String resourceId)
			throws ApsSystemException;
	
	/**
	 * Salva una risorsa nel cestino
	 * 
	 * @param resource la risorsa da salvare
	 * @param basePath il percorso RealPath di esecuzione per il calcolo dei percorsi sul filesystem
	 * */
	void addTrashedResource(ResourceInterface resource)
			throws ApsSystemException;
	
	public InputStream getTrashFileStream(ResourceInterface resource, ResourceInstance instance) throws ApsSystemException;
	
	//Map<String, String> resourceInstancesTrashFilePaths(
	//		ResourceInterface resource) throws ApsSystemException;
	
}