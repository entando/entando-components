/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.List;

/**
 * @author E.Santoboni
 */
public interface IArtifactInstallerManager {
    
    public List<String> findAvailableVersions(Integer availableComponentId) throws ApsSystemException;
	
	public boolean downloadArtifact(Integer availableComponentId, String version) throws ApsSystemException;
    
    public boolean installArtifact(Integer availableComponentId, String version) throws ApsSystemException;
	
	public boolean uninstallArtifact(Integer availableComponentId) throws ApsSystemException;
	
	//public boolean uninstallArtifact(Component component) throws ApsSystemException;
	
}
