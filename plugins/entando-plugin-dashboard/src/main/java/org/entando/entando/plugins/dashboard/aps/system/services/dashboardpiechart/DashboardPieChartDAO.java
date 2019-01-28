/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;

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

public class DashboardPieChartDAO extends AbstractSearcherDAO implements IDashboardPieChartDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardPieChartDAO.class);

    @Override
    public int countDashboardPieCharts(FieldSearchFilter[] filters) {
        Integer dashboardPieCharts = null;
        try {
            dashboardPieCharts = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardPieCharts", t);
            throw new RuntimeException("error in count dashboardPieCharts", t);
        }
        return dashboardPieCharts;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardpiechart";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardPieCharts(FieldSearchFilter[] filters) {
            List<Integer> dashboardPieChartsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardPieChartsId.add(Integer.parseInt(idString)));
        return dashboardPieChartsId;
        }


	@Override
	public List<Integer> loadDashboardPieCharts() {
		List<Integer> dashboardPieChartsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDPIECHARTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardPieChartsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardPieChart list",  t);
			throw new RuntimeException("Error loading DashboardPieChart list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardPieChartsId;
	}
	
	@Override
	public void insertDashboardPieChart(DashboardPieChart dashboardPieChart) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardPieChart(dashboardPieChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardPieChart",  t);
			throw new RuntimeException("Error on insert dashboardPieChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardPieChart(DashboardPieChart dashboardPieChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDPIECHART);
			int index = 1;
			stat.setInt(index++, dashboardPieChart.getId());
 			stat.setString(index++, dashboardPieChart.getWidgetId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardPieChart",  t);
			throw new RuntimeException("Error on insert dashboardPieChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardPieChart(DashboardPieChart dashboardPieChart) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardPieChart(dashboardPieChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardPieChart {}", dashboardPieChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardPieChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardPieChart(DashboardPieChart dashboardPieChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDPIECHART);
			int index = 1;

 			stat.setString(index++, dashboardPieChart.getWidgetId());
			stat.setInt(index++, dashboardPieChart.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardPieChart {}", dashboardPieChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardPieChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardPieChart(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardPieChart(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardPieChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardPieChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardPieChart(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDPIECHART);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardPieChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardPieChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardPieChart loadDashboardPieChart(int id) {
		DashboardPieChart dashboardPieChart = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardPieChart = this.loadDashboardPieChart(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardPieChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardPieChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardPieChart;
	}

	public DashboardPieChart loadDashboardPieChart(int id, Connection conn) {
		DashboardPieChart dashboardPieChart = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDPIECHART);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardPieChart = this.buildDashboardPieChartFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardPieChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardPieChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardPieChart;
	}

	protected DashboardPieChart buildDashboardPieChartFromRes(ResultSet res) {
		DashboardPieChart dashboardPieChart = null;
		try {
			dashboardPieChart = new DashboardPieChart();				
			dashboardPieChart.setId(res.getInt("id"));
			dashboardPieChart.setWidgetId(res.getString("widgetid"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardPieChartFromRes", t);
		}
		return dashboardPieChart;
	}

	private static final String ADD_DASHBOARDPIECHART = "INSERT INTO dashboard_dashboardpiechart (id, widgetid ) VALUES (?, ? )";

	private static final String UPDATE_DASHBOARDPIECHART = "UPDATE dashboard_dashboardpiechart SET widgetid=? WHERE id = ?";

	private static final String DELETE_DASHBOARDPIECHART = "DELETE FROM dashboard_dashboardpiechart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDPIECHART = "SELECT id, widgetid  FROM dashboard_dashboardpiechart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDPIECHARTS_ID  = "SELECT id FROM dashboard_dashboardpiechart";
	
}