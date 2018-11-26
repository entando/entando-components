package org.entando.entando.plugins.jacms.aps.system.persistence;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.AttributeDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.AttributeType;
import org.entando.entando.web.common.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class AttributeDaoImpl extends AbstractDAO implements AttributeDao {

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
    public AttributeDto update(AttributeDto model) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, String.valueOf(model.getType()));
            preparedStatement.setString(2, model.getCode());
            preparedStatement.setString(3, model.getName());
            preparedStatement.setBoolean(4, model.isMandatory());
            preparedStatement.setBoolean(5, model.isSearchable());
            preparedStatement.setBoolean(6, model.isFilterable());
            preparedStatement.setLong(7, model.getId());
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error updating content model {} ", model.getId(),  t);
            throw new RuntimeException("Error updating content model " + model.getId(), t);
        } finally {
            closeDaoResources(null, preparedStatement, conn);
        }
        return model;
    }

    @Override
    public AttributeDto create(AttributeDto model) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(CREATE_SQL);
            preparedStatement.setLong(1, model.getId());
            preparedStatement.setString(2, String.valueOf(model.getType()));
            preparedStatement.setString(3, model.getCode());
            preparedStatement.setString(4, model.getName());
            preparedStatement.setBoolean(5, model.isMandatory());
            preparedStatement.setBoolean(6, model.isSearchable());
            preparedStatement.setBoolean(7, model.isFilterable());
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error adding content type {}", model.getId() ,  t);
            throw new RuntimeException("Error adding content model " + model.getId(), t);
        } finally {
            closeDaoResources(null, preparedStatement, conn);
        }
        return model;
    }

    @Override
    public void delete(Long id) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(DELETE_SQL);
            stat.setLong(1, id);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            executeRollback(conn);
            logger.error("Error deleting content type {} ", id,  t);
            throw new RuntimeException("Error deleting content model " + id, t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public PagedMetadata<AttributeDto> list() {
        List<AttributeDto> attributes = new ArrayList<>();

        Connection conn = null;
        Statement stat = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            stat = conn.createStatement();
            res = stat.executeQuery(FIND_ALL_SQL);
            while(res.next()){
                attributes.add(loadContentType(res));
            }
        } catch (Throwable t) {
            logger.error("Error loading content types",  t);
            throw new RuntimeException("Error loading content types", t);
        } finally{
            closeDaoResources(res, stat, conn);
        }

        // TODO Fix pagination
        SearcherDaoPaginatedResult<AttributeDto> result = new SearcherDaoPaginatedResult<>(attributes.size(),
                attributes);

        return new PagedMetadata<>(new RestListRequest(), result);
    }

    @Override
    public Optional<AttributeDto> findById(Long id) {
        AttributeDto attribute = null;

        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            stat = conn.prepareStatement(FIND_ONE_SQL);
            stat.setLong(1, id);
            res = stat.executeQuery();

            if (res.next()) {
                attribute = loadContentType(stat.getResultSet());
            }
        } catch (Throwable t) {
            logger.error("Error loading content types",  t);
            throw new RuntimeException("Error loading content types", t);
        } finally{
            closeDaoResources(res, stat, conn);
        }

        return Optional.ofNullable(attribute);
    }

    private AttributeDto loadContentType(ResultSet res) throws SQLException {
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
