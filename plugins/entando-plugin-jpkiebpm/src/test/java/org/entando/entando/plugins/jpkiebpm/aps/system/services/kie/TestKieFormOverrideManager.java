package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.*;

import java.util.*;

import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.MortgageDemoTest.*;


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
        int widgetInfoId = 1;
        boolean online = true;
        String containerId = "containerId";
        Date now = new Date();
        String formField = "down_payments";
        String processId = "processId";
        String sourceId = "1";
        boolean active = true;
        OverrideList ol = createOverrideListForTests();

        try {
            kfo.setWidgetInfoId(widgetInfoId);
            kfo.setOnline(online);
            kfo.setContainerId(containerId);
            kfo.setDate(now);
            kfo.setField(formField);
            kfo.setId(0);
            kfo.setOverrides(ol);
            kfo.setProcessId(processId);
            kfo.setSourceId(sourceId);
            kfo.setActive(active);

            Thread.sleep(500);
            _overrideManager.addKieFormOverride(kfo);
            assertFalse(kfo.getId() == 0);
            KieFormOverride ver = _overrideManager.getKieFormOverride(kfo.getId());
            assertNotNull(ver);
            assertNotSame(kfo, ver);
            assertEquals(widgetInfoId, ver.getWidgetInfoId());
            assertEquals(online, ver.isOnline());
            assertEquals(containerId, ver.getContainerId());
            assertTrue(now.before(ver.getDate())); // date updated on every update
            assertEquals(formField, ver.getField());
            assertEquals(processId, ver.getProcessId());
            assertEquals(sourceId, ver.getSourceId());
            assertEquals(active, ver.isActive());
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
            int widgetInfoId = 1;
            boolean online = true;
            String containerId = "containerId";
            Date now = new Date();
            String formField1 = "down_payments";
            String formField2 = "applicant_ssn";
            String processId = "processId";
            String sourceId = "1";
            boolean active = true;
            OverrideList ol = new OverrideList();

            // field 1 - default value override for 'down_payments'
            ol.addOverride(createDefaultOverrideForTest());
            kfo1.setWidgetInfoId(widgetInfoId);
            kfo1.setOnline(online);
            kfo1.setContainerId(containerId);
            kfo1.setDate(now);
            kfo1.setField(formField1);
            kfo1.setId(0);
            kfo1.setOverrides(ol);
            kfo1.setProcessId(processId);
            kfo1.setSourceId(sourceId);
            kfo1.setActive(active);

            ol = new OverrideList();
            // field 2 - placeholder override for 'down_payments'
            ol.addOverride(createPlaceholderOverrideForTest());
            kfo2.setWidgetInfoId(widgetInfoId);
            kfo2.setOnline(online);
            kfo2.setContainerId(containerId);
            kfo2.setDate(now);
            kfo2.setField(formField2);
            kfo2.setId(0);
            kfo2.setOverrides(ol);
            kfo2.setProcessId(processId);
            kfo2.setSourceId(sourceId);
            kfo2.setActive(active);

            _overrideManager.addKieFormOverride(kfo1);
            _overrideManager.addKieFormOverride(kfo2);

            List<KieFormOverride> list = _overrideManager.getFormOverrides(widgetInfoId, online, containerId, processId, sourceId);
            assertNotNull(list);
            assertFalse(list.isEmpty());
            assertEquals(2,
                    list.size());
            testEqualsOverrides(kfo1,
                    list.get(0));
            testEqualsOverrides(kfo2,
                    list.get(1));

            list = _overrideManager.getFormOverrides(widgetInfoId, online);
            assertEquals(2, list.size());
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
        assertEquals(original.getSourceId(),
                ver.getSourceId());
        assertEquals(original.getWidgetInfoId(),
                ver.getWidgetInfoId());
        assertEquals(original.isOnline(),
                ver.isOnline());
        assertEquals(original.isActive(),
                ver.isActive());
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
