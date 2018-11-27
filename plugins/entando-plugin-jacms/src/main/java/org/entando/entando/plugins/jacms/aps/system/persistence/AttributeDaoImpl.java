package org.entando.entando.plugins.jacms.aps.system.persistence;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.AttributeType;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class AttributeDaoImpl extends SimpleCrudImpl<AttributeDto, Long> implements AttributeDao {

    @Autowired
    public AttributeDaoImpl(DataSource portDataSource) {
        setDataSource(portDataSource);
    }

    private static final Logger logger =  LoggerFactory.getLogger(AttributeDaoImpl.class);

    private static final String DELETE_SQL = "DELETE FROM attributes WHERE id = ? ";

    private static final String CREATE_SQL =
            "INSERT INTO attributes (id, type, code, name, mandatory, searchable, filterable) " +
                    "VALUES (? , ? , ? , ? , ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE attributes " +
                    "SET type = ?, code = ?, name = ?, mandatory = ?, searchable = ?, filterable = ? " +
                    "WHERE id = ?";

    private static final String FIND_ALL_SQL = "SELECT id, type, code, name, mandatory, searchable, filterable " +
            "FROM attributes";

    private static final String FIND_ONE_SQL = "SELECT id, type, code, name, mandatory, searchable, filterable " +
            "FROM attributes WHERE id = ?";

    @Override
    String getEntityName() {
        return "Attribute";
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    PreparedStatement getCreatePreparedStatement(AttributeDto model, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(CREATE_SQL);
        preparedStatement.setLong(1, model.getId());
        preparedStatement.setString(2, String.valueOf(model.getType()));
        preparedStatement.setString(3, model.getCode());
        preparedStatement.setString(4, model.getName());
        preparedStatement.setBoolean(5, model.isMandatory());
        preparedStatement.setBoolean(6, model.isSearchable());
        preparedStatement.setBoolean(7, model.isFilterable());
        return preparedStatement;
    }

    @Override
    PreparedStatement getUpdatePreparedStatement(AttributeDto model, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL);
        preparedStatement.setString(1, String.valueOf(model.getType()));
        preparedStatement.setString(2, model.getCode());
        preparedStatement.setString(3, model.getName());
        preparedStatement.setBoolean(4, model.isMandatory());
        preparedStatement.setBoolean(5, model.isSearchable());
        preparedStatement.setBoolean(6, model.isFilterable());
        preparedStatement.setLong(7, model.getId());
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
    AttributeDto loadEntity(ResultSet res) throws SQLException {
        AttributeDto attribute = new AttributeDto();
        attribute.setId(res.getLong(1));
        attribute.setType(AttributeType.fromValue(res.getString(2)));
        attribute.setCode(res.getString(3));
        attribute.setName(res.getString(4));
        attribute.setMandatory(res.getBoolean(5));
        attribute.setSearchable(res.getBoolean(6));
        attribute.setFilterable(res.getBoolean(7));
        return attribute;
    }
}
