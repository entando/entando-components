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

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.ITrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.IVersioningManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author G.Cocco
 */
public class VersionAction extends AbstractContentAction {
	
	private static final Logger logger = LoggerFactory.getLogger(VersionAction.class);

    public String history() {
		return SUCCESS;
	}
	
	public String trash() {
		return SUCCESS;
	}
	
	public String delete() {
		try {
			this.getVersioningManager().deleteVersion(this.getVersionId());
		} catch (Exception e) {
			logger.error("Error deleting version " + this.getVersionId(), e);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String preview() {
		try {
			ContentVersion contentVersion = this.getContentVersion();
			Content currentContent = this.getVersioningManager().getContent(contentVersion);
			this.setContent(currentContent);
		} catch (Exception e) {
			logger.error("Error on preview of version ", e);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String entryRecover() {
		return SUCCESS;
	}
	
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
            String lastVersionNumber = null;
            ContentRecordVO currentRecordVo = this.getContentManager().loadContentVO(contentVersion.getContentId());
            if (null != currentRecordVo) {
                lastVersionNumber = currentRecordVo.getVersion();
            } else {
                ContentVersion lastVersion = this.getVersioningManager().getLastVersion(contentVersion.getContentId());
                lastVersionNumber = lastVersion.getVersion();
            }
            content.setVersion(lastVersionNumber);
            this.getContentManager().saveContent(content);
            String marker = AbstractContentAction.buildContentOnSessionMarker(contentVersion.getContentId(), 
                    contentVersion.getContentType(), ApsAdminSystemConstants.EDIT);
            this.setContentOnSessionMarker(marker);
			this.getRequest().getSession()
					.setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + marker, content);
		} catch (Exception e) {
			logger.error("Error recovering version " + this.getVersionId(), e);
			return FAILURE;
		}
		return SUCCESS;
	}
    
    public Content getCurrentContent(String contentId) {
        Content current = null;
		try {
			current = this.getContentManager().loadContent(contentId, false);
		} catch (Exception e) {
			logger.error("Error extracting current content " + contentId, e);
			throw new RuntimeException("Error extracting current content " + contentId, e);
		}
		return current;
    }
	
	public List<Long> getContentVersions() {
		List<Long> versions = null;
		try {
			versions = this.getVersioningManager().getVersions(this.getContentId());
		} catch (Exception e) {
			logger.error("Error extracting versions of content " + this.getContentId(), e);
			throw new RuntimeException("Errore in estrazione lista versioni per contenuto " + this.getContentId(), e);
		}
		return versions;
	}
	
	public ContentVersion getContentVersion(long versionId) {
		ContentVersion version = null;	
		try {
			version = this.getVersioningManager().getVersion(versionId);
		} catch (Exception e) {
			logger.error("Error extracting current content " + versionId, e);
			throw new RuntimeException("Error extracting current version " + versionId, e);
		}
		return version;
	}
	
	public ResourceInterface getTrashedResource(String id) {
		ResourceInterface resource = null;	
		try {
			resource  = this.getTrashedResourceManager().loadTrashedResource(id);
		} catch (Exception e) {
			logger.error("Error loading resource " + id, e);
			throw new RuntimeException("Error loading resource " + id, e);
		}
		return resource;
	}
	
    public List<String> getTrashedResources() {
        if (this._trashedResources == null) {
            try {
                this.loadTrashResources();
            } catch (Exception e) {
                logger.error("Error loading trashed resources for content with version " + this.getVersionId(), e);
                throw new RuntimeException("Error loading trashed resources for content with version " + getVersionId(), e);
            }
        }
        return this._trashedResources;
    }
	
    public Set<String> getTrashRemovedResources() {
        if (this._trashRemovedResources == null) {
            try {
                this.loadTrashResources();
            } catch (Exception e) {
                logger.error("Error loading trashed resources for content with version " + this.getVersionId(), e);
                throw new RuntimeException("Error loading trashed resources for content with version " + getVersionId(), e);
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
				logger.error("Error checking resource permanently removed, id " + id, t);
				throw new RuntimeException("Error checking resource permanently removed, id " + id, t);
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
							archivedResources = new ArrayList<>();
						}
						archivedResources.add(id);
					}
				} catch (Throwable t) {
                    logger.error("Error checking resource " + id, t);
					throw new RuntimeException("Error checking resource " + id, t);
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
							trashedResources = new ArrayList<>();
						}
						trashedResources.add(id);
					}
				} catch (Throwable t) {
                    logger.error("Error checking resource " + id, t);
					throw new RuntimeException("Error checking resource " + id, t);
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
				resourcesId = new ArrayList<>();
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
                logger.error("Error extracting content version " + this.getVersionId(), t);
				throw new RuntimeException("Error extracting content version " + this.getVersionId(), t);
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