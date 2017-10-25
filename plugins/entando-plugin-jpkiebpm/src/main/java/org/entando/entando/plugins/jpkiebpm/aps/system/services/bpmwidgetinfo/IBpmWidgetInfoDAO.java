/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IBpmWidgetInfoDAO {

	List<Integer> searchBpmWidgetInfos(FieldSearchFilter[] filters);

	BpmWidgetInfo loadBpmWidgetInfo(int id);

	List<Integer> loadBpmWidgetInfos();

	void removeBpmWidgetInfo(int id);

	void updateBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo);

	void insertBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo);

	void removeBpmWidgetInfo(String code);

	List<BpmWidgetInfo> searchBpmWidgetInfo(String code);

	List<BpmWidgetInfo> searchBpmWidgetInfo(String code, int frameDraft);

}
