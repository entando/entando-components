/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardDonutChartDAO extends AbstractSearcherDAO implements IDashboardDonutChartDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardDonutChartDAO.class);

    @Override
    public int countDashboardDonutCharts(FieldSearchFilter[] filters) {
        Integer dashboardDonutCharts = null;
        try {
            dashboardDonutCharts = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardDonutCharts", t);
            throw new RuntimeException("error in count dashboardDonutCharts", t);
        }
        return dashboardDonutCharts;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboarddonutchart";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardDonutCharts(FieldSearchFilter[] filters) {
            List<Integer> dashboardDonutChartsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardDonutChartsId.add(Integer.parseInt(idString)));
        return dashboardDonutChartsId;
        }


	@Override
	public List<Integer> loadDashboardDonutCharts() {
		List<Integer> dashboardDonutChartsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDDONUTCHARTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardDonutChartsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardDonutChart list",  t);
			throw new RuntimeException("Error loading DashboardDonutChart list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardDonutChartsId;
	}
	
	@Override
	public void insertDashboardDonutChart(DashboardDonutChart dashboardDonutChart) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardDonutChart(dashboardDonutChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardDonutChart",  t);
			throw new RuntimeException("Error on insert dashboardDonutChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardDonutChart(DashboardDonutChart dashboardDonutChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDDONUTCHART);
			int index = 1;
			stat.setInt(index++, dashboardDonutChart.getId());
 			stat.setString(index++, dashboardDonutChart.getWidgetId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardDonutChart",  t);
			throw new RuntimeException("Error on insert dashboardDonutChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardDonutChart(DashboardDonutChart dashboardDonutChart) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardDonutChart(dashboardDonutChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardDonutChart {}", dashboardDonutChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardDonutChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardDonutChart(DashboardDonutChart dashboardDonutChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDDONUTCHART);
			int index = 1;

 			stat.setString(index++, dashboardDonutChart.getWidgetId());
			stat.setInt(index++, dashboardDonutChart.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardDonutChart {}", dashboardDonutChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardDonutChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardDonutChart(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardDonutChart(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardDonutChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardDonutChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardDonutChart(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDDONUTCHART);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardDonutChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardDonutChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardDonutChart loadDashboardDonutChart(int id) {
		DashboardDonutChart dashboardDonutChart = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardDonutChart = this.loadDashboardDonutChart(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardDonutChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardDonutChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardDonutChart;
	}

	public DashboardDonutChart loadDashboardDonutChart(int id, Connection conn) {
		DashboardDonutChart dashboardDonutChart = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDDONUTCHART);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardDonutChart = this.buildDashboardDonutChartFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardDonutChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardDonutChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardDonutChart;
	}

	protected DashboardDonutChart buildDashboardDonutChartFromRes(ResultSet res) {
		DashboardDonutChart dashboardDonutChart = null;
		try {
			dashboardDonutChart = new DashboardDonutChart();				
			dashboardDonutChart.setId(res.getInt("id"));
			dashboardDonutChart.setWidgetId(res.getString("widgetid"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardDonutChartFromRes", t);
		}
		return dashboardDonutChart;
	}

	private static final String ADD_DASHBOARDDONUTCHART = "INSERT INTO dashboard_dashboarddonutchart (id, widgetid ) VALUES (?, ? )";

	private static final String UPDATE_DASHBOARDDONUTCHART = "UPDATE dashboard_dashboarddonutchart SET widgetid=? WHERE id = ?";

	private static final String DELETE_DASHBOARDDONUTCHART = "DELETE FROM dashboard_dashboarddonutchart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDDONUTCHART = "SELECT id, widgetid  FROM dashboard_dashboarddonutchart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDDONUTCHARTS_ID  = "SELECT id FROM dashboard_dashboarddonutchart";
	
}