/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.event.BpmWidgetInfoChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BpmWidgetInfoManager extends AbstractService implements IBpmWidgetInfoManager, PageChangedObserver {

    private static final Logger _logger = LoggerFactory.getLogger(BpmWidgetInfoManager.class);


    @Override
    public void init() throws Exception {
        _logger.debug("{} ready.", this.getClass().getName());
    }

    @Override
    public BpmWidgetInfo getBpmWidgetInfo(int id) throws ApsSystemException {
        BpmWidgetInfo bpmWidgetInfo = null;
        try {
            bpmWidgetInfo = this.getBpmWidgetInfoDAO().loadBpmWidgetInfo(id);
        } catch (Throwable t) {
            _logger.error("Error loading bpmWidgetInfo with id '{}'", id, t);
            throw new ApsSystemException("Error loading bpmWidgetInfo with id: " + id, t);
        }
        return bpmWidgetInfo;
    }

    @Override
    public List<Integer> getBpmWidgetInfos() throws ApsSystemException {
        List<Integer> bpmWidgetInfos = new ArrayList<Integer>();
        try {
            bpmWidgetInfos = this.getBpmWidgetInfoDAO().loadBpmWidgetInfos();
        } catch (Throwable t) {
            _logger.error("Error loading BpmWidgetInfo list", t);
            throw new ApsSystemException("Error loading BpmWidgetInfo ", t);
        }
        return bpmWidgetInfos;
    }

    @Override
    public List<Integer> searchBpmWidgetInfos(FieldSearchFilter filters[]) throws ApsSystemException {
        List<Integer> bpmWidgetInfos = new ArrayList<Integer>();
        try {
            bpmWidgetInfos = this.getBpmWidgetInfoDAO().searchBpmWidgetInfos(filters);
        } catch (Throwable t) {
            _logger.error("Error searching BpmWidgetInfos", t);
            throw new ApsSystemException("Error searching BpmWidgetInfos", t);
        }
        return bpmWidgetInfos;
    }

    @Override
    public void addBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) throws ApsSystemException {
        try {
            int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
            bpmWidgetInfo.setId(key);
            this.getBpmWidgetInfoDAO().insertBpmWidgetInfo(bpmWidgetInfo);
            this.notifyBpmWidgetInfoChangedEvent(bpmWidgetInfo, BpmWidgetInfoChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error adding BpmWidgetInfo", t);
            throw new ApsSystemException("Error adding BpmWidgetInfo", t);
        }
    }

    @Override
    public void updateBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) throws ApsSystemException {
        try {
            this.getBpmWidgetInfoDAO().updateBpmWidgetInfo(bpmWidgetInfo);
            this.notifyBpmWidgetInfoChangedEvent(bpmWidgetInfo, BpmWidgetInfoChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error updating BpmWidgetInfo", t);
            throw new ApsSystemException("Error updating BpmWidgetInfo " + bpmWidgetInfo, t);
        }
    }

    @Override
    public void deleteBpmWidgetInfo(int id) throws ApsSystemException {
        try {
            BpmWidgetInfo bpmWidgetInfo = this.getBpmWidgetInfo(id);
            this.getBpmWidgetInfoDAO().removeBpmWidgetInfo(id);
            this.notifyBpmWidgetInfoChangedEvent(bpmWidgetInfo, BpmWidgetInfoChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error deleting BpmWidgetInfo with id {}", id, t);
            throw new ApsSystemException("Error deleting BpmWidgetInfo with id:" + id, t);
        }
    }


    @Override
    public void deleteBpmWidgetInfo(final String pageCode) throws ApsSystemException {

        List<BpmWidgetInfo> listBpmWidgetInfo = _bpmWidgetInfoDAO.searchBpmWidgetInfo(pageCode);
        this.deleteBpmWidgetInfoMethodImplementation(listBpmWidgetInfo);
    }


    @Override
    public void deleteBpmWidgetInfo(final BpmWidgetInfo bpmWidgetInfo) throws ApsSystemException {

        final List<BpmWidgetInfo> listBpmWidgetInfo = _bpmWidgetInfoDAO.searchBpmWidgetInfo(bpmWidgetInfo.getPageCode(), bpmWidgetInfo.getFramePosDraft());
        this.deleteBpmWidgetInfoMethodImplementation(listBpmWidgetInfo);
    }

    private void deleteBpmWidgetInfoMethodImplementation(final List<BpmWidgetInfo> listBpmWidgetInfo) throws ApsSystemException {
        if (listBpmWidgetInfo != null) {
            for (final BpmWidgetInfo bpmWidgetInfo : listBpmWidgetInfo) {
                try {
                    _bpmWidgetInfoDAO.removeBpmWidgetInfo(bpmWidgetInfo.getId());
                    this.notifyBpmWidgetInfoChangedEvent(bpmWidgetInfo, BpmWidgetInfoChangedEvent.REMOVE_OPERATION_CODE);
                } catch (Throwable t) {
                    _logger.error("Error deleting BpmWidgetInfo with id {}", bpmWidgetInfo.getId(), t);
                    throw new ApsSystemException("Error deleting BpmWidgetInfo with id:" + bpmWidgetInfo.getId(), t);
                }
            }
        }

    }

    private void notifyBpmWidgetInfoChangedEvent(BpmWidgetInfo bpmWidgetInfo, int operationCode) {
        BpmWidgetInfoChangedEvent event = new BpmWidgetInfoChangedEvent();
        event.setBpmWidgetInfo(bpmWidgetInfo);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    protected IKeyGeneratorManager getKeyGeneratorManager() {
        return _keyGeneratorManager;
    }

    public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
        this._keyGeneratorManager = keyGeneratorManager;
    }

    public void setBpmWidgetInfoDAO(IBpmWidgetInfoDAO bpmWidgetInfoDAO) {
        this._bpmWidgetInfoDAO = bpmWidgetInfoDAO;
    }

    protected IBpmWidgetInfoDAO getBpmWidgetInfoDAO() {
        return _bpmWidgetInfoDAO;
    }

    private IKeyGeneratorManager _keyGeneratorManager;
    private IBpmWidgetInfoDAO _bpmWidgetInfoDAO;

    @Override
    public void updateFromPageChanged(PageChangedEvent event) {
        if (event.getOperationCode() == PageChangedEvent.REMOVE_OPERATION_CODE) {
            this.getBpmWidgetInfoDAO().removeBpmWidgetInfo(event.getPage().getCode());
        } else if (event.getOperationCode() == PageChangedEvent.EDIT_FRAME_OPERATION_CODE &&
                event.getEventType().equals(PageChangedEvent.EVENT_TYPE_REMOVE_WIDGET)) {
            List<BpmWidgetInfo> bpmWidgetInfoList = this.getBpmWidgetInfoDAO().searchBpmWidgetInfo(event.getPage().getCode(), event.getFramePosition());
            if (null != bpmWidgetInfoList) {

                for (final BpmWidgetInfo bpmWidgetInfo : bpmWidgetInfoList) {

                    bpmWidgetInfo.setInformationDraft(null);
                    bpmWidgetInfo.setFramePosDraft(null);
                    this.getBpmWidgetInfoDAO().updateBpmWidgetInfo(bpmWidgetInfo);
                }

            }


        } else if (event.getOperationCode() == PageChangedEvent.EDIT_FRAME_OPERATION_CODE &&
                event.getEventType().equals(PageChangedEvent.EVENT_TYPE_MOVE_WIDGET)) {
            List<BpmWidgetInfo> listBpmWidgetInfo = this.getBpmWidgetInfoDAO().searchBpmWidgetInfo(event.getPage().getCode(), event.getFramePosition());

            if (null != listBpmWidgetInfo) {
                for (final BpmWidgetInfo bpmWidgetInfo : listBpmWidgetInfo) {
                    bpmWidgetInfo.setFramePosDraft(event.getDestFrame());
                    this.getBpmWidgetInfoDAO().updateBpmWidgetInfo(bpmWidgetInfo);
                }
            }
        } else if (StringUtils.isNotBlank(event.getEventType()) &&
                event.getEventType().equals(PageChangedEvent.EVENT_TYPE_SET_PAGE_ONLINE)) {
            List<BpmWidgetInfo> bpmWidgetInfos = this.getBpmWidgetInfoDAO().searchBpmWidgetInfo(event.getPage().getCode());
            if (null != bpmWidgetInfos) {

                for (BpmWidgetInfo bpmWidgetInfo : bpmWidgetInfos) {
                    if (StringUtils.isNotBlank(bpmWidgetInfo.getInformationDraft())) {
                        bpmWidgetInfo.setInformationOnline(bpmWidgetInfo.getInformationDraft());
                        bpmWidgetInfo.setFramePosOnline(bpmWidgetInfo.getFramePosDraft());
                        this.getBpmWidgetInfoDAO().updateBpmWidgetInfo(bpmWidgetInfo);
                    } else {
                        this.getBpmWidgetInfoDAO().removeBpmWidgetInfo(bpmWidgetInfo.getId());
                    }
                }
            }
        } else if (StringUtils.isNotBlank(event.getEventType()) &&
                event.getEventType().equals(PageChangedEvent.EVENT_TYPE_SET_PAGE_OFFLINE)) {
            List<BpmWidgetInfo> bpmWidgetInfos = this.getBpmWidgetInfoDAO().searchBpmWidgetInfo(event.getPage().getCode());
            if (bpmWidgetInfos != null) {

                for (BpmWidgetInfo bpmWidgetInfo : bpmWidgetInfos) {
                    if (null != bpmWidgetInfo) {
                        bpmWidgetInfo.setInformationOnline(null);
                        bpmWidgetInfo.setFramePosOnline(null);
                        this.getBpmWidgetInfoDAO().updateBpmWidgetInfo(bpmWidgetInfo);
                    }
                }
            }
        } else if (StringUtils.isBlank(event.getEventType()) &&
                event.getOperationCode() == PageChangedEvent.UPDATE_OPERATION_CODE) {

            List<BpmWidgetInfo> bpmWidgetInfos = this.getBpmWidgetInfoDAO().searchBpmWidgetInfo(event.getPage().getCode());
            if (null == bpmWidgetInfos) {
                return;
            }

            for (BpmWidgetInfo bpmWidgetInfo : bpmWidgetInfos) {
                //restore
                if (StringUtils.isNotBlank(bpmWidgetInfo.getInformationOnline())) {
                    bpmWidgetInfo.setInformationDraft(bpmWidgetInfo.getInformationOnline());
                    bpmWidgetInfo.setFramePosDraft(bpmWidgetInfo.getFramePosOnline());
                    this.getBpmWidgetInfoDAO().updateBpmWidgetInfo(bpmWidgetInfo);
                } else {
                    this._bpmWidgetInfoDAO.removeBpmWidgetInfo(bpmWidgetInfo.getId());
                }
            }

        }
    }
}
