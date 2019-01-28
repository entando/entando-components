/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.event.DashboardBarChartChangedEvent;
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

public class DashboardBarChartManager extends AbstractService implements IDashboardBarChartManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardBarChartManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardBarChart getDashboardBarChart(int id) throws ApsSystemException {
		DashboardBarChart dashboardBarChart = null;
		try {
			dashboardBarChart = this.getDashboardBarChartDAO().loadDashboardBarChart(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardBarChart with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardBarChart with id: " + id, t);
		}
		return dashboardBarChart;
	}

	@Override
	public List<Integer> getDashboardBarCharts() throws ApsSystemException {
		List<Integer> dashboardBarCharts = new ArrayList<Integer>();
		try {
			dashboardBarCharts = this.getDashboardBarChartDAO().loadDashboardBarCharts();
		} catch (Throwable t) {
			logger.error("Error loading DashboardBarChart list",  t);
			throw new ApsSystemException("Error loading DashboardBarChart ", t);
		}
		return dashboardBarCharts;
	}

	@Override
	public List<Integer> searchDashboardBarCharts(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardBarCharts = new ArrayList<Integer>();
		try {
			dashboardBarCharts = this.getDashboardBarChartDAO().searchDashboardBarCharts(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardBarCharts", t);
			throw new ApsSystemException("Error searching DashboardBarCharts", t);
		}
		return dashboardBarCharts;
	}

	@Override
	public void addDashboardBarChart(DashboardBarChart dashboardBarChart) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardBarChart.setId(key);
			this.getDashboardBarChartDAO().insertDashboardBarChart(dashboardBarChart);
			this.notifyDashboardBarChartChangedEvent(dashboardBarChart, DashboardBarChartChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardBarChart", t);
			throw new ApsSystemException("Error adding DashboardBarChart", t);
		}
	}
 
	@Override
	public void updateDashboardBarChart(DashboardBarChart dashboardBarChart) throws ApsSystemException {
		try {
			this.getDashboardBarChartDAO().updateDashboardBarChart(dashboardBarChart);
			this.notifyDashboardBarChartChangedEvent(dashboardBarChart, DashboardBarChartChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardBarChart", t);
			throw new ApsSystemException("Error updating DashboardBarChart " + dashboardBarChart, t);
		}
	}

	@Override
	public void deleteDashboardBarChart(int id) throws ApsSystemException {
		try {
			DashboardBarChart dashboardBarChart = this.getDashboardBarChart(id);
			this.getDashboardBarChartDAO().removeDashboardBarChart(id);
			this.notifyDashboardBarChartChangedEvent(dashboardBarChart, DashboardBarChartChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardBarChart with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardBarChart with id:" + id, t);
		}
	}


	private void notifyDashboardBarChartChangedEvent(DashboardBarChart dashboardBarChart, int operationCode) {
		DashboardBarChartChangedEvent event = new DashboardBarChartChangedEvent();
		event.setDashboardBarChart(dashboardBarChart);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardBarChart> getDashboardBarCharts(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardBarChart> pagedResult = null;
        try {
            List<DashboardBarChart> dashboardBarCharts = new ArrayList<>();
            int count = this.getDashboardBarChartDAO().countDashboardBarCharts(filters);

            List<Integer> dashboardBarChartNames = this.getDashboardBarChartDAO().searchDashboardBarCharts(filters);
            for (Integer dashboardBarChartName : dashboardBarChartNames) {
                dashboardBarCharts.add(this.getDashboardBarChart(dashboardBarChartName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardBarChart>(count, dashboardBarCharts);
        } catch (Throwable t) {
            logger.error("Error searching dashboardBarCharts", t);
            throw new ApsSystemException("Error searching dashboardBarCharts", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardBarChart> getDashboardBarCharts(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardBarCharts(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardBarChartDAO(IDashboardBarChartDAO dashboardBarChartDAO) {
		 this._dashboardBarChartDAO = dashboardBarChartDAO;
	}
	protected IDashboardBarChartDAO getDashboardBarChartDAO() {
		return _dashboardBarChartDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardBarChartDAO _dashboardBarChartDAO;
}
