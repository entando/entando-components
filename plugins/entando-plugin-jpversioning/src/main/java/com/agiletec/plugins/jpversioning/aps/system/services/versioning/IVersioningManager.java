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
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public interface IVersioningManager {
	
	public List<Long> getVersions(String contentId) throws ApsSystemException;
	
	public List<Long> getLastVersions(String contentType, String descr) throws ApsSystemException;
	
	public ContentVersion getVersion(long id) throws ApsSystemException;
	
	public ContentVersion getLastVersion(String contentId) throws ApsSystemException;
	
	public void saveContentVersion(String contentId) throws ApsSystemException;
	
	public void deleteVersion(long versionid) throws ApsSystemException;
	
	public void deleteWorkVersions(String contentId, int onlineVersion) throws ApsSystemException;
	
	public Content getContent(ContentVersion contentVersion) throws ApsSystemException;
	
}