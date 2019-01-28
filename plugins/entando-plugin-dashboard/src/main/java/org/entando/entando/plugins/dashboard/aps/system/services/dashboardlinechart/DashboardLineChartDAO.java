/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart;

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

public class DashboardLineChartDAO extends AbstractSearcherDAO implements IDashboardLineChartDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardLineChartDAO.class);

    @Override
    public int countDashboardLineCharts(FieldSearchFilter[] filters) {
        Integer dashboardLineCharts = null;
        try {
            dashboardLineCharts = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardLineCharts", t);
            throw new RuntimeException("error in count dashboardLineCharts", t);
        }
        return dashboardLineCharts;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardlinechart";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardLineCharts(FieldSearchFilter[] filters) {
            List<Integer> dashboardLineChartsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardLineChartsId.add(Integer.parseInt(idString)));
        return dashboardLineChartsId;
        }


	@Override
	public List<Integer> loadDashboardLineCharts() {
		List<Integer> dashboardLineChartsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDLINECHARTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardLineChartsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardLineChart list",  t);
			throw new RuntimeException("Error loading DashboardLineChart list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardLineChartsId;
	}
	
	@Override
	public void insertDashboardLineChart(DashboardLineChart dashboardLineChart) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardLineChart(dashboardLineChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardLineChart",  t);
			throw new RuntimeException("Error on insert dashboardLineChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardLineChart(DashboardLineChart dashboardLineChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDLINECHART);
			int index = 1;
			stat.setInt(index++, dashboardLineChart.getId());
 			stat.setString(index++, dashboardLineChart.getWidgetId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardLineChart",  t);
			throw new RuntimeException("Error on insert dashboardLineChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardLineChart(DashboardLineChart dashboardLineChart) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardLineChart(dashboardLineChart, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardLineChart {}", dashboardLineChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardLineChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardLineChart(DashboardLineChart dashboardLineChart, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDLINECHART);
			int index = 1;

 			stat.setString(index++, dashboardLineChart.getWidgetId());
			stat.setInt(index++, dashboardLineChart.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardLineChart {}", dashboardLineChart.getId(),  t);
			throw new RuntimeException("Error updating dashboardLineChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardLineChart(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardLineChart(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardLineChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardLineChart", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardLineChart(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDLINECHART);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardLineChart {}", id, t);
			throw new RuntimeException("Error deleting dashboardLineChart", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardLineChart loadDashboardLineChart(int id) {
		DashboardLineChart dashboardLineChart = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardLineChart = this.loadDashboardLineChart(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardLineChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardLineChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardLineChart;
	}

	public DashboardLineChart loadDashboardLineChart(int id, Connection conn) {
		DashboardLineChart dashboardLineChart = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDLINECHART);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardLineChart = this.buildDashboardLineChartFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardLineChart with id {}", id, t);
			throw new RuntimeException("Error loading dashboardLineChart with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardLineChart;
	}

	protected DashboardLineChart buildDashboardLineChartFromRes(ResultSet res) {
		DashboardLineChart dashboardLineChart = null;
		try {
			dashboardLineChart = new DashboardLineChart();				
			dashboardLineChart.setId(res.getInt("id"));
			dashboardLineChart.setWidgetId(res.getString("widgetid"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardLineChartFromRes", t);
		}
		return dashboardLineChart;
	}

	private static final String ADD_DASHBOARDLINECHART = "INSERT INTO dashboard_dashboardlinechart (id, widgetid ) VALUES (?, ? )";

	private static final String UPDATE_DASHBOARDLINECHART = "UPDATE dashboard_dashboardlinechart SET widgetid=? WHERE id = ?";

	private static final String DELETE_DASHBOARDLINECHART = "DELETE FROM dashboard_dashboardlinechart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDLINECHART = "SELECT id, widgetid  FROM dashboard_dashboardlinechart WHERE id = ?";
	
	private static final String LOAD_DASHBOARDLINECHARTS_ID  = "SELECT id FROM dashboard_dashboardlinechart";
	
}