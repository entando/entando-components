/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.cache.IContentMapperCacheWrapper;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ContentFriendlyCode;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingDAO;

/**
 * @author E.Santoboni
 */
public interface ISeoMappingCacheWrapper {

	public static final String SEO_MAPPER_CACHE_NAME = IContentMapperCacheWrapper.CONTENT_MAPPER_CACHE_NAME;
	public static final String MAPPING_BY_CODE_CACHE_KEY = "SeoMappingManager_byCode";
    public static final String MAPPING_BY_CODE_CACHE_KEY_PREFIX = "SeoMappingManager_byCode_";
    public static final String MAPPING_BY_PAGE_CACHE_KEY = "SeoMappingManager_byPage";
    public static final String MAPPING_BY_PAGE_CACHE_KEY_PREFIX = "SeoMappingManager_byPage_";
    public static final String MAPPING_BY_CONTENT_CACHE_KEY = "SeoMappingManager_byContent";
    public static final String MAPPING_BY_CONTENT_CACHE_KEY_PREFIX = "SeoMappingManager_byContent_";
    
	public void initCache(ISeoMappingDAO seoMappingDAO) throws ApsSystemException;
    
    public FriendlyCodeVO getMappingByFriendlyCode(String friendlyCode);
    
    public FriendlyCodeVO getMappingByPageCode(String pageCode);
    
    public ContentFriendlyCode getMappingByContentId(String contentId);
    
}
