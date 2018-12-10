/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services;

import org.entando.entando.plugins.jpiot.aps.JpiotBaseTestCase;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IIotListDevicesManager;

public class TestIotListDevicesManager extends JpiotBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetIotListDevices() {
		//TODO complete test
		assertNotNull(this._iotListDevicesManager);
	}

	public void testGetIotListDevicess() {
		//TODO complete test
		assertNotNull(this._iotListDevicesManager);
	}
	
	public void testSearchIotListDevicess() {
		//TODO complete test
		assertNotNull(this._iotListDevicesManager);
	}

	public void testAddIotListDevices() {
		//TODO complete test
		assertNotNull(this._iotListDevicesManager);
	}

	public void testUpdateIotListDevices() {
		//TODO complete test
		assertNotNull(this._iotListDevicesManager);
	}

	public void testDeleteIotListDevices() {
		//TODO complete test
		assertNotNull(this._iotListDevicesManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._iotListDevicesManager = (IIotListDevicesManager) this.getService("jpiotIotListDevicesManager");
	}
	
	private IIotListDevicesManager _iotListDevicesManager;
}

