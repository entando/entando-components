/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap;

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

public class DashboardMapDAO extends AbstractSearcherDAO implements IDashboardMapDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardMapDAO.class);

    @Override
    public int countDashboardMaps(FieldSearchFilter[] filters) {
        Integer dashboardMaps = null;
        try {
            dashboardMaps = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardMaps", t);
            throw new RuntimeException("error in count dashboardMaps", t);
        }
        return dashboardMaps;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardmap";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardMaps(FieldSearchFilter[] filters) {
            List<Integer> dashboardMapsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardMapsId.add(Integer.parseInt(idString)));
        return dashboardMapsId;
        }


	@Override
	public List<Integer> loadDashboardMaps() {
		List<Integer> dashboardMapsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDMAPS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardMapsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardMap list",  t);
			throw new RuntimeException("Error loading DashboardMap list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardMapsId;
	}
	
	@Override
	public void insertDashboardMap(DashboardMap dashboardMap) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardMap(dashboardMap, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardMap",  t);
			throw new RuntimeException("Error on insert dashboardMap", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardMap(DashboardMap dashboardMap, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDMAP);
			int index = 1;
			stat.setInt(index++, dashboardMap.getId());
 			stat.setString(index++, dashboardMap.getWidgetId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardMap",  t);
			throw new RuntimeException("Error on insert dashboardMap", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardMap(DashboardMap dashboardMap) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardMap(dashboardMap, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardMap {}", dashboardMap.getId(),  t);
			throw new RuntimeException("Error updating dashboardMap", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardMap(DashboardMap dashboardMap, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDMAP);
			int index = 1;

 			stat.setString(index++, dashboardMap.getWidgetId());
			stat.setInt(index++, dashboardMap.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardMap {}", dashboardMap.getId(),  t);
			throw new RuntimeException("Error updating dashboardMap", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardMap(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardMap(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardMap {}", id, t);
			throw new RuntimeException("Error deleting dashboardMap", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardMap(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDMAP);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardMap {}", id, t);
			throw new RuntimeException("Error deleting dashboardMap", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardMap loadDashboardMap(int id) {
		DashboardMap dashboardMap = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardMap = this.loadDashboardMap(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardMap with id {}", id, t);
			throw new RuntimeException("Error loading dashboardMap with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardMap;
	}

	public DashboardMap loadDashboardMap(int id, Connection conn) {
		DashboardMap dashboardMap = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDMAP);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardMap = this.buildDashboardMapFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardMap with id {}", id, t);
			throw new RuntimeException("Error loading dashboardMap with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardMap;
	}

	protected DashboardMap buildDashboardMapFromRes(ResultSet res) {
		DashboardMap dashboardMap = null;
		try {
			dashboardMap = new DashboardMap();				
			dashboardMap.setId(res.getInt("id"));
			dashboardMap.setWidgetId(res.getString("widgetid"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardMapFromRes", t);
		}
		return dashboardMap;
	}

	private static final String ADD_DASHBOARDMAP = "INSERT INTO dashboard_dashboardmap (id, widgetid ) VALUES (?, ? )";

	private static final String UPDATE_DASHBOARDMAP = "UPDATE dashboard_dashboardmap SET widgetid=? WHERE id = ?";

	private static final String DELETE_DASHBOARDMAP = "DELETE FROM dashboard_dashboardmap WHERE id = ?";
	
	private static final String LOAD_DASHBOARDMAP = "SELECT id, widgetid  FROM dashboard_dashboardmap WHERE id = ?";
	
	private static final String LOAD_DASHBOARDMAPS_ID  = "SELECT id FROM dashboard_dashboardmap";
	
}