/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.digitalexchange.install;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.entando.entando.aps.system.init.model.servdb.DigitalExchangeComponentInstallation.*;

@Component
public class DigitalExchangeComponentInstallationDAOImpl extends AbstractDAO implements DigitalExchangeComponentInstallationDAO {

    private static final long serialVersionUID = 1L;

    private static final String ALL_COLS = String.join(", ",
            COL_ID, COL_DIGITAL_EXCHANGE, COL_COMPONENT, COL_PROGRESS, COL_STATUS, COL_ERROR_MESSAGE);

    private static final String INSERT_JOB = String.format(
            "INSERT INTO %s (%s) VALUES (?, ?, ?, ?, ?, ?)",
            TABLE_NAME, ALL_COLS);

    private static final String SELECT_JOB = String.format(
            "SELECT %s FROM %s WHERE %s = ?",
            ALL_COLS, TABLE_NAME, COL_ID);

    private static final String UPDATE_JOB = String.format(
            "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
            TABLE_NAME, COL_DIGITAL_EXCHANGE, COL_COMPONENT, COL_PROGRESS, COL_STATUS, COL_ERROR_MESSAGE, COL_ID);

    @Autowired
    public DigitalExchangeComponentInstallationDAOImpl(DataSource servDataSource) {
        super.setDataSource(servDataSource);
    }

    @Override
    public void createComponentInstallationJob(ComponentInstallationJob job) {
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_JOB)) {

            int i = 0;
            ps.setString(++i, job.getId());
            ps.setString(++i, job.getDigitalExchange());
            ps.setString(++i, job.getComponent());
            ps.setDouble(++i, job.getProgress());
            ps.setString(++i, job.getStatus().toString());
            ps.setString(++i, job.getErrorMessage());

            ps.execute();

        } catch (SQLException | ApsSystemException ex) {
            throw new InstallationException("Unable to store installation job information", ex);
        }
    }

    @Override
    public Optional<ComponentInstallationJob> fingById(String id) {

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_JOB)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    ComponentInstallationJob job = new ComponentInstallationJob();

                    job.setId(rs.getString(COL_ID));
                    job.setDigitalExchange(rs.getString(COL_DIGITAL_EXCHANGE));
                    job.setComponent(rs.getString(COL_COMPONENT));
                    job.setProgress(rs.getInt(COL_PROGRESS));
                    job.setStatus(InstallationStatus.valueOf(rs.getString(COL_STATUS)));
                    job.setErrorMessage(rs.getString(COL_ERROR_MESSAGE));

                    return Optional.of(job);

                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException | ApsSystemException ex) {
            throw new InstallationException("Unable to retrieve installation job information", ex);
        }
    }

    @Override
    public void updateComponentInstallationJob(ComponentInstallationJob job) {
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_JOB)) {

            int i = 0;
            ps.setString(++i, job.getDigitalExchange());
            ps.setString(++i, job.getComponent());
            ps.setDouble(++i, job.getProgress());
            ps.setString(++i, job.getStatus().toString());
            ps.setString(++i, job.getErrorMessage());

            ps.setString(++i, job.getId());

            ps.execute();

        } catch (SQLException | ApsSystemException ex) {
            throw new InstallationException("Unable to update installation job information", ex);
        }
    }

    @Override
    public void deleteComponentInstallationJob(ComponentInstallationJob job) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
