/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.event.DashboardPieChartChangedEvent;
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

public class DashboardPieChartManager extends AbstractService implements IDashboardPieChartManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardPieChartManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardPieChart getDashboardPieChart(int id) throws ApsSystemException {
		DashboardPieChart dashboardPieChart = null;
		try {
			dashboardPieChart = this.getDashboardPieChartDAO().loadDashboardPieChart(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardPieChart with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardPieChart with id: " + id, t);
		}
		return dashboardPieChart;
	}

	@Override
	public List<Integer> getDashboardPieCharts() throws ApsSystemException {
		List<Integer> dashboardPieCharts = new ArrayList<Integer>();
		try {
			dashboardPieCharts = this.getDashboardPieChartDAO().loadDashboardPieCharts();
		} catch (Throwable t) {
			logger.error("Error loading DashboardPieChart list",  t);
			throw new ApsSystemException("Error loading DashboardPieChart ", t);
		}
		return dashboardPieCharts;
	}

	@Override
	public List<Integer> searchDashboardPieCharts(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardPieCharts = new ArrayList<Integer>();
		try {
			dashboardPieCharts = this.getDashboardPieChartDAO().searchDashboardPieCharts(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardPieCharts", t);
			throw new ApsSystemException("Error searching DashboardPieCharts", t);
		}
		return dashboardPieCharts;
	}

	@Override
	public void addDashboardPieChart(DashboardPieChart dashboardPieChart) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardPieChart.setId(key);
			this.getDashboardPieChartDAO().insertDashboardPieChart(dashboardPieChart);
			this.notifyDashboardPieChartChangedEvent(dashboardPieChart, DashboardPieChartChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardPieChart", t);
			throw new ApsSystemException("Error adding DashboardPieChart", t);
		}
	}
 
	@Override
	public void updateDashboardPieChart(DashboardPieChart dashboardPieChart) throws ApsSystemException {
		try {
			this.getDashboardPieChartDAO().updateDashboardPieChart(dashboardPieChart);
			this.notifyDashboardPieChartChangedEvent(dashboardPieChart, DashboardPieChartChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardPieChart", t);
			throw new ApsSystemException("Error updating DashboardPieChart " + dashboardPieChart, t);
		}
	}

	@Override
	public void deleteDashboardPieChart(int id) throws ApsSystemException {
		try {
			DashboardPieChart dashboardPieChart = this.getDashboardPieChart(id);
			this.getDashboardPieChartDAO().removeDashboardPieChart(id);
			this.notifyDashboardPieChartChangedEvent(dashboardPieChart, DashboardPieChartChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardPieChart with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardPieChart with id:" + id, t);
		}
	}


	private void notifyDashboardPieChartChangedEvent(DashboardPieChart dashboardPieChart, int operationCode) {
		DashboardPieChartChangedEvent event = new DashboardPieChartChangedEvent();
		event.setDashboardPieChart(dashboardPieChart);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardPieChart> getDashboardPieCharts(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardPieChart> pagedResult = null;
        try {
            List<DashboardPieChart> dashboardPieCharts = new ArrayList<>();
            int count = this.getDashboardPieChartDAO().countDashboardPieCharts(filters);

            List<Integer> dashboardPieChartNames = this.getDashboardPieChartDAO().searchDashboardPieCharts(filters);
            for (Integer dashboardPieChartName : dashboardPieChartNames) {
                dashboardPieCharts.add(this.getDashboardPieChart(dashboardPieChartName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardPieChart>(count, dashboardPieCharts);
        } catch (Throwable t) {
            logger.error("Error searching dashboardPieCharts", t);
            throw new ApsSystemException("Error searching dashboardPieCharts", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardPieChart> getDashboardPieCharts(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardPieCharts(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardPieChartDAO(IDashboardPieChartDAO dashboardPieChartDAO) {
		 this._dashboardPieChartDAO = dashboardPieChartDAO;
	}
	protected IDashboardPieChartDAO getDashboardPieChartDAO() {
		return _dashboardPieChartDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardPieChartDAO _dashboardPieChartDAO;
}
