package org.entando.entando.plugins.jpkiebpm.apsadmin.form.override;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormField;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.kieProcess;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.AbstractBpmOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.DefaultValueOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.IBpmOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.PlaceHolderOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

public class KieFormOverrideAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(KieFormOverrideAction.class);
	public static final String PROP_NAME_PROCESS_ID = "processId";
	public static final String PROP_NAME_CONTAINER_ID = "containerId";

	public String newModel() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			this.setFormModel(new KieFormOverride());
		} catch (Throwable t) {
			_logger.error("error in new model", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String editModel() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
			KieFormOverride model = this.getKieFormOverrideManager().getKieFormOverride(this.getId());
			if (null == model) {
				this.addActionError(this.getText("jpkie.formModel.null"));
				return INPUT;
			}
			this.setFormModel(model);
			this.setProcessPathToController(model);
			this.setField(model.getField());
			if (null != model.getOverrides() && null != model.getOverrides().getList()) {
				for (IBpmOverride override : model.getOverrides().getList()) {
					AbstractBpmOverride target = (AbstractBpmOverride) override;
					if (target instanceof DefaultValueOverride) {
						this.setDefaultValueOverride(((DefaultValueOverride) target).getDefaultValue());
					} else if (target instanceof PlaceHolderOverride) {
						this.setPlaceHolderOverride(((PlaceHolderOverride) target).getPlaceHolder());
					}
				}
			}

		} catch (Throwable t) {
			_logger.error("error in edit model", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String chooseForm() {
		try {
			KieFormOverride model = this.getKieFormOverrideManager().getKieFormOverride(this.getId());
			if (null == model && this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.addActionError(this.getText("jpkie.formModel.null"));
				return INPUT;
			}
			this.setProcessPathToController(model);
		} catch (Throwable t) {
			_logger.error("error in edit model", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String chooseField() {
		try {

			String[] params = this.getProcessPath().split("@");
			String processId = params[0];
			String containerId = params[1];

			List<KieFormOverride> result = this.getKieFormOverrideManager().getFormOverrides(containerId, processId, this.getFormModel().getField());
			if (null != result && !result.isEmpty()) {
				this.addActionError(this.getText("jpkie.formModel.present"));
				return INPUT;
			}

			this.setField(this.getFormModel().getField());
		} catch (Throwable t) {
			_logger.error("error in choose field", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String changeForm() {
		try {
			this.setProcessPath(null);
			this.setField(null);
			this.setPlaceHolderOverride(null);
			this.setDefaultValueOverride(null);
		} catch (Throwable t) {
			_logger.error("error in change form", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String changeField() {
		try {
			this.setField(null);
			this.setPlaceHolderOverride(null);
			this.setDefaultValueOverride(null);
		} catch (Throwable t) {
			_logger.error("error in change field", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trashModel() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);

			KieFormOverride model = this.getKieFormOverrideManager().getKieFormOverride(this.getId());
			if (null == model) {
				this.addActionError(this.getText("jpkie.formModel.null"));
				return INPUT;
			}

			List<IPage> pagesOnline = this.getPageManager().getOnlineWidgetUtilizers(KieBpmSystemConstants.WIDGET_CODE_BPM_FORM);
			List<IPage> pagesDraft = this.getPageManager().getOnlineWidgetUtilizers(KieBpmSystemConstants.WIDGET_CODE_BPM_FORM);
			checkReferences(pagesOnline);
			checkReferences(pagesDraft);
			if (this.hasActionErrors()) {
				return INPUT;
			}

			this.setFormModel(model);
		} catch (Throwable t) {
			_logger.error("error in trash model", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void checkReferences(List<IPage> pages) {
		if (null != pages && !pages.isEmpty()) {
			for (IPage page : pages) {
				for (Widget widget : page.getWidgets()) {
					if (widget.getType().getCode().equals(KieBpmSystemConstants.WIDGET_CODE_BPM_FORM)) {
						if (widget.getConfig().containsKey(KieBpmSystemConstants.WIDGET_PROP_NAME_OVERRIDE_ID)) {
							String listOverrrides = widget.getConfig().getProperty(KieBpmSystemConstants.WIDGET_PROP_NAME_OVERRIDE_ID);
							if (StringUtils.isNotBlank(listOverrrides) && !listOverrrides.equals("null")) {
								String[] idStrings = listOverrrides.split(",");
								String theId = String.valueOf(this.getId());
								if (ArrayUtils.contains(idStrings, theId)) {
									this.addActionError(this.getText("jpkie.formModel.referencedby", new String[] { page.getCode() }));
								}
							}
						}
					}
				}
			}
		}
	}

	public String saveModel() {
		try {

			KieFormOverride model = this.getFormModel();
			this.setProcessPathToModel(model);
			model.setField(this.getField());
			if (!StringUtils.isBlank(this.getDefaultValueOverride())) {
				DefaultValueOverride override = new DefaultValueOverride();
				override.setDefaultValue(this.getDefaultValueOverride());
				model.addOverride(override);
			}
			if (!StringUtils.isBlank(this.getPlaceHolderOverride())) {
				PlaceHolderOverride override = new PlaceHolderOverride();
				override.setPlaceHolder(this.getPlaceHolderOverride());
				model.addOverride(override);
			}

			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getKieFormOverrideManager().addKieFormOverride(model);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				KieFormOverride modelOverride = this.getKieFormOverrideManager().getKieFormOverride(this.getId());
				model.setId(modelOverride.getId());
				model.setDate(modelOverride.getDate());
				this.getKieFormOverrideManager().updateKieFormOverride(model);
			}
		} catch (Throwable t) {
			_logger.error("error in save model", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String deleteModel() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getKieFormOverrideManager().deleteKieFormOverride(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete model", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<kieProcess> getProcessList() throws ApsSystemException {
		List<kieProcess> list = this.getKieFormManager().getProcessDefinitionsList();
		return list;
	}

	public List<KieProcessFormField> getFields() throws ApsSystemException {
		String[] params = this.getProcessPath().split("@");
		String processId = (params[0]);
		String containerId = (params[1]);
		KieProcessFormQueryResult form = this.getKieFormManager().getProcessForm(containerId, processId);
		List<KieProcessFormField> fileds = new ArrayList<>();
		this.addFileds(form, fileds);
		return fileds;
	}

	protected void setProcessPathToModel(KieFormOverride kieFormOverride) {
		if (null != kieFormOverride) {
			String[] params = this.getProcessPath().split("@");
			kieFormOverride.setProcessId(params[0]);
			kieFormOverride.setContainerId(params[1]);
		}
	}

	protected void setProcessPathToController(KieFormOverride kieFormOverride) {
		if (null != kieFormOverride) {
			String procString = kieFormOverride.getProcessId() + "@" + kieFormOverride.getContainerId();
			this.setProcessPath(procString);
		}
	}

	protected void addFileds(KieProcessFormQueryResult form, List<KieProcessFormField> fileds) {
		if (null != form && null != form.getFields()) {
			for (KieProcessFormField kieProcessFormField : form.getFields()) {
				fileds.add(kieProcessFormField);
			}
			if (null != form.getForms()) {
				for (KieProcessFormQueryResult subForm : form.getForms()) {
					this.addFileds(subForm, fileds);
				}
			}
		}
	}

	public int getStrutsAction() {
		return strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this.strutsAction = strutsAction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public KieFormOverride getFormModel() {
		return formModel;
	}

	public void setFormModel(KieFormOverride formModel) {
		this.formModel = formModel;
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

	public String getProcessPath() {
		return processPath;
	}

	public void setProcessPath(String processPath) {
		this.processPath = processPath;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getPlaceHolderOverride() {
		return placeHolderOverride;
	}

	public void setPlaceHolderOverride(String placeHolderOverride) {
		this.placeHolderOverride = placeHolderOverride;
	}

	public String getDefaultValueOverride() {
		return defaultValueOverride;
	}

	public void setDefaultValueOverride(String defaultValueOverride) {
		this.defaultValueOverride = defaultValueOverride;
	}

	public IPageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this.pageManager = pageManager;
	}

	private int strutsAction;
	private KieFormOverride formModel;
	private int id;
	private String processPath;
	private String field;
	private String placeHolderOverride;
	private String defaultValueOverride;

	private IKieFormOverrideManager kieFormOverrideManager;
	private IKieFormManager kieFormManager;
	private IPageManager pageManager;

}
