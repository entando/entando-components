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
package com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.net.www.protocol.file.FileURLConnection;
import sun.net.www.protocol.ftp.FtpURLConnection;

import com.agiletec.aps.system.ApsSystemUtils;

public class RemoteResourceLoaderActionHelper {

	public static URLConnection getRemoteResourceConnection(String url, String defaultStorageDirPath) throws Throwable {
		if (!url.startsWith("http://") && !url.startsWith("ftp://")) {
			if (url.startsWith("/")) url = url.substring(1);
			url = "file://" + defaultStorageDirPath + url;
		}
		//System.out.println(url);
		URL myFileUrl = new URL(url);          
		String protocol = myFileUrl.getProtocol();
		//System.out.println("Protocol: " + protocol);
		URLConnection conn = null;
		if (protocol.equals("http")) {
			conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);			
		} else if (protocol.equals("ftp")) {
			conn = (FtpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
		} else if (protocol.equals("file")) {
			conn = (FileURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);						
		}
		return conn;
	}
	
	public static String extractFilenameFromUrl(String url) {
		String filename = null;
		String[] tokens = url.split("/");
		String fname = tokens[tokens.length - 1];
		if (null != fname && fname.trim().length() > 0) {
			String regExp = "^(\\w.*)\\.(\\w.*)";
			Pattern pattern = Pattern.compile(regExp);
			Matcher codeMatcher = pattern.matcher("");
			codeMatcher.reset(fname);
			if (codeMatcher.matches()) {
				filename = fname;
			}
		}
		return filename;
	}
	
	public static File createFileFromInputStream(InputStream inputStream, String filename) {
		File file = null;
		try {
			file = new File(filename);
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while((len = inputStream.read(buf))>0)
				out.write(buf,0,len);
			out.close();
			inputStream.close();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("error on create File from InputStream");
		}
		return file;
	}
	
}