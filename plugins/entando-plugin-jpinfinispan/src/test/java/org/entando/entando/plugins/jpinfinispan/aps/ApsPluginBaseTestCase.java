package org.entando.entando.plugins.jpinfinispan.aps;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;
import org.entando.entando.plugins.jpinfinispan.PluginConfigTestUtils;

public class ApsPluginBaseTestCase extends BaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
}
