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
package com.agiletec.plugins.jpversioning.apsadmin.versioning;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager;

/**
 * @author G.Cocco
 */
public class VersionFinderAction extends BaseAction implements IVersionFinderAction {
	
	@Override
	public List<Long> getLatestVersions() {
		List<Long> latestVersions = null;
		try {
			latestVersions = this.getVersioningManager().getLastVersions(this.getContentType(), this.getDescr());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getLatestVersions");
			throw new RuntimeException("Error loading latest content versions", t);
		}
		return latestVersions;
	}
	
	@Override
	public ContentVersion getContentVersion(long id) {
		ContentVersion version = null;	
		try {
			version = this.getVersioningManager().getVersion(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "viewVersions");
			throw new RuntimeException("Error loading content version of id " + id, t);
		}
		return version;
	}
	
	/**
	 * Restituisce la lista di contenuti (in forma small) definiti nel sistema.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire 
	 * una corretta visualizzazione della pagina.
	 * @return La lista di tipi di contenuto (in forma small) definiti nel sistema.
	 */
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	/**
	 * Restituisce un tipo di contenuto in forma small.
	 * @param typeCode Il codice del tipo di contenuto.
	 * @return Il tipo di contenuto (in forma small) cercato.
	 */
	public SmallContentType getSmallContentType(String typeCode) {
		Map<String, SmallContentType> smallContentTypes = this.getContentManager().getSmallContentTypesMap();
		return (SmallContentType) smallContentTypes.get(typeCode);
	}
	
	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	public long getVersionid() {
		return _versionid;
	}
	public void setVersionid(long versionid) {
		this._versionid = versionid;
	}
	
	public long getBackid() {
		return _backid;
	}
	public void setBackid(long backid) {
		this._backid = backid;
	}
	
	protected IVersioningManager getVersioningManager() {
		return _versioningManager;
	}
	public void setVersioningManager(IVersioningManager versioningManager) {
		this._versioningManager = versioningManager;
	}
	
	/**
	 * Restituisce il manager gestore delle operazioni sui contenuti.
	 * @return Il manager gestore delle operazioni sui contenuti.
	 */
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	/**
	 * Setta il manager gestore delle operazioni sui contenuti.
	 * @param contentManager Il manager gestore delle operazioni sui contenuti.
	 */
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private String _contentOnSessionMarker;
	
	private String _contentType;
	private String _descr;
	private long _versionid;
	private long _backid;
	
	private IVersioningManager _versioningManager;
	private IContentManager _contentManager;
	
}