/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services;

import org.entando.entando.plugins.jpiot.aps.JpiotBaseTestCase;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IIotConfigManager;

public class TestIotConfigManager extends JpiotBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetIotConfig() {
		//TODO complete test
		assertNotNull(this._iotConfigManager);
	}

	public void testGetIotConfigs() {
		//TODO complete test
		assertNotNull(this._iotConfigManager);
	}
	
	public void testSearchIotConfigs() {
		//TODO complete test
		assertNotNull(this._iotConfigManager);
	}

	public void testAddIotConfig() {
		//TODO complete test
		assertNotNull(this._iotConfigManager);
	}

	public void testUpdateIotConfig() {
		//TODO complete test
		assertNotNull(this._iotConfigManager);
	}

	public void testDeleteIotConfig() {
		//TODO complete test
		assertNotNull(this._iotConfigManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._iotConfigManager = (IIotConfigManager) this.getService("jpiotIotConfigManager");
	}
	
	private IIotConfigManager _iotConfigManager;
}

