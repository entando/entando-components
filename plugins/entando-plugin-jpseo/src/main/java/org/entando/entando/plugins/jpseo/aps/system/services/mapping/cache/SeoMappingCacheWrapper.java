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

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ContentFriendlyCode;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class SeoMappingCacheWrapper extends AbstractCacheWrapper implements ISeoMappingCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(SeoMappingCacheWrapper.class);
    
	@Override
	public void initCache(ISeoMappingDAO seoMappingDAO) throws ApsSystemException {
		try {
            Cache cache = this.getCache();
            this.releaseCachedObjects(cache, MAPPING_BY_CODE_CACHE_KEY, MAPPING_BY_CODE_CACHE_KEY_PREFIX);
            this.releaseCachedObjects(cache, MAPPING_BY_PAGE_CACHE_KEY, MAPPING_BY_PAGE_CACHE_KEY_PREFIX);
            this.releaseCachedObjects(cache, MAPPING_BY_CONTENT_CACHE_KEY, MAPPING_BY_CONTENT_CACHE_KEY_PREFIX);
            Map<String, FriendlyCodeVO> mapping = seoMappingDAO.loadMapping();
			Map<String, FriendlyCodeVO> pageFriendlyCodes = new HashMap<>();
			Map<String, ContentFriendlyCode> contentFriendlyCodes = new HashMap<>();
			Iterator<FriendlyCodeVO> codesIter = mapping.values().iterator();
			while (codesIter.hasNext()) {
				FriendlyCodeVO currentCode = codesIter.next();
				if (currentCode.getPageCode()!=null) {
					pageFriendlyCodes.put(currentCode.getPageCode(), currentCode);
				} else if (currentCode.getContentId()!=null) {
					String contentId = currentCode.getContentId();
					ContentFriendlyCode content = contentFriendlyCodes.get(contentId);
					if (content==null) {
						content = new ContentFriendlyCode();
						content.setContentId(contentId);
						contentFriendlyCodes.put(contentId, content);
					}
					content.addFriendlyCode(currentCode.getLangCode(), currentCode.getFriendlyCode());
				}
			}
            this.insertVoObjectsOnCache(cache, mapping, MAPPING_BY_CODE_CACHE_KEY, MAPPING_BY_CODE_CACHE_KEY_PREFIX);
            this.insertVoObjectsOnCache(cache, pageFriendlyCodes, MAPPING_BY_PAGE_CACHE_KEY, MAPPING_BY_PAGE_CACHE_KEY_PREFIX);
			this.insertVoObjectsOnCache(cache, contentFriendlyCodes, MAPPING_BY_CONTENT_CACHE_KEY, MAPPING_BY_CONTENT_CACHE_KEY_PREFIX);
		} catch (Throwable t) {
			_logger.error("Error loading seo mapper", t);
			throw new ApsSystemException("Error loading seo mapper", t);
		}
	}
    
    protected void releaseCachedObjects(Cache cache, String listKey, String prefixKey) {
		List<String> codes = (List<String>) this.get(cache, listKey, List.class);
		if (null != codes) {
			for (String code : codes) {
				cache.evict(prefixKey + code);
			}
			cache.evict(listKey);
		}
	}
    
    protected void insertVoObjectsOnCache(Cache cache, 
            Map<String, ?> objects, String listKey, String prefixKey) {
		List<String> codes = new ArrayList<>();
		Iterator<String> iter = objects.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			cache.put(prefixKey + key, objects.get(key));
			codes.add(key);
		}
		cache.put(listKey, codes);
	}
    
	@Override
	public FriendlyCodeVO getMappingByFriendlyCode(String friendlyCode) {
        return this.get(this.getCache(), MAPPING_BY_CODE_CACHE_KEY_PREFIX + friendlyCode, FriendlyCodeVO.class);
	}

    @Override
    public FriendlyCodeVO getMappingByPageCode(String pageCode) {
        return this.get(this.getCache(), MAPPING_BY_PAGE_CACHE_KEY_PREFIX+ pageCode, FriendlyCodeVO.class);
	}
    
    @Override
    public ContentFriendlyCode getMappingByContentId(String contentId) {
        return this.get(this.getCache(), MAPPING_BY_CONTENT_CACHE_KEY_PREFIX + contentId, ContentFriendlyCode.class);
	}

    @Override
    protected String getCacheName() {
        return ISeoMappingCacheWrapper.SEO_MAPPER_CACHE_NAME;
    }
	
}
