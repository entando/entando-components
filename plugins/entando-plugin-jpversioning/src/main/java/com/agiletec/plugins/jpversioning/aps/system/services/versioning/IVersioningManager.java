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
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import com.agiletec.aps.system.common.entity.IEntityManager;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public interface IVersioningManager {

	static final String VERSION_ID_FILTER_KEY = "id";
	static final String VERSION_CONTENT_TYPE_FILTER_KEY = "contentType";
	static final String VERSION_DESCRIPTION_FILTER_KEY = "description";

	static final String[] METADATA_FILTER_KEYS = {VERSION_ID_FILTER_KEY, VERSION_CONTENT_TYPE_FILTER_KEY,
			VERSION_DESCRIPTION_FILTER_KEY};

	public List<Long> getVersions(String contentId) throws ApsSystemException;
	
	public List<Long> getLastVersions(String contentType, String descr) throws ApsSystemException;
	
	public ContentVersion getVersion(long id) throws ApsSystemException;
	
	public ContentVersion getLastVersion(String contentId) throws ApsSystemException;
	
	public void saveContentVersion(String contentId) throws ApsSystemException;
	
	public void deleteVersion(long versionid) throws ApsSystemException;
	
	public void deleteWorkVersions(String contentId, int onlineVersion) throws ApsSystemException;
	
	public Content getContent(ContentVersion contentVersion) throws ApsSystemException;
	
}