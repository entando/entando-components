/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;

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

public class IotConfigDAO extends AbstractSearcherDAO implements IIotConfigDAO {

	private static final Logger logger =  LoggerFactory.getLogger(IotConfigDAO.class);

    @Override
    public int countIotConfigs(FieldSearchFilter[] filters) {
        Integer iotConfigs = null;
        try {
            iotConfigs = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count iotConfigs", t);
            throw new RuntimeException("error in count iotConfigs", t);
        }
        return iotConfigs;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpiot_iotconfig";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchIotConfigs(FieldSearchFilter[] filters) {
            List<Integer> iotConfigsId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> iotConfigsId.add(Integer.parseInt(idString)));
        return iotConfigsId;
        }


	@Override
	public List<Integer> loadIotConfigs() {
		List<Integer> iotConfigsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_IOTCONFIGS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				iotConfigsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading IotConfig list",  t);
			throw new RuntimeException("Error loading IotConfig list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return iotConfigsId;
	}
	
	@Override
	public void insertIotConfig(IotConfig iotConfig) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertIotConfig(iotConfig, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert iotConfig",  t);
			throw new RuntimeException("Error on insert iotConfig", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertIotConfig(IotConfig iotConfig, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_IOTCONFIG);
			int index = 1;
			stat.setInt(index++, iotConfig.getId());
 			stat.setString(index++, iotConfig.getName());
 			stat.setString(index++, iotConfig.getHostname());
			stat.setInt(index++, iotConfig.getPort());
 			stat.setString(index++, iotConfig.getWebapp());
 			if(StringUtils.isNotBlank(iotConfig.getUsername())) {
				stat.setString(index++, iotConfig.getUsername());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(iotConfig.getPassword())) {
				stat.setString(index++, iotConfig.getPassword());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(iotConfig.getToken())) {
				stat.setString(index++, iotConfig.getToken());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert iotConfig",  t);
			throw new RuntimeException("Error on insert iotConfig", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateIotConfig(IotConfig iotConfig) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateIotConfig(iotConfig, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating iotConfig {}", iotConfig.getId(),  t);
			throw new RuntimeException("Error updating iotConfig", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateIotConfig(IotConfig iotConfig, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_IOTCONFIG);
			int index = 1;

 			stat.setString(index++, iotConfig.getName());
 			stat.setString(index++, iotConfig.getHostname());
			stat.setInt(index++, iotConfig.getPort());
 			stat.setString(index++, iotConfig.getWebapp());
 			if(StringUtils.isNotBlank(iotConfig.getUsername())) {
				stat.setString(index++, iotConfig.getUsername());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(iotConfig.getPassword())) {
				stat.setString(index++, iotConfig.getPassword());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(iotConfig.getToken())) {
				stat.setString(index++, iotConfig.getToken());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.setInt(index++, iotConfig.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating iotConfig {}", iotConfig.getId(),  t);
			throw new RuntimeException("Error updating iotConfig", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeIotConfig(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeIotConfig(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting iotConfig {}", id, t);
			throw new RuntimeException("Error deleting iotConfig", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeIotConfig(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_IOTCONFIG);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting iotConfig {}", id, t);
			throw new RuntimeException("Error deleting iotConfig", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public IotConfig loadIotConfig(int id) {
		IotConfig iotConfig = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			iotConfig = this.loadIotConfig(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading iotConfig with id {}", id, t);
			throw new RuntimeException("Error loading iotConfig with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return iotConfig;
	}

	public IotConfig loadIotConfig(int id, Connection conn) {
		IotConfig iotConfig = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_IOTCONFIG);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				iotConfig = this.buildIotConfigFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading iotConfig with id {}", id, t);
			throw new RuntimeException("Error loading iotConfig with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return iotConfig;
	}

	protected IotConfig buildIotConfigFromRes(ResultSet res) {
		IotConfig iotConfig = null;
		try {
			iotConfig = new IotConfig();				
			iotConfig.setId(res.getInt("id"));
			iotConfig.setName(res.getString("name"));
			iotConfig.setHostname(res.getString("hostname"));
			iotConfig.setPort(res.getInt("port"));
			iotConfig.setWebapp(res.getString("webapp"));
			iotConfig.setUsername(res.getString("username"));
			iotConfig.setPassword(res.getString("password"));
			iotConfig.setToken(res.getString("token"));
		} catch (Throwable t) {
			logger.error("Error in buildIotConfigFromRes", t);
		}
		return iotConfig;
	}

	private static final String ADD_IOTCONFIG = "INSERT INTO jpiot_iotconfig (id, name, hostname, port, webapp, username, password, token ) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_IOTCONFIG = "UPDATE jpiot_iotconfig SET  name=?,  hostname=?,  port=?,  webapp=?,  username=?,  password=?, token=? WHERE id = ?";

	private static final String DELETE_IOTCONFIG = "DELETE FROM jpiot_iotconfig WHERE id = ?";
	
	private static final String LOAD_IOTCONFIG = "SELECT id, name, hostname, port, webapp, username, password, token  FROM jpiot_iotconfig WHERE id = ?";
	
	private static final String LOAD_IOTCONFIGS_ID  = "SELECT id FROM jpiot_iotconfig";
	
}