/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.event.DashboardGaugeChartChangedEvent;
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

public class DashboardGaugeChartManager extends AbstractService implements IDashboardGaugeChartManager {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardGaugeChartManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public DashboardGaugeChart getDashboardGaugeChart(int id) throws ApsSystemException {
		DashboardGaugeChart dashboardGaugeChart = null;
		try {
			dashboardGaugeChart = this.getDashboardGaugeChartDAO().loadDashboardGaugeChart(id);
		} catch (Throwable t) {
			logger.error("Error loading dashboardGaugeChart with id '{}'", id,  t);
			throw new ApsSystemException("Error loading dashboardGaugeChart with id: " + id, t);
		}
		return dashboardGaugeChart;
	}

	@Override
	public List<Integer> getDashboardGaugeCharts() throws ApsSystemException {
		List<Integer> dashboardGaugeCharts = new ArrayList<Integer>();
		try {
			dashboardGaugeCharts = this.getDashboardGaugeChartDAO().loadDashboardGaugeCharts();
		} catch (Throwable t) {
			logger.error("Error loading DashboardGaugeChart list",  t);
			throw new ApsSystemException("Error loading DashboardGaugeChart ", t);
		}
		return dashboardGaugeCharts;
	}

	@Override
	public List<Integer> searchDashboardGaugeCharts(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> dashboardGaugeCharts = new ArrayList<Integer>();
		try {
			dashboardGaugeCharts = this.getDashboardGaugeChartDAO().searchDashboardGaugeCharts(filters);
		} catch (Throwable t) {
			logger.error("Error searching DashboardGaugeCharts", t);
			throw new ApsSystemException("Error searching DashboardGaugeCharts", t);
		}
		return dashboardGaugeCharts;
	}

	@Override
	public void addDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			dashboardGaugeChart.setId(key);
			this.getDashboardGaugeChartDAO().insertDashboardGaugeChart(dashboardGaugeChart);
			this.notifyDashboardGaugeChartChangedEvent(dashboardGaugeChart, DashboardGaugeChartChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding DashboardGaugeChart", t);
			throw new ApsSystemException("Error adding DashboardGaugeChart", t);
		}
	}
 
	@Override
	public void updateDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) throws ApsSystemException {
		try {
			this.getDashboardGaugeChartDAO().updateDashboardGaugeChart(dashboardGaugeChart);
			this.notifyDashboardGaugeChartChangedEvent(dashboardGaugeChart, DashboardGaugeChartChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating DashboardGaugeChart", t);
			throw new ApsSystemException("Error updating DashboardGaugeChart " + dashboardGaugeChart, t);
		}
	}

	@Override
	public void deleteDashboardGaugeChart(int id) throws ApsSystemException {
		try {
			DashboardGaugeChart dashboardGaugeChart = this.getDashboardGaugeChart(id);
			this.getDashboardGaugeChartDAO().removeDashboardGaugeChart(id);
			this.notifyDashboardGaugeChartChangedEvent(dashboardGaugeChart, DashboardGaugeChartChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting DashboardGaugeChart with id {}", id, t);
			throw new ApsSystemException("Error deleting DashboardGaugeChart with id:" + id, t);
		}
	}


	private void notifyDashboardGaugeChartChangedEvent(DashboardGaugeChart dashboardGaugeChart, int operationCode) {
		DashboardGaugeChartChangedEvent event = new DashboardGaugeChartChangedEvent();
		event.setDashboardGaugeChart(dashboardGaugeChart);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<DashboardGaugeChart> getDashboardGaugeCharts(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<DashboardGaugeChart> pagedResult = null;
        try {
            List<DashboardGaugeChart> dashboardGaugeCharts = new ArrayList<>();
            int count = this.getDashboardGaugeChartDAO().countDashboardGaugeCharts(filters);

            List<Integer> dashboardGaugeChartNames = this.getDashboardGaugeChartDAO().searchDashboardGaugeCharts(filters);
            for (Integer dashboardGaugeChartName : dashboardGaugeChartNames) {
                dashboardGaugeCharts.add(this.getDashboardGaugeChart(dashboardGaugeChartName));
            }
            pagedResult = new SearcherDaoPaginatedResult<DashboardGaugeChart>(count, dashboardGaugeCharts);
        } catch (Throwable t) {
            logger.error("Error searching dashboardGaugeCharts", t);
            throw new ApsSystemException("Error searching dashboardGaugeCharts", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<DashboardGaugeChart> getDashboardGaugeCharts(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getDashboardGaugeCharts(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setDashboardGaugeChartDAO(IDashboardGaugeChartDAO dashboardGaugeChartDAO) {
		 this._dashboardGaugeChartDAO = dashboardGaugeChartDAO;
	}
	protected IDashboardGaugeChartDAO getDashboardGaugeChartDAO() {
		return _dashboardGaugeChartDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IDashboardGaugeChartDAO _dashboardGaugeChartDAO;
}
