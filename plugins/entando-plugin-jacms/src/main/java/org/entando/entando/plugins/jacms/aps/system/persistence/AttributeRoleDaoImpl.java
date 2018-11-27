package org.entando.entando.plugins.jacms.aps.system.persistence;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeRoleDto;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class AttributeRoleDaoImpl extends SimpleCrudImpl<AttributeRoleDto, Long> implements AttributeRoleDao {

    @Autowired
    public AttributeRoleDaoImpl(DataSource portDataSource) {
        setDataSource(portDataSource);
    }

    private static final Logger logger =  LoggerFactory.getLogger(AttributeRoleDaoImpl.class);

    private static final String CREATE_SQL =
            "INSERT INTO attributeroles (id, name) " +
                    "VALUES (? , ?)";

    private static final String UPDATE_SQL =
            "UPDATE attributeroles " +
                    "SET name = ? " +
                    "WHERE id = ?";

    private static final String DELETE_SQL = "DELETE FROM attributeroles WHERE id = ?";

    private static final String FIND_ALL_SQL = "SELECT id, name " +
            "FROM attributeroles";

    private static final String FIND_ONE_SQL = "SELECT id, name " +
            "FROM attributeroles WHERE id = ?";

    @Override
    String getEntityName() {
        return "AttributeRole";
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    PreparedStatement getCreatePreparedStatement(AttributeRoleDto model, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(CREATE_SQL);
        preparedStatement.setLong(1, model.getId());
        preparedStatement.setString(2, model.getName());
        return preparedStatement;
    }

    @Override
    PreparedStatement getUpdatePreparedStatement(AttributeRoleDto model, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL);
        preparedStatement.setString(1, model.getName());
        preparedStatement.setLong(2, model.getId());
        return preparedStatement;
    }

    @Override
    PreparedStatement getDeletePreparedStatement(Long id, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement;
    }

    @Override
    PreparedStatement getFindOnePreparedStatement(Long id, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_ONE_SQL);
        preparedStatement.setLong(1, id);
        return preparedStatement;
    }

    @Override
    String getFindAllSql() {
        return FIND_ALL_SQL;
    }

    @Override
    AttributeRoleDto loadEntity(ResultSet res) throws SQLException {
        AttributeRoleDto attributeRole = new AttributeRoleDto();
        attributeRole.setId(res.getLong(1));
        attributeRole.setName(res.getString(2));
        return attributeRole;
    }
}
