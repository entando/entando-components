/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.plugins.jpkiebpm.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.IKieBpmService;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.model.DatatableWidgetConfigDto;
import org.entando.entando.plugins.jpkiebpm.web.model.DatatableWidgetConfigRequest;
import org.json.JSONArray;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class KieBpmServiceIntegrationTest extends ApsPluginBaseTestCase {

    private IKieFormManager formManager;

    private IKieBpmService kieService;

    private IDataObjectManager datatypeManager;

    public IKieFormManager getFormManager() {
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public IKieBpmService getKieService() {
        return kieService;
    }

    public void setKieService(IKieBpmService kieService) {
        this.kieService = kieService;
    }

    public IDataObjectManager getDatatypeManager() {
        return datatypeManager;
    }

    public void setDatatypeManager(IDataObjectManager datatypeManager) {
        this.datatypeManager = datatypeManager;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
        KieBpmConfig config = new KieBpmConfig();
        config.setActive(Boolean.TRUE);
        config.setDebug(Boolean.TRUE);
        config.setHostname(HOSTNAME);
        config.setName("newconfig");
        config.setPassword(PASSWORD);
        config.setPort(PORT);
        config.setSchema(SCHEMA);
        config.setTimeoutMsec(TIMEOUT);
        config.setUsername(USERNAME);
        config.setWebapp(WEBAPP);
        this.getFormManager().addConfig(config);
    }

    private void init() throws Exception {
        try {
            kieService = (IKieBpmService) this.getApplicationContext().getBean(IKieBpmService.BEAN_NAME);
            datatypeManager = (IDataObjectManager) this.getApplicationContext().getBean("DataObjectManager");
            formManager = (IKieFormManager) this.getService(IKieFormManager.BEAN_NAME_ID);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testCreateDataTypeWidget() {
        DatatableWidgetConfigRequest req = new DatatableWidgetConfigRequest();
        req.setContainerId("mortgage_1.0");
        req.setFramePosDraft(0);
        req.setKnowledgeSourceId("a06398e71a864fad90bad6975fc3f99420180711T110006583");
        req.setPageCode("pagina_1");
        req.setProcessId("com.redhat.bpms.examples.mortgage.MortgageApplication");
        req.setWidgetType("bpm-datatype-form");
        DatatableWidgetConfigDto dto = this.getKieService().updateDataTypeWIdgetConfig(req);
        String entityType = dto.getDataType();
        IApsEntity entity = null;
        entity = this.getDatatypeManager().getEntityPrototype(entityType);
        assertNotNull(entity);
        assertTrue(entity.getAttributeList().size() > 0);
    }

    @Test
    public void testGetServerStatus() {
        JSONArray status = null;
        try {
            status = this.getFormManager().getKieServerStatus();
        } catch (ApsSystemException ex) {
            Logger.getLogger(KieBpmServiceIntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertNotNull(status);
        assertTrue(status.length() > 0);
    }

    public final static String USERNAME = "pamAdmin";
    public final static String PASSWORD = "redhatpam1!"; // ansible.serv.run

    public final static String HOSTNAME = "rhpam7-cc-dispute-kieserver-rhpam7-cc-dispute-en.54.36.53.206.nip.io";//192.168.77.235";
    public final static String SCHEMA = "http";
    public final static String WEBAPP = "";
    public final static Integer PORT = 80;
    public final static Integer TIMEOUT = 1000;
}
