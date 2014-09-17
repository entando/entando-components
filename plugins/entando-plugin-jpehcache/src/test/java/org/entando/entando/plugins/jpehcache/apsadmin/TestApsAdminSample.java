package org.entando.entando.plugins.jpehcache.apsadmin;

public class TestApsAdminSample extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void test() {
		assertTrue(true);
	}
	
	private void init() throws Exception {
    	try {
    		// init services
		} catch (Exception e) {
			throw e;
		}
    }
	
}
