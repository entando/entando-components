/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.event.KieFormOverrideChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public class KieFormOverrideManager extends AbstractService implements IKieFormOverrideManager {

	private static final Logger _logger = LoggerFactory.getLogger(KieFormOverrideManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}

	@Override
	public KieFormOverride getKieFormOverride(int id) throws ApsSystemException {
		KieFormOverride kieFormOverride = null;
		try {
			kieFormOverride = this.getKieFormOverrideDAO().loadKieFormOverride(id);
		} catch (Throwable t) {
			_logger.error("Error loading kieFormOverride with id '{}'", id, t);
			throw new ApsSystemException("Error loading kieFormOverride with id: " + id, t);
		}
		return kieFormOverride;
	}

	@Override
	public List<Integer> getKieFormOverrides() throws ApsSystemException {
		List<Integer> kieFormOverrides = new ArrayList<Integer>();
		try {
			kieFormOverrides = this.getKieFormOverrideDAO().loadKieFormOverrides();
		} catch (Throwable t) {
			_logger.error("Error loading KieFormOverride list", t);
			throw new ApsSystemException("Error loading KieFormOverride ", t);
		}
		return kieFormOverrides;
	}

	@Override
	public List<Integer> searchKieFormOverrides(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> kieFormOverrides = new ArrayList<Integer>();
		try {
			kieFormOverrides = this.getKieFormOverrideDAO().searchKieFormOverrides(filters);
		} catch (Throwable t) {
			_logger.error("Error searching KieFormOverrides", t);
			throw new ApsSystemException("Error searching KieFormOverrides", t);
		}
		return kieFormOverrides;
	}

	@Override
	public void addKieFormOverride(KieFormOverride kieFormOverride) throws ApsSystemException {
		try {
			kieFormOverride.setDate(new Date());
			this.getKieFormOverrideDAO().insertKieFormOverride(kieFormOverride);
			this.notifyKieFormOverrideChangedEvent(kieFormOverride, KieFormOverrideChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding KieFormOverride", t);
			throw new ApsSystemException("Error adding KieFormOverride", t);
		}
	}

	@Override
	public void updateKieFormOverride(KieFormOverride kieFormOverride) throws ApsSystemException {
		try {
			this.getKieFormOverrideDAO().updateKieFormOverride(kieFormOverride);
			this.notifyKieFormOverrideChangedEvent(kieFormOverride, KieFormOverrideChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating KieFormOverride", t);
			throw new ApsSystemException("Error updating KieFormOverride " + kieFormOverride, t);
		}
	}

	@Override
	public void deleteKieFormOverride(int id) throws ApsSystemException {
		try {
			KieFormOverride kieFormOverride = this.getKieFormOverride(id);
			this.getKieFormOverrideDAO().removeKieFormOverride(id);
			this.notifyKieFormOverrideChangedEvent(kieFormOverride, KieFormOverrideChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting KieFormOverride with id {}", id, t);
			throw new ApsSystemException("Error deleting KieFormOverride with id:" + id, t);
		}
	}

	@Override
	public List<KieFormOverride> getFormOverrides(String containerId, String processId) throws ApsSystemException {
		List<KieFormOverride> list = new ArrayList<KieFormOverride>();

		FieldSearchFilter containerFilter = new FieldSearchFilter(("containerId"), containerId, true);
		FieldSearchFilter processFilter = new FieldSearchFilter(("processId"), processId, true);
		FieldSearchFilter filters[] = { containerFilter, processFilter };
		List<Integer> ids = searchKieFormOverrides(filters);
		if (null != ids) {
			for (Integer id : ids) {
				KieFormOverride override = getKieFormOverride(id);
				if (null != override) {
					list.add(override);
				}
			}
		}
		return list;
	}

	@Override
	public List<KieFormOverride> getFormOverrides(String containerId, String processId, String field) throws ApsSystemException {
		List<KieFormOverride> list = new ArrayList<KieFormOverride>();

		FieldSearchFilter containerFilter = new FieldSearchFilter(("containerId"), containerId, true);
		FieldSearchFilter processFilter = new FieldSearchFilter(("processId"), processId, true);
		FieldSearchFilter fieldFilter = new FieldSearchFilter(("field"), field, true);

		FieldSearchFilter filters[] = { containerFilter, processFilter, fieldFilter };
		List<Integer> ids = searchKieFormOverrides(filters);
		if (null != ids) {
			for (Integer id : ids) {
				KieFormOverride override = getKieFormOverride(id);
				if (null != override) {
					list.add(override);
				}
			}
		}
		return list;
	}

	private void notifyKieFormOverrideChangedEvent(KieFormOverride kieFormOverride, int operationCode) {
		KieFormOverrideChangedEvent event = new KieFormOverrideChangedEvent();
		event.setKieFormOverride(kieFormOverride);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	public void setKieFormOverrideDAO(IKieFormOverrideDAO kieFormOverrideDAO) {
		this._kieFormOverrideDAO = kieFormOverrideDAO;
	}

	protected IKieFormOverrideDAO getKieFormOverrideDAO() {
		return _kieFormOverrideDAO;
	}

	private IKieFormOverrideDAO _kieFormOverrideDAO;
}
