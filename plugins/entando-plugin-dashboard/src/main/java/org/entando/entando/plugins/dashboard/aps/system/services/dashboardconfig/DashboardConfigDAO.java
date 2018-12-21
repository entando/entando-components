/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

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

public class DashboardConfigDAO extends AbstractSearcherDAO implements IDashboardConfigDAO {

	private static final Logger logger =  LoggerFactory.getLogger(DashboardConfigDAO.class);

    @Override
    public int countDashboardConfigs(FieldSearchFilter[] filters) {
        Integer dashboardConfigs = null;
        try {
            dashboardConfigs = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count dashboardConfigs", t);
            throw new RuntimeException("error in count dashboardConfigs", t);
        }
        return dashboardConfigs;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "dashboard_dashboardconfig";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchDashboardConfigs(FieldSearchFilter[] filters) {
            List<Integer> dashboardConfigsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> dashboardConfigsId.add(Integer.parseInt(idString)));
        return dashboardConfigsId;
        }


	@Override
	public List<Integer> loadDashboardConfigs() {
		List<Integer> dashboardConfigsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DASHBOARDCONFIGS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				dashboardConfigsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading DashboardConfig list",  t);
			throw new RuntimeException("Error loading DashboardConfig list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardConfigsId;
	}
	
	@Override
	public void insertDashboardConfig(DashboardConfig dashboardConfig) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertDashboardConfig(dashboardConfig, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert dashboardConfig",  t);
			throw new RuntimeException("Error on insert dashboardConfig", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertDashboardConfig(DashboardConfig dashboardConfig, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_DASHBOARDCONFIG);
			int index = 1;
			stat.setInt(index++, dashboardConfig.getId());
 			stat.setString(index++, dashboardConfig.getServerDescription());
 			stat.setString(index++, dashboardConfig.getServerURI());
 			if(StringUtils.isNotBlank(dashboardConfig.getUsername())) {
				stat.setString(index++, dashboardConfig.getUsername());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(dashboardConfig.getPassword())) {
				stat.setString(index++, dashboardConfig.getPassword());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(dashboardConfig.getToken())) {
				stat.setString(index++, dashboardConfig.getToken());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.setInt(index++, dashboardConfig.getTimeConnection());
			stat.setInt(index++, dashboardConfig.getActive());
			stat.setInt(index++, dashboardConfig.getDebug());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert dashboardConfig",  t);
			throw new RuntimeException("Error on insert dashboardConfig", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateDashboardConfig(DashboardConfig dashboardConfig) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateDashboardConfig(dashboardConfig, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating dashboardConfig {}", dashboardConfig.getId(),  t);
			throw new RuntimeException("Error updating dashboardConfig", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateDashboardConfig(DashboardConfig dashboardConfig, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_DASHBOARDCONFIG);
			int index = 1;

 			stat.setString(index++, dashboardConfig.getServerDescription());
 			stat.setString(index++, dashboardConfig.getServerURI());
 			if(StringUtils.isNotBlank(dashboardConfig.getUsername())) {
				stat.setString(index++, dashboardConfig.getUsername());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(dashboardConfig.getPassword())) {
				stat.setString(index++, dashboardConfig.getPassword());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(dashboardConfig.getToken())) {
				stat.setString(index++, dashboardConfig.getToken());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.setInt(index++, dashboardConfig.getTimeConnection());
			stat.setInt(index++, dashboardConfig.getActive());
			stat.setInt(index++, dashboardConfig.getDebug());
			stat.setInt(index++, dashboardConfig.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating dashboardConfig {}", dashboardConfig.getId(),  t);
			throw new RuntimeException("Error updating dashboardConfig", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeDashboardConfig(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeDashboardConfig(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting dashboardConfig {}", id, t);
			throw new RuntimeException("Error deleting dashboardConfig", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeDashboardConfig(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_DASHBOARDCONFIG);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting dashboardConfig {}", id, t);
			throw new RuntimeException("Error deleting dashboardConfig", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public DashboardConfig loadDashboardConfig(int id) {
		DashboardConfig dashboardConfig = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			dashboardConfig = this.loadDashboardConfig(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading dashboardConfig with id {}", id, t);
			throw new RuntimeException("Error loading dashboardConfig with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return dashboardConfig;
	}

	public DashboardConfig loadDashboardConfig(int id, Connection conn) {
		DashboardConfig dashboardConfig = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_DASHBOARDCONFIG);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				dashboardConfig = this.buildDashboardConfigFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading dashboardConfig with id {}", id, t);
			throw new RuntimeException("Error loading dashboardConfig with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return dashboardConfig;
	}

	protected DashboardConfig buildDashboardConfigFromRes(ResultSet res) {
		DashboardConfig dashboardConfig = null;
		try {
			dashboardConfig = new DashboardConfig();				
			dashboardConfig.setId(res.getInt("id"));
			dashboardConfig.setServerDescription(res.getString("serverdescription"));
			dashboardConfig.setServerURI(res.getString("serveruri"));
			dashboardConfig.setUsername(res.getString("username"));
			dashboardConfig.setPassword(res.getString("password"));
			dashboardConfig.setToken(res.getString("token"));
			dashboardConfig.setTimeConnection(res.getInt("timeconnection"));
			dashboardConfig.setActive(res.getInt("active"));
			dashboardConfig.setDebug(res.getInt("debug"));
		} catch (Throwable t) {
			logger.error("Error in buildDashboardConfigFromRes", t);
		}
		return dashboardConfig;
	}

	private static final String ADD_DASHBOARDCONFIG = "INSERT INTO dashboard_dashboardconfig (id, serverdescription, serveruri, username, password, token, timeconnection, active, debug ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_DASHBOARDCONFIG = "UPDATE dashboard_dashboardconfig SET  serverdescription=?,  serveruri=?,  username=?,  password=?,  token=?,  timeconnection=?,  active=?, debug=? WHERE id = ?";

	private static final String DELETE_DASHBOARDCONFIG = "DELETE FROM dashboard_dashboardconfig WHERE id = ?";
	
	private static final String LOAD_DASHBOARDCONFIG = "SELECT id, serverdescription, serveruri, username, password, token, timeconnection, active, debug  FROM dashboard_dashboardconfig WHERE id = ?";
	
	private static final String LOAD_DASHBOARDCONFIGS_ID  = "SELECT id FROM dashboard_dashboardconfig";
	
}