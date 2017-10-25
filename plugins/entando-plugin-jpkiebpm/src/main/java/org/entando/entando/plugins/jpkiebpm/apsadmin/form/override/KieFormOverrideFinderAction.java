package org.entando.entando.plugins.jpkiebpm.apsadmin.form.override;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.kieProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;

public class KieFormOverrideFinderAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(KieFormOverrideAction.class);

	@SuppressWarnings("rawtypes")
	public List<Integer> getList() throws ApsSystemException {
		List<Integer> list = new ArrayList<>();
		EntitySearchFilter[] filters = this.createSearchFilters();
		list = this.getKieFormOverrideManager().searchKieFormOverrides(filters);
		return list;
	}

	public KieFormOverride getKieFormOverride(int id) throws ApsSystemException {
		KieFormOverride model = this.getKieFormOverrideManager().getKieFormOverride(id);
		return model;
	}

	@SuppressWarnings("rawtypes")
	private EntitySearchFilter[] createSearchFilters() {
		EntitySearchFilter[] filters = new EntitySearchFilter[0];
		if (StringUtils.isNotBlank(this.getField())) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter("field", false, this.getField(), true);
			filters = this.addFilter(filters, filterToAdd);
		}
		if (StringUtils.isNotBlank(this.getProcessPath())) {
			String[] params = this.getProcessPath().split("@");
			String processId = params[0];
			String containerId = params[1];
			EntitySearchFilter processIdFilter = new EntitySearchFilter("processId", false, processId, true);
			EntitySearchFilter containerIdFilter = new EntitySearchFilter("containerId", false, containerId, true);
			filters = this.addFilter(filters, processIdFilter);
			filters = this.addFilter(filters, containerIdFilter);
		}
		return filters;
	}

	@SuppressWarnings("rawtypes")
	protected EntitySearchFilter[] addFilter(EntitySearchFilter[] filters, EntitySearchFilter filterToAdd) {
		int len = filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for (int i = 0; i < len; i++) {
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public List<kieProcess> getProcessList() throws ApsSystemException {
		List<kieProcess> list = this.getKieFormManager().getProcessDefinitionsList();
		return list;
	}

	public IKieFormOverrideManager getKieFormOverrideManager() {
		return kieFormOverrideManager;
	}

	public void setKieFormOverrideManager(IKieFormOverrideManager kieFormOverrideManager) {
		this.kieFormOverrideManager = kieFormOverrideManager;
	}

	public IKieFormManager getKieFormManager() {
		return kieFormManager;
	}

	public void setKieFormManager(IKieFormManager kieFormManager) {
		this.kieFormManager = kieFormManager;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getProcessPath() {
		return processPath;
	}

	public void setProcessPath(String processPath) {
		this.processPath = processPath;
	}

	private IKieFormOverrideManager kieFormOverrideManager;
	private IKieFormManager kieFormManager;
	private String field;
	private String processPath;

}
