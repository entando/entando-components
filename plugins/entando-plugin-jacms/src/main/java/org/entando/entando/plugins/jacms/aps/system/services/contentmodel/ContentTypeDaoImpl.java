package org.entando.entando.plugins.jacms.aps.system.services.contentmodel;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;
import org.entando.entando.web.common.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class ContentTypeDaoImpl extends AbstractDAO implements ContentTypeDao {

    @Autowired
    public ContentTypeDaoImpl(DataSource portDataSource) {
        setDataSource(portDataSource);
    }

    private static final Logger logger =  LoggerFactory.getLogger(ContentTypeDaoImpl.class);

    private static final String DELETE_SQL = "DELETE FROM ContentTypes WHERE id = ? ";

    private static final String CREATE_SQL =
            "INSERT INTO contenttypes (id, code, name, default_content_model, default_content_model_list) " +
                    "VALUES (? , ? , ? , ? , ?)";

    private static final String UPDATE_SQL =
            "UPDATE contenttypes " +
                    "SET code = ?, name = ?, default_content_model = ?, default_content_model_list = ? " +
                    "WHERE id = ?";

    private static final String FIND_ALL_SQL = "SELECT id, code, name, default_content_model, default_content_model_list " +
            "FROM contenttypes";

    private static final String FIND_ONE_SQL = "SELECT id, code, name, default_content_model, default_content_model_list " +
            "FROM contenttypes WHERE id = ?";

    @Override
    public ContentTypeDto create(ContentTypeDto model) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(CREATE_SQL);
            preparedStatement.setLong(1, model.getId());
            preparedStatement.setString(2, model.getCode());
            preparedStatement.setString(3, model.getName());
            preparedStatement.setString(4, String.valueOf(model.getDefaultContentModel()));
            preparedStatement.setString(5, String.valueOf(model.getDefaultContentModelList()));
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
    public ContentTypeDto update(ContentTypeDto model) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(UPDATE_SQL);
            stat.setString(1, model.getCode());
            stat.setString(2, model.getName());
            stat.setString(3, String.valueOf(model.getDefaultContentModel()));
            stat.setString(4, String.valueOf(model.getDefaultContentModelList()));
            stat.setLong(5, model.getId());
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error updating content model {} ", model.getId(),  t);
            throw new RuntimeException("Error updating content model " + model.getId(), t);
        } finally {
            closeDaoResources(null, stat, conn);
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

    public Optional<ContentTypeDto> findById(Long id) {

        ContentTypeDto contentType = null;

        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            stat = conn.prepareStatement(FIND_ONE_SQL);
            stat.setLong(1, id);
            res = stat.executeQuery();

            if (res.next()) {
                contentType = loadContentType(stat.getResultSet());
            }
        } catch (Throwable t) {
            logger.error("Error loading content types",  t);
            throw new RuntimeException("Error loading content types", t);
        } finally{
            closeDaoResources(res, stat, conn);
        }

        return Optional.ofNullable(contentType);
    }


    @Override
    public PagedMetadata<ContentTypeDto> list() {
        List<ContentTypeDto> contentTypes = new ArrayList<>();

        Connection conn = null;
        Statement stat = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            stat = conn.createStatement();
            res = stat.executeQuery(FIND_ALL_SQL);
            while(res.next()){
                contentTypes.add(loadContentType(res));
            }
        } catch (Throwable t) {
            logger.error("Error loading content types",  t);
            throw new RuntimeException("Error loading content types", t);
        } finally{
            closeDaoResources(res, stat, conn);
        }

        // TODO Fix pagination
        SearcherDaoPaginatedResult<ContentTypeDto> result = new SearcherDaoPaginatedResult<>(contentTypes.size(),
                contentTypes);

        return new PagedMetadata<>(new RestListRequest(), result);
    }


    private ContentTypeDto loadContentType(ResultSet res) throws SQLException {
        ContentTypeDto contentType = new ContentTypeDto();
        contentType.setId(res.getLong(1));
        contentType.setCode(res.getString(2));
        contentType.setName(res.getString(3));
        contentType.defaultContentModel(DefaultContentModel.fromValue(res.getString(4)));
        contentType.defaultContentModelList(DefaultContentModel.fromValue(res.getString(5)));
        return contentType;
    }
}
