/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.event.DashboardTableChangedEvent;
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

public class DashboardTableManager extends AbstractService implements IDashboardTableManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardTableManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardTable getDashboardTable(int id) throws ApsSystemException {
		DashboardTable dashboardTable = null;
		try {
			dashboardTable = this.getDashboardTableDAO().loadDashboardTable(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardTable with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardTable with id: " + id, t);
		}
		return dashboardTable;
	}

	@Override
	public List<Integer> getDashboardTables() throws ApsSystemException {
		List<Integer> dashboardTables = new ArrayList<Integer>();
		try {
			dashboardTables = this.getDashboardTableDAO().loadDashboardTables();
		} catch (Throwable t) {
			logger.error("Error loading DashboardTable list",  t);
			throw new ApsSystemException("Error loading DashboardTable ", t);
		}
		return dashboardTables;
	}

	@Override
	public List<Integer> searchDashboardTables(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardTables = new ArrayList<Integer>();
		try {
			dashboardTables = this.getDashboardTableDAO().searchDashboardTables(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardTables", t);
			throw new ApsSystemException("Error searching DashboardTables", t);
		}
		return dashboardTables;
	}

	@Override
	public void addDashboardTable(DashboardTable dashboardTable) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardTable.setId(key);
			this.getDashboardTableDAO().insertDashboardTable(dashboardTable);
			this.notifyDashboardTableChangedEvent(dashboardTable, DashboardTableChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardTable", t);
			throw new ApsSystemException("Error adding DashboardTable", t);
		}
	}
 
	@Override
	public void updateDashboardTable(DashboardTable dashboardTable) throws ApsSystemException {
		try {
			this.getDashboardTableDAO().updateDashboardTable(dashboardTable);
			this.notifyDashboardTableChangedEvent(dashboardTable, DashboardTableChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardTable", t);
			throw new ApsSystemException("Error updating DashboardTable " + dashboardTable, t);
		}
	}

	@Override
	public void deleteDashboardTable(int id) throws ApsSystemException {
		try {
			DashboardTable dashboardTable = this.getDashboardTable(id);
			this.getDashboardTableDAO().removeDashboardTable(id);
			this.notifyDashboardTableChangedEvent(dashboardTable, DashboardTableChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardTable with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardTable with id:" + id, t);
		}
	}


	private void notifyDashboardTableChangedEvent(DashboardTable dashboardTable, int operationCode) {
		DashboardTableChangedEvent event = new DashboardTableChangedEvent();
		event.setDashboardTable(dashboardTable);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardTable> getDashboardTables(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardTable> pagedResult = null;
        try {
            List<DashboardTable> dashboardTables = new ArrayList<>();
            int count = this.getDashboardTableDAO().countDashboardTables(filters);

            List<Integer> dashboardTableNames = this.getDashboardTableDAO().searchDashboardTables(filters);
            for (Integer dashboardTableName : dashboardTableNames) {
                dashboardTables.add(this.getDashboardTable(dashboardTableName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardTable>(count, dashboardTables);
        } catch (Throwable t) {
            logger.error("Error searching dashboardTables", t);
            throw new ApsSystemException("Error searching dashboardTables", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardTable> getDashboardTables(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardTables(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardTableDAO(IDashboardTableDAO dashboardTableDAO) {
		 this._dashboardTableDAO = dashboardTableDAO;
	}
	protected IDashboardTableDAO getDashboardTableDAO() {
		return _dashboardTableDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardTableDAO _dashboardTableDAO;
}
