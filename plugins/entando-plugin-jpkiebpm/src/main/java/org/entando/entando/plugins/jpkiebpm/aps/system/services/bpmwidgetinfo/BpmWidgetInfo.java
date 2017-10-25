/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo;

import com.agiletec.aps.util.ApsProperties;

public class BpmWidgetInfo {

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getInformationOnline() {
		return _informationOnline;
	}

	public String getInformationDraft() {
		return _informationDraft;
	}

	public void setInformationOnline(final String informationOnline) {
		this._informationOnline = informationOnline;
	}

	public void setInformationDraft(final String informationDraft) {
		this._informationDraft = informationDraft;
	}

	public String getWidgetType() {
		return _widgetType;
	}

	public void setWidgetType(final String widgetType) {
		this._widgetType = widgetType;
	}

	public ApsProperties getConfigOnline() {
		return configOnline;
	}

	protected void setConfigOnline(final ApsProperties configOnline) {
		this.configOnline = configOnline;
	}

	public ApsProperties getConfigDraft() {
		return configDraft;
	}

	protected void setConfigDraft(final ApsProperties configDraft) {
		this.configDraft = configDraft;
	}

	public String getPageCode() {
		return _pageCode;
	}

	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}

	public Integer getFramePosDraft() {
		return framePosDraft;
	}

	public void setFramePosDraft(Integer framePosDraft) {
		this.framePosDraft = framePosDraft;
	}

	public Integer getFramePosOnline() {
		return framePosOnline;
	}

	public void setFramePosOnline(Integer framePosOnline) {
		this.framePosOnline = framePosOnline;
	}

	private int _id;
	private String _informationOnline;
	private String _informationDraft;
	private String _widgetType;
	private String _pageCode;
	private Integer framePosOnline;
	private Integer framePosDraft;

	private ApsProperties configOnline;
	private ApsProperties configDraft;

}
