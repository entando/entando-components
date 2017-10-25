/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.OverrideList;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

public class KieFormOverrideDAO extends AbstractSearcherDAO implements IKieFormOverrideDAO {

	private static final Logger _logger = LoggerFactory.getLogger(KieFormOverrideDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}

	@Override
	protected String getMasterTableName() {
		return "jpkiebpm_kieformoverride";
	}

	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

	@Override
	protected boolean isForceCaseInsensitiveLikeSearch() {
		return true;
	}

	@Override
	public List<Integer> searchKieFormOverrides(FieldSearchFilter[] filters) {
		List<Integer> kieFormOverridesId = new ArrayList<Integer>();

		try {
			List<String> result = super.searchId(filters);
			if (null != result) {
				for (String id : result) {
					kieFormOverridesId.add(Integer.valueOf(id));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in searchKieFormOverrides", t);
			throw new RuntimeException("error in searchKieFormOverrides", t);
		}
		return kieFormOverridesId;
	}

	@Override
	public List<Integer> loadKieFormOverrides() {
		List<Integer> kieFormOverridesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_KIEFORMOVERRIDES_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				kieFormOverridesId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading KieFormOverride list", t);
			throw new RuntimeException("Error loading KieFormOverride list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return kieFormOverridesId;
	}

	@Override
	public void insertKieFormOverride(KieFormOverride kieFormOverride) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			int id = getUniqueId(conn);
			kieFormOverride.setId(id);
			this.insertKieFormOverride(kieFormOverride, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert kieFormOverride", t);
			throw new RuntimeException("Error on insert kieFormOverride", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertKieFormOverride(KieFormOverride kieFormOverride, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_KIEFORMOVERRIDE);
			int index = 1;
			stat.setInt(index++, kieFormOverride.getId());
			Timestamp dateTimestamp = new Timestamp(kieFormOverride.getDate().getTime());
			stat.setTimestamp(index++, dateTimestamp);
			stat.setString(index++, kieFormOverride.getField());
			stat.setString(index++, kieFormOverride.getContainerId());
			stat.setString(index++, kieFormOverride.getProcessId());
			String txt = JAXBHelper.marshall(kieFormOverride.getOverrides(), true, false);
			stat.setString(index++, txt);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert kieFormOverride", t);
			throw new RuntimeException("Error on insert kieFormOverride", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	/**
	 * This inspect the given table and return the id to be used as primary key
	 * for further operations
	 * 
	 * @param query
	 * the query used to inspect the datasource
	 * @param conn
	 * the connection to the datasource
	 * @return The first free id to use as primary key
	 */
	protected int getUniqueId(Connection conn) {
		int id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(MAX_ID);
			res.next();
			id = res.getInt(1) + 1;
		} catch (Throwable t) {
			_logger.error("Error while getting last used ID", t);
			throw new RuntimeException("Error while getting last used ID", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}

	@Override
	public void updateKieFormOverride(KieFormOverride kieFormOverride) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateKieFormOverride(kieFormOverride, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating kieFormOverride {}", kieFormOverride.getId(), t);
			throw new RuntimeException("Error updating kieFormOverride", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateKieFormOverride(KieFormOverride kieFormOverride, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_KIEFORMOVERRIDE);
			int index = 1;

			Timestamp dateTimestamp = new Timestamp(kieFormOverride.getDate().getTime());
			stat.setTimestamp(index++, dateTimestamp);
			stat.setString(index++, kieFormOverride.getField());
			stat.setString(index++, kieFormOverride.getContainerId());
			stat.setString(index++, kieFormOverride.getProcessId());
			String txt = JAXBHelper.marshall(kieFormOverride.getOverrides(), true, false);
			stat.setString(index++, txt);
			stat.setInt(index++, kieFormOverride.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating kieFormOverride {}", kieFormOverride.getId(), t);
			throw new RuntimeException("Error updating kieFormOverride", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeKieFormOverride(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeKieFormOverride(id, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting kieFormOverride {}", id, t);
			throw new RuntimeException("Error deleting kieFormOverride", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void removeKieFormOverride(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_KIEFORMOVERRIDE);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting kieFormOverride {}", id, t);
			throw new RuntimeException("Error deleting kieFormOverride", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public KieFormOverride loadKieFormOverride(int id) {
		KieFormOverride kieFormOverride = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			kieFormOverride = this.loadKieFormOverride(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading kieFormOverride with id {}", id, t);
			throw new RuntimeException("Error loading kieFormOverride with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return kieFormOverride;
	}

	public KieFormOverride loadKieFormOverride(int id, Connection conn) {
		KieFormOverride kieFormOverride = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_KIEFORMOVERRIDE);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				kieFormOverride = this.buildKieFormOverrideFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading kieFormOverride with id {}", id, t);
			throw new RuntimeException("Error loading kieFormOverride with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return kieFormOverride;
	}

	protected KieFormOverride buildKieFormOverrideFromRes(ResultSet res) {
		KieFormOverride kieFormOverride = null;
		try {
			kieFormOverride = new KieFormOverride();
			kieFormOverride.setId(res.getInt("id"));
			Timestamp dateValue = res.getTimestamp("date");
			if (null != dateValue) {
				kieFormOverride.setDate(new Date(dateValue.getTime()));
			}
			kieFormOverride.setField(res.getString("field"));
			kieFormOverride.setContainerId(res.getString("containerid"));
			kieFormOverride.setProcessId(res.getString("processid"));
			String txt = res.getString("override");
			OverrideList ol = (OverrideList) JAXBHelper.unmarshall(txt, OverrideList.class, true, false);
			kieFormOverride.setOverrides(ol);
		} catch (Throwable t) {
			_logger.error("Error in buildKieFormOverrideFromRes", t);
		}
		return kieFormOverride;
	}

	private static final String ADD_KIEFORMOVERRIDE = "INSERT INTO jpkiebpm_kieformoverride (id, date, field, containerid, processid, override ) VALUES (?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_KIEFORMOVERRIDE = "UPDATE jpkiebpm_kieformoverride SET  date=?,  field=?,  containerid=?,  processid=?, override=? WHERE id = ?";

	private static final String DELETE_KIEFORMOVERRIDE = "DELETE FROM jpkiebpm_kieformoverride WHERE id = ?";

	private static final String LOAD_KIEFORMOVERRIDE = "SELECT id, date, field, containerid, processid, override  FROM jpkiebpm_kieformoverride WHERE id = ?";

	private static final String LOAD_KIEFORMOVERRIDES_ID = "SELECT id FROM jpkiebpm_kieformoverride";

	private static final String MAX_ID = "SELECT MAX(id) FROM jpkiebpm_kieformoverride";

}
