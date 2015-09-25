/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepGuiConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.TypeVersionGuiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class FormTypeGuiDAO extends AbstractDAO implements IFormTypeGuiDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(FormTypeGuiDAO.class);

	@Override
	public Map<String, StepGuiConfig> getWorkGuiConfigs(String formTypeCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, StepGuiConfig> configs = new HashMap<String, StepGuiConfig>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_WORK_GUIS);
			stat.setString(1, formTypeCode);
			res = stat.executeQuery();
			while (res.next()) {
				StepGuiConfig config = new StepGuiConfig();
				config.setFormTypeCode(formTypeCode);
				config.setStepCode(res.getString("stepcode"));
				config.setUserGui(res.getString("gui"));
				config.setUserCss(res.getString("css"));
				configs.put(config.getStepCode(), config);
			}
		} catch (Throwable t) {
			_logger.error("Error loading work gui configs",  t);
			throw new RuntimeException("Error loading work gui configs", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return configs;
	}
	
	@Override
	public StepGuiConfig getWorkGuiConfig(String formTypeCode, String stepCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		StepGuiConfig config = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_WORK_GUI);
			stat.setString(1, formTypeCode);
			stat.setString(2, stepCode);
			res = stat.executeQuery();
			if (res.next()) {
				config = new StepGuiConfig();
				config.setFormTypeCode(formTypeCode);
				config.setStepCode(stepCode);
				config.setUserGui(res.getString("gui"));
				config.setUserCss(res.getString("css"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading work gui config",  t);
			throw new RuntimeException("Error loading work gui config", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return config;
	}
	
	@Override
	public void saveWorkGuiConfig(StepGuiConfig config) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteWorkGuiConfig(config.getFormTypeCode(), config.getStepCode(), conn);
			stat = conn.prepareStatement(ADD_WORK_GUI);
			//typecode, stepcode, gui, css
			stat.setString(1, config.getFormTypeCode());
			stat.setString(2, config.getStepCode());
			stat.setString(3, config.getUserGui());
			stat.setString(4, config.getUserCss());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error saving work gui - typeCode {} - stepCode {}", config.getFormTypeCode(), config.getStepCode(),  t);
			throw new RuntimeException("Error saving work gui - typeCode " + config.getFormTypeCode() + " - stepCode " + config.getStepCode(), t);
		} finally {
			super.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteAllWorkGuiConfig(String typeCode) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteAllWorkGuiConfig(typeCode, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting work gui - typeCode {}", typeCode,  t);
			throw new RuntimeException("Error deleting work gui - typeCode " + typeCode, t);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteWorkGuiConfig(String typeCode, String stepCode) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteWorkGuiConfig(typeCode, stepCode, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting work gui - typeCode {} - stepCode {}",typeCode, stepCode,  t);
			throw new RuntimeException("Error deleting work gui - typeCode " + typeCode + " - stepCode " + stepCode, t);
		} finally {
			closeConnection(conn);
		}
	}
	
	protected void deleteWorkGuiConfig(String typeCode, String stepCode, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_WORK_GUI);
			stat.setString(1, typeCode);
			stat.setString(2, stepCode);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting work gui - typeCode {} - stepCode {}", typeCode, stepCode,  t);
			throw new RuntimeException("Error deleting work gui - typeCode " + typeCode + " - stepCode " + stepCode, t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected void deleteAllWorkGuiConfig(String typeCode, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_ALL_WORK_GUI);
			stat.setString(1, typeCode);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting work gui - typeCode {}", typeCode,  t);
			throw new RuntimeException("Error deleting work gui - typeCode " + typeCode, t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	@Override
	public void deleteTypeVersion(String typeCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			deleteGui(conn, typeCode);
			stat = conn.prepareStatement(DELETE_VERSION_CONFIG);
			stat.setString(1, typeCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting typeVersion - typeCode {}", typeCode,  t);
			throw new RuntimeException("Error deleting typeVersion - typeCode " + typeCode, t);
		} finally {
			closeConnection(conn);
		}
	}
	
	private void deleteGui(Connection conn, String typeCode) throws SQLException {
		PreparedStatement stat;
		stat = conn.prepareStatement(DELETE_GUI);
		stat.setString(1, typeCode);
		stat.executeUpdate();
	}
	
	
	
	
	
	// ***************** ******************
	
	@Override
	public TypeVersionGuiConfig getTypeVersionGui(String formTypeCode) {
		int lastVersion = this.extractMax(formTypeCode, "version");
		if (0 == lastVersion) return null;
		return this.getTypeVersionGui(formTypeCode, lastVersion);
	}
	
	@Override
	public TypeVersionGuiConfig getTypeVersionGui(String formTypeCode, Integer version) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		TypeVersionGuiConfig config = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_VERSION_CONFIG);
			stat.setString(1, formTypeCode);
			stat.setInt(2, version);
			res = stat.executeQuery();
			config = this.createConfig(formTypeCode, res);
		} catch (Throwable t) {
			_logger.error("Error loading type version gui", t);
			throw new RuntimeException("Error loading type version gui", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return config;
	}
	
	protected TypeVersionGuiConfig createConfig(String formTypeCode, ResultSet res) throws Throwable {
		TypeVersionGuiConfig config = null;
		Integer currentCode = null;
		while (res.next()) {
			if (null == config) {
				config = new TypeVersionGuiConfig();
			}
			Integer code = res.getInt(1);
			if (null == currentCode) {
				config.setCode(code);
				config.setFormTypeCode(formTypeCode);
				config.setVersion(res.getInt(3));
				config.setPrototypeXml(res.getString(4));
				config.setStepsConfigXml(res.getString(5));
				currentCode = code;
			}
			StepGuiConfig stepGuiConfig = new StepGuiConfig();
			stepGuiConfig.setFormTypeCode(formTypeCode);
			stepGuiConfig.setStepCode(res.getString(6));
			stepGuiConfig.setUserGui(res.getString(7));
			stepGuiConfig.setUserCss(res.getString(8));
			config.getGuiConfigs().add(stepGuiConfig);
		}
		return config;
	}
	
	@Override
	public synchronized Integer addTypeVersionGui(TypeVersionGuiConfig config) {
		int lastVersion = this.extractMax(config.getFormTypeCode(), "version");
		int lastCode = this.extractMax(null, "code");
		config.setVersion(++lastVersion);
		config.setCode(++lastCode);
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_VERSION_CONFIG);
			//code, typecode, version, modelxml, stepxml
			stat.setInt(1, config.getCode());
			stat.setString(2, config.getFormTypeCode());
			stat.setInt(3, config.getVersion());
			stat.setString(4, config.getPrototypeXml());
			stat.setString(5, config.getStepsConfigXml());
			stat.executeUpdate();
			this.addGuiConfigs(config.getCode(), config.getGuiConfigs(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error saving version type - typeCode {}", config.getFormTypeCode(),  t);
			throw new RuntimeException("Error saving version type - typeCode " + config.getFormTypeCode(), t);
		} finally {
			super.closeDaoResources(null, stat, conn);
		}
		return lastVersion;
	}
	
	private void addGuiConfigs(Integer code, List<StepGuiConfig> guiConfigs, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_GUI);
			//code, stepcode, gui, css
			if (null != guiConfigs) {
				for (int i = 0; i < guiConfigs.size(); i++) {
					StepGuiConfig stepGuiConfig = guiConfigs.get(i);
					stat.setInt(1, code);
					stat.setString(2, stepGuiConfig.getStepCode());
					stat.setString(3, stepGuiConfig.getUserGui());
					stat.setString(4, stepGuiConfig.getUserCss());
					stat.addBatch();
					stat.clearParameters();
				}
			}
			stat.executeBatch();
		} catch (BatchUpdateException be) {
			_logger.error("Error while adding gui configs",  be);
			throw new RuntimeException("Error while adding gui configs", be);
		} catch (Throwable t) {
			_logger.error("Error while adding gui configs", t);
			throw new RuntimeException("Error while adding gui configs", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected int extractMax(String typeCode, String fieldName) {
		int code = 0;
		Connection conn = null;
		try {
			conn = this.getConnection();
			code = this.extractMax(typeCode, fieldName, conn);
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while extract Max Code", t);
			throw new RuntimeException("Error while extract Max Code", t);
			//processDaoException(t, "Error while extract Max Code", "extractMaxCode");
		} finally {
			closeConnection(conn);
		}
		return code;
	}
	
	protected int extractMax(String typeCode, String fieldName, Connection conn) {
		int id = 0;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			if (null != typeCode) {
				stat = conn.prepareStatement("SELECT MAX(" + fieldName + ") FROM jpwebform_typeversions WHERE typecode = ?");
				stat.setString(1, typeCode);
			} else {
				stat = conn.prepareStatement("SELECT MAX(" + fieldName + ") FROM jpwebform_typeversions");
			}
			res = stat.executeQuery();
			res.next();
			id = res.getInt(1);
		} catch (Throwable t) {
			_logger.error("Error extracting max id", t);
			throw new RuntimeException("Error extracting max id", t);
			//processDaoException(t, "Error extracting max id", "extractMaxId");
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}
	/*
	private static final String SELECT_VERSION_CONFIG_FROM_CODE = // t jpwebform_typeversions   ----   g jpwebform_gui
		"SELECT t.code, t.typecode, t.version, t.modelxml, t.stepxml, "
		+ "g.stepcode, g.gui, g.css "
		+ "FROM jpwebform_typeversions t LEFT JOIN jpwebform_gui g ON t.code = g.code "
		+ " WHERE t.code = ? ";
	*/
	private static final String SELECT_VERSION_CONFIG = // t jpwebform_typeversions   ----   g jpwebform_gui
		"SELECT t.code, t.typecode, t.version, t.modelxml, t.stepxml, "
		+ "g.stepcode, g.gui, g.css "
		+ "FROM jpwebform_typeversions t LEFT JOIN jpwebform_gui g ON t.code = g.code "
		+ " WHERE t.typecode = ? AND t.version = ? ";
	
	private static final String DELETE_VERSION_CONFIG = "DELETE FROM jpwebform_typeversions WHERE typecode = ?";
	private static final String DELETE_GUI = "DELETE FROM jpwebform_gui WHERE jpwebform_gui.code in (select code from jpwebform_typeversions where typecode=?)";
	
	private static final String ADD_VERSION_CONFIG = 
			"INSERT INTO jpwebform_typeversions(code, typecode, version, modelxml, stepxml) VALUES (?, ?, ?, ?, ?)";
	private static final String ADD_GUI = 
			"INSERT INTO jpwebform_gui(code, stepcode, gui, css) VALUES (?, ?, ?, ?)";
	private static final String SELECT_WORK_GUI = "SELECT typecode, stepcode, gui, css FROM jpwebform_workgui WHERE typecode = ? AND stepcode = ?";
	private static final String SELECT_WORK_GUIS = "SELECT typecode, stepcode, gui, css FROM jpwebform_workgui WHERE typecode = ?";
	private static final String DELETE_WORK_GUI = "DELETE FROM jpwebform_workgui WHERE typecode = ? AND stepcode = ?";
	private static final String DELETE_ALL_WORK_GUI = "DELETE FROM jpwebform_workgui WHERE typecode = ?";
	private static final String ADD_WORK_GUI = "INSERT INTO jpwebform_workgui(typecode, stepcode, gui, css) VALUES (?, ?, ?, ?)";


}
/*
 * 
CREATE TABLE jpwebform_typeversions
(
  code integer NOT NULL,
  typecode character varying(30) NOT NULL,
  version integer NOT NULL,
  modelxml character varying NOT NULL,
  stepxml character varying NOT NULL,
  CONSTRAINT jpwebform_typeversions_pkey PRIMARY KEY (code )
)
* 
CREATE TABLE jpwebform_gui
(
  code integer NOT NULL,
  stepcode character varying(40) NOT NULL,
  gui character varying,
  css character varying,
  CONSTRAINT jpwebform_gui_code_fkey FOREIGN KEY (code)
      REFERENCES jpwebform_typeversions (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */
