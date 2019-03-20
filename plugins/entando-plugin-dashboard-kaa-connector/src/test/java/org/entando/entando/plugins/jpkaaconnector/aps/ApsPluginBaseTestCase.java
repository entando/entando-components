package org.entando.entando.plugins.jpkaaconnector.aps;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;

import org.entando.entando.plugins.jpkaaconnector.PluginConfigTestUtils;

public class ApsPluginBaseTestCase extends BaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
}
