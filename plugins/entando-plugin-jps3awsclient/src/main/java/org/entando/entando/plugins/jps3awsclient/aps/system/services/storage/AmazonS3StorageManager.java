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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jps3awsclient.aps.system.S3ClientSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.FileTextReader;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.StorageClass;
import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;

/**
 * @author E.Santoboni
 */
public class AmazonS3StorageManager implements IStorageManager, BeanFactoryAware {

	private static final Logger _logger =  LoggerFactory.getLogger(AmazonS3StorageManager.class);
	
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void saveFile(String subPath, boolean isProtectedResource, InputStream is) throws ApsSystemException {
		if (!this.isActive()) {
			return;
		}
		try {
			String key = this.getKey(subPath, isProtectedResource);
			byte[] bytes = this.getBytes(is);
			InputStream isClone = new ByteArrayInputStream(bytes); 
			BaseStorageObject bso = new BaseStorageObject(this.getBucketName(), key, isClone);
			bso.setContentLength(bytes.length);
			String filename = subPath.substring(subPath.lastIndexOf('/') + 1).trim();
			String contentType = URLConnection.guessContentTypeFromName(filename);
			bso.setContentType(contentType);
			CannedAccessControlList acl = (isProtectedResource) ? CannedAccessControlList.Private : CannedAccessControlList.PublicRead;
			this.store(bso, isProtectedResource, acl);
		} catch (Throwable t) {
			_logger.error("Error saving files", t);
			throw new ApsSystemException("Error saving files", t);
		}
	}
	
	@Override
	public boolean deleteFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		if (!this.isActive()) {
			return false;
		}
		String key = this.getKey(subPath, isProtectedResource);
		this.delete(this.getBucketName(), key);
		return true;
	}
	
	@Override
	public void createDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException {
		// not used
	}
	
	@Override
	public void deleteDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException {
		if (!this.isActive()) {
			return;
		}
		String key = this.getKey(subPath, isProtectedResource);
		this.delete(this.getBucketName(), key);
	}
	
	@Override
	public InputStream getStream(String subPath, boolean isProtectedResource) throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		String key = this.getKey(subPath, isProtectedResource);
		S3Object object = this.getS3Object(this.getBucketName(), key, true);
		if (null != object) {
			return object.getObjectContent();
		}
		return null;
	}
	
	@Override
	public BasicFileAttributeView getAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		BasicFileAttributeView bfav = null;
		if (!this.isActive()) {
			return bfav;
		}
		String key = this.getKey(subPath, isProtectedResource);
		S3Object object = this.getS3Object(this.getBucketName(), key, true);
		if (null != object) {
			bfav = new BasicFileAttributeView();
			bfav.setName(object.getKey());
			bfav.setSize(object.getObjectMetadata().getContentLength());
			bfav.setDirectory(false);
			bfav.setLastModifiedTime(object.getObjectMetadata().getLastModified());
		}
		return bfav;
	}
	
	@Override
	public String getResourceUrl(String subPath, boolean isProtectedResource) {
		if (!this.isActive()) {
			return null;
		}
		String key = this.getKey(subPath, isProtectedResource);
		return this.getResourceUrl(this.getBucketName(), key);
	}
	
	@Override
	public boolean exists(String subPath, boolean isProtectedResource) throws ApsSystemException {
		if (!this.isActive()) {
			return false;
		}
		return (null != this.getStream(subPath, isProtectedResource));
	}
	
	private String getKey(String subPath, boolean isProtectedResource) {
		String bucketFolder = (isProtectedResource) ? S3ClientSystemConstants.PROTECTED_BUCHET_FOLDER : S3ClientSystemConstants.PUBLIC_BUCHET_FOLDER;
		StringBuilder path = new StringBuilder(bucketFolder);
		if (!subPath.startsWith("/")) {
			path.append("/");
		}
		path.append(subPath);
		return path.toString();
	}
	
	public void store(IStorageObject obj, boolean reducedRedundancy, CannedAccessControlList acl) throws ApsSystemException {
		try {
			AmazonS3Client client = this.getS3Client();
			String bucketName = obj.getBucketName().toLowerCase();
			this.checkForAndCreateBucket(bucketName, client);
			ObjectMetadata omd = new ObjectMetadata();
			omd.setContentType(obj.getContentType());
			omd.setContentLength(obj.getContentLength());
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, obj.getStoragePath(), obj.getInputStream(), omd);
			// Check if reduced redundancy is enabled
			if (reducedRedundancy) {
				putObjectRequest.setStorageClass(StorageClass.ReducedRedundancy);
			}
			if (null != obj.getUserMetadata()) {
				ObjectMetadata objectMetadata = new ObjectMetadata();
				putObjectRequest.setMetadata(objectMetadata);
				Iterator<String> iter = obj.getUserMetadata().keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					objectMetadata.addUserMetadata(key, obj.getUserMetadata().get(key));
				}
			}
			client.putObject(putObjectRequest);
			// If we have an ACL set access permissions for the the data on S3
			if (acl != null) {
				client.setObjectAcl(bucketName, obj.getStoragePath(), acl);
			}
		} catch (Throwable t) {
			_logger.error("Error storing object", t);
			throw new ApsSystemException("Error storing object", t);
		}
	}
	
	public void checkForAndCreateBucket(String bucketName, AmazonS3Client client) {
		// Make sure it's lower case to comply with Amazon S3 recommendations
		bucketName = bucketName.toLowerCase();
		if (this._bucketMap.get(bucketName) == null) {
			if (client.doesBucketExist(bucketName)) {
				this._bucketMap.put(bucketName, true);
			} else {
				// Bucket hasn't been created yet so we create it
				CreateBucketRequest request = new CreateBucketRequest(bucketName);
				request.withCannedAcl(CannedAccessControlList.LogDeliveryWrite);
			    client.createBucket(request);
				this._bucketMap.put(bucketName, true);
			}
		}
	}
	
	public InputStream loadInputStream(String bucketName, String key) throws ApsSystemException {
		return this.loadInputStream(bucketName, key, false);
	}
	
	public InputStream loadInputStream(String bucketName, String key, boolean canBeNull) throws ApsSystemException {
		InputStream stream = null;
		try {
			AmazonS3Client client = this.getS3Client();
			S3Object s3Object = client.getObject(bucketName, key);
			InputStream mainIs = s3Object.getObjectContent();
			byte[] bytes = this.getBytes(mainIs);
			stream = new ByteArrayInputStream(bytes);
		} catch (Throwable t) {
			if (!canBeNull) {
				_logger.error("Error loading inputstream : bucket {} - key {}",bucketName, key, t);
				throw new ApsSystemException("Error loading inputstream : bucket " + bucketName + " - key " + key, t);
			}
		}
		return stream;
	}
	
	private byte[] getBytes(InputStream is) throws Throwable {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			int len;
			int size = 1024;
			byte[] buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
		} catch (Throwable t) {
			_logger.error("error copying stream", t);
			throw t;
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return bos.toByteArray();
	}
	
	public List<Bucket> getBuckets() throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		try {
			return this.getS3Client().listBuckets();
		} catch (Throwable t) {
			_logger.error("error in getBuckets", t);
			throw new ApsSystemException("Error", t);
		}
	}
	
	@Override
	public String[] listDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.list(subPath, isProtectedResource, true, false);
	}
	
	@Override
	public BasicFileAttributeView[] listDirectoryAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.listAttributes(subPath, isProtectedResource, true, false);
	}
	
	@Override
	public String[] listFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.list(subPath, isProtectedResource, false, true);
	}
	
	@Override
	public BasicFileAttributeView[] listFileAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.listAttributes(subPath, isProtectedResource, false, true);
	}
	
	@Override
	public String[] list(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.list(subPath, isProtectedResource, true, true);
	}
	
	@Override
	public BasicFileAttributeView[] listAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.listAttributes(subPath, isProtectedResource, true, true);
	}
	
	private String[] list(String subPath, boolean isProtectedResource, boolean addFolders, boolean addFiles) throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		String folder = this.getKey(subPath, isProtectedResource);
		ObjectListing objectListing = this.getS3Objects(folder);
		if (null == objectListing) {
			return null;
		}
		String[] objects = new String[]{};
		if (null != objectListing.getCommonPrefixes() && addFolders) {
			for (int i = 0; i < objectListing.getCommonPrefixes().size(); i++) {
				String object = objectListing.getCommonPrefixes().get(i);
				String name = object.substring(folder.length(), (object.length() - 1));
				objects = this.addChild(name, objects);
			}
		}
		if (null != objectListing.getObjectSummaries() && addFiles) {
			for (int i = 0; i < objectListing.getObjectSummaries().size(); i++) {
				S3ObjectSummary s3os = objectListing.getObjectSummaries().get(i);
				String key = s3os.getKey();
				String name = key.substring(folder.length());
				objects = this.addChild(name, objects);
			}
		}
		return objects;
	}
	
	private BasicFileAttributeView[] listAttributes(String subPath, boolean isProtectedResource, boolean addFolders, boolean addFiles) throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		String folder = this.getKey(subPath, isProtectedResource);
		ObjectListing objectListing = this.getS3Objects(folder);
		if (null == objectListing) {
			return null;
		}
		BasicFileAttributeView[] objects = new BasicFileAttributeView[]{};
		if (null != objectListing.getCommonPrefixes() && addFolders) {
			for (int i = 0; i < objectListing.getCommonPrefixes().size(); i++) {
				String object = objectListing.getCommonPrefixes().get(i);
				String name = object.substring(folder.length(), (object.length() - 1));
				BasicFileAttributeView bfav = new BasicFileAttributeView();
				bfav.setDirectory(true);
				bfav.setName(name);
				objects = this.addChildAttribute(bfav, objects);
			}
		}
		if (null != objectListing.getObjectSummaries() && addFiles) {
			for (int i = 0; i < objectListing.getObjectSummaries().size(); i++) {
				S3ObjectSummary s3os = objectListing.getObjectSummaries().get(i);
				String key = s3os.getKey();
				String name = key.substring(folder.length());
				BasicFileAttributeView bfav = new BasicFileAttributeView();
				bfav.setDirectory(false);
				bfav.setName(name);
				bfav.setLastModifiedTime(s3os.getLastModified());
				bfav.setSize(s3os.getSize());
				objects = this.addChildAttribute(bfav, objects);
			}
		}
		return objects;
	}
	
	protected String[] addChild(String stringToAdd, String[] objects) {
		int len = objects.length;
		String[] newArray = new String[len + 1];
		for (int i=0; i < len; i++){
			newArray[i] = objects[i];
		}
		newArray[len] = stringToAdd;
		return newArray;
	}
	
	protected BasicFileAttributeView[] addChildAttribute(BasicFileAttributeView toAdd, BasicFileAttributeView[] objects) {
		int len = objects.length;
		BasicFileAttributeView[] newArray = new BasicFileAttributeView[len + 1];
		for (int i=0; i < len; i++){
			newArray[i] = objects[i];
		}
		newArray[len] = toAdd;
		return newArray;
	}
	
	public S3Object getS3Object(String bucketName, String key, boolean ignoreErrors) throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		S3Object s3Object = null;
		try {
			AmazonS3Client client = this.getS3Client();
			s3Object = client.getObject(bucketName, key);
		} catch (Throwable t) {
			if (!ignoreErrors) {
				_logger.error("error in getS3Object", t);
				throw new ApsSystemException("Error", t);
			} else {
				return null;
			}
		}
		return s3Object;
	}
	
	public ObjectListing getS3Objects(String prefix) throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		if (prefix == null || prefix.trim().length() == 0) {
			return null;
		}
		try {
			String key = prefix;
			if (!key.endsWith("/")) {
				key += "/";
			}
			ListObjectsRequest lor = new ListObjectsRequest();
			lor.setBucketName(this.getBucketName());
			lor.setPrefix(key);
			lor.setDelimiter("/");
			AWSCredentials creds = new BasicAWSCredentials(this.getCredentialKey(), this.getCredentialSecret());
			lor.setRequestCredentials(creds);
			lor.setPrefix(key);
			return this.getS3Client().listObjects(lor);
		} catch (Throwable t) {
			_logger.error("error in getS3Objects", t);
			throw new ApsSystemException("Error", t);
		}
	}
	
	public ObjectListing getS3Objects(ListObjectsRequest lor) throws ApsSystemException {
		if (!this.isActive()) {
			return null;
		}
		try {
			return this.getS3Client().listObjects(lor);
		} catch (Throwable t) {
			_logger.error("error in getS3Objects", t);
			throw new ApsSystemException("Error", t);
		}
	}
	
	public void delete(String bucketName, String key) throws ApsSystemException {
		if (!this.isActive()) {
			return;
		}
		try {
			if (key == null || key.trim().length() == 0) {
				_logger.warn("Empty storage path passed to delete method");
				return; // We don't want to delete everything in a path
			}
			AmazonS3Client client = this.getS3Client();
			// Go through the store structure and delete child objects
			ObjectListing listing = this.getS3Client().listObjects(bucketName, key);
			while (true) {
				List<S3ObjectSummary> objectList = listing.getObjectSummaries();
				for (int i = 0; i < objectList.size(); i++) {
					S3ObjectSummary summary = objectList.get(i);
					client.deleteObject(bucketName, summary.getKey());
				}
				if (listing.isTruncated()) {
					listing = client.listNextBatchOfObjects(listing);
				} else {
					break;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error deleting objects : bucket {} - key {}", bucketName, key, t);
			throw new ApsSystemException("Error deleting objects : bucket " + bucketName + " - key " + key, t);
		}
	}
	
	public String getResourceUrl(String bucketName, String key) {
	    if (!this.isActive()) {
			return null;
		}
		return this.getS3Client().getResourceUrl(bucketName, key);
	}
	
	@Override
	public String readFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		InputStream is = this.getStream(subPath, isProtectedResource);
		if (null == is) {
			return null;
		}
		return FileTextReader.getText(is);
	}
	
	@Override
	public void editFile(String subPath, boolean isProtectedResource, InputStream is) throws ApsSystemException {
		this.saveFile(subPath, isProtectedResource, is);
	}
	
	public boolean isActive() {
		try {
			String activeString = this.getConfigParam(S3ClientSystemConstants.AMAZONS3_ACTIVE_PARAM);
			Boolean active = Boolean.parseBoolean(activeString);
			return active.booleanValue();
		} catch (Throwable t) {}
		return false;
	}
	
	protected String getCredentialKey() {
        return (String) this.getConfigParam(S3ClientSystemConstants.AMAZONS3_ACCESS_KEY_PARAM);
    }
	
	protected String getCredentialSecret() {
        return (String) this.getConfigParam(S3ClientSystemConstants.AMAZONS3_SECRET_KEY_PARAM);
    }
	
    private String getConfigParam(String paramName) {
        String paramValue = this.getConfigManager().getParam(paramName);
        if (null == paramValue || paramValue.trim().length() == 0) {
			return null;
		}
        return paramValue.trim();
    }
	
	protected AmazonS3Client getS3Client() {
		if (!this.isActive()) {
			return null;
		}
		AWSCredentials creds = new BasicAWSCredentials(this.getCredentialKey(), this.getCredentialSecret());
		return new AmazonS3Client(creds);
	}
	
	protected String getBucketName() {
		String bucketName = (String) this.getConfigParam(S3ClientSystemConstants.AMAZONS3_BUCKET_NAME_PARAM);
		if (null == bucketName || bucketName.trim().length() == 0) {
			bucketName = DEFAULT_BUCHET_NAME;
		}
		return bucketName;
	}
	
	protected ConfigInterface getConfigManager() {
		return (ConfigInterface) this.getBeanFactory().getBean(SystemConstants.BASE_CONFIG_MANAGER);
	}
	
	protected BeanFactory getBeanFactory() {
		return this._beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private static Map<String,Boolean> _bucketMap = new LinkedHashMap<String, Boolean>();
	
	private BeanFactory _beanFactory;
	
	private static final String DEFAULT_BUCHET_NAME = "defaultbucket";
	
}