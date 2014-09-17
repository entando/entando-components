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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class FileHelper {
	
	public static void deleteDir(File dir, String dirPath) {
		if (dir.exists() || dir.isDirectory()) {
			String[] filesName = dir.list();
			for (int i=0; i<filesName.length; i++) {
				String path = dirPath + File.separator + filesName[i];
				File fileToDelete = new File(path);
				if (dir.exists() && fileToDelete.isDirectory()) {
					deleteDir(fileToDelete, path);
				} else {
					fileToDelete.delete();
				}
			}
			dir.delete();
		}
	}
	
	public static void saveFile(String filePath, InputStream is) throws ApsSystemException {
		try {
			File f = new File(filePath);
			OutputStream out = new FileOutputStream(f);
			byte buf[] = new byte[1024];
			int len;
			while((len = is.read(buf))>0)
			out.write(buf, 0, len);
			out.close();
			is.close();
		} catch (Throwable t) {
			throw new ApsSystemException("Errora in salvataggio file attachment su path " + filePath, t);
		}
	}
	
}