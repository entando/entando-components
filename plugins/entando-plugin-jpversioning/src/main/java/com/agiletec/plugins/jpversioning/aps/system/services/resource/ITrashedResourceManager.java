/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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