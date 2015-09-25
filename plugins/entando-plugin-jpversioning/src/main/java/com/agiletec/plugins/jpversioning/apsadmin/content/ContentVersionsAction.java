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