/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpsurvey.apsadmin.survey;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * This is the base class for the _survey actions. To avoid useless duplication
 * of JSP pages we inherit the parameter 'isQuestionnaire' which tells us in
 * advance whether the _survey is a poll or a questionnaire
 */
public class SurveyAction extends AbstractSurveyAction implements ISurveyAction {
	
	@Override
	public void validate() {
		super.validate();
		this.checkExistingIds();
		this.checkDates();
		this.fetchLocalizedFields(); 
	}
	
	private void checkExistingIds() {
		if (null == this.getStrutsAction()) {
			this.addFieldError("strutsAction", this.getText(
					"message.surveyAction.sysError", new String[] { "strutsAction" }));
			} else {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				if (null == this.getSurveyId()) {
					this.addFieldError("surveyId", this.getText("message.surveyAction.sysError", new String[] { "surveyId" }));
				}
			}
		}
	}

	/**
	 * Inspect the request so to find all references of strings in the form of
	 * 'titles-xy' and 'descriptions-xy' where 'xy' is the language code
	 * 
	 * @throws ApsSystemException
	 */
	private void fetchLocalizedFields() {
		Iterator<Lang> itr = this.getLangManager().getLangs().iterator();
		Survey survey = null;
		try {
			while (itr.hasNext()) {
				Lang currentLang = itr.next();
				Lang defaultLanguage = this.getLangManager().getDefaultLang();
				String currentLangCode = currentLang.getCode();
				String titleKey = "title-" + currentLangCode;
				String descriptionKey = "description-" + currentLangCode;
				String imageDescriptionKey = "imageDescription-" + currentLangCode;
				String title = this.getRequest().getParameter(titleKey);
				String description = this.getRequest().getParameter(descriptionKey);
				String imageDescription = this.getRequest().getParameter(imageDescriptionKey);
				if (null != title && title.trim().length() > 0) {
					this.getTitles().put(currentLangCode, title.trim());
				} else {
					if (currentLang.getCode().equals(defaultLanguage.getCode())) {
						String[] args = { defaultLanguage.getDescr(), this.getText("title") };
						this.addFieldError(titleKey, this.getText("message.jpsurvey.defaultLangRequired", args));
					}
				}
				if (null != description && description.trim().length() > 0) {
					this.getDescriptions().put(currentLangCode,	description.trim());
				} else {
					if (currentLang.getCode().equals(defaultLanguage.getCode())) {
						String[] args = { defaultLanguage.getDescr(), this.getText("description") };
						this.addFieldError(descriptionKey, this.getText("message.jpsurvey.defaultLangRequired", args));
					}
				}
				if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
					// image description is required if and only if an image has been associated to the current editing _survey
					survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
					// there shouldn't be the need to check for the lenght of the imageId...
					if (null != survey && null != survey.getImageId()) {
						if (null != imageDescriptionKey	&& null != imageDescription && imageDescription.trim().length() > 0) {
							this.getImageDescriptions().put(currentLangCode, imageDescription.trim());
						} else {
							if (currentLang.getCode().equals(defaultLanguage.getCode())) {
								String[] args = { defaultLanguage.getDescr(), this.getText("imageDescription") };
								this.addFieldError(imageDescriptionKey,	this.getText("message.jpsurvey.defaultLangRequired", args));
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "fetchLocalizedFields");
			throw new RuntimeException(	"Error during the validation of the multilanguage fields", t);
		}
	}

	private void checkDates() {
		if (null != this.getEndDate() && null != this.getStartDate()
				&& this.getStartDate().getTime() > this.getEndDate().getTime()) {
			this.addFieldError("endDate", this.getText("message.surveyAction.wrongDate"));
		}
	}
	
	@Override
	public String addQuestion() {
		String result = this.saveSurvey();
		// check the action type
		if (this.getStrutsAction() != ApsAdminSystemConstants.EDIT) {
			ApsSystemUtils.getLogger().error("jpsurvey: wrong struts action (" + this.getStrutsAction()+ ") detected in 'addQuestion', only EDIT allowed");
			return FAILURE;
		}
		if (result.equals(FAILURE)) return FAILURE;
		// Trampoline to question actions
		return SUCCESS;
	}

	@Override
	public String newSurvey() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}

	@Override
	public boolean isEditable(Integer surveyId) {
		boolean res = false;
		try {
			if (null == surveyId)
				return true;
			Survey survey = this.getSurveyManager().loadSurvey(surveyId);
			res = !survey.isElegibleForVoting(false);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isEditable");
			throw new RuntimeException("Errore nel valutare lo stato \"editabile\" di _survey id " + surveyId, t);
		}
		return res;
	}

	@Override
	public int getResponseOccurences(Integer questionId) {
		List<SingleQuestionResponse> responses = null;
		try {
			if (questionId == null) {
				return 0;
			}
			responses = this.getResponseManager().aggregateResponseByIds(null,questionId, null, null);
			if (null != responses) {
				return responses.size();
			} else {
				return 0;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getResponseOccurences");
			throw new RuntimeException("Error checking the \"editable\" state of the survey with id ", t);
		}
	}

	@Override
	public int getVotersNumber(Integer surveyId) {
		List<Integer> list = null;
		try {
			list = this.getVoterManager().searchVotersByIds(null, null, null,
					null, null, surveyId, null);
			if (null != list) {
				return list.size();
			} else {
				return 0;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getResponseOccurences");
			throw new RuntimeException("Error loading the voters number for survey " + surveyId, t);
		}
	}

	@Override
	public String editSurvey() {
		Survey survey = null;
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
			if (null == survey) {
				this.addActionError(this.getText("message.surveyAction.nullSurvey", new String[] {getText("message.jpsurvey.poll.type"), String.valueOf(this.getSurveyId())}));
				return "listSurveys";
			}
			this.setSurvey(survey);
			this.setSurveyId(survey.getId());
			this.setDescriptions(survey.getDescriptions());
			this.setGroupName(survey.getGroupName());
			this.setStartDate(survey.getStartDate());
			this.setEndDate(survey.getEndDate());
			this.setActive(survey.isActive() ? 1 : 0);
			this.setPublicPartialResult(survey.isPublicPartialResult() ? 1 : 0);
			this.setPublicResult(survey.isPublicResult() ? 1 : 0);
			this.setQuestionnaire(survey.isQuestionnaire());
			this.setGatherUserInfo(survey.isGatherUserInfo() ? 1 : 0);
			this.setCheckCookie(survey.isCheckCookie());
			this.setCheckIpAddress(survey.isCheckIpAddress());
			this.setCheckUsername(survey.isCheckUsername());
			this.setTitles(survey.getTitles());
			this.setImageId(survey.getImageId());
			this.setImageDescriptions(survey.getImageDescriptions());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "editSurvey");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Returns a list of system groups.
	 * @return The list of the systyem groups.
	 */
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}

	@Override
	public String saveSurvey() {
		Survey survey = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
				if (null == survey) {
					return FAILURE;
				}
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				survey = new Survey();
				survey.setQuestionnaire(this.getQuestionnaire());
			}
			survey.setDescriptions(this.getDescriptions());
			survey.setGroupName(this.getGroupName());
			survey.setStartDate(this.getStartDate());
			survey.setEndDate(this.getEndDate());
			if (null == this.getGatherUserInfo()) {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return "listSurveys";
			}
			if (survey.isQuestionnaire()) {
				survey.setPublicPartialResult(false);
				survey.setPublicResult(false);
			} else {
				survey.setPublicPartialResult(null != this.getPublicPartialResult() && this.getPublicPartialResult() == 1);
				survey.setPublicResult(null != this.getPublicResult() && this.getPublicResult() == 1);
			}
			survey.setGatherUserInfo(this.getGatherUserInfo() == 1);
			survey.setTitles(this.getTitles());
			survey.setCheckCookie(this.getCheckCookie() == null ? false : this.getCheckCookie().booleanValue());
			survey.setCheckIpAddress(this.getCheckIpAddress() == null ? false : this.getCheckIpAddress().booleanValue());
			survey.setCheckUsername(this.getCheckUsername() == null ? false : this.getCheckUsername().booleanValue());
			
			survey.setImageId(this.getImageId());
			// refresh the image description if and only if the _survey as an image. A new _survey has no image and description by definition
			if (null != survey.getImageId()	&& this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				survey.setImageDescriptions(this.getImageDescriptions());
			} else {
				survey.setImageDescriptions(null);
			}
			this.setSurvey(survey);
			// if the we're editing update the record and return to the proper list
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getSurveyManager().updateSurvey(survey);
				return "listSurveys";
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.setStrutsAction(ApsAdminSystemConstants.EDIT);
				this.getSurveyManager().saveSurvey(survey);
				this.setSurveyId(survey.getId());
				return "editSurvey";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveSurvey");
			return FAILURE;
		}
		return FAILURE;
	}
	
	@Override
	public String associateSurveyImageEntry() {
		if (this.getStrutsAction() != ApsAdminSystemConstants.EDIT) {
			this.addActionError(this.getText("message.surveyAction.sysInvalidStatus"));
			return INPUT;
		}
		String result = this.saveSurvey();
		if (result.equals(FAILURE)) return FAILURE;
		if (!result.equals("listSurveys")) {
			return INPUT;
		}
		return SUCCESS;
	}

	public String removeSurveyImage() throws Throwable {
		try {
			String result = this.saveSurvey();
			if (result.equals(FAILURE)) return FAILURE;
			if (!result.equals("listSurveys")) {
				return INPUT;
			}
			Survey survey =  this.getSurveyManager().loadSurvey(this.getSurveyId());
			if (null != survey) {
				survey.setImageId(null);
				survey.setImageDescriptions(null);
				this.getSurveyManager().updateSurvey(survey);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeSurveyImage");
		}
		return SUCCESS;
	}

	@Override
	public String trashSurvey() {
		Survey survey = null;
		try {
			if (null != this.getSurveyId()) {
				survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
				if (null == survey) return INPUT;
				this.setSurvey(survey);
			} else {
				this.addActionError(this.getText("message.surveyAction.sysError", new String[]{"surveyId"}));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "trashSurvey");
		}
		return SUCCESS;
	}
	
	@Override
	public String deleteSurvey() {
		Survey survey = null;
		try {
			if (null == this.getSurveyId() || null == this.getQuestionnaire()) {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return INPUT;
			}
			survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
			this.getResponseManager().deleteResponseBySurvey(survey);
			this.getVoterManager().deleteVoterBySurveyId(this.getSurveyId());
			this.getSurveyManager().deleteSurvey(this.getSurveyId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteSurvey");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String retireSurvey() {
		Survey survey = null;
		try {
			if (null != this.getQuestionnaire() && null != getSurveyId()) {
				survey = getSurveyManager().loadSurvey(this.getSurveyId());
				if (null != survey) {
					survey.setActive(false);
					getSurveyManager().updateSurvey(survey);
					String surveyInfo = this.getQuestionnaire() ? this.getText("message.jpsurvey.survey.type"): this.getText("message.jpsurvey.poll.type");
					this.addActionMessage(this.getText("message.jpsurvey.surveyUnpublished", new String[] {surveyInfo, this.getLabel(survey.getTitles()) } ));
					return SUCCESS;
				} else {
					this.addActionError(this.getText("message.surveyAction.cannotProceed"));
					return INPUT;
				}
			} else {
				this.addActionError(this.getText("message.surveyAction.cannotProceed"));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "retireSurvey");
			return FAILURE;
		}
	}
	
	@Override
	public String publishSurvey() {
		Survey survey = null;
		try {
			if (null != this.getSurveyId()) {
				survey = this.getSurveyManager().loadSurvey(this.getSurveyId());
				if (null != survey ) {
					if (!survey.isPublishable()) {
						this.addActionError(this.getText("message.jpsurvey.activationNotAllowed"));
					} else {
						survey.setActive(true);
						this.getSurveyManager().updateSurvey(survey);
						String surveyInfo = this.getQuestionnaire() ? this.getText("message.jpsurvey.survey.type"): this.getText("message.jpsurvey.poll.type");
						this.addActionMessage(this.getText("message.jpsurvey.surveyPublished", new String[] {surveyInfo, this.getLabel(survey.getTitles()) } ));
					}
				} else {
					return INPUT;
				}
			} else {
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "publishSurvey");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String editQuestion() {
		return SUCCESS;
	}

	public String getDefaultLangCode() {
		return this.getLangManager().getDefaultLang().getCode();
	}

	public void setSurveyId(Integer surveyId) {
		this._surveyId = surveyId;
	}
	public Integer getSurveyId() {
		return _surveyId;
	}

	public void setStrutsAction(Integer strutsAction) {
		this._strutsAction = strutsAction;
	}
	public Integer getStrutsAction() {
		return _strutsAction;
	}

	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}
	public ApsProperties getDescriptions() {
		return _descriptions;
	}

	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}
	public String getGroupName() {
		return _groupName;
	}

	public void setStartDate(Date startDate) {
		this._startDate = startDate;
	}
	public Date getStartDate() {
		return _startDate;
	}

	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}
	public Date getEndDate() {
		return _endDate;
	}

	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	public ApsProperties getTitles() {
		return _titles;
	}

	public void setPublicResult(Integer publicResult) {
		this._publicResult = publicResult;
	}
	public Integer getPublicResult() {
		return _publicResult;
	}

	public void setPublicPartialResult(Integer publicPartialResult) {
		this._publicPartialResult = publicPartialResult;
	}
	public Integer getPublicPartialResult() {
		return _publicPartialResult;
	}

	public void setActive(Integer active) {
		this._active = active;
	}
	public Integer getActive() {
		return _active;
	}
	
	public Boolean getQuestionnaire() {
		return _questionnaire;
	}
	public void setQuestionnaire(Boolean questionnaire) {
		this._questionnaire = questionnaire;
	}

	public Boolean getCheckCookie() {
		return _checkCookie;
	}
	public void setCheckCookie(Boolean checkCookie) {
		this._checkCookie = checkCookie;
	}

	public Boolean getCheckIpAddress() {
		return _checkIpAddress;
	}
	public void setCheckIpAddress(Boolean checkIpAddress) {
		this._checkIpAddress = checkIpAddress;
	}

	public Boolean getCheckUsername() {
		return _checkUsername;
	}
	public void setCheckUsername(Boolean checkUsername) {
		this._checkUsername = checkUsername;
	}
	
	public void setResponseManager(IResponseManager responseManager) {
		this._responseManager = responseManager;
	}
	protected IResponseManager getResponseManager() {
		return _responseManager;
	}

	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	protected IVoterManager getVoterManager() {
		return _voterManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setImageId(String imageId) {
		this._imageId = imageId;
	}
	public String getImageId() {
		return _imageId;
	}

	public void setImageDescriptions(ApsProperties imageDescriptions) {
		this._imageDescriptions = imageDescriptions;
	}
	public ApsProperties getImageDescriptions() {
		return _imageDescriptions;
	}

	public void setGatherUserInfo(Integer gatherUserInfo) {
		this._gatherUserInfo = gatherUserInfo;
	}
	public Integer getGatherUserInfo() {
		return _gatherUserInfo;
	}
	
	public void setSurvey(Survey survey) {
		this._survey = survey;
	}
	public Survey getSurvey() {
		return _survey;
	}

	// management variables
	private Integer _strutsAction;
	private Survey _survey;

	// _survey fields
	private Integer _surveyId;
	private ApsProperties _descriptions = new ApsProperties();
	private String _groupName;
	private Date _startDate;
	private Date _endDate;
	private Integer _active;
	private Integer _publicPartialResult;
	private Integer _publicResult;
	private Boolean _questionnaire; // inherited (and needed) when creating new elements
	private Integer _gatherUserInfo;
	private String _imageId;
	private ApsProperties _imageDescriptions = new ApsProperties();
	private ApsProperties _titles = new ApsProperties();

	public Boolean _checkCookie;
	public Boolean _checkIpAddress;
	private Boolean _checkUsername;

	// managers
	private IResponseManager _responseManager;
	private IVoterManager _voterManager;
	private IGroupManager _groupManager;
}
