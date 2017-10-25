/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IBpmWidgetInfoManager {

    BpmWidgetInfo getBpmWidgetInfo(int id) throws ApsSystemException;

    List<Integer> getBpmWidgetInfos() throws ApsSystemException;

    List<Integer> searchBpmWidgetInfos(FieldSearchFilter filters[]) throws ApsSystemException;

    void addBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) throws ApsSystemException;

    void updateBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) throws ApsSystemException;

    void deleteBpmWidgetInfo(int id) throws ApsSystemException;

    void deleteBpmWidgetInfo(final String pageCode) throws ApsSystemException;

    void deleteBpmWidgetInfo(final BpmWidgetInfo bpmWidgetInfo) throws ApsSystemException;

}