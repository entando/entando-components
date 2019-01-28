/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.event.DashboardDonutChartChangedEvent;
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

public class DashboardDonutChartManager extends AbstractService implements IDashboardDonutChartManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardDonutChartManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardDonutChart getDashboardDonutChart(int id) throws ApsSystemException {
		DashboardDonutChart dashboardDonutChart = null;
		try {
			dashboardDonutChart = this.getDashboardDonutChartDAO().loadDashboardDonutChart(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardDonutChart with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardDonutChart with id: " + id, t);
		}
		return dashboardDonutChart;
	}

	@Override
	public List<Integer> getDashboardDonutCharts() throws ApsSystemException {
		List<Integer> dashboardDonutCharts = new ArrayList<Integer>();
		try {
			dashboardDonutCharts = this.getDashboardDonutChartDAO().loadDashboardDonutCharts();
		} catch (Throwable t) {
			logger.error("Error loading DashboardDonutChart list",  t);
			throw new ApsSystemException("Error loading DashboardDonutChart ", t);
		}
		return dashboardDonutCharts;
	}

	@Override
	public List<Integer> searchDashboardDonutCharts(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardDonutCharts = new ArrayList<Integer>();
		try {
			dashboardDonutCharts = this.getDashboardDonutChartDAO().searchDashboardDonutCharts(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardDonutCharts", t);
			throw new ApsSystemException("Error searching DashboardDonutCharts", t);
		}
		return dashboardDonutCharts;
	}

	@Override
	public void addDashboardDonutChart(DashboardDonutChart dashboardDonutChart) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardDonutChart.setId(key);
			this.getDashboardDonutChartDAO().insertDashboardDonutChart(dashboardDonutChart);
			this.notifyDashboardDonutChartChangedEvent(dashboardDonutChart, DashboardDonutChartChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardDonutChart", t);
			throw new ApsSystemException("Error adding DashboardDonutChart", t);
		}
	}
 
	@Override
	public void updateDashboardDonutChart(DashboardDonutChart dashboardDonutChart) throws ApsSystemException {
		try {
			this.getDashboardDonutChartDAO().updateDashboardDonutChart(dashboardDonutChart);
			this.notifyDashboardDonutChartChangedEvent(dashboardDonutChart, DashboardDonutChartChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardDonutChart", t);
			throw new ApsSystemException("Error updating DashboardDonutChart " + dashboardDonutChart, t);
		}
	}

	@Override
	public void deleteDashboardDonutChart(int id) throws ApsSystemException {
		try {
			DashboardDonutChart dashboardDonutChart = this.getDashboardDonutChart(id);
			this.getDashboardDonutChartDAO().removeDashboardDonutChart(id);
			this.notifyDashboardDonutChartChangedEvent(dashboardDonutChart, DashboardDonutChartChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardDonutChart with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardDonutChart with id:" + id, t);
		}
	}


	private void notifyDashboardDonutChartChangedEvent(DashboardDonutChart dashboardDonutChart, int operationCode) {
		DashboardDonutChartChangedEvent event = new DashboardDonutChartChangedEvent();
		event.setDashboardDonutChart(dashboardDonutChart);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardDonutChart> getDashboardDonutCharts(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardDonutChart> pagedResult = null;
        try {
            List<DashboardDonutChart> dashboardDonutCharts = new ArrayList<>();
            int count = this.getDashboardDonutChartDAO().countDashboardDonutCharts(filters);

            List<Integer> dashboardDonutChartNames = this.getDashboardDonutChartDAO().searchDashboardDonutCharts(filters);
            for (Integer dashboardDonutChartName : dashboardDonutChartNames) {
                dashboardDonutCharts.add(this.getDashboardDonutChart(dashboardDonutChartName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardDonutChart>(count, dashboardDonutCharts);
        } catch (Throwable t) {
            logger.error("Error searching dashboardDonutCharts", t);
            throw new ApsSystemException("Error searching dashboardDonutCharts", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardDonutChart> getDashboardDonutCharts(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardDonutCharts(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardDonutChartDAO(IDashboardDonutChartDAO dashboardDonutChartDAO) {
		 this._dashboardDonutChartDAO = dashboardDonutChartDAO;
	}
	protected IDashboardDonutChartDAO getDashboardDonutChartDAO() {
		return _dashboardDonutChartDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardDonutChartDAO _dashboardDonutChartDAO;
}
