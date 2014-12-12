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

import java.util.List;

import org.entando.entando.plugins.jpcomponentinstaller.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpcomponentinstaller.aps.system.InstallerSystemConstants;

/**
 * @author E.Santoboni
 */
public class TestComponentCatalogueManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testGetArtifacts() {
		assertNotNull(this._componentCatalogueManager);
		List<AvailableArtifact> artifacts = this._componentCatalogueManager.getArtifacts();
		assertNotNull(artifacts);
		assertTrue(artifacts.size() > 0);
	}
	
	private void init() throws Exception {
    	try {
    		this._componentCatalogueManager = (IComponentCatalogueManager) this.getService(InstallerSystemConstants.COMPONENT_CATALOGUE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IComponentCatalogueManager _componentCatalogueManager = null;
    
}
