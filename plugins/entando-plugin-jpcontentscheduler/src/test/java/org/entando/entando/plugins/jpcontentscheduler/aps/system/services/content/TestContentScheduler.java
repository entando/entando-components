package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content;

import org.entando.entando.plugins.jpcontentscheduler.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.parse.ContentThreadConfigDOM;

import com.agiletec.aps.system.exception.ApsSystemException;

public class TestContentScheduler extends ApsPluginBaseTestCase {

	private IContentSchedulerManager contentScheduler;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();

	}

	public void testMe() throws ApsSystemException {
		assertTrue(null != contentScheduler);
		ContentThreadConfig config = contentScheduler.getConfig();
		ContentThreadConfigDOM dd = new ContentThreadConfigDOM();
		String xml = dd.createConfigXml(config);
		System.out.println(xml);
	}

	public void init() throws Exception {
		this.contentScheduler = (IContentSchedulerManager) this.getApplicationContext().getBean("jpcontentschedulerContentSchedulerManager");
		super.setUp();
	}

}
