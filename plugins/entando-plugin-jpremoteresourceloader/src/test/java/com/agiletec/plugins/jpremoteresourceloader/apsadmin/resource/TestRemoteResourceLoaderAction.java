package com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.agiletec.plugins.jpremoteresourceloader.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jpremoteresourceloader.apsadmin.resource.RemoteResourceLoaderAction;
import com.opensymphony.xwork2.Action;

public class TestRemoteResourceLoaderAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testNew() throws Throwable {
		String result = this.executeNew();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testAddValidation() throws Throwable {

		//	no url param
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.setUserOnSession("admin");
		this.addParameter("ResourceTypeCode", "Image");
		this.addParameter("strutsAction", "1");
		this.addParameter("descr", "Image");
		this.addParameter("alternateName", "Image");
		this.addParameter("mainGroup", "free");
		
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		
		RemoteResourceLoaderAction action = (RemoteResourceLoaderAction) this.getAction();
		assertNotNull(action);
		
		Collection<String> actionErrors = action.getActionErrors();
		assertNotNull(actionErrors);
		assertEquals(0, actionErrors.size());

		Map<String,List<String>> fieldErrors = action.getFieldErrors();
		assertNotNull(fieldErrors);
		assertEquals(1, fieldErrors.size());
		
		// no well formed url
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.setUserOnSession("admin");
		this.addParameter("ResourceTypeCode", "Image");
		this.addParameter("strutsAction", "1");
		this.addParameter("descr", "Image");
		this.addParameter("alternateName", "Image");
		this.addParameter("mainGroup", "free");
		this.addParameter("url", "fakeURL");
		
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		
		action = (RemoteResourceLoaderAction) this.getAction();
		assertNotNull(action);
		
		actionErrors = action.getActionErrors();
		assertNotNull(actionErrors);
		assertEquals(0, actionErrors.size());

		fieldErrors = action.getFieldErrors();
		assertNotNull(fieldErrors);
		assertEquals(1, fieldErrors.size());
		
		// not valid url
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.setUserOnSession("admin");
		this.addParameter("ResourceTypeCode", "Image");
		this.addParameter("strutsAction", "1");
		this.addParameter("descr", "Image");
		this.addParameter("alternateName", "Image");
		this.addParameter("mainGroup", "free");
		this.addParameter("url", "http://www.google.com/entando/resources/static/img/headerLogo_background.jpg");
		
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		
		action = (RemoteResourceLoaderAction) this.getAction();
		assertNotNull(action);
		
		actionErrors = action.getActionErrors();
		assertNotNull(actionErrors);
		assertEquals(0, actionErrors.size());

		fieldErrors = action.getFieldErrors();
		assertNotNull(fieldErrors);
		assertEquals(1, fieldErrors.size());
		
	}

	public void testSaveHttp() throws Throwable {
		String result = this.executeSaveFromHttp(NAME_DESCR);	
		assertEquals(Action.SUCCESS, result);
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		assertEquals(1, resIds.size());
	}

	public void testSaveFtp() throws Throwable {
		String result = this.executeSaveFromFtp(NAME_DESCR);
		assertEquals(Action.SUCCESS, result);
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		assertEquals(1, resIds.size());
	}

	public void testSaveFile() throws Throwable {
		File file = new File("admin/test/entando_logo.jpg");
		File destFile = new File("admin/test/jpremoteresourceloader_entando_logo.jpg");
		FileUtils.copyFile(file, destFile, true);
		String result = this.executeSaveFromFile(NAME_DESCR, destFile.getAbsolutePath());	
		assertEquals(Action.INPUT, result);
//		assertEquals(Action.SUCCESS, result);
//		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
//		assertEquals(1, resIds.size());
	}

	public void testSaveFileFromDefaultDir_1() throws Throwable {
		String result = this.executeSaveFromDefaultDir(NAME_DESCR, "label.jpg");	
		assertEquals(Action.SUCCESS, result);
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		assertEquals(1, resIds.size());
	}
	public void testSaveFileFromDefaultDir_2() throws Throwable {
		String result = this.executeSaveFromDefaultDir(NAME_DESCR, "/label.jpg");	
		assertEquals(Action.SUCCESS, result);
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		assertEquals(1, resIds.size());
	}
	public void testSaveFileFromDefaultDir_3() throws Throwable {
		String result = this.executeSaveFromDefaultDir(NAME_DESCR, "Immagini/teschio.png");	
		assertEquals(Action.SUCCESS, result);
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		assertEquals(1, resIds.size());
	}
	public void testSaveFileFromDefaultDir_4() throws Throwable {
		String result = this.executeSaveFromDefaultDir(NAME_DESCR, "/Immagini/teschio.png");	
		assertEquals(Action.SUCCESS, result);
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		assertEquals(1, resIds.size());
	}

	private String executeNew() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpremoteresourceloader/Resource", "new");
		return this.executeAction();
	}

	private String executeSaveFromHttp(String nameDescr) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.addParameter("url", "http://www.japsportal.org/jAPSPortal/resources/cms/images/box3Interno_1_d0.jpg");
		this.addParameter("alternateName", nameDescr);
		this.addParameter("descr", nameDescr);
		this.addParameter("resourceTypeCode", "Image");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("mainGroup", "free");
		return this.executeAction();
	}

	private String executeSaveFromFtp(String nameDescr) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.addParameter("url", "ftp://spuddu:12345678@spuddu.altervista.org/tipa.png");
		this.addParameter("alternateName", nameDescr);
		this.addParameter("descr", nameDescr);
		this.addParameter("resourceTypeCode", "Image");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("mainGroup", "free");
		return this.executeAction();
	}

	private String executeSaveFromFile(String nameDescr, String path) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.addParameter("url", "file://" + path);
		this.addParameter("alternateName", nameDescr);
		this.addParameter("descr", nameDescr);
		this.addParameter("resourceTypeCode", "Image");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("mainGroup", "free");
		return this.executeAction();
	}

	private String executeSaveFromDefaultDir(String nameDescr, String path) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpremoteresourceloader/Resource", "save");
		this.addParameter("url", path);
		this.addParameter("alternateName", nameDescr);
		this.addParameter("descr", nameDescr);
		this.addParameter("resourceTypeCode", "Image");
		this.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
		this.addParameter("mainGroup", "free");
		return this.executeAction();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		List<String> resIds = this._resourceManager.searchResourcesId("Image", NAME_DESCR, null, null);
		if (null != resIds && resIds.size() > 0) {
			String resId = resIds.get(0);
			this._resourceManager.deleteResource(this._resourceManager.loadResource(resId));
			File destFile = new File("admin/test/jpremoteresourceloader_entando_logo.jpg");
			if (destFile.exists()) {
				FileUtils.forceDelete(destFile);
			}
		}
	}
	
	private void init() {
		this._resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
	}
	
	private IResourceManager _resourceManager;
	private static final String  NAME_DESCR = "testFile";
	
}