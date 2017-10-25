/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BpmWidgetInfoDAO extends AbstractSearcherDAO implements IBpmWidgetInfoDAO {

    private static final Logger _logger = LoggerFactory.getLogger(BpmWidgetInfoDAO.class);

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return "jpkiebpm_bpmwidgetinfo";
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
    public List<Integer> searchBpmWidgetInfos(FieldSearchFilter[] filters) {
        List bpmWidgetInfosId = null;
        try {
            bpmWidgetInfosId = super.searchId(filters);
        } catch (Throwable t) {
            _logger.error("error in searchBpmWidgetInfos", t);
            throw new RuntimeException("error in searchBpmWidgetInfos", t);
        }
        return bpmWidgetInfosId;
    }

    @Override
    public List<Integer> loadBpmWidgetInfos() {
        List<Integer> bpmWidgetInfosId = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(LOAD_BPMWIDGETINFOS_ID);
            res = stat.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                bpmWidgetInfosId.add(id);
            }
        } catch (Throwable t) {
            _logger.error("Error loading BpmWidgetInfo list", t);
            throw new RuntimeException("Error loading BpmWidgetInfo list", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return bpmWidgetInfosId;
    }

    @Override
    public void insertBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.insertBpmWidgetInfo(bpmWidgetInfo, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error on insert bpmWidgetInfo", t);
            throw new RuntimeException("Error on insert bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void insertBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_BPMWIDGETINFO);
            int index = 1;
            stat.setInt(index++, bpmWidgetInfo.getId());
            if (StringUtils.isNotBlank(bpmWidgetInfo.getInformationOnline())) {
                stat.setString(index++, bpmWidgetInfo.getInformationOnline());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(bpmWidgetInfo.getInformationDraft())) {
                stat.setString(index++, bpmWidgetInfo.getInformationDraft());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(bpmWidgetInfo.getWidgetType())) {
                stat.setString(index++, bpmWidgetInfo.getWidgetType());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(bpmWidgetInfo.getPageCode())) {
                stat.setString(index++, bpmWidgetInfo.getPageCode());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }

            if (null != bpmWidgetInfo.getFramePosOnline()) {
                stat.setInt(index++, bpmWidgetInfo.getFramePosOnline());
            } else {
                stat.setNull(index++, Types.INTEGER);
            }
            if (null != bpmWidgetInfo.getFramePosDraft()) {
                stat.setInt(index++, bpmWidgetInfo.getFramePosDraft());
            } else {
                stat.setNull(index++, Types.INTEGER);
            }

            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error on insert bpmWidgetInfo", t);
            throw new RuntimeException("Error on insert bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void updateBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateBpmWidgetInfo(bpmWidgetInfo, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error updating bpmWidgetInfo {}", bpmWidgetInfo.getId(), t);
            throw new RuntimeException("Error updating bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void updateBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE_BPMWIDGETINFO);
            int index = 1;

            if (StringUtils.isNotBlank(bpmWidgetInfo.getInformationOnline())) {
                stat.setString(index++, bpmWidgetInfo.getInformationOnline());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }

            if (StringUtils.isNotBlank(bpmWidgetInfo.getInformationDraft())) {
                stat.setString(index++, bpmWidgetInfo.getInformationDraft());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }

            if (StringUtils.isNotBlank(bpmWidgetInfo.getWidgetType())) {
                stat.setString(index++, bpmWidgetInfo.getWidgetType());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(bpmWidgetInfo.getPageCode())) {
                stat.setString(index++, bpmWidgetInfo.getPageCode());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }

            if (null != bpmWidgetInfo.getFramePosOnline()) {
                stat.setInt(index++, bpmWidgetInfo.getFramePosOnline());
            } else {
                stat.setNull(index++, Types.INTEGER);
            }
            if (null != bpmWidgetInfo.getFramePosDraft()) {
                stat.setInt(index++, bpmWidgetInfo.getFramePosDraft());
            } else {
                stat.setNull(index++, Types.INTEGER);
            }

            stat.setInt(index++, bpmWidgetInfo.getId());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error updating bpmWidgetInfo {}", bpmWidgetInfo.getId(), t);
            throw new RuntimeException("Error updating bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void removeBpmWidgetInfo(int id) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.removeBpmWidgetInfo(id, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error deleting bpmWidgetInfo {}", id, t);
            throw new RuntimeException("Error deleting bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void removeBpmWidgetInfo(int id, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_BPMWIDGETINFO);
            int index = 1;
            stat.setInt(index++, id);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error deleting bpmWidgetInfo {}", id, t);
            throw new RuntimeException("Error deleting bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    public void removeBpmWidgetInfoByPageCode(String pageCode, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_BPMWIDGETINFO_BY_PAGE);
            int index = 1;
            stat.setString(index++, pageCode);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error deleting bpmWidgetInfo {}", pageCode, t);
            throw new RuntimeException("Error deleting bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void removeBpmWidgetInfo(String pageCode) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.removeBpmWidgetInfoByPageCode(pageCode, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error deleting bpmWidgetInfo by page {}", pageCode, t);
            throw new RuntimeException("Error deleting bpmWidgetInfo", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }

    }

    @Override
    public List<BpmWidgetInfo> searchBpmWidgetInfo(String pageCode, int framePositionDraft) {
        List<BpmWidgetInfo> bpmWidgetInfoList = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            bpmWidgetInfoList = this.loadBpmWidgetInfo(pageCode, framePositionDraft, conn);
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with id {}", pageCode, t);
            throw new RuntimeException("Error loading bpmWidgetInfo with id " + pageCode, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return bpmWidgetInfoList;
    }

    @Override
    public List<BpmWidgetInfo> searchBpmWidgetInfo(String pageCode) {
        List<BpmWidgetInfo> bpmWidgetInfoList = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            bpmWidgetInfoList = this.loadBpmWidgetInfo(pageCode, conn);
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with id {}", pageCode, t);
            throw new RuntimeException("Error loading bpmWidgetInfo with id " + pageCode, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return bpmWidgetInfoList;
    }

    public BpmWidgetInfo loadBpmWidgetInfo(int id) {
        BpmWidgetInfo bpmWidgetInfo = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            bpmWidgetInfo = this.loadBpmWidgetInfo(id, conn);
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with id {}", id, t);
            throw new RuntimeException("Error loading bpmWidgetInfo with id " + id, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return bpmWidgetInfo;
    }

    public List<BpmWidgetInfo> loadBpmWidgetInfo(String pageCode, Connection conn) {
        List<BpmWidgetInfo> list = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_BPMWIDGETINFO_BY_PAGECODE);
            int index = 1;
            stat.setString(index++, pageCode);
            res = stat.executeQuery();
            while (res.next()) {
                if (list == null) list = new ArrayList<>();
                BpmWidgetInfo bpmWidgetInfo = this.buildBpmWidgetInfoFromRes(res);
                list.add(bpmWidgetInfo);
            }
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with for page {}", pageCode, t);
            throw new RuntimeException("Error loading bpmWidgetInfo for page " + pageCode, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return list;
    }

    public List<BpmWidgetInfo> loadBpmWidgetInfo(String pageCode, int frame, Connection conn) {
        List<BpmWidgetInfo> list = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_BPMWIDGETINFO_BY_POS_DRAFT);
            int index = 1;
            stat.setString(index++, pageCode);
            stat.setInt(index++, frame);
            res = stat.executeQuery();
            while (res.next()) {
                if (list == null) list = new ArrayList<>();
                BpmWidgetInfo bpmWidgetInfo = this.buildBpmWidgetInfoFromRes(res);
                list.add(bpmWidgetInfo);
            }
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with for page {}", pageCode, t);
            throw new RuntimeException("Error loading bpmWidgetInfo for page " + pageCode, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return list;
    }

    public BpmWidgetInfo loadBpmWidgetInfo(int id, Connection conn) {
        BpmWidgetInfo bpmWidgetInfo = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_BPMWIDGETINFO);
            int index = 1;
            stat.setInt(index++, id);
            res = stat.executeQuery();
            if (res.next()) {
                bpmWidgetInfo = this.buildBpmWidgetInfoFromRes(res);
            }
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with id {}", id, t);
            throw new RuntimeException("Error loading bpmWidgetInfo with id " + id, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return bpmWidgetInfo;
    }

    protected BpmWidgetInfo buildBpmWidgetInfoFromRes(ResultSet res) {
        BpmWidgetInfo widgetInfo = null;
        try {
            widgetInfo = new BpmWidgetInfo();
            widgetInfo.setId(res.getInt("id"));
            widgetInfo.setInformationOnline(res.getString("information_online"));
            widgetInfo.setInformationDraft(res.getString("information_draft"));
            widgetInfo.setWidgetType(res.getString("widget_type"));
            widgetInfo.setPageCode(res.getString("pageCode"));
            widgetInfo.setFramePosOnline(res.getInt("framepos_online"));
            widgetInfo.setFramePosDraft(res.getInt("framepos_draft"));

            if (StringUtils.isNotBlank(widgetInfo.getInformationOnline())) {
                ApsProperties properties = new ApsProperties();
                properties.loadFromXml(widgetInfo.getInformationOnline());
                widgetInfo.setConfigOnline(properties);
            }
            if (StringUtils.isNotBlank(widgetInfo.getInformationDraft())) {
                ApsProperties properties = new ApsProperties();
                properties.loadFromXml(widgetInfo.getInformationDraft());
                widgetInfo.setConfigDraft(properties);
            }

        } catch (Throwable t) {
            _logger.error("Error in buildBpmWidgetInfoFromRes", t);
        }
        return widgetInfo;
    }

    private static final String ADD_BPMWIDGETINFO = "INSERT INTO jpkiebpm_widgetinfo (id, information_online, information_draft,  widget_type, pagecode, framepos_online, framepos_draft ) VALUES (?, ? ,?, ?, ?, ?, ?)";

    private static final String UPDATE_BPMWIDGETINFO = "UPDATE jpkiebpm_widgetinfo SET  information_online=?, information_draft=?, widget_type=?, pagecode=?, framepos_online=?, framepos_draft=? WHERE id = ?";

    private static final String DELETE_BPMWIDGETINFO = "DELETE FROM jpkiebpm_widgetinfo WHERE id = ?";

    private static final String DELETE_BPMWIDGETINFO_BY_PAGE = "DELETE FROM jpkiebpm_widgetinfo WHERE pagecode = ?";

    private static final String LOAD_BPMWIDGETINFO = "SELECT id, information_online, information_draft, widget_type, pagecode, framepos_online, framepos_draft FROM jpkiebpm_widgetinfo WHERE id = ?";

    private static final String LOAD_BPMWIDGETINFO_BY_POS_DRAFT = "SELECT id, information_online, information_draft, widget_type, pagecode, framepos_online, framepos_draft FROM jpkiebpm_widgetinfo "
            + "WHERE pagecode=? and framepos_draft=?";

    private static final String LOAD_BPMWIDGETINFO_BY_POS_ONLINE = "SELECT id, information_online, information_draft, widget_type, pagecode, framepos_online, framepos_draft FROM jpkiebpm_widgetinfo "
            + "WHERE pagecode=? and framepos_online=?";

    private static final String LOAD_BPMWIDGETINFO_BY_PAGECODE = "SELECT id, information_online, information_draft, widget_type, pagecode, framepos_online, framepos_draft FROM jpkiebpm_widgetinfo "
            + "WHERE pagecode=?";

    private static final String LOAD_BPMWIDGETINFOS_ID = "SELECT id FROM jpkiebpm_widgetinfo";

}
