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
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.xml.sax.SAXException;

/**
 * @author M.Casari
 */
public interface IPluginInstaller {
        
    public void install(AvailableArtifact availableArtifact, String version, InputStream is) throws ApsSystemException;
    
}
