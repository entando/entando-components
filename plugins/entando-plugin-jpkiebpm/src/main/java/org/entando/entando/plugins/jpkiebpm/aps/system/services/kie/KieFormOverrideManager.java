package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import com.agiletec.aps.system.common.*;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.event.KieFormOverrideChangedEvent;
import org.slf4j.*;

import java.util.*;

public class KieFormOverrideManager extends AbstractService implements IKieFormOverrideManager {

    private static final Logger _logger = LoggerFactory.getLogger(KieFormOverrideManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready.", this.getClass().getName());
    }

    @Override
    public KieFormOverride getKieFormOverride(int id) throws ApsSystemException {
        KieFormOverride kieFormOverride = null;
        try {
            kieFormOverride = this.getKieFormOverrideDAO().loadKieFormOverride(id);
        } catch (Throwable t) {
            _logger.error("Error loading kieFormOverride with id '{}'", id, t);
            throw new ApsSystemException("Error loading kieFormOverride with id: " + id, t);
        }
        return kieFormOverride;
    }

    @Override
    public List<Integer> getKieFormOverrides() throws ApsSystemException {
        List<Integer> kieFormOverrides = new ArrayList<Integer>();
        try {
            kieFormOverrides = this.getKieFormOverrideDAO().loadKieFormOverrides();
        } catch (Throwable t) {
            _logger.error("Error loading KieFormOverride list", t);
            throw new ApsSystemException("Error loading KieFormOverride ", t);
        }
        return kieFormOverrides;
    }

    @Override
    public List<Integer> searchKieFormOverrides(FieldSearchFilter filters[]) throws ApsSystemException {
        List<Integer> kieFormOverrides = new ArrayList<Integer>();
        try {
            kieFormOverrides = this.getKieFormOverrideDAO().searchKieFormOverrides(filters);
        } catch (Throwable t) {
            _logger.error("Error searching KieFormOverrides", t);
            throw new ApsSystemException("Error searching KieFormOverrides", t);
        }
        return kieFormOverrides;
    }

    @Override
    public void addKieFormOverride(KieFormOverride kieFormOverride) throws ApsSystemException {
        try {
            kieFormOverride.setDate(new Date());
            this.getKieFormOverrideDAO().insertKieFormOverride(kieFormOverride);
            this.notifyKieFormOverrideChangedEvent(kieFormOverride, KieFormOverrideChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error adding KieFormOverride", t);
            throw new ApsSystemException("Error adding KieFormOverride", t);
        }
    }

    @Override
    public void updateKieFormOverride(KieFormOverride kieFormOverride) throws ApsSystemException {
        try {
            this.getKieFormOverrideDAO().updateKieFormOverride(kieFormOverride);
            this.notifyKieFormOverrideChangedEvent(kieFormOverride, KieFormOverrideChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error updating KieFormOverride", t);
            throw new ApsSystemException("Error updating KieFormOverride " + kieFormOverride, t);
        }
    }

    @Override
    public void deleteKieFormOverride(int id) throws ApsSystemException {
        try {
            KieFormOverride kieFormOverride = this.getKieFormOverride(id);
            this.getKieFormOverrideDAO().removeKieFormOverride(id);
            this.notifyKieFormOverrideChangedEvent(kieFormOverride, KieFormOverrideChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error deleting KieFormOverride with id {}", id, t);
            throw new ApsSystemException("Error deleting KieFormOverride with id:" + id, t);
        }
    }

    @Override
    public void deleteWidgetKieFormOverrides(int widgetInfoId) throws ApsSystemException {
        FieldSearchFilter widgetInfoIdFilter = new FieldSearchFilter("widgetInfoId", (Integer) widgetInfoId, false);
        List<Integer> ids = searchKieFormOverrides(new FieldSearchFilter[]{widgetInfoIdFilter});
        if (null != ids) {
            for (Integer id : ids) {
                deleteKieFormOverride(id);
            }
        }
    }
    
    @Override
    public void deleteWidgetKieFormOverrides(int widgetInfoId, boolean online) throws ApsSystemException {
        FieldSearchFilter widgetInfoIdFilter = new FieldSearchFilter("widgetInfoId", (Integer) widgetInfoId, false);
        FieldSearchFilter onlineFilter = new FieldSearchFilter("online", (Integer) (online ? 1 : 0), false);
        List<Integer> ids = searchKieFormOverrides(new FieldSearchFilter[]{widgetInfoIdFilter, onlineFilter});
        if (null != ids) {
            for (Integer id : ids) {
                deleteKieFormOverride(id);
            }
        }
    }

    @Override
    @Deprecated
    public List<KieFormOverride> getFormOverrides(String containerId, String processId) throws ApsSystemException {
        List<KieFormOverride> list = new ArrayList<KieFormOverride>();

        FieldSearchFilter containerFilter = new FieldSearchFilter(("containerId"), containerId, true);
        FieldSearchFilter processFilter = new FieldSearchFilter(("processId"), processId, true);
        FieldSearchFilter filters[] = {containerFilter, processFilter};
        List<Integer> ids = searchKieFormOverrides(filters);
        if (null != ids) {
            for (Integer id : ids) {
                KieFormOverride override = getKieFormOverride(id);
                if (null != override) {
                    list.add(override);
                }
            }
        }
        return list;
    }

    @Override
    public List<KieFormOverride> getFormOverrides(int widgetInfoId, boolean online, String containerId, String processId, String sourceId) throws ApsSystemException {
        return getFormOverrides(widgetInfoId, online, containerId, processId, sourceId, null);
    }

    @Override
    public List<KieFormOverride> getFormOverrides(int widgetInfoId, boolean online) throws ApsSystemException {
        return getFormOverrides(widgetInfoId, online, null, null, null, null);
    }

    @Override
    public List<KieFormOverride> getFormOverrides(int widgetInfoId, boolean online, String containerId, String processId, String sourceId, String field) throws ApsSystemException {
        FieldSearchFilter[] filters = buildFilters(widgetInfoId, online, containerId, processId, sourceId, field);
        return getKieFormOverridesFromFilters(filters);
    }

    private FieldSearchFilter[] buildFilters(int widgetInfoId, boolean online, String containerId, String processId, String sourceId, String field) {
        List<FieldSearchFilter> filters = new ArrayList<>();

        filters.add(new FieldSearchFilter("widgetInfoId", (Integer) widgetInfoId, false));
        filters.add(new FieldSearchFilter("online", (Integer) (online ? 1 : 0), false));

        if (containerId != null) {
            filters.add(new FieldSearchFilter("containerId", containerId, true));
        }
        if (processId != null) {
            filters.add(new FieldSearchFilter("processId", processId, true));
        }
        if (sourceId != null) {
            filters.add(new FieldSearchFilter("sourceId", sourceId, true));
        }
        if (field != null) {
            filters.add(new FieldSearchFilter("field", field, true));
        }

        FieldSearchFilter[] filtersArray = new FieldSearchFilter[filters.size()];
        return filters.toArray(filtersArray);
    }

    private List<KieFormOverride> getKieFormOverridesFromFilters(FieldSearchFilter[] filters) throws ApsSystemException {
        List<KieFormOverride> list = new ArrayList<>();
        List<Integer> ids = searchKieFormOverrides(filters);
        if (null != ids) {
            for (Integer id : ids) {
                KieFormOverride override = getKieFormOverride(id);
                if (null != override) {
                    list.add(override);
                }
            }
        }
        return list;
    }

    private void notifyKieFormOverrideChangedEvent(KieFormOverride kieFormOverride, int operationCode) {
        KieFormOverrideChangedEvent event = new KieFormOverrideChangedEvent();
        event.setKieFormOverride(kieFormOverride);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    public void setKieFormOverrideDAO(IKieFormOverrideDAO kieFormOverrideDAO) {
        this._kieFormOverrideDAO = kieFormOverrideDAO;
    }

    protected IKieFormOverrideDAO getKieFormOverrideDAO() {
        return _kieFormOverrideDAO;
    }

    private IKieFormOverrideDAO _kieFormOverrideDAO;
}
