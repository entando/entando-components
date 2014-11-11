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
public class BaseStorageObject implements IStorageObject {
	
	public BaseStorageObject(String bucketName, String path, InputStream is) {
		this.setBucketName(bucketName);
		this.setStoragePath(path);
		this.setInputStream(is);
	}
	
	@Override
	public String getBucketName() {
		return _bucketName;
	}
	protected void setBucketName(String bucketName) {
		this._bucketName = bucketName;
	}
	
	@Override
	public String getStoragePath() {
		return _storagePath;
	}
	protected void setStoragePath(String storagePath) {
		this._storagePath = storagePath;
	}
	
	@Override
	public InputStream getInputStream() {
		return _inputStream;
	}
	protected void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	
	@Override
	public long getContentLength() {
		return _contentLength;
	}

	public void setContentLength(long contentLength) {
		this._contentLength = contentLength;
	}
	
	@Override
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	@Override
	public Map<String, String> getUserMetadata() {
		return _userMetadata;
	}
	public void setUserMetadata(Map<String, String> userMetadata) {
		this._userMetadata = userMetadata;
	}
	
	private String _bucketName;
	private String _storagePath;
	private InputStream _inputStream;
	private long _contentLength;
	private String _contentType;
	private Map<String, String> _userMetadata;
}
