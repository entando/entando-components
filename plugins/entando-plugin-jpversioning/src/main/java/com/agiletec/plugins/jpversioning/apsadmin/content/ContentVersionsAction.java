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
package com.agiletec.plugins.jpversioning.apsadmin.content;

import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager;

/**
 * @author E.Santoboni 
 * @author G.Cocco
 */
public class ContentVersionsAction extends BaseAction implements IContentVersionsAction {
	
	@Override
	public List<Long> getContentVersions() {
		Content currentContent = (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
		if (null == currentContent) {
			throw new RuntimeException("Contenuto corrente nullo");
		}
		List<Long> versions = null;
		try {
			versions = this.getVersioningManager().getVersions(currentContent.getId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVersions");
			throw new RuntimeException("Errore in estrazione lista versioni per contenuto " + currentContent.getId(), t);
		}
		return versions;
	}
	
	@Override
	public ContentVersion getContentVersion(long id) {
		ContentVersion version = null;	
		try {
			version = this.getVersioningManager().getVersion(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVersion");
			throw new RuntimeException("Errore in estrazione versione contenuto, id versione " + id, t);
		}
		return version;
	}
	
	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}
	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}
	
	protected IVersioningManager getVersioningManager() {
		return _versioningManager;
	}
	public void setVersioningManager(IVersioningManager versioningManager) {
		this._versioningManager = versioningManager;
	}
	
	public long getVersionid() {
		return _versionid;
	}
	public void setVersionid(long versionid) {
		this._versionid = versionid;
	}
	
	private String _contentOnSessionMarker;
	
	private IVersioningManager _versioningManager;
	private long _versionid;
	
}