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
package org.entando.entando.plugins.jpfileattribute.aps.system.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jpfileattribute.aps.system.entity.model.FileAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
@Aspect
public class FilePersistenceManager extends AbstractService implements IFilePersistenceManager {

	private static final Logger _logger =  LoggerFactory.getLogger(FilePersistenceManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.common.entity.AbstractEntityDAO.addEntity(..)) && args(entity,..)")
    public void addEntity(Object entity) {
		this.updateEntityFiles((IApsEntity) entity);
    }
	
    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.common.entity.AbstractEntityDAO.updateEntity(..)) && args(entity,..)")
    public void updateEntity(Object entity) {
		this.updateEntityFiles((IApsEntity) entity);
    }
	
	private void updateEntityFiles(IApsEntity apsEntity) {
		try {
			List<Integer> ids = this.getFilesByEntity(apsEntity);
			this.getFilePersistenceDAO().updateEntityFiles(apsEntity.getId(), ids);
		} catch (Throwable t) {
			_logger.error("Error updating files by entity {}", apsEntity.getId(), t);
		}
    }
	
    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.common.entity.AbstractEntityDAO.deleteEntity(..)) && args(entityId,..)")
    public void deleteEntity(Object entityId) {
		try {
			FieldSearchFilter filter = new FieldSearchFilter("entityid", entityId.toString(), false);
			FieldSearchFilter[] filters = {filter};
			List<String> ids = this.getFilePersistenceDAO().searchId(filters);
			if (null != ids && !ids.isEmpty()) {
				for (int i = 0; i < ids.size(); i++) {
					String idString = ids.get(i);
					this.deleteFile(Integer.parseInt(idString));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error deleting files by entity {}", entityId, t);
		}
    }
	
	private List<Integer> getFilesByEntity(IApsEntity entity) {
		List<Integer> ids = new ArrayList<Integer>();
		if (null == entity) {
			return ids;
		}
		EntityAttributeIterator attributeIter = new EntityAttributeIterator(entity);
		while (attributeIter.hasNext()) {
			AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
			if (currAttribute instanceof FileAttribute) {
				FileAttribute fileAttribute = (FileAttribute) currAttribute;
				if (null != fileAttribute.getAttachedFile()) {
					ids.add(fileAttribute.getAttachedFile().getId());
				}
			}
		}
		return ids;
	}
	
	@Override
	public synchronized int addFile(AttachedFile file) throws ApsSystemException {
		int id = -1;
		String path = null;
		try {
			id = this.getFilePersistenceDAO().extractNexId();
			file.setId(id);
			this.getFilePersistenceDAO().addFile(file);
			path = this.getFilePath(file);
			InputStream is = new ByteArrayInputStream(file.getBase64());
			this.getStorageManager().saveFile(path, true, is);
		} catch (Throwable t) {
			if (null != path) {
				this.getStorageManager().deleteFile(path, true);
			}
			this.getFilePersistenceDAO().deleteFile(id);
			_logger.error("Error adding a file", t);
			throw new ApsSystemException("Error adding a file", t);
		}
		return id;
	}
	
	protected String getFilePath(AttachedFile file) {
		StringBuilder urlPath = new StringBuilder();
		urlPath.append(this.getFolder()).append(File.separator).append(file.getId()).append(File.separator);
		urlPath.append(file.getFilename());
		return urlPath.toString();
	}
    
	@Override
	public AttachedFile loadFile(Integer id) throws ApsSystemException {
		AttachedFile attachedFile = null;
		try {
			attachedFile = this.getFilePersistenceDAO().loadFile(id);
			if (null != attachedFile) {
				String path = this.getFilePath(attachedFile);
				InputStream is = this.getStorageManager().getStream(path, true);
				if (null != is) {
					attachedFile.setBase64(this.streamToByteArray(is));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading file with id {}", id, t);
			throw new ApsSystemException("Error loading file with id " + id, t);
		}
		return attachedFile;
	}
	
	private byte[] streamToByteArray(InputStream is) throws Throwable {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			for (int readNum; (readNum = is.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
		} catch (IOException ex) {
			_logger.error("Error creating byte array", ex);
			throw new ApsSystemException("Error creating byte array", ex);
		}
		return bos.toByteArray();
	}
	
	@Override
	public void deleteFile(Integer id) throws ApsSystemException {
		try {
			AttachedFile attachedFile = this.getFilePersistenceDAO().loadFile(id);
			if (null != attachedFile) {
				String path = this.getFilePath(attachedFile);
				this.getStorageManager().deleteFile(path, true);
			}
		} catch (Throwable t) {
			_logger.error("Error deleting file with id {}", id, t);
			throw new ApsSystemException("Error deleting file with id " + id, t);
		}
	}
	
	protected String getFolder() {
		if (null == this._folder) {
			return DEFAULT_FOLDER;
		}
		return _folder;
	}
	public void setFolder(String folder) {
		if (!folder.endsWith("/")) {
			folder += "/";
		}
		this._folder = folder;
	}
	
	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}
	
	protected IFilePersistenceDAO getFilePersistenceDAO() {
		return _filePersistenceDAO;
	}
	public void setFilePersistenceDAO(IFilePersistenceDAO filePersistenceDAO) {
		this._filePersistenceDAO = filePersistenceDAO;
	}
	
	private String _folder;
	
	private IStorageManager _storageManager;
	private IFilePersistenceDAO _filePersistenceDAO;
	
	private static final String DEFAULT_FOLDER = "plugins/jpfileattribute/";
	
}