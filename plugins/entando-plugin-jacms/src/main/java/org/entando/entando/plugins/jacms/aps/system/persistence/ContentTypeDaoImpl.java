package org.entando.entando.plugins.jacms.aps.system.persistence;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.enums.DefaultContentModel;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class ContentTypeDaoImpl extends SimpleCrudImpl<ContentTypeDto, Long> implements ContentTypeDao {

    @Autowired
    public ContentTypeDaoImpl(DataSource portDataSource) {
        setDataSource(portDataSource);
    }

    private static final Logger logger =  LoggerFactory.getLogger(ContentTypeDaoImpl.class);

    private static final String DELETE_SQL = "DELETE FROM contenttypes WHERE id = ? ";

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
    protected Logger getLogger() {
        return logger;
    }


    @Override
    String getEntityName() {
        return "ContentType";
    }

    @Override
    PreparedStatement getCreatePreparedStatement(ContentTypeDto model, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(CREATE_SQL);
        preparedStatement.setLong(1, model.getId());
        preparedStatement.setString(2, model.getCode());
        preparedStatement.setString(3, model.getName());
        preparedStatement.setString(4, String.valueOf(model.getDefaultContentModel()));
        preparedStatement.setString(5, String.valueOf(model.getDefaultContentModelList()));

        return preparedStatement;
    }

    @Override
    PreparedStatement getUpdatePreparedStatement(ContentTypeDto model, Connection conn) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(UPDATE_SQL);
        stat.setString(1, model.getCode());
        stat.setString(2, model.getName());
        stat.setString(3, String.valueOf(model.getDefaultContentModel()));
        stat.setString(4, String.valueOf(model.getDefaultContentModelList()));
        stat.setLong(5, model.getId());
        return stat;
    }

    @Override
    PreparedStatement getDeletePreparedStatement(Long id, Connection conn) throws SQLException {
        PreparedStatement prepareStatement = conn.prepareStatement(DELETE_SQL);
        prepareStatement.setLong(1, id);
        return prepareStatement;
    }

    @Override
    PreparedStatement getFindOnePreparedStatement(Long id, Connection conn) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(FIND_ONE_SQL);
        stat.setLong(1, id);
        return stat;
    }

    @Override
    String getFindAllSql() {
        return FIND_ALL_SQL;
    }

    @Override
    ContentTypeDto loadEntity(ResultSet res) throws SQLException {
        ContentTypeDto contentType = new ContentTypeDto();
        contentType.setId(res.getLong(1));
        contentType.setCode(res.getString(2));
        contentType.setName(res.getString(3));
        contentType.defaultContentModel(DefaultContentModel.fromValue(res.getString(4)));
        contentType.defaultContentModelList(DefaultContentModel.fromValue(res.getString(5)));
        return contentType;
    }
}
