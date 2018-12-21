/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps;


import org.entando.entando.plugins.dashboard.DashboardConfigTestUtils;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;

public class DashboardBaseTestCase extends BaseTestCase {

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new DashboardConfigTestUtils();
	}

	
}
