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
package com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public interface ITagCloudManager {
	
	public Map<ITreeNode, Integer> getCloudInfos(UserDetails currentUser) throws ApsSystemException;
	
	public List<String> loadPublicTaggedContentsId(String categoryCode, UserDetails currentUser) throws ApsSystemException;
	
}