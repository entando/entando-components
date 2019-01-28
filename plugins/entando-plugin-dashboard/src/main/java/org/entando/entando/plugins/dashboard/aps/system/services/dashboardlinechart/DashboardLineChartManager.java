/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.event.DashboardLineChartChangedEvent;
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

public class DashboardLineChartManager extends AbstractService implements IDashboardLineChartManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardLineChartManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardLineChart getDashboardLineChart(int id) throws ApsSystemException {
		DashboardLineChart dashboardLineChart = null;
		try {
			dashboardLineChart = this.getDashboardLineChartDAO().loadDashboardLineChart(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardLineChart with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardLineChart with id: " + id, t);
		}
		return dashboardLineChart;
	}

	@Override
	public List<Integer> getDashboardLineCharts() throws ApsSystemException {
		List<Integer> dashboardLineCharts = new ArrayList<Integer>();
		try {
			dashboardLineCharts = this.getDashboardLineChartDAO().loadDashboardLineCharts();
		} catch (Throwable t) {
			logger.error("Error loading DashboardLineChart list",  t);
			throw new ApsSystemException("Error loading DashboardLineChart ", t);
		}
		return dashboardLineCharts;
	}

	@Override
	public List<Integer> searchDashboardLineCharts(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardLineCharts = new ArrayList<Integer>();
		try {
			dashboardLineCharts = this.getDashboardLineChartDAO().searchDashboardLineCharts(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardLineCharts", t);
			throw new ApsSystemException("Error searching DashboardLineCharts", t);
		}
		return dashboardLineCharts;
	}

	@Override
	public void addDashboardLineChart(DashboardLineChart dashboardLineChart) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardLineChart.setId(key);
			this.getDashboardLineChartDAO().insertDashboardLineChart(dashboardLineChart);
			this.notifyDashboardLineChartChangedEvent(dashboardLineChart, DashboardLineChartChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardLineChart", t);
			throw new ApsSystemException("Error adding DashboardLineChart", t);
		}
	}
 
	@Override
	public void updateDashboardLineChart(DashboardLineChart dashboardLineChart) throws ApsSystemException {
		try {
			this.getDashboardLineChartDAO().updateDashboardLineChart(dashboardLineChart);
			this.notifyDashboardLineChartChangedEvent(dashboardLineChart, DashboardLineChartChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardLineChart", t);
			throw new ApsSystemException("Error updating DashboardLineChart " + dashboardLineChart, t);
		}
	}

	@Override
	public void deleteDashboardLineChart(int id) throws ApsSystemException {
		try {
			DashboardLineChart dashboardLineChart = this.getDashboardLineChart(id);
			this.getDashboardLineChartDAO().removeDashboardLineChart(id);
			this.notifyDashboardLineChartChangedEvent(dashboardLineChart, DashboardLineChartChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardLineChart with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardLineChart with id:" + id, t);
		}
	}


	private void notifyDashboardLineChartChangedEvent(DashboardLineChart dashboardLineChart, int operationCode) {
		DashboardLineChartChangedEvent event = new DashboardLineChartChangedEvent();
		event.setDashboardLineChart(dashboardLineChart);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardLineChart> getDashboardLineCharts(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardLineChart> pagedResult = null;
        try {
            List<DashboardLineChart> dashboardLineCharts = new ArrayList<>();
            int count = this.getDashboardLineChartDAO().countDashboardLineCharts(filters);

            List<Integer> dashboardLineChartNames = this.getDashboardLineChartDAO().searchDashboardLineCharts(filters);
            for (Integer dashboardLineChartName : dashboardLineChartNames) {
                dashboardLineCharts.add(this.getDashboardLineChart(dashboardLineChartName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardLineChart>(count, dashboardLineCharts);
        } catch (Throwable t) {
            logger.error("Error searching dashboardLineCharts", t);
            throw new ApsSystemException("Error searching dashboardLineCharts", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardLineChart> getDashboardLineCharts(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardLineCharts(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardLineChartDAO(IDashboardLineChartDAO dashboardLineChartDAO) {
		 this._dashboardLineChartDAO = dashboardLineChartDAO;
	}
	protected IDashboardLineChartDAO getDashboardLineChartDAO() {
		return _dashboardLineChartDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardLineChartDAO _dashboardLineChartDAO;
}
