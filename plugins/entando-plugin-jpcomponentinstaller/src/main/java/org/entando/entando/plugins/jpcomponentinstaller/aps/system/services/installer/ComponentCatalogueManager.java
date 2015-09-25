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
	public AvailableArtifact getArtifact(String groupId, String artifactId) {
		if (null == groupId || null == artifactId) {
			return null;
		}
		if (null == this._artifacts) {
			this.loadCatalogue();
		}
		for (int i = 0; i < this._artifacts.size(); i++) {
			AvailableArtifact artifact = this._artifacts.get(i);
			if (artifact.getGroupId().equals(groupId) && artifact.getArtifactId().equals(artifactId)) {
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
