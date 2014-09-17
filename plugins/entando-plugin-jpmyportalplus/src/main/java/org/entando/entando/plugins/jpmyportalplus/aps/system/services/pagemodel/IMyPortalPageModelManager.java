/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
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
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public interface IMyPortalPageModelManager {
	
	public Map<Integer, MyPortalFrameConfig> getPageModelConfig(String name);
	
	public void updateModelConfig(String code, Map<Integer, MyPortalFrameConfig> configuration) throws ApsSystemException;
	
}
