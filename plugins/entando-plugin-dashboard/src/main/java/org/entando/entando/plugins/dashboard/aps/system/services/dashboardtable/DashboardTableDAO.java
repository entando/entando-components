/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

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

public class DashboardTableDAO extends AbstractSearcherDAO implements IDashboardTableDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardTableDAO.class);

    @Override
    public int countDashboardTables(FieldSearchFilter[] filters) {
        Integer dashboardTables = null;
        try {
            dashboardTables = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardTables", t);
            throw new RuntimeException("error in count dashboardTables", t);
        }
        return dashboardTables;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardtable";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardTables(FieldSearchFilter[] filters) {
            List<Integer> dashboardTablesId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardTablesId.add(Integer.parseInt(idString)));
        return dashboardTablesId;
        }


	@Override
	public List<Integer> loadDashboardTables() {
		List<Integer> dashboardTablesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDTABLES_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardTablesId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardTable list",  t);
			throw new RuntimeException("Error loading DashboardTable list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardTablesId;
	}
	
	@Override
	public void insertDashboardTable(DashboardTable dashboardTable) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardTable(dashboardTable, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardTable",  t);
			throw new RuntimeException("Error on insert dashboardTable", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardTable(DashboardTable dashboardTable, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDTABLE);
			int index = 1;
			stat.setInt(index++, dashboardTable.getId());
 			stat.setString(index++, dashboardTable.getWidgetTitle());
 			stat.setString(index++, dashboardTable.getDatasource());
 			stat.setString(index++, dashboardTable.getContext());
			stat.setInt(index++, dashboardTable.getDownload());
			stat.setInt(index++, dashboardTable.getFilter());
			stat.setInt(index++, dashboardTable.getAllColumns());
 			stat.setString(index++, dashboardTable.getColumns());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardTable",  t);
			throw new RuntimeException("Error on insert dashboardTable", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardTable(DashboardTable dashboardTable) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardTable(dashboardTable, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardTable {}", dashboardTable.getId(),  t);
			throw new RuntimeException("Error updating dashboardTable", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardTable(DashboardTable dashboardTable, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDTABLE);
			int index = 1;

 			stat.setString(index++, dashboardTable.getWidgetTitle());
 			stat.setString(index++, dashboardTable.getDatasource());
 			stat.setString(index++, dashboardTable.getContext());
			stat.setInt(index++, dashboardTable.getDownload());
			stat.setInt(index++, dashboardTable.getFilter());
			stat.setInt(index++, dashboardTable.getAllColumns());
 			stat.setString(index++, dashboardTable.getColumns());
			stat.setInt(index++, dashboardTable.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardTable {}", dashboardTable.getId(),  t);
			throw new RuntimeException("Error updating dashboardTable", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardTable(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardTable(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardTable {}", id, t);
			throw new RuntimeException("Error deleting dashboardTable", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardTable(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDTABLE);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardTable {}", id, t);
			throw new RuntimeException("Error deleting dashboardTable", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardTable loadDashboardTable(int id) {
		DashboardTable dashboardTable = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardTable = this.loadDashboardTable(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardTable with id {}", id, t);
			throw new RuntimeException("Error loading dashboardTable with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardTable;
	}

	public DashboardTable loadDashboardTable(int id, Connection conn) {
		DashboardTable dashboardTable = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDTABLE);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardTable = this.buildDashboardTableFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardTable with id {}", id, t);
			throw new RuntimeException("Error loading dashboardTable with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardTable;
	}

	protected DashboardTable buildDashboardTableFromRes(ResultSet res) {
		DashboardTable dashboardTable = null;
		try {
			dashboardTable = new DashboardTable();				
			dashboardTable.setId(res.getInt("id"));
			dashboardTable.setWidgetTitle(res.getString("widgettitle"));
			dashboardTable.setDatasource(res.getString("datasource"));
			dashboardTable.setContext(res.getString("context"));
			dashboardTable.setDownload(res.getInt("download"));
			dashboardTable.setFilter(res.getInt("filter"));
			dashboardTable.setAllColumns(res.getInt("allcolumns"));
			dashboardTable.setColumns(res.getString("columns"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardTableFromRes", t);
		}
		return dashboardTable;
	}

	private static final String ADD_DASHBOARDTABLE = "INSERT INTO dashboard_dashboardtable (id, widgettitle, datasource, context, download, filter, allcolumns, columns ) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_DASHBOARDTABLE = "UPDATE dashboard_dashboardtable SET  widgettitle=?,  datasource=?,  context=?,  download=?,  filter=?,  allcolumns=?, columns=? WHERE id = ?";

	private static final String DELETE_DASHBOARDTABLE = "DELETE FROM dashboard_dashboardtable WHERE id = ?";
	
	private static final String LOAD_DASHBOARDTABLE = "SELECT id, widgettitle, datasource, context, download, filter, allcolumns, columns  FROM dashboard_dashboardtable WHERE id = ?";
	
	private static final String LOAD_DASHBOARDTABLES_ID  = "SELECT id FROM dashboard_dashboardtable";
	
}