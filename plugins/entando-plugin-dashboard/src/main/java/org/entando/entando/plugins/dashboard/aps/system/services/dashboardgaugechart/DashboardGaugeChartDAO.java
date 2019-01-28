/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart;

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

public class DashboardGaugeChartDAO extends AbstractSearcherDAO implements IDashboardGaugeChartDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardGaugeChartDAO.class);

    @Override
    public int countDashboardGaugeCharts(FieldSearchFilter[] filters) {
        Integer dashboardGaugeCharts = null;
        try {
            dashboardGaugeCharts = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardGaugeCharts", t);
            throw new RuntimeException("error in count dashboardGaugeCharts", t);
        }
        return dashboardGaugeCharts;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardgaugechart";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardGaugeCharts(FieldSearchFilter[] filters) {
            List<Integer> dashboardGaugeChartsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardGaugeChartsId.add(Integer.parseInt(idString)));
        return dashboardGaugeChartsId;
        }


	@Override
	public List<Integer> loadDashboardGaugeCharts() {
		List<Integer> dashboardGaugeChartsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDGAUGECHARTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardGaugeChartsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardGaugeChart list",  t);
			throw new RuntimeException("Error loading DashboardGaugeChart list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardGaugeChartsId;
	}
	
	@Override
	public void insertDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardGaugeChart(dashboardGaugeChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardGaugeChart",  t);
			throw new RuntimeException("Error on insert dashboardGaugeChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDGAUGECHART);
			int index = 1;
			stat.setInt(index++, dashboardGaugeChart.getId());
 			stat.setString(index++, dashboardGaugeChart.getWidgetId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardGaugeChart",  t);
			throw new RuntimeException("Error on insert dashboardGaugeChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardGaugeChart(dashboardGaugeChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardGaugeChart {}", dashboardGaugeChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardGaugeChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDGAUGECHART);
			int index = 1;

 			stat.setString(index++, dashboardGaugeChart.getWidgetId());
			stat.setInt(index++, dashboardGaugeChart.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardGaugeChart {}", dashboardGaugeChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardGaugeChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardGaugeChart(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardGaugeChart(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardGaugeChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardGaugeChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardGaugeChart(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDGAUGECHART);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardGaugeChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardGaugeChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardGaugeChart loadDashboardGaugeChart(int id) {
		DashboardGaugeChart dashboardGaugeChart = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardGaugeChart = this.loadDashboardGaugeChart(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardGaugeChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardGaugeChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardGaugeChart;
	}

	public DashboardGaugeChart loadDashboardGaugeChart(int id, Connection conn) {
		DashboardGaugeChart dashboardGaugeChart = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDGAUGECHART);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardGaugeChart = this.buildDashboardGaugeChartFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardGaugeChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardGaugeChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardGaugeChart;
	}

	protected DashboardGaugeChart buildDashboardGaugeChartFromRes(ResultSet res) {
		DashboardGaugeChart dashboardGaugeChart = null;
		try {
			dashboardGaugeChart = new DashboardGaugeChart();				
			dashboardGaugeChart.setId(res.getInt("id"));
			dashboardGaugeChart.setWidgetId(res.getString("widgetid"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardGaugeChartFromRes", t);
		}
		return dashboardGaugeChart;
	}

	private static final String ADD_DASHBOARDGAUGECHART = "INSERT INTO dashboard_dashboardgaugechart (id, widgetid ) VALUES (?, ? )";

	private static final String UPDATE_DASHBOARDGAUGECHART = "UPDATE dashboard_dashboardgaugechart SET widgetid=? WHERE id = ?";

	private static final String DELETE_DASHBOARDGAUGECHART = "DELETE FROM dashboard_dashboardgaugechart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDGAUGECHART = "SELECT id, widgetid  FROM dashboard_dashboardgaugechart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDGAUGECHARTS_ID  = "SELECT id FROM dashboard_dashboardgaugechart";
	
}