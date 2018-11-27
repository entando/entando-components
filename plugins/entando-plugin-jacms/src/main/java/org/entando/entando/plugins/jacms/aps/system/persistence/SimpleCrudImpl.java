package org.entando.entando.plugins.jacms.aps.system.persistence;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.web.common.model.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public abstract class SimpleCrudImpl<T extends Identifiable<ID>, ID> extends AbstractDAO {

    /**
     * Used for logging
     * @return A logging friendly name for the entity being persisted
     */
    abstract String getEntityName();

    abstract Logger getLogger();

    abstract PreparedStatement getCreatePreparedStatement(T model, Connection conn) throws SQLException;
    abstract PreparedStatement getUpdatePreparedStatement(T model, Connection conn) throws SQLException;
    abstract PreparedStatement getDeletePreparedStatement(ID id, Connection conn) throws SQLException;
    abstract PreparedStatement getFindOnePreparedStatement(ID id, Connection conn) throws SQLException;
    abstract String getFindAllSql();

    abstract T loadEntity(ResultSet res) throws SQLException;

    public T create(T model) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = prepareWritingConnection();
            preparedStatement = getCreatePreparedStatement(model, conn);
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            handleError("adding", model.getId(), t, conn);
        } finally {
            closeDaoResources(null, preparedStatement, conn);
        }
        return model;
    }

    public T update(T model) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = prepareWritingConnection();
            preparedStatement = getUpdatePreparedStatement(model, conn);
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            handleError("updating", model.getId(), t, conn);
        } finally {
            closeDaoResources(null, preparedStatement, conn);
        }
        return model;
    }

    public void delete(ID id) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = prepareWritingConnection();
            preparedStatement = getDeletePreparedStatement(id, conn);
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            handleError("deleting", id, t, conn);
        } finally {
            closeDaoResources(null, preparedStatement, conn);
        }
    }

    public Optional<T> findById(ID id) {
        T entity = null;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            preparedStatement = getFindOnePreparedStatement(id, conn);
            res = preparedStatement.executeQuery();

            if (res.next()) {
                entity = loadEntity(preparedStatement.getResultSet());
            }
        } catch (Throwable t) {
            handleError("loading", id, t, conn);
        } finally{
            closeDaoResources(res, preparedStatement, conn);
        }

        return Optional.ofNullable(entity);
    }

    public PagedMetadata<T> list() {
        List<T> entities = new ArrayList<>();

        Connection conn = null;
        Statement stat = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            stat = conn.createStatement();
            res = stat.executeQuery(getFindAllSql());

            while(res.next()){
                entities.add(loadEntity(res));
            }
        } catch (Throwable t) {
            handleError("loading all ", null, t, conn);
        } finally{
            closeDaoResources(res, stat, conn);
        }

        // TODO Fix pagination
        SearcherDaoPaginatedResult<T> result = new SearcherDaoPaginatedResult<>(entities.size(),
                entities);

        return new PagedMetadata<>(new RestListRequest(), result);
    }


    private void handleError(String operation, ID id, Throwable t, Connection conn) {
        executeRollback(conn);

        String errorMessage = createErrorMessage(operation, id);
        getLogger().error(errorMessage,  t);
        throw new RuntimeException(errorMessage, t);
    }

    private String createErrorMessage(String operation, ID id) {
        String errorMessage = String.format("Error %s %s", operation, getEntityName());

        if (id != null) {
            errorMessage += " with id " + id;
        }

        return errorMessage;
    }

    private Connection prepareWritingConnection() throws ApsSystemException, SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        return conn;
    }
}
