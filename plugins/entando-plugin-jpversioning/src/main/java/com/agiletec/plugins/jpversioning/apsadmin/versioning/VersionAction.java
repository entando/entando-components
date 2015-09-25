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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager;

/**
 * @author G.Cocco
 */
public class VersionAction extends AbstractContentAction implements IVersionAction {
	
	@Override
	public String history() {
		return SUCCESS;
	}
	
	@Override
	public String trash() {
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			this.getVersioningManager().deleteVersion(getVersionId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String preview() {
		try {
			ContentVersion contentVersion = this.getContentVersion();
			Content currentContent = this.getVersioningManager().getContent(contentVersion);
			this.setContent(currentContent);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "preview");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String entryRecover() {
		return SUCCESS;
	}
	
	@Override
	public String recover() {
		Content content = null;
		try {
			ContentVersion contentVersion = this.getContentVersion();
			if (null == contentVersion) {
				throw new RuntimeException("Null content version : versionid " + this.getVersionId());
			}
			List<String> trashedResources = this.getTrashedResources();
			if (null != trashedResources && trashedResources.size() > 0) {
				for (String resourceId : trashedResources) {
					this.getTrashedResourceManager().restoreResource(resourceId);
				}
			}
			content = this.getVersioningManager().getContent(contentVersion);
			this.getRequest().getSession()
					.setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + super.getContentOnSessionMarker(), content);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "recover");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<Long> getContentVersions() {
		List<Long> versions = null;
		try {
			versions = this.getVersioningManager().getVersions(this.getContentId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVersions");
			throw new RuntimeException("Errore in estrazione lista versioni per contenuto " + this.getContentId(), t);
		}
		return versions;
	}
	
	public ContentVersion getContentVersion(long versionId) {
		ContentVersion version = null;	
		try {
			version = this.getVersioningManager().getVersion(versionId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVersion");
			throw new RuntimeException("Errore in estrazione versione contenuto, id versione " + versionId, t);
		}
		return version;
	}
	
	public ResourceInterface getTrashedResource(String id) {
		ResourceInterface resource = null;	
		try {
			resource  = this.getTrashedResourceManager().loadTrashedResource(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTrashedResource");
			throw new RuntimeException("Errore in caricamento risorsa, id " + id, t);
		}
		return resource;
	}
	
	public List<String> getTrashedResources() {
		if (this._trashedResources == null) {
			try {
				this.loadTrashResources();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getTrashedResources");
				throw new RuntimeException("Errore in recupero id risorse cestinate per la versione contenuto corrente " + getVersionId(), t);
			}
		}
		return this._trashedResources;
	}
	
	public Set<String> getTrashRemovedResources() {
		if (this._trashRemovedResources == null) {
			try {
				this.loadTrashResources();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getTrashRemovedResources");
				throw new RuntimeException("Errore in recupero id risorse rimosse dal cestino (non più ripristinabili) per la versione contenuto corrente " + getVersionId(), t);
			}
		}
		return this._trashRemovedResources;
	}
	
	private void loadTrashResources() throws Exception {
		ContentVersion contentVersion = this.getContentVersion();
		String contentXml = contentVersion.getXml();
		Document doc = this.loadContentDocumentDOM(contentXml);			
		List<String> resourceIds = this.loadResourcesIdFromContentDocumentDOM(doc);
		List<String> archivedResources = this.getArchivedResourcesId(resourceIds);
		if (null != resourceIds && resourceIds.size() > 0 ) {
			this._trashedResources = this.getTrashedResourcesId(resourceIds, archivedResources);
			this._trashRemovedResources = this.getTrashRemovedResourcesId(resourceIds, archivedResources);
		}
	}
	
	/*
	 * Restituisce la lista degli identificativi di quelle risorse 
	 * che sono state cestinate ed eliminate dal cestino.
	 * */
	private Set<String> getTrashRemovedResourcesId(List<String> resourceIds, List<String> archivedResources) {
		Set<String> trashRemovedResources = null;
		Iterator<String> it = resourceIds.iterator();
		String id = null;
		while (it.hasNext()) {
			id = (String)it.next();
			ResourceInterface resourceInterfaceArchived = null;
			ResourceInterface resourceInterfaceTrashed = null;
			try {
				resourceInterfaceTrashed = this.getTrashedResourceManager().loadTrashedResource(id);
				resourceInterfaceArchived = this.getResourceManager().loadResource(id);
				if (!(null != resourceInterfaceArchived || null != resourceInterfaceTrashed)) {
					if (null == trashRemovedResources) {
						trashRemovedResources = new HashSet<String>();
					}
					trashRemovedResources.add(id);
				}
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getTrashRemovedResourcesId");
				throw new RuntimeException("Errore in verifica risorsa rimossa definitivamente da cestino, id " + id, t);
			}
		}
		return trashRemovedResources;
	}
	
	/*
	 * Verifica quali delle risorse presenti in resourcesIds risulta nel cestino risorse
	 * */
	private List<String> getArchivedResourcesId(List<String> resourceIds) {
		List<String> archivedResources = null;
		if (null != resourceIds) {
			Iterator<String> it = resourceIds.iterator();
			String id = null;
			while (it.hasNext()) {
				ResourceInterface resourceInterface = null;
				id = (String) it.next();
				try {
					resourceInterface = this.getResourceManager().loadResource(id);
					if (null != resourceInterface) {
						if ( null == archivedResources) {
							archivedResources = new ArrayList<String>();
						}
						archivedResources.add(id);
					}
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "verifyArchivedResourcesId");
					throw new RuntimeException("Errore in verifica risorsa in archivio, id " + id, t);
				}
			}
		}
		return archivedResources;
	}
	
	private List<String> getTrashedResourcesId(List<String> resourceIds, List<String> archivedResources) {
		List<String> trashedResources = null; 
		Iterator<String> it = resourceIds.iterator();
		String id = null;
		while (it.hasNext()) {
			id = (String) it.next();
			if (null == archivedResources || !archivedResources.contains(id)) {
				ResourceInterface resourceInterface = null;
				try {
					resourceInterface = this.getTrashedResourceManager().loadTrashedResource(id);
					if (null != resourceInterface) {
						if (null == trashedResources) {
							trashedResources = new ArrayList<String>();
						}
						trashedResources.add(id);
					}
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "verifyNotTrashedResources");
					throw new RuntimeException("Errore in verifica risorsa cestinata, id " + id, t);
				}
			}
		}
		return trashedResources;
	}
	
	/*
	 * Restituisce gli id delle risorse presenti nel documento
	 * */
	private List<String> loadResourcesIdFromContentDocumentDOM(Document doc) {
		List<String> resourcesId = null;
		NodeList listaNodiAttributi = doc.getElementsByTagName(RESOURCE);
		for (int i = 0 ; i < listaNodiAttributi.getLength(); i++) {
			Node node = listaNodiAttributi.item(i);
			NamedNodeMap namedNodeMap = node.getAttributes();
			if (null == resourcesId) {
				resourcesId = new ArrayList<String>();
			}
			resourcesId.add(namedNodeMap.getNamedItem(RESOURCE_ID).getNodeValue());
		}
		return resourcesId;
	}
	
	private Document loadContentDocumentDOM(String contentXml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fact.newDocumentBuilder();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentXml.getBytes("UTF-8"));
		Document doc = builder.parse(byteArrayInputStream);
		return doc;
	}
	
	public ContentVersion getContentVersion() {
		if (this._contentVersion == null) {
			try {
				this._contentVersion = this.getVersioningManager().getVersion(this.getVersionId());
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "viewVersions");
				throw new RuntimeException("Errore in estrazione versione contenuto, id versione " + this.getVersionId(), t);
			}
		}
		return this._contentVersion;
	}
	
	@Override
	public SmallContentType getSmallContentType(String typeCode) {
		SmallContentType contentType = this.getContentManager().getSmallContentTypesMap().get(typeCode);
		return contentType;
	}
	
	public long getBackId() {
		return _backId;
	}
	public void setBackId(long backId) {
		this._backId = backId;
	}
	
	public long getVersionId() {
		return _versionId;
	}
	public void setVersionId(long versionId) {
		this._versionId = versionId;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public boolean isFromEdit() {
		return _fromEdit;
	}
	public void setFromEdit(boolean fromEdit) {
		this._fromEdit = fromEdit;
	}
	
	public Content getContent() {
		return _content;
	}
	protected void setContent(Content content) {
		this._content = content;
	}
	
	protected IVersioningManager getVersioningManager() {
		return _versioningManager;
	}
	public void setVersioningManager(IVersioningManager versioningManager) {
		this._versioningManager = versioningManager;
	}
	
	public ITrashedResourceManager getTrashedResourceManager() {
		return _trashedResourceManager;
	}
	public void setTrashedResourceManager(ITrashedResourceManager trashedResourceManager) {
		this._trashedResourceManager = trashedResourceManager;
	}
	
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	public IResourceManager getResourceManager() {
		return _resourceManager;
	}
	
	private long _versionId;
	private String _contentId;
	
	private long _backId;
	private boolean _fromEdit;
	
	private Content _content;
	private ContentVersion _contentVersion;
	private List<String> _trashedResources = null;
	private Set<String> _trashRemovedResources = null;
	
	private IVersioningManager _versioningManager;
	private IResourceManager _resourceManager;
	private ITrashedResourceManager _trashedResourceManager;
	
	private final static String RESOURCE = "resource";
	private final static String RESOURCE_ID = "id";
	
}