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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.ContentMailInfo;

public interface IContentNotifierDAO {
	
	public void saveEvent(PublicContentChangedEvent event) throws ApsSystemException;
	
	public void signNotifiedContents(List<ContentMailInfo> contentsNotifiedInfo) throws ApsSystemException;
	
	public List<ContentMailInfo> getContentsToNotify() throws ApsSystemException;
	
}