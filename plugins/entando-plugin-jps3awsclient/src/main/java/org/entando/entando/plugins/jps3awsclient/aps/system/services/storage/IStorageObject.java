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
package org.entando.entando.plugins.jps3awsclient.aps.system.services.storage;

import java.io.InputStream;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public interface IStorageObject {
	
	public String getBucketName();
	
	public InputStream getInputStream();
	
	public String getStoragePath();
	
	public long getContentLength();
	
	public String getContentType();
	
	public Map<String, String> getUserMetadata();
	
}
