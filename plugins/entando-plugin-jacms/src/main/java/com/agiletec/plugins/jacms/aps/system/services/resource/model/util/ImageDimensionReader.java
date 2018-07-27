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
package com.agiletec.plugins.jacms.aps.system.services.resource.model.util;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.RefreshableBean;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ImageDimensionDOM;

/**
 * Classe delegata al caricamento 
 * delle dimensioni per il redimensionamento delle immagini.
 * @author E.Santoboni
 */
public class ImageDimensionReader implements IImageDimensionReader, RefreshableBean, Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(ImageDimensionReader.class);
	
	/**
	 * Inizializzazione della classe.
	 * Effettua il caricamento delle dimensioni di resize delle immagini.
	 * @throws Exception In caso di errore.
	 */
	public void init() throws Exception {
		try {
    		String xml = this.getConfigManager().getConfigItem(JacmsSystemConstants.CONFIG_ITEM_IMAGE_DIMENSIONS);
    		if (xml == null) {
    			throw new ApsSystemException("Missing config Item: " + JacmsSystemConstants.CONFIG_ITEM_IMAGE_DIMENSIONS);
    		}
    		ImageDimensionDOM dimensionDom = new ImageDimensionDOM(xml);
    		this._imageDimensions = dimensionDom.getDimensions();
    	} catch (Throwable t) {
    		_logger.error("Error loading dimensions", t);
    		//ApsSystemUtils.logThrowable(t, this, "init");
    		throw new ApsSystemException("Error loading dimensions", t);
    	}
	}
	
	@Override
	public void refresh() throws Throwable {
		this.init();
	}
	
	/**
     * Restituisce la mappa delle dimensioni di resize delle immagini, 
     * indicizzate in base all'id della dimensione.
     * @return La mappa delle dimensioni di resize delle immagini.
     */
	@Override
	public Map<Integer, ImageResourceDimension> getImageDimensions() {
    	return _imageDimensions;
    }
    
    protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configService) {
		this._configManager = configService;
	}
	
	/**
     * Mappa delle dimensioni di resize delle immagini, 
     * indicizzate in base all'id della dimensione.
     */
    private Map<Integer, ImageResourceDimension> _imageDimensions;
    
    private transient ConfigInterface _configManager;
	
}
