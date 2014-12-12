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
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ComponentCatalogueManager extends AbstractService implements IComponentCatalogueManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(ComponentCatalogueManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	protected void release() {
		super.release();
		this.setArtifacts(null);
	}
	
	protected void loadCatalogue() {
		try {
			String xml = this.extractArtifactDefinition();
			ComponentDOM dom = new ComponentDOM(xml);
			List<AvailableArtifact> artifacts = dom.getArtifacts();
			this.setArtifacts(artifacts);
		} catch (Throwable t) {
			_logger.error("Error loading catalogue", t);
			throw new RuntimeException("Error loading catalogue", t);
		}
	}
	
	protected String extractArtifactDefinition() throws ApsSystemException {
		String xml = null;
		try {
			//TODO add check for URI (catalogue on entando.com portal)
			InputStream is = this.getClass().getResourceAsStream("availableComponents_default.xml");
			xml = FileTextReader.getText(is);
		} catch (Throwable t) {
			_logger.error("Error extracting component definition", t);
			throw new RuntimeException("Error extracting component definition", t);
		}
		return xml;
	}
	
	@Override
	public AvailableArtifact getArtifact(Integer id) {
		if (id == null) {
			return null;
		}
		if (null == this._artifacts) {
			this.loadCatalogue();
		}
		for (int i = 0; i < this._artifacts.size(); i++) {
			AvailableArtifact artifact = this._artifacts.get(i);
			if (artifact.getId().equals(id)) {
				return (AvailableArtifact) artifact.clone();
			}
		}
		return null;
	}
	
	@Override
	public List<AvailableArtifact> getArtifacts() {
		if (null == this._artifacts) {
			this.loadCatalogue();
		}
		List<AvailableArtifact> artifactsCloned = new ArrayList<AvailableArtifact>();
		for (int i = 0; i < this._artifacts.size(); i++) {
			AvailableArtifact artifact = this._artifacts.get(i);
			artifactsCloned.add((AvailableArtifact) artifact.clone());
		}
		return artifactsCloned;
	}
	
	protected void setArtifacts(List<AvailableArtifact> artifacts) {
		this._artifacts = artifacts;
	}
	
	private List<AvailableArtifact> _artifacts;
	
}
