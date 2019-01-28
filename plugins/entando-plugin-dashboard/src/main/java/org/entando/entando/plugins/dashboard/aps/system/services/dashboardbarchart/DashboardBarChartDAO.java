/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart;

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

public class DashboardBarChartDAO extends AbstractSearcherDAO implements IDashboardBarChartDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardBarChartDAO.class);

    @Override
    public int countDashboardBarCharts(FieldSearchFilter[] filters) {
        Integer dashboardBarCharts = null;
        try {
            dashboardBarCharts = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardBarCharts", t);
            throw new RuntimeException("error in count dashboardBarCharts", t);
        }
        return dashboardBarCharts;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardbarchart";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardBarCharts(FieldSearchFilter[] filters) {
            List<Integer> dashboardBarChartsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardBarChartsId.add(Integer.parseInt(idString)));
        return dashboardBarChartsId;
        }


	@Override
	public List<Integer> loadDashboardBarCharts() {
		List<Integer> dashboardBarChartsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDBARCHARTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardBarChartsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardBarChart list",  t);
			throw new RuntimeException("Error loading DashboardBarChart list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardBarChartsId;
	}
	
	@Override
	public void insertDashboardBarChart(DashboardBarChart dashboardBarChart) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardBarChart(dashboardBarChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardBarChart",  t);
			throw new RuntimeException("Error on insert dashboardBarChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardBarChart(DashboardBarChart dashboardBarChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDBARCHART);
			int index = 1;
			stat.setInt(index++, dashboardBarChart.getId());
 			stat.setString(index++, dashboardBarChart.getWidgetId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardBarChart",  t);
			throw new RuntimeException("Error on insert dashboardBarChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardBarChart(DashboardBarChart dashboardBarChart) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardBarChart(dashboardBarChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardBarChart {}", dashboardBarChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardBarChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardBarChart(DashboardBarChart dashboardBarChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDBARCHART);
			int index = 1;

 			stat.setString(index++, dashboardBarChart.getWidgetId());
			stat.setInt(index++, dashboardBarChart.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardBarChart {}", dashboardBarChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardBarChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardBarChart(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardBarChart(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardBarChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardBarChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardBarChart(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDBARCHART);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardBarChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardBarChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardBarChart loadDashboardBarChart(int id) {
		DashboardBarChart dashboardBarChart = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardBarChart = this.loadDashboardBarChart(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardBarChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardBarChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardBarChart;
	}

	public DashboardBarChart loadDashboardBarChart(int id, Connection conn) {
		DashboardBarChart dashboardBarChart = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDBARCHART);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardBarChart = this.buildDashboardBarChartFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardBarChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardBarChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardBarChart;
	}

	protected DashboardBarChart buildDashboardBarChartFromRes(ResultSet res) {
		DashboardBarChart dashboardBarChart = null;
		try {
			dashboardBarChart = new DashboardBarChart();				
			dashboardBarChart.setId(res.getInt("id"));
			dashboardBarChart.setWidgetId(res.getString("widgetid"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardBarChartFromRes", t);
		}
		return dashboardBarChart;
	}

	private static final String ADD_DASHBOARDBARCHART = "INSERT INTO dashboard_dashboardbarchart (id, widgetid ) VALUES (?, ? )";

	private static final String UPDATE_DASHBOARDBARCHART = "UPDATE dashboard_dashboardbarchart SET widgetid=? WHERE id = ?";

	private static final String DELETE_DASHBOARDBARCHART = "DELETE FROM dashboard_dashboardbarchart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDBARCHART = "SELECT id, widgetid  FROM dashboard_dashboardbarchart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDBARCHARTS_ID  = "SELECT id FROM dashboard_dashboardbarchart";
	
}