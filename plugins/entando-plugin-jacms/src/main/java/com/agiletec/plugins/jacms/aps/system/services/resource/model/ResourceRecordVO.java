/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Oggetto di utilità per le risorse. Ha la funzione fare da tramite 
 * (tra dati estratti da db e parser) nel caricamento di una risorsa completa.
 * @author E.Santoboni
 */
public class ResourceRecordVO implements Serializable {
	
	/**
	 * Restituisce l'id della risorsa.
	 * @return L'id della risorsa.
	 */
	public String getId() {
		return _id;
	}
	
	/**
	 * Setta l'id della risorsa.
	 * @param id L'id della risorsa.
	 */
	public void setId(String id) {
		this._id = id;
	}
	
	/**
	 * Restituisce il tipo della risorsa.
	 * @return Il tipo della risorsa.
	 */
	public String getResourceType() {
		return _resourceType;
	}

	/**
	 * Setta il tipo della risorsa.
	 * @param resourceType Il tipo della risorsa.
	 */
	public void setResourceType(String resourceType) {
		this._resourceType = resourceType;
	}
	
	/**
	 * Restituisce la descrizione della risorsa.
	 * @return La descrizione della risorsa.
	 */
	public String getDescr() {
		return _descr;
	}
	
	/**
	 * Setta la descrizione della risorsa.
	 * @param descr La descrizione della risorsa.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	/**
     * Restituisce la stringa identificante 
     * il gruppo principale di cui la risorsa è membro.
     * @return Il gruppo principale di cui la risorsa è membro.
     */
    public String getMainGroup() {
		return _mainGroup;
	}
	
    /**
	 * Setta la stringa identificante 
     * il gruppo principale di cui il contenuto è membro.
	 * @param mainGroup Il gruppo principale di cui il contenuto è membro.
	 */
    public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public String getMasterFileName() {
		return _masterFileName;
	}
	public void setMasterFileName(String masterFileName) {
		this._masterFileName = masterFileName;
	}
	
	/**
	 * Restituisce l'xml completo della risorsa.
	 * @return L'xml completo della risorsa.
	 */
	public String getXml() {
		return _xml;
	}
	
	/**
	 * Setta l'xml completo della risorsa.
	 * @param xml L'xml completo della risorsa.
	 */
	public void setXml(String xml) {
		this._xml = xml;
	}

	/**
	 * Gets the owner of the resource
	 * @return The owner's username
	 */
	public String getOwner() {
		return _owner;
	}

	/**
	 * Sets the owner of the resource
	 * @param owner The owner's username
	 */
	public void setOwner(String owner) {
		this._owner = owner;
	}

	public Date getCreationDate() {
		return _creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	public Date getLastModified() {
		return _lastModified;
	}
	public void setLastModified(Date lastModified) {
		this._lastModified = lastModified;
	}
	
	private String _id;
	private String _resourceType;
	private String _descr;
	private String _mainGroup;
	private String _masterFileName;
	private String _xml;
	private String _owner;
	
	private Date _creationDate;
	private Date _lastModified;
	
}