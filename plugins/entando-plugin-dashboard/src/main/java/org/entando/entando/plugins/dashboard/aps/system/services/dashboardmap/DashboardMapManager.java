/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.event.DashboardMapChangedEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

public class DashboardMapManager extends AbstractService implements IDashboardMapManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardMapManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardMap getDashboardMap(int id) throws ApsSystemException {
		DashboardMap dashboardMap = null;
		try {
			dashboardMap = this.getDashboardMapDAO().loadDashboardMap(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardMap with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardMap with id: " + id, t);
		}
		return dashboardMap;
	}

	@Override
	public List<Integer> getDashboardMaps() throws ApsSystemException {
		List<Integer> dashboardMaps = new ArrayList<Integer>();
		try {
			dashboardMaps = this.getDashboardMapDAO().loadDashboardMaps();
		} catch (Throwable t) {
			logger.error("Error loading DashboardMap list",  t);
			throw new ApsSystemException("Error loading DashboardMap ", t);
		}
		return dashboardMaps;
	}

	@Override
	public List<Integer> searchDashboardMaps(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardMaps = new ArrayList<Integer>();
		try {
			dashboardMaps = this.getDashboardMapDAO().searchDashboardMaps(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardMaps", t);
			throw new ApsSystemException("Error searching DashboardMaps", t);
		}
		return dashboardMaps;
	}

	@Override
	public void addDashboardMap(DashboardMap dashboardMap) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardMap.setId(key);
			this.getDashboardMapDAO().insertDashboardMap(dashboardMap);
			this.notifyDashboardMapChangedEvent(dashboardMap, DashboardMapChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardMap", t);
			throw new ApsSystemException("Error adding DashboardMap", t);
		}
	}
 
	@Override
	public void updateDashboardMap(DashboardMap dashboardMap) throws ApsSystemException {
		try {
			this.getDashboardMapDAO().updateDashboardMap(dashboardMap);
			this.notifyDashboardMapChangedEvent(dashboardMap, DashboardMapChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardMap", t);
			throw new ApsSystemException("Error updating DashboardMap " + dashboardMap, t);
		}
	}

	@Override
	public void deleteDashboardMap(int id) throws ApsSystemException {
		try {
			DashboardMap dashboardMap = this.getDashboardMap(id);
			this.getDashboardMapDAO().removeDashboardMap(id);
			this.notifyDashboardMapChangedEvent(dashboardMap, DashboardMapChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardMap with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardMap with id:" + id, t);
		}
	}


	private void notifyDashboardMapChangedEvent(DashboardMap dashboardMap, int operationCode) {
		DashboardMapChangedEvent event = new DashboardMapChangedEvent();
		event.setDashboardMap(dashboardMap);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardMap> getDashboardMaps(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardMap> pagedResult = null;
        try {
            List<DashboardMap> dashboardMaps = new ArrayList<>();
            int count = this.getDashboardMapDAO().countDashboardMaps(filters);

            List<Integer> dashboardMapNames = this.getDashboardMapDAO().searchDashboardMaps(filters);
            for (Integer dashboardMapName : dashboardMapNames) {
                dashboardMaps.add(this.getDashboardMap(dashboardMapName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardMap>(count, dashboardMaps);
        } catch (Throwable t) {
            logger.error("Error searching dashboardMaps", t);
            throw new ApsSystemException("Error searching dashboardMaps", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardMap> getDashboardMaps(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardMaps(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardMapDAO(IDashboardMapDAO dashboardMapDAO) {
		 this._dashboardMapDAO = dashboardMapDAO;
	}
	protected IDashboardMapDAO getDashboardMapDAO() {
		return _dashboardMapDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardMapDAO _dashboardMapDAO;
}
