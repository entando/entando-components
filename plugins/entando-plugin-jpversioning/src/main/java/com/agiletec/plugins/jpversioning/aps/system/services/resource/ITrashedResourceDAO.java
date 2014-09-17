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
package com.agiletec.plugins.jpversioning.aps.system.services.resource;

import java.util.List;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;

public interface ITrashedResourceDAO {
	
	public ResourceRecordVO getTrashedResource(String id);
	
	public void addTrashedResource(ResourceInterface resource);
	
	public void delTrashedResource(String id);
	
	public List<String> searchTrashedResourceIds(String resourceTypeCode, String text, List<String> allowedGroups);
	
}