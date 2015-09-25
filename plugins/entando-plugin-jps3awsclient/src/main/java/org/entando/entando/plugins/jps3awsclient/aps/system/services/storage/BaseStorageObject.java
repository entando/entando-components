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
