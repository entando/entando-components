/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;

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

public class IotListDevicesDAO extends AbstractSearcherDAO implements IIotListDevicesDAO {

	private static final Logger logger =  LoggerFactory.getLogger(IotListDevicesDAO.class);

    @Override
    public int countIotListDevicess(FieldSearchFilter[] filters) {
        Integer iotListDevicess = null;
        try {
            iotListDevicess = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count iotListDevicess", t);
            throw new RuntimeException("error in count iotListDevicess", t);
        }
        return iotListDevicess;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpiot_iotlistdevices";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

    @Override
    public List<Integer> searchIotListDevicess(FieldSearchFilter[] filters) {
            List<Integer> iotListDevicessId = new ArrayList<>();
        List<String> masterList = super.searchId(filters);
        masterList.stream().forEach(idString -> iotListDevicessId.add(Integer.parseInt(idString)));
        return iotListDevicessId;
        }


	@Override
	public List<Integer> loadIotListDevicess() {
		List<Integer> iotListDevicessId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_IOTLISTDEVICESS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				iotListDevicessId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading IotListDevices list",  t);
			throw new RuntimeException("Error loading IotListDevices list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return iotListDevicessId;
	}
	
	@Override
	public void insertIotListDevices(IotListDevices iotListDevices) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertIotListDevices(iotListDevices, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert iotListDevices",  t);
			throw new RuntimeException("Error on insert iotListDevices", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertIotListDevices(IotListDevices iotListDevices, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_IOTLISTDEVICES);
			int index = 1;
			stat.setInt(index++, iotListDevices.getId());
 			stat.setString(index++, iotListDevices.getWidgetTitle());
 			stat.setString(index++, iotListDevices.getDatasource());
 			stat.setString(index++, iotListDevices.getContext());
			stat.setInt(index++, iotListDevices.getDownload());
			stat.setInt(index++, iotListDevices.getFilter());
			stat.setInt(index++, iotListDevices.getAllColumns());
 			stat.setString(index++, iotListDevices.getColumns());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert iotListDevices",  t);
			throw new RuntimeException("Error on insert iotListDevices", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateIotListDevices(IotListDevices iotListDevices) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateIotListDevices(iotListDevices, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating iotListDevices {}", iotListDevices.getId(),  t);
			throw new RuntimeException("Error updating iotListDevices", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateIotListDevices(IotListDevices iotListDevices, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_IOTLISTDEVICES);
			int index = 1;

 			stat.setString(index++, iotListDevices.getWidgetTitle());
 			stat.setString(index++, iotListDevices.getDatasource());
 			stat.setString(index++, iotListDevices.getContext());
			stat.setInt(index++, iotListDevices.getDownload());
			stat.setInt(index++, iotListDevices.getFilter());
			stat.setInt(index++, iotListDevices.getAllColumns());
 			stat.setString(index++, iotListDevices.getColumns());
			stat.setInt(index++, iotListDevices.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating iotListDevices {}", iotListDevices.getId(),  t);
			throw new RuntimeException("Error updating iotListDevices", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeIotListDevices(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeIotListDevices(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting iotListDevices {}", id, t);
			throw new RuntimeException("Error deleting iotListDevices", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeIotListDevices(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_IOTLISTDEVICES);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting iotListDevices {}", id, t);
			throw new RuntimeException("Error deleting iotListDevices", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public IotListDevices loadIotListDevices(int id) {
		IotListDevices iotListDevices = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			iotListDevices = this.loadIotListDevices(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading iotListDevices with id {}", id, t);
			throw new RuntimeException("Error loading iotListDevices with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return iotListDevices;
	}

	public IotListDevices loadIotListDevices(int id, Connection conn) {
		IotListDevices iotListDevices = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_IOTLISTDEVICES);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				iotListDevices = this.buildIotListDevicesFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading iotListDevices with id {}", id, t);
			throw new RuntimeException("Error loading iotListDevices with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return iotListDevices;
	}

	protected IotListDevices buildIotListDevicesFromRes(ResultSet res) {
		IotListDevices iotListDevices = null;
		try {
			iotListDevices = new IotListDevices();				
			iotListDevices.setId(res.getInt("id"));
			iotListDevices.setWidgetTitle(res.getString("widgettitle"));
			iotListDevices.setDatasource(res.getString("datasource"));
			iotListDevices.setContext(res.getString("context"));
			iotListDevices.setDownload(res.getInt("download"));
			iotListDevices.setFilter(res.getInt("filter"));
			iotListDevices.setAllColumns(res.getInt("allcolumns"));
			iotListDevices.setColumns(res.getString("columns"));
		} catch (Throwable t) {
			logger.error("Error in buildIotListDevicesFromRes", t);
		}
		return iotListDevices;
	}

	private static final String ADD_IOTLISTDEVICES = "INSERT INTO jpiot_iotlistdevices (id, widgettitle, datasource, context, download, filter, allcolumns, columns ) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_IOTLISTDEVICES = "UPDATE jpiot_iotlistdevices SET  widgettitle=?,  datasource=?,  context=?,  download=?,  filter=?,  allcolumns=?, columns=? WHERE id = ?";

	private static final String DELETE_IOTLISTDEVICES = "DELETE FROM jpiot_iotlistdevices WHERE id = ?";
	
	private static final String LOAD_IOTLISTDEVICES = "SELECT id, widgettitle, datasource, context, download, filter, allcolumns, columns  FROM jpiot_iotlistdevices WHERE id = ?";
	
	private static final String LOAD_IOTLISTDEVICESS_ID  = "SELECT id FROM jpiot_iotlistdevices";
	
}