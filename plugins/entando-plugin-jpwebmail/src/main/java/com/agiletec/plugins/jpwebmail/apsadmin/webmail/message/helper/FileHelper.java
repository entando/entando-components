/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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