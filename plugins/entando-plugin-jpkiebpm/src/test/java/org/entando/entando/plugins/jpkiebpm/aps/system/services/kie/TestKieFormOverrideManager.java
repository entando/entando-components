/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Date;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.entando.entando.plugins.jpkiebpm.aps.ApsPluginBaseTestCase;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.MortgageDemoTest.createDefaultOverrideForTest;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.MortgageDemoTest.createOverrideListForTests;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.MortgageDemoTest.createPlaceholderOverrideForTest;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DropDownOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.IBpmOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.OverrideList;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;

/**
 *
 * @author Entando
 */
public class TestKieFormOverrideManager extends ApsPluginBaseTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        init();

    }

    @Override
    public void tearDown() {
        // do nothing
    }

    public void testInsertReload() throws ApsSystemException, Throwable {
        KieFormOverride kfo
                = new KieFormOverride();
        String containerId = "containerId";
        Date now = new Date();
        String formField = "down_payments";
        String processId = "processId";
        OverrideList ol = createOverrideListForTests();

        try {
            kfo.setContainerId(containerId);
            kfo.setDate(now);
            kfo.setField(formField);
            kfo.setId(0);
            kfo.setOverrides(ol);
            kfo.setProcessId(processId);

            Thread.sleep(500);
            _overrideManager.addKieFormOverride(kfo);
            assertFalse(kfo.getId() == 0);
            KieFormOverride ver = _overrideManager.getKieFormOverride(kfo.getId());
            assertNotNull(ver);
            assertNotSame(kfo, ver);
            assertEquals(containerId, ver.getContainerId());
            assertTrue(now.before(ver.getDate())); // date updated on every update
            assertEquals(formField, ver.getField());
            assertEquals(processId, ver.getProcessId());
            assertNotNull(ver.getOverrides());

            List<IBpmOverride> overrides = ver.getOverrides().getList();

            assertFalse(overrides.isEmpty());
            assertEquals(3, overrides.size());

            assertTrue(overrides.get(0) instanceof DefaultValueOverride);
            assertTrue(overrides.get(1) instanceof PlaceHolderOverride);
            assertTrue(overrides.get(2) instanceof DropDownOverride);

            DefaultValueOverride elem1 = (DefaultValueOverride) overrides.get(0);
            assertEquals(DEFAULTVALUE,
                    elem1.getDefaultValue());
            PlaceHolderOverride elem2 = (PlaceHolderOverride) overrides.get(1);
            assertEquals(PLACEHOLDER,
                    elem2.getPlaceHolder());
            DropDownOverride elem3 = (DropDownOverride) overrides.get(2);
            List<String> vals = elem3.getValues();
            assertNotNull(vals);
            assertFalse(vals.isEmpty());
            for (String value : vals) {
                assertTrue(
                        value.equals(VAL1)
                        || value.equals(VAL2)
                );
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            _overrideManager.deleteKieFormOverride(kfo.getId());
        }
    }

    public void testGetFormOverrides() throws Throwable {
        KieFormOverride kfo1
                = new KieFormOverride();
        KieFormOverride kfo2
                = new KieFormOverride();
        try {
            String containerId = "containerId";
            Date now = new Date();
            String formField1 = "down_payments";
            String formField2 = "applicant_ssn";
            String processId = "processId";
            OverrideList ol = new OverrideList();

            // field 1 - default value override for 'down_payments'
            ol.addOverride(createDefaultOverrideForTest());
            kfo1.setContainerId(containerId);
            kfo1.setDate(now);
            kfo1.setField(formField1);
            kfo1.setId(0);
            kfo1.setOverrides(ol);
            kfo1.setProcessId(processId);

            ol = new OverrideList();
            // field 2 - placeholder override for 'down_payments'
            ol.addOverride(createPlaceholderOverrideForTest());
            kfo2.setContainerId(containerId);
            kfo2.setDate(now);
            kfo2.setField(formField2);
            kfo2.setId(0);
            kfo2.setOverrides(ol);
            kfo2.setProcessId(processId);

            _overrideManager.addKieFormOverride(kfo1);
            _overrideManager.addKieFormOverride(kfo2);

            List<KieFormOverride> list = _overrideManager.getFormOverrides(containerId, processId);
            assertNotNull(list);
            assertFalse(list.isEmpty());
            assertEquals(2,
                    list.size());
            testEqualsOverrides(kfo1,
                    list.get(0));
            testEqualsOverrides(kfo2,
                    list.get(1));

        } finally {
            _overrideManager.deleteKieFormOverride(kfo1.getId());
            _overrideManager.deleteKieFormOverride(kfo2.getId());
        }
    }

    private void testEqualsOverrides(KieFormOverride original, KieFormOverride ver) {
        assertNotNull(original);
        assertNotNull(ver);
        assertEquals(original.getContainerId(),
                ver.getContainerId());
        assertEquals(original.getContainerId(),
                ver.getContainerId());
        assertEquals(original.getDate(),
                ver.getDate());
        assertEquals(original.getField(),
                ver.getField());
        assertEquals(original.getId(),
                ver.getId());
        assertEquals(original.getProcessId(),
                ver.getProcessId());
        List<IBpmOverride> lo = original.getOverrides().getList();
        List<IBpmOverride> lv = ver.getOverrides().getList();
        assertEquals(lo.size(),
                lv.size());
        for (int i = 0; i < lo.size(); i++) {
            assertEquals(lo.get(i).getClass(),
                    lv.get(i).getClass());
        }

    }

    private void init() {
        _overrideManager = (IKieFormOverrideManager) this.getService(IKieFormOverrideManager.BEAN_ID);
        assertNotNull(_overrideManager);
    }

    private IKieFormOverrideManager _overrideManager;

    public final static String DEFAULTVALUE = "default value!";
    public final static String PLACEHOLDER = "change me asap!";
    public final static String VAL1 = "Entando";
    public final static String VAL2 = "4.3 override";

    private IBpmOverride createPlaholferOverrideForTest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
