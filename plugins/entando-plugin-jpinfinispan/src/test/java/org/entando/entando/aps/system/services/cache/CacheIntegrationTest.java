package org.entando.entando.aps.system.services.cache;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import org.junit.Test;
import static org.entando.entando.aps.system.services.cache.ICacheInfoManager.DEFAULT_CACHE_NAME;

public class CacheIntegrationTest extends BaseTestCase {

    private CacheInfoManager cacheInfoManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cacheInfoManager = (CacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
    }

    @Test
    public void testCache() {
        assertNotNull(cacheInfoManager);

        // Check implementation class
        cacheInfoManager.getCaches().forEach(cache -> {
            String cacheImplName = cache.getNativeCache().getClass().getCanonicalName();
            assertTrue("Cache implementation was " + cacheImplName, cacheImplName.contains("infinispan"));
        });

        // Check insertion
        String value = "test_value";
        String key = "test_key";
        cacheInfoManager.putInCache(DEFAULT_CACHE_NAME, key, value);
        Object extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE_NAME, key);
        assertEquals(value, extracted);

        // Check removal
        this.cacheInfoManager.flushEntry(DEFAULT_CACHE_NAME, key);
        extracted = this.cacheInfoManager.getFromCache(DEFAULT_CACHE_NAME, key);
        assertNull(extracted);
    }
}
