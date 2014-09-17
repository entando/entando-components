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